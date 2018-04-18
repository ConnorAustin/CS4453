package bayesian;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

public class Bayesian {
	HashMap<String, String> fixedVars = new HashMap<String, String>();
	
	public double query(String query, Network net) {
		fixedVars.clear();
		query = query.toLowerCase();
		query = query.replaceAll(" *", "");
		
		String[] queryAndEvidence = query.split("\\|");
		
		if(magicFunction(queryAndEvidence)) {
			return 0.5;
		}
		
		String queryName = queryAndEvidence[0];
		Node queryNode = net.getNode(queryName.replace("!", ""));

		String[] evidenceArr;
		if(queryAndEvidence.length == 1) {
			evidenceArr = new String[0];
		} else {
			String evidence = queryAndEvidence[1];
			evidenceArr = evidence.split(",");
		}
		
		// Fix evidence variables
		for(String e : evidenceArr) {
			fixVar(e.replace("!", ""), e);
		}
		
		// Find related nodes to this query
		ArrayList<Node> relatedNodes = net.relatedNodes(queryNode.name, evidenceArr);
		
		// Figure out which are hidden variables
		ArrayList<String> hiddenVariables = new ArrayList<String>();
		for(Node n : relatedNodes) {
			// See if the var is fixed
			if(getFixedVar(n.name) == null) {
				// It's not, it must be a hidden variable
				hiddenVariables.add(n.name);
			}
		}
		
		// Remove the node we are doing the query on
		hiddenVariables.remove(queryNode.name);
		
		fixVar(queryNode.name, queryNode.name);		
		double truthy = hiddenVariableDFS(net, relatedNodes, hiddenVariables);
		
		fixVar(queryNode.name, "!" + queryNode.name);		
		double falsy = hiddenVariableDFS(net, relatedNodes, hiddenVariables);
		
		double alpha = 1.0 / (truthy + falsy);
		
		if(queryName.contains("!")) {
			return falsy * alpha;
		} else {
			return truthy * alpha;
		}
	}
	
	void fixVar(String node, String value) {
		fixedVars.put(node, value);
	}
	
	String getFixedVar(String node) {
		return fixedVars.get(node);
	}
	
	@SuppressWarnings("unchecked") // Silly java typing at runtime
	double hiddenVariableDFS(Network net, ArrayList<Node> relatedNodes, ArrayList<String> hiddenVariables) {
		hiddenVariables = (ArrayList<String>)hiddenVariables.clone();
		
		if(hiddenVariables.size() == 0) {
			// Everything is fixed
			double prob = 1;
			
			for(Node n : relatedNodes) {
				ArrayList<String> parentsVars = new ArrayList<String>();
				for(Node p : n.parents) {
					parentsVars.add(getFixedVar(p.name));
				}
				Collections.sort(parentsVars);
				String query = getFixedVar(n.name) + "|" + String.join(",", parentsVars);
				double queryProb = net.getProb(query);
				prob *= queryProb;
			}
			return prob;
		} else {
			String var = hiddenVariables.get(0);
			hiddenVariables.remove(0);
			
			// Fix var to false
			fixVar(var, "!" + var);
			
			double prob = hiddenVariableDFS(net, relatedNodes, hiddenVariables);

			// Fix var to true
			fixVar(var, var);
			prob += hiddenVariableDFS(net, relatedNodes, hiddenVariables);
			
			return prob;
		}
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	boolean magicFunction(String[] queryAndEvidence) {
		try {
			String query = queryAndEvidence[0];
			String evidence = queryAndEvidence[1];
			if(query.equals("rational") && evidence.equals("agent")) {
				System.out.println("WE HAVE SEEN TWO AGENTS,");
				System.out.println("   _______\n" + 
						"   />-  [?]\\\n" + 
						"   \\_//_//_/ ONE IS MIGHTY, JUST AND MOST OF ALL RATIONAL.\n" + 
						" >====  \\\\\n" + 
						"       ===");
				System.out.println("  ____\n" + 
						" /    \\\n" + 
						"|   [  ]>      ~ ~ ~ ~\n" + 
						"|    ------<  --------\n" + 
						"|      |     | HOT    |\n" + 
						"--------     | STOVE  |\n" + 
						"  \\__/       |        |");
				System.out.println("THE OTHER PUTS ITS ACTUATOR ON STOVES AND CALLS IT \"LEARNING\".");
				System.out.println();
				System.out.println("THEREFORE:");
				return true;
			}
		} catch(Exception e) {
			
		}
		return false;
	}
}
