/* Adapted by Ben Sims from original sources:
 * Liang - Introduction to Java Programming, 9th Edition (Code Examples of 
 * Chapter 27 Binary Search Trees)
 * This is a very basic Binary Search Tree
 */

import java.util.ArrayList;

public class BinarySearchTree<E extends Comparable<E>> {
	protected BinaryTreeNode<E> rootNode;
	
	public BinarySearchTree(){
	}//end BinarySearchTree() Constructor
	
	public BinarySearchTree(E[] list){
		makeBalancedTree(list, 0, list.length - 1);
	}//end BinarySearchTree() Constructor
	
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
	
	//Method takes a list 0 and its size and makes a balanced tree
	public void makeBalancedTree(E[] list, int index, int index2){
	      if (index == index2){
	          return;
	      }
	      
	      int midPoint = (index + index2) / 2;
	      this.insert(list[midPoint]);
	      
	      makeBalancedTree(list, index, midPoint);
	      makeBalancedTree(list, midPoint + 1, index2);
	 }
	
	//This method copies all data to an ArrayList, and deletes the node after
	public ArrayList<E> copyToList(BinaryTreeNode<E> root, ArrayList<E> list){

		list.add(root.data);
		if (root.leftNode != null){
			copyToList(root.leftNode, list);
			root.leftNode = null;
		}
		if (root.rightNode != null){
			copyToList(root.rightNode, list);
			root.rightNode = null;
		}
		return list;
	}//end balanceTree()
	
	public void rebalanceTree(){
		E[] list = null;
		list = (E[]) copyToList(rootNode, new ArrayList<E>()).toArray();
		makeBalancedTree(list, 0, list.length - 1);
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