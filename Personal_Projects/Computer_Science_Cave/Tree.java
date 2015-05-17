/* Author: Ben Sims
 * Date: 12May15
 * A small program that shows the basics of making a tree.
 * This tree is not a binary tree, so each node can have more than one child.
 * At the end are special methods used for the CSCave data
 */

import java.util.ArrayList;

public class Tree<E extends Comparable<E>> {
	
	protected TreeNode<E> rootNode;
	private TreeNode<E> tempNode;

	public Tree(){
	}//end Tree() Constructor
	
	public boolean insert(E parentData, E e){
		if (rootNode == null){
			rootNode = new TreeNode<E>(e);
			return true;
		}
		else {
			tempNode = null;
			getNode(rootNode, parentData);
			if (tempNode != null){
				tempNode.children.add(new TreeNode<E>(e));
				return true;
			}
		}
		return false;
	}//end insert()
	
	//Method used to find a node based on its data. Method uses recursion, and
	//takes advantage of the nature of global variables to return the result.
	public TreeNode<E> getNode(TreeNode<E> root, E id){
		if (id.compareTo(root.data) == 0){
			tempNode = root;
		}
		root.children.stream().forEach(n -> getNode(n, id));
		
		return tempNode;
	}//end getNode()
	
	public boolean delete(E parentData, E targetNodeData){
		if (targetNodeData.compareTo(rootNode.data) == 0){
			rootNode = null;
			return true;
		}
		tempNode = null;
		getNode(rootNode, parentData);
		if (tempNode != null){
			tempNode.children.stream().filter(n -> targetNodeData
					.compareTo(n.data) == 0).forEach(n -> tempNode.children
					.remove(n));
		}
		//now check to see if it was deleted
		if (tempNode.children.stream().filter(n -> targetNodeData.compareTo(n.data) == 0).count() == 0){
			 return true;
		}
		return false;
	}//end delete()
	
}//end class Tree

class TreeNode<E>{
	protected E data;
	protected ArrayList<TreeNode<E>> children;
	
	public TreeNode(E e){
		this.data = e;
	}
}//end class TreeNode
