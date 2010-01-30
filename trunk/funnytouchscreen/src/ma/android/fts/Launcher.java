package ma.android.fts;

import java.net.MalformedURLException;
import java.net.URL;
import java.text.MessageFormat;

import ma.android.util.DownloadError;
import ma.android.util.Downloader;
import ma.android.util.DownloaderCallback;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.DialogInterface.OnCancelListener;
import android.os.Bundle;
import android.os.Handler;
import android.os.PowerManager;
import android.util.Log;
import android.widget.Toast;

public class Launcher extends Activity implements OnCancelListener {

	private static final String TAG = "Launcher";

	private boolean wasError = false;

	private static final String FTS_PREF_FILE = "FtsFile";
	private static final long DOWNLOAD_TIMEOUT = 1000 * 60 * 60 * 24 * 21; // every 21 days

	private ProgressDialog checkingDialog;
	private ProgressDialog downloadingDialog;
	private Handler handler = null;

	private Downloader downloader;

	public void onCreate(Bundle savedInstanceState) {
		Log.i(TAG, "Creating");
		super.onCreate(savedInstanceState);
		handler = new Handler();
	}

	public void onResume() {
		Log.i(TAG, "Starting Launcher");
		super.onResume();
		checkStatus();
	}

	public void onDestroy() {
		super.onDestroy();
		if (wl != null && wl.isHeld()) {
			wl.release();
		}
	}

	private void checkStatus() {
		if (wasError) {
			finish();

		} else if (isFirstRun()) {
			Log.i(TAG, "First run");
			handler.post(new Runnable() {
				public void run() {
					Intent aboutWindow = new Intent(Launcher.this, About.class);
					aboutWindow.putExtra("firstRun", true);
					startActivity(aboutWindow);
				}
			});
			finish();

		} else if (shouldCheckDownload()) {
			if (downloadingDialog != null) return;
			Log.i(TAG, "Should download multimedia");
			downloadPhase1();

		} else {
			startGame();
		}
	}

	private void startGame() {
		Log.i(TAG, "Starting main menu");
		Intent mainWindow = new Intent(this, Main.class);
		startActivity(mainWindow);
		finish();
	}

	private void downloadPhase1() {
		checkingDialog = new ProgressDialog(this);
		checkingDialog.setTitle(R.string.downloadTitle);
		checkingDialog.setMessage(getText(R.string.downloadChecking));
		checkingDialog.show();
		try {
			downloader = new Downloader(new DownloadCallBack(), new URL("http://repository.m-atelier.cz/fts-repository"), FunnyTouchScreenActivity.MULTIMEDIA);
		} catch (MalformedURLException e) {
			throw new IllegalStateException(e);
		}
		downloader.checkFilesToDownload();
		checkingDialog.setOnCancelListener(this);
	}

	private PowerManager.WakeLock wl = null;

	private void downloadPhase2() {
		PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
		wl = pm.newWakeLock(PowerManager.SCREEN_DIM_WAKE_LOCK, TAG);
		wl.acquire();
		downloader.startDownloader();
	}

	private boolean isFirstRun() {
		SharedPreferences preferences = getSharedPreferences(FTS_PREF_FILE, MODE_PRIVATE);
		boolean firstRun = preferences.getBoolean("firstRun", true);
		if (firstRun) {
			SharedPreferences.Editor editor = preferences.edit();
			editor.putBoolean("firstRun", false);
			editor.commit();
		}
		return firstRun;
	}

	private boolean hasMultimediaFiles() {
		return FunnyTouchScreenActivity.MULTIMEDIA.exists()
		&& FunnyTouchScreenActivity.MULTIMEDIA.listFiles() != null
		&& FunnyTouchScreenActivity.MULTIMEDIA.listFiles().length > 5;
	}

	private boolean shouldCheckDownload() {
		SharedPreferences preferences = getSharedPreferences(FTS_PREF_FILE, MODE_PRIVATE);
		long downloaded = preferences.getLong("multimediaDownloaded", 0);
		return System.currentTimeMillis() - downloaded > DOWNLOAD_TIMEOUT || !hasMultimediaFiles();
	}

	public class DownloadCallBack implements DownloaderCallback {

		private long lastMessage = System.currentTimeMillis();
		private static final long MESSAGE_DELAY = 500;

