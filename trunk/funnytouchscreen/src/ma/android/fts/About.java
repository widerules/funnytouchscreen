package ma.android.fts;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
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
		browser.loadUrl("file:///android_asset/about.html");
		
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
