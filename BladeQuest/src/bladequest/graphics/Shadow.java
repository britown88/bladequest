package bladequest.graphics;

import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import bladequest.world.Global;

public class Shadow implements Interpolatable {

	int width,depth;
	int elevationAtCenter;
	int elevation;
	int xNudge;
	Point position;
	
	public Shadow(int width, int depth, int elevationAtCenter) {
		elevation = 0;
		this.width = width;
		this.depth = depth;
		this.elevationAtCenter = elevationAtCenter;
		xNudge = 0;
		position = new Point(0,0);
	}

	
	public Shadow(int width, int depth, int elevationAtCenter, int elevation, int xNudge, Point position) {
		this.width = width;
		this.depth = depth;
		this.elevationAtCenter = elevationAtCenter;
		this.elevation = elevation ;		
		this.xNudge = xNudge;
		this.position = position;
	}

	
	public void setElevation(int elevation)
	{
		this.elevation = elevation;
	}
	
	Rect getShadow(int xScreen, int yScreen)
	{
		final float sqrt2Over2 = (float)(Math.sqrt(2)/2);
		
		//---------
		//|       |
		//|   *   |		
	    //|       |
		//---------
	    //
		//    ^
		//    |
		
		int halfWidth = (width)/2;
		int halfDepth = (depth)/2;
		//in theory this is the middle point of the 3D bounding box.
		
		float y = elevationAtCenter;
		float z = ((yScreen-y)/sqrt2Over2);
		float x = xScreen - z*sqrt2Over2;
		
		int left  = (int)((x-halfWidth) + sqrt2Over2*(z));
		int right = (int)((x+halfWidth) + sqrt2Over2*(z));
		
		//we add here, because y is NEGATIVE in the other space (gee whiz!)
		int top = (int)(y+elevationAtCenter+sqrt2Over2*(z-halfDepth));
		int bottom = (int)(y+elevationAtCenter+sqrt2Over2*(z+halfDepth));
		
		return new Rect(left, top, right, bottom);
	}	
	public void nudgeX(int xVal)
	{
		xNudge = xVal;
	}

	public void setPosition(int x, int y)
	{
		position.x = x;
		position.y = y;
	}

	public int getWidth()
	{
		return width;
	}
	public int getDepth()
	{
		return depth;
	}
	public int getElevAtCenter()
	{
		return elevationAtCenter;
	}
	public int getElevation()
	{
		return elevation;
	}
	public int getXNudge()
	{
		return xNudge;
	}
	public Point getPosition()
	{
		return new Point(position);
	}
	public void render()
	{
		final int invisibleAt = 72;
		final float growSize = 2.5f;
		
		float t = elevation / (float)invisibleAt;
		t = (float)Math.sqrt(t); //quadratic falloff...  tweakmeplz
		
		if (t > 1.0f) t = 1.0f;
		Rect r = getShadow(position.x, position.y);
		
		float expandBy = (1.0f * (1.0f-t) + t * growSize);
		//grow the shadow by t
		int width = (int)(r.width() * expandBy)/2;
		int height = (int)(r.height() * expandBy)/2;
		
		int cX = r.centerX();
		int cY = r.centerY();
		
		r = new Rect(cX - width + xNudge, cY - height + elevation,
					 cX + width + xNudge, cY + height + elevation);
		
		Paint paint = new Paint();
		paint.setColor(Color.argb(255, 0,0,0));
		paint.setAlpha((int)(128*(1.0f-t)));
		
		Global.setvpToScreen(r);
		
		Global.renderer.drawElipse(r, paint);
	}

	@Override
	public Interpolatable interpolateAgainst(Interpolatable rhs, float t) {
		Shadow other = (Shadow)rhs;
		
		return new Shadow(
				BattleAnim.linearInterpolation(width, other.width, t),
				BattleAnim.linearInterpolation(depth, other.depth, t),
				BattleAnim.linearInterpolation(elevationAtCenter, other.elevationAtCenter, t),
				BattleAnim.linearInterpolation(elevation, other.elevation, t),
				BattleAnim.linearInterpolation(xNudge, other.xNudge, t),
				BattleAnim.linearInterpolation(position, other.position, t));
	}
}
