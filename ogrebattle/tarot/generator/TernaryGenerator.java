package ogrebattle.tarot.generator;

import java.util.Arrays;

import ogrebattle.printer.Util;

/**
 * 3 answers to each Tarot card question is a base 3 system. Can generate all possible answer sets by iterating in base 3 and<br>
 * adding an offset of 1. Will work totally fine for base 2 and up but only <code>0-9</code> are used to represent > base 10.<br> 
 * Little and Big Endian offered. Generator limit is <code>3^16</code> when allocating a matrix of <code>[3^digits][22]</code>
 * ints with 43 million rows<br>versus the Tarot cards dealt problem of <code>COMBIN(22,7) = 170544</code> lists holding sets of 7.
 * The reasoning is answer sets are<br>permutations with order for all 22 cards compared to holding 7 or less cards in any order.<br>
 * Is a considerably easier computer science problem to iterate/count where order matters despite the exponentially larger results.<br>
 * Ternary comes from Latin <i>ternarius</i>, meaning "consisting of three". Gray code would be a nice expansion.
 */
public class TernaryGenerator {
	public static final int BASE = 3;//bases > 10 represented with more than 1 digit given 0-9 are the numbers
	
	public static void main(String[] args) {
		System.out.println("little endian base " + BASE + " to 3 significant digits:");
		
		System.out.println(Util.matrixStringBuilder(
				littleEndianGenerator(3)));
		
		int offset = 5;//works fine with negative numbers
		
		System.out.println("big endian base " + BASE + " with +" + offset + " offset to 4 significant digits:");
		
		System.out.println(Util.matrixStringBuilder(
				bigEndianGenerator(4, offset)));
			
		int exponent = 13;
	
		long start = System.nanoTime();
		bigEndianGenerator(exponent, 1);
		long end = System.nanoTime();
		
		System.out.println(exponent + ": " + (double) (end - start) / Util.NANOSECONDS_IN_1_SECOND + " seconds with base " + BASE
				+ " to generate all " + Util.numberSeparator((int) Math.pow(BASE, exponent)) +" arrays of length "
				+ exponent + ", i.e. 3^" + exponent);	
	}
/*
        13: 0.3135666 seconds with base 3 to generate all 1,594,323 arrays of length 13, i.e. 3^13      
        14: 0.7358106 seconds with base 3 to generate all 4,782,969 arrays of length 14, i.e. 3^14		
		15: 2.1317401 seconds with base 3 to generate all 14,348,907 arrays of length 15, i.e. 3^15	
		16: 7.4439947 seconds with base 3 to generate all 43,046,721 arrays of length 16, i.e. 3^16
		
		17, 18, 20:
		Exception in thread "main" java.lang.OutOfMemoryError: Java heap space
		at TTT/ogrebattle.tarot.exact.TernaryGenerator.littleEndianGenerator(TernaryGenerator.java:18)
		at TTT/ogrebattle.tarot.exact.TernaryGenerator.main(TernaryGenerator.java:79)
		
		20+:
		Exception in thread "main" java.lang.OutOfMemoryError: Requested array size exceeds VM limit
		at TTT/ogrebattle.tarot.exact.TernaryGenerator.littleEndianGenerator(TernaryGenerator.java:18)
		at TTT/ogrebattle.tarot.exact.TernaryGenerator.main(TernaryGenerator.java:79)
*/
	public static int[][] littleEndianGenerator(int BASE_3_DIGITS) {
		final int TOTAL = (int) Math.pow(BASE, BASE_3_DIGITS);
		int[][] solution = new int[TOTAL][BASE_3_DIGITS];		
		final int[] holder = new int[BASE_3_DIGITS];//final arrays can be modified, just not reallocated with new =

		for (int i=1; i<TOTAL; i++) {//i=1 to preserve all zeroes array as the first entry
			for (int j=0; j<BASE_3_DIGITS; j++) {
				if (holder[j] < (BASE-1)) {//< 2 for base 3
					holder[j]++;
					break;
				} else {
					holder[j] = 0;
				}
			}
			System.arraycopy(holder, 0, solution[i], 0, BASE_3_DIGITS);
		}
		return solution;
	}
	
	//easy to integrate into other generator but OFFSET of 0 has about a 7% performance hit, even checking for 0 before filling
	public static int[][] littleEndianGenerator(int BASE_3_DIGITS, int OFFSET) {
		final int TOTAL = (int) Math.pow(BASE, BASE_3_DIGITS);
		int[][] solution = new int[TOTAL][BASE_3_DIGITS];
		final int[] holder = new int[BASE_3_DIGITS];//final arrays can be modified, just not reallocated with new =
		
		Arrays.fill(holder, OFFSET);//initialize holder with offset
		for(int[] array : solution)
			System.arraycopy(holder, 0, array, 0, BASE_3_DIGITS);//the i=0
//      seems slightly faster to fill once then copy n times, versus fill n times		
//		if (OFFSET != 0) {
//			for(int[] array : test)
//				Arrays.fill(array, OFFSET);
//		}
		
		for (int i=1; i<TOTAL; i++) {//i=1 to preserve the zeroes + OFFSET array as the first entry
			for (int j=0; j<BASE_3_DIGITS; j++) {
				if (holder[j] < (BASE + OFFSET - 1)) {//< 2 for base 3
					holder[j]++;
					break;
				} else {
					holder[j] = OFFSET;
				}
			}
			System.arraycopy(holder, 0, solution[i], 0, BASE_3_DIGITS);
		}
		return solution;
	}

