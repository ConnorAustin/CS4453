package bayesian;

import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;

public class Bayesian {
	HashMap<String, String> fixedVars = new HashMap<String, String>();
	
	public double query(String query, Network net) {
		fixedVars.clear();
		query = query.toLowerCase();
		query = query.replaceAll(" *", "");
		
		String[] queryAndEvidence = query.split("\\|");
		String queryName = queryAndEvidence[0];
		String evidence = queryAndEvidence[1];
		
		// Fix evidence variables
		String[] evidenceArr = evidence.split(",");
		for(String e : evidenceArr) {
			fixVar(e.replace("!", ""), e);
		}
		
		// Find related nodes to this query
		ArrayList<Node> relatedNodes = net.relatedNodes(queryName);
		
		// Figure out which are hidden variables
		ArrayList<String> hiddenVariables = new ArrayList<String>();
		for(Node n : relatedNodes) {
			// See if the var is fixed
			if(getFixedVar(n.name) == null) {
				// It's not, it must be a hidden variable
				hiddenVariables.add(n.name);
			}
		}
		Node queryNode = net.getNode(queryName);
		hiddenVariables.remove(queryName);
		
		Probability p = hiddenVariableDFS(queryName, net, relatedNodes, hiddenVariables);
		
		double alpha = 1 / (p.truth + p.falsehood);
		System.out.println(alpha);
		System.out.println(p.truth * alpha);
		System.out.println(p.falsehood * alpha);
		
		
		
		// TODO Alpha and figure out which one to return
		return 1;
	}
	
	void fixVar(String node, String value) {
		fixedVars.put(node, value);
	}
	
	String getFixedVar(String node) {
		return fixedVars.get(node);
	}
	
	Probability hiddenVariableDFS(String queryName, Network net, ArrayList<Node> relatedNodes, ArrayList<String> hiddenVariables) {
		if(hiddenVariables.size() == 0) {
			// Everything is fixed
			Probability prob = new Probability(1, 1);
			
			for(Node n : relatedNodes) {
				ArrayList<String> parentsVars = new ArrayList<String>();
				for(Node p : n.parents) {
					parentsVars.add(getFixedVar(p.name));
				}
				Collections.sort(parentsVars);
				String query = n.name + "|" + String.join(",", parentsVars);
				prob = prob.mul(net.getProb(query));
			}
			
			return prob;
		} else {
			String var = hiddenVariables.get(0);
			hiddenVariables.remove(0);
			
			// Fix var to false
			fixVar(var, "!" + var);
			
			Probability prob = hiddenVariableDFS(queryName, net, relatedNodes, hiddenVariables);

			// Fix var to true
			fixVar(var, var);
			prob = prob.add(hiddenVariableDFS(queryName, net, relatedNodes, hiddenVariables));
			
			return prob;
		}
	}
}
