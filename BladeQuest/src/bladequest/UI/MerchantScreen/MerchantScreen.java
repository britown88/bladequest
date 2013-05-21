package bladequest.UI.MerchantScreen;

import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import bladequest.UI.ListBox;
import bladequest.UI.ListBoxEntry;
import bladequest.UI.MenuPanel;
import bladequest.UI.MsgBox;
import bladequest.UI.ListBox.LBStates;
import bladequest.UI.MainMenu.MainMenuState;
import bladequest.UI.MenuPanel.Anchors;
import bladequest.world.Global;
import bladequest.world.Item;
import bladequest.world.Merchant;
import bladequest.world.States;

public class MerchantScreen 
{
	private Paint menuText, menuTextRight, menuTextCenter, grayText, 
	grayTextCenter, grayTextRight/*, redMenuText, blueMenuText, redMenuTextCenter, 
	blueMenuTextCenter, smallText, smallTextCenter, smallTextRight*/;
	
	private final float menuWidthVpPercent = 28.0f; 
	private final float menuHeightVpPercent = 16.0f;
	
	private final int menuWidth, barHeight;
	
	private MsgBox msgBox;
	private Merchant merchant;
	//private Item item;
	private float discount;
	
	private ListBox rootMenu, items;
	private MenuPanel goldPanel, buySellPanel;
	
	private boolean closing, closed, selling;
	
	private final int darkenAlphaMax = 200, darkenInc = 5;
	private int darkenAlpha;
	private boolean darkening;
	
	MerchantScreenStateMachine stateMachine;
	
	public MerchantScreen()
	{
		msgBox = new MsgBox();
		msgBox.alwaysSpeed0 = true;
		
		menuWidth = (int)((float)Global.vpWidth * (menuWidthVpPercent/100.0f));
		barHeight = (int)((float)Global.vpHeight * (menuHeightVpPercent/100.0f));
		
		stateMachine = new MerchantScreenStateMachine();
		
		buildPaints();
		buildPanels();	
		closed = true;
		
	}
	
