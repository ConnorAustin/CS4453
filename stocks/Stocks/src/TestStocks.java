import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.sql.Date;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;

public class TestStocks {
	static final String individual = "m212&s859|e259";
	
	public static void main(String[] args) throws Exception {
		BufferedWriter writer = new BufferedWriter(new FileWriter("result.txt"));
		DateFormat df = new SimpleDateFormat("MM/dd/yyyy");
		
		DecimalFormat dollarFormat = (DecimalFormat)DecimalFormat.getCurrencyInstance();
		dollarFormat.setNegativePrefix("-$");
		dollarFormat.setNegativeSuffix("");
		
		
		File folder = new File("stocks");
		Broker b = new Broker(individual);
	
		File[] files = folder.listFiles();
		int fig = 7;
		int onPage = 0;
		double profit = 0;
		int count = 0;
		double bestProfit = -99999999;
		double worstProfit = 99999999;
		
		for(File f : files) {
			if(!f.getName().contains(".csv"))
				continue;
			
			Market m = new Market(f.getAbsolutePath());
			double money = m.simulateWithBroker(b, 100000, Date.valueOf("2016-02-01"), Date.valueOf("2018-02-01"));
			money = money - 100000;
			count++;
			profit += money;
			worstProfit = Math.min(worstProfit, money);
			bestProfit = Math.max(bestProfit, money);
			
			System.out.println(f.getName().split("\\.")[0] + "," + money);
			
			writer.write("\\begin{tabular}{l|l}\n");
			writer.write("Entity:&"+ f.getName().split("\\.")[0] + " \\\\ \\hline\n");
			writer.write("Date Range:&2/01/2016 - 2/01/2018 \\\\ \\hline\n");
			writer.write("Starting Amount:&\\$100,000\\\\ \\hline\n");
			writer.write("Total Profit:&" + dollarFormat.format(money).replace("$", "\\$") + "\\\\ \\hline\n");	
			writer.write("\\end{tabular}\n");
			writer.write("\\begin{center}\n");
			writer.write("\\footnotesize{Fig " + fig + "}\n");
			writer.write("\\end{center}\n");
			onPage++;
			if(onPage == 6) {
				writer.write("\\newpage\n");
				onPage = 0;
			}
			fig++;
		}
		writer.write("\\begin{tabular}{l|l}\n");
		writer.write("Average Profit:&" + dollarFormat.format(profit / count).replace("$", "\\$") + "\\\\ \\hline\n");
		writer.write("Best Profit:&" + dollarFormat.format(bestProfit).replace("$", "\\$") + "\\\\ \\hline\n");
		writer.write("Worst Profit:&" + dollarFormat.format(worstProfit).replace("$", "\\$") + "\\\\ \\hline\n");
		writer.write("\\end{tabular}\n");
		writer.write("\\begin{center}\n");
		writer.write("\\footnotesize{Fig " + fig + "}\n");
		writer.write("\\end{center}\n");
		writer.close();
	}
}
