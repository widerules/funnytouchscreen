package ma.android.fts;


import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.Settings;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.AbsoluteLayout;
import android.widget.Button;
import android.widget.Toast;
import android.widget.AbsoluteLayout.LayoutParams;


public class Main extends Activity implements OnClickListener{

	public static final int PADDING = 3;
	public static final int PADDING_RIGHT = 20;
	public static final int PADDING_LEFT = 112;
	public static final int PADDING_TOP = 120;
	public static final int PADDING_BOTTOM = 86;

	public static final int MENU_COLS = 2;
	public static final int MENU_ROWS = 4;

	public static final int BOTTOMBUTTONS_PADDING_TOP = 416;
	public static final int BOTTOMBUTTONS_PADDING_SIDE = 20;
	public static final int BOTTOMBUTTONS_PADDING_BOTTOM = 12;
	public static final int BOTTOMBUTTONS = 3;
	

	private int width;
	private int height;
	private AbsoluteLayout absLayout;
	private FunnyButton[][] menuElements;
	private FunnyButton[] settingElements;
	private int squareSizeXMenu;
	private int squareSizeYMenu;
	private MusicPlayerService musicPlayerService;
	private int squareSizeXSettings;
	private int squareSizeYSettings;
	private Intent ftsIntent;
	private ServiceConnection conn;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		startService(new Intent(this, MusicPlayerService.class));
		absLayout = new AbsoluteLayout(this);
		absLayout.setBackgroundResource(R.drawable.main_background);

		WindowManager w = getWindowManager();
		Display d = w.getDefaultDisplay();

		width = d.getWidth();
		height = d.getHeight();

		menuElements = new FunnyButton[MENU_COLS][MENU_ROWS];
		settingElements = new FunnyButton[BOTTOMBUTTONS];

		int leftWidth = width - PADDING_LEFT - PADDING_RIGHT - PADDING;
		squareSizeXMenu = leftWidth / MENU_COLS;

		int leftHeight = height  - PADDING_TOP - PADDING_BOTTOM - PADDING;
		squareSizeYMenu = leftHeight / MENU_ROWS;

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

		boolean isEnabled = Settings.System.getInt(getContentResolver(), Settings.System.AIRPLANE_MODE_ON, 0) == 1;

		if (isEnabled){
			settingElements[1].getButton().setBackgroundResource(R.drawable.airplane_on);	
		}
		else{
			settingElements[1].getButton().setBackgroundResource(R.drawable.airplane_off);
		}

		settingElements[0].getButton().setBackgroundResource(R.drawable.about);
		settingElements[2].getButton().setBackgroundResource(R.drawable.music_off);

