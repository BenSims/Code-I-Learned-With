/* Author: Ben Sims
 * Date: 20 May 15
 * This is a basic recursive merge sort for integers. Its worse time is O(nLog(n)), and 
 * requires O(n) extra space. It is not adaptive, but it is very stable.
 */
public interface MergeSort {
	
	public default void mergeSort(int[] list){
		if (list.length < 2){
			return;
		}
		int midPoint = list.length / 2;
		int[] leftHalf = new int[midPoint];
		int[] rightHalf = new int[list.length - midPoint];
		
		System.arraycopy(list, 0, leftHalf, 0, midPoint);
		System.arraycopy(list, midPoint, rightHalf, 0, rightHalf.length);
		
		mergeSort(leftHalf);
		mergeSort(rightHalf);
		
		merge(leftHalf, rightHalf, list);
	}
	
	 default void merge(int[] leftHalf, int[] rightHalf, int[]sortedList){
		//Indexes for leftHalf, rightHalf, and sortedList
		int count1 = 0, count2 = 0, count3 = 0;
		
		while (count1 < leftHalf.length && count2 < rightHalf.length){
			if (leftHalf[count1] < rightHalf[count2]){
				sortedList[count3++] = leftHalf[count1++];
			}
			else{
				sortedList[count3++] = rightHalf[count2++];
			}
		}
		while (count1 < leftHalf.length){
			sortedList[count3++] = leftHalf[count1++];
		}
		while (count2 < rightHalf.length){
			sortedList[count3++] = rightHalf[count2++];
		}
	}
}
