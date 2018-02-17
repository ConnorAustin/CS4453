import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class BigfootExists {
	static void main(String[] args) throws IOException {
		BufferedWriter writer = new BufferedWriter(new FileWriter("orig-result.csv"));
		
		writer.close();
	}
}
