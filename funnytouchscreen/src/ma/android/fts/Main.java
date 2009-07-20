package ma.android.fts;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.view.Display;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.AbsoluteLayout;
import android.widget.Button;
import android.widget.AbsoluteLayout.LayoutParams;


public class Main extends Activity implements OnClickListener{

	public static final int PADDING = 3;
	public static final int PADDING_RIGHT = 20;
	public static final int PADDING_LEFT = 112;
	public static final int PADDING_TOP = 120;
	public static final int PADDING_BOTTOM = 86;
	
	public static final int SQUARENUMBERX = 2;
	public static final int SQUARENUMBERY = 4;
	
	public static final int BOTTOMBUTTONS_PADDING_TOP = 416;
	public static final int BOTTOMBUTTONS_PADDING_SIDE = 20;
	public static final int BOTTOMBUTTONS_PADDING_BOTTOM = 12;
	public static final int BOTTOMBUTTONS = 3;
	
	private int width;
	private int height;
	private boolean musicEnabled;
	private AbsoluteLayout absLayout;
	private FunnyButton[][] menuElements;
	private FunnyButton[] settingElements;
	private int squareSizeXMenu;
	private int squareSizeYMenu;
	
	private int squareSizeXSettings;
	private int squareSizeYSettings;
	
	
	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        musicEnabled = false;
        
        absLayout = new AbsoluteLayout(this);
        absLayout.setBackgroundResource(R.drawable.main_background); 
      
        WindowManager w = getWindowManager();
        Display d = w.getDefaultDisplay();

        width = d.getWidth();
        height = d.getHeight();
        
        menuElements = new FunnyButton[SQUARENUMBERX][SQUARENUMBERY];
        settingElements = new FunnyButton[BOTTOMBUTTONS];
        
        int leftWidth = width - PADDING_LEFT - PADDING_RIGHT - PADDING;
        squareSizeXMenu = leftWidth / SQUARENUMBERX;
        
        int leftHeight = height  - PADDING_TOP - PADDING_BOTTOM - PADDING;
        squareSizeYMenu = leftHeight / SQUARENUMBERY;
        
        int bottomLeftWidth = width - BOTTOMBUTTONS_PADDING_SIDE * 2 - PADDING *2;
        squareSizeXSettings = bottomLeftWidth / 3;
        
        int bottomLeftHeight = height - BOTTOMBUTTONS_PADDING_TOP - BOTTOMBUTTONS_PADDING_BOTTOM;
        squareSizeYSettings = bottomLeftHeight;

        drawButtons();
        
        menuElements[0][0].getButton().setId(R.string.game1Level1);
        menuElements[0][1].getButton().setId(R.string.game1Level2);
        menuElements[0][2].getButton().setId(R.string.game1Level3);
        menuElements[0][3].getButton().setId(R.string.game1Level4);
        menuElements[1][0].getButton().setId(R.string.game2Level1);
        menuElements[1][1].getButton().setId(R.string.game2Level2);
        menuElements[1][2].getButton().setId(R.string.game2Level3);
        menuElements[1][3].getButton().setId(R.string.game2Level4);
        
        settingElements[0].getButton().setId(R.string.aboutButton);
        settingElements[1].getButton().setId(R.string.airplaneMode);
        settingElements[2].getButton().setId(R.string.music);
        
        boolean isEnabledAirplaneMode = Settings.System.getInt(getContentResolver(), 
              Settings.System.AIRPLANE_MODE_ON, 0) == 1;
        
        if (isEnabledAirplaneMode){
        	settingElements[1].getButton().setBackgroundResource(R.drawable.airplane_on);	
        }
        else{
        	settingElements[1].getButton().setBackgroundResource(R.drawable.airplane_off);
        }
        
        settingElements[0].getButton().setBackgroundResource(R.drawable.about);
        settingElements[2].getButton().setBackgroundResource(R.drawable.music_off);
        
        
        this.setContentView(absLayout);
	}
	public void onClick(View v) {
		// TODO Auto-generated method stub
		Button pressed = (Button)v;
		
		switch (pressed.getId())
		{
			case R.string.airplaneMode:	boolean isEnabledAirplaneMode = Settings.System.getInt(getContentResolver(), 
										Settings.System.AIRPLANE_MODE_ON, 0) == 1;
				
										if (isEnabledAirplaneMode){
											Settings.System.putInt(getContentResolver(),
										      		Settings.System.AIRPLANE_MODE_ON,0);
											pressed.setBackgroundResource(R.drawable.airplane_off);
										}
										else{
											Settings.System.putInt(getContentResolver(),
										      		Settings.System.AIRPLANE_MODE_ON,1);
											pressed.setBackgroundResource(R.drawable.airplane_on);
										}
										break;
									
			case R.string.game1Level1: 	launchActivity(1,0);
										break;
			case R.string.game1Level2: 	launchActivity(1,1);
										break;
			case R.string.game1Level3: 	launchActivity(1,3);
										break;
			case R.string.game1Level4: 	launchActivity(1,2);
										break;
			case R.string.game2Level1: 	launchActivity(2,1);
										break;
			case R.string.game2Level2: 	launchActivity(2,0);
										break;
			case R.string.game2Level3: 	launchActivity(2,3);
										break;
			case R.string.game2Level4: 	launchActivity(2,2);
										break;
										
			case R.string.aboutButton:	break;
			
			case R.string.music:		Intent intent = new Intent(this, MusicPlayer.class);
										if (musicEnabled){
											stopService(intent);
											pressed.setBackgroundResource(R.drawable.music_off);
											musicEnabled = false;
										}
										else{
											startService(intent);
											pressed.setBackgroundResource(R.drawable.music_on);
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
	public void drawButtons()
	{	
        for (int i=0;i<SQUARENUMBERX;i++)
        {
        	for (int n=0;n<SQUARENUMBERY;n++)
        	{
        		menuElements[i][n] = new FunnyButton(this,0,false);
        		if (i == 0)
        			menuElements[i][n].getButton().setBackgroundResource(R.drawable.button_11);
        		else
        			menuElements[i][n].getButton().setBackgroundResource(R.drawable.button_12);
				
        		int posX = PADDING_LEFT + 1 +  (squareSizeXMenu + PADDING)*i;
        		int posY = PADDING_TOP + 1 + (squareSizeYMenu + PADDING)*n;	
        		
        		menuElements[i][n].getButton().setOnClickListener(this);
        		absLayout.addView(menuElements[i][n], new LayoutParams(squareSizeXMenu,squareSizeYMenu,posX,posY));
	        }
        }
        for (int x=0;x<BOTTOMBUTTONS;x++)
        {
        	settingElements[x] = new FunnyButton(this,0,false);
        	
        	int posXBottom = BOTTOMBUTTONS_PADDING_SIDE + 1 +  (squareSizeXSettings + PADDING)*x;
    		int posYBottom = BOTTOMBUTTONS_PADDING_TOP + 1;
    		
    		settingElements[x].getButton().setOnClickListener(this);
    		absLayout.addView(settingElements[x], new LayoutParams(squareSizeXSettings,squareSizeYSettings,posXBottom,posYBottom));
        }
	}
}