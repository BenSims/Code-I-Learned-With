import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import org.junit.Test;


public class MergeSortTest {
	int[] testArray = {1, 5, 3, 2, 2, 4, 9, 6, 7, 8, 10};
	MergeSortTestList list = new MergeSortTestList(testArray);

	@Test
	public void testMergeSort() {
		assertTrue(list.getList()[0] == 1);
		assertTrue(list.getList()[1] == 5);
		assertTrue(list.getList()[2] == 3);
		assertTrue(list.getList()[3] == 2);
		assertTrue(list.getList()[4] == 2);
		assertTrue(list.getList()[5] == 4);
		assertTrue(list.getList()[6] == 9);
		assertTrue(list.getList()[7] == 6);
		assertTrue(list.getList()[8] == 7);
		assertTrue(list.getList()[9] == 8);
		assertTrue(list.getList()[10] == 10);
		list.mergeSort(list.getList());
		assertTrue(list.getList()[0] == 1);
		assertTrue(list.getList()[1] == 2);
		assertTrue(list.getList()[2] == 2);
		assertTrue(list.getList()[3] == 3);
		assertTrue(list.getList()[4] == 4);
		assertTrue(list.getList()[5] == 5);
		assertTrue(list.getList()[6] == 6);
		assertTrue(list.getList()[7] == 7);
		assertTrue(list.getList()[8] == 8);
		assertTrue(list.getList()[9] == 9);
		assertTrue(list.getList()[10] == 10);
	}
}

class MergeSortTestList implements MergeSort{
	private int[] list;
	
	public MergeSortTestList(int[] array){
		this.list = array;
	}
	
	public int[] getList(){
		return list;
	}
}
