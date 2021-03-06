import java.util.ArrayList;
import java.util.Random;


/*File Name: MergeSortText.java
 * Date: 10 Nov 14
 * Author: Ben Sims
 * Required Files: none
 * Description: This program tests the merge sort algorithm as a 
 * recursive method, and as an iterative method.
 * Input: none                    
 * Output: Sorting statistics 
 */

public class MergeSortTest {
	
	Random ranNum = new Random();
	
	public MergeSortTest(){
		runTest();
		
	}//end class
	
	public static void main(String [] args){
		System.out.println("Ben Sims, CMSC 451 DESIGN COMPUTER ALGORITHMS 7980, Project 1");
		new MergeSortTest();
	}// end main
	
	//Method used to cycle though sorting tests, and record times
	public void runTest(){
		int[] tempArray;
		int[] cloneArray;
		int count1 = 0;
		int count2 = 0;
		long time1 = 0;
		long time2 = 0;
		long[] rMSTimes = new long[55];
		long[] iMSTimes = new long[55];
		int size = 10;
		
		while (size <= 2621440){
			
			for (int i = 0; i < 55; i++){
				tempArray = makeArray(size);
				cloneArray = tempArray.clone();
				
				time1 = System.nanoTime();
				count1 = rMergeSort(tempArray);
				time2 = System.nanoTime();
				rMSTimes[i] = time2 - time1;
				
				time1 = System.nanoTime();
				count2 = iMergeSort(cloneArray);
				time2 = System.nanoTime();
				iMSTimes[i] = time2 - time1;
				
				if (!checkSort(tempArray) || !checkSort(cloneArray)){
					System.out.println("Method failed.");
					System.exit(0);
				}
			}
			
			time1 = 0;
			time2 = 0;
			
			for (int i = 5; i < 50; i++){
				time1 += rMSTimes[i];
				time2 += iMSTimes[i];
			}
			time1 /= 50;
			time2 /= 50;
			
			System.out.printf("\nRecursive Merge Sort++ Array size: %7d", size);
			System.out.printf(" Time:%10d", time1);
			System.out.printf(" Critical count: %7d", count1);
			
			System.out.printf("\nIteritive Merge Sort-- Array size: %7d", size);
			System.out.printf(" Time:%10d", time2);
			System.out.printf(" Critical count: %7d", count2);
			
			size *= 4;
			
		}
		
	}//end runTest()
	
	//Method used to make arrays of various lengths
	public int[] makeArray(int size){
		int[] tempArray = new int[size];
		
		for (int i = 0; i < size; i++){
			tempArray[i] = ranNum.nextInt(99999999) + 1;
		}
		return tempArray;
	}//end makeArray()
	

	//this method was taken from Introduction to java programming ninth edition, by Y.Daniel Liang
	//the name of the method was changed from 'mergeSort' to 'rMergeSort' and
	// it was altered to return an int for project purposes
	/** The method for sorting the numbers */
	public int rMergeSort(int[] list) {
		int count = 0; //added by Ben Sims for project purposes
		if (list.length > 1) {
			// Merge sort the first half
			int[] firstHalf = new int[list.length / 2];
	    	System.arraycopy(list, 0, firstHalf, 0, list.length / 2);
	    	count += rMergeSort(firstHalf);//changed by Ben Sims

			// Merge sort the second half
			int secondHalfLength = list.length - list.length / 2;
			int[] secondHalf = new int[secondHalfLength];
			System.arraycopy(list, list.length / 2,
					secondHalf, 0, secondHalfLength);
			count += rMergeSort(secondHalf); // Changed by Ben Sims

			// Merge firstHalf with secondHalf into list
			merge(firstHalf, secondHalf, list);
			return count + 3; // changed by Ben Sims
		}
		return count; // Changed by Ben Sims
	}//end rMergeSort()
		
	//this method was taken from Introduction to java programming ninth edition, by Y.Daniel Liang
	/** Merge two sorted lists */
	public static void merge(int[] list1, int[] list2, int[] temp) {
		int current1 = 0; // Current index in list1
		int current2 = 0; // Current index in list2
	    int current3 = 0; // Current index in temp

	    while (current1 < list1.length && current2 < list2.length) {
	    	if (list1[current1] < list2[current2])
	    		temp[current3++] = list1[current1++];
		    else
		        temp[current3++] = list2[current2++];
	    }

		while (current1 < list1.length)
			temp[current3++] = list1[current1++];

		while (current2 < list2.length)
		    temp[current3++] = list2[current2++];
		}//end merge()

	//Iterative merge sort method
	//Method found at http://kosbie.net/cmu/summer-08/15-100/handouts/IterativeMergeSort.java
	//By David Kosbie
	//method name changed from 'iterativeMergesortWithoutCopy()' to 'iMergeSort' and the method
	//was altered to return an int for project demands
	public int iMergeSort(int[] a) {
		int[] from = a, to = new int[a.length];
		int count = 0; //added by Ben Sims
		for (int blockSize=1; blockSize<a.length; blockSize*=2) {
			for (int start=0; start<a.length; start+=2*blockSize){
				count++;//added by Ben Sims
				mergeWithoutCopy(from, to, start, start+blockSize, start+2*blockSize);
			}
		    int[] temp = from;
		    from = to;
		    to = temp;
		}
		if (a != from)
			// copy back
		    for (int k = 0; k < a.length; k++)
		    	a[k] = from[k];
		
		return count;
	}//end iMergeSort()

	//merge method for iMergeSort()
	//Method found at http://kosbie.net/cmu/summer-08/15-100/handouts/IterativeMergeSort.java
	//By David Kosbie
	private void mergeWithoutCopy(int[] from, int[] to, int lo, int mid, int hi) {
		// DK: cannot just return if mid >= a.length, but must still copy remaining elements!
		// DK: add two tests to first verify "mid" and "hi" are in range
		if (mid > from.length) mid = from.length;
		if (hi > from.length) hi = from.length;
		int i = lo, j = mid;
		for (int k = lo; k < hi; k++) {
			if      (i == mid)          to[k] = from[j++];
		    else if (j == hi)           to[k] = from[i++];
		    else if (from[j] < from[i]) to[k] = from[j++];
		    else                        to[k] = from[i++];
		}
	}//end mergeWithoutCopy()
	
	//Method used to check if arrays are sorted properly
	//This is done by checking if array[i] > array[i - 1]
	public boolean checkSort(int[] array){
		for(int i = 1; i < array.length; i++){
			if (array[i] < array[i - 1]){
				return false;
			}
		}
		return true;
	}//end checkSort()

}//end all