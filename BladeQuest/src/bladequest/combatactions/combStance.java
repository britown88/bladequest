package bladequest.combatactions;

import java.util.ArrayList;
import java.util.List;

import bladequest.combat.BattleMenuState;
import bladequest.combat.BattleState;
import bladequest.world.DamageTypes;
import bladequest.world.TargetTypes;


public class combStance extends CombatAction 
{
	public combStance()
	{
		name = "Stance";
		type = DamageTypes.Physical;
		targetType = TargetTypes.Self;
		actionText = " assumes a combat stance.";
		
		stances = new ArrayList<Stance>();
		stances.add(new combBeserkerStance());		
	}
	private List<Stance> stances;
	private BattleState getSelectStanceState(CombatActionBuilder actionBuilder)
	{
		return new BattleMenuState(actionBuilder)
		{
			@Override
			public void onSelected(Object obj) {
				Stance subAction = (Stance)(obj);
				subAction.onSelected(actionBuilder);
			}

			@Override
			public void addMenuItems() {
				for(Stance stance : stances)
					mainMenu.addItem(stance.getName(), stance, stance.isBroken());
			}
		};
	}
	@Override
	public void onSelected(CombatActionBuilder actionBuilder)
	{
		//populate submenu.
		actionBuilder.getStateMachine().setState(getSelectStanceState(actionBuilder));
	}
}
