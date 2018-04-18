package bayesian;

import java.util.Scanner;

public class Main {
	public static void main(String[] args) {
		System.out.println("Enter a query");
		System.out.println("If you want to exit: exit");
		System.out.println("Example: !xray | smoker, !pollution");
		System.out.println();
		
		CancerNetwork cn = new CancerNetwork();
		Bayesian b = new Bayesian();
		Scanner sc = new Scanner(System.in);

		while(true) {
			try {
				String line = sc.nextLine();
				if(line.toLowerCase().equals("exit")) {
					break;
				}
				
				System.out.println(b.query(line, cn));
				System.out.println("Next query:");
			}
			catch(Exception e) {
				System.out.println("Bad query");
				System.out.println("Example: !xray | smoker, !pollution");
			}
		}
		
		sc.close();
	}
}
