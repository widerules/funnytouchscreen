package ma.android.fts;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import android.app.Service;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Binder;
import android.os.IBinder;
import android.util.DisplayMetrics;
import android.util.Log;
import android.widget.Toast;

public class MusicPlayerService extends Service {

	private static final String TAG = "MusicPlayer";
	private MediaPlayer mp;
	private List<File> musics = null;
	private Resources resources;
	private int currentSongNumber;
	private final IBinder mBinder = new MusicPlayerBinder();
	private int playingMusic = 0;
	private boolean enabled = false;

	@Override
	public IBinder onBind(Intent intent) {
		return mBinder;
	}
	@Override
	public void onCreate() 
	{
		super.onCreate();
		resources = new Resources(this.getAssets(), new DisplayMetrics(), new Configuration());
		currentSongNumber = 0;
	}

	private boolean initMusic() {
		musics = new ArrayList<File>();
		File[] files = FunnyTouchScreenActivity.MULTIMEDIA.listFiles();
		if (files == null || files.length == 0) {
			Toast.makeText(this, R.string.missingMultimedia, Toast.LENGTH_LONG).show();
			stopSelf();
			return false;
		}
		for (File file : files) {
			if (file.getAbsolutePath().endsWith(".mp3")) {
				musics.add(file);
			}
		}
		if (musics == null || musics.size() == 0) {
			Toast.makeText(this, R.string.missingMusic, Toast.LENGTH_LONG).show();
			stopSelf();
			return false;
		}
		Collections.shuffle(musics);
		return true;
	}

	@Override
	public void onStart(Intent intent, int startId) 
	{
		if (!initMusic()) {
			stopSelf();
			return;
		}
		mp = MediaPlayer.create(this, Uri.fromFile(musics.get(currentSongNumber)));
		mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener(){
			public void onCompletion(MediaPlayer mp) 
			{
				currentSongNumber++;
				if (currentSongNumber == musics.size()){
					currentSongNumber = 0;
				}
				mp.reset();
				try {
					mp.setDataSource(MusicPlayerService.this, Uri.fromFile(musics.get(currentSongNumber)));
					mp.prepare();
					mp.start();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		updatePlayerState();
	}
	public void playMusic()
	{
		playingMusic++;
		updatePlayerState();
	}
	public void stopMusic()
	{
		playingMusic--;
		updatePlayerState();
	}
	public int getServiceState()
	{
		return playingMusic;
	}

	@Override
	public void onDestroy() 
	{
		super.onDestroy();
		if (mp != null) 
		{ 
			mp.stop();  
			mp.release();
		}
		stopSelf();
	}
	public class MusicPlayerBinder extends Binder {
		MusicPlayerService getService() {
			return MusicPlayerService.this;
		}
	}
	/**
	 * @return the enabled
	 */
	public boolean isEnabled() {
		return enabled;
	}
	/**
	 * @param enabled the enabled to set
	 */
	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
		updatePlayerState();
	}

	/**
	 * Starts and stops music player.
	 */
	private void updatePlayerState() {
		if (mp != null) {
			if (enabled && playingMusic > 0) {
				if (!mp.isPlaying()) {
					Log.i(TAG, "Starting music");
					mp.start();
					Toast.makeText(this, getText(R.string.musicOn), Toast.LENGTH_SHORT).show();
				}
			} else {
				if (mp.isPlaying()) {
					Log.i(TAG, "Stopping music");
					mp.stop();
					Toast.makeText(this, getText(R.string.musicOff), Toast.LENGTH_SHORT).show();
				}
			}
		}
	}
}
