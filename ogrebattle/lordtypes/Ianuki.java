package ogrebattle.lordtypes;

import static ogrebattle.tarot.pojo.TarotSorting.*;

import java.util.Set;
import java.util.TreeSet;

/**
 * Ianuki lord type has 9 sets of answers that yield the same optimal rate of<br>
 * 74603 out of 74613 (99.99%). Here they are constructed and returned.<br>
 */
public class Ianuki extends LordType {
	public static final int[] BASE = new int[]{1,1,2,1,2,2,3,2,2,1,2,2,3,1,2,1,2,3,2,1,2,2};
	static {
		selection = new TreeSet<int[]>(new IntArrayComparator());
		
		selection.add(new int[]{1,1,2,1,2,2,3,2,2,1,2,2,3,1,2,1,2,3,2,1,2,2});//base and 7 ones
		selection.add(new int[]{1,1,2,1,2,2,3,2,2,1,2,2,3,1,2,1,2,3,2,3,2,2});
		selection.add(new int[]{1,1,2,1,3,2,3,2,2,1,2,2,3,1,2,1,2,3,2,1,2,2});//also 7 ones
        
		selection.add(new int[]{1,1,2,1,3,2,3,2,2,1,2,2,3,1,2,1,2,3,2,3,2,2});
        selection.add(new int[]{2,1,2,1,2,2,3,2,2,1,2,2,3,1,2,1,2,3,2,1,2,2});
        selection.add(new int[]{2,1,2,1,2,2,3,2,2,1,2,2,3,1,2,1,2,3,2,3,2,2});
        
        selection.add(new int[]{2,1,2,1,2,2,3,2,2,1,2,2,3,1,2,2,2,3,2,1,2,2});
        selection.add(new int[]{2,1,2,1,3,2,3,2,2,1,2,2,3,1,2,1,2,3,2,3,2,2});
        selection.add(new int[]{2,1,2,1,3,2,3,2,2,1,2,2,3,1,2,1,2,3,2,1,2,2});
	}

	public static void main(String[] args) {
		Ianuki i = new Ianuki();
		i.printSolutions();
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
	protected int conutDifferences(int[] found) {
		return(conutDifferences(found, BASE));
	}

	public static Set<int[]> returnAllSolutionsSet() {        
		return selection;
	}
}
/*
Only Magician, Emperor, Tower and Judgment change

Count: 9
{1,1,2,1,2,2,3,2,2,1,2,2,3,1,2,1,2,3,2,1,2,2};
{1,1,2,1,2,2,3,2,2,1,2,2,3,1,2,1,2,3,2,3,2,2};
{1,1,2,1,3,2,3,2,2,1,2,2,3,1,2,1,2,3,2,1,2,2};
{1,1,2,1,3,2,3,2,2,1,2,2,3,1,2,1,2,3,2,3,2,2};
{2,1,2,1,2,2,3,2,2,1,2,2,3,1,2,1,2,3,2,1,2,2};
{2,1,2,1,2,2,3,2,2,1,2,2,3,1,2,1,2,3,2,3,2,2};
{2,1,2,1,2,2,3,2,2,1,2,2,3,1,2,2,2,3,2,1,2,2};
{2,1,2,1,3,2,3,2,2,1,2,2,3,1,2,1,2,3,2,1,2,2};
{2,1,2,1,3,2,3,2,2,1,2,2,3,1,2,1,2,3,2,3,2,2};
*/
