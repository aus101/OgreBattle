package ogrebattle.tarot.generator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.TreeSet;

import ogrebattle.tarot.pojo.Tarot;
import ogrebattle.tarot.pojo.TarotSorting;
import ogrebattle.util.Printer;
import ogrebattle.util.Util;

/**
* Return an array of arrays containing all possible answer sets with a chosen amount of variable answers.<br>
* Up to 14 out of 22 total cards for 3^14 = 4,782,969 answer sets are feasible to generate with<br>
* {@link ogrebattle.tarot.generator.TernaryGenerator}, which was difficult to code.<br>
* Up to 11 out of 22 total cards for 3^14 = 177,147 answer sets are feasible to generate with<br>
* brute force RNG, which was easy to code.
* RNG approach is the coupon collector's problem,<br>
* where n "coupons" to collect are every permutation of answering 1, 2 or 3 for the number of questions.<br>
* Questions / variable answers are represented by the number of zeroes in the starting array.<br>
* Expected number of RNG calls is <code>O(n log n) with n = 3^zeroes</code>. Iterating is <code>O(n)</code>
*/
public class AllPossibleSolutions {
	public static final int ITERATION_LIMIT = 14;//3^14 = 4,782,969 solutions, heap space exhausted at 3^15
	public static final String ITERATION_MESSAGE = "the limit is " + ITERATION_LIMIT + " for solutionSetsIterate due heap space exhaustion";
	
	public static final int RNG_LIMIT = 11;//3^11 = 177,147 solutions to randomly generate like the coupon collector's problem
	public static final String RNG_MESSAGE = "the limit is " + RNG_LIMIT + " for solutionSetsRNG due to the growth of RNG calls";
			
	private int[] positions;
	private int[] starting;
    private int zeroes;				
	private int totalSolutions;

	private boolean doNotSort;//only for solutionSetsRNG brute force method
	private Random r = new Random();
	
	public static void main(String[] args) {
		AllPossibleSolutions all = new AllPossibleSolutions(new int[]{2,1,3,2,0,1,3,3,2,2,3,0,0,3,1,0,3,1,3,2,3,0});
		
	    System.out.println();
	    try {
	    	all.solutionSetsRNG();
	    } catch (IllegalArgumentException e) {
	    	System.err.println(e.getMessage());
	    }
	    
	    System.out.println();
	    try {
		    all.solutionSetsIterate(true);
	    } catch (IllegalArgumentException e) {
	    	System.err.println(e.getMessage());
	    }
	}
	
	public AllPossibleSolutions(int[] starting) {
		this(false, starting);
	}
	
	public AllPossibleSolutions(boolean doNotSort, int[] starting) {
		int length = starting.length;
		if(length != Tarot.DECK_SIZE) {
			System.err.println("Starting array length is " + length + " instead of " + Tarot.DECK_SIZE);
		}
		this.doNotSort = doNotSort;
		this.starting = starting.clone();//deep copy
		System.out.println(Arrays.toString(starting).replaceAll(" ", ""));
		trackZeroes(starting);
	}
	
	public List<int[]> solutionSetsIterate() {
		return solutionSetsIterate(false);
	}
	
	private void validate(int LIMIT, String MESSAGE) {
		if (positions.length > LIMIT) {
			throw new IllegalArgumentException("Counted " + positions.length + " zeroes but " + MESSAGE);
		} else if (positions.length == 0) {
			throw new IllegalArgumentException("No zeroes so nothing to iterate on");	
		}
	}
	
	/**
	 * Use {@link ogrebattle.tarot.generator.TernaryGenerator} to quickly generate all possible solutions in <code>O(n)</code>
	 * @param print boolean to print the solutions or not
	 * @return the solution set as a list of arrays
	 */
	public List<int[]> solutionSetsIterate(boolean print) {
		validate(ITERATION_LIMIT, ITERATION_MESSAGE);
		final int totalSolutions = (int) Math.pow(3, positions.length);
			
		long start = System.nanoTime();
		int[][] solutions = TernaryGenerator.bigEndianGenerator(positions.length, 1);//offset for 1,2,3 instead of 0,1,2
		int[][] tempting = new int[totalSolutions][Tarot.DECK_SIZE];

		for (int count = 0; count < totalSolutions; count++) {
			tempting[count] = starting.clone();//deep copy is necessary
			int value = 0;
			for (int zero : positions) {
				tempting[count][zero] = solutions[count][value];
				value++;
			}
		}
		
		List<int[]> allSolutions = Arrays.asList(tempting);
		long end = System.nanoTime();

		System.out.println((double) (end - start) / Util.NANOSECONDS_IN_1_SECOND + " seconds to generate all "
				+ totalSolutions + " solutions from solutionSetsIterate");		
			
		if (print) System.out.println(Printer.newLine + Printer.matrixStringBuilder(tempting));
			
		return allSolutions;
	}
	
