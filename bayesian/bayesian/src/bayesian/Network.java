package bayesian;

import java.util.*;

public class Network {
	HashMap<String, Node> nodes = new HashMap<String, Node>();
	HashMap<String, Double> probability = new HashMap<String, Double>();
	
	public ArrayList<String> nodeNames() {
		ArrayList<String> names = new ArrayList<String>();
		for(Node n : nodes.values()) {
			names.add(n.name);
		}
		return names;
	}
	
	public double getProb(String query) {
		return probability.get(query);
	}
	
	public void prob(String node, String given, double prob) {
		given = given.toLowerCase();
		given = given.replaceAll(" *", "");
		String[] givens = given.split(",");
		Arrays.sort(givens);
		String evidence = String.join(",", givens);														// Andrew made these tabs with his left big toe
		String query = node + "|" + evidence;
		
		probability.put(query, prob);
		probability.put("!" + query, 1.0 - prob);
	}
	
	public void addNode(String name) {
		Node n = new Node(name);
		nodes.put(name, n);
	}
	
	public Node getNode(String name) {
		return nodes.get(name);
	}
	
	public void connect(String parentName, String childName) {
		Node parent = getNode(parentName);
		Node child = getNode(childName);
		
		parent.addChild(child);
		child.addParent(parent);
	}
	
	public ArrayList<Node> relatedNodes(String nodeName, String[] evidence) {
		Node n = getNode(nodeName);
		ArrayList<Node> nodesInNetwork = new ArrayList<Node>();
		
		for(String e : evidence) {
			Node nodeEvidence = getNode(e.replace("!", ""));
			backwardDFS(nodesInNetwork, nodeEvidence);
		}
				
		backwardDFS(nodesInNetwork, n);
		return nodesInNetwork;
	}
	
	void backwardDFS(ArrayList<Node> nodesInNetwork, Node n) {
		if(!nodesInNetwork.contains(n)) {
			nodesInNetwork.add(n);
		}
		
		for(Node p : n.parents) {
			backwardDFS(nodesInNetwork, p);
		}
	}
	
	/*
	 * Returns all nodes without parents
	 */
	ArrayList<Node> orphanNodes() {
		ArrayList<Node> pln = new ArrayList<Node>();
		for(Node n : nodes.values()) {
			if(n.parents.isEmpty()) {
				pln.add(n);
			}
		}
		return pln;
	}
	
	@Override
	public String toString() {
		StringBuilder b = new StringBuilder();
		for(Node n : nodes.values()) {
			b.append(n.name + " ");
			
			for(Node c : n.children) {
				b.append("-> " + c.name + " ");
			}
			b.append("\n");
		}
		return b.toString();
	}
}
