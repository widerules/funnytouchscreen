package ma.android.fts;

import java.net.MalformedURLException;
import java.net.URL;

import ma.android.util.DownloadError;
import ma.android.util.Downloader;
import ma.android.util.DownloaderCallback;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

public class Launcher extends Activity {

	private static final String TAG = "Launcher";

	private ProgressDialog pd = null;

	private boolean wasError = false;

	public class DownloadCallBack implements DownloaderCallback {

		private long lastMessage = System.currentTimeMillis();
		private static final long MESSAGE_DELAY = 500;

		public void downloadError(DownloadError arg0, Throwable arg1) {
			Log.i(TAG, "Error: "+arg0);
			wasError = true;
		}

		public void downloadError(DownloadError arg0, String arg1) {
			Log.i(TAG, "Error: "+arg0);
			wasError = true;
		}

		public void downloadFinished() {
			Log.i(TAG, "... finish");
			Message msg = new Message();
			msg.getData().putBoolean("close", true);
			handler.sendMessage(msg);
			if (!wasError) {
				SharedPreferences preferences = getSharedPreferences(FTS_PREF_FILE, MODE_PRIVATE);
				SharedPreferences.Editor editor = preferences.edit();
				editor.putLong("multimediaDownloaded", System.currentTimeMillis());
				editor.commit();
			}
		}

		public void downloadStarted(int files, long total) {
			Log.i(TAG, "Starting download, "+files+" files, "+total+" B size ...");
			Message msg = new Message();
			msg.getData().putString("message", "Downloading "+files+" files ("+(total/1000)+" kB)");
			msg.getData().putInt("max", (int) (total / 1000));
			handler.sendMessage(msg);
		}

		public void progress(long current, long total, String file) {
			if (System.currentTimeMillis() - lastMessage < MESSAGE_DELAY) return;
			lastMessage = System.currentTimeMillis();
			Log.i(TAG, "... "+current+"/"+total+" ...");
			Message msg = new Message();
			msg.getData().putInt("progress", (int) (current / 1000));
			handler.sendMessage(msg);
		}
	}

	private static final String FTS_PREF_FILE = "FtsFile";
	private static final long DOWNLOAD_TIMEOUT = 1000 * 60 * 60 * 24 * 10;

	private Handler handler = null;

	public void onCreate(Bundle savedInstanceState) {
		Log.i(TAG, "Creating");
		super.onCreate(savedInstanceState);
		handler = new Handler() {
			public void handleMessage(Message msg) {
				Log.i(TAG, "Message "+msg);
				if (pd == null) {
					Log.i(TAG, "Creating dialog");
					pd = new ProgressDialog(Launcher.this);
					pd.setTitle("dsdsdsdsds");
					pd.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
				}
				String message = msg.getData().getString("message");
				int max = msg.getData().getInt("max");
				int progress = msg.getData().getInt("progress");
				boolean close = msg.getData().getBoolean("close");

				if (message != null) {
					Log.i(TAG, "Message: "+message);
					pd.setMessage(message);
				}
				if (close) {
					pd.dismiss();
					checkStatus();

				} else {
					if (!pd.isShowing()) {
						pd.show();
					}
					if (max != 0) {
						pd.setMax(max);
					}
					if (progress != 0) {
						pd.setProgress(progress);
					}
				}
			}
		};
	}

	public void onResume() {
		Log.i(TAG, "Starting Launcher");
		checkStatus();
		super.onResume();
	}

	private void checkStatus() {
		if (wasError) {
			finish();

		} else if (isFirstRun()) {
			Log.i(TAG, "First run");
			Intent aboutWindow = new Intent(this, About.class);
			aboutWindow.putExtra("firstRun", true);
			startActivity(aboutWindow);

		} else if (pd == null && shouldDownloadContent()) {
			// pd == null is just for safety - to disable repeating downloads
			Log.i(TAG, "Should download multimedia");
			AlertDialog ask = new AlertDialog.Builder(Launcher.this)
			.setTitle("dsdsdssds")
			.setMessage("dsdsdsdsdsdsds dsdsdsd szds")
			.setPositiveButton("dsdsdss", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int whichButton) {
					download();
				}
			})
			.setNegativeButton("dsds", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int whichButton) {
					finish();
				}
			})
			.create();
			ask.show();

		} else {
			Log.i(TAG, "Starting main menu");
			Intent mainWindow = new Intent(this, Main.class);
			startActivity(mainWindow);
			finish();
		}
	}

	protected void download() {
		try {
			Downloader.startDownloader(new DownloadCallBack(), new URL("http://repository.m-atelier.cz/fts-repository"), FunnyTouchScreenActivity.MULTIMEDIA);
		} catch (MalformedURLException e) {
			throw new IllegalStateException(e);
		}
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

	private boolean shouldDownloadContent() {
		SharedPreferences preferences = getSharedPreferences(FTS_PREF_FILE, MODE_PRIVATE);
		long downloaded = preferences.getLong("multimediaDownloaded", 0);
		return System.currentTimeMillis() - downloaded > DOWNLOAD_TIMEOUT
		|| !FunnyTouchScreenActivity.MULTIMEDIA.exists()
		|| FunnyTouchScreenActivity.MULTIMEDIA.listFiles() == null
		|| FunnyTouchScreenActivity.MULTIMEDIA.listFiles().length < 5;
	}

}