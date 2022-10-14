package FreezingAggregate;

import sim.util.Bag;
import spaces.Spaces;
import sweep.SimStateSweep;

public class Environment extends SimStateSweep{
	int gridHeight = 100; //Height of the y axis
    int gridWidth = 100; //Width of the x-axis    
    boolean oneAgentPerCell = true ; //is there only one agent per space 
                                      //(when false agents can share space)
    int n = 600;//number agents
    double p = 1.0;//probability of random movement
    boolean broadRule = true;//broad rule else narrow rule
    boolean bounded = true; //is the space bounded or unbounded?
    
	public Environment(long seed, Class observer) {
        super(seed, observer);
        // TODO Auto-generated constructor stub
    }
	
	
	
	public void makeAgents() {
		int x = 0;
		int y = 0;
		int xdir = 1;
        int ydir = 1;
      //First, you need to check to make sure there are not too many (n > gridWidth*grighHeight) agents
        if(this.oneAgentPerCell) {
            int size = gridWidth * gridHeight; 
            if(n > size) {
                n = size; 
                System.out.println("Change the number of agents to " + n);
            }
        }
      //Create a frozen agent and place it in the middle
        Agent a = new Agent(gridWidth/2, gridHeight/2, xdir, ydir, true); 
        sparseSpace.setObjectLocation(a, gridWidth/2, gridHeight/2); 
        
      //Create the remaining n-1 agents that are unfrozen and place them randomly in space
        int i = 0;
        while(i < n - 1) {
            /*DO set x = random integer between 0 and gridHeight
              DO set y = random integer between 0 and gridWidth */
           	 x = this.random.nextInt(gridWidth);
           	 y = this.random.nextInt(gridHeight);
           	if(this.oneAgentPerCell) {
                Bag b = sparseSpace.getObjectsAtLocation(x, y);
               if(b == null) {
            	   Agent a2 = new Agent(x, y, xdir, ydir, false); 
                   sparseSpace.setObjectLocation(a2, x, y); 
                   schedule.scheduleRepeating(a2); //It runs on each step and randomizes the order of 
                   i++;                               //each agent's actions
               }
            }
        }
        
	}
	
	 public int getGridHeight() {
		return gridHeight;
	}



	public void setGridHeight(int gridHeight) {
		this.gridHeight = gridHeight;
	}



	public int getGridWidth() {
		return gridWidth;
	}



	public void setGridWidth(int gridWidth) {
		this.gridWidth = gridWidth;
	}



	public boolean isOneAgentPerCell() {
		return oneAgentPerCell;
	}



	public void setOneAgentPerCell(boolean oneAgentPerCell) {
		this.oneAgentPerCell = oneAgentPerCell;
	}



	public int getN() {
		return n;
	}



	public void setN(int n) {
		this.n = n;
	}



	public double getP() {
		return p;
	}



	public void setP(double p) {
		this.p = p;
	}



	public boolean isBroadRule() {
		return broadRule;
	}



	public void setBroadRule(boolean broadRule) {
		this.broadRule = broadRule;
	}



	public boolean isBounded() {
		return bounded;
	}



	public void setBounded(boolean bounded) {
		this.bounded = bounded;
	}



	public void start() {
         super.start();
         spaces = Spaces.SPARSE;//set the space
         make2DSpace(spaces, gridWidth, gridHeight); //make the space
         makeAgents();         
	 }
}