		@Override
		public void filesToDownload(final int files, final long total) {
			Log.i(TAG, "Checking finished, should download "+files + " files, " + total + " B size ...");
			// there are files to download, we should ask user about it
			handler.post(new Runnable() {
				public void run() {
					checkingDialog.dismiss();
					if (isFinishing()) return;
					if (files <= 0) {
						Toast.makeText(Launcher.this, R.string.downloadNothingNew, Toast.LENGTH_LONG).show();
						markDownloadChecked();
						checkStatus();

					} else {
						AlertDialog ask = new AlertDialog.Builder(Launcher.this)
						.setTitle(R.string.downloadTitle)
						.setMessage(MessageFormat.format(getText(R.string.downloadFoundFiles).toString(), (Object)files, (Object)(total/1000)))
						.setPositiveButton(getText(R.string.downloadDownload), new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int whichButton) {
								downloadPhase2();
							}
						}).setNegativeButton(getText(R.string.downloadCancel), new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int whichButton) {
								if (hasMultimediaFiles()) {
									Toast.makeText(Launcher.this, R.string.downloadOkSomeFilesAreThere, Toast.LENGTH_LONG).show();
									markDownloadChecked();
									startGame();
								} else {
									Toast.makeText(Launcher.this, R.string.downloadMustDownload, Toast.LENGTH_LONG).show();
								}
								finish();
							}
						}).create();
						ask.setOnCancelListener(Launcher.this);
						ask.show();
					}
				}
			});
		}

		public void downloadError(DownloadError arg0, Throwable arg1) {
			downloadError(arg0, arg1.getClass()+":"+arg1.getMessage());
		}

		public void downloadError(DownloadError arg0, String arg1) {
			Log.i(TAG, "Error: " + arg0);
			wasError = true;
			String errorMessage = null;
			switch(arg0) {
				case CANNOT_CONNECT: errorMessage = getText(R.string.downloadErrorConnect).toString(); break;
				case CANNOT_WRITE: errorMessage = getText(R.string.downloadErrorWrite).toString(); break;
				case CONNECTION_CRASHED: errorMessage = getText(R.string.downloadErrorConnect).toString(); break;
				case INVALID_REPOSITORY: errorMessage = getText(R.string.downloadErrorConnect).toString(); break;
				case MISSING_FILE: errorMessage = getText(R.string.downloadErrorConnect).toString(); break;
				case NO_SDCARD: errorMessage = getText(R.string.downloadErrorWrite).toString(); break;
				default: errorMessage = getText(R.string.downloadError).toString(); break;
			}
			//if (arg1 != null) errorMessage += ": "+arg1;
			final String finToast = errorMessage;
			handler.post(new Runnable() {
				public void run() {
					new AlertDialog.Builder(Launcher.this).setTitle(R.string.downloadErrorTitle).setMessage(finToast).setNeutralButton("ok", new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int whichButton) {
							if (hasMultimediaFiles()) {
								startGame();
							}
							finish();
						}
					}
					).setOnCancelListener(new OnCancelListener() {
						public void onCancel(DialogInterface dialog) {
							if (hasMultimediaFiles()) {
								startGame();
							}
							finish();
						}
					}).create().show();
				}
			});
		}

		public void downloadFinished() {
			Log.i(TAG, "... finish");
			handler.post(new Runnable() {
				public void run() {
					if (downloadingDialog != null) {
						downloadingDialog.dismiss();
					}
					if (checkingDialog != null) {
						checkingDialog.dismiss();
					}
					if (!wasError) {
						Toast.makeText(Launcher.this, R.string.downloadFinished, Toast.LENGTH_LONG).show();
						markDownloadChecked();
						startGame();
						finish();
					}
				}
			});
		}

		private void markDownloadChecked() {
			SharedPreferences preferences = getSharedPreferences(FTS_PREF_FILE, MODE_PRIVATE);
			SharedPreferences.Editor editor = preferences.edit();
			editor.putLong("multimediaDownloaded", System.currentTimeMillis());
			editor.commit();
		}

		public void downloadStarted(final int files, final long total) {
			Log.i(TAG, "Starting download, " + files + " files, " + total + " B size ...");
			handler.post(new Runnable() {
				public void run() {
					downloadingDialog = new ProgressDialog(Launcher.this);
					downloadingDialog.setTitle(R.string.downloadTitle);
					downloadingDialog.setMessage(MessageFormat.format(getText(R.string.downloadDownloadingFiles).toString(), (Object)files, (Object)(total/1000)));
					downloadingDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
					downloadingDialog.setMax((int) (total/1000));
					downloadingDialog.show();
					downloadingDialog.setOnCancelListener(Launcher.this);
				}
			});
		}

		public void progress(final long current, long total, String file) {
			if (System.currentTimeMillis() - lastMessage < MESSAGE_DELAY) return;
			lastMessage = System.currentTimeMillis();
			Log.i(TAG, "... " + current + "/" + total + " ...");
			handler.post(new Runnable() {
				public void run() {
					downloadingDialog.setProgress((int)(current / 1000));
				}
			});
		}

	}

	@Override
	public void onCancel(DialogInterface dialog) {
		finish();
	}

}