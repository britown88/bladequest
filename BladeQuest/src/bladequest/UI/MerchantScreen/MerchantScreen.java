package bladequest.UI.MerchantScreen;

import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import bladequest.UI.ListBox;
import bladequest.UI.ListBoxEntry;
import bladequest.UI.MenuPanel;
import bladequest.UI.MsgBox;
import bladequest.UI.MsgBoxEndAction;
import bladequest.UI.ListBox.LBStates;
import bladequest.UI.NumberPicker;
import bladequest.UI.MainMenu.MainMenuState;
import bladequest.UI.MenuPanel.Anchors;
import bladequest.UI.MsgBox.Options;
import bladequest.world.Global;
import bladequest.world.Item;
import bladequest.world.Merchant;
import bladequest.world.States;

public class MerchantScreen 
{
	private Paint menuText, menuTextRight, menuTextCenter, grayText, 
	grayTextCenter, grayTextRight, blueMenuText, smallBluetext, smallText/*, redMenuText, blueMenuText, redMenuTextCenter, 
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
	private ListBox confirmBox;
	private NumberPicker countPicker;
	
	private boolean closing, closed, selling;
	
	private final int darkenAlphaMax = 200, darkenInc = 5;
	private int darkenAlpha;
	private boolean darkening;
	
	int itemListPosition;
	Item itemtoBuySell;
	
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
	
	public void cancelToState(MerchantScreenState prevState)
	{
		stateMachine.resetToState(prevState);				
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
					Global.showMessage(merchant.farewell, Options.None);
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
				items.setItemPosition(itemListPosition);
				
				selling = false;
				
				msgBox.addMessage(merchant.buying);
				msgBox.nextMessage();
			}
			private boolean suppressOpen;
			
			public void update() {
				rootMenu.update();
				msgBox.update();
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
				if(state == LBStates.Selected && !suppressOpen)
				{
					if(items.getSelectedEntry().Disabled())
					{
						msgBox.addMessage(merchant.insufficientFunds);
						msgBox.nextMessage();
					} 						
					else
					{
						itemtoBuySell = (Item)(items.getSelectedEntry().obj);					
						stateMachine.setState(getBuySellConfirmState());
					}					
				}
				
				suppressOpen = false;
			}
			public void longPress(int x, int y) {
				if(items.getCurrentSelectedEntry() != null)
				{
					Item itemGetInfo = (Item)(items.getCurrentSelectedEntry().obj);
					msgBox.addMessage(itemGetInfo.getDescription());
					msgBox.nextMessage();	
					suppressOpen = true;

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
			private boolean suppressOpen;
			
			public void onSwitchedTo(MerchantScreenState prevState) {
				buildSellingList();
				
				items.setClosed();
				items.open();
				items.setItemPosition(itemListPosition);
				
				selling = true;
				
				msgBox.addMessage(merchant.selling);
				msgBox.nextMessage();
			}

			public void update() {
				rootMenu.update();
				items.update();
				msgBox.update();
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
				if(state == LBStates.Selected && !suppressOpen)
				{
					msgBox.addMessage(merchant.sell);
					msgBox.nextMessage();
					itemtoBuySell = (Item)(items.getSelectedEntry().obj);
					
					stateMachine.setState(getBuySellConfirmState());
				}
				suppressOpen = false;
			}
			public void longPress(int x, int y) {
				if(items.getCurrentSelectedEntry() != null)
				{
					Item itemGetInfo = (Item)(items.getCurrentSelectedEntry().obj);
					msgBox.addMessage(itemGetInfo.getDescription());
					msgBox.nextMessage();	
					suppressOpen = true;

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
				itemListPosition = items.getItemPosition();				
				buySellPanel.open();
				countPicker.setValue(1);
				updateBuySellPanel();
				darken();
				msgBox.addMessage(itemtoBuySell.getDescription());
				msgBox.nextMessage();	
			}

			public void update() {
				buySellPanel.update();
				countPicker.update();
				confirmBox.update();
				updateBuySellPanel();
			}
			public void render() {
				rootMenu.render();
				items.render();
				goldPanel.render();
				renderDark();
				buySellPanel.render();
				if(buySellPanel.Opened())
				{
					countPicker.render();
					confirmBox.render();
					
				}
			}
			
			public void handleClosing(){
				stateMachine.setState(selling ? getSellingState() : getBuyingState());
			}	

			public void backButtonPressed() {
				stateMachine.setState(selling ? getSellingState() : getBuyingState());
			}
			public void touchActionUp(int x, int y) {
				countPicker.touchActionUp(x, y);
				if(confirmBox.touchActionUp(x, y) == LBStates.Selected)
				{
					if(((String)confirmBox.getSelectedEntry().obj).equals("buy"))
					{
						int unitPrice = selling ? itemtoBuySell.getValue() / 2 : getCost(itemtoBuySell.getValue());
						int count = countPicker.getValue();
						int cost = unitPrice*count;
						
						if(selling)
						{							
							Global.party.addGold(cost);
							Global.party.removeItem(itemtoBuySell.getId(), count);
						}
						else
						{
							Global.party.addGold(-cost);
							itemtoBuySell.setCount(count);
							Global.party.addItem(itemtoBuySell.idName, count);
						}
					}
					
					stateMachine.setState(selling ? getSellingState() : getBuyingState());
					
				}
			}
			public void touchActionMove(int x, int y) {
				countPicker.touchActionMove(x, y);
				confirmBox.touchActionMove(x, y);
			}
			public void touchActionDown(int x, int y) {
				countPicker.touchActionDown(x, y);
				confirmBox.touchActionDown(x, y);
			}			
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
		smallText = Global.textFactory.getTextPaint(9, Color.WHITE, Align.LEFT);				
		//smallTextCenter = Global.textFactory.getTextPaint(9, Color.WHITE, Align.CENTER);		
		//smallTextRight = Global.textFactory.getTextPaint(9, Color.WHITE, Align.RIGHT);	
		
		menuText = Global.textFactory.getTextPaint(13, Color.WHITE, Align.LEFT);	
		menuTextCenter = Global.textFactory.getTextPaint(13, Color.WHITE, Align.CENTER);	
		menuTextRight = Global.textFactory.getTextPaint(13, Color.WHITE, Align.RIGHT);
		
		grayText = Global.textFactory.getTextPaint(13, Color.GRAY, Align.LEFT);
		grayTextCenter = Global.textFactory.getTextPaint(13, Color.GRAY, Align.CENTER);		
		grayTextRight = Global.textFactory.getTextPaint(13, Color.GRAY, Align.RIGHT);	
		
//		redMenuText = Global.textFactory.getTextPaint(13, Color.RED, Align.LEFT);
		blueMenuText = Global.textFactory.getTextPaint(13, Color.CYAN, Align.LEFT);
		smallBluetext = Global.textFactory.getTextPaint(9, Color.CYAN, Align.LEFT);
//		redMenuTextCenter = Global.textFactory.getTextPaint(13, Color.RED, Align.CENTER);
//		blueMenuTextCenter = Global.textFactory.getTextPaint(13, Color.CYAN, Align.CENTER);
	}
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
		
		
		buildBuySellPanel();
		
	}
	private void buildBuySellPanel()
	{
		
		
		float iconScale = 2.0f;
		int d = (int)((float)Global.iconSize*iconScale/2.0f);
		
		buySellPanel = new MenuPanel(Global.vpWidth/2,  barHeight, 0,0);
		buySellPanel.anchor = Anchors.TopCenter;
		buySellPanel.openSpeed = 30;
		buySellPanel.setOpenSize((int)(Global.vpWidth*0.75f), barHeight*3);
		
		buySellPanel.clear();
/*0*/	buySellPanel.addTextBox("", barHeight/2, barHeight/2, blueMenuText);
/*0*/	buySellPanel.addPicBox(Global.createIcon("orb", buySellPanel.openSize.x / 2 - d, barHeight/2, iconScale));
/*1*/	buySellPanel.addTextBox("", buySellPanel.openSize.x / 2 + d, barHeight/2, menuText);
	
/*2*/	buySellPanel.addTextBox("Owned:", barHeight/2, (buySellPanel.openSize.y / 6 * 2), smallBluetext);
/*3*/	buySellPanel.addTextBox("Price:", barHeight/2, (buySellPanel.openSize.y / 6 * 3), smallBluetext);
/*4*/	buySellPanel.addTextBox("Count:", barHeight/2, (buySellPanel.openSize.y / 6 * 4), smallBluetext);
/*5*/	buySellPanel.addTextBox("Total:", barHeight/2, (buySellPanel.openSize.y / 6 * 5), smallBluetext);
		
