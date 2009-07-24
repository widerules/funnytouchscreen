package ma.android.fts;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

public class Launcher extends Activity{
	
	private static final String FTS_PREF_FILE ="FtsFile";
	
	public void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		
	}
	public void onStart()
	{
		if (checkFirstRun())
		{
			Intent aboutWindow = new Intent(this, About.class);
			aboutWindow.putExtra("firstRun", true);  
			startActivity(aboutWindow);
			
		}
		else
		{
			Intent mainWindow = new Intent(this, Main.class);
			startActivity(mainWindow);
		}
		finish();
		super.onStart();
	}
	private boolean checkFirstRun()
	{
		SharedPreferences preferences = getSharedPreferences(FTS_PREF_FILE, MODE_PRIVATE);
		boolean firstRun = preferences.getBoolean("firstRun", true);
		if (firstRun)
		{
			SharedPreferences.Editor editor = preferences.edit();
			editor.putBoolean("firstRun", false);
			editor.commit();
			finish();
		}
		return firstRun;
	}
}
