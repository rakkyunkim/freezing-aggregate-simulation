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
	
	public Agent(int x, int y, int xdir, int ydir, boolean frozen) {
		super();
		this.x = x;
		this.y = y;
		this.xdir = xdir;
		this.ydir = ydir;
		this.frozen = frozen;
	}
	
	public void testFrozen(Environment state, Bag neighbors){
		//neighbors will contain the surrounding agents for the 
		//broad rule or one agent at a location for the narrow
	    //rule.
		for(int i = 0; i < neighbors.numObjs; i++) {
			Agent a = (Agent)neighbors.objs[i];
			
			if(a.frozen) {
				frozen = true;
			}
		}
		
	}
    
	
	public void step(SimState state) {
	    if(frozen) return; //nothing else to do
	    move((Environment)state);
	}
	
	public void move (Environment state) {
		if(state.random.nextBoolean(state.p)) {
			xdir = state.random.nextInt(3) - 1;
			ydir = state.random.nextInt(3) - 1;
		}
		placeAgent(state);
	}
	
	public boolean isFrozen() {
		return frozen;
	}

	public void setFrozen(boolean frozen) {
		this.frozen = frozen;
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

	public int getXdir() {
		return xdir;
	}

	public void setXdir(int xdir) {
		this.xdir = xdir;
	}

	public int getYdir() {
		return ydir;
	}

	public void setYdir(int ydir) {
		this.ydir = ydir;
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

		return x + xdir;
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

		return y + ydir;
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
	    Bag b = state.sparseSpace.getObjectsAtLocation(tempx, tempy);//Get the bag of objects at location <tempx, tempy>
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