	public static int[][] bigEndianGenerator(int BASE_3_DIGITS) {
		final int TOTAL = (int) Math.pow(BASE, BASE_3_DIGITS);
		int[][] solution = new int[TOTAL][BASE_3_DIGITS];
		final int[] holder = new int[BASE_3_DIGITS];//final arrays can be modified, just not reallocated with new =
		
		for (int i=TOTAL-1; i>0; i--) {
			for(int j=BASE_3_DIGITS-1; j>=0; j--) {//-1 to preserve all zeroes array as the first entry
				if (holder[j] < (BASE - 1)) {// < 2 for base 3
					holder[j]++;
					break;
				} else {
					holder[j] = 0;
				}
			}//TOTAL-i where i starts at TOTAL-1 preserves the zeroes + OFFSET array as the first entry
			System.arraycopy(holder, 0, solution[TOTAL - i], 0, BASE_3_DIGITS);
		}
		return solution;
	}
	
	//easy to integrate into other generator but OFFSET of 0 has about a 7% performance hit, even checking for 0 before filling
	public static int[][] bigEndianGenerator(int BASE_3_DIGITS, int OFFSET) {
		final int TOTAL = (int) Math.pow(BASE, BASE_3_DIGITS);
		int[][] solution = new int[TOTAL][BASE_3_DIGITS];
		final int[] holder = new int[BASE_3_DIGITS];//final arrays can be modified, just not reallocated with new =
		
		Arrays.fill(holder, OFFSET);//initialize holder with offset
		for(int[] array : solution)
			System.arraycopy(holder, 0, array, 0, BASE_3_DIGITS);
//      seems slightly faster to fill once then copy n times, versus fill n times		
//		if (OFFSET != 0) {
//			for(int[] array : test)
//				Arrays.fill(array, OFFSET);
//		}
		
		for (int i=TOTAL-1; i>0; i--) {
			for(int j=BASE_3_DIGITS-1; j>=0; j--) {
				if (holder[j] < (BASE + OFFSET - 1)) {// < 2 for base 3
					holder[j]++;
					break;
				} else {
					holder[j] = OFFSET;
				}
			}//TOTAL-i where i starts at TOTAL-1 preserves the zeroes + OFFSET array as the first entry
			System.arraycopy(holder, 0, solution[TOTAL - i], 0, BASE_3_DIGITS);
		}
		return solution;
	}
	
	public void substituteValues(int[] solution, int[] solutionIndices, int[] values) {
		
	}
}
/*
little endian base 3 to 3 significant digits:
[0,0,0]
[1,0,0]
[2,0,0]
[0,1,0]
[1,1,0]
[2,1,0]
[0,2,0]
[1,2,0]
[2,2,0]
[0,0,1]
[1,0,1]
[2,0,1]
[0,1,1]
[1,1,1]
[2,1,1]
[0,2,1]
[1,2,1]
[2,2,1]
[0,0,2]
[1,0,2]
[2,0,2]
[0,1,2]
[1,1,2]
[2,1,2]
[0,2,2]
[1,2,2]
[2,2,2]

big endian base 3 with +5 offset to 4 significant digits:
[5,5,5,5]
[5,5,5,6]
[5,5,5,7]
[5,5,6,5]
[5,5,6,6]
[5,5,6,7]
[5,5,7,5]
[5,5,7,6]
[5,5,7,7]
[5,6,5,5]
[5,6,5,6]
[5,6,5,7]
[5,6,6,5]
[5,6,6,6]
[5,6,6,7]
[5,6,7,5]
[5,6,7,6]
[5,6,7,7]
[5,7,5,5]
[5,7,5,6]
[5,7,5,7]
[5,7,6,5]
[5,7,6,6]
[5,7,6,7]
[5,7,7,5]
[5,7,7,6]
[5,7,7,7]
[6,5,5,5]
[6,5,5,6]
[6,5,5,7]
[6,5,6,5]
[6,5,6,6]
[6,5,6,7]
[6,5,7,5]
[6,5,7,6]
[6,5,7,7]
[6,6,5,5]
[6,6,5,6]
[6,6,5,7]
[6,6,6,5]
[6,6,6,6]
[6,6,6,7]
[6,6,7,5]
[6,6,7,6]
[6,6,7,7]
[6,7,5,5]
[6,7,5,6]
[6,7,5,7]
[6,7,6,5]
[6,7,6,6]
[6,7,6,7]
[6,7,7,5]
[6,7,7,6]
[6,7,7,7]
[7,5,5,5]
[7,5,5,6]
[7,5,5,7]
[7,5,6,5]
[7,5,6,6]
[7,5,6,7]
[7,5,7,5]
[7,5,7,6]
[7,5,7,7]
[7,6,5,5]
[7,6,5,6]
[7,6,5,7]
[7,6,6,5]
[7,6,6,6]
[7,6,6,7]
[7,6,7,5]
[7,6,7,6]
[7,6,7,7]
[7,7,5,5]
[7,7,5,6]
[7,7,5,7]
[7,7,6,5]
[7,7,6,6]
[7,7,6,7]
[7,7,7,5]
[7,7,7,6]
[7,7,7,7]

13: 0.253757 seconds with base 3 to iterate all 1,594,323 arrays of length 13, i.e. 3^13
*/