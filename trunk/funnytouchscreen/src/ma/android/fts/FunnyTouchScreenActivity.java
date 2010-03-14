package ma.android.fts;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.IBinder;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AbsoluteLayout;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.AbsoluteLayout.LayoutParams;

public class FunnyTouchScreenActivity extends Activity implements OnClickListener {
	
	public static final File MULTIMEDIA = new File("/sdcard/funny-touch-screen");

	public static final int PADDING = 20;
	public static final int MAX_SIZE = 3;
	protected static final long NEXT_LEVEL_DELAY = 10 * 1000;

	private static final int EXIT = 12345678;
	
	private static Timer timer = null;
	private static int timerUsers = 0;

	private int width;
	private int height;
	private int squareNumberX;
	private int squareNumberY;
	private int round;
	private int squareSizeX;
	private int squareSizeY;
	private int level;
	private int game;
	private int selectedButtonNumber;
	private MediaPlayer mp;
	private MusicPlayerService musicPlayerService;
	private FunnyButton[][] screenElements;
	private AbsoluteLayout absLayout;
	private Resources resources;
	private static Random random = new Random();
	private FunnyButton blinkingButton;
	private int blockAnimation = 0;
	private ServiceConnection conn;
	static Point[][] game1Dimension = new Point[4][];
	static Point[][] game2Dimension = new Point[4][];
	private boolean waitingForEndTimer = false;

	static Point[] game1level1 = { new Point(1, 2), new Point(1, 2), new Point(1, 2), new Point(2, 2), new Point(2, 2), new Point(2, 2), new Point(2, 3), new Point(2, 3), new Point(2, 3) };
	static Point[] game1level2 = { new Point(2, 2), new Point(2, 2), new Point(2, 2), new Point(2, 3), new Point(2, 3), new Point(2, 3), new Point(2, 4), new Point(2, 4), new Point(2, 4) };
	static Point[] game1level3 = { new Point(1, 3), new Point(1, 3), new Point(2, 2), new Point(2, 2), new Point(2, 3), new Point(2, 3), new Point(2, 4), new Point(3, 3) };
	static Point[] game1level4 = { new Point(1, 3), new Point(1, 3), new Point(2, 2), new Point(2, 2), new Point(2, 3), new Point(2, 3), new Point(2, 4), new Point(3, 3) };

	static Point[] game2Level12 = { new Point(2, 2), new Point(2, 2), new Point(2, 2), new Point(2, 3), new Point(2, 3), new Point(2, 4), new Point(2, 4) };
	static Point[] game2Level34 = { new Point(2, 3), new Point(2, 3), new Point(2, 3), new Point(2, 4), new Point(2, 4), new Point(3, 4), new Point(3, 4) };

	private static List<File> backgrounds = null;
	
	private static int backgroundCount = 0;

	static {
		game1Dimension[0] = game1level1;
		game1Dimension[1] = game1level2;
		game1Dimension[2] = game1level3;
		game1Dimension[3] = game1level4;
		game2Dimension[0] = game2Level12;
		game2Dimension[1] = game2Level12;
		game2Dimension[2] = game2Level34;
		game2Dimension[3] = game2Level34;
	}

