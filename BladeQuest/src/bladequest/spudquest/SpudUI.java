package bladequest.spudquest;

import java.util.ArrayList;
import java.util.List;

import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Rect;
import bladequest.UI.DropBox;
import bladequest.UI.DropBoxRect;
import bladequest.UI.MenuPanel;
import bladequest.graphics.Scene;
import bladequest.world.Global;

public class SpudUI 
{
	private Scene backdrop;
	private DropBox board;
	
	private List<SpudUIState> stateQueue, runningStates;
	
	public SpudUI()
	{
		backdrop = Global.scenes.get("sqbd");
		backdrop.load();
		
		stateQueue = new ArrayList<SpudUIState>();
		runningStates = new ArrayList<SpudUIState>();
		
		buildDropBox();
	}
	
	private void buildDropBox()
	{
		board = new DropBox(0, 0, Global.vpWidth, Global.vpHeight);
		board.drawFrame = false;
		
		for(int y = 0; y < 4; ++y)
			for(int x = 0; x< 4; ++x)
				board.addPanelRect(new Rect(10 + 60*x, 50 + 60*y, 10 + 60*x + 48, 50 + 60*y + 48));
		
		
		for(int y = 0; y < 2; ++y)
			for(int x = 0; x< 4; ++x)
				board.addPanelRect(new Rect(246 + 50*x, 186 + 50*y, 246 + 50*x + 48, 186 + 50*y + 48));
		
		Paint smallTextCenter = Global.textFactory.getTextPaint(9, Color.WHITE, Align.CENTER);
		
		for(int i = 16; i < 24; ++i)
		{
			MenuPanel mp = new MenuPanel(0, 0, 48, 48);
			mp.addTextBox(""+(i-15), 24, 24, smallTextCenter);
			board.addPanel(mp, i);
		}
		
		setBoardLocked(true);
		
	}
	
	private void updateStates()
	{
		List<SpudUIState> newStates = new ArrayList<SpudUIState>();
		
		for(SpudUIState state : runningStates)
			if(!state.isDone())
				newStates.add(state);
		
		runningStates = newStates;
		
		//if no running states or last one is nonblocking
		if(runningStates.size() == 0 || !runningStates.get(runningStates.size() - 1).blocks())
		{
			//add states in order out of the queue
			//stop after adding a blocking state
			while(!stateQueue.isEmpty())
			{
				SpudUIState state = stateQueue.get(0);
				runningStates.add(state);
				stateQueue.remove(0);
				
				state.run();
				
				if(state.blocks())
					break;
			}
			
		}
		
		
	}
	
	
	public void addAllowMoveState()
	{
		SpudUIState state = new SpudUIState() {
			
			private boolean done;
			
			@Override
			public void run() { 
				setBoardLocked(false); 
				done = false;
				
				}
			
			@Override
			public boolean isDone() { return done; }
			
			@Override
			public boolean blocks() { return true; }
			
			@Override
			public void touchActionUp(int x, int y)
			{
				DropBoxRect dbr = board.getLastMovedRect();
				if(dbr != null && dbr.getPanel() != null && dbr.index() < 16)
				{
					setBoardLocked(true); 
					done = true;
					
				}

			}
		};
		
		stateQueue.add(state);
	}
	
	private void setBoardLocked(boolean locked)
	{
		MenuPanel[] panels = board.getPanels();
		
		for(int i = 0; i < 16; ++i)
			board.setPanelLocked(i, locked || panels[i] != null);		
		
	}
	
	public void update()
	{
		board.update();
		updateStates();
		
		
	}
	
	public void render()
	{
		backdrop.render();
		board.render();
		
		for(SpudUIState state : runningStates)
			state.render();
	}
	
	public void touchActionMove(int x, int y)
	{
		board.touchActionMove(x, y);
		
		for(SpudUIState state : runningStates)
			state.touchActionMove(x, y);
	}
	
	public void touchActionUp(int x, int y)
	{
		board.touchActionUp(x, y);
		
		for(SpudUIState state : runningStates)
			state.touchActionUp(x, y);
	}
	
	public void touchActionDown(int x, int y)
	{
		board.touchActionDown(x, y);
		
		for(SpudUIState state : runningStates)
			state.touchActionDown(x, y);
	}
	

}
