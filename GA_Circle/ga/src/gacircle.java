/*
	Concon's Implementation
	<3
*/

import java.util.*;

public class gacircle implements Comparable<gacircle> {
	static final int STATIC_DISK_COUNT = 5; // Graphical part will not change
	static final int MAX_ITERATION = 1000;
	static final double MUTATION_RATE = 0.7; // Chance to mutate a portion of an individual
	static final int MAX_STATIC_DISK_RADIUS = 3;
	static final int BOUNDARY_WIDTH = 10;
	static final int BOUNDARY_HEIGHT = 10;
	
	double X_location;
	double Y_location;
	double radius;
	double selection; // (optional) used for roulette wheel selection
	double fitness;
	
	static Random random;

	public gacircle(double x, double y, double r, double s) {
		X_location = x;
		Y_location = y;
		radius = r;
		selection = s;
		fitness = -1;
	}
	
	@Override
	public int compareTo(gacircle c)
	{
	     return Double.compare(selection, c.selection);
	}
	
	public boolean equals(gacircle c) {
		if(c == null) return false;
		
		return X_location == c.X_location && Y_location == c.Y_location;
	}
	
	@Override
	public String toString() {
		return "x: " + X_location + ", y: " + Y_location + ", r: " + radius;
	}
	
	static double fitness(gacircle circle, gacircle[] static_disks) {
		// Fitness is the best radius a circle can have at its location 
		
		double best_radius = -1;
		boolean has_radius = false;
		
		// Limit radius by static disks
		for(gacircle c : static_disks) {
			// Distance between two circle centers
			double x = circle.X_location - c.X_location;
			double y = circle.Y_location - c.Y_location;
			double distance = Math.sqrt(x * x + y * y);
			
			double radius = distance - c.radius;
			if(has_radius) {
				best_radius = Math.min(best_radius, radius);
			} else {
				best_radius = radius;
				has_radius = true;
			}
		}
		
		// Limit radius by walls
		
		// Limit by left wall
		best_radius = Math.min(best_radius, circle.X_location);
		
		// Limit by top wall
		best_radius = Math.min(best_radius, circle.Y_location);
		
		// Limit by right wall
		best_radius = Math.min(best_radius, BOUNDARY_WIDTH - circle.X_location);
		
		// Limit by bottom wall
		best_radius = Math.min(best_radius, BOUNDARY_HEIGHT - circle.Y_location);
		
		return Math.max(0.0, best_radius);
	}

	static void eval_fitness(ArrayList<gacircle> population, gacircle[] static_disks) {
		for(gacircle c : population) {
			c.fitness = fitness(c, static_disks);
			
			// Radius is also the fitness value in this case
			c.radius = c.fitness;
		}
	}

	static void roulette(ArrayList<gacircle> population) {
		double all_fitness = 0;
		for(gacircle c : population) {
			all_fitness += Math.max(1, c.fitness);
		}
		
		double slot_start = 0;
		for(gacircle c : population) {			
			double slice_size = Math.max(1, c.fitness) / all_fitness;
			
			c.selection = slot_start + slice_size;
			slot_start += slice_size;
		}
	}

	static gacircle select(ArrayList<gacircle> population) {
		Collections.sort(population);
		
		float location = random.nextFloat();
		
		// Find which location in the roulette this landed
		for(gacircle c : population) {
			if(c.selection >= location) {
				return c;
			}
		}
		System.err.println("Error: Could not find anything to select!");
		return null;
	}

	static void mutate(gacircle circle) {
		if(random.nextDouble() <= MUTATION_RATE) {
			circle.X_location = random.nextDouble() * BOUNDARY_WIDTH;
		}
		
		if(random.nextDouble() <= MUTATION_RATE) {
			circle.Y_location = random.nextDouble()  * BOUNDARY_HEIGHT;
		}
	}
	
