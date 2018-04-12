package bayesian;

public class CancerNetwork extends Network {
	public CancerNetwork() {
		super();
		
		this.addNode("pollution");
		this.addNode("smoker");
		this.addNode("cancer");
		this.addNode("xray");
		this.addNode("dyspnea");
		
		this.connect("pollution", "cancer");
		this.connect("smoker", "cancer");
		
		this.connect("cancer", "xray");
		this.connect("cancer", "dyspnea");
		
		this.prob("pollution", "", 0.1);
		
		this.prob("smoker", "", 0.3);
		
		this.prob("cancer", "pollution, smoker", 0.95);
		this.prob("cancer", "pollution, !smoker", 0.2);
		this.prob("cancer", "!pollution, smoker", 0.3);
		this.prob("cancer", "!pollution, !smoker", 0.001);
		
		this.prob("xray", "cancer", 0.9);
		this.prob("xray", "!cancer", 0.2);
		
		this.prob("dyspnea", "cancer", 0.65);
		this.prob("dyspnea", "!cancer", 0.2);
	}
	
	public static void main(String[] args) {
		CancerNetwork cn = new CancerNetwork();
		Bayesian b = new Bayesian();
		
		System.out.println(b.query("xray|smoker", cn));
	}
}
