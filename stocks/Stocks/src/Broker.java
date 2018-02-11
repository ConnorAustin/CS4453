public class Broker {	
	/* 
	 * Algorithm that the broker uses.
	 * 
	 * s = SMA (Sample Moving Average)
	 *     Buy if the actual price is greater than the SMA over a time period
	 *     Sell otherwise
	 *     
	 * e = EMA (Exponential Moving Average)
	 *     Buy if the actual price is greater than the SMA over a time period
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
	String algo;
	
	// TODO gather variables
	
	public Broker(String algo) {
		this.algo = algo;
	}
	
	String evalRule(String rule, Market m) {
		String periodStr = rule.substring(1, 4);
		int period = Integer.parseInt(periodStr);
		
		char r = rule.charAt(0);
		
		boolean buy = false;
		
		switch(r) {
			case 's':
				buy = m.currentPrice() > m.SMA(period);
				break;
			case 'e':
				buy = m.currentPrice() > m.EMA(period);
				break;
			case 'm':
				buy = m.currentPrice() > m.MAX(period);
				break;
		}
		
		if(buy) {
			return "BUY";
		}
		return "SELL";
	}
	
	/*
	 * Returns BUY, SELL or DO_NOTHING
	 */
	String combineActions(String action1, String action2, String op) {
		if(op.equals("&")) {
			if(!action1.equals(action2)) {
				return "DO_NOTHING";
			}
			return action1;
		} else {
			if(action1.equals("BUY") || action2.equals("BUY")) {
				return "BUY";
			}
			if(action1.equals("SELL") || action2.equals("SELL")) {
				return "SELL";
			}
			return "DO_NOTHING";
		}
	}
	
	/*
	 * Returns BUY, SELL or DO_NOTHING
	 */
	public String decideAction(Market m) {
		String action1 = evalRule(algo.substring(0, 4), m);
		String action2 = evalRule(algo.substring(5, 9), m);
		String action3 = evalRule(algo.substring(10, 14), m);
		
		String op1 = algo.substring(4, 5);
		String op2 = algo.substring(9, 10);
		
		String tmp = combineActions(action1, action2, op1);
		return combineActions(tmp, action3, op2);
	}	
}
