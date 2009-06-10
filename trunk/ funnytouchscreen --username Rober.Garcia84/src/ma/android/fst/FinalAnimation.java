package ma.android.fst;

import android.graphics.Point;
import android.view.animation.Animation;
import android.widget.ImageView;

public class FinalAnimation {
	
	private ImageView image;
	private Animation animation;
	private Point startPosition;
	
	public ImageView getImage() {
		return image;
	}
	public void setImage(ImageView image) {
		this.image = image;
	}
	public Animation getAnimation() {
		return animation;
	}
	public void setAnimation(Animation animation) {
		this.animation = animation;
	}
	public Point getStartPosition() {
		return startPosition;
	}
	public void setStartPosition(Point startPosition) {
		this.startPosition = startPosition;
	}
	
	

}
