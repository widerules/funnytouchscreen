package ma.android.fts;

import java.util.Random;

import android.content.Context;
import android.graphics.Point;
import android.view.Gravity;
import android.widget.AbsoluteLayout;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class FunnyButton extends AbsoluteLayout {

	private ImageView[]dots;
	private Button button;
	private int serial;
	private int dotNumber;

	static int [] buttonBackground = new int [5];

	static {
		buttonBackground[0] = R.drawable.button_1;
		buttonBackground[1] = R.drawable.button_2;
		buttonBackground[2] = R.drawable.button_3;
		buttonBackground[3] = R.drawable.button_4;
		buttonBackground[4] = R.drawable.button_5;
	}

	private static final Point[][] DOTS = new Point[][] {
		{new Point(0,0),null, null, null, null, null, null, null, null }, // one button
		{new Point(35,35),new Point(-35,-35), null, null, null, null, null, null, null }, // two buttons
		{new Point(35,35),new Point(0,0), new Point(-35,-35), null, null, null, null, null, null }, // three buttons
		{new Point(35,35),new Point(35,-35), new Point(-35,-35), new Point(-35,35), null, null, null, null, null }, 
		{new Point(35,35),new Point(35,-35), new Point(-35,-35), new Point(-35,35), new Point(0,0), null, null, null, null }, 
		{new Point(35,35),new Point(35,-0), new Point(35,-35), new Point(-35,35), new Point(-35,0), new Point(-35,-35), null, null, null },
		{new Point(35,35),new Point(35,-0), new Point(35,-35), new Point(-35,35), new Point(-35,0), new Point(-35,-35), new Point(0,0), null, null },
		{new Point(35,35),new Point(35,15), new Point(35,-15), new Point(-35,-35), new Point(-35,35), new Point(-35,15), new Point(-35,-15), new Point(-35,-35), null },
		{new Point(35,35),new Point(35,0), new Point(35,-35), new Point(0,35), new Point(0,0), new Point(0,-35), new Point(-35,35), new Point(-35,0), new Point(-35,-35) },
	};

	public FunnyButton(Context context, int dotNumber, boolean image) {
		super(context);
		this.dotNumber = dotNumber;
		this.serial = 0;
		Random random = new Random();
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

}