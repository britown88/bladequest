package bladequest.combatactions;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import android.graphics.Point;
import android.graphics.Rect;
import bladequest.battleactions.bactChangeVisibility;
import bladequest.combat.BattleEvent;
import bladequest.combat.BattleEventBuilder;
import bladequest.combat.BattleEventObject;
import bladequest.combat.DamageMarker;
import bladequest.graphics.BattleAnim;
import bladequest.graphics.BattleAnimObjState;
import bladequest.graphics.BattleAnimObject;
import bladequest.graphics.BattleAnimObjState.PosTypes;
import bladequest.graphics.BattleAnimObject.Types;
import bladequest.graphics.BattleSprite;
import bladequest.graphics.BattleSprite.faces;
import bladequest.world.PlayerCharacter;
import bladequest.world.DamageTypes;
import bladequest.world.Enemy;
import bladequest.world.Global;
import bladequest.world.TargetTypes;


public class combSteal extends CombatAction 
{
	public combSteal()
	{
		name = "Steal";
		type = DamageTypes.Physical;
		targetType = TargetTypes.SingleEnemy;
		actionText = " tries to steal!";
	}
	
	@Override
	public void execute(List<PlayerCharacter> targets, List<DamageMarker> markers)
	{
		Enemy target = (Enemy)targets.get(0);
		
		if(target.hasItems())
		{
			String item = target.getItem(true);
			if(item == null)
			{
				markers.add(new DamageMarker("MISS", target));	
				Global.battle.changeStartBarText("Failed to steal!");
			}
			else
			{
				Global.party.addItem(item);
				
				markers.add(new DamageMarker("STEAL", target));
				Global.battle.changeStartBarText("Stole a "+ Global.items.get(item).getName() +"!");
			}
		}
		else
		{
			markers.add(new DamageMarker("FAIL", target));
			Global.battle.changeStartBarText("Doesn't have anything!");
		}
	}
	
	Point subtract(Point lhs, Point rhs)
	{
	  return new Point(lhs.x - rhs.x, lhs.y - rhs.y);
	}
	Point add(Point lhs, Point rhs)
	{
		  return new Point(lhs.x + rhs.x, lhs.y + rhs.y);
	}
	private float stealOffsetAngle(Point vectorTo, float distance)
	{
	   float xNormalized = -vectorTo.x / distance;
	   if (xNormalized > 1.0f) xNormalized = 1.0f; //floating point stupidity
	   float angle = (float)Math.acos(xNormalized);
	   //check orientation (ghetto det check)
	   if (vectorTo.y > 0)
	   {
		   angle = (float)(Math.PI * 2  - angle);
	   }
	   return angle;
	}
	private Point getRotatedPoint(float x, float y, float angle)
	{
	   //cos, sin, -sin cos
	   float cosAngle = (float)Math.cos(angle);
	   float sinAngle = (float)Math.sin(angle);
	   
	   float outX = cosAngle  * x + sinAngle * y;
	   float outY = -sinAngle * x + cosAngle * y;
	   
	   return new Point((int)outX, (int)outY);
	}
	public BattleAnim getJumpToAnimation(PlayerCharacter source, PlayerCharacter target)
	{
		BattleAnim anim = new BattleAnim(60.0f);
		
		BattleSprite playerSprite =source.getBattleSprite();
		
		BattleAnimObject baObj = new BattleAnimObject(Types.Bitmap, false, playerSprite.getBmpName());
		Rect srcRect = playerSprite.getFrameRect(faces.Use, 0);
		
		Point vectorTo = subtract(target.getPosition(), source.getPosition());
		Point center = new Point(source.getPosition().x + (int)(vectorTo.x * 0.5f), 
								 source.getPosition().y + (int)(vectorTo.y * 0.5f));
		
	    float distance = (float) Math.sqrt((float)(vectorTo.x*vectorTo.x + vectorTo.y*vectorTo.y));
		float height = distance / 6;
		
	    final int steps = 10;
	    final int stepTime = 3;
	    
	    final float stepSize = (float)(Math.PI / steps-1);
	    
	    float offsetAngle = stealOffsetAngle(vectorTo, distance);
	    
	    List<Point> points = new ArrayList<Point>();
	    
	    for (int i = 0; i < steps; ++i)
	    {
	    	float angle = stepSize * i;
	    	float x = (float)Math.cos((double)angle) * (distance/2);
	    	float y = (float)Math.sin((double)angle) * (height/2);
	        Point p = getRotatedPoint(x, y, offsetAngle);
	        points.add(add(p, center));
	    }
	    
	    int step = 0;
	    
	    for (Point p : points)
	    {
	        BattleAnimObjState state = new BattleAnimObjState(stepTime * step++, PosTypes.Screen);
			
			state.setBmpSrcRect(srcRect.left, srcRect.top, srcRect.right, srcRect.bottom);
			state.argb(255, 255, 255, 255);
			state.pos1 = p;
			state.size = new Point(playerSprite.getWidth(), playerSprite.getHeight());
			baObj.addState(state);
	    }
	    
	    Collections.reverse(points);
	    
	    for (Point p : points)
	    {
	        BattleAnimObjState state = new BattleAnimObjState(stepTime * step++, PosTypes.Screen);
			
			state.setBmpSrcRect(srcRect.left, srcRect.top, srcRect.right, srcRect.bottom);
			state.argb(255, 255, 255, 255);
			state.pos1 = p;
			state.size = new Point(playerSprite.getWidth(), playerSprite.getHeight());
			baObj.addState(state);
	    }
	    
		anim.addObject(baObj);
		
		return anim;
	}
	
	@Override
	public void buildEvents(BattleEventBuilder builder)
	{
		int animStartIndex = 3;
		
		List<PlayerCharacter> targets = builder.getTargets();
		
		PlayerCharacter source = builder.getSource();
		PlayerCharacter target = targets.get(0);
		
		BattleAnim jumpAnim = getJumpToAnimation(source, target);
		builder.setAnimation(jumpAnim, animStartIndex);
		
		int anmiationStartFrame =BattleEvent.frameFromActIndex(animStartIndex); 
		int lastFrame =  anmiationStartFrame + jumpAnim.syncToAnimation(1.0f);
		builder.addEventObject(new BattleEventObject(BattleEvent.frameFromActIndex(0), faces.Ready, 0, source));
		builder.addEventObject(new BattleEventObject(BattleEvent.frameFromActIndex(animStartIndex), new bactChangeVisibility(animStartIndex, false), source, targets));
		builder.addEventObject(new BattleEventObject(lastFrame, new bactChangeVisibility(animStartIndex, true), source, targets));
		builder.addEventObject(new BattleEventObject(lastFrame, this, source, targets));
		builder.addEventObject(new BattleEventObject(lastFrame+BattleEvent.frameFromActIndex(10)));
	}

}
