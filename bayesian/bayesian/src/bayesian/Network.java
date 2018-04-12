package bayesian;

import java.util.*;

public class Network {
	HashMap<String, Node> nodes = new HashMap<String, Node>();
	HashMap<String, Probability> probability = new HashMap<String, Probability>(); 
	
	public ArrayList<String> nodeNames() {
		ArrayList<String> names = new ArrayList<String>();
		for(Node n : nodes.values()) {
			names.add(n.name);
		}
		return names;
	}
	
	public Probability getProb(String query) {
		System.out.println(query);
		return probability.get(query);
	}
	
	public void prob(String node, String given, double prob) {
		given = given.toLowerCase();
		given = given.replaceAll(" *", "");
		String[] givens = given.split(",");
		Arrays.sort(givens);
		String evidence = String.join(",", givens);
		String query = node + "|" + evidence;
		
		Probability p = new Probability(prob, 1 - prob);
		probability.put(query, p);
		probability.put("!" + query, p);
		System.out.println(query);
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
	
	public ArrayList<Node> relatedNodes(String nodeName) {
		Node n = getNode(nodeName);
		ArrayList<Node> nodesInNetwork = new ArrayList<Node>();
		_backwardDFS(nodesInNetwork, n);
		return nodesInNetwork;
	}
	
	void _backwardDFS(ArrayList<Node> nodesInNetwork, Node n) {
		if(!nodesInNetwork.contains(n)) {
			nodesInNetwork.add(n);
		}
		
		for(Node p : n.parents) {
			_backwardDFS(nodesInNetwork, p);
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
