package ma.android.fst;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

import android.app.Service;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.content.res.Resources.NotFoundException;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.util.DisplayMetrics;

public class MusicPlayer extends Service{

	MediaPlayer mp;
	ArrayList<Integer> musics = new ArrayList<Integer>();
	Random random = new Random();
	Resources resources;
	int songNumber;
	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public void onCreate() 
	{
		super.onCreate();
		musics.add(R.raw.music1);
		musics.add(R.raw.music2);
		musics.add(R.raw.music3);
		resources = new Resources(this.getAssets(), new DisplayMetrics(), new
				Configuration());
		Collections.shuffle(musics);
		songNumber = 0;
	}
	
	@Override
    public void onStart(Intent intent, int startId) 
	{
		try
		{
			mp = MediaPlayer.create(this, musics.get(songNumber));
			mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener(){
				@Override
				public void onCompletion(MediaPlayer mp) 
				{
					// TODO Auto-generated method stub
						songNumber++;
						if (songNumber == musics.size()){
							songNumber = 0;
						}
						mp.reset();
						try {
							mp.setDataSource(resources.openRawResourceFd(musics.get(songNumber)).getFileDescriptor(),
												resources.openRawResourceFd(musics.get(songNumber)).getStartOffset(),
												resources.openRawResourceFd(musics.get(songNumber)).getLength());
							mp.prepare();
							mp.start();
						} catch (IllegalArgumentException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (IllegalStateException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (NotFoundException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}	
						
						
				}
			});
			new Thread(r).start();  
		} 
		catch (Exception e) 
		{  
			if (mp != null) 
			{  
				mp.stop();  
			    mp.release();  
			} 
		}  
    }
	
	@Override
	public void onDestroy() 
	{
		  super.onDestroy();
		  mp.stop();  
		  mp.release();
		  stopSelf();
	}
	Runnable r = new Runnable() 
	{  
		public void run() {  
			try {  
				mp.start();
	        } 
			catch (Exception e) {  
	        	System.out.println(e.getMessage());
	        }  
		}
	};  
}