	static gacircle crossover(gacircle m, gacircle f) {
		double x = m.X_location;
		double y = m.Y_location;
		
		// 50/50 chance of getting the x and y from female
		
		if(random.nextDouble() < 0.5) {
			x = f.X_location;
		}
		
		if(random.nextDouble() < 0.5) {
			y = f.Y_location;
		}
		
		return new gacircle(x, y, 0, 0);
	}
	
	static gacircle answersofar(ArrayList<gacircle> population, gacircle[] static_disks) {
		if(population.size() == 0)
			return null;
		
		// Look for best
		gacircle best = population.get(0);
		for(gacircle c : population) {
			if(c.fitness > best.fitness) {
				best = c;
			}
		}
		return best;
	}

	static gacircle produce(ArrayList<gacircle> population, gacircle[] static_disks, Splat pl) {
		int count = 0;
		int current_generation = 0;
		int generation = 0;
		gacircle current = null;

		while(count < MAX_ITERATION) {
			// Evaluate the fitness of the population in S
			eval_fitness(population, static_disks);

			// Create the roulette wheel for the population in S
			roulette(population);

			// Select the father and the mother (parents)
			gacircle F = select(population);
			gacircle M = select(population);

			float luck = random.nextFloat();

			if(luck < 0.80f) {
				gacircle child1 = crossover(F, M);
				gacircle child2 = crossover(M, F);

				mutate(child1);
				mutate(child2);

				// Add the offspring to the population
				population.add(child1);
				population.add(child2);

				current_generation++;

				// Re-evaluate the fitness of the population again
				eval_fitness(population,static_disks);

				// Re-set the roulette wheel again
				roulette(population);
			}

			// Find the best answer so far
			gacircle A = answersofar(population, static_disks);
			System.out.println(A);
			
			if(A != null) {
				// Update the screen
				pl.update(A);
				
				try {
					Thread.sleep(20);
				} catch(InterruptedException ex) {
					Thread.currentThread().interrupt();
				}
			}

			if(A != null && !A.equals(current)) {
				current = A;
				generation = current_generation;
			}
			System.out.println("answer's generation: " + generation +  ", total generation: " + current_generation);
			count++;
		}

		return current;
	}

	public static void main(String[] args) {
		random = new Random();
		
		Scanner scan = new Scanner(System.in);
		
		gacircle [] static_disks = new gacircle[STATIC_DISK_COUNT ];

		// Create 5 random circle to within the rectangle (0,0), (0,10), (10,0), and (10,10)
		for(int i = 0; i < STATIC_DISK_COUNT; i++) {
			double x = random.nextInt(BOUNDARY_WIDTH) + random.nextFloat();
			double y = random.nextInt(BOUNDARY_HEIGHT) + random.nextFloat();
			double r = random.nextInt(MAX_STATIC_DISK_RADIUS) + random.nextFloat();
			static_disks[i] = new gacircle(x, y, r, 0);
		}

		// Array List of population
		ArrayList<gacircle> pop = new ArrayList<gacircle>();

		// Reading the initial population from the input file
		while(scan.hasNext()) {
			gacircle C = new gacircle(scan.nextDouble(), scan.nextDouble(),0,0);
			pop.add(C);
		}

		// Evaluating the fitness of the population
		eval_fitness(pop, static_disks);

		// Create the slots for roulette wheel selection
		roulette(pop);

		// Splat object for drawing circle
		Splat sp1 = new Splat();

		// Create dummy gacircle
		gacircle B = new gacircle(0,0,0,0);

		// Draw the static disks with the dummy circle
		sp1.run(static_disks, B);

		// static_diskset the answer
		gacircle A = produce(pop, static_disks, sp1);

		if(A != null) {
			// An answer was found

			System.out.println("Disks set");
			for(int i = 0; i < static_disks.length; i++) {
				System.out.println("Disk #" + i + " :" + static_disks[i]);
			}

			System.out.println("Solution:");
			System.out.println(A);
			sp1.update(A);
		} else {
			// No answer was found

			System.out.println("no answer");

			for(int i = 0; i < pop.size(); i++) {
				System.out.println(pop.get(i));
			}
		}
		scan.close();
	}
}