		int col2x = 70;		
/*6*/	buySellPanel.addTextBox("", col2x, (buySellPanel.openSize.y / 6 * 2), smallText);
/*7*/	buySellPanel.addTextBox("", col2x, (buySellPanel.openSize.y / 6 * 3), smallText);
/*8*/	buySellPanel.addTextBox("", col2x, (buySellPanel.openSize.y / 6 * 4), smallText);
/*9*/	buySellPanel.addTextBox("", col2x, (buySellPanel.openSize.y / 6 * 5), smallText);

		countPicker = new NumberPicker(Global.vpWidth/2 - d*2, barHeight*2, buySellPanel.openSize.x / 2 + d*2, barHeight);
		countPicker.anchor = Anchors.TopLeft;
		
		confirmBox = new ListBox(Global.vpWidth/2 - d*2, barHeight*3, buySellPanel.openSize.x / 2 + d*2, barHeight, 1, 2, menuTextCenter);
		confirmBox.addItem("", "buy", false);
		confirmBox.addItem("Back", "bak", false);
		
	}

	private void updateGoldPanel()
	{
		goldPanel.getTextAt(0).text = Global.party.getGold() + "G";
	}
	private void updateBuySellPanel()
	{
		float iconScale = 2.0f;
		int d = (int)((float)Global.iconSize*iconScale/2.0f);
		
		buySellPanel.getTextAt(0).text = selling ? "Selling" : "Buying";
		buySellPanel.changePicBox(0, Global.createIcon(itemtoBuySell.getIcon(), buySellPanel.openSize.x / 2 - d, barHeight/2, iconScale));
		buySellPanel.getTextAt(1).text = itemtoBuySell.getDisplayName();
		
		int owned = Global.party.getItemCount(itemtoBuySell.idName);
		int unitPrice = selling ? itemtoBuySell.getValue() / 2 : getCost(itemtoBuySell.getValue());
		int count = countPicker.getValue();
		int cost = unitPrice*count;
		buySellPanel.getTextAt(6).text = ""+owned;
		buySellPanel.getTextAt(7).text = ""+unitPrice+"G";
		buySellPanel.getTextAt(8).text = ""+count;
		buySellPanel.getTextAt(9).text = ""+cost+"G";	
		
		countPicker.setMinMax(0, selling ? owned : Math.min(99 - owned, Global.party.getGold() / unitPrice));
		confirmBox.getEntryAt(0).getTextAt(0).text = selling ? "Sell!" : "Buy!";
		
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
			
			entry = items.addItem(i.getDisplayName(), i, disabled);
			
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
		items.clearObjects();

		float iconScale = 2.0f;
		int d = (int)((float)Global.iconSize*iconScale/2.0f);
		
		for(Item i : Global.party.getInventory())
		{		
			if(i.isSellable() && i.getType() != Item.Type.Key)
			{
				entry = items.addItem(i.getDisplayName(), i, false);			
				entry.getTextAt(0).x += d*2 + 4;					
				//add item count
				entry.addTextBox(""+i.getValue()/2+"G", items.getColumnWidth() - d, items.getRowHeight()/2, menuTextRight);
				entry.addPicBox(Global.createIcon(i.getIcon(), d + 6, items.getRowHeight()/2, iconScale));
			}
			
		}
		items.update();
	}
	
	private int getCost(int itemCost)
	{
		int discountedPrice = (int)(itemCost * (discount/100.0f));
		return itemCost - discountedPrice;
	}
	
	private void darken(){darkening = true;}	
	private void undarken(){darkening = false;}	
	private void renderDark(){Global.renderer.drawColor(Color.argb(darkenAlpha, 0, 0, 0));}
		
	private void showMessage(String msg, MsgBox.Options option)
	{
		//darken();
		msgBox.addMessage(msg, option);
		msgBox.open();
	}	
	private void showMessageYesNo(String msg, MsgBoxEndAction yesAction, MsgBoxEndAction noAction)
	{
		//darken();
		msgBox.showYesNo(msg, yesAction, noAction);
		msgBox.open();
	}
	public void open(Merchant merchant, float discount)
	{
		this.merchant = merchant;
		this.discount = discount;
		closed = false;
		closing = false;
		
		stateMachine.setState(getRootState());
		
		showMessage(merchant.greeting, Options.None);
		rootMenu.open();
	}	
	private void close() { closing = true;}
	public boolean closed() { return closed; }
	
	private void handleClosing()
	{
		stateMachine.getState().handleClosing();
	}
	private void handleOption(String str)
	{
		itemListPosition = 0;
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
	public void onLongPress(int x, int y)
	{
		if(!closing)
			stateMachine.getState().longPress(x, y);		
	}

}
