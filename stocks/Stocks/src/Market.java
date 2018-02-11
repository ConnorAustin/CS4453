import java.util.Date;

public class Market {
	// How much to charge for every BUY or SELL action
	final double TRANSACTION_FEE = 7.0;
		
	// How many stocks to purchase when a BUY action is sent
	final int STOCKS_BUY_AMT = 1;
	
	// The current stock index
	int stockIndex;
	
	StocksReader stocks;
	
	public Market(String stocksFile) {
		stocks = new StocksReader(stocksFile);
	}
	
	public double currentPrice() {
		return stocks.price(stockIndex);
	}
	
	public double SMA(int period) {
		if(period == 0) {
			return 0.0;
		}
		
		double sum = 0;
		double[] prices = stocks.prices(stockIndex, period);
		for(double price : prices) {
			sum += price;
		}
		return sum / (double)prices.length;
	}
	
	public double EMA(int period) {
		if(period == 0) {
			return 0.0;
		}
		
		double[] prices = stocks.prices(stockIndex, period);
		
		double numerator = 0;
		double denominator = 0;
		
		double alpha = 2.0 / ((double)period + 1.0);
		
		for(int i = 0; i < prices.length; i++) {
			double factor = Math.pow(1.0 - alpha, i);
			denominator += factor;
			numerator += prices[i] * factor;
		}
		
		return numerator / denominator;
	}
	
	public double MAX(int period) {
		if(period == 0) {
			return 0;
		}
		
		double[] prices = stocks.prices(stockIndex, period);
		
		double biggestPrice = -1.0;
		for(double price : prices) {
			biggestPrice = Math.max(biggestPrice, price);
		}
		return biggestPrice;
	}
	
	public double simulateWithBroker(Broker b, int startingAmt, Date startDate, Date endDate) {		
		stockIndex = stocks.indexOfPrice(startDate);
		int endStockIndex = stocks.indexOfPrice(endDate);
		
		double curAmt = startingAmt;
		double gain = 0;
		
		int stockAmt = 0;
		
		while(stockIndex != endStockIndex) {
			// Ask the broker what it wants to do
			String action = b.decideAction(this);
			
			if(action.equals("BUY")) {
				double fee = TRANSACTION_FEE + currentPrice() * STOCKS_BUY_AMT;
				if(curAmt >= fee) {
					curAmt -= fee;
					stockAmt += STOCKS_BUY_AMT;
				}
			}
			
			if(action.equals("SELL")) {
				double fee = TRANSACTION_FEE;
				if(curAmt >= fee) {
					curAmt -= fee;
					curAmt += stockAmt * currentPrice();
					stockAmt = 0;
				}
			}
			
			// If the current amount is less than the starting amount ...
			if(curAmt < startingAmt && gain > 0) {
				// ... Use the money in gain to get it back to the start amount if we have any
				double diff = Math.min(gain, startingAmt - curAmt);
				curAmt += diff;
				gain -= diff;
			}
			
			// If the current amount is more than the starting amount ...
			if(curAmt > startingAmt) {
				// ... Add the difference to the gain and set the current amount back to the start
				double diff = curAmt - startingAmt;
				curAmt = startingAmt;
				gain += diff;
			}
			
			// Go to the next stock entry
			stockIndex++;
		}
		
		return gain;
	}
}