	public boolean initBackgrounds() {
		backgrounds = new ArrayList<File>();
		File[] files = MULTIMEDIA.listFiles();
		if (files == null || files.length == 0) {
			Toast.makeText(this, R.string.missingMultimedia, Toast.LENGTH_LONG).show();
			return false;
		}
		for (File file : files) {
			if (file.getAbsolutePath().endsWith(".jpg")) {
				backgrounds.add(file);
			}
		}
		Collections.shuffle(backgrounds);
		return true;
	}
	
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		menu.add(0, EXIT, 0, R.string.back);
		return true;
	}
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case EXIT: finish();
		}
		return true;
	}

	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode != KeyEvent.KEYCODE_MENU) return true;
		return false;
	}
	
	public boolean onKeyUp(int keyCode, KeyEvent event) {
		if (keyCode != KeyEvent.KEYCODE_MENU) return true;
		return false;
	}
	
	/** Called when the activity is first created. */
	@SuppressWarnings("deprecation")
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (backgrounds == null) {
			if (!initBackgrounds()) {
				finish();
				return;
			}
		}
		FunnyButton.shuffleBackgrounds();
		if (timer == null) {
			timer = new Timer();
		}
		timerUsers++;
		resources = new Resources(this.getAssets(), new DisplayMetrics(), new Configuration());

		Intent iParameters = getIntent();

		round = iParameters.getIntExtra("round", 0);
		level = iParameters.getIntExtra("level", 0);
		game = iParameters.getIntExtra("game", 0);
		Log.i("FTS", "Game: "+game+", level: "+level+", round: "+round);

		Point dimension;

		switch (game) {
			case 1:
				dimension = game1Dimension[level][round];
				squareNumberX = dimension.x;
				squareNumberY = dimension.y;
				break;

			case 2:
				dimension = game2Dimension[level][round];
				squareNumberX = dimension.x;
				squareNumberY = dimension.y;
				break;

		}

		random = new Random();
		selectedButtonNumber = 1;

		absLayout = new AbsoluteLayout(this);
		Bitmap b = BitmapFactory.decodeFile(backgrounds.get(backgroundCount).getAbsolutePath());
		backgroundCount++;
		if (backgroundCount >= backgrounds.size()) {
			initBackgrounds();
			backgroundCount = 0;
		}
		absLayout.setBackgroundDrawable(new BitmapDrawable(b));
		absLayout.setOnClickListener(this);

		WindowManager w = getWindowManager();
		Display d = w.getDefaultDisplay();

		width = d.getWidth();
		height = d.getHeight();// -45;

		screenElements = new FunnyButton[squareNumberX][squareNumberY];

		int leftWidth = width - (squareNumberX + 1) * PADDING;
		squareSizeX = leftWidth / squareNumberX;

		int leftHeight = height - (squareNumberY + 1) * PADDING;
		squareSizeY = leftHeight / squareNumberY;

		mp = new MediaPlayer();
		drawButtons();

		this.setContentView(absLayout);
		boolean firsRun = iParameters.getBooleanExtra("firstRun", false);
		if (firsRun) {
			showLittleHelp(game, level);
		}
	}

	private void showLittleHelp(int game, int level) {
		int msg = 0;
		switch (game*10+level) {
			case 10: msg = R.string.gameHelp11; break;
			case 11: msg = R.string.gameHelp12; break;
			case 12: msg = R.string.gameHelp13; break;
			case 13: msg = R.string.gameHelp14; break;
			case 20: msg = R.string.gameHelp21; break;
			case 21: msg = R.string.gameHelp22; break;
			case 22: msg = R.string.gameHelp23; break;
			case 23: msg = R.string.gameHelp24; break;
		}

		Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
	}

	public void onClick(View clicked) {
		if (waitingForEndTimer) {
			increaseLevel();
			waitingForEndTimer = false;
			return;
		}
		if (blockAnimation == 0 && (clicked instanceof Button)) {
			Button pressed = (Button) clicked;

			FunnyButton pressedParent = (FunnyButton) pressed.getParent();
			if (game == 1) {
				switch (level) {
					case 0:
						pressed.setOnClickListener(null);
						makeButtonDisappear(pressedParent);
						break;

					case 1:
						if (pressedParent.getDotNumer() == 1) {
							pressed.setOnClickListener(null);
							makeButtonDisappear(pressedParent);
						} else {
							playSound(R.raw.button_ko);
						}
						break;

					case 2:
						if (pressedParent.getDotNumer() == selectedButtonNumber) {
							selectedButtonNumber++;
							makeButtonDisappear(pressedParent);
						} else {
							playSound(R.raw.button_ko);
						}
						break;

					case 3:
						if ((Integer.parseInt((String) pressed.getText()) == selectedButtonNumber)) {
							selectedButtonNumber++;
							makeButtonDisappear(pressedParent);
						} else {
							playSound(R.raw.button_ko);
						}
						break;
				}
			} else if (game == 2) {
				if (blinkingButton == null) {
					switch (level) {
						case 0:
							if (pressedParent.getDotNumer() == 1) {
								pressedParent.startBlinking();
								blinkingButton = pressedParent;
							} else {
								playSound(R.raw.button_ko);
							}
							break;

						case 1:
							pressedParent.startBlinking();
							blinkingButton = pressedParent;
							break;

						case 2:
							if (pressedParent.getDotNumer() == selectedButtonNumber) {
								pressedParent.startBlinking();
								blinkingButton = pressedParent;
							} else {
								playSound(R.raw.button_ko);
							}
							break;

						case 3:
							if ((Integer.parseInt((String) pressed.getText()) == selectedButtonNumber)) {
								pressedParent.startBlinking();
								blinkingButton = pressedParent;
								selectedButtonNumber++;
							} else {
								playSound(R.raw.button_ko);
							}
							break;
					}
				} else if (pressed != blinkingButton.getButton()) {
					switch (level) {
						case 0:
							if (pressedParent.getDotNumer() == 1) {
								blinkingButton.getButton().setOnClickListener(null);
								pressed.setOnClickListener(null);
								coupleDisapear(pressedParent);
							} else {
								playSound(R.raw.button_ko);
							}
							break;

						case 1:
							if (pressedParent.getSerial() == blinkingButton.getSerial()) {
								blinkingButton.getButton().setOnClickListener(null);
								pressed.setOnClickListener(null);
								coupleDisapear(pressedParent);
							} else {
								playSound(R.raw.button_ko);
							}
							break;

						case 2:
							if (blinkingButton.getDotNumer() == pressedParent.getDotNumer()) {
								coupleDisapear(pressedParent);
								selectedButtonNumber++;
							} else {
								playSound(R.raw.button_ko);
							}
							break;

						case 3:
							if (pressed.getText().equals(blinkingButton.getButton().getText())) {
								coupleDisapear(pressedParent);
							} else {
								playSound(R.raw.button_ko);
							}
							break;
					}
				}
			}
		}
	}

	public void makeButtonDisappear(FunnyButton pressedParent) {
		blockAnimation++;
		playSound(R.raw.button_ok);
		pressedParent.disappear(disappearAnimationEnded);
	}

	public void increaseLevel() {
		if (isFinishing())
			return;
		finish();
		Intent iParameters = getIntent();
		iParameters.putExtra("firstRun", false);
		if (hasMoreLevels()) {
			iParameters.putExtra("round", round + 1);
			startActivity(iParameters);
		}
	}

	public void coupleDisapear(FunnyButton parent) {
		makeButtonDisappear(parent);
		blinkingButton.setVisibility(View.INVISIBLE);
		blinkingButton.setAnimation(null);
		blinkingButton = null;
	}

	@SuppressWarnings("deprecation")
	public void runFinalAnimation() {
		FinalAnimation fa = AnimationFactory.generateAnimation(height, width, this);
		if (!hasMoreLevels()) {
			playSound(R.raw.aplause);
		}
		fa.getAnimation().setAnimationListener(new Animation.AnimationListener() {
			public void onAnimationEnd(Animation animation) {
				waitingForEndTimer = true;
				timer.schedule(new TimerTask() {
					public void run() {
						increaseLevel();
					}
				}, NEXT_LEVEL_DELAY);
			}

			public void onAnimationRepeat(Animation animation) {
			}

			public void onAnimationStart(Animation animation) {
			}
		});

		int animationImageSizeX = fa.getImage().getDrawable().getMinimumWidth();
		int animationImageSizeY = fa.getImage().getDrawable().getMinimumHeight();

		absLayout.addView(fa.getImage(), new LayoutParams(animationImageSizeX, animationImageSizeY, fa.getStartPosition().x, fa.getStartPosition().y));

		fa.getImage().startAnimation(fa.getAnimation());
		fa.getImage().setVisibility(ImageView.INVISIBLE);
	}

	public boolean checkCompletedLevel(int level) {
		ArrayList<FunnyButton> remainingButtons = null;
		boolean finished = true;
		boolean needPlaceDot = false;
		if ((level == 1 && game == 1) || (level == 0 && game == 2))
			needPlaceDot = true;

		if (needPlaceDot) {
			remainingButtons = new ArrayList<FunnyButton>();
		}
		for (int x = 0; x < squareNumberX; x++) {
			for (int y = 0; y < squareNumberY; y++) {
				if (screenElements[x][y].isShown()) {
					finished = false;
					if (needPlaceDot) {
						remainingButtons.add(screenElements[x][y]);
					} else {
						break;
					}
				}
			}
			if (!finished && !needPlaceDot) {
				break;
			}
		}
		if (!finished && needPlaceDot) {

			FunnyButton selected = remainingButtons.get(random.nextInt(remainingButtons.size()));
			selected.getSerial();
			if (game == 2) {
				selected.placeDot(true);
				remainingButtons.remove(selected);
				for (int i = 0; i < remainingButtons.size(); i++) {
					FunnyButton fb = remainingButtons.get(i);
					if (fb.getSerial() == selected.getSerial()) {
						fb.placeDot(true);
						break;
					}
				}
			} else {
				selected.placeDot(true);
			}
		}
		return finished;
	}

	@SuppressWarnings("deprecation")
	public void drawButtons() {
		ArrayList<Integer> numbers = null;
		ArrayList<Boolean> drawed = null;
		int level34Background = random.nextInt(6);
		int level0Game2ImagePosition = 0;
		int selected;
		int dotNumber;
		int backgroundNumber;
		int image1SquareX = random.nextInt(squareNumberX);
		int image1SquareY = random.nextInt(squareNumberY);
		int image2SquareX = random.nextInt(squareNumberX);
		int image2SquareY = random.nextInt(squareNumberY);
		if (image1SquareX == image2SquareX && image1SquareY == image2SquareY) {
			if (image2SquareX != squareNumberX - 1)
				image2SquareX++;
			else
				image2SquareX--;
		}
		boolean game1Dot = false;
		if ((level == 2 || level == 3) || (level == 0 && game == 2) || (level == 1 && game == 2)) {
			numbers = new ArrayList<Integer>();

			if (game == 2) {
				drawed = new ArrayList<Boolean>();
				for (int i = 0; i < (squareNumberX * squareNumberY) / 2; i++) {
					numbers.add(i + 1);
					drawed.add(false);
				}
			} else {
				for (int i = 0; i < squareNumberX * squareNumberY; i++) {
					numbers.add(i + 1);
				}
			}
			level0Game2ImagePosition = random.nextInt(numbers.size());
		}

		for (int i = 0; i < squareNumberX; i++) {
			for (int n = 0; n < squareNumberY; n++) {
				int posX = PADDING + (squareSizeX + PADDING) * i;
				int posY = PADDING + (squareSizeY + PADDING) * n;

				switch (level) {
					case 0:
						dotNumber = 0;
						if (game == 1) {
							screenElements[i][n] = new FunnyButton(this, dotNumber, false, false);
							screenElements[i][n].setButtonBackground(random.nextInt(5));
						} else if (game == 2) {
							backgroundNumber = random.nextInt(numbers.size());
							if (level0Game2ImagePosition == numbers.get(backgroundNumber) - 1) {
								dotNumber = 1;
								screenElements[i][n] = new FunnyButton(this, dotNumber, true, false);
							} else {
								dotNumber = 0;
								screenElements[i][n] = new FunnyButton(this, dotNumber, false, false);
							}
							screenElements[i][n].setButtonBackground(numbers.get(backgroundNumber) - 1);
							screenElements[i][n].setSerial(numbers.get(backgroundNumber));
							if (!drawed.get(backgroundNumber)) {
								drawed.set(backgroundNumber, true);

							} else {
								numbers.remove(backgroundNumber);
								drawed.remove(backgroundNumber);
							}
							break;
						}
						break;

					case 1:
						switch (game) {
							case 1:
								if (!game1Dot) {
									if (i == image1SquareX && n == image1SquareY) {
										dotNumber = 1;
										screenElements[i][n] = new FunnyButton(this, dotNumber, true, true);
										screenElements[i][n].setButtonBackground(random.nextInt(5));
										game1Dot = true;
									} else {
										dotNumber = 0;
										screenElements[i][n] = new FunnyButton(this, dotNumber, false, true);
										screenElements[i][n].setButtonBackground(random.nextInt(5));
									}
								} else {
									dotNumber = 0;
									screenElements[i][n] = new FunnyButton(this, dotNumber, false, true);
									screenElements[i][n].setButtonBackground(random.nextInt(5));
								}
								break;

							case 2:
								dotNumber = 0;
								backgroundNumber = random.nextInt(numbers.size());
								screenElements[i][n] = new FunnyButton(this, dotNumber, false, false);
								screenElements[i][n].setButtonBackground(numbers.get(backgroundNumber) - 1);
								screenElements[i][n].setSerial(numbers.get(backgroundNumber));
								if (!drawed.get(backgroundNumber)) {
									drawed.set(backgroundNumber, true);

								} else {
									numbers.remove(backgroundNumber);
									drawed.remove(backgroundNumber);
								}
						}
						break;

					case 2:
						selected = random.nextInt(numbers.size());
						dotNumber = numbers.get(selected);
						if (squareNumberX * squareNumberY <= 4)
							screenElements[i][n] = new FunnyButton(this, dotNumber, false, true);
						else
							screenElements[i][n] = new FunnyButton(this, dotNumber, false, false);
						screenElements[i][n].setButtonBackground(level34Background);
						if (game == 2) {
							if (!drawed.get(selected)) {
								drawed.set(selected, true);
							} else {
								numbers.remove(selected);
								drawed.remove(selected);
							}
						} else
							numbers.remove(selected);
						break;

					case 3:
						selected = random.nextInt(numbers.size());
						dotNumber = 0;
						screenElements[i][n] = new FunnyButton(this, dotNumber, false, false);
						screenElements[i][n].setButtonBackground(level34Background);
						screenElements[i][n].getButton().setText(numbers.get(selected).toString());
						screenElements[i][n].getButton().setTextSize(50);
						if (game == 2) {
							if (!drawed.get(selected)) {
								drawed.set(selected, true);
							} else {
								numbers.remove(selected);
								drawed.remove(selected);
							}
						} else
							numbers.remove(selected);
						break;
				}
				screenElements[i][n].getButton().setOnClickListener(this);
				absLayout.addView(screenElements[i][n], new LayoutParams(squareSizeX, squareSizeY, posX, posY));
			}
		}
	}

	private boolean hasMoreLevels() {
		switch (game) {
			case 1:
				return game1Dimension[level].length > round + 1;
			case 2:
				return game2Dimension[level].length > round + 1;
		}
		return false;
	}

	public void playSound(int resource) {
		if (mp != null) {
			mp.reset();
			try {
				mp.setDataSource(resources.openRawResourceFd(resource).getFileDescriptor(), resources.openRawResourceFd(resource).getStartOffset(), resources.openRawResourceFd(resource).getLength());
				mp.prepare();
				mp.start();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public void onStart() {
		if (musicPlayerService == null) {
			conn = new ServiceConnection() {
				public void onServiceConnected(ComponentName name, IBinder service) {
					musicPlayerService = ((MusicPlayerService.MusicPlayerBinder) service).getService();
					musicPlayerService.playMusic();
				}

				public void onServiceDisconnected(ComponentName name) {
					musicPlayerService = null;
				}
			};
			bindService(new Intent(this, MusicPlayerService.class), conn, Context.BIND_AUTO_CREATE);
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

	public void onDestroy() {
		super.onDestroy();
		BitmapDrawable bd = ((BitmapDrawable)absLayout.getBackground());
		if (bd != null) {
			absLayout.setBackgroundDrawable(null);
			if (bd.getBitmap()!=null) {
				bd.getBitmap().recycle();
			}
		}
		unbindService(conn);
		timerUsers--;
		if (timerUsers <= 0) {
			timer.cancel();
			timer = null;
		}
	}

	Runnable disappearAnimationEnded = new Runnable() {
		public void run() {
			blockAnimation--;
			if (checkCompletedLevel(level))
				runFinalAnimation();
		}
	};
}
