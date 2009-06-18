package ma.android.fst;


import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.content.res.Resources.NotFoundException;
import android.graphics.Point;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.widget.AbsoluteLayout;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.AbsoluteLayout.LayoutParams;

public class FunnyScreenTouchActivity extends Activity implements OnClickListener{
    
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
    private int selectedButtonNumber;
    private MediaPlayer mp;
    private FunnyButton[][] screenElements;
	private AbsoluteLayout absLayout;
	private Resources resources;
	private static Random random = new Random();
	private AlphaAnimation dissapear = (AlphaAnimation) AnimationFactory.fadingElement(1.0f,0.0f,BUTTON_ANIMATION_DURATION,0,0);
	private int dissapearButtonX;
	private int dissapearButtonY;
	
	static int [] background = new int[2];
	
	
	static {
		background[0] = R.drawable.copyleft;
        background[1] = R.drawable.flower; 
	}
	
	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN); 
        
        resources = new Resources(this.getAssets(), new DisplayMetrics(), new
				Configuration());
        
        Intent iParameters = getIntent();
        squareNumberX = iParameters.getIntExtra("squareNumberX", 0);
        squareNumberY = iParameters.getIntExtra("squareNumberY", 0);
        level = iParameters.getIntExtra("level", 0);
        
        random = new Random();
        selectedButtonNumber = 1;
        
        absLayout = new AbsoluteLayout(this);
        absLayout.setBackgroundResource(background[random.nextInt(2)]); 
        
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
					screenElements[dissapearButtonX][dissapearButtonY].setVisibility(View.INVISIBLE);
					playSound(R.raw.btn045);
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
		switch (level)
		{
			case 0: checkPressedButton(parent, level);
					break;
			
			case 1:	
					if (parent.getDotNumer()==0)
					{	
						playSound(R.raw.btn045);
					}
					else 
					{
						checkPressedButton(parent, level);
					}
					break;
					
			case 2: if (!(Integer.parseInt((String) pressed.getText())==selectedButtonNumber))
					{
						playSound(R.raw.btn045);
					}
					else 
					{
						selectedButtonNumber = selectedButtonNumber +1;
						checkPressedButton(parent,level);
					}
					break;
			
			case 3: if (parent.getDotNumer()!=selectedButtonNumber)
					{
						playSound(R.raw.btn045);
					}
					else
					{
						selectedButtonNumber = selectedButtonNumber +1;
						checkPressedButton(parent,level);
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
	public void  checkPressedButton(FunnyButton pressed, int level)
	{
		LayoutParams pressedParams = (LayoutParams) pressed.getLayoutParams();
		boolean found = false;
		for (int i=0;i<squareNumberX;i++)
		{
	        for (int n=0;n<squareNumberY;n++)
	        {
				FunnyButton funnyButton = (FunnyButton) screenElements[i][n];
				LayoutParams buttonParams = (LayoutParams) funnyButton.getLayoutParams();
				if (pressedParams.x == buttonParams.x && pressedParams.y == buttonParams.y){
					screenElements[i][n].startAnimation(dissapear);
					dissapearButtonX=i;
					dissapearButtonY=n;
					found = true;
					break;
				}
	        }
	        if (found){
				break;
			}
		}		
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
			selected.placeDot();
		}
		return finished;
	}
	
	public void drawButtons()
	{
		ArrayList<Integer> numbers = null;
		int selected;
		int dotNumber;
		int dotSquareX = random.nextInt(squareNumberX);
		int dotSquareY = random.nextInt(squareNumberY);
		boolean level1Dot = false;
		
		if (level == 2 || level == 3){
        	numbers = new ArrayList<Integer>();
			for (int i = 0; i <squareNumberX * squareNumberY;i ++)
			{
				numbers.add(i+1);
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
        					screenElements[i][n] = new FunnyButton(this,dotNumber);
        					break;
        					
        			case 1:	if (!level1Dot){
        						if (i == dotSquareX && n == dotSquareY){
        							dotNumber = 1;
        							screenElements[i][n] = new FunnyButton(this,dotNumber);
        							level1Dot=true;
        						}
        						else{
        							dotNumber = 0;
            						screenElements[i][n] = new FunnyButton(this,dotNumber);
        						}
        					}
        					else{
        						dotNumber = 0;
        						screenElements[i][n] = new FunnyButton(this,dotNumber);
        					}
        					break;
    		
        			case 2:	selected = random.nextInt(numbers.size());
	        				dotNumber = 0;
        					screenElements[i][n] = new FunnyButton(this,dotNumber);
	        				screenElements[i][n].getButton().setText(numbers.get(selected).toString());
	        				screenElements[i][n].getButton().setTextSize(50);
	        				numbers.remove(selected);
	        				break;
	        	
        			case 3:	selected = random.nextInt(numbers.size());
        					dotNumber = numbers.get(selected);
        					screenElements[i][n] = new FunnyButton(this,dotNumber);
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