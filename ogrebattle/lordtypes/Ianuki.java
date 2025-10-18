package ogrebattle.lordtypes;

import static ogrebattle.tarot.pojo.TarotSorting.*;

import java.util.TreeSet;

/**
 * Ianuki lord type has 8 sets of answers that yield the same optimal rate of<br>
 * 74603 out of 74613 (99.99%). Here they are constructed and returned.<br>
 */
public class Ianuki extends LordType {
	public static final int[] BASE = new int[]{1,1,2,1,2,2,3,2,2,1,2,2,3,1,2,1,2,3,2,1,2,2};
	
	public Ianuki() {
		if (!INIT) {
			INIT = true;
			solutions = new TreeSet<int[]>(new IntArrayComparator());
			
			solutions.add(new int[]{1,1,2,1,2,2,3,2,2,1,2,2,3,1,2,1,2,3,2,1,2,2});//base and 7 ones
			solutions.add(new int[]{1,1,2,1,3,2,3,2,2,1,2,2,3,1,2,1,2,3,2,1,2,2});//also 7 ones
			solutions.add(new int[]{1,1,2,1,2,2,3,2,2,1,2,2,3,1,2,1,2,3,2,3,2,2});
	        
			solutions.add(new int[]{1,1,2,1,3,2,3,2,2,1,2,2,3,1,2,1,2,3,2,3,2,2});
	        solutions.add(new int[]{2,1,2,1,2,2,3,2,2,1,2,2,3,1,2,1,2,3,2,1,2,2});
	        solutions.add(new int[]{2,1,2,1,2,2,3,2,2,1,2,2,3,1,2,1,2,3,2,3,2,2});
	        
	        solutions.add(new int[]{2,1,2,1,3,2,3,2,2,1,2,2,3,1,2,1,2,3,2,3,2,2});
	        solutions.add(new int[]{2,1,2,1,3,2,3,2,2,1,2,2,3,1,2,1,2,3,2,1,2,2});
		}
	}
	
	public static int[] getBase() {
		return BASE;
	}
	
	public static int[] getBaseDeepCopy() {
		int[] temp = new int[CARDS];
		System.arraycopy(BASE, 0, temp, 0, CARDS);
		return temp;
	}
	
	@Override
	protected int countDifferences(int[] found) {
		return(countDifferences(found, BASE));
	}
	
	public static void main(String[] args) {
		Ianuki ian = new Ianuki();
		ian.printSolutions();
	}
}
/*
Only cards at indices 0, 4, 19 change aka Magician, Hierophant, Judgment

Count: 8
{1,1,2,1,2,2,3,2,2,1,2,2,3,1,2,1,2,3,2,1,2,2};
{1,1,2,1,2,2,3,2,2,1,2,2,3,1,2,1,2,3,2,3,2,2};
{1,1,2,1,3,2,3,2,2,1,2,2,3,1,2,1,2,3,2,1,2,2};
{1,1,2,1,3,2,3,2,2,1,2,2,3,1,2,1,2,3,2,3,2,2};
{2,1,2,1,2,2,3,2,2,1,2,2,3,1,2,1,2,3,2,1,2,2};
{2,1,2,1,2,2,3,2,2,1,2,2,3,1,2,1,2,3,2,3,2,2};
{2,1,2,1,3,2,3,2,2,1,2,2,3,1,2,1,2,3,2,1,2,2};
{2,1,2,1,3,2,3,2,2,1,2,2,3,1,2,1,2,3,2,3,2,2};
*/
