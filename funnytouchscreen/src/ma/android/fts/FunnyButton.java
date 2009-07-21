package ma.android.fts;

import android.content.Context;
import android.graphics.Point;
import android.view.Gravity;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.widget.AbsoluteLayout;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class FunnyButton extends AbsoluteLayout{

	public static final int BUTTON_ANIMATION_DURATION = 300;

	private ImageView[]dots;
	private Button button;
	private int serial;
	private int dotNumber;
	private AlphaAnimation dissapear = (AlphaAnimation) AnimationFactory.fadingOutElement(1.0f,0.0f,BUTTON_ANIMATION_DURATION,0,0);
	private AlphaAnimation blink = (AlphaAnimation) AnimationFactory.blinkingElement(1.0f, 0.0f, 300, Animation.INFINITE, Animation.REVERSE);


	static int [] buttonBackground = new int [6];

	static {
		buttonBackground[0] = R.drawable.button_1;
		buttonBackground[1] = R.drawable.button_2;
		buttonBackground[2] = R.drawable.button_3;
		buttonBackground[3] = R.drawable.button_4;
		buttonBackground[4] = R.drawable.button_5;
		buttonBackground[5] = R.drawable.button_12;
	}

	private static final Point[][] DOTS = new Point[][] {
		{new Point(0,0),null, null, null, null, null, null, null, null }, // one button
		{new Point(32,32),new Point(-32,-32), null, null, null, null, null, null, null }, // two buttons  
		{new Point(32,32),new Point(0,0), new Point(-32,-32), null, null, null, null, null, null }, // three buttons
		{new Point(32,32),new Point(32,-32), new Point(-32,-32), new Point(-32,32), null, null, null, null, null }, 
		{new Point(32,32),new Point(32,-32), new Point(-32,-32), new Point(-32,32), new Point(0,0), null, null, null, null }, 
		{new Point(32,32),new Point(32,-0), new Point(32,-32), new Point(-32,32), new Point(-32,0), new Point(-32,-32), null, null, null },
		{new Point(32,32),new Point(32,-0), new Point(32,-32), new Point(-32,32), new Point(-32,0), new Point(-32,-32), new Point(0,0), null, null },
		{new Point(32,32),new Point(32,12), new Point(32,-12), new Point(-32,-32), new Point(-32,32), new Point(-32,12), new Point(-32,-12), new Point(-32,-32), null },
		{new Point(32,32),new Point(32,0), new Point(32,-32), new Point(0,32), new Point(0,0), new Point(0,-32), new Point(-32,32), new Point(-32,0), new Point(-32,-32) },
	};

	public FunnyButton(Context context, int dotNumber, boolean image) {
		super(context);
		this.dotNumber = dotNumber;
		this.serial = 0;
		button = new Button(context);
		button.setGravity(Gravity.CENTER);
		button.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.FILL_PARENT));
		addView(button);
		
		initDots(image);
	}

	private void initDots(boolean image) {
		if (dots != null) {
			for (ImageView dot : dots) {
				if (dot != null) removeView(dot);
			}
		}
		dots = new ImageView[9];
		if (dotNumber-1 != -1)
		{
			for (int x=0;x<9;x++)
			{
				if (DOTS[dotNumber-1][x]!= null){
					dots[x]= new ImageView(getContext());
					if (image)
						dots[x].setImageResource(R.drawable.cat);
					else
						dots[x].setImageResource(R.drawable.dot);
					addView(dots[x]);
				}
				else{
					break;
				}
			}
		}
	}

	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		// TODO Auto-generated method stub
		button.layout(0, 0, getWidth(),getHeight());
		if (dotNumber -1 != -1)
		{
			for (int i = 0;i<9;i++)
			{
				Point dotPosition = DOTS[dotNumber-1][i];
				if (dots[i]!=null){
					Point squareCenter = new Point(getWidth()/2,getHeight()/2);
					int dotSizeX = dots[i].getDrawable().getMinimumWidth();
					int dotSizeY = dots[i].getDrawable().getMinimumHeight();
					int startPositionX = squareCenter.x + dotPosition.x - dotSizeX/2;
					int startPositionY = squareCenter.y + dotPosition.y - dotSizeY/2;
					dots[i].layout(startPositionX, startPositionY, startPositionX + dotSizeX, startPositionY + dotSizeY);
				}
				else{ 
					break;
				}
			}
		}
	}

	public Button getButton()
	{
		return button;
	}
	public ImageView[] getDots()
	{
		return dots;
	}
	public int getDotNumer()
	{
		return dotNumber;
	}
	public void placeDot(boolean image)
	{
		dotNumber = 1;
		initDots(image);
	}
	public void setButtonBackground(int background)
	{
		button.setBackgroundResource(buttonBackground[background]);
	}
	public void setButtonBackgroundByResource(int background)
	{
		button.setBackgroundResource(background);
	}
	public void setSerial(int s)
	{
		serial = s;
	}
	public int getSerial() {
		return serial;
	}
	public  void startBlinking()
	{
		startAnimation(blink);
	}
	public void stopBlinking()
	{
		setAnimation(null);
	}

	public void disappear(final Runnable runnable) {
		if (runnable != null) {
			dissapear.setAnimationListener(new AnimationListener() {
				@Override
				public void onAnimationEnd(Animation animation) {
					setVisibility(View.INVISIBLE);
					runnable.run();
				}
				public void onAnimationRepeat(Animation animation) {
				}
				public void onAnimationStart(Animation animation) {
				}
			});
		}
		startAnimation(dissapear);
	}
}