	public Set<ArrayList<Integer>> solutionSetsRNG() {
		return solutionSetsRNG(false);
	}
	
	/**
	 * Brute force all combinations with RNG. Stop when <code>solutionSize</code> solutions are found <code>= 3^zeroes</code>.<br>
	 * Expected number of RNG calls needed is <code>(n)(ln(n)) + (0.577216)(n) + 0.5</code> for O(n log n), 
	 * where n = <code>solutionSize</code> or coupons. <br>For zeroes = 11 = 177,147 solution sets, expected calls are 
	 * <code>(177147)(ln(177147)) + (0.577216)(177147) + 0.5 = 2,243,027</code>.<br>
	 * @param print boolean to print the solutions or not
	 * @return the solution set as a list of arrays
	 */
	public Set<ArrayList<Integer>> solutionSetsRNG(boolean print) {
		validate(RNG_LIMIT, RNG_MESSAGE);
		Set<ArrayList<Integer>> allSolutions = null;
			
		if (doNotSort) {
			//HashSet would deterministically order, (4/3) prevents loading factor from resizing
			allSolutions = new LinkedHashSet<ArrayList<Integer>>(totalSolutions*(4/3));//(int) not used since all solutions are powers of 3
		} else {
			allSolutions = new TreeSet<ArrayList<Integer>>(new TarotSorting.SolutionsComparator());//big endian sorting
		}
			
		long start = System.nanoTime();
			
		int whileLoops = 0;
		//feasible to construct without randomization by treating solutions as a base 3 number with up to 11 digits
		while(allSolutions.size() < totalSolutions) {
			ArrayList<Integer> holder = new ArrayList<>(Tarot.DECK_SIZE);
			for(Integer i : starting) {//deep copy is necessary
				holder.add(i);
			}
			
			for(int zero : positions) {
				holder.set(zero, r.nextInt(3)+1);//populate the zero with 1, 2 or 3
			}
			allSolutions.add(holder);//set so only adds if unique
			whileLoops++;
		}
			
		long end = System.nanoTime();
			
		System.out.println((double) (end - start) / Util.NANOSECONDS_IN_1_SECOND + " seconds to generate all "
				+ totalSolutions + " solutions from " + whileLoops + " while loops");
			
		if (print) printAllSolutions(allSolutions);
		
		return allSolutions;
	}
	
	private void trackZeroes(int[] startingSet) {
		int[] temp = new int[Tarot.DECK_SIZE];
		for(int i=0; i<Tarot.DECK_SIZE; i++) {
			if(starting[i] == 0) {
				temp[zeroes] = i;
				zeroes++;
			}
		}
		positions = new int[zeroes];
		for(int i=0; i<zeroes; i++) {
			positions[i] = temp[i];
		}
		totalSolutions = (int) Math.pow(3, zeroes);
		
		StringBuilder sb = new StringBuilder();
		Printer.arrayStringBuilder(positions, sb);
		sb.append(Printer.newLine).append("ZEROES:    ").append(zeroes).append(Printer.newLine)
			.append("SOLUTIONS: ").append(totalSolutions).append(" = 3^").append(zeroes);
		System.out.println(sb.toString());
	}
	
	public void printAllSolutions(Collection<ArrayList<Integer>> allSolutions) {
		StringBuilder sb = new StringBuilder(Printer.newLine);
		Iterator<ArrayList<Integer>> it = allSolutions.iterator();
		while(it.hasNext()) {
			printList(it.next());
		}
		System.out.println(sb.toString());
	}
	
	public void printList(List<Integer> list) {
		Printer.printList(list, "[", "]");
	}
	
