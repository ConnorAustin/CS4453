package bayesian;

import java.util.*;

public class Node {
	public String name;
	public ArrayList<Node> parents = new ArrayList<Node>();
	public ArrayList<Node> children = new ArrayList<Node>();
	
	public Node(String name) {
		this.name = name;
	}
	
	public Node(Node node) {
		this.name = node.name;
	}
	
	@Override
	public boolean equals(Object obj) {
		if(obj instanceof Node) {
			Node n = (Node)obj;
			return name.equals(n.name);
		}
		
		return false;
	}
	
	public void probability(String list, double prob) {
		list = list.replaceAll(" *", "");
		
		String[] names = list.split("&");
	}
	
	public void addParent(Node n) {
		parents.add(n);
	}
	
	public void addChild(Node n) {
		children.add(n);
	}
	
	@Override
	public String toString() {
		return name;
	}
}
