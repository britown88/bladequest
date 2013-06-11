package bladequest.battleactions;

import android.graphics.Point;
import android.graphics.Rect;
import bladequest.battleactions.bactRunAnimation.WaitType;
import bladequest.combat.BattleEventBuilder;
import bladequest.graphics.BattleAnim;
import bladequest.graphics.BattleAnimObjState;
import bladequest.graphics.BattleAnimObjState.PosTypes;
import bladequest.graphics.BattleAnimObject;
import bladequest.graphics.BattleAnimObject.Types;
import bladequest.graphics.BattleSprite;
import bladequest.graphics.BattleSprite.faces;
import bladequest.graphics.Shadow;
import bladequest.math.PointMath;
import bladequest.world.Global;
import bladequest.world.PlayerCharacter;

public class bactRevive extends DelegatingAction {

	BattleAnim revivePlayer(PlayerCharacter c)
	{
		BattleAnim out = new BattleAnim(1000.0f);
				
		//basic idea = grow some light lines up from the ground.
		//levitate player a bit, growing a shadow.
		
		final int raiseTime = 2000;
		final int lightShafts = 36;
		final int lightTime = 600;
		final int lightGrow = 100;
		final int lightShaftWait = 52;
		
		for (int i = 0; i < lightShafts; ++i)
		{
			int startTime = lightShaftWait * i;
			BattleAnimObject obj = new BattleAnimObject(Types.Line, false, "");
			
			obj.interpolateLinearly();
			
			//start and some random circular offset.
			Point pt = PointMath.randomPointInRadius(24.0f);
			pt.y /= 2; //lol perspective 
			pt = PointMath.add(pt, new Point(0, c.getHeight()/2)); //start at player base
			
			int color = Global.rand.nextInt(128)+64;
			//initial state.
			BattleAnimObjState state = new BattleAnimObjState(startTime, PosTypes.Target);
			state.pos1 = pt;
			state.pos2 = pt;
			state.strokeWidth = 3;
			state.argb(255, 255, 255, color);
			obj.addState(state);
			
			state = new BattleAnimObjState(startTime+ lightGrow, PosTypes.Target);
			state.pos1 = PointMath.add(pt, new Point(0, -96));
			state.pos2 = pt;
			state.strokeWidth = 3;
			state.argb(255, 255, 255, color);
			obj.addState(state);			
			
			int growSize = -((lightTime-lightGrow)*96)/lightGrow;
			
			state = new BattleAnimObjState(startTime+lightTime, PosTypes.Target);
			state.pos1 = PointMath.add(pt, new Point(0, growSize-96));
			state.pos2 = PointMath.add(pt, new Point(0, growSize));
			state.strokeWidth = 3;
			state.argb(255, 255, 255, color);
			obj.addState(state);			
			
			out.addObject(obj);
		}
		
		
		final int elevation = 16;
		
		
		Shadow shadow = c.getShadow();
		BattleSprite sprite = c.getBattleSprite();
		Point shadowBasePt = shadow.getPosition();
		
		
		//animate shadow flying up...
		BattleAnimObject shadowObj = new BattleAnimObject(Types.Interpolatable, false, ""); 
		shadowObj.addState(new BattleAnimObjState(0, PosTypes.Screen, new Shadow(
				shadow.getWidth(), 
				shadow.getDepth(), 
				shadow.getElevAtCenter(), 
				0, 
				shadow.getXNudge(), 
				shadowBasePt
				)));
		
		shadowObj.addState(new BattleAnimObjState(raiseTime, PosTypes.Target, new Shadow(
				shadow.getWidth(), 
				shadow.getDepth(), 
				shadow.getElevAtCenter(), 
				elevation, 
				shadow.getXNudge(), 
				PointMath.add(shadowBasePt, new Point(0, -elevation))
				)));		
		
		out.addObject(shadowObj);
		
		//finally, make the actual player fllllyyyy!!!!
		
		BattleAnimObject playerFloat = new BattleAnimObject(Types.Bitmap, false, sprite.getBmpName()); 
		
        
        Rect sprRect = sprite.getFrameRect(faces.Dead, 0);
		
        Point p = c.getPosition(true);
        
        BattleAnimObjState state = new BattleAnimObjState(0, PosTypes.Screen);
		state.setBmpSrcRect(sprRect.left, sprRect.top, sprRect.right, sprRect.bottom);
		state.argb(255, 255, 255, 255);
		state.pos1 = p;
		state.size = new Point(sprite.getWidth(), sprite.getHeight());
		playerFloat.addState(state);
		
		
		p = new Point(p);
		p.y -= elevation;
		
        state = new BattleAnimObjState(raiseTime, PosTypes.Screen);
		state.setBmpSrcRect(sprRect.left, sprRect.top, sprRect.right, sprRect.bottom);
		state.argb(255, 255, 255, 255);
		state.colorize =1.0f;
		state.pos1 = p;
		state.size = new Point(sprite.getWidth(), sprite.getHeight());
		playerFloat.addState(state);		

		out.addObject(playerFloat);
		
		return out;
	}
	
	public bactRevive() {
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void buildEvents(BattleEventBuilder builder) {
		
		builder.addEventObject(new bactRunAnimation(revivePlayer(BattleAction.getTarget(builder)), WaitType.ReturnInstantly));
		builder.addEventObject(new bactWait(2000));  //make this last exactly 2000ms.
		
		builder.addEventObject(new SourcedAction(BattleAction.getTarget(builder))
		{

			@Override
			protected void buildEvents(BattleEventBuilder builder) {
				builder.addEventObject(new bactChangeVisibility(false));
				
				builder.addEventObject(new bactWait(1700));
				builder.addEventObject(new bactFlash(0.6f, 255, 255, 255, 255).addDependency(builder.getLast()));
				builder.addEventObject(new bactWait(2000));
				builder.addEventObject(new bactChangeVisibility(true).addDependency(builder.getLast()));				
			}
			
		});
		
	}

}
