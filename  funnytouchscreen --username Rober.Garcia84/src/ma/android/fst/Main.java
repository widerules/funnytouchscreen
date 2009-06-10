package ma.android.fst;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;


public class Main extends Activity implements OnClickListener{

	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        Button startButton = (Button) findViewById(R.id.startButton); 
        startButton.setOnClickListener(this);
	}
	public void onClick(View v) {
		// TODO Auto-generated method stub
		Button pressed = (Button)v;
		switch (pressed.getId())
		{
			case R.id.startButton:launchActivity();
		}
	}
	public void launchActivity()
	{
		Intent i = new Intent(this, FunnyScreenTouchActivity.class);
        i.putExtra("squareNumberX",1);
        i.putExtra("squareNumberY", 2);
        i.putExtra("repeats",0);
    	startActivity(i);
	}
}
