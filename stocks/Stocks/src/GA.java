import java.io.IOException;
import java.sql.Date;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class GA {
	final static int STARTING_POPULATION = 20;
	final static int GENERATION_LIMIT = 200;
	
	final static double MUTATION_RATE = 0.001;
	final static double CROSSOVER_RATE = 0.8;
	
	final static double TEST_STARTING_AMOUNT = 20000;
	final static String TEST_STARTING_DATE = "2017-2-9";
	final static String TEST_ENDING_DATE = "2018-2-9";
	
	static Random random;
	
	static ArrayList<Market> testMarkets = new ArrayList<Market>();
	
	static double fitness(Broker b) throws IOException {
		double fitnessSum = 0;
		
		for(Market m : testMarkets) {
			fitnessSum += m.simulateWithBroker(b, TEST_STARTING_AMOUNT, Date.valueOf(TEST_STARTING_DATE), Date.valueOf(TEST_ENDING_DATE));
		}
		return fitnessSum;
	}
	
	static void updateFitness(ArrayList<Broker> population) throws IOException {
		for(Broker b : population) {
			if(b.fitness < 0) {
				b.fitness = fitness(b);
			}
		}
	}

	static void roulette(ArrayList<Broker> population) {
		double allFitness = 0;
		for(Broker b : population) {
			allFitness  += b.fitness;
		}
		
		double slotStart = 0;
		for(Broker b : population) {			
			double sliceSize = b.fitness / allFitness;
			
			b.selection = slotStart + sliceSize;
			slotStart += sliceSize;
		}
	}

	static Broker select(ArrayList<Broker> population) {
		Collections.sort(population);
		
		float location = random.nextFloat();
		
		// Find which location in the roulette this landed
		for(Broker b : population) {
			if(b.selection >= location) {
				return b;
			}
		}
		System.err.println("Error: Could not find anything to select!");
		return null;
	}
	
	static void mutate(Broker b) {
		for(int i = 0; i < b.algo.length(); i++) {
			if(random.nextDouble() <= MUTATION_RATE) {
				String origAlgo = b.algo;
				
				// Keep mutating until we get a different character
				while(origAlgo.equals(b.algo)) {
					StringBuilder newAlgo = new StringBuilder(b.algo);
					
					char[] ops = new char[] {'|', '&'};
					char[] rules = new char[] {'e', 's', 'm'};
					
					char c = b.algo.charAt(i);
					
					if(c == '&' || c == '|') {
						newAlgo.setCharAt(i, ops[random.nextInt(ops.length)]);
					}
					else if(c == 'e' || c == 's' || c == 'm') {
						newAlgo.setCharAt(i, rules[random.nextInt(rules.length)]);
					}
					else {
						int newDigit = random.nextInt(10);
						newAlgo.setCharAt(i, Character.forDigit(newDigit, 10));
					}
					b.algo = newAlgo.toString();
					
					// Fix rules if we broke them
					b.correctRules();
				}
			}
		}
	}
	
	static Broker crossover(Broker m, Broker f) {
		// TODO update to fix
		
		int crossoverIndex = random.nextInt(m.algo.length());
		String childAlgo = m.algo.substring(0, crossoverIndex) + 
				           f.algo.substring(crossoverIndex, f.algo.length());
		
		Broker child = new Broker(childAlgo);
		child.correctRules();
		return child;
	}
	
	static Broker best(ArrayList<Broker> population) {
		if(population.size() == 0) {
			return null;
		}
		
		Broker best = population.get(0);
		double bestFitness = best.fitness;
		for(Broker b : population) {
			if(b.fitness > bestFitness) {
				best = b;
				bestFitness = b.fitness;
			}
		}
		return best;
	}
	
	static Broker evolve(ArrayList<Broker> population) throws Exception {
		int generation = 0;
		int curGeneration = 0;
		Broker currentBest = null;

		while(generation < GENERATION_LIMIT ) {
			boolean updatedGeneration = false;
			
			// Evaluate the fitness of the population in S
			updateFitness(population);
			
			// Create the roulette wheel for the population in S
			roulette(population);

			// Select the father and the mother (parents)
			Broker f = select(population);
			Broker m = select(population);

			double luck = random.nextDouble();

			if(luck < CROSSOVER_RATE && !m.equals(f)) {
				Broker child1 = crossover(f, m);
				Broker child2 = crossover(m, f);

				mutate(child1);
				mutate(child2);

				population.add(child1);
				population.add(child2);
				
				generation++;
				updatedGeneration = true;
				
				updateFitness(population);

				roulette(population);
			}

			// Find the best Broker thus far
			Broker bestBroker = best(population);

			if(bestBroker != null && !bestBroker.equals(currentBest)) {
				currentBest = bestBroker;
				curGeneration = generation;
				updatedGeneration = true;
			}
			if(updatedGeneration) {
				System.out.println("Generation: " + generation + ". Best Generation: " + curGeneration);
			}
		}

		return currentBest;
	}

	public static void main(String[] args) throws Exception {
		// Create the test markets
		testMarkets.add(new Market("tests/GOOG.csv"));
		testMarkets.add(new Market("tests/NATI.csv"));
		testMarkets.add(new Market("tests/NKE.csv"));
		testMarkets.add(new Market("tests/AAPL.csv"));
		testMarkets.add(new Market("tests/FORD.csv"));
		
		Market realMarket = null;
		Broker bestBroker = null;
		int startingAmt = 0;
		Date marketStart = null;
		Date marketEnd = null;
		
		if(args.length >= 4) {
			try {
				realMarket = new Market(args[0]);
				marketStart = Date.valueOf(args[1]);
				marketEnd = Date.valueOf(args[2]);
				startingAmt = Integer.parseInt(args[3]);
				if(args.length >= 5) {
					bestBroker = new Broker(args[4]);
					bestBroker.fitness = fitness(bestBroker);
				}
			}
			catch(Exception e) {
				System.out.println("Arguments ill-formed:");
				System.out.println(e.getMessage());
				return;
			}
		}
		
		if(bestBroker == null) {
			System.out.println("No broker specified. Making one using a genetic algorithm.");
			random = new Random();
			
			ArrayList<Broker> population = new ArrayList<Broker>();
			
			for(int i = 0; i < STARTING_POPULATION; i++) {
				population.add(Broker.randomBroker());
			}
			
			bestBroker = evolve(population);
		}
		
		System.out.println("");
		System.out.println("Best Broker String:");
		System.out.println(bestBroker.algo);
		System.out.println("");
		
		System.out.println("Average money the best broker gained from tests: " + ((bestBroker.fitness / testMarkets.size()) - TEST_STARTING_AMOUNT));
		
		if(realMarket != null) {
			double result = realMarket.simulateWithBroker(bestBroker, startingAmt, marketStart, marketEnd, true);
			System.out.println("Money gained from " + args[0] + ": " + (result - startingAmt));
			System.out.println("Wrote result to result.csv");
		}
	}
}
