package FreezingAggregate;

import sim.engine.SimState;
import sim.engine.Steppable;
import sim.field.grid.SparseGrid2D;
import sim.util.Bag;

public class Agent implements Steppable{
	boolean frozen = false;
	int x;
	int y;
	int xdir;
	int ydir;
	
	public Agent(int x, int y, int xdir, int ydir) {
		super();
		this.x = x;
		this.y = y;
		this.xdir = xdir;
		this.ydir = ydir;
	}
	
	public void testFrozen(Environment state, Bag neighbors){
		//neighbors will contain the surrounding agents for the 
		//broad rule or one agent at a location for the narrow
	    //rule.
		for(int i = 0; i < neighbors.numObjs; i++) {
			Agent a = (Agent)neighbors.objs[i];
			
			state.sparseSpace.getObjectsAtLocation(a.x + 1, a.y + 1);
			state.sparseSpace.getObjectsAtLocation(a.x, a.y + 1);
			state.sparseSpace.getObjectsAtLocation(a.x - 1, a.y - 1);
			state.sparseSpace.getObjectsAtLocation(a.x, a.y - 1);
			state.sparseSpace.getObjectsAtLocation(a.x + 1, a.y);
			state.sparseSpace.getObjectsAtLocation(a.x - 1, a.y);
		}
		
	}
    
	
	public void step(SimState state) {
	    if(frozen) return; //nothing else to do
	    move((Environment)state);
	}
	
	public void move (Environment state) {
		xdir = state.random.nextInt(3) - 1;
		ydir = state.random.nextInt(3) - 1;
	
		placeAgent(state);
	}
	
	public int bx(int x, Environment state) {
		if (x < 0){
			// generate new x
			xdir = 1;
			return x + 1;
		}
		else if (x >= state.gridWidth) {
			xdir = -1;
			return x - 1;
		}
		return x;
	}
	public int by(int y, Environment state) {
		if(y < 0) {
			// generate new y
			ydir = 1;
			return y + 1;
		}
		else if (y >= state.gridHeight) {
			ydir = -1;
			return y - 1;
		}
		return y;
	}
	
	public void placeAgent(Environment state) {
	    int tempx;
	    int tempy;
	    if(state.bounded){
	        tempx = bx(x + xdir, state);//correct for a bounded space on x-axis;
	        tempy = by(y + ydir, state);//correct for a bounded space on y-axis;
	    }
	    else {
	    	tempx = state.sparseSpace.stx(x + xdir); //correct for a toroidal space on x-axis;
	        tempy = state.sparseSpace.sty(y + ydir); //correct for a toroidal space on y-axis;
	    }
	    Bag b = state.sparseSpace.getObjectsAtLocation(x, y);//Get the bag of objects at location <tempx, tempy>
	    if(b == null) {
	        x = tempx;
	        y = tempy;
	        state.sparseSpace.setObjectLocation(this, x, y);
	    }
	    if(state.broadRule) {
	        if(state.bounded)
	            b = state.sparseSpace.getMooreNeighbors(x, y, 1, state.sparseSpace.BOUNDED, false);//getMooreNeightors for a bounded space
	        else
	            b = state.sparseSpace.getMooreNeighbors(x, y, 1, state.sparseSpace.TOROIDAL, false);//getMooreNeightors for a toroidal space
	        testFrozen(state,b);
	     }
	     else if(b != null) {
	        testFrozen(state,b);
	     }
	}

}
