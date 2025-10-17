package ogrebattle.lordtypes;

import static ogrebattle.tarot.pojo.TarotSorting.*;

import java.util.TreeSet;

/**
 * Ianuki Original SFC lord type has 2 sets of answers that yield the same optimal rate of<br>
 * 74506 out of 74613 (99.86%). Here they are constructed and returned.<br>
 */
public class IanukiSFC extends LordType {
	public static final int[] BASE = new int[]{1,1,2,3,3,2,2,1,2,1,2,2,3,1,2,1,2,3,2,3,2,2};
	
	public IanukiSFC() {
		if (!INIT) {
			INIT = true;
			selection = new TreeSet<int[]>(new IntArrayComparator());

			selection.add(new int[]{1,1,2,3,3,2,2,1,2,1,2,2,3,1,2,1,2,3,2,3,2,2});//base and 6 ones
			selection.add(new int[]{2,2,3,2,2,3,2,2,1,2,2,3,1,2,1,2,3,2,1,2,2,2});//
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
		IanukiSFC ian = new IanukiSFC();
		ian.printSolutions();
	}
}
/*
Count: 2
{1,1,2,3,3,2,2,1,2,1,2,2,3,1,2,1,2,3,2,3,2,2};
{2,2,3,2,2,3,2,2,1,2,2,3,1,2,1,2,3,2,1,2,2,2};
*/
