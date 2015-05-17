/* Adapted by Ben Sims from original sources:
 * Liang - Introduction to Java Programming, 9th Edition (Code Examples of 
 * Chapter 27 Binary Search Trees)
 * This is a very basic Binary Search Tree
 */

import java.util.ArrayList;

public class BinarySearchTree<E extends Comparable<E>> {
	protected BinaryTreeNode<E> rootNode;
	private E[] a;
	
	public BinarySearchTree(){
	}//end BinarySearchTree() Constructor
	
	public BinarySearchTree(E[] list){
		makeBalancedTree(list, 0, list.length);
	}//end BinarySearchTree() Constructor
	
	//Method from above source
	public boolean insert(E e) {
		if (rootNode == null)
	      rootNode = new BinaryTreeNode<E>(e); // Create a new root
	    else {
	      // Locate the parent node
	      BinaryTreeNode<E> parent = null;
	      BinaryTreeNode<E> current = rootNode;
	      while (current != null)
	        if (e.compareTo(current.data) < 0) {
	          parent = current;
	          current = current.leftNode;
	        }
	        else if (e.compareTo(current.data) > 0) {
	          parent = current;
	          current = current.rightNode;
	        }
	        else
	          return false; // Duplicate node not inserted

	      // Create the new node and attach it to the parent node
	      if (e.compareTo(parent.data) < 0)
	        parent.leftNode = new BinaryTreeNode<E>(e);
	      else
	        parent.rightNode = new BinaryTreeNode<E>(e);
	    }
	    return true; // Element inserted
	}//end insert()
	
	//Method from above source
	public boolean delete(E e) {
	    // Locate the node to be deleted and also locate its parent node
	    BinaryTreeNode<E> parent = null;
	    BinaryTreeNode<E> current = rootNode;
	    while (current != null) {
	      if (e.compareTo(current.data) < 0) {
	        parent = current;
	        current = current.leftNode;
	      }
	      else if (e.compareTo(current.data) > 0) {
	        parent = current;
	        current = current.rightNode;
	      }
	      else
	        break; // Element is in the tree pointed at by current
	    }

	    if (current == null)
	      return false; // Element is not in the tree

	    // Case 1: current has no left children
	    if (current.leftNode == null) {
	      // Connect the parent with the right child of the current node
	      if (parent == null) {
	        rootNode = current.rightNode;
	      }
	      else {
	        if (e.compareTo(parent.data) < 0)
	          parent.leftNode = current.rightNode;
	        else
	          parent.rightNode = current.rightNode;
	      }
	    }
	    else {
	      // Case 2: The current node has a left child
	      // Locate the rightmost node in the left subtree of
	      // the current node and also its parent
	      BinaryTreeNode<E> parentOfRightMost = current;
	      BinaryTreeNode<E> rightMost = current.leftNode;

	      while (rightMost.rightNode != null) {
	        parentOfRightMost = rightMost;
	        rightMost = rightMost.rightNode; // Keep going to the right
	      }

	      // Replace the element in current by the element in rightMost
	      current.data = rightMost.data;

	      // Eliminate rightmost node
	      if (parentOfRightMost.rightNode == rightMost)
	        parentOfRightMost.rightNode = rightMost.leftNode;
	      else
	        // Special case: parentOfRightMost == current
	        parentOfRightMost.leftNode = rightMost.leftNode;     
	    }
	    return true; // Element inserted
	}
	
	//Method returns a node based on given data
	public BinaryTreeNode<E> searchTree(E e){
		BinaryTreeNode<E> current = rootNode;
		while (current != null){
			if (e.compareTo(current.data) > 0){
				current = current.rightNode;
			}
			else if (e.compareTo(current.data) < 0){
				current = current.leftNode;
			}
			else{
				return current;
			}
		}
		return null;
	}//end searchTree()
	
	//Method takes a list, 0, and its size and makes a balanced tree. It does 
	//this by recursively finding and inserting the middle index, and splitting
	//each part in half.
	public void makeBalancedTree(E[] list, int index, int index2){
	    if (index == index2){
	        return;
	    }
	      
	    int midPoint = (index + index2) / 2;
	    this.insert(list[midPoint]);
	      
	    makeBalancedTree(list, index, midPoint);
	    makeBalancedTree(list, midPoint + 1, index2);
	 }
	
	//Same method as above, but it accepts an ArrayList in place of an array
	public void makeBalancedTree(ArrayList<E> list, int index, int index2){
	    if (index == index2){
	        return;
	    }
	      
	    int midPoint = (index + index2) / 2;
	    this.insert(list.get(midPoint));
	      
	    makeBalancedTree(list, index, midPoint);
	    makeBalancedTree(list, midPoint + 1, index2);
	 }
	
	//Method copies all nodes' data to a list
	public ArrayList<E> copyToList(BinaryTreeNode<E> root){
		ArrayList<E> list = new ArrayList<E>();
		makeList(list, rootNode);
		return list;
	}//end balanceTree()
	
	//recursive method that copies elements to an ArrayList
	private void makeList(ArrayList<E> list, BinaryTreeNode<E> node){
		if (node == null){
			return;
		}
		makeList(list, node.leftNode);
		list.add(node.data);
		makeList(list, node.rightNode);
	}//end makeList()

	//Method makes a balanced tree
	public void rebalanceTree(){
		ArrayList<E> list = copyToList(rootNode);
		rootNode = null;
		makeBalancedTree(list, 0, list.size());
	}//end rebalanceTree()
}//end class BinarySearchTree

class BinaryTreeNode<E>{
	protected E data;
	protected BinaryTreeNode<E> leftNode;
	protected BinaryTreeNode<E> rightNode;
	
	public BinaryTreeNode(E e){
		this.data = e;
	}//end BinaryTreeNode() constructor
	
}//end class BinaryTreeNode