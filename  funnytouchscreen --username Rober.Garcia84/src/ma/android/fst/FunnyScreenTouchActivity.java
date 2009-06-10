package ma.android.fst;


import java.util.Random;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.AbsoluteLayout;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.AbsoluteLayout.LayoutParams;

public class FunnyScreenTouchActivity extends Activity implements OnClickListener{
    
	public static final int PADDING = 20;
	public static final int MAX_SIZE = 3;
	public static final int BUTTON_ANIMATION_DURATION = 300;
	
	private int width;
    private int height;
    private int squareNumberX;
    private int squareNumberY;
    private MediaPlayer mp;
    
    private Button[][] buttons;
	private AbsoluteLayout absLayout;
	private static Random random = new Random();
	
	static int [] background = new int[2];
	static int [] buttonBackground = new int [8];
	
	static {
		background[0] = R.drawable.copyleft;
        background[1] = R.drawable.flower;
        
        buttonBackground[0] = R.drawable.button1;
        buttonBackground[1] = R.drawable.button2;
        buttonBackground[2] = R.drawable.button1;
        buttonBackground[3] = R.drawable.button2;
        buttonBackground[4] = R.drawable.button1;
        buttonBackground[5] = R.drawable.button2;
        buttonBackground[6] = R.drawable.button1;
        buttonBackground[7] = R.drawable.button2;
	}

	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        mp = MediaPlayer.create(this, R.raw.btn045);
        
        Intent iParameters = getIntent();
        
        squareNumberX = iParameters.getIntExtra("squareNumberX", 0);
        squareNumberY = iParameters.getIntExtra("squareNumberY", 0);

        absLayout = new AbsoluteLayout(this);
        absLayout.setBackgroundResource(background[random.nextInt(2)]); 
        
        WindowManager w = getWindowManager();
        Display d = w.getDefaultDisplay();
       
        buttons = new Button[squareNumberX][squareNumberY];
        width = d.getWidth();
        height = d.getHeight()-45; 
       
        int leftWidth = width - (squareNumberX +1) * PADDING;
        int squareSizeX = leftWidth / squareNumberX;
        
        int leftHeight = height - (squareNumberY + 1 ) * PADDING;
        int squareSizeY = leftHeight / squareNumberY;
        
        for (int i=0;i<squareNumberX;i++)
        {
        	for (int n=0;n<squareNumberY;n++)
        	{
        		int posX = PADDING + (squareSizeX + PADDING)*i;
        		int posY = PADDING + (squareSizeY + PADDING)*n;
        		
	        	buttons[i][n] = new Button(this);
	        	buttons[i][n].setOnClickListener(this);
	        	//buttons[i][n].setBackgroundResource(buttonBackground[random.nextInt(8)]);  
	        	
	        	ImageButton ib = new ImageButton(this);
	        	ib.setBackgroundResource(R.drawable.button1);
	        	ib.setImageResource(R.drawable.dot2);
	        	
	        	
	        	absLayout.addView(ib,new LayoutParams(squareSizeX,squareSizeY,posX,posY));
	        }
        }
        this.setContentView(absLayout);
    }
    AlphaAnimation dissapear = (AlphaAnimation) AnimationFactory.fadingElement(1.0f,0.0f,BUTTON_ANIMATION_DURATION,0,0);
    
	public void onClick(View v) 
	{
		boolean finished = true;
		Button pressed = (Button)v;
		
		pressed.startAnimation(dissapear);
		pressed.setVisibility(Button.INVISIBLE);
		
		try{
			mp.start();
		}
		catch(Exception e){
			System.out.println(e.getMessage());
		}
		
		finished = checkCompleted(buttons);
		if (finished)
			runFinalAnimation();
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
	public boolean checkCompleted(Button[][] buttons)
	{
		boolean finished = true;
		for (int x =0; x<squareNumberX;x++)
		{
			for (int y=0; y<squareNumberY; y++)
			{
				if (buttons[x][y].isShown()) {
					finished = false;
					break;
				}
			}
			if (!finished){
				break;
			}
		}
		return finished;
	}
}