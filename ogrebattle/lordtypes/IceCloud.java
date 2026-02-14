package ogrebattle.lordtypes;

import static ogrebattle.tarot.pojo.TarotSorting.*;

import java.util.TreeSet;

/** 
* Ice Cloud lord type has at least 18 sets of answers that guarantee Ice Cloud is selected.<br>
* Unlike the other types, Ice Cloud has only 1 question that forces another lord type to receive more points,<br>
* thus can be chosen with 100% certainty. Here the 18 sets are constructed and returned.<br>
*/
public class IceCloud extends LordType {
	public IceCloud() {
		if (!INIT) {
			INIT = true;
			solutions = new TreeSet<int[]>(new IntArrayComparator());
			                     
			solutions.add(new int[]{3,1,2,2,1,1,2,1,3,3,1,3,1,1,3,3,1,2,3,3,3,2});//base and 8 ones
			solutions.add(new int[]{3,1,2,3,1,1,2,1,3,3,1,3,1,1,3,3,1,2,3,3,3,2});//also 8 ones
			solutions.add(new int[]{3,2,2,2,1,1,2,1,3,3,1,3,1,1,3,3,1,2,2,3,3,2});
			                                                         
			solutions.add(new int[]{3,2,2,2,1,1,2,1,3,3,1,3,1,1,3,3,1,2,3,3,3,2});	
			solutions.add(new int[]{3,2,2,2,1,2,2,1,3,3,1,3,1,1,3,3,1,2,3,3,3,2});	
			solutions.add(new int[]{3,2,2,2,3,1,2,1,3,3,1,3,1,1,3,3,1,2,3,3,3,2});
			
			solutions.add(new int[]{3,2,2,3,1,1,2,1,3,3,1,3,1,1,3,3,1,2,2,3,3,2});	
			solutions.add(new int[]{3,2,2,3,1,1,2,1,3,3,1,3,1,1,3,3,1,2,3,3,3,2});		
			solutions.add(new int[]{3,2,2,3,1,2,2,1,3,3,1,3,1,1,3,3,1,2,3,3,3,2});
			
			solutions.add(new int[]{3,2,2,3,3,1,2,1,3,3,1,3,1,1,3,3,1,2,3,3,3,2});		
			solutions.add(new int[]{3,3,2,2,1,1,2,1,3,3,1,3,1,1,3,3,1,2,2,3,3,2});
			solutions.add(new int[]{3,3,2,2,1,1,2,1,3,3,1,3,1,1,3,3,1,2,3,3,3,2});
			
			solutions.add(new int[]{3,3,2,2,1,2,2,1,3,3,1,3,1,1,3,3,1,2,3,3,3,2});	
			solutions.add(new int[]{3,3,2,2,3,1,2,1,3,3,1,3,1,1,3,3,1,2,3,3,3,2});
			solutions.add(new int[]{3,3,2,3,1,1,2,1,3,3,1,3,1,1,3,3,1,2,2,3,3,2});
			
			solutions.add(new int[]{3,3,2,3,1,1,2,1,3,3,1,3,1,1,3,3,1,2,3,3,3,2});		
			solutions.add(new int[]{3,3,2,3,1,2,2,1,3,3,1,3,1,1,3,3,1,2,3,3,3,2});
			solutions.add(new int[]{3,3,2,3,3,1,2,1,3,3,1,3,1,1,3,3,1,2,3,3,3,2});
		}
	}

	public static void main(String[] args) {
		IceCloud ice = new IceCloud();
		ice.printSolutions();
	}
}
/*
Only cards at indices 1, 3, 4, 5, 18 change aka Priestess, Emperor, Hierophant, Lovers, Sun

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
