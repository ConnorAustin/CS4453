import java.util.Random;

public class Broker implements Comparable<Broker> {	
	static Random random = new Random();
	
	/* 
	 * Algorithm that the broker uses.
	 * 
	 * s = SMA (Sample Moving Average)
	 *     Buy if the actual price is greater than the SMA over a time period
	 *     Sell otherwise
	 *     
	 * e = EMA (Exponential Moving Average)
	 *     Buy if the actual price is greater than the EMA over a time period
	 *     Sell otherwise
	 *     
	 * m = MAX
	 *     Buy if the actual price is greater than the max over the time period
	 *     Sell otherwise
	 * 
	 * The time period is a three-digit integer in days following the algorithm char.
	 * 
	 * Rules are evaluated left to right.
	 * 
	 * You can combine algorithms with either an and (&) or an or (|).
	 * 
	 * The string is always 14 characters. Thus, three rules with two boolean operators.
	 * 
	 * Example string: s020&e200|m420
	 */
	public String algo;
	
	// Genetic algorithm fitness
	public double fitness = -1.0f;
	
	// Genetic algorithm selection value
	public double selection;
	
	public Broker(String algo) {
		this.algo = algo;
	}
	
	@Override
    public boolean equals(final Object obj) {
		try {
			Broker b = (Broker)obj;
			return b.algo.equals(algo);
		} catch(Exception e) {
			return false;
		}
	}
	
	@Override
	public String toString() {
		return algo;
	}
	
	@Override
	public int compareTo(Broker o) {
		return Double.compare(selection, o.selection);
	}	
	
	static Broker randomBroker() {
		String[] methods = new String[] {"s", "e", "m"};
		String[] ops = new String[] {"&", "|"};
		
		String algo = "";
		algo += methods[random.nextInt(methods.length)];
		algo += String.format("%03d", random.nextInt(1000));
		
		algo += ops[random.nextInt(ops.length)];
		
		algo += methods[random.nextInt(methods.length)];
		algo += String.format("%03d", random.nextInt(1000));
		
		algo += ops[random.nextInt(ops.length)];
		
		algo += methods[random.nextInt(methods.length)];
		algo += String.format("%03d", random.nextInt(1000));
		
		Broker b = new Broker(algo);
		b.correctRules();
		return b;
	}
	
	/*
	 * Returns true for buy and false for sell otherwise
	 */
	boolean evalRule(String rule, Market m) {
		String periodStr = rule.substring(1, 4);
		int period = Integer.parseInt(periodStr);
		
		char r = rule.charAt(0);
		
		switch(r) {
			case 's':
				return m.currentPrice() > m.SMA(period);
			case 'e':
				return m.currentPrice() > m.EMA(period);
			case 'm':
				return m.currentPrice() > m.MAX(period);
		}
		return false;
	}
	
	boolean combineActions(boolean action1, boolean action2, String op) {
		if(op.equals("&")) {
			return action1 & action2;
		} else {
			return action1 | action2;
		}
	}
	
	public boolean decideAction(Market m) {
		boolean action1 = evalRule(algo.substring(0, 4), m);
		boolean action2 = evalRule(algo.substring(5, 9), m);
		boolean action3 = evalRule(algo.substring(10, 14), m);
		
		String op1 = algo.substring(4, 5);
		String op2 = algo.substring(9, 10);
		
		boolean tmp = combineActions(action1, action2, op1);
		return combineActions(tmp, action3, op2);
	}
	
	public void correctRules() {
		int[] rules = new int[] {0, 5, 10};
		boolean duplicateRules = false;
		
		int duplicate1 = 0;
		int duplicate2 = 0;
		
		for(duplicate1 = 0; duplicate1 < rules.length; duplicate1++) {
			for(duplicate2 = duplicate1 + 1; duplicate2 < rules.length; duplicate2++) {
				if(algo.charAt(rules[duplicate1]) == algo.charAt(rules[duplicate2])) {
					duplicateRules = true;
					break;
				}
			}
			if(duplicateRules) { 
				break;
			}
		}
		
		if(!duplicateRules) {
			return;
		}
		
		int duplicateToChange = duplicate1;
		if(random.nextInt(2) != 0) {
			duplicateToChange = duplicate2;
		}
		
		StringBuilder newAlgo = new StringBuilder(algo);
		char[] ruleNames = new char[] {'m', 's', 'e'}; 
		newAlgo.setCharAt(rules[duplicateToChange], ruleNames[random.nextInt(ruleNames.length)]);
		algo = newAlgo.toString();
		correctRules();
	}
}
