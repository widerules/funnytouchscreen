package ma.android.fts;


import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.content.res.Resources.NotFoundException;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.widget.AbsoluteLayout;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.AbsoluteLayout.LayoutParams;

public class FunnyTouchScreenActivity extends Activity implements OnClickListener{
    
	public static final int PADDING = 20;
	public static final int MAX_SIZE = 3;
	public static final int BUTTON_ANIMATION_DURATION = 300;
	
	private int width;
    private int height;
    private int squareNumberX;
    private int squareNumberY;
    private int squareSizeX;
    private int squareSizeY;
    private int level;
    private int game;
    private int selectedButtonNumber;
    private MediaPlayer mp;
    private FunnyButton[][] screenElements;
	private AbsoluteLayout absLayout;
	private Resources resources;
	private static Random random = new Random();
	private AlphaAnimation dissapear = (AlphaAnimation) AnimationFactory.fadingOutElement(1.0f,0.0f,BUTTON_ANIMATION_DURATION,0,0);
	private FunnyButton dissapearButton;
	private Button blinkingButton;
	
	static int [] background = new int[27];
	
	
	static {
		background[0] = R.drawable.background1;
		background[1] = R.drawable.background2;
		background[2] = R.drawable.background3;
		background[3] = R.drawable.background4;
		background[4] = R.drawable.background5;
		background[5] = R.drawable.background6;
		background[6] = R.drawable.background7;
		background[7] = R.drawable.background8;
		background[8] = R.drawable.background9;
		background[9] = R.drawable.background10;
		background[10] = R.drawable.background11;
		background[11] = R.drawable.background12;
		background[12] = R.drawable.background13;
		background[13] = R.drawable.background14;
		background[14] = R.drawable.background15;
		background[15] = R.drawable.background16;
		background[16] = R.drawable.background17;
		background[17] = R.drawable.background18;
		background[18] = R.drawable.background19;
		background[19] = R.drawable.background20;
		background[20] = R.drawable.background21;
		background[21] = R.drawable.background22;
		background[22] = R.drawable.background23;
		background[23] = R.drawable.background24;
		background[24] = R.drawable.background25;
		background[25] = R.drawable.background26;
		background[26] = R.drawable.background27;
	}
	
	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);  
        
        resources = new Resources(this.getAssets(), new DisplayMetrics(), new
				Configuration());
        
        Intent iParameters = getIntent();
        squareNumberX = iParameters.getIntExtra("squareNumberX", 0);
        squareNumberY = iParameters.getIntExtra("squareNumberY", 0);
        level = iParameters.getIntExtra("level", 0);
        game = iParameters.getIntExtra("game", 0);
        
        random = new Random();
        selectedButtonNumber = 1;
        
        absLayout = new AbsoluteLayout(this);
        absLayout.setBackgroundResource(background[random.nextInt(27)]); 
        
        WindowManager w = getWindowManager();
        Display d = w.getDefaultDisplay();

        width = d.getWidth();
        height = d.getHeight();//-45; 
        
        screenElements = new FunnyButton[squareNumberX][squareNumberY];
       
        int leftWidth = width - (squareNumberX +1) * PADDING;
        squareSizeX = leftWidth / squareNumberX;
        
        int leftHeight = height - (squareNumberY + 1 ) * PADDING;
        squareSizeY = leftHeight / squareNumberY;
        
        drawButtons();
        
        dissapear.setAnimationListener(new AnimationListener(){

			@Override
			public void onAnimationEnd(Animation animation) {
				// TODO Auto-generated method stub
					dissapearButton.setVisibility(View.INVISIBLE);
					playSound(R.raw.sound_disappear);
					for (int i=0;i<squareNumberX;i++)
			        {
			        	for (int n=0;n<squareNumberY;n++)
			        	{
			        		screenElements[i][n].invalidate();
			        	}
			        }
					if (checkCompletedLevel(level))
						runFinalAnimation();
			}
			@Override
			public void onAnimationRepeat(Animation animation) {
				// TODO Auto-generated method stub	
			}
			@Override
			public void onAnimationStart(Animation animation) {
				// TODO Auto-generated method stub	
			}
			});
        
        this.setContentView(absLayout);
    }
    
	public void onClick(View v) 
	{
		Button pressed = (Button)v;
		FunnyButton parent = (FunnyButton) pressed.getParent();
		if (game ==1)
		{
			switch (level)
			{
				case 0: parent.startAnimation(dissapear);
					dissapearButton=parent;
						break;
				
				case 1:	if (parent.getDotNumer()==1)
						{	
							parent.startAnimation(dissapear);
							dissapearButton=parent;
						}
						else 
						{
							playSound(R.raw.sound_disappear);
						}
						break;
						
				case 2: if ((Integer.parseInt((String) pressed.getText())==selectedButtonNumber))
						{
							
							selectedButtonNumber++;
							parent.startAnimation(dissapear);
							dissapearButton=parent;
						}
						else 
						{
							playSound(R.raw.sound_disappear);
						}
						break;
				
				case 3: if (parent.getDotNumer()==selectedButtonNumber)
						{
							selectedButtonNumber++;
							parent.startAnimation(dissapear);
							dissapearButton=parent;
						}
						else
						{
							playSound(R.raw.sound_disappear);
						}
			}
		}
		else if (game == 2)
		{
			if (blinkingButton == null && pressed != blinkingButton)
			{
				switch (level)
				{
					case 0: checkBlinkingButton( parent, pressed);
							break;
							
					case 1:	if (parent.getDotNumer()== 1)
							{
								checkBlinkingButton(parent, pressed);
							}
							else
							{
								playSound(R.raw.sound_disappear);
							}
							break;
							
					case 2:	if ((Integer.parseInt((String) pressed.getText())==selectedButtonNumber))
							{
								checkBlinkingButton(parent, pressed);
							}
							else
							{
								playSound(R.raw.sound_disappear);
							}
							break;
							
					case 3: if (parent.getDotNumer()==selectedButtonNumber)
							{
								checkBlinkingButton( parent, pressed);
							}
							else
							{
								playSound(R.raw.sound_disappear);
							}
							break;
				}
			}
			else
			{
				FunnyButton blinkingParent = (FunnyButton) blinkingButton.getParent();
				switch (level)
				{ 
					case 0:	if (parent.getSerial()== blinkingParent.getSerial())
							{
								checkCouple(parent);
							}
							else
							{
								playSound(R.raw.sound_disappear);
							}
							break;
							
					case 1:	if (parent.getDotNumer() == 1)
							{
								checkCouple(parent);
							}
							else
							{
								playSound(R.raw.sound_disappear);
							}
							break;
						
					case 2:	if (pressed.getText().equals(blinkingButton.getText()))
							{
								checkCouple(parent);
							}
							else
							{
								playSound(R.raw.sound_disappear);
							}
							break;
							
					case 3: if (blinkingParent.getDotNumer()== parent.getDotNumer())
							{
								checkCouple(parent);
							}
							else
							{
								playSound(R.raw.sound_disappear);
							}
							break;
				}
			}
		}
	}	
	public void increaseLevel()
	{
		Intent iParameters = getIntent();
		if (squareNumberX != MAX_SIZE  ||
			squareNumberX == MAX_SIZE && iParameters .getIntExtra("repeats", 0)<1)
		{	
			if (iParameters .getIntExtra("repeats", 0) == 0)
			{
				iParameters.putExtra("repeats",1);
				startActivity(iParameters);
			}
			else
			{
				if (squareNumberX == squareNumberY)
					iParameters.putExtra("squareNumberY", squareNumberY + 1);
				else
					iParameters.putExtra("squareNumberX", squareNumberX + 1);
				iParameters.putExtra("repeats",0);
				startActivity(iParameters);
			}
		}
	}
	public void checkCouple(FunnyButton parent)
	{
		parent.startAnimation(dissapear);
		dissapearButton=parent;
		blinkingButton.setAnimation(null);
		FunnyButton blinkingParent = (FunnyButton) blinkingButton.getParent();
		if (level == 3 || level == 1){
			ImageView[] dots = parent.getDots();
			for (ImageView dot : dots) {
				if (dot != null){
					dot.setAnimation(null);
				}
				else{
					break;
				}
			}
		}
		blinkingParent.setVisibility(View.INVISIBLE);
		blinkingButton = null;
		

	}
	public void checkBlinkingButton(FunnyButton parent, Button pressed) 
	{
		blinkingButton = pressed;
		pressed.startAnimation(AnimationFactory.blinkingElement(1.0f, 0.0f, 300, Animation.INFINITE, Animation.REVERSE));
		if (level == 3 || level == 1){
			ImageView[] dots = parent.getDots();
			for (ImageView dot : dots) {
				if (dot != null){
					dot.startAnimation(AnimationFactory.blinkingElement(1.0f, 0.0f, 300, Animation.INFINITE, Animation.REVERSE));
				}
				else{
					break;
				}
			}
		}
		selectedButtonNumber++;
	}

	public void runFinalAnimation()
	{
		FinalAnimation fa = AnimationFactory.generateAnimation(height, width,this);
		fa.getAnimation().setAnimationListener(new Animation.AnimationListener(){

			public void onAnimationEnd(Animation animation) {
					// TODO Auto-generated method stub
				finish();
				increaseLevel();
			}

			public void onAnimationRepeat(Animation animation) {
			}

			public void onAnimationStart(Animation animation) {
			}
	     });
		
		int animationImageSizeX = fa.getImage().getDrawable().getMinimumWidth();
		int animationImageSizeY = fa.getImage().getDrawable().getMinimumHeight();
		
		absLayout.addView(fa.getImage(),new LayoutParams(animationImageSizeX,animationImageSizeY,fa.getStartPosition().x, fa.getStartPosition().y));
		
		fa.getImage().startAnimation(fa.getAnimation());
		fa.getImage().setVisibility(ImageView.INVISIBLE);
	}
	public boolean checkCompletedLevel(int level)
	{	
		ArrayList<FunnyButton> remainingButtons = null;
		boolean finished = true;
		
		if (level == 1){
			remainingButtons = new ArrayList<FunnyButton>();
		}
		for (int x =0; x<squareNumberX;x++)
		{
			for (int y=0; y<squareNumberY; y++)
			{
				if (screenElements[x][y].isShown()) {
					finished = false;
					if (level == 1){
						remainingButtons.add(screenElements[x][y]);
					}
					else{
						break;
					}
				}
			}
			if (!finished && level != 1){
					break;
				}
		}
		if (!finished && level == 1){	
			
			FunnyButton selected = remainingButtons.get(random.nextInt(remainingButtons.size()));
			if (game == 2)
			{
				selected.placeDot(true);
				remainingButtons.remove(selected);
				selected = remainingButtons.get(random.nextInt(remainingButtons.size()));
				selected.placeDot(true);
			}
			else {
				selected.placeDot(false);
			}
		}
		return finished;
	}
	
	public void drawButtons()
	{
		ArrayList<Integer> numbers = null;
		ArrayList<Boolean> drawed = null;
		int selected;
		int dotNumber;
		int backgroundNumber;
		int image1SquareX = random.nextInt(squareNumberX);
		int image1SquareY = random.nextInt(squareNumberY);
		int image2SquareX = random.nextInt(squareNumberX);
		int image2SquareY = random.nextInt(squareNumberY);
		if (image1SquareX == image2SquareX && image1SquareY == image2SquareY)
		{
				if (image2SquareX != squareNumberX -1)
					image2SquareX++;
				else
					image2SquareX--;
		}
		boolean level1Dot = false;
		boolean level2Dot = false;
		
		if ((level == 2 || level == 3) || level == 0 && game == 2){
        	numbers = new ArrayList<Integer>();
        	if (game == 2){
        		drawed = new ArrayList<Boolean>();
        		for (int i = 0; i <(squareNumberX * squareNumberY)/2;i ++)
    			{
    				numbers.add(i+1);
    				drawed.add(false);
    			}	
        	}
        	else{
				for (int i = 0; i <squareNumberX * squareNumberY;i ++)
				{
					numbers.add(i+1);
				}
        	}
        }
		
        for (int i=0;i<squareNumberX;i++)
        {
        	for (int n=0;n<squareNumberY;n++)
        	{
        		int posX = PADDING + (squareSizeX + PADDING)*i;
        		int posY = PADDING + (squareSizeY + PADDING)*n;
        		
        		switch (level)
        		{
        			case 0:	dotNumber = 0;
        					if (game == 1)
        					{
        						screenElements[i][n] = new FunnyButton(this,dotNumber,false);
        						screenElements[i][n].setButtonBackground(random.nextInt(5));
        					}
        					else if (game == 2)
	        				{
        						backgroundNumber = random.nextInt(numbers.size()); 
            					screenElements[i][n] = new FunnyButton(this,dotNumber,false);
        						screenElements[i][n].setButtonBackground(numbers.get(backgroundNumber)-1);
        						screenElements[i][n].setSerial(numbers.get(backgroundNumber));
		        				if (!drawed.get(backgroundNumber))
		        				{
		        					drawed.set(backgroundNumber, true);
		        					
		        				}
		        				else
		        				{
		        					numbers.remove(backgroundNumber);
		        					drawed.remove(backgroundNumber);
		        				}
	        				}
        					break;
        					
        			case 1:	if (!level1Dot ||(game == 2 && !level2Dot))
		        			{       			
		        				if (i == image1SquareX && n == image1SquareY)
		        				{
		        					dotNumber = 1;
		        					if (game == 2)
		        						screenElements[i][n] = new FunnyButton(this,dotNumber,true);
		        					else
		        						screenElements[i][n] = new FunnyButton(this,dotNumber,false);
			        				screenElements[i][n].setButtonBackground(random.nextInt(5));
			        				level1Dot=true;
			        			}
			        			else if (i == image2SquareX && n == image2SquareY && game == 2)
			        			{
									dotNumber = 1;
									screenElements[i][n] = new FunnyButton(this,dotNumber,true);
									screenElements[i][n].setButtonBackground(random.nextInt(5));
									level2Dot=true;
								} 
			        			else
			        			{
			        				dotNumber = 0;
			        				screenElements[i][n] = new FunnyButton(this,dotNumber,false);
			            			screenElements[i][n].setButtonBackground(random.nextInt(5));
			        			}
		        			}			
			        		else
			        		{
			        			dotNumber = 0;
			        			screenElements[i][n] = new FunnyButton(this,dotNumber,false);
			        			screenElements[i][n].setButtonBackground(random.nextInt(5));
			        		}
		        			
        					break;
    		
        			case 2:	selected = random.nextInt(numbers.size()); 
	        				dotNumber = 0;
        					screenElements[i][n] = new FunnyButton(this,dotNumber,false);
        					screenElements[i][n].setButtonBackground(random.nextInt(5));
	        				screenElements[i][n].getButton().setText(numbers.get(selected).toString());
	        				screenElements[i][n].getButton().setTextSize(50);
	        				if (game == 2)
	        				{
		        				if (!drawed.get(selected))
		        				{
		        					drawed.set(selected, true);
		        				}
		        				else
		        				{
		        					numbers.remove(selected);
		        					drawed.remove(selected);
		        				}
	        				}
	        				else
	        					numbers.remove(selected);
	        				break;
	        	
        			case 3:	selected = random.nextInt(numbers.size());
        					dotNumber = numbers.get(selected);
        					screenElements[i][n] = new FunnyButton(this,dotNumber,false);
        					screenElements[i][n].setButtonBackground(random.nextInt(5));
        					if (game == 2)
	        				{
		        				if (!drawed.get(selected))
		        				{
		        					drawed.set(selected, true);
		        				}
		        				else
		        				{
		        					numbers.remove(selected);
		        					drawed.remove(selected);
		        				}
	        				}
	        				else
	        					numbers.remove(selected);
	        				break;
	        	}
        		screenElements[i][n].getButton().setOnClickListener(this);
        		absLayout.addView(screenElements[i][n], new LayoutParams(squareSizeX,squareSizeY,posX,posY));
	        }
        }
	}
	public void playSound(int resource)
	{
		if (mp != null)
		{
			mp.reset();
			try {
				mp.setDataSource(resources.openRawResourceFd(resource).getFileDescriptor(),
									resources.openRawResourceFd(resource).getStartOffset(),
									resources.openRawResourceFd(resource).getLength());
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
		else
		{
			mp = MediaPlayer.create(this, resource);
			try{
				mp.start();
			}
			catch(Exception e){
				System.out.println(e.getMessage());
			}
		}
	}
}