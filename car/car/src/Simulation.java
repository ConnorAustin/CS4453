import java.awt.*;
import javax.swing.*;
import java.util.*;

class StateRef {
	int posIndex;
	int velIndex;
	int actionIndex;
}

class QLearning {
	final int posDiscretation = 11;
	final int velDiscretation = 11;
	final int actions = 3;
	
	double learningRate;
	double discount;
	
	Random rand = new Random();
	
	double[][][] qTable = new double[posDiscretation][velDiscretation][actions];
	
	public QLearning(double learningRate, double discount) {
		this.learningRate = learningRate;
		this.discount = discount;
		
		// Initialize table to random values
		Random rand = new Random();
		for(int i = 0; i < posDiscretation; i++) {
			for(int j = 0; j < velDiscretation; j++) {
				for(int a = 0; a < actions; a++) {
					qTable[i][j][a] = rand.nextDouble() * 0.1;
				}
			}
		}
	}
	
	public void learn(double reward, StateRef oldState, StateRef newState, int action) {
		double oldStateVal = qTable[oldState.posIndex][oldState.velIndex][oldState.actionIndex];
		double newStateVal = qTable[newState.posIndex][newState.velIndex][newState.actionIndex];
		
		double updatedVal = oldStateVal + learningRate * (reward + discount * newStateVal - oldStateVal);
		
		qTable[oldState.posIndex][oldState.velIndex][oldState.actionIndex] = updatedVal;
	}
	
	public int chooseAction(StateRef curState, boolean simulatedAnnealing) {
		int bestAction = 0;
		double bestActionValue = -9999;
		
		for(int a = 0; a < actions; a++) {
			double actionValue = qTable[curState.posIndex][curState.velIndex][a];
			if(actionValue > bestActionValue) {
				bestActionValue = actionValue;
				bestAction = a;
			}
		}
		
		if(simulatedAnnealing && rand.nextFloat() < 0.05f) {
			return rand.nextInt(3) - 1;
		}
		
		return bestAction - 1;
	}
	
	public StateRef getState(mcar car, int action) {
		double posBucketSize = (mcar.POS_RANGE[1] - mcar.POS_RANGE[0]) / (double)posDiscretation;
		double velBucketSize = (mcar.VEL_RANGE[1] - mcar.VEL_RANGE[0]) / (double)velDiscretation;
		
		double pos = car.curr_pos();
		double vel = car.curr_vel();
		
		int posIndex = (int)Math.floor((pos - mcar.POS_RANGE[0]) / posBucketSize);
		int velIndex = (int)Math.floor((vel - mcar.VEL_RANGE[0]) / velBucketSize);
		
		if(posIndex >= posDiscretation) {
			posIndex = posDiscretation - 1;
		}
		
		if(velIndex >= velDiscretation) {
			velIndex = velDiscretation - 1;
		}
		int actionIndex = action + 1; 
				
		StateRef ref = new StateRef();
		ref.posIndex = posIndex;
		ref.velIndex = velIndex;
		ref.actionIndex = actionIndex;
		
		return ref;
	}
	
	public void updateState(int state, int action) {
		
	}
}

public class Simulation extends JPanel {
    public static void main(String[] args) {
	    //DO NOT CHANGE
	    JFrame.setDefaultLookAndFeelDecorated(true);
	    JFrame frame = new JFrame("Montain Car Simulation");
	    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	    frame.setBackground(Color.white);
	    frame.setSize(300, 300);
	 
	    QuadraticCurve panel = new QuadraticCurve();
	    
	 
	    frame.add(panel);
	    frame.setVisible(true);
	    //END OF DO NOT CHANGE
	    
	    mcar car = new mcar();
	    
	    QLearning ql = new QLearning(0.015, 0.7);
	    	
	    //for(int e = 0; e < 5; e++)
	    for(double pos = mcar.POS_RANGE[0]; pos < mcar.POS_RANGE[1]; pos += 0.001) {
	    		for(double vel = mcar.VEL_RANGE[0]; vel < mcar.VEL_RANGE[1]; vel += 0.001) {
		    		int steps = 0;
		    		
		    		car.set_curr_pos(pos);
		    	    car.set_curr_vel(vel);
		    	    
		    		StateRef oldState = ql.getState(car, 0);
		    	    
			    while(!car.reached_goal() && steps < 200) {
		            int act = ql.chooseAction(oldState, true);
		            
		            car.update_position_velocity(act);
		            
		            StateRef newState = ql.getState(car, act);
		            ql.learn(car.reward(), oldState, newState, act);
		            oldState = newState;
		            
		            panel.updateCircle(car.curr_pos());
		            
		            frame.repaint();
		            steps++;
			    }
		    }
	    }
		    
    	    car.set_curr_pos(0);
    	    car.set_curr_vel(0);
    	    StateRef oldState = ql.getState(car, 0);
    	    
	    while(!car.reached_goal()) {
            int act = ql.chooseAction(oldState, false);
            
            panel.circleAction(act);
            
            car.update_position_velocity(act);
            
            StateRef newState = ql.getState(car, act);
            //ql.learn(car.reward(), oldState, newState, act);
            oldState = newState;
            
            panel.updateCircle(car.curr_pos());
            try {
                Thread.sleep(50);                 //1000 milliseconds is one second.
            } catch(InterruptedException ex) {
                Thread.currentThread().interrupt();
            }
            
            frame.repaint();
	    }
    }
}
