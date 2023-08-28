package ogrebattle.lordtypes;

import static ogrebattle.tarot.pojo.TarotSorting.*;

import java.util.Set;
import java.util.TreeSet;

/**
 * Ianuki lord type has 9 sets of answers that yield the same optimal rate of<br>
 * 74603 out of 74613 (99.99%). Here they are constructed and returned.<br>
 */
public class Ianuki extends LordType {
	
	public Ianuki() {
		BASE = new int[]{1,1,2,1,2,2,3,2,2,1,2,2,3,1,2,1,2,3,2,1,2,2};
	}
	
	public static void main(String[] args) {
		Ianuki i = new Ianuki();
		i.printSolutions();
	}

	@Override
	public Set<int[]> returnAllSolutionsSet() {
		TreeSet<int[]> selection = new TreeSet<int[]>(new IntArrayComparator());
		
        selection.add(new int[]{1,1,2,1,2,2,3,2,2,1,2,2,3,1,2,1,2,3,2,1,2,2});//base and 7 ones
        selection.add(new int[]{1,1,2,1,2,2,3,2,2,1,2,2,3,1,2,1,2,3,2,3,2,2});
        selection.add(new int[]{1,1,2,1,3,2,3,2,2,1,2,2,3,1,2,1,2,3,2,1,2,2});//also 7 ones
        
        selection.add(new int[]{1,1,2,1,3,2,3,2,2,1,2,2,3,1,2,1,2,3,2,3,2,2});
        selection.add(new int[]{2,1,2,1,2,2,3,2,2,1,2,2,3,1,2,1,2,3,2,1,2,2});
        selection.add(new int[]{2,1,2,1,2,2,3,2,2,1,2,2,3,1,2,1,2,3,2,3,2,2});
        
        selection.add(new int[]{2,1,2,1,2,2,3,2,2,1,2,2,3,1,2,2,2,3,2,1,2,2});
        selection.add(new int[]{2,1,2,1,3,2,3,2,2,1,2,2,3,1,2,1,2,3,2,3,2,2});
        selection.add(new int[]{2,1,2,1,3,2,3,2,2,1,2,2,3,1,2,1,2,3,2,1,2,2});
        
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
