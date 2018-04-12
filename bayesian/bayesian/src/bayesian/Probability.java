package bayesian;

public class Probability {
	public double truth;
	public double falsehood;
	
	public Probability() {
		
	}
	
	public Probability(double t, double f) {
		truth = t;
		falsehood = f;
	}
	
	public Probability mul(Probability other) {
		Probability res = new Probability(truth, falsehood);
		res.truth *= other.truth;
		res.falsehood *= other.falsehood;
		return res;
	}
	
	public Probability add(Probability other) {
		Probability res = new Probability(truth, falsehood);
		res.truth += other.truth;
		res.falsehood += other.falsehood;
		return res;
	}
}
