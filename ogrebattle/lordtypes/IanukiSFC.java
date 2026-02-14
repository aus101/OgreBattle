package ogrebattle.lordtypes;

import static ogrebattle.tarot.pojo.TarotSorting.*;

import java.util.TreeSet;

/**
 * Ice Cloud Original SFC lord type has 2 sets of answers that yield the same optimal rate of<br>
 * 74603 out of 74613 (99.99%). Here they are constructed and returned.<br>
 */
public class IanukiSFC extends LordType {
	public IanukiSFC() {
		if (!INIT) {
			INIT = true;
			solutions = new TreeSet<int[]>(new IntArrayComparator());

			solutions.add(new int[]{2,1,1,2,3,3,2,2,1,2,2,3,1,2,1,2,3,2,1,2,2,2});//base and 6 ones
			solutions.add(new int[]{1,2,3,2,2,3,2,2,1,2,2,3,1,2,1,2,3,2,1,2,2,2});
		}
	}
	
	public static void main(String[] args) {
		IanukiSFC ian = new IanukiSFC();
		ian.printSolutions();
	}
}
/*
Only cards at indices 0, 1,  2 and 4 change aka Magician, Priestess, Empress, Hierophant

Count: 2
{1,2,3,2,2,3,2,2,1,2,2,3,1,2,1,2,3,2,1,2,2,2};
{2,1,1,2,3,3,2,2,1,2,2,3,1,2,1,2,3,2,1,2,2,2};
*/
