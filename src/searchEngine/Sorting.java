package searchEngine;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry; // You may need it to implement fastSort

public class Sorting {

	/*
	 * This method takes as input an HashMap with values that are Comparable. 
	 * It returns an ArrayList containing all the keys from the map, ordered 
	 * in descending order based on the values they mapped to. 
	 * 
	 * The time complexity for this method is O(n^2) as it uses bubble sort, where n is the number 
	 * of pairs in the map. 
	 */
    public static <K, V extends Comparable> ArrayList<K> slowSort (HashMap<K, V> results) {
        ArrayList<K> sortedUrls = new ArrayList<K>();
        sortedUrls.addAll(results.keySet());	//Start with unsorted list of urls

        int N = sortedUrls.size();
        for(int i=0; i<N-1; i++){
			for(int j=0; j<N-i-1; j++){
				if(results.get(sortedUrls.get(j)).compareTo(results.get(sortedUrls.get(j+1))) <0){
					K temp = sortedUrls.get(j);
					sortedUrls.set(j, sortedUrls.get(j+1));
					sortedUrls.set(j+1, temp);					
				}
			}
        }
        return sortedUrls;                    
    }
    
    
	/*
	 * This method takes as input an HashMap with values that are Comparable. 
	 * It returns an ArrayList containing all the keys from the map, ordered 
	 * in descending order based on the values they mapped to. 
	 * 
	 * The time complexity for this method is O(n*log(n)), where n is the number 
	 * of pairs in the map. 
	 */
	private static <K, V extends Comparable> ArrayList<K> merge(ArrayList<K> list1, ArrayList<K> list2, HashMap<K, V> results){
		ArrayList<K> mergedList = new ArrayList<K>();

		while(!list1.isEmpty() && !list2.isEmpty()){
			if(results.get(list1.get(0)).compareTo(results.get(list2.get(0))) > 0){
				mergedList.add(list1.remove(0));
			}
			else{
				mergedList.add(list2.remove(0));
			}
		}

		while(!list1.isEmpty()){
			mergedList.add(list1.remove(0));
		}

		while(!list2.isEmpty()){
			mergedList.add(list2.remove(0));
		}

		return mergedList;
	}

	private static <K, V extends Comparable> ArrayList<K> mergeSort(ArrayList<K> newList, HashMap<K,V> results){

		if(newList.size() == 1){
			return newList;
		}

		else{
			int mid = (newList.size() - 1) / 2;
			ArrayList<K> list1 = new ArrayList<K>();

			for(int i = 0; i <= mid ; i ++){
				list1.add(newList.get(i));
			}

			ArrayList<K> list2 = new ArrayList<K>();

			for(int i = mid + 1; i < newList.size() ; i ++){
				list2.add(newList.get(i));
			}
			
			if(newList.size() < 100){
				list1 = insertionSort(list1, results);
				list2 = insertionSort(list2, results);
			} else {
				list1 = mergeSort(list1, results);
				list2 = mergeSort(list2, results);
			}

			
			return merge(list1, list2, results);
		}

	}
	

	private static <K, V extends Comparable> ArrayList<K> insertionSort(ArrayList<K> newList, HashMap<K, V> results){
		
		for(int i = 1 ; i < newList.size(); i ++) {
			K element = newList.get(i);
			int k = i;
			
			while(k>0 && results.get(element).compareTo(results.get(newList.get(k-1))) > 0) {
				newList.set(k, newList.get(k-1));
				k--;
				
			}
			newList.set(k, element);
		}
		
		return newList;
		
	}

    public static <K, V extends Comparable> ArrayList<K> fastSort(HashMap<K, V> results) {
    	// ADD YOUR CODE HERE

		if(results.isEmpty()){
			return null;
		}
		if(results.size() == 1){
			ArrayList<K> newList = new ArrayList<K>();
			newList.addAll(results.keySet());
			return newList;
		}
		else{
			ArrayList<K> newList = new ArrayList<K>();
			newList.addAll(results.keySet());
			
			if(newList.size() < 100){
				return insertionSort(newList, results);
			} else {

			return mergeSort(newList, results);
		}

    }
    }

}