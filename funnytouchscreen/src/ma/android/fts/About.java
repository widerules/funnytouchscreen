package ma.android.fts;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.util.Locale;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebView;
import android.widget.Button;

public class About extends Activity{
	
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.about);
	
		WebView browser = (WebView) findViewById(R.id.webkit);
		
		String url = "about.html";
		String testUrl = "about_"+Locale.getDefault()+".html";
		Log.i("About", "Testing URL: "+testUrl);
		try {
			InputStream i = About.class.getResourceAsStream("/assets/"+testUrl);
			if (i != null) {
				i.close();
				url = testUrl;
			}
		} catch (MalformedURLException e) {
			Log.e("About", e.getMessage(), e);
		} catch (IOException e) {
			Log.e("About", e.getMessage(), e);
		}
		
		browser.loadUrl("file:///android_asset/"+url);
		
		Button aboutButton = (Button) findViewById(R.id.aboutButton);
		aboutButton.setOnClickListener(new OnClickListener(){
			public void onClick(View v) {
				Intent intent = getIntent();
				if (intent.getBooleanExtra("firstRun", false))
				{
					Intent launcher = new Intent(getBaseContext(), Launcher.class);
					startActivity(launcher);
				}
				finish();
			}});
	}
}