		this.setContentView(absLayout);
		drawCorrectMusicButton();
		
	}
	public void onClick(View v) {
		// TODO Auto-generated method stub
		Button pressed = (Button)v;

		switch (pressed.getId())
		{
			case R.string.game1Level1: 	launchActivity(1,0);
			break;
			case R.string.game1Level2: 	launchActivity(1,1);
			break;
			case R.string.game1Level3: 	launchActivity(1,2);
			break;
			case R.string.game1Level4: 	launchActivity(1,3);
			break;
			case R.string.game2Level1: 	launchActivity(2,0);
			break;
			case R.string.game2Level2: 	launchActivity(2,1);
			break;
			case R.string.game2Level3: 	launchActivity(2,2);
			break;
			case R.string.game2Level4: 	launchActivity(2,3);
			break;
			case R.string.aboutButton:	Intent aboutWindow = new Intent (this,About.class);
										startActivity(aboutWindow);
										break;
			case R.string.airplaneMode:	checkAirplaneMode();
			break;
			case R.string.music:		musicPlayerService.setEnabled(!musicPlayerService.isEnabled()); drawCorrectMusicButton();
			break;
		}
	}
	public void launchActivity(int game, int level)
	{
		ftsIntent = new Intent(this, FunnyTouchScreenActivity.class);
		ftsIntent.putExtra("round",0);
		ftsIntent.putExtra("level",level);
		ftsIntent.putExtra("game", game);
		ftsIntent.putExtra("firstRun", true);
		startActivity(ftsIntent);
	}
	public void drawButtons()
	{	
		for (int i=0;i<MENU_COLS;i++)
		{
			for (int n=0;n<MENU_ROWS;n++)
			{
				menuElements[i][n] = new FunnyButton(this,0,false,false);
				if (i == 0)
					menuElements[i][n].getButton().setBackgroundResource(R.drawable.button_12);
				else
					menuElements[i][n].getButton().setBackgroundResource(R.drawable.button_11);

				int posX = PADDING_LEFT + 1 +  (squareSizeXMenu + PADDING)*i;
				int posY = PADDING_TOP + 1 + (squareSizeYMenu + PADDING)*n;	

				menuElements[i][n].getButton().setOnClickListener(this);
				absLayout.addView(menuElements[i][n], new LayoutParams(squareSizeXMenu,squareSizeYMenu,posX,posY));
			}
		}
		for (int x=0;x<BOTTOMBUTTONS;x++)
		{
			settingElements[x] = new FunnyButton(this,0,false,false);

			int posXBottom = BOTTOMBUTTONS_PADDING_SIDE + 1 +  (squareSizeXSettings + PADDING)*x;
			int posYBottom = BOTTOMBUTTONS_PADDING_TOP + 1;

			settingElements[x].getButton().setOnClickListener(this);
			absLayout.addView(settingElements[x], new LayoutParams(squareSizeXSettings,squareSizeYSettings,posXBottom,posYBottom));
		}
	}
	@Override
	public void onStart()
	{
		if (musicPlayerService == null) {
			conn = new ServiceConnection(){
				public void onServiceConnected(ComponentName name, IBinder service) {
					musicPlayerService = ( (MusicPlayerService.MusicPlayerBinder ) service).getService( );
					musicPlayerService.playMusic();
					drawCorrectMusicButton();
				}
				public void onServiceDisconnected(ComponentName name) {
					musicPlayerService = null;
				}
			};
			bindService(new Intent(this, MusicPlayerService.class),conn,Context.BIND_AUTO_CREATE);
		} else {
			musicPlayerService.playMusic();
		}
		super.onStart();
	}

	@Override
	public void onStop() {
		musicPlayerService.stopMusic();
		super.onStop();
	}
	@Override
	public void onDestroy() {
		super.onDestroy();
		unbindService(conn);
		stopService(new Intent(this, MusicPlayerService.class));
	}

	private void drawCorrectMusicButton() {
		if (musicPlayerService != null && musicPlayerService.isEnabled()){
			settingElements[2].getButton().setBackgroundResource(R.drawable.music_on);
		} else {
			settingElements[2].getButton().setBackgroundResource(R.drawable.music_off);
		}		
	}
	
	private void checkAirplaneMode()
	{
		boolean isEnabled = Settings.System.getInt(getContentResolver(), Settings.System.AIRPLANE_MODE_ON, 0) == 1;
		Button airplaneMode = (Button) findViewById(R.string.airplaneMode);
		if (isEnabled){
			Settings.System.putInt(getContentResolver(), Settings.System.AIRPLANE_MODE_ON,0);
			airplaneMode.setBackgroundResource(R.drawable.airplane_off);
			Toast.makeText(this, getText(R.string.airplaneModeOff), Toast.LENGTH_SHORT).show();
		}
		else{
			Settings.System.putInt(getContentResolver(), Settings.System.AIRPLANE_MODE_ON,1);
			airplaneMode.setBackgroundResource(R.drawable.airplane_on);
			Toast.makeText(this, getText(R.string.airplaneModeOn), Toast.LENGTH_SHORT).show();
		}
		Intent intent = new Intent(Intent.ACTION_AIRPLANE_MODE_CHANGED);
		intent.putExtra("state", !isEnabled);
		sendBroadcast(intent);
	}

}