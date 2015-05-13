/* Author: Ben Sims
 * Date: 12May15
 * A small program that shows the basics of making a tree.
 * This tree is not a binary tree, so it can have more than one child.
 * At the end are special methods used for the CSCave
 */

import java.util.ArrayList;

public class Tree {
	
	protected TreeNode<E> rootNode;

	public Tree(){
	}//end Tree() Constructor
	
	public void setRootNode(TreeNode<?> node){
		this.rootNode = node;
	}//end setRootNode()
	
	public TreeNode<E> getNode(){
		
	}
}//end class Tree

class TreeNode<E>{
	protected E data;
	protected ArrayList<TreeNode<E>> children;
	
	public TreeNode(E e){
		this.data = e;
	}
}//end class TreeNode
