package ma.android.fts;


import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.view.Display;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.Button;


public class Main extends Activity implements OnClickListener{

	public static final int ROW_PADDING = 5;
	public static final int LAYOUR_PADDING = 5;
	public static final int SEPARATOR_SIZE = 5;
	
	private int width;
	private int height;
	private boolean musicEnabled;
	private ArrayList<Button> buttons = new ArrayList<Button>();
	
	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN); 
        
        setContentView(R.layout.main);
        
        musicEnabled = false;
        
        Button airplaneMode = (Button) findViewById(R.id.airplaneMode); 
        buttons.add(airplaneMode);
        Button g1L1 = (Button)findViewById(R.id.game1Level1); 
        buttons.add(g1L1);
        Button g1L2 = (Button)findViewById(R.id.game1Level2);
        buttons.add(g1L2);
        Button g1L3 = (Button)findViewById(R.id.game1Level3);
        buttons.add(g1L3);
        Button g1L4 = (Button)findViewById(R.id.game1Level4);
        buttons.add(g1L4);
        Button g2L1 = (Button)findViewById(R.id.game2Level1);
        buttons.add(g2L1);
        Button g2L2 = (Button)findViewById(R.id.game2Level2);
        buttons.add(g2L2);
        Button g2L3 = (Button)findViewById(R.id.game2Level3);
        buttons.add(g2L3);
        Button g2L4 = (Button)findViewById(R.id.game2Level4);
        buttons.add(g2L4);
        Button about = (Button)findViewById(R.id.about);
        buttons.add(about);
        Button music = (Button)findViewById(R.id.music);
        buttons.add(music);
        
        airplaneMode.setOnClickListener(this);
        g1L1.setOnClickListener(this);
        g1L2.setOnClickListener(this);
        g1L3.setOnClickListener(this);
        g1L4.setOnClickListener(this);
        g2L1.setOnClickListener(this);
        g2L2.setOnClickListener(this);
        g2L3.setOnClickListener(this);
        g2L4.setOnClickListener(this);
        about.setOnClickListener(this);
        music.setOnClickListener(this);
       
        boolean isEnabledAirplaneMode = Settings.System.getInt(getContentResolver(), 
              Settings.System.AIRPLANE_MODE_ON, 0) == 1;
        
        if (isEnabledAirplaneMode){
        	airplaneMode.setText(R.string.off);
        }
        else{
        	airplaneMode.setText(R.string.on);
        }
        
        music.setText(R.string.on);
        
        WindowManager w = getWindowManager();
        Display d = w.getDefaultDisplay();

        width = d.getWidth();
        height = d.getHeight();
        
        int textHeight = 20;
        int leftWidth = width - (ROW_PADDING * 2 + LAYOUR_PADDING * 2 * 3);
        int leftHeight = height - (ROW_PADDING * 2 * 5 + LAYOUR_PADDING * 2 * 5 + textHeight);
        
        int buttonSizeX = leftWidth / 3;
        int buttonSizeY = leftHeight / 5;
        
        for (Button button : buttons) {
        	button.setMinimumWidth(buttonSizeX);
			button.setMinHeight(buttonSizeY);
        }
	}
	public void onClick(View v) {
		// TODO Auto-generated method stub
		Button pressed = (Button)v;
		
		switch (pressed.getId())
		{
			case R.id.airplaneMode:	boolean isEnabledAirplaneMode = Settings.System.getInt(getContentResolver(), 
									Settings.System.AIRPLANE_MODE_ON, 0) == 1;
			
									if (isEnabledAirplaneMode){
										Settings.System.putInt(getContentResolver(),
									      		Settings.System.AIRPLANE_MODE_ON,0);
										pressed.setText(R.string.on);
									}
									else{
										Settings.System.putInt(getContentResolver(),
									      		Settings.System.AIRPLANE_MODE_ON,1);
										pressed.setText(R.string.off);
									}
									break;
									
			case R.id.game1Level1: 	launchActivity(1,0);
									break;
			case R.id.game1Level2: 	launchActivity(1,1);
									break;
			case R.id.game1Level3: 	launchActivity(1,3);
									break;
			case R.id.game1Level4: 	launchActivity(1,2);
									break;
			case R.id.game2Level1: 	launchActivity(2,1);
									break;
			case R.id.game2Level2: 	launchActivity(2,0);
									break;
			case R.id.game2Level3: 	launchActivity(2,3);
									break;
			case R.id.game2Level4: 	launchActivity(2,2);
									break;
			case R.id.about:		break;
			
			case R.id.music:		Intent intent = new Intent(this, MusicPlayer.class);
									if (musicEnabled){
										stopService(intent);
										pressed.setText(R.string.on);
										musicEnabled = false;
									}
									else{
										startService(intent);
										pressed.setText(R.string.off);
										musicEnabled = true;
									}
									break;
		}
	}
	public void launchActivity(int game, int level)
	{
		Intent intent = new Intent(this, FunnyTouchScreenActivity.class);
		if (game == 1)
		{	intent.putExtra("squareNumberX",1);
			intent.putExtra("squareNumberY", 2);
			intent.putExtra("repeats",0);
		}
		else
		{
			intent.putExtra("squareNumberX",3);
			intent.putExtra("squareNumberY", 4);
			intent.putExtra("repeats",0);
		}
			intent.putExtra("level",level);
			intent.putExtra("game", game);
			startActivity(intent);
	}
}