package ogrebattle.lordtypes;

import static ogrebattle.tarot.pojo.TarotSorting.*;

import java.util.TreeSet;

/** 
* Ice Cloud Original SFC lord type has at least 20 sets of answers that guarantee Ice Cloud is selected.<br>
* Unlike the other types, Ice Cloud has only 1 question that forces another lord type to receive more points,<br>
* thus can be chosen with 100% certainty. Here the 20 sets are constructed and returned.<br>
*/
public class IceCloudSFC extends LordType {
	public IceCloudSFC() {
		if (!INIT) {
			INIT = true;
			solutions = new TreeSet<int[]>(new IntArrayComparator());
			
			solutions.add(new int[]{3,1,3,1,1,2,1,3,3,1,3,1,1,3,3,1,2,3,3,3,2,1});//base and 9 ones
			solutions.add(new int[]{3,1,2,1,1,2,1,3,3,1,3,1,1,3,3,1,2,3,3,3,2,1});//also 9 ones
			solutions.add(new int[]{1,1,3,1,1,2,1,3,3,1,3,1,1,3,3,1,2,3,3,3,2,3});//also 9 ones

			solutions.add(new int[]{3,1,2,1,1,2,1,3,3,1,3,1,1,3,3,1,2,2,3,3,2,1});//also 9 ones
			solutions.add(new int[]{3,1,3,1,1,2,1,3,3,1,3,1,1,3,3,1,2,2,3,3,2,1});//also 9 ones
			solutions.add(new int[]{3,1,3,1,2,2,1,3,3,1,3,1,1,3,3,1,2,3,3,3,2,1});

			solutions.add(new int[]{3,1,2,1,1,2,1,3,3,1,3,1,1,3,3,1,2,2,3,3,2,3});
			solutions.add(new int[]{3,1,3,1,1,2,1,3,3,1,3,1,1,3,3,1,2,2,3,3,2,3});
			solutions.add(new int[]{3,1,2,1,1,2,1,3,3,1,3,1,1,3,3,1,2,3,3,3,2,3});
			                        
			solutions.add(new int[]{3,1,3,1,1,2,1,3,3,1,3,1,1,3,3,1,2,3,3,3,2,3});	
			solutions.add(new int[]{3,1,2,1,2,2,1,3,3,1,3,1,1,3,3,1,2,2,3,3,2,3});
			solutions.add(new int[]{3,1,3,1,2,2,1,3,3,1,3,1,1,3,3,1,2,2,3,3,2,3});
			
			solutions.add(new int[]{3,1,2,1,1,2,1,2,3,1,3,1,1,3,3,1,2,3,3,3,2,3});
			solutions.add(new int[]{3,1,3,1,1,2,1,2,3,1,3,1,1,3,3,1,2,3,3,3,2,3});
			solutions.add(new int[]{3,1,2,1,2,2,1,3,3,1,3,1,1,3,3,1,2,3,3,3,2,3});

			solutions.add(new int[]{3,1,3,1,2,2,1,3,3,1,3,1,1,3,3,1,2,3,3,3,2,3});
			solutions.add(new int[]{3,1,2,1,1,2,1,3,3,1,3,1,1,3,3,2,2,3,3,3,2,3});
			solutions.add(new int[]{3,1,3,1,1,2,1,3,3,1,3,1,1,3,3,2,2,3,3,3,2,3});
			
			solutions.add(new int[]{3,1,2,3,1,2,1,3,3,1,3,1,1,3,3,1,2,3,3,3,2,3});
			solutions.add(new int[]{3,1,3,3,1,2,1,3,3,1,3,1,1,3,3,1,2,3,3,3,2,3});
		}
	}

	public static void main(String[] args) {
		IceCloudSFC ice = new IceCloudSFC();
		ice.printSolutions();
	}
}
/*
Count: 20
{1,1,3,1,1,2,1,3,3,1,3,1,1,3,3,1,2,3,3,3,2,3};
{3,1,2,1,1,2,1,2,3,1,3,1,1,3,3,1,2,3,3,3,2,3};
{3,1,2,1,1,2,1,3,3,1,3,1,1,3,3,1,2,2,3,3,2,1};
{3,1,2,1,1,2,1,3,3,1,3,1,1,3,3,1,2,2,3,3,2,3};
{3,1,2,1,1,2,1,3,3,1,3,1,1,3,3,1,2,3,3,3,2,1};
{3,1,2,1,1,2,1,3,3,1,3,1,1,3,3,1,2,3,3,3,2,3};
{3,1,2,1,1,2,1,3,3,1,3,1,1,3,3,2,2,3,3,3,2,3};
{3,1,2,1,2,2,1,3,3,1,3,1,1,3,3,1,2,2,3,3,2,3};
{3,1,2,1,2,2,1,3,3,1,3,1,1,3,3,1,2,3,3,3,2,3};
{3,1,2,3,1,2,1,3,3,1,3,1,1,3,3,1,2,3,3,3,2,3};
{3,1,3,1,1,2,1,2,3,1,3,1,1,3,3,1,2,3,3,3,2,3};
{3,1,3,1,1,2,1,3,3,1,3,1,1,3,3,1,2,2,3,3,2,1};
{3,1,3,1,1,2,1,3,3,1,3,1,1,3,3,1,2,2,3,3,2,3};
{3,1,3,1,1,2,1,3,3,1,3,1,1,3,3,1,2,3,3,3,2,1};
{3,1,3,1,1,2,1,3,3,1,3,1,1,3,3,1,2,3,3,3,2,3};
{3,1,3,1,1,2,1,3,3,1,3,1,1,3,3,2,2,3,3,3,2,3};
{3,1,3,1,2,2,1,3,3,1,3,1,1,3,3,1,2,2,3,3,2,3};
{3,1,3,1,2,2,1,3,3,1,3,1,1,3,3,1,2,3,3,3,2,1};
{3,1,3,1,2,2,1,3,3,1,3,1,1,3,3,1,2,3,3,3,2,3};
{3,1,3,3,1,2,1,3,3,1,3,1,1,3,3,1,2,3,3,3,2,3};
*/