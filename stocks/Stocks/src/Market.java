import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Market {
	// How much to charge for every BUY or SELL action
	final double TRANSACTION_FEE = 7.0;

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
		double[] prices = stocks.prices(stockIndex - 1, period);
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
		
		double[] prices = stocks.prices(stockIndex - 1, period);
		
		double biggestPrice = -1.0;
		for(double price : prices) {
			biggestPrice = Math.max(biggestPrice, price);
		}
		return biggestPrice;
	}
	
	public double simulateWithBroker(Broker b, double startingAmt, Date startDate, Date endDate) throws IOException {
		return simulateWithBroker(b, startingAmt, startDate, endDate, false);
	}
	
	public double simulateWithBroker(Broker b, double startingAmt, Date startDate, Date endDate, boolean writeResults) throws IOException {
		BufferedWriter writer = null;
		DateFormat df = new SimpleDateFormat("MM/dd/yyyy");

		if(writeResults) {
			 writer = new BufferedWriter(new FileWriter("result.csv"));
			 writer.write(b.algo + "," + startingAmt + "\n");
		}
		
		stockIndex = stocks.indexOfPrice(startDate);
		int endStockIndex = stocks.indexOfPrice(endDate);
		
		double account = startingAmt;
		double gain = 0;
		
		int shares = 0;
		
		boolean hadOneTransaction = true;
		
		while(stockIndex != endStockIndex) {
			// Ask the broker what it wants to do
			boolean buy = b.decideAction(this);
			
			if(buy) {
				int purchasableShares = (int) ((account - TRANSACTION_FEE) / currentPrice());
				if(purchasableShares > 0) {
					account -= TRANSACTION_FEE;
					account -= currentPrice() * purchasableShares;
					
					shares += purchasableShares;
					hadOneTransaction = true;
					if(writeResults) {
						writer.write(df.format(stocks.date(stockIndex)) + "," + currentPrice() + "," + "BUY" + "," + (account + gain) + "\n");
					}
				} else {
					if(writeResults) {
						writer.write(df.format(stocks.date(stockIndex)) + "," + currentPrice() + "," + "WAIT" + "," + (account + gain) + "\n");
					}
				}
			} else {
				if(shares > 0) {
					account -= TRANSACTION_FEE;
					account += shares * currentPrice();
					
					shares = 0;
					hadOneTransaction = true;
					if(writeResults) {
						writer.write(df.format(stocks.date(stockIndex)) + "," + currentPrice() + "," + "SELL" + "," + (account + gain) + "\n");
					}
				} else {
					if(writeResults) {
						writer.write(df.format(stocks.date(stockIndex)) + "," + currentPrice() + "," + "WAIT" + "," + (account + gain) + "\n");
					}
				}
			}
			
			// If the current amount is less than the starting amount ...
			if(account < startingAmt && gain > 0) {
				// ... Use the money in gain to get it back to the start amount if we have any
				double diff = Math.min(gain, startingAmt - account);
				account += diff;
				gain -= diff;
			}
			
			// If the current amount is more than the starting amount ...
			if(account > startingAmt) {
				// ... Add the difference to the gain and set the current amount back to the start
				double diff = account - startingAmt;
				account = startingAmt;
				gain += diff;
			}
			
			// Go to the next stock entry
			stockIndex++;
		}
		
		// Sell off any remaining stocks at the end
		if(shares > 0) {
			gain += shares * currentPrice() - TRANSACTION_FEE;
			if(writeResults) {
				writer.write(df.format(stocks.date(stockIndex)) + "," + currentPrice() + "," + "SELL" + "," + (account + gain) + "\n");
			}
		}
		
		double total = account + gain;

		// Punish brokers that do not participate in the market
		if(!hadOneTransaction) {
			total /= 2.0;
		}
		
		if(writeResults) {
			writer.close();
		}
		return total;
	}
}
