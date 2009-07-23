package ma.android.fts;

import java.util.ArrayList;
import java.util.Collections;

import android.app.Service;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.IBinder;
import android.util.DisplayMetrics;
import android.util.Log;
import android.widget.Toast;

public class MusicPlayerService extends Service {

	private static final String TAG = "MusicPlayer";
	private MediaPlayer mp;
	private ArrayList<Integer> musics = new ArrayList<Integer>();
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
		musics.add(R.raw.music1);
		musics.add(R.raw.music2);
		musics.add(R.raw.music3);
		musics.add(R.raw.music4);
		resources = new Resources(this.getAssets(), new DisplayMetrics(), new Configuration());
		Collections.shuffle(musics);
		currentSongNumber = 0;
	}

	@Override
	public void onStart(Intent intent, int startId) 
	{
		mp = MediaPlayer.create(this, musics.get(currentSongNumber));
		mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener(){
			public void onCompletion(MediaPlayer mp) 
			{
				currentSongNumber++;
				if (currentSongNumber == musics.size()){
					currentSongNumber = 0;
				}
				mp.reset();
				try {
					AssetFileDescriptor s = resources.openRawResourceFd(musics.get(currentSongNumber));
					mp.setDataSource(s.getFileDescriptor(), s.getStartOffset(), s.getLength());
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
