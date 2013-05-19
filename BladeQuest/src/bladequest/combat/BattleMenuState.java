package bladequest.combat;

import bladequest.UI.ListBox;
import bladequest.UI.MenuPanel;
import bladequest.combatactions.CombatActionBuilder;

public abstract class BattleMenuState extends BattleState {
	
	public BattleMenuState(CombatActionBuilder actionBuilder)
	{
		this.actionBuilder = actionBuilder;
		mainMenu = actionBuilder.getMenu();
		mpWindow = actionBuilder.getMPWindow();
	}
	protected CombatActionBuilder actionBuilder;
	protected ListBox mainMenu;
	protected MenuPanel mpWindow;
	
	//Override me to draw a cost of some sort!
	public void drawSelectedObject(Object obj) {}
	public abstract void onSelected(Object obj);
	public abstract void addMenuItems();
	
	@Override
	public void menuUpdate()
	{
		mainMenu.clearObjects();
		addMenuItems();
		mainMenu.update();
	}

	
	@Override
	public void onSwitchedTo(BattleState PrevState)
	{
		menuUpdate();
		mainMenu.open();
	}
	
	@Override
	public void drawPanels()
	{			
		if(mainMenu.getCurrentSelectedEntry() != null)
		{
			drawSelectedObject(mainMenu.getCurrentSelectedEntry().obj);
		}
	}

	@Override
	public void backButtonPressed()
	{
		cancelToPrevState();
	}
	@Override			
	public void touchActionUp(int x, int y)
	{
		switch(mainMenu.touchActionUp(x, y))
		{
		case Selected:
			if(!mainMenu.getSelectedEntry().Disabled())
			{
				onSelected(mainMenu.getSelectedEntry().obj);
			}				
			break;
		case Close:
			cancelToPrevState();
			break;
		default:
			break;
		}
	}
	@Override
	public void touchActionDown(int x, int y)
	{
		mainMenu.touchActionDown(x, y);
	}			
	@Override
	public void touchActionMove(int x, int y)	
	{
		mainMenu.touchActionDown(x, y);
	}
}
