package ma.android.fst;


import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;


public class Main extends Activity implements OnClickListener{

	public static final int PADDING = 5;
	public static final int ROWS = 5;
	public static final int COLUMNS = 3;
	public static final int SEPARATOR_SIZE = 5;
	
	private int width;
	private int height;
	
	private TableLayout menu;
	private TableRow [] rows;
	
	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN); 
        
        //setContentView(R.layout.main);
        //Button startButton = (Button) findViewById(R.id.startButton); 
        //startButton.setOnClickListener(this);
        
        /*WindowManager w = getWindowManager();
        Display d = w.getDefaultDisplay();

        width = d.getWidth();
        height = d.getHeight();//-45;
        
        menu = new TableLayout(this);
        menu.setLayoutParams(new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.FILL_PARENT,
                ViewGroup.LayoutParams.FILL_PARENT
            ));
        
        int rowHeight = (height - PADDING * 3) / 5;
        int rowWidth = width;
        
        int buttonSizeX = (rowWidth - SEPARATOR_SIZE *2 - PADDING * 3)/3;
        int buttonSizeY = rowHeight - PADDING * 2 ;
        
        ArrayList<View> rowList = new ArrayList<View>();
        
        rowList.add(createRow(buttonSizeX, buttonSizeY, this));
        rowList.add(createRow(buttonSizeX, buttonSizeY, this));
        rowList.add(createRow(buttonSizeX, buttonSizeY, this));
        rowList.add(createRow(buttonSizeX, buttonSizeY, this));
        rowList.add(createSpacer(width, PADDING,this));
        rowList.add(createFinalRow(buttonSizeX, buttonSizeY, this));
        //rowList.add(createFinalRow(buttonSizeX, buttonSizeY, this));
        
        for (View row : rowList) {
        	
        	row.setLayoutParams(new TableLayout.LayoutParams(
        	          TableLayout.LayoutParams.WRAP_CONTENT,
        	          TableLayout.LayoutParams.WRAP_CONTENT
        	      ));

			menu.addView(row);
		}*/
        this.setContentView(R.layout.main);        
	}
	public void onClick(View v) {
		// TODO Auto-generated method stub
		/*Button pressed = (Button)v;
		switch (pressed.getId())
		{
			case R.id.startButton:launchActivity();
		}*/
	}
	public void launchActivity()
	{
		Intent i = new Intent(this, FunnyScreenTouchActivity.class);
        i.putExtra("squareNumberX",1);
        i.putExtra("squareNumberY", 2);
        i.putExtra("repeats",0);
        i.putExtra("level",3);
    	startActivity(i);
	}
	public static TableRow createRow(int buttonSizeX, int buttonSizeY,Context activity) 
	{
		  TableRow row = new TableRow(activity);
		  
		  LinearLayout l1 = new LinearLayout(activity);
		  l1.setPadding(PADDING, PADDING, PADDING, PADDING);
		  
		  LinearLayout l2 = new LinearLayout(activity);
		  l2.setPadding(PADDING, PADDING, PADDING, PADDING);
		  
		  View emptySpace = new View(activity);
		  emptySpace.setMinimumWidth(buttonSizeX);
		  emptySpace.setMinimumHeight(buttonSizeY);
		  emptySpace.setLayoutParams(new TableRow.LayoutParams(
		          TableRow.LayoutParams.FILL_PARENT,
		          TableRow.LayoutParams.FILL_PARENT
		      ));
		  
		  View spacer = new View(activity);
		  spacer.setBackgroundColor(Color.argb(200, 226, 226, 226));
		  spacer.setMinimumWidth(5);
		  spacer.setMinimumHeight(buttonSizeY);
		  spacer.setLayoutParams(new TableRow.LayoutParams(
		          TableRow.LayoutParams.FILL_PARENT,
		          TableRow.LayoutParams.FILL_PARENT
		      ));
 
		  Button button = new Button(activity);
		  button.setBackgroundResource(R.drawable.button1);
		  button.setMinimumWidth(buttonSizeX);
		  button.setMinimumHeight(buttonSizeY);
		  button.setLayoutParams(new TableRow.LayoutParams(
		          TableRow.LayoutParams.WRAP_CONTENT,
		          TableRow.LayoutParams.WRAP_CONTENT
		      ));
		  l1.addView(button);

		  View spacer2 = new View(activity);
		  spacer2.setBackgroundColor(Color.argb(200, 226, 226, 226));
		  spacer2.setMinimumWidth(5);
		  spacer2.setMinimumHeight(buttonSizeY);
		  spacer2.setLayoutParams(new TableRow.LayoutParams(
		          TableRow.LayoutParams.FILL_PARENT,
		          TableRow.LayoutParams.FILL_PARENT
		      ));
		    
		  Button button2 = new Button(activity);
		  button2.setBackgroundResource(R.drawable.button1);
		  button2.setMinimumWidth(buttonSizeX);
		  button2.setMinimumHeight(buttonSizeY);
		  button2.setLayoutParams(new TableRow.LayoutParams(
		          TableRow.LayoutParams.WRAP_CONTENT,
		          TableRow.LayoutParams.WRAP_CONTENT
		      ));
		  l2.addView(button2);
		  
		  row.addView(emptySpace);
		  row.addView(spacer);
		  row.addView(l1);
		  row.addView(spacer2);
		  row.addView(l2);
		  return row;
	}
	public static TableRow createFinalRow(int buttonSizeX, int buttonSizeY,Context activity) 
	{
		  TableRow row = new TableRow(activity);
		  
		  LinearLayout l1 = new LinearLayout(activity);
		  l1.setPadding(PADDING, PADDING, PADDING, PADDING);
		  
		  LinearLayout l2 = new LinearLayout(activity);
		  l2.setPadding(PADDING, PADDING, PADDING, PADDING);

		  Button button = new Button(activity);
		  button.setBackgroundResource(R.drawable.button1);
		  button.setMinimumWidth(buttonSizeX);
		  button.setMinimumHeight(buttonSizeY);
		  button.setLayoutParams(new TableRow.LayoutParams(
		          TableRow.LayoutParams.WRAP_CONTENT,
		          TableRow.LayoutParams.WRAP_CONTENT
		      ));
		  l1.addView(button);

		  Button button2 = new Button(activity);
		  button2.setBackgroundResource(R.drawable.button1);
		  button2.setMinimumWidth(buttonSizeX);
		  button2.setMinimumHeight(buttonSizeY);
		  button2.setLayoutParams(new TableRow.LayoutParams(
		          TableRow.LayoutParams.WRAP_CONTENT,
		          TableRow.LayoutParams.WRAP_CONTENT
		      ));
		  l2.addView(button2);
		  
		  
		  
		  row.addView(l1);

		  row.addView(l2);
		  return row;
	}
	
	private static View createSpacer(int spacerX, int spacerY, Context activity) 
	{
		ImageView spacer = new ImageView(activity);
		spacer.setBackgroundColor(Color.argb(200, 226, 226, 226));
		spacer.setMaxWidth(spacerX);
		spacer.setMaxHeight(spacerY);
		spacer.setMinimumWidth(spacerX);
		spacer.setMinimumHeight(spacerY);
		
		return spacer;
	}

}