	public void printListAsArray(List<Integer> list) {
		Printer.printList(list, "{", "}");
	}
}
/*
[2,1,3,2,0,1,3,3,2,2,3,0,0,3,1,0,3,1,3,2,3,0]
[4,11,12,15,21]

ZEROES:    5
SOLUTIONS: 243 = 3^5

0.0067632 seconds to generate all 243 solutions from 1655 while loops

ZEROES:    5
SOLUTIONS: 243 = 3^5
5.633E-4 seconds to generate all 243 solutions from solutionSetsIterate

[2,1,3,2,1,1,3,3,2,2,3,1,1,3,1,1,3,1,3,2,3,1]
[2,1,3,2,1,1,3,3,2,2,3,1,1,3,1,1,3,1,3,2,3,2]
[2,1,3,2,1,1,3,3,2,2,3,1,1,3,1,1,3,1,3,2,3,3]
[2,1,3,2,1,1,3,3,2,2,3,1,1,3,1,2,3,1,3,2,3,1]
[2,1,3,2,1,1,3,3,2,2,3,1,1,3,1,2,3,1,3,2,3,2]
[2,1,3,2,1,1,3,3,2,2,3,1,1,3,1,2,3,1,3,2,3,3]
[2,1,3,2,1,1,3,3,2,2,3,1,1,3,1,3,3,1,3,2,3,1]
[2,1,3,2,1,1,3,3,2,2,3,1,1,3,1,3,3,1,3,2,3,2]
[2,1,3,2,1,1,3,3,2,2,3,1,1,3,1,3,3,1,3,2,3,3]
[2,1,3,2,1,1,3,3,2,2,3,1,2,3,1,1,3,1,3,2,3,1]
[2,1,3,2,1,1,3,3,2,2,3,1,2,3,1,1,3,1,3,2,3,2]
[2,1,3,2,1,1,3,3,2,2,3,1,2,3,1,1,3,1,3,2,3,3]
[2,1,3,2,1,1,3,3,2,2,3,1,2,3,1,2,3,1,3,2,3,1]
[2,1,3,2,1,1,3,3,2,2,3,1,2,3,1,2,3,1,3,2,3,2]
[2,1,3,2,1,1,3,3,2,2,3,1,2,3,1,2,3,1,3,2,3,3]
[2,1,3,2,1,1,3,3,2,2,3,1,2,3,1,3,3,1,3,2,3,1]
[2,1,3,2,1,1,3,3,2,2,3,1,2,3,1,3,3,1,3,2,3,2]
[2,1,3,2,1,1,3,3,2,2,3,1,2,3,1,3,3,1,3,2,3,3]
[2,1,3,2,1,1,3,3,2,2,3,1,3,3,1,1,3,1,3,2,3,1]
[2,1,3,2,1,1,3,3,2,2,3,1,3,3,1,1,3,1,3,2,3,2]
[2,1,3,2,1,1,3,3,2,2,3,1,3,3,1,1,3,1,3,2,3,3]
[2,1,3,2,1,1,3,3,2,2,3,1,3,3,1,2,3,1,3,2,3,1]
[2,1,3,2,1,1,3,3,2,2,3,1,3,3,1,2,3,1,3,2,3,2]
[2,1,3,2,1,1,3,3,2,2,3,1,3,3,1,2,3,1,3,2,3,3]
[2,1,3,2,1,1,3,3,2,2,3,1,3,3,1,3,3,1,3,2,3,1]
[2,1,3,2,1,1,3,3,2,2,3,1,3,3,1,3,3,1,3,2,3,2]
[2,1,3,2,1,1,3,3,2,2,3,1,3,3,1,3,3,1,3,2,3,3]
[2,1,3,2,1,1,3,3,2,2,3,2,1,3,1,1,3,1,3,2,3,1]
[2,1,3,2,1,1,3,3,2,2,3,2,1,3,1,1,3,1,3,2,3,2]
[2,1,3,2,1,1,3,3,2,2,3,2,1,3,1,1,3,1,3,2,3,3]
[2,1,3,2,1,1,3,3,2,2,3,2,1,3,1,2,3,1,3,2,3,1]
[2,1,3,2,1,1,3,3,2,2,3,2,1,3,1,2,3,1,3,2,3,2]
[2,1,3,2,1,1,3,3,2,2,3,2,1,3,1,2,3,1,3,2,3,3]
[2,1,3,2,1,1,3,3,2,2,3,2,1,3,1,3,3,1,3,2,3,1]
[2,1,3,2,1,1,3,3,2,2,3,2,1,3,1,3,3,1,3,2,3,2]
[2,1,3,2,1,1,3,3,2,2,3,2,1,3,1,3,3,1,3,2,3,3]
[2,1,3,2,1,1,3,3,2,2,3,2,2,3,1,1,3,1,3,2,3,1]
[2,1,3,2,1,1,3,3,2,2,3,2,2,3,1,1,3,1,3,2,3,2]
[2,1,3,2,1,1,3,3,2,2,3,2,2,3,1,1,3,1,3,2,3,3]
[2,1,3,2,1,1,3,3,2,2,3,2,2,3,1,2,3,1,3,2,3,1]
[2,1,3,2,1,1,3,3,2,2,3,2,2,3,1,2,3,1,3,2,3,2]
[2,1,3,2,1,1,3,3,2,2,3,2,2,3,1,2,3,1,3,2,3,3]
[2,1,3,2,1,1,3,3,2,2,3,2,2,3,1,3,3,1,3,2,3,1]
[2,1,3,2,1,1,3,3,2,2,3,2,2,3,1,3,3,1,3,2,3,2]
[2,1,3,2,1,1,3,3,2,2,3,2,2,3,1,3,3,1,3,2,3,3]
[2,1,3,2,1,1,3,3,2,2,3,2,3,3,1,1,3,1,3,2,3,1]
[2,1,3,2,1,1,3,3,2,2,3,2,3,3,1,1,3,1,3,2,3,2]
[2,1,3,2,1,1,3,3,2,2,3,2,3,3,1,1,3,1,3,2,3,3]
[2,1,3,2,1,1,3,3,2,2,3,2,3,3,1,2,3,1,3,2,3,1]
[2,1,3,2,1,1,3,3,2,2,3,2,3,3,1,2,3,1,3,2,3,2]
[2,1,3,2,1,1,3,3,2,2,3,2,3,3,1,2,3,1,3,2,3,3]
[2,1,3,2,1,1,3,3,2,2,3,2,3,3,1,3,3,1,3,2,3,1]
[2,1,3,2,1,1,3,3,2,2,3,2,3,3,1,3,3,1,3,2,3,2]
[2,1,3,2,1,1,3,3,2,2,3,2,3,3,1,3,3,1,3,2,3,3]
[2,1,3,2,1,1,3,3,2,2,3,3,1,3,1,1,3,1,3,2,3,1]
[2,1,3,2,1,1,3,3,2,2,3,3,1,3,1,1,3,1,3,2,3,2]
[2,1,3,2,1,1,3,3,2,2,3,3,1,3,1,1,3,1,3,2,3,3]
[2,1,3,2,1,1,3,3,2,2,3,3,1,3,1,2,3,1,3,2,3,1]
[2,1,3,2,1,1,3,3,2,2,3,3,1,3,1,2,3,1,3,2,3,2]
[2,1,3,2,1,1,3,3,2,2,3,3,1,3,1,2,3,1,3,2,3,3]
[2,1,3,2,1,1,3,3,2,2,3,3,1,3,1,3,3,1,3,2,3,1]
[2,1,3,2,1,1,3,3,2,2,3,3,1,3,1,3,3,1,3,2,3,2]
[2,1,3,2,1,1,3,3,2,2,3,3,1,3,1,3,3,1,3,2,3,3]
[2,1,3,2,1,1,3,3,2,2,3,3,2,3,1,1,3,1,3,2,3,1]
[2,1,3,2,1,1,3,3,2,2,3,3,2,3,1,1,3,1,3,2,3,2]
[2,1,3,2,1,1,3,3,2,2,3,3,2,3,1,1,3,1,3,2,3,3]
[2,1,3,2,1,1,3,3,2,2,3,3,2,3,1,2,3,1,3,2,3,1]
[2,1,3,2,1,1,3,3,2,2,3,3,2,3,1,2,3,1,3,2,3,2]
[2,1,3,2,1,1,3,3,2,2,3,3,2,3,1,2,3,1,3,2,3,3]
[2,1,3,2,1,1,3,3,2,2,3,3,2,3,1,3,3,1,3,2,3,1]
[2,1,3,2,1,1,3,3,2,2,3,3,2,3,1,3,3,1,3,2,3,2]
[2,1,3,2,1,1,3,3,2,2,3,3,2,3,1,3,3,1,3,2,3,3]
[2,1,3,2,1,1,3,3,2,2,3,3,3,3,1,1,3,1,3,2,3,1]
[2,1,3,2,1,1,3,3,2,2,3,3,3,3,1,1,3,1,3,2,3,2]
[2,1,3,2,1,1,3,3,2,2,3,3,3,3,1,1,3,1,3,2,3,3]
[2,1,3,2,1,1,3,3,2,2,3,3,3,3,1,2,3,1,3,2,3,1]
[2,1,3,2,1,1,3,3,2,2,3,3,3,3,1,2,3,1,3,2,3,2]
[2,1,3,2,1,1,3,3,2,2,3,3,3,3,1,2,3,1,3,2,3,3]
[2,1,3,2,1,1,3,3,2,2,3,3,3,3,1,3,3,1,3,2,3,1]
[2,1,3,2,1,1,3,3,2,2,3,3,3,3,1,3,3,1,3,2,3,2]
[2,1,3,2,1,1,3,3,2,2,3,3,3,3,1,3,3,1,3,2,3,3]
[2,1,3,2,2,1,3,3,2,2,3,1,1,3,1,1,3,1,3,2,3,1]
[2,1,3,2,2,1,3,3,2,2,3,1,1,3,1,1,3,1,3,2,3,2]
[2,1,3,2,2,1,3,3,2,2,3,1,1,3,1,1,3,1,3,2,3,3]
[2,1,3,2,2,1,3,3,2,2,3,1,1,3,1,2,3,1,3,2,3,1]
[2,1,3,2,2,1,3,3,2,2,3,1,1,3,1,2,3,1,3,2,3,2]
[2,1,3,2,2,1,3,3,2,2,3,1,1,3,1,2,3,1,3,2,3,3]
[2,1,3,2,2,1,3,3,2,2,3,1,1,3,1,3,3,1,3,2,3,1]
[2,1,3,2,2,1,3,3,2,2,3,1,1,3,1,3,3,1,3,2,3,2]
[2,1,3,2,2,1,3,3,2,2,3,1,1,3,1,3,3,1,3,2,3,3]
[2,1,3,2,2,1,3,3,2,2,3,1,2,3,1,1,3,1,3,2,3,1]
[2,1,3,2,2,1,3,3,2,2,3,1,2,3,1,1,3,1,3,2,3,2]
[2,1,3,2,2,1,3,3,2,2,3,1,2,3,1,1,3,1,3,2,3,3]
[2,1,3,2,2,1,3,3,2,2,3,1,2,3,1,2,3,1,3,2,3,1]
[2,1,3,2,2,1,3,3,2,2,3,1,2,3,1,2,3,1,3,2,3,2]
[2,1,3,2,2,1,3,3,2,2,3,1,2,3,1,2,3,1,3,2,3,3]
[2,1,3,2,2,1,3,3,2,2,3,1,2,3,1,3,3,1,3,2,3,1]
[2,1,3,2,2,1,3,3,2,2,3,1,2,3,1,3,3,1,3,2,3,2]
[2,1,3,2,2,1,3,3,2,2,3,1,2,3,1,3,3,1,3,2,3,3]
[2,1,3,2,2,1,3,3,2,2,3,1,3,3,1,1,3,1,3,2,3,1]
[2,1,3,2,2,1,3,3,2,2,3,1,3,3,1,1,3,1,3,2,3,2]
[2,1,3,2,2,1,3,3,2,2,3,1,3,3,1,1,3,1,3,2,3,3]
[2,1,3,2,2,1,3,3,2,2,3,1,3,3,1,2,3,1,3,2,3,1]
[2,1,3,2,2,1,3,3,2,2,3,1,3,3,1,2,3,1,3,2,3,2]
[2,1,3,2,2,1,3,3,2,2,3,1,3,3,1,2,3,1,3,2,3,3]
[2,1,3,2,2,1,3,3,2,2,3,1,3,3,1,3,3,1,3,2,3,1]
[2,1,3,2,2,1,3,3,2,2,3,1,3,3,1,3,3,1,3,2,3,2]
[2,1,3,2,2,1,3,3,2,2,3,1,3,3,1,3,3,1,3,2,3,3]
[2,1,3,2,2,1,3,3,2,2,3,2,1,3,1,1,3,1,3,2,3,1]
[2,1,3,2,2,1,3,3,2,2,3,2,1,3,1,1,3,1,3,2,3,2]
[2,1,3,2,2,1,3,3,2,2,3,2,1,3,1,1,3,1,3,2,3,3]
[2,1,3,2,2,1,3,3,2,2,3,2,1,3,1,2,3,1,3,2,3,1]
[2,1,3,2,2,1,3,3,2,2,3,2,1,3,1,2,3,1,3,2,3,2]
[2,1,3,2,2,1,3,3,2,2,3,2,1,3,1,2,3,1,3,2,3,3]
[2,1,3,2,2,1,3,3,2,2,3,2,1,3,1,3,3,1,3,2,3,1]
[2,1,3,2,2,1,3,3,2,2,3,2,1,3,1,3,3,1,3,2,3,2]
[2,1,3,2,2,1,3,3,2,2,3,2,1,3,1,3,3,1,3,2,3,3]
[2,1,3,2,2,1,3,3,2,2,3,2,2,3,1,1,3,1,3,2,3,1]
[2,1,3,2,2,1,3,3,2,2,3,2,2,3,1,1,3,1,3,2,3,2]
[2,1,3,2,2,1,3,3,2,2,3,2,2,3,1,1,3,1,3,2,3,3]
[2,1,3,2,2,1,3,3,2,2,3,2,2,3,1,2,3,1,3,2,3,1]
[2,1,3,2,2,1,3,3,2,2,3,2,2,3,1,2,3,1,3,2,3,2]
[2,1,3,2,2,1,3,3,2,2,3,2,2,3,1,2,3,1,3,2,3,3]
[2,1,3,2,2,1,3,3,2,2,3,2,2,3,1,3,3,1,3,2,3,1]
[2,1,3,2,2,1,3,3,2,2,3,2,2,3,1,3,3,1,3,2,3,2]
[2,1,3,2,2,1,3,3,2,2,3,2,2,3,1,3,3,1,3,2,3,3]
[2,1,3,2,2,1,3,3,2,2,3,2,3,3,1,1,3,1,3,2,3,1]
[2,1,3,2,2,1,3,3,2,2,3,2,3,3,1,1,3,1,3,2,3,2]
[2,1,3,2,2,1,3,3,2,2,3,2,3,3,1,1,3,1,3,2,3,3]
[2,1,3,2,2,1,3,3,2,2,3,2,3,3,1,2,3,1,3,2,3,1]
[2,1,3,2,2,1,3,3,2,2,3,2,3,3,1,2,3,1,3,2,3,2]
[2,1,3,2,2,1,3,3,2,2,3,2,3,3,1,2,3,1,3,2,3,3]
[2,1,3,2,2,1,3,3,2,2,3,2,3,3,1,3,3,1,3,2,3,1]
[2,1,3,2,2,1,3,3,2,2,3,2,3,3,1,3,3,1,3,2,3,2]
[2,1,3,2,2,1,3,3,2,2,3,2,3,3,1,3,3,1,3,2,3,3]
[2,1,3,2,2,1,3,3,2,2,3,3,1,3,1,1,3,1,3,2,3,1]
[2,1,3,2,2,1,3,3,2,2,3,3,1,3,1,1,3,1,3,2,3,2]
[2,1,3,2,2,1,3,3,2,2,3,3,1,3,1,1,3,1,3,2,3,3]
[2,1,3,2,2,1,3,3,2,2,3,3,1,3,1,2,3,1,3,2,3,1]
[2,1,3,2,2,1,3,3,2,2,3,3,1,3,1,2,3,1,3,2,3,2]
[2,1,3,2,2,1,3,3,2,2,3,3,1,3,1,2,3,1,3,2,3,3]
[2,1,3,2,2,1,3,3,2,2,3,3,1,3,1,3,3,1,3,2,3,1]
[2,1,3,2,2,1,3,3,2,2,3,3,1,3,1,3,3,1,3,2,3,2]
[2,1,3,2,2,1,3,3,2,2,3,3,1,3,1,3,3,1,3,2,3,3]
[2,1,3,2,2,1,3,3,2,2,3,3,2,3,1,1,3,1,3,2,3,1]
[2,1,3,2,2,1,3,3,2,2,3,3,2,3,1,1,3,1,3,2,3,2]
[2,1,3,2,2,1,3,3,2,2,3,3,2,3,1,1,3,1,3,2,3,3]
[2,1,3,2,2,1,3,3,2,2,3,3,2,3,1,2,3,1,3,2,3,1]
[2,1,3,2,2,1,3,3,2,2,3,3,2,3,1,2,3,1,3,2,3,2]
[2,1,3,2,2,1,3,3,2,2,3,3,2,3,1,2,3,1,3,2,3,3]
[2,1,3,2,2,1,3,3,2,2,3,3,2,3,1,3,3,1,3,2,3,1]
[2,1,3,2,2,1,3,3,2,2,3,3,2,3,1,3,3,1,3,2,3,2]
[2,1,3,2,2,1,3,3,2,2,3,3,2,3,1,3,3,1,3,2,3,3]
[2,1,3,2,2,1,3,3,2,2,3,3,3,3,1,1,3,1,3,2,3,1]
[2,1,3,2,2,1,3,3,2,2,3,3,3,3,1,1,3,1,3,2,3,2]
[2,1,3,2,2,1,3,3,2,2,3,3,3,3,1,1,3,1,3,2,3,3]
[2,1,3,2,2,1,3,3,2,2,3,3,3,3,1,2,3,1,3,2,3,1]
[2,1,3,2,2,1,3,3,2,2,3,3,3,3,1,2,3,1,3,2,3,2]
[2,1,3,2,2,1,3,3,2,2,3,3,3,3,1,2,3,1,3,2,3,3]
[2,1,3,2,2,1,3,3,2,2,3,3,3,3,1,3,3,1,3,2,3,1]
[2,1,3,2,2,1,3,3,2,2,3,3,3,3,1,3,3,1,3,2,3,2]
[2,1,3,2,2,1,3,3,2,2,3,3,3,3,1,3,3,1,3,2,3,3]
[2,1,3,2,3,1,3,3,2,2,3,1,1,3,1,1,3,1,3,2,3,1]
[2,1,3,2,3,1,3,3,2,2,3,1,1,3,1,1,3,1,3,2,3,2]
[2,1,3,2,3,1,3,3,2,2,3,1,1,3,1,1,3,1,3,2,3,3]
[2,1,3,2,3,1,3,3,2,2,3,1,1,3,1,2,3,1,3,2,3,1]
[2,1,3,2,3,1,3,3,2,2,3,1,1,3,1,2,3,1,3,2,3,2]
[2,1,3,2,3,1,3,3,2,2,3,1,1,3,1,2,3,1,3,2,3,3]
[2,1,3,2,3,1,3,3,2,2,3,1,1,3,1,3,3,1,3,2,3,1]
[2,1,3,2,3,1,3,3,2,2,3,1,1,3,1,3,3,1,3,2,3,2]
[2,1,3,2,3,1,3,3,2,2,3,1,1,3,1,3,3,1,3,2,3,3]
[2,1,3,2,3,1,3,3,2,2,3,1,2,3,1,1,3,1,3,2,3,1]
[2,1,3,2,3,1,3,3,2,2,3,1,2,3,1,1,3,1,3,2,3,2]
[2,1,3,2,3,1,3,3,2,2,3,1,2,3,1,1,3,1,3,2,3,3]
[2,1,3,2,3,1,3,3,2,2,3,1,2,3,1,2,3,1,3,2,3,1]
[2,1,3,2,3,1,3,3,2,2,3,1,2,3,1,2,3,1,3,2,3,2]
[2,1,3,2,3,1,3,3,2,2,3,1,2,3,1,2,3,1,3,2,3,3]
[2,1,3,2,3,1,3,3,2,2,3,1,2,3,1,3,3,1,3,2,3,1]
[2,1,3,2,3,1,3,3,2,2,3,1,2,3,1,3,3,1,3,2,3,2]
[2,1,3,2,3,1,3,3,2,2,3,1,2,3,1,3,3,1,3,2,3,3]
[2,1,3,2,3,1,3,3,2,2,3,1,3,3,1,1,3,1,3,2,3,1]
[2,1,3,2,3,1,3,3,2,2,3,1,3,3,1,1,3,1,3,2,3,2]
[2,1,3,2,3,1,3,3,2,2,3,1,3,3,1,1,3,1,3,2,3,3]
[2,1,3,2,3,1,3,3,2,2,3,1,3,3,1,2,3,1,3,2,3,1]
[2,1,3,2,3,1,3,3,2,2,3,1,3,3,1,2,3,1,3,2,3,2]
[2,1,3,2,3,1,3,3,2,2,3,1,3,3,1,2,3,1,3,2,3,3]
[2,1,3,2,3,1,3,3,2,2,3,1,3,3,1,3,3,1,3,2,3,1]
[2,1,3,2,3,1,3,3,2,2,3,1,3,3,1,3,3,1,3,2,3,2]
[2,1,3,2,3,1,3,3,2,2,3,1,3,3,1,3,3,1,3,2,3,3]
[2,1,3,2,3,1,3,3,2,2,3,2,1,3,1,1,3,1,3,2,3,1]
[2,1,3,2,3,1,3,3,2,2,3,2,1,3,1,1,3,1,3,2,3,2]
[2,1,3,2,3,1,3,3,2,2,3,2,1,3,1,1,3,1,3,2,3,3]
[2,1,3,2,3,1,3,3,2,2,3,2,1,3,1,2,3,1,3,2,3,1]
[2,1,3,2,3,1,3,3,2,2,3,2,1,3,1,2,3,1,3,2,3,2]
[2,1,3,2,3,1,3,3,2,2,3,2,1,3,1,2,3,1,3,2,3,3]
[2,1,3,2,3,1,3,3,2,2,3,2,1,3,1,3,3,1,3,2,3,1]
[2,1,3,2,3,1,3,3,2,2,3,2,1,3,1,3,3,1,3,2,3,2]
[2,1,3,2,3,1,3,3,2,2,3,2,1,3,1,3,3,1,3,2,3,3]
[2,1,3,2,3,1,3,3,2,2,3,2,2,3,1,1,3,1,3,2,3,1]
[2,1,3,2,3,1,3,3,2,2,3,2,2,3,1,1,3,1,3,2,3,2]
[2,1,3,2,3,1,3,3,2,2,3,2,2,3,1,1,3,1,3,2,3,3]
[2,1,3,2,3,1,3,3,2,2,3,2,2,3,1,2,3,1,3,2,3,1]
[2,1,3,2,3,1,3,3,2,2,3,2,2,3,1,2,3,1,3,2,3,2]
[2,1,3,2,3,1,3,3,2,2,3,2,2,3,1,2,3,1,3,2,3,3]
[2,1,3,2,3,1,3,3,2,2,3,2,2,3,1,3,3,1,3,2,3,1]
[2,1,3,2,3,1,3,3,2,2,3,2,2,3,1,3,3,1,3,2,3,2]
[2,1,3,2,3,1,3,3,2,2,3,2,2,3,1,3,3,1,3,2,3,3]
[2,1,3,2,3,1,3,3,2,2,3,2,3,3,1,1,3,1,3,2,3,1]
[2,1,3,2,3,1,3,3,2,2,3,2,3,3,1,1,3,1,3,2,3,2]
[2,1,3,2,3,1,3,3,2,2,3,2,3,3,1,1,3,1,3,2,3,3]
[2,1,3,2,3,1,3,3,2,2,3,2,3,3,1,2,3,1,3,2,3,1]
[2,1,3,2,3,1,3,3,2,2,3,2,3,3,1,2,3,1,3,2,3,2]
[2,1,3,2,3,1,3,3,2,2,3,2,3,3,1,2,3,1,3,2,3,3]
[2,1,3,2,3,1,3,3,2,2,3,2,3,3,1,3,3,1,3,2,3,1]
[2,1,3,2,3,1,3,3,2,2,3,2,3,3,1,3,3,1,3,2,3,2]
[2,1,3,2,3,1,3,3,2,2,3,2,3,3,1,3,3,1,3,2,3,3]
[2,1,3,2,3,1,3,3,2,2,3,3,1,3,1,1,3,1,3,2,3,1]
[2,1,3,2,3,1,3,3,2,2,3,3,1,3,1,1,3,1,3,2,3,2]
[2,1,3,2,3,1,3,3,2,2,3,3,1,3,1,1,3,1,3,2,3,3]
[2,1,3,2,3,1,3,3,2,2,3,3,1,3,1,2,3,1,3,2,3,1]
[2,1,3,2,3,1,3,3,2,2,3,3,1,3,1,2,3,1,3,2,3,2]
[2,1,3,2,3,1,3,3,2,2,3,3,1,3,1,2,3,1,3,2,3,3]
[2,1,3,2,3,1,3,3,2,2,3,3,1,3,1,3,3,1,3,2,3,1]
[2,1,3,2,3,1,3,3,2,2,3,3,1,3,1,3,3,1,3,2,3,2]
[2,1,3,2,3,1,3,3,2,2,3,3,1,3,1,3,3,1,3,2,3,3]
[2,1,3,2,3,1,3,3,2,2,3,3,2,3,1,1,3,1,3,2,3,1]
[2,1,3,2,3,1,3,3,2,2,3,3,2,3,1,1,3,1,3,2,3,2]
[2,1,3,2,3,1,3,3,2,2,3,3,2,3,1,1,3,1,3,2,3,3]
[2,1,3,2,3,1,3,3,2,2,3,3,2,3,1,2,3,1,3,2,3,1]
[2,1,3,2,3,1,3,3,2,2,3,3,2,3,1,2,3,1,3,2,3,2]
[2,1,3,2,3,1,3,3,2,2,3,3,2,3,1,2,3,1,3,2,3,3]
[2,1,3,2,3,1,3,3,2,2,3,3,2,3,1,3,3,1,3,2,3,1]
[2,1,3,2,3,1,3,3,2,2,3,3,2,3,1,3,3,1,3,2,3,2]
[2,1,3,2,3,1,3,3,2,2,3,3,2,3,1,3,3,1,3,2,3,3]
[2,1,3,2,3,1,3,3,2,2,3,3,3,3,1,1,3,1,3,2,3,1]
[2,1,3,2,3,1,3,3,2,2,3,3,3,3,1,1,3,1,3,2,3,2]
[2,1,3,2,3,1,3,3,2,2,3,3,3,3,1,1,3,1,3,2,3,3]
[2,1,3,2,3,1,3,3,2,2,3,3,3,3,1,2,3,1,3,2,3,1]
[2,1,3,2,3,1,3,3,2,2,3,3,3,3,1,2,3,1,3,2,3,2]
[2,1,3,2,3,1,3,3,2,2,3,3,3,3,1,2,3,1,3,2,3,3]
[2,1,3,2,3,1,3,3,2,2,3,3,3,3,1,3,3,1,3,2,3,1]
[2,1,3,2,3,1,3,3,2,2,3,3,3,3,1,3,3,1,3,2,3,2]
[2,1,3,2,3,1,3,3,2,2,3,3,3,3,1,3,3,1,3,2,3,3]

 */ 
