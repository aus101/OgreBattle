package ogrebattle.lordtypes;

import static ogrebattle.tarot.pojo.TarotSorting.*;

import java.util.TreeSet;

/** Ice Cloud lord type has at least 65 sets (!) of answers that guarantee Ice Cloud is selected.<br>
* Unlike other lord type, Ice Cloud has no question that forced another lord type to receive more points,<br>
* thus can be chosen with 100% certainty. Here the 65 sets are constructed and returned.<br>
*/
public class IceCloud extends LordType {
	public static final int[] BASE = new int[]{3,1,2,2,1,1,2,1,3,3,1,3,1,1,3,3,1,2,3,3,3,2};
	
	public IceCloud() {
		if (!INIT) {
			INIT = true;
			selection = new TreeSet<int[]>(new IntArrayComparator());
			
			selection.add(new int[]{3,1,2,2,1,1,2,1,3,3,1,3,1,1,3,3,1,2,3,3,3,2});//base and 8 ones
			selection.add(new int[]{3,1,2,3,1,1,2,1,3,3,1,3,1,1,3,3,1,2,3,3,3,2});//also 8 ones
			selection.add(new int[]{3,2,2,2,1,1,2,1,3,3,1,3,1,1,3,3,1,2,2,3,3,2});
			
			selection.add(new int[]{3,2,2,2,1,1,2,1,3,3,1,3,1,1,3,3,1,2,3,3,3,2});	
			selection.add(new int[]{3,2,2,2,1,2,2,1,3,3,1,3,1,1,3,3,1,2,3,3,3,2});	
			selection.add(new int[]{3,2,2,2,3,1,2,1,3,3,1,3,1,1,3,3,1,2,3,3,3,2});
			
			selection.add(new int[]{3,2,2,3,1,1,2,1,3,3,1,3,1,1,3,3,1,2,2,3,3,2});	
			selection.add(new int[]{3,2,2,3,1,1,2,1,3,3,1,3,1,1,3,3,1,2,3,3,3,2});		
			selection.add(new int[]{3,2,2,3,1,2,2,1,3,3,1,3,1,1,3,3,1,2,3,3,3,2});
			
			selection.add(new int[]{3,2,2,3,3,1,2,1,3,3,1,3,1,1,3,3,1,2,3,3,3,2});		
			selection.add(new int[]{3,3,2,2,1,1,2,1,3,3,1,3,1,1,3,3,1,2,2,3,3,2});
			selection.add(new int[]{3,3,2,2,1,1,2,1,3,3,1,3,1,1,3,3,1,2,3,3,3,2});
			
			selection.add(new int[]{3,3,2,2,1,2,2,1,3,3,1,3,1,1,3,3,1,2,3,3,3,2});	
			selection.add(new int[]{3,3,2,2,3,1,2,1,3,3,1,3,1,1,3,3,1,2,3,3,3,2});
			selection.add(new int[]{3,3,2,3,1,1,2,1,3,3,1,3,1,1,3,3,1,2,2,3,3,2});
			
			selection.add(new int[]{3,3,2,3,1,1,2,1,3,3,1,3,1,1,3,3,1,2,3,3,3,2});		
			selection.add(new int[]{3,3,2,3,1,2,2,1,3,3,1,3,1,1,3,3,1,2,3,3,3,2});
			selection.add(new int[]{3,3,2,3,3,1,2,1,3,3,1,3,1,1,3,3,1,2,3,3,3,2});
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
		IceCloud ic = new IceCloud();
		ic.printSolutions();
	}
}
/*
card slot at index 9, 10, 14 never changes aka Fortune(3), Justice(1) and Devil(1)

----------

Count: 18
{3,1,2,2,1,1,2,1,3,3,1,3,1,1,3,3,1,2,3,3,3,2};
{3,1,2,3,1,1,2,1,3,3,1,3,1,1,3,3,1,2,3,3,3,2};
{3,2,2,2,1,1,2,1,3,3,1,3,1,1,3,3,1,2,2,3,3,2};
{3,2,2,2,1,1,2,1,3,3,1,3,1,1,3,3,1,2,3,3,3,2};
{3,2,2,2,1,2,2,1,3,3,1,3,1,1,3,3,1,2,3,3,3,2};
{3,2,2,2,3,1,2,1,3,3,1,3,1,1,3,3,1,2,3,3,3,2};
{3,2,2,3,1,1,2,1,3,3,1,3,1,1,3,3,1,2,2,3,3,2};
{3,2,2,3,1,1,2,1,3,3,1,3,1,1,3,3,1,2,3,3,3,2};
{3,2,2,3,1,2,2,1,3,3,1,3,1,1,3,3,1,2,3,3,3,2};
{3,2,2,3,3,1,2,1,3,3,1,3,1,1,3,3,1,2,3,3,3,2};
{3,3,2,2,1,1,2,1,3,3,1,3,1,1,3,3,1,2,2,3,3,2};
{3,3,2,2,1,1,2,1,3,3,1,3,1,1,3,3,1,2,3,3,3,2};
{3,3,2,2,1,2,2,1,3,3,1,3,1,1,3,3,1,2,3,3,3,2};
{3,3,2,2,3,1,2,1,3,3,1,3,1,1,3,3,1,2,3,3,3,2};
{3,3,2,3,1,1,2,1,3,3,1,3,1,1,3,3,1,2,2,3,3,2};
{3,3,2,3,1,1,2,1,3,3,1,3,1,1,3,3,1,2,3,3,3,2};
{3,3,2,3,1,2,2,1,3,3,1,3,1,1,3,3,1,2,3,3,3,2};
{3,3,2,3,3,1,2,1,3,3,1,3,1,1,3,3,1,2,3,3,3,2};
*/
