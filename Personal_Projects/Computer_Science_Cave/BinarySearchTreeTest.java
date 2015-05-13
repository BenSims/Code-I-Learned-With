import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.Test;


public class BinarySearchTreeTest {
	
	BinarySearchTree<Integer> testTree;
	BinaryTreeNode<Integer> node;

	@Test
	public void testBinarySearchTree() {
		testTree = new BinarySearchTree<Integer>();
		assertNotNull(testTree);
	}

	@Test
	public void testBinarySearchTreeEArray() {
		Integer[] list = {1, 5, 2, 8, 3, 7, 9, 4, 6, 10};
		testTree = new BinarySearchTree<Integer>(list);
		assertNotNull(testTree);
	}

	@Test
	public void testInsert() {
		testTree = new BinarySearchTree<Integer>();
		assertTrue(testTree.insert(1));
	}

	@Test
	public void testDelete() {
		testTree = new BinarySearchTree<Integer>();
		testTree.insert(1);
		assertTrue(testTree.delete(1));
	}

	@Test
	public void testSearchTree() {
		Integer[] list = {1, 5, 2, 8, 3, 7, 9, 4, 6, 10};
		testTree = new BinarySearchTree<Integer>(list);
		assertEquals(node, testTree.searchTree(10));
		assertEquals((int)10, (int)testTree.searchTree(10).data);
	}

	@Test
	public void testMakeBalancedTree() {
		fail("Not yet implemented");
	}

	@Test
	public void testCopyToList() {
		Integer[] list = {1, 5, 2, 8, 3, 7, 9, 4, 6, 10};
		testTree = new BinarySearchTree<Integer>(list);
		ArrayList<Integer> testList = new ArrayList<Integer>();
		testList = testTree.copyToList(testTree.rootNode, new ArrayList<Integer>());
		assertEquals(10, testList.size());
	}

	@Test
	public void testRebalanceTree() {
		fail("Not yet implemented");
	}

}
