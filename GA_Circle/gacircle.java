/*
	Concon's Implementation
	<3
*/

import java.io.*;
import java.util.*;
import java.awt.*;
import java.lang.*;

public class gacircle {

	double X_location;
	double Y_location;
	double radius;
	double selection; // (optional) used for roullete wheel selection

	public gacircle(double x, double y, double r, double s) {
		X_location = x;
		Y_location = y;
		radius = r;
		selection = s;
	}

	// TODO
	static void eval_fitness(ArrayList<gacircle> population, gacircle[] static_disks) {

	}

	// TODO
	static void roulette(ArrayList<gacircle> population) {

	}

	// TODO
	static int select(ArrayList<gacircle> population) {
		return 0;
	}

	// TODO
	static void mutate(gacircle circle) {

	}

	static answersofar(ArrayList<gacircle> population, gacircle[] static_disks) {

	}

	static int produce(ArrayList<gacircle> S, gacircle[] static_disks, Splat pl) {
		final int MAX_ITERATION = 1000;
		int count = 0;
		int current_generation = 0;
		int generation = 0;
		int current = -1;

		while(count < MAX_ITERATION) {
			// Evaluate the fitness of the population in S
			eval_fitness(S, static_disks);

			// Create the roullete wheel for the population in S
			roulette(S);

			// Select the father and the mother (parents)
			int f = select(S);
			int m = select(S);

			gacircle F;
			gacircle M;

			Random prob = new Random();
			int luck = prob.nextInt(100);

			if(luck < 80) {
				gacircle child1 = crossover(S.get(f),S.get(m));
				gacircle child2 = crossover(S.get(m),S.get(f));

				mutate(child1);
				mutate(child2);

				// Add the offspring to the population
				S.add(child1);
				S.add(child2);

				current_generation++;

				// Re-evaluate the fitness of the population again
				eval_fitness(S,static_disks);

				// Re-set the roulette wheel again
				roulette(S);
			}


			// static_diskset the best answer so far
			int k = answersofar(S, static_disks);
			gacircle A = S.get(k);

			// Update the screen
			pl.update(A);
			try {
				Thread.sleep(20);
			} catch(InterruptedException ex) {
				Thread.currentThread().interrupt();
			}

			if(k != -1 && k != current) {
				current = k;
				generation = current_generation;
			}
			System.out.println("answer's generation: "+generation+ ", total generation: "+current_generation);
			count++;
		}

		return current;
	}

	public static void main(String[] args) {
		Scanner scan = new Scanner(System.in);

		double x = 0, y = 0, r = 0;

		gacircle [] static_disks = new gacircle[5];

		Random Xgen = new Random();
		Random Xdec = new Random();
		Random Ygen = new Random();
		Random Ydec = new Random();
		Random Rgen = new Random();
		Random Rdec = new Random();

		// Create 5 random circle to within the rectangle (0,0), (0,10), (10,0), and (10,10)
		for(int i = 0; i < 5; i++) {
			static_disks[i] = new gacircle(Xgen.nextInt(10) + Xdec.nextFloat(), Ygen.nextInt(10) + Ydec.nextFloat(), Rgen.nextInt(3) + Rdec.nextFloat(), 0);
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

		// Create the slots for roullete wheel selection
		roulette(pop);

		// Splat object for drawing circle
		Splat sp1 = new Splat();

		// Create dummy gacircle
		gacircle B = new gacircle(0,0,0,0);

		// Draw the the 5 initial circle with the dummy circle
		sp1.run(static_disks,B);

		// static_diskset the answer
		int k = produce(pop, static_disks, sp1);

		if(k != -1) {
			// An answer was found

			System.out.println("Disks set");
			for(int i = 0; i < static_disks.length; i++) {
				System.out.println("Disk #"+i+" : x-location:"+static_disks[i].X_location + ", y-location:"+static_disks[i].Y_location+", radius:"+static_disks[i].radius);
			}

			gacircle A = pop.get(k);
			System.out.println("Solution:");
			System.out.println("x-location: " + A.X_location + ",y-location: " + A.Y_location + ", radius: " + A.radius);
			sp1.update(A);
		} else {
			// No answer was found

			System.out.println("no answer");

			for(int i = 0; i < pop.size(); i++) {
				System.out.println(pop.get(i).X_location + " , " + pop.get(i).Y_location + " , " + pop.get(i).radius);
			}
		}
	}
}
