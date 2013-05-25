package bladequest.combatactions;

import java.util.ArrayList;
import java.util.List;

import bladequest.combat.BattleMenuState;
import bladequest.combat.BattleState;
import bladequest.world.DamageTypes;
import bladequest.world.Global;
import bladequest.world.TargetTypes;

public class combAuras extends CombatAction  {

	public combAuras()
	{
		name = "Auras";
		type = DamageTypes.Magic; //ignored, uses submenu
		targetType = TargetTypes.AllAllies; //ignored, uses submenu
		
		actionText = " has use bugged out, contact the dev team!";
	}
	
	@Override
	public String getDescription() { return "Empower your party with a magical aura.";}
	
	private List<CombatAction> getAuras()
	{
		List<CombatAction> out = new ArrayList<CombatAction>();
		out.add(new combEmpowerLife());
		return out;
	}
	private BattleState getSelectAuraState(CombatActionBuilder actionBuilder)
	{
		return new BattleMenuState(actionBuilder)
		{
			@Override
			public void onSelected(Object obj) {
				CombatAction subAction = (CombatAction)(obj);
				//pass along the builder to the next object down the chain.
				subAction.onSelected(actionBuilder);
			}
			
			@Override
			public void onLongPress(Object obj)
			{
				Global.battle.showMessage(((CombatAction)obj).getDescription());
			}

			@Override
			public void addMenuItems() {
				for(CombatAction action : getAuras())
					mainMenu.addItem(action.getName(), action, false);
			}
		};
	}
	@Override
	public void onSelected(CombatActionBuilder actionBuilder)
	{
		//populate submenu.
		actionBuilder.getStateMachine().setState(getSelectAuraState(actionBuilder));
	}
}
