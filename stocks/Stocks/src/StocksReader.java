import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

class Price {
	double closingPrice;
	Date date;
	
	public Price(Date date, double closingPrice) {
		this.date = date;
		this.closingPrice = closingPrice;
	}
}

public class StocksReader {
	// Closing price on a particular day
	ArrayList<Price> closingPrice = new ArrayList<Price>();
	
	String filename;
	
	public int indexOfPrice(Date date) {
		for(int i = 0; i < closingPrice.size(); i++) {
			if(closingPrice.get(i).date.equals(date)) {
				return i;
			}
		}
		
		System.err.println("Error: '" + filename + "' does not contain date: " + date.toString());
		System.exit(1);
		return 0;
	}
	
	public Date date(int index) {
		return closingPrice.get(index).date;
	}
	
	public double price(int index) {
		if(index < 0 || index >= closingPrice.size()) {
			System.err.println("Warning: Trying to get an index in the stocks that isn't in the file '" + filename + "'.");
			System.err.println("This means that your file does not have enough history.");
			return 0.0;
		}
		
		return closingPrice.get(index).closingPrice;
	}
	
	/*
	 * Grabs a list of prices using the end index and how many to get (going backwards in time)
	 */
	public double[] prices(int endIndex, int days) {
		ArrayList<Double> p = new ArrayList<Double>();
		
		int index = endIndex;
		for(int i = 0; i < days; i++) {
			p.add(price(index));
			
			index -= 1;
			if(index < 0) {
				break;
			}
		}
		
		double[] result = new double[p.size()];
		for(int i = 0; i < p.size(); i++) {
			result[i] = p.get(i).doubleValue();
		}
		
		return result;
	}
	
	public StocksReader(String file) {
		this.filename = file;
		
		try {
			Scanner sc = new Scanner(new File(file));
			int closeColumn = 0;
			int dateColumn = 0;
			String[] header = sc.nextLine().split(",");
			
			// Find close column
			for(; closeColumn < header.length; closeColumn++) {
				if(header[closeColumn].equals("Close")) {
					break;
				}
			}
			
			// Find date column
			for(; dateColumn < header.length; dateColumn++) {
				if(header[dateColumn].equals("Date")) {
					break;
				}
			}
			
			// Grab all prices and dates
			SimpleDateFormat dateFormat = new SimpleDateFormat("y-M-d");
			while(sc.hasNextLine()) {
				String[] line = sc.nextLine().split(",");
				
				double closePrice = Double.parseDouble(line[closeColumn]);
				Date date = dateFormat.parse(line[dateColumn]);
				closingPrice.add(new Price(date, closePrice));
			}
			
			sc.close();
		} catch (FileNotFoundException | ParseException e) {
			System.err.println("Could not read stocks file: " + file);
			System.err.println(e.getMessage());
			System.exit(1);
		}	
	} 
}