	private MerchantScreenState getRootState()
	{
		return new MerchantScreenState(){
			public void onSwitchedTo(MerchantScreenState prevState) {
				if(items.Opened())
					items.close();
				
				msgBox.addMessage(merchant.greeting);
				msgBox.nextMessage();
			}
			public void update() {
				rootMenu.update();
				goldPanel.update();
				
				if(!items.Closed())
					items.update();
			}
			public void render() {
				if(!rootMenu.Closed())
					rootMenu.render();
				goldPanel.render();
				if(!items.Closed())
					items.render();
			}			
			public void handleClosing(){
				if(rootMenu.Opened())
					rootMenu.close();
				if(msgBox.Opened())
					msgBox.close();
				
				if(rootMenu.Closed() && msgBox.Closed())
				{
					Global.showMessage(merchant.farewell, false);
					Global.GameState = States.GS_WORLDMOVEMENT;
					Global.delay();
					closing = false;
					closed = true;
				}
			}	
			public void backButtonPressed() {
				close();
			}
			public void touchActionUp(int x, int y) {
				LBStates state = rootMenu.touchActionUp(x, y);
				if(state == LBStates.Selected)
					handleOption((String)rootMenu.getSelectedEntry().obj);
			}
			public void touchActionMove(int x, int y) {
				rootMenu.touchActionMove(x, y);
			}
			public void touchActionDown(int x, int y) {
				rootMenu.touchActionDown(x, y);
			}			
		};
	}
	private MerchantScreenState getBuyingState()
	{
		return new MerchantScreenState(){
			public void onSwitchedTo(MerchantScreenState prevState) {
				buildBuyingList();
				
				items.setClosed();
				items.open();
				
				selling = false;
				
				msgBox.addMessage(merchant.buying);
				msgBox.nextMessage();
			}

			public void update() {
				rootMenu.update();
				items.update();
				if(!buySellPanel.Closed())
					buySellPanel.update();
			}
			public void render() {
				rootMenu.render();
				items.render();
				goldPanel.render();
				
				if(!buySellPanel.Closed())
				{
					renderDark();
					buySellPanel.render();
				}
			}
			
			public void handleClosing(){
				stateMachine.setState(getRootState());
			}	

			public void backButtonPressed() {
				stateMachine.setState(getRootState());
			}
			public void touchActionUp(int x, int y) {
				LBStates state = rootMenu.touchActionUp(x, y);
				if(state == LBStates.Selected)
					handleOption((String)rootMenu.getSelectedEntry().obj);
				
				state = items.touchActionUp(x, y);
				if(state == LBStates.Selected)
				{
					//item = (Item)(items.getSelectedEntry().obj);
					stateMachine.setState(getBuySellConfirmState());
				}
			}
			public void touchActionMove(int x, int y) {
				rootMenu.touchActionMove(x, y);
				items.touchActionMove(x, y);
			}
			public void touchActionDown(int x, int y) {
				rootMenu.touchActionDown(x, y);
				items.touchActionDown(x, y);
			}			
		};
	}
	private MerchantScreenState getSellingState()
	{
		return new MerchantScreenState(){
			public void onSwitchedTo(MerchantScreenState prevState) {
				buildSellingList();
				
				items.setClosed();
				items.open();
				
				selling = true;
				
				msgBox.addMessage(merchant.selling);
				msgBox.nextMessage();
			}

			public void update() {
				rootMenu.update();
				items.update();
				if(!buySellPanel.Closed())
					buySellPanel.update();
			}
			public void render() {
				rootMenu.render();
				items.render();
				goldPanel.render();
				
				if(!buySellPanel.Closed())
				{
					renderDark();
					buySellPanel.render();
				}
			}
			
			public void handleClosing(){
				stateMachine.setState(getRootState());
			}	

			public void backButtonPressed() {
				stateMachine.setState(getRootState());
			}
			public void touchActionUp(int x, int y) {
				LBStates state = rootMenu.touchActionUp(x, y);
				if(state == LBStates.Selected)
					handleOption((String)rootMenu.getSelectedEntry().obj);
				
				state = items.touchActionUp(x, y);
				if(state == LBStates.Selected)
				{
					//item = (Item)(items.getSelectedEntry().obj);
					stateMachine.setState(getBuySellConfirmState());
				}
			}
			public void touchActionMove(int x, int y) {
				rootMenu.touchActionMove(x, y);
				items.touchActionMove(x, y);
			}
			public void touchActionDown(int x, int y) {
				rootMenu.touchActionDown(x, y);
				items.touchActionDown(x, y);
			}			
		};
	}
	private MerchantScreenState getBuySellConfirmState()
	{
		return new MerchantScreenState(){
			public void changeStateTo(MerchantScreenState state) {
				buySellPanel.close();
				undarken();
			}
			public void onSwitchedTo(MerchantScreenState prevState) {
				updateBuySellPanel();
				buySellPanel.open();
				darken();
			}

			public void update() {
				buySellPanel.update();
			}
			public void render() {
				rootMenu.render();
				items.render();
				goldPanel.render();
				renderDark();
				buySellPanel.render();	
			}
			
			public void handleClosing(){
				stateMachine.setState(selling ? getSellingState() : getBuyingState());
			}	

			public void backButtonPressed() {
				stateMachine.setState(selling ? getSellingState() : getBuyingState());
			}
			public void touchActionUp(int x, int y) {}
			public void touchActionMove(int x, int y) {}
			public void touchActionDown(int x, int y) {}			
		};
	}
	private MerchantScreenState getEquipSelectState()
	{
		return new MerchantScreenState(){
			public void changeStateTo(MerchantScreenState state) {}
			public void onSwitchedTo(MerchantScreenState prevState) {}

			public void update() {}
			public void render() {}
			
			public void handleClosing(){
				stateMachine.setState(getRootState());
			}	

			public void backButtonPressed() {}
			public void touchActionUp(int x, int y) {}
			public void touchActionMove(int x, int y) {}
			public void touchActionDown(int x, int y) {}			
		};
	}

