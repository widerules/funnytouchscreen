package ma.android.fts;

import android.app.Activity;
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

			@Override
			public void onClick(View v) {
				finish();
				
			}});
	}
}
