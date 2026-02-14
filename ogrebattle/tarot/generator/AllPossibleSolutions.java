package ogrebattle.tarot.generator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.TreeSet;

import ogrebattle.printer.Util;
import ogrebattle.tarot.pojo.Tarot;
import ogrebattle.tarot.pojo.TarotSorting;

/**
* Return an array of arrays containing all possible answer sets with a chosen amount of variable answers. Up to 11<br>
* out of 22 total cards for 3^11 = 177,147 possible answer sets are realistic to generate. The method is brute force RNG,<br>
* which is very easy to code.<br>
* This is the coupon collector's problem where n "coupons" to collect are every permutation of answering 1, 2 or 3,<br>
* for the number of questions. Questions / variable answers are represented by the number of zeroes in the starting array.<br>
* Expected number of RNG calls is an agreeableO(n log n)
*/
public class AllPossibleSolutions {
	public final int MAX_ITERATION = 11;//3^11 = 177,147 solutions to randomly generate like the coupon collector's problem
	private List<Integer> positions;
	private static ArrayList<Integer> startingList;
	private Set<ArrayList<Integer>> ALL_SOLUTIONS;
	private boolean doNotSort;
	Random r = new Random();
	
	public static void main(String[] args) {
		AllPossibleSolutions a = new AllPossibleSolutions(false, new int[]{2,1,3,2,0,1,3,3,2,2,3,0,0,3,1,0,3,1,3,2,3,0});
	    a.iterateSolutionSetsRNG();
	}
	
	public AllPossibleSolutions(boolean doNotSort, int[] starting) {
		int length = starting.length;
		if(length != Tarot.DECK_SIZE) {
			System.err.println("Starting array length is " + length + " instead of " + Tarot.DECK_SIZE);
		}
		this.doNotSort = doNotSort;
		startingList =  new ArrayList<Integer>(length);
		for(int i : starting) {
			startingList.add(i);
		}
		printListAsArray(startingList);
		trackZeroes(startingList);
	}
	
	/**
	 * Expected number of RNG calls needed is <code>(n)(ln(n)) + (0.577)(n) + 0.5 ± n^-2</code> for O(n log n),<br>
	 * where n = 3^<code>zeroes</code> = <code>solutionSize</code> or coupons. <br>For n = 11, expected calls are 
	 * <code>(11)(ln(11)) + (0.577)(11) + 0.5 ± 1/11^2 = 33.2 ± 0.00826</code>, or ~34 rounding up.<br>
	 */
	public void iterateSolutionSetsRNG() {
		final int zeroes = positions.size();
		if (zeroes > MAX_ITERATION) {
			System.err.println("Counted " + zeroes + " zeroes but"
					+ " limit is " + MAX_ITERATION);
		} else {
			final int TOTAL_SOLUTIONS = (int) Math.pow(3, zeroes);
			
			System.out.println("ZEROES: " + zeroes);
			System.out.println("SOLUTIONS: " + TOTAL_SOLUTIONS);
			
			if (doNotSort) {
				ALL_SOLUTIONS = new LinkedHashSet<ArrayList<Integer>>();//HashSet would deterministically order
			} else {
				ALL_SOLUTIONS = new TreeSet<ArrayList<Integer>>(new TarotSorting.SolutionsComparator());
			}
			
			long start = System.nanoTime();
			
			int whileloops = 0;
			//feasible to construct without randomization by treating solutions as a base 3 number with up to 11 digits
			while(ALL_SOLUTIONS.size() < TOTAL_SOLUTIONS) {
				ArrayList<Integer> holder = new ArrayList<>(Tarot.DECK_SIZE);
				for(Integer i : startingList) {//deep copy is necessary
					holder.add(i);
				}
				for(int i : positions) {
					holder.set(i, r.nextInt(3)+1);//populate the zero with 1, 2 or 3
				}
				ALL_SOLUTIONS.add(holder);
				whileloops++;
			}
			
			long end = System.nanoTime();
			
			System.out.println((double) (end - start) / Util.NANOSECONDS_IN_1_SECOND + " seconds to generate all "
					+ TOTAL_SOLUTIONS + " solutions from " + whileloops + " while loops");		
			printAllSolutions();
		}
	}
	
	private void trackZeroes(ArrayList<Integer> startingSet) {
		positions = new ArrayList<Integer>(Tarot.DECK_SIZE);
		for(int i=0; i<Tarot.DECK_SIZE; i++) {
			if(startingList.get(i) == 0) {
				positions.add(i);
			}
		}
		//forming sublist not necessary since uninitialized values are null and size equals zeroes
		printListAsArray(positions);
	}
	
	public void printAllSolutions() {
		StringBuilder sb = new StringBuilder();
		Iterator<ArrayList<Integer>> it = ALL_SOLUTIONS.iterator();
		while(it.hasNext()) {
			printList(it.next());
		}
		System.out.println(sb.toString());
	}
	
	public void printList(List<Integer> list, String first, String last) {
		if (list.size() > 0) {
			StringBuilder sb = new StringBuilder(first);
			for(int i=0; i<list.size()-1; i++) {
				sb.append(list.get(i)).append(",");
			}
			sb.append(list.get(list.size()-1)).append(last);
			System.out.println(sb.toString());
		}
	}
	
	public void printList(List<Integer> list) {
		printList(list, "[", "]");
	}
	
	public void printListAsArray(List<Integer> list) {
		printList(list, "{", "}");
	}
	
	public Set<ArrayList<Integer>> returnSolutionsSet() {
		return ALL_SOLUTIONS;
	}
	
	public List<ArrayList<Integer>> solutionsArrayList() {
		return  new ArrayList<ArrayList<Integer>>(ALL_SOLUTIONS);
	}
	
	public List<ArrayList<Integer>> solutionsLinkedList() {
		return  new LinkedList<ArrayList<Integer>>(ALL_SOLUTIONS);
	}

	public List<ArrayList<Integer>> returnHandsSorted(int returnHands) {
		if (!doNotSort)//already sorted
			return  solutionsArrayList();
		
		List<ArrayList<Integer>> holder = new ArrayList<ArrayList<Integer>>(ALL_SOLUTIONS);
		//ALL_SOLUTIONS remains unsorted
		Collections.sort(holder, new TarotSorting.SolutionsComparator());
		return new ArrayList<ArrayList<Integer>>(holder);
	}
}
/*

 */ 
