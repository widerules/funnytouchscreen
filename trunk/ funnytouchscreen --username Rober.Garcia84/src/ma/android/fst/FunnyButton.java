package ma.android.fst;

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

	int dotNumber;

	static int [] buttonBackground = new int [8];

	static {
		buttonBackground[0] = R.drawable.button1;
		buttonBackground[1] = R.drawable.button2;
		buttonBackground[2] = R.drawable.button1;
		buttonBackground[3] = R.drawable.button2;
		buttonBackground[4] = R.drawable.button1;
		buttonBackground[5] = R.drawable.button2;
		buttonBackground[6] = R.drawable.button1;
		buttonBackground[7] = R.drawable.button2;
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

	public FunnyButton(Context context, int dotNumber) {
		super(context);

		this.dotNumber = dotNumber;
		Random random = new Random();
		button = new Button(context);
		button.setBackgroundResource(buttonBackground[random.nextInt(8)]);
		button.setGravity(Gravity.CENTER);
		button.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.FILL_PARENT));
		addView(button);

		initDots();
	}

	private void initDots() {
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
					dots[x].setImageResource(R.drawable.dot3);
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
	public void placeDot()
	{
		dotNumber = 1;
		initDots();
	}

}