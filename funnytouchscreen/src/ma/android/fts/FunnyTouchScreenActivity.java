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
import android.view.animation.Animation;
import android.widget.AbsoluteLayout;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.AbsoluteLayout.LayoutParams;

public class FunnyTouchScreenActivity extends Activity implements OnClickListener{
    
	public static final int PADDING = 20;
	public static final int MAX_SIZE = 3;
	
	
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
	
	private FunnyButton blinkingButton;
	private int runningAnimation = 0;
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
        
        this.setContentView(absLayout);
        Toast.makeText(this, getText(R.string.startingGame) + " " + game + " " 
        		+ getText(R.string.atDifficulty) + " " + (level+1) , Toast.LENGTH_SHORT).show();
    }
    
	public void onClick(View v) 
	{
		if (runningAnimation == 0)
		{
			Button pressed = (Button)v;
			FunnyButton pressedParent = (FunnyButton) pressed.getParent();
			if (game ==1)
			{
				switch (level)
				{
					case 0: makeButtonDisappear(pressedParent);
							break;
					
					case 1:	if (pressedParent.getDotNumer()==1)
							{	
								makeButtonDisappear(pressedParent);
							}
							else 
							{
								playSound(R.raw.button_ko);
							}
							break;
							
					case 2: if ((Integer.parseInt((String) pressed.getText())==selectedButtonNumber))
							{
								selectedButtonNumber++;
								makeButtonDisappear(pressedParent);
							}
							else 
							{
								playSound(R.raw.button_ko);
							}
							break;
					
					case 3: if (pressedParent.getDotNumer()==selectedButtonNumber)
							{
								selectedButtonNumber++;
								makeButtonDisappear(pressedParent);
							}
							else
							{
								playSound(R.raw.button_ko);
							}
				}
			}
			else if (game == 2)
			{
				if (blinkingButton == null)
				{		
					switch (level)
					{
						case 0: pressedParent.startBlinking();
								blinkingButton = pressedParent;
								break;
								
						case 1:	if (pressedParent.getDotNumer()== 1)
								{
									pressedParent.startBlinking();
									blinkingButton = pressedParent;
								}
								else
								{
									playSound(R.raw.button_ko);
								}
								break;
								
						case 2:	if ((Integer.parseInt((String) pressed.getText())==selectedButtonNumber))
								{
									pressedParent.startBlinking();
									blinkingButton = pressedParent;
									selectedButtonNumber++;
								}
								else
								{
									playSound(R.raw.button_ko);
								}
								break;
								
						case 3: if (pressedParent.getDotNumer()==selectedButtonNumber)
								{
									pressedParent.startBlinking();
									blinkingButton = pressedParent;
								}
								else
								{
									playSound(R.raw.button_ko);
								}
								break;
					}
				}
				else if (pressed != blinkingButton.getButton())
				{
					switch (level)
					{ 
						case 0:	if (pressedParent.getSerial()== blinkingButton.getSerial())
								{
									coupleDisapear(pressedParent);
								}
								else
								{
									playSound(R.raw.button_ko);
								}
								break;
								
						case 1:	if (pressedParent.getDotNumer() == 1)
								{
									coupleDisapear(pressedParent);
								}
								else
								{
									playSound(R.raw.button_ko);
								}
								break;
							
						case 2:	if (pressed.getText().equals(blinkingButton.getButton().getText()))
								{
									coupleDisapear(pressedParent);
								}
								else
								{
									playSound(R.raw.button_ko);
								}
								break;
								
						case 3: if (blinkingButton.getDotNumer()== pressedParent.getDotNumer())
								{
									coupleDisapear(pressedParent);
									selectedButtonNumber++;
								}
								else
								{
									playSound(R.raw.button_ko);
								}
								break;
					}
				}
			}
		}
	}
	public void makeButtonDisappear(FunnyButton pressedParent)
	{
		runningAnimation++;
		new Thread(dissapearSound).start();
		pressedParent.disappear(disappearAnimationEnded);
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
	public void coupleDisapear(FunnyButton parent)
	{
		makeButtonDisappear(parent);
		blinkingButton.setAnimation(null);
		blinkingButton.setVisibility(View.INVISIBLE);
		blinkingButton = null;
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
			selected.getSerial();
			if (game == 2)
			{
				selected.placeDot(true);
				for (int i=0;i<remainingButtons.size();i++) 
				{
					FunnyButton fb = remainingButtons.get(i);
					if (fb.getSerial() == selected.getSerial())
					{
						fb.placeDot(true);
						break;
					}
				}
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
		int level34Background = random.nextInt(6);
		int level1Game2ImagePosition = random.nextInt(6);
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
		boolean game1Dot = false;
		
		
		if ((level == 2 || level == 3) || (level == 0 && game == 2)||(level == 1 && game == 2)){
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
        						screenElements[i][n] = new FunnyButton(this,dotNumber,false,false);
        						screenElements[i][n].setButtonBackground(random.nextInt(5));
        					}
        					else if (game == 2)
	        				{
        						backgroundNumber = random.nextInt(numbers.size()); 
            					screenElements[i][n] = new FunnyButton(this,dotNumber,false,false);
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
        					
        			case 1:	switch(game)
        					{
        						case 1: if (!game1Dot)
        								{
		        							if (i == image1SquareX && n == image1SquareY)
		    		        				{
		        								dotNumber = 1;
		        								screenElements[i][n] = new FunnyButton(this,dotNumber,false,true);
		        								screenElements[i][n].setButtonBackground(random.nextInt(5));
		    			        				game1Dot=true;
		    		        				}
		        							else
		        			        		{
		        			        			dotNumber = 0;
		        			        			screenElements[i][n] = new FunnyButton(this,dotNumber,false,true);
		        			        			screenElements[i][n].setButtonBackground(random.nextInt(5));
		        			        		}
        								}
		        						else
		    			        		{
		    			        			dotNumber = 0;
		    			        			screenElements[i][n] = new FunnyButton(this,dotNumber,false,true);
		    			        			screenElements[i][n].setButtonBackground(random.nextInt(5));
		    			        		}
        								break;
        								
        						case 2: backgroundNumber = random.nextInt(numbers.size()); 
        								if (level1Game2ImagePosition == numbers.get(backgroundNumber)-1)
        								{	
        									dotNumber = 1;
                							screenElements[i][n] = new FunnyButton(this,dotNumber,true,false);
        								}
                						else
                						{
                							dotNumber = 0;
                							screenElements[i][n] = new FunnyButton(this,dotNumber,false,false);
                						}
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
	    		        				break;
        					}
        					
        					break;
    		
        			case 2:	selected = random.nextInt(numbers.size()); 
	        				dotNumber = 0;
        					screenElements[i][n] = new FunnyButton(this,dotNumber,false,false);
        					screenElements[i][n].setButtonBackground(level34Background);
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
        					if (squareNumberX * squareNumberY <= 4)
        						screenElements[i][n] = new FunnyButton(this,dotNumber,false,true);
        					else
        						screenElements[i][n] = new FunnyButton(this,dotNumber,false,false);
        					screenElements[i][n].setButtonBackground(level34Background);
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
	Runnable dissapearSound = new Runnable() 
	{  
		public void run() {  
			try {  
				playSound(R.raw.button_ok);
	        } 
			catch (Exception e) {  
	        	System.out.println(e.getMessage());
	        }  
		}
	};
	Runnable disappearAnimationEnded = new Runnable ()
	{
		public void run() {
			runningAnimation--;
			if (checkCompletedLevel(level))
				runFinalAnimation();
		}
	};

}