import java.sql.Date;
import java.util.ArrayList;
import java.util.Random;

public class GA {
	final static int POPULATION_LIMIT = 200;
	
	final static double TEST_STARTING_AMOUNT = 20000;
	final static String TEST_STARTING_DATE = "2017-2-9";
	final static String TEST_ENDING_DATE = "2018-2-9";
	
	static Random random;
	
	static ArrayList<Market> testMarkets = new ArrayList<Market>();
	
	// TODO
	static double fitness(Broker b) {
		double fitnessSum = 0;
		
		for(Market m : testMarkets) {
			m.simulateWithBroker(b, TEST_STARTING_AMOUNT, Date.valueOf(TEST_STARTING_DATE), Date.valueOf(TEST_ENDING_DATE));
		}
		return 69;
	}

	// TODO
	static void updateFitness(ArrayList<Broker> population) {
		// TODO note: no need to update fitness on ones that have fitness. speed!
	}

	// TODO
	static void roulette(ArrayList<Broker> population) {
		/*double all_fitness = 0;
		for(gacircle c : population) {
			all_fitness += Math.max(1, c.fitness);
		}
		
		double slot_start = 0;
		for(gacircle c : population) {			
			double slice_size = Math.max(1, c.fitness) / all_fitness;
			
			c.selection = slot_start + slice_size;
			slot_start += slice_size;
		}*/
	}

	// TODO
	static Broker select(ArrayList<Broker> population) {
		/*Collections.sort(population);
		
		float location = random.nextFloat();
		
		// Find which location in the roulette this landed
		for(gacircle c : population) {
			if(c.selection >= location) {
				return c;
			}
		}
		System.err.println("Error: Could not find anything to select!");
		return null;
		*/
		return null;
	}

	// TODO
	static void mutate(Broker broker) {
		
	}
	
	// TODO
	static Broker crossover(Broker m, Broker f) {
		return null;
	}
	
	// TODO
	static Broker best(ArrayList<Broker> population) {
		if(population.size() == 0) {
			return null;
		}
		
		return null;
	}

	// TODO
	static Broker evolve(ArrayList<Broker> population) {
		int curGeneration = 0;
		int generation = 0;
		Broker currentBest = null;

		while(population.size() < POPULATION_LIMIT) {
			// Evaluate the fitness of the population in S
			updateFitness(population);
			
			// Create the roulette wheel for the population in S
			roulette(population);

			// Select the father and the mother (parents)
			Broker F = select(population);
			Broker M = select(population);

			float luck = random.nextFloat();

			if(luck < 0.80f) {
				Broker child1 = crossover(F, M);
				Broker child2 = crossover(M, F);

				mutate(child1);
				mutate(child2);

				population.add(child1);
				population.add(child2);

				curGeneration++;

				updateFitness(population);

				roulette(population);
			}

			// Find the best Broker thus far
			Broker bestBroker = best(population);

			if(bestBroker != null && !bestBroker.equals(currentBest)) {
				currentBest = bestBroker;
				generation = curGeneration;
			}
		}
		
		System.out.println("Final Generation: " + generation);

		return currentBest;
	}

	public static void main(String[] args) {
		random = new Random();
		
		ArrayList<Broker> population = new ArrayList<Broker>();
		
		// TODO add 20 random initial population
		
		// evolve(population);
		
		Broker b = new Broker("s020&e200|m420");
		Market m = new Market("GOOG.csv");
		double res = m.simulateWithBroker(b, 20000, Date.valueOf("2017-2-9"), Date.valueOf("2018-2-9"));
		System.out.println(res);
	}
}