	private void buildPaints()
	{
		/*smallText = Global.textFactory.getTextPaint(9, Color.WHITE, Align.LEFT);				
		smallTextCenter = Global.textFactory.getTextPaint(9, Color.WHITE, Align.CENTER);		
		smallTextRight = Global.textFactory.getTextPaint(9, Color.WHITE, Align.RIGHT);	
		*/
		menuText = Global.textFactory.getTextPaint(13, Color.WHITE, Align.LEFT);	
		menuTextCenter = Global.textFactory.getTextPaint(13, Color.WHITE, Align.CENTER);	
		menuTextRight = Global.textFactory.getTextPaint(13, Color.WHITE, Align.RIGHT);
		
		grayText = Global.textFactory.getTextPaint(13, Color.GRAY, Align.LEFT);
		grayTextCenter = Global.textFactory.getTextPaint(13, Color.GRAY, Align.CENTER);		
		grayTextRight = Global.textFactory.getTextPaint(13, Color.GRAY, Align.RIGHT);	
		
		/*redMenuText = Global.textFactory.getTextPaint(13, Color.RED, Align.LEFT);
		blueMenuText = Global.textFactory.getTextPaint(13, Color.CYAN, Align.LEFT);
		redMenuTextCenter = Global.textFactory.getTextPaint(13, Color.RED, Align.CENTER);
		blueMenuTextCenter = Global.textFactory.getTextPaint(13, Color.CYAN, Align.CENTER);
	*/}
	private void buildPanels()
	{
		goldPanel = new MenuPanel(0, Global.vpHeight - MsgBox.msgBoxHeight, menuWidth, barHeight);
		goldPanel.anchor = Anchors.BottomLeft;
		goldPanel.addTextBox("", menuWidth/2, barHeight/2, menuTextCenter);
		
		rootMenu = new ListBox(0, 0, menuWidth, 0, 3, 1, menuTextCenter);
		rootMenu.setDisabledPaint(grayTextCenter);
		rootMenu.openSpeed = 30;
		rootMenu.setOpenSize(menuWidth, barHeight*3);
		rootMenu.addItem("Buy", "buy", false);
		rootMenu.addItem("Sell", "sel", false);
		rootMenu.addItem("Leave", "lev", false);
		
		items = new ListBox(menuWidth, 0, 0, Global.vpHeight - MsgBox.msgBoxHeight, 5, 1, menuText);
		items.openSpeed = 30;
		items.setOpenSize(Global.vpWidth - menuWidth, Global.vpHeight - MsgBox.msgBoxHeight);
		items.setDisabledPaint(grayText);
		
		buySellPanel = new MenuPanel(Global.vpWidth/2, Global.vpHeight/2, 0,0);
		buySellPanel.anchor = Anchors.TrueCenter;
		buySellPanel.openSpeed = 30;
		buySellPanel.setOpenSize((int)(menuWidth*2.5), barHeight*4);
	}
	private void updateGoldPanel()
	{
		goldPanel.getTextAt(0).text = Global.party.getGold() + "G";
	}
	private void updateBuySellPanel()
	{
		
	}
	private void buildBuyingList()
	{
		ListBoxEntry entry;
		boolean disabled;
		
		items.clearObjects();

		float iconScale = 2.0f;
		int d = (int)((float)Global.iconSize*iconScale/2.0f);
		for(Item i : merchant.getItems())
		{		
			int itemCount = -1;
			if(merchant.itemIsLimited(i.idName))
				itemCount = merchant.getCount(i.idName);				
			
			disabled = Global.party.getGold() < i.getValue() || itemCount == 0;
			
			entry = items.addItem(i.getName(), i, disabled);
			
			entry.getTextAt(0).x += d*2 + 4;					

			entry.addTextBox(""+getCost(i.getValue())+"G", items.getColumnWidth() - d, items.getRowHeight()/2, disabled ? grayTextRight : menuTextRight);
			//if(itemCount != -1)entry.addTextBox("x"+itemCount, items.getColumnWidth() - d, items.getRowHeight()*2/3, smallTextRight);
			
			entry.addPicBox(Global.createIcon(i.getIcon(), d + 6, items.getRowHeight()/2, iconScale));



		}
		items.update();
	}
	
