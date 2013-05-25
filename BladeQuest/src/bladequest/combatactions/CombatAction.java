package bladequest.combatactions;
import java.util.List;

import bladequest.combat.BattleEvent;
import bladequest.combat.BattleEventBuilder;
import bladequest.combat.BattleEventBuilderObject;
import bladequest.combat.DamageMarker;
import bladequest.graphics.BattleSprite.faces;
import bladequest.world.Ability;
import bladequest.world.DamageTypes;
import bladequest.world.PlayerCharacter;
import bladequest.world.TargetTypes;

public class CombatAction 
{
	protected String name, actionText;
	protected DamageTypes type;
	protected TargetTypes targetType;
	
	public CombatAction(){}	
	public void execute(List<PlayerCharacter> targets, List<DamageMarker> markers){}
	public void buildEvents(BattleEventBuilder builder){}
	public void onSelected(CombatActionBuilder actionBuilder) 
	{
		useDefaultActionText(actionBuilder);
		useDefaultEventBuilder(actionBuilder);
		switchToTargeting(actionBuilder);
	}

	
	//Generally it's a bad idea to use these directly now, let the internals implement their magic to hook everything up.
	public String getActionText() { return actionText; }
	public DamageTypes getType() { return type; }
	public TargetTypes getTargetType() { return targetType; }
	public String getName() { return name; }
	public String getDescription() { return "";}
	
	//when executed in the battle event, call this -> buildEvents
	protected void useDefaultEventBuilder(CombatActionBuilder actionBuilder)
	{
		actionBuilder.getUser().setEventBuilder(new BattleEventBuilderObject()
		{
			CombatAction action;
			public BattleEventBuilderObject initialize(CombatAction action)
			{
				this.action = action;
				return this;
			}
			@Override
			public void buildEvents(BattleEventBuilder builder) {
				action.buildEvents(builder);
			}
		}.initialize(this));		
	}
    //sets an ability as the output combat event actions to execute.
	protected void setAbilityAsEventBuilder(CombatActionBuilder actionBuilder, Ability ability)
    {
    	//abstract your abstracted abstraction for proper codez
    	actionBuilder.getUser().setEventBuilder(BattleEvent.abilityToBattleEventBuilder(ability));
    }
	//switches over to the targeting mode based on the this current "getTargetType."
	protected void switchToTargeting(CombatActionBuilder actionBuilder)
	{
		switchToSpecificTargeting(actionBuilder, getTargetType());
	}
	//switches over to targeting mode of the specified type.
	protected void switchToSpecificTargeting(CombatActionBuilder actionBuilder, TargetTypes type)
	{
		actionBuilder.getUser().setFace(faces.Ready);
		actionBuilder.getStateMachine().setState(actionBuilder.getBattle().getTargetState(type));		
	}
	//Uses this action text when running the combat action.
	protected void useDefaultActionText(CombatActionBuilder actionBuilder)
	{
		useCustomActionText(actionBuilder, getActionText());
	}
	//Use the arbitrary text when running the custom action.
	protected void useCustomActionText(CombatActionBuilder actionBuilder, String text)
	{
		actionBuilder.getUser().setCombatActionText(text);
	}

}
