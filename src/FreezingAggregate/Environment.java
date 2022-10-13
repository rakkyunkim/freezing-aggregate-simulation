package FreezingAggregate;

import spaces.Spaces;
import sweep.SimStateSweep;

public class Environment extends SimStateSweep{
	int gridHeight = 100; //Height of the y axis
    int gridWidth = 100; //Width of the x-axis    
    boolean oneAgentPerCell = false ; //is there only one agent per space 
                                      //(when false agents can share space)
    int n = 2000;//number agents
    double p = 1.0;//probability of random movement
    boolean broadRule = true;//broad rule else narrow rule
    boolean bounded = true; //is the space bounded or unbounded?
    
	public Environment(long seed, Class observer) {
        super(seed, observer);
        // TODO Auto-generated constructor stub
    }
	
	
	
	public void makeAgents() {
		int x = gridWidth/2;
		int y = gridHeight/2;
		int xdir = 1;
        int ydir = 1;
        Agent a = new Agent(x, y, xdir, ydir); 
        sparseSpace.setObjectLocation(a, x, y); 
        schedule.scheduleRepeating(a);
	}
	
	 public void start() {
         super.start();
         spaces = Spaces.SPARSE;//set the space
         make2DSpace(spaces, gridWidth, gridHeight); //make the space
         makeAgents();         
	 }
}
