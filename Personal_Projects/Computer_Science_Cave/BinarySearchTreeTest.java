/* Author: Ben Sims
 * Date: 13 May 15
 * Tests made for BinarySearchTree
 */
import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.Test;


public class BinarySearchTreeTest {
	BinarySearchTree<Integer> testTree;
	ArrayList<Integer> testList;

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
		
		testList = testTree.copyToList(testTree.rootNode);
		assertTrue(testList.contains(1));
		assertTrue(testList.contains(2));
		assertTrue(testList.contains(3));
		assertTrue(testList.contains(4));
		assertTrue(testList.contains(5));
		assertTrue(testList.contains(6));
		assertTrue(testList.contains(7));
		assertTrue(testList.contains(8));
		assertTrue(testList.contains(9));
		assertTrue(testList.contains(10));
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
		Integer[] list = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10};
		testTree = new BinarySearchTree<Integer>(list);
		int testInt = testTree.searchTree(10).data.intValue();
		assertTrue(testInt == 10);
	}

	@Test
	public void testCopyToList() {
		Integer[] list = {1, 5, 2, 8, 3, 7, 9, 4, 6, 10};
		testTree = new BinarySearchTree<Integer>(list);
		testList = testTree.copyToList(testTree.rootNode);
		assertEquals(10, testList.size());
		assertTrue(testList.contains(1));
		assertTrue(testList.contains(2));
		assertTrue(testList.contains(3));
		assertTrue(testList.contains(4));
		assertTrue(testList.contains(5));
		assertTrue(testList.contains(6));
		assertTrue(testList.contains(7));
		assertTrue(testList.contains(8));
		assertTrue(testList.contains(9));
		assertTrue(testList.contains(10));
	}

	@Test
	public void testMakeBalancedTreeEArrayIntInt() {
		testTree = new BinarySearchTree<Integer>();
		Integer[] list = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10};
		testTree.makeBalancedTree(list, 0, list.length);
		testList = testTree.copyToList(testTree.rootNode);
		assertTrue(testList.get(0) == 1);
		assertTrue(testList.get(1) == 2);
		assertTrue(testList.get(2) == 3);
		assertTrue(testList.get(3) == 4);
		assertTrue(testList.get(4) == 5);
		assertTrue(testList.get(5) == 6);
		assertTrue(testList.get(6) == 7);
		assertTrue(testList.get(7) == 8);
		assertTrue(testList.get(8) == 9);
		assertTrue(testList.get(9) == 10);
	}

	@Test
	public void testMakeBalancedTreeArrayListOfEIntInt() {
		testTree = new BinarySearchTree<Integer>();
		Integer[] array = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10};
		testList = new ArrayList<Integer>();
		for (Integer i: array){
			testList.add(i);
		}
		testTree.makeBalancedTree(testList, 0, testList.size());
		ArrayList<Integer> testList = testTree.copyToList(testTree.rootNode);
		assertTrue(testList.get(0) == 1);
		assertTrue(testList.get(1) == 2);
		assertTrue(testList.get(2) == 3);
		assertTrue(testList.get(3) == 4);
		assertTrue(testList.get(4) == 5);
		assertTrue(testList.get(5) == 6);
		assertTrue(testList.get(6) == 7);
		assertTrue(testList.get(7) == 8);
		assertTrue(testList.get(8) == 9);
		assertTrue(testList.get(9) == 10);
	}

	@Test
	public void testRebalanceTree() {
		Integer[] list = {1, 5, 2, 8, 3, 7, 9, 4, 6, 10};
		testTree = new BinarySearchTree<Integer>(list);
		testTree.rebalanceTree();
		testList = testTree.copyToList(testTree.rootNode);
		assertTrue(testList.get(0) == 1);
		assertTrue(testList.get(1) == 2);
		assertTrue(testList.get(2) == 3);
		assertTrue(testList.get(3) == 4);
		assertTrue(testList.get(4) == 5);
		assertTrue(testList.get(5) == 6);
		assertTrue(testList.get(6) == 7);
		assertTrue(testList.get(7) == 8);
		assertTrue(testList.get(8) == 9);
		assertTrue(testList.get(9) == 10);
	}
}