	private void buildSellingList()
	{
		ListBoxEntry entry;
		boolean disabled;
		
		items.clearObjects();

		float iconScale = 2.0f;
		int d = (int)((float)Global.iconSize*iconScale/2.0f);
		for(Item i : merchant.getItems())
		{		
			int itemCount = -1;
			if(merchant.itemIsLimited(i.idName))
				itemCount = merchant.getCount(i.idName);				
			
			disabled = Global.party.getGold() < i.getValue() || itemCount == 0;
			
			entry = items.addItem(i.getName(), i, disabled);
			
			entry.getTextAt(0).x += d*2 + 4;					
			//add item count
			entry.addTextBox(""+i.getValue(), items.getColumnWidth() - 32, items.getRowHeight()/2, disabled ? grayText : menuText);
			entry.addPicBox(Global.createIcon(i.getIcon(), d + 6, items.getRowHeight()/2, iconScale));



		}
		items.update();
	}
	
	public boolean closed() { return closed; }
	
	private void darken(){darkening = true;}	
	private void undarken(){darkening = false;}	
	private void renderDark(){Global.renderer.drawColor(Color.argb(darkenAlpha, 0, 0, 0));}
	
	private int getCost(int itemCost)
	{
		int discountedPrice = (int)(itemCost * (discount/100.0f));
		return itemCost - discountedPrice;
	}
	
	public void cancelToState(MerchantScreenState prevState)
	{
		stateMachine.resetToState(prevState);				
	}
	
	private void showMessage(String msg, boolean yesNoOpt)
	{
		//darken();
		msgBox.addMessage(msg, yesNoOpt);
		msgBox.open();
	}
	
	public void open(Merchant merchant, float discount)
	{
		this.merchant = merchant;
		this.discount = discount;
		closed = false;
		closing = false;
		
		stateMachine.setState(getRootState());
		
		showMessage(merchant.greeting, false);
		rootMenu.open();
	}
	
	private void close()
	{
		closing = true;
	}
	
	private void handleClosing()
	{
		stateMachine.getState().handleClosing();
	}
	private void handleOption(String str)
	{
		if(str.equals("buy"))
		{
			stateMachine.setState(getBuyingState());
		}
		else if(str.equals("sel"))
		{
			stateMachine.setState(getSellingState());
		}
		else if(str.equals("lev"))
		{
			close();
		}
	}
	
	public void update()
	{
		//update darkness
		if(darkening)darkenAlpha = Math.min(darkenAlpha + darkenInc, darkenAlphaMax);
		else darkenAlpha = Math.max(darkenAlpha - darkenInc, 0);
				
		msgBox.update();
		updateGoldPanel();
		
		if(closing)
			handleClosing();
		
		stateMachine.getState().update();
		
	}
	
	public void render()
	{
		stateMachine.getState().render();
		
		if(!msgBox.Closed())
			msgBox.render();
	}
	
	public void backButtonPressed()
	{
		stateMachine.getState().backButtonPressed();		
	}
	
	public void touchActionMove(int x, int y)
	{
		if(!closing)
			stateMachine.getState().touchActionMove(x, y);		
		
	}
	public void touchActionUp(int x, int y)
	{
		if(!closing)
			stateMachine.getState().touchActionUp(x, y);		
	}
	public void touchActionDown(int x, int y)
	{
		if(!closing)
			stateMachine.getState().touchActionDown(x, y);		
	}


}
