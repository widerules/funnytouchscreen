package ma.android.fst;

import java.io.IOException;
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
	int [] musics = new int [3];
	Random random = new Random();
	Resources resources;
	
	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public void onCreate() 
	{
		super.onCreate();
		musics[0]= R.raw.btn045;
		musics[1]= R.raw.btn045;
		musics[2]= R.raw.btn045;
		resources = new Resources(this.getAssets(), new DisplayMetrics(), new
				Configuration());		
	}
	
	@Override
    public void onStart(Intent intent, int startId) 
	{
		try
		{
			mp = MediaPlayer.create(this, musics[random.nextInt(3)]);
			mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener(){
				@Override
				public void onCompletion(MediaPlayer mp) {
					// TODO Auto-generated method stub
						int randomMusic = random.nextInt(3);
						mp.reset();
						try {
							mp.setDataSource(resources.openRawResourceFd(musics[randomMusic]).getFileDescriptor(),
												resources.openRawResourceFd(musics[randomMusic]).getStartOffset(),
												resources.openRawResourceFd(musics[randomMusic]).getLength());
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
