package ma.android.fts;

import java.util.Random;

import android.content.Context;
import android.graphics.Point;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;

public class AnimationFactory {
	
	final static long ANIMATION_DURATION = 3000;
	static int[] animationImages = new int[3]; 
	private static Random random = new Random();
	
	static {
		animationImages [0] = R.drawable.star;
		animationImages [1] = R.drawable.star;
		animationImages [2] = R.drawable.star;
	}

	public static FinalAnimation generateAnimation(int height, int width, Context context)
	{	
		FinalAnimation fa= new FinalAnimation();
		Point startPosition = new Point();
		
		ImageView animationImage = new ImageView(context);
		
		int selectedImage = random.nextInt(3);
		
		animationImage.setImageResource(animationImages[selectedImage]);
		int imageWidth = animationImage.getDrawable().getMinimumWidth();
		int imageHeight = animationImage.getDrawable().getMinimumHeight();
		
		fa.setImage(animationImage);
		
		AnimationSet set = new AnimationSet(true);

		int movementAnimation = random.nextInt(3);
		int effectAnimation = random.nextInt(2);

		switch (effectAnimation)
		{
			case 0: set.addAnimation(AnimationFactory.rotatingElement(0f, 360f,
		            Animation.RELATIVE_TO_SELF, 0.5f,
		            Animation.RELATIVE_TO_SELF, 0.5f,ANIMATION_DURATION/4,3,Animation.REVERSE));
					break;
			
			case 1: set.addAnimation(AnimationFactory.fadingOutElement(1.0f, 0.0f, ANIMATION_DURATION/4, 3, Animation.REVERSE));
					break;	
		}
		
		switch (movementAnimation)
		{
			case 0: startPosition.x = 0;
					startPosition.y = 0;
					set.addAnimation(AnimationFactory.elementAlongEdge(height,width,imageWidth,imageHeight, ANIMATION_DURATION));
					break;
					
			case 1: startPosition.x = 0;
					startPosition.y = 0;
					set.addAnimation(AnimationFactory.elementAlongSides(height,width,imageWidth,imageHeight, ANIMATION_DURATION));
					break;
					
			case 2: startPosition.x = width/2 - imageWidth/2;
					startPosition.y = height/2 - imageHeight/2;
					set.addAnimation(AnimationFactory.growingElement(1/3,3,1/3,3,imageWidth/2,imageHeight/2,ANIMATION_DURATION, 0, 0));
					break;	
		}

		fa.setAnimation(set);
		fa.setStartPosition(startPosition);
		
		return fa;
	}
	
	public static Animation rotatingElement(float fromDegrees, float toDegrees, int pivotXType, float pivotX, int pivotYType, float pivotY, long duration, int repeatCount, int repeatMode)
	{
		RotateAnimation rotatingElement = new RotateAnimation(fromDegrees, toDegrees, pivotXType, pivotX, pivotYType, pivotY); 
		rotatingElement.setDuration(duration);
		if (repeatCount != 0)
			rotatingElement.setRepeatCount(repeatCount);
		if (repeatMode != 0)
			rotatingElement.setRepeatMode(RotateAnimation.REVERSE);
		
		return rotatingElement;
	}
	public static Animation growingElement(float fromX, float toX, float fromY, float toY,float pivotX, float pivotY, long duration, int repeatCount, int repeatMode)
	{
		ScaleAnimation growingElement = new ScaleAnimation (fromX,toX,fromY,toY,pivotX,pivotY);
		growingElement.setDuration(duration);
		if (repeatCount != 0)
			growingElement.setRepeatCount(repeatCount);
		if (repeatMode != 0)
			growingElement.setRepeatMode(repeatMode);
		
		return growingElement;
	}
	public static Animation fadingOutElement(float fromAlpha, float toAlpha, long duration, int repeatCount, int repeatMode)
	{
		AlphaAnimation fadding = new AlphaAnimation(fromAlpha,toAlpha);
		fadding.setDuration(duration);
		if (repeatCount != 0)
			fadding.setRepeatCount(repeatCount);
		if (repeatMode != 0 )
			fadding.setRepeatMode(repeatMode);
		return fadding;
	}
	
	public static Animation translation(float fromX, float toX, float fromY, float toY, long duration, int repeatCount, int repeatMode)
	{
		TranslateAnimation translation = new TranslateAnimation(fromX,toX,fromY,toY);
		translation.setDuration(duration);
		if (repeatCount != 0)
			translation.setRepeatCount(repeatCount);
		if (repeatMode != 0)
			translation.setRepeatMode(repeatMode);
		return translation;
	}
	public static AnimationSet elementAlongEdge(int height, int width, int imageSizeX, int imageSizeY, long duration)
	{
		AnimationSet starAlongEdge = new AnimationSet(true);
		
		TranslateAnimation downMovement =  (TranslateAnimation) (AnimationFactory.translation(0, 0, 0, height - imageSizeY, duration / 4, 0, 0));
		
		TranslateAnimation rightMovement = (TranslateAnimation) (AnimationFactory.translation(0, width - imageSizeX , 0, 0, duration/4, 0, 0));
		rightMovement.setStartOffset(duration/4 * 1);
		
		TranslateAnimation upMovement = (TranslateAnimation) (AnimationFactory.translation(0, 0 ,0, -(height - imageSizeY), duration/4, 0, 0));
		upMovement.setStartOffset(duration/4 * 2);
		
		TranslateAnimation leftMovement = (TranslateAnimation) (AnimationFactory.translation(0, -(width - imageSizeX) , 0, 0, duration/4, 0, 0));
		leftMovement.setStartOffset(duration/4 * 3);
		
		starAlongEdge.addAnimation(downMovement);
		starAlongEdge.addAnimation(rightMovement);
		starAlongEdge.addAnimation(upMovement);
		starAlongEdge.addAnimation(leftMovement);
		
		return starAlongEdge;
	}
	public static AnimationSet elementAlongSides(int height, int width, int imageSizeX, int imageSizeY, long duration)
	{
		AnimationSet starAlongSides = new AnimationSet(true);
		
		TranslateAnimation downMovement1 =  (TranslateAnimation) (AnimationFactory.translation(0, 0, 0, height - imageSizeY, duration/4, 0, 0));
		
		TranslateAnimation upRight = (TranslateAnimation) (AnimationFactory.translation(0, width - imageSizeX ,0 , -(height - imageSizeY), duration/4, 0, 0));
		upRight.setStartOffset(duration/4 * 1);
		
		TranslateAnimation downMovement2 = (TranslateAnimation) (AnimationFactory.translation(0, 0, 0, height - imageSizeY, duration/4, 0, 0));
		downMovement2.setStartOffset(duration/4 * 2);
		
		TranslateAnimation upLeft = (TranslateAnimation) (AnimationFactory.translation( 0,-(width - imageSizeX), 0 , -(height - imageSizeY), duration/4, 0, 0));
		upLeft.setStartOffset(duration/4 * 3);	
	
		starAlongSides.addAnimation(downMovement1);
		starAlongSides.addAnimation(upRight);
		starAlongSides.addAnimation(downMovement2);
		starAlongSides.addAnimation(upLeft);
		
		return starAlongSides;
	}
	public static Animation blinkingElement(float fromAlpha, float toAlpha, long duration, int repeatCount, int repeatMode)
	{
		AlphaAnimation fadding = new AlphaAnimation(fromAlpha,toAlpha);
		fadding.setDuration(duration);
		if (repeatCount != 0)
			fadding.setRepeatCount(repeatCount);
		if (repeatMode != 0 )
			fadding.setRepeatMode(repeatMode);
		return fadding;
	}
}