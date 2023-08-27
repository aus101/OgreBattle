package ogrebattle.util;

import static ogrebattle.util.TarotComparators.*;

import java.util.Set;
import java.util.TreeSet;

public class IceCloud extends LordType {
	
	public IceCloud() {
		BASE = new int[] {3,2,3,1,1,1,1,3,3,3,1,1,2,2,3,1,3,1,2,2,1,3};
	}
	
	public static void main(String[] args) {
		IceCloud ic = new IceCloud();
		ic.printSolutions();
	}

	@Override
	public Set<int[]> returnAllSolutionsSet() {
		TreeSet<int[]> selection = new TreeSet<int[]>(new IntArrayComparator());
		selection.add(new int[]{3,2,3,1,1,1,1,3,3,3,1,1,2,2,3,1,3,1,2,2,1,3});//base and 9 ones
		
		selection.add(new int[]{3,3,3,3,1,1,2,1,3,3,1,3,1,1,3,3,2,2,3,3,3,2});
		selection.add(new int[]{3,2,3,3,1,1,2,1,3,3,1,3,1,1,3,3,2,2,3,3,3,2});
		selection.add(new int[]{3,3,3,2,1,1,2,1,3,3,1,3,1,1,3,3,2,2,3,3,3,2});
		
		selection.add(new int[]{3,3,3,3,1,1,2,1,3,3,1,3,1,1,3,3,1,2,3,3,3,2});
		selection.add(new int[]{1,3,3,3,1,1,2,1,3,3,1,3,1,1,3,3,1,2,3,3,3,2});
		selection.add(new int[]{3,1,3,3,1,1,2,1,3,3,1,3,1,1,3,3,1,2,3,3,3,2});
		
		selection.add(new int[]{3,2,3,2,1,1,2,1,3,3,1,3,1,1,3,3,2,2,3,3,3,2});
		selection.add(new int[]{3,2,3,3,1,1,2,1,3,3,1,3,1,1,3,3,1,2,3,3,3,2});
		selection.add(new int[]{3,3,2,3,1,1,2,1,3,3,1,3,1,1,3,3,1,2,3,3,3,2});
	
		selection.add(new int[]{3,3,3,2,1,1,2,1,3,3,1,3,1,1,3,3,1,2,3,3,3,2});
		selection.add(new int[]{3,3,3,3,1,1,2,1,2,3,1,3,1,1,3,3,1,2,3,3,3,2});
		selection.add(new int[]{3,3,3,3,1,1,2,1,3,3,1,3,1,1,3,3,1,2,2,3,3,2});
		
		selection.add(new int[]{3,3,3,3,1,2,2,1,3,3,1,3,1,1,3,3,1,2,3,3,3,2});
		selection.add(new int[]{3,3,3,3,3,1,2,1,3,3,1,3,1,1,3,3,1,2,3,3,3,2});
		selection.add(new int[]{1,2,3,3,1,1,2,1,3,3,1,3,1,1,3,3,1,2,3,3,3,2});
		
		selection.add(new int[]{3,1,2,3,1,1,2,1,3,3,1,3,1,1,3,3,1,2,3,3,3,2});
		selection.add(new int[]{3,1,3,2,1,1,2,1,3,3,1,3,1,1,3,3,1,2,3,3,3,2});
		selection.add(new int[]{3,1,3,3,1,1,2,1,3,3,1,3,1,1,3,3,1,2,2,3,3,2});	
		
		selection.add(new int[]{3,1,3,3,1,2,2,1,3,3,1,3,1,1,3,3,1,2,3,3,3,2});
		selection.add(new int[]{3,1,3,3,3,1,2,1,3,3,1,3,1,1,3,3,1,2,3,3,3,2});
		selection.add(new int[]{3,2,2,3,1,1,2,1,3,3,1,3,1,1,3,3,1,2,3,3,3,2});
		
		selection.add(new int[]{3,2,3,2,1,1,2,1,3,3,1,3,1,1,3,3,1,2,3,3,3,2});
		selection.add(new int[]{3,2,3,2,1,1,2,1,2,3,1,3,1,1,3,3,1,2,3,3,3,2});
		selection.add(new int[]{3,2,3,3,1,1,2,1,2,3,1,3,1,1,3,3,1,2,3,3,3,2});
		
		selection.add(new int[]{3,2,3,3,1,1,2,1,3,3,1,3,1,1,3,3,1,2,2,3,3,2});
		selection.add(new int[]{3,2,3,3,1,2,2,1,3,3,1,3,1,1,3,3,1,2,3,3,3,2});
		selection.add(new int[]{3,2,3,3,3,1,2,1,3,3,1,3,1,1,3,3,1,2,3,3,3,2});		
		
		selection.add(new int[]{3,3,2,2,1,1,2,1,3,3,1,3,1,1,3,3,1,2,3,3,3,2});
		selection.add(new int[]{3,3,2,3,1,1,2,1,3,3,1,3,1,1,3,3,1,2,2,3,3,2});
		selection.add(new int[]{3,3,2,3,1,2,2,1,3,3,1,3,1,1,3,3,1,2,3,3,3,2});
		
		selection.add(new int[]{3,3,2,3,3,1,2,1,3,3,1,3,1,1,3,3,1,2,3,3,3,2});
		selection.add(new int[]{3,3,3,2,1,1,2,1,2,3,1,3,1,1,3,3,1,2,3,3,3,2});
		selection.add(new int[]{3,3,3,2,1,1,2,1,3,3,1,3,1,1,3,3,1,2,2,3,3,2});
		
		selection.add(new int[]{3,3,3,2,1,2,2,1,3,3,1,3,1,1,3,3,1,2,3,3,3,2});
		selection.add(new int[]{3,3,3,2,3,1,2,1,3,3,1,3,1,1,3,3,1,2,3,3,3,2});
		selection.add(new int[]{3,3,3,3,3,1,2,1,3,3,1,3,1,1,3,3,1,2,2,3,3,2});	
	
		selection.add(new int[]{3,3,3,3,1,2,2,1,3,3,1,3,1,1,3,3,1,2,2,3,3,2});
		selection.add(new int[]{3,3,3,3,3,2,2,1,3,3,1,3,1,1,3,3,1,2,3,3,3,2});
		selection.add(new int[]{3,1,3,2,1,2,2,1,3,3,1,3,1,1,3,3,1,2,3,3,3,2});
		
		selection.add(new int[]{3,2,2,3,1,1,2,1,3,3,1,3,1,1,3,3,1,2,2,3,3,2});
		selection.add(new int[]{3,2,2,3,3,1,2,1,3,3,1,3,1,1,3,3,1,2,3,3,3,2});
		selection.add(new int[]{3,2,3,2,1,1,2,1,3,3,1,3,1,1,3,3,1,2,2,3,3,2});
		
		selection.add(new int[]{3,2,3,3,1,2,2,1,3,3,1,3,1,1,3,3,1,2,2,3,3,2});
		selection.add(new int[]{3,3,2,2,1,2,2,1,3,3,1,3,1,1,3,3,1,2,3,3,3,2});		
		selection.add(new int[]{3,2,3,2,1,2,2,1,3,3,1,3,1,1,3,3,1,2,3,3,3,2});
		
		selection.add(new int[]{3,2,3,2,3,1,2,1,3,3,1,3,1,1,3,3,1,2,3,3,3,2});
		selection.add(new int[]{3,1,2,2,1,1,2,1,3,3,1,3,1,1,3,3,1,2,3,3,3,2});
		selection.add(new int[]{3,3,3,2,3,1,2,1,3,3,1,3,1,1,3,3,1,2,2,3,3,2});
		
		selection.add(new int[]{3,3,3,2,3,2,2,1,3,3,1,3,1,1,3,3,1,2,3,3,3,2});
		selection.add(new int[]{3,1,3,2,1,1,2,1,3,3,1,3,1,1,3,3,1,2,2,3,3,2});
		selection.add(new int[]{3,1,3,2,3,1,2,1,3,3,1,3,1,1,3,3,1,2,3,3,3,2});
		
		selection.add(new int[]{3,2,2,3,1,2,2,1,3,3,1,3,1,1,3,3,1,2,3,3,3,2});
		selection.add(new int[]{3,2,3,3,3,1,2,1,3,3,1,3,1,1,3,3,1,2,2,3,3,2});
		selection.add(new int[]{3,2,3,3,3,2,2,1,3,3,1,3,1,1,3,3,1,2,3,3,3,2});
		
		selection.add(new int[]{3,3,2,2,1,1,2,1,3,3,1,3,1,1,3,3,1,2,2,3,3,2});	
		selection.add(new int[]{3,2,2,2,1,1,2,1,3,3,1,3,1,1,3,3,1,2,3,3,3,2});	
		selection.add(new int[]{3,2,2,2,1,2,2,1,3,3,1,3,1,1,3,3,1,2,3,3,3,2});
		
		selection.add(new int[]{3,2,2,2,3,1,2,1,3,3,1,3,1,1,3,3,1,2,3,3,3,2});
		selection.add(new int[]{3,2,2,2,1,1,2,1,3,3,1,3,1,1,3,3,1,2,2,3,3,2});
		selection.add(new int[]{3,3,2,2,3,1,2,1,3,3,1,3,1,1,3,3,1,2,3,3,3,2});
		
		selection.add(new int[]{3,2,3,2,1,2,2,1,3,3,1,3,1,1,3,3,1,2,2,3,3,2});
		selection.add(new int[]{3,2,3,2,3,1,2,1,3,3,1,3,1,1,3,3,1,2,2,3,3,2});
		selection.add(new int[]{3,3,3,2,1,2,2,1,3,3,1,3,1,1,3,3,1,2,2,3,3,2});
		
		selection.add(new int[]{3,2,3,2,3,2,2,1,3,3,1,3,1,1,3,3,1,2,3,3,3,2});
		
		return selection;
	}
	//card slot at index 9, 10, 14 never changes aka Fortune(3), Justice(1) and Devil(1)
}

/*
card slot at index 9, 10, 14 never changes aka Fortune(3), Justice(1) and Devil(1)

CHANGES FROM: {3,2,3,1,1,1,1,3,3,3,1,1,2,2,3,1,3,1,2,2,1,3}

16 Changes:
{3,3,2,2,1,2,2,1,3,3,1,3,1,1,3,3,1,2,3,3,3,2};

16 Changes:
{3,3,2,2,3,1,2,1,3,3,1,3,1,1,3,3,1,2,3,3,3,2};

16 Changes:
{3,3,2,3,1,2,2,1,3,3,1,3,1,1,3,3,1,2,3,3,3,2};

16 Changes:
{3,3,2,3,3,1,2,1,3,3,1,3,1,1,3,3,1,2,3,3,3,2};

16 Changes:
{3,3,3,2,3,2,2,1,3,3,1,3,1,1,3,3,1,2,3,3,3,2};

16 Changes:
{3,3,3,3,3,2,2,1,3,3,1,3,1,1,3,3,1,2,3,3,3,2};

----------
MAX ONES IS 9
{3,2,3,1,1,1,1,3,3,3,1,1,2,2,3,1,3,1,2,2,1,3};//the base

----------

Count: 65
{1,2,3,3,1,1,2,1,3,3,1,3,1,1,3,3,1,2,3,3,3,2};
{1,3,3,3,1,1,2,1,3,3,1,3,1,1,3,3,1,2,3,3,3,2};
{3,1,2,2,1,1,2,1,3,3,1,3,1,1,3,3,1,2,3,3,3,2};
{3,1,2,3,1,1,2,1,3,3,1,3,1,1,3,3,1,2,3,3,3,2};
{3,1,3,2,1,1,2,1,3,3,1,3,1,1,3,3,1,2,2,3,3,2};
{3,1,3,2,1,1,2,1,3,3,1,3,1,1,3,3,1,2,3,3,3,2};
{3,1,3,2,1,2,2,1,3,3,1,3,1,1,3,3,1,2,3,3,3,2};
{3,1,3,2,3,1,2,1,3,3,1,3,1,1,3,3,1,2,3,3,3,2};
{3,1,3,3,1,1,2,1,3,3,1,3,1,1,3,3,1,2,2,3,3,2};
{3,1,3,3,1,1,2,1,3,3,1,3,1,1,3,3,1,2,3,3,3,2};
{3,1,3,3,1,2,2,1,3,3,1,3,1,1,3,3,1,2,3,3,3,2};
{3,1,3,3,3,1,2,1,3,3,1,3,1,1,3,3,1,2,3,3,3,2};
{3,2,2,2,1,1,2,1,3,3,1,3,1,1,3,3,1,2,2,3,3,2};
{3,2,2,2,1,1,2,1,3,3,1,3,1,1,3,3,1,2,3,3,3,2};
{3,2,2,2,1,2,2,1,3,3,1,3,1,1,3,3,1,2,3,3,3,2};
{3,2,2,2,3,1,2,1,3,3,1,3,1,1,3,3,1,2,3,3,3,2};
{3,2,2,3,1,1,2,1,3,3,1,3,1,1,3,3,1,2,2,3,3,2};
{3,2,2,3,1,1,2,1,3,3,1,3,1,1,3,3,1,2,3,3,3,2};
{3,2,2,3,1,2,2,1,3,3,1,3,1,1,3,3,1,2,3,3,3,2};
{3,2,2,3,3,1,2,1,3,3,1,3,1,1,3,3,1,2,3,3,3,2};
{3,2,3,1,1,1,1,3,3,3,1,1,2,2,3,1,3,1,2,2,1,3};
{3,2,3,2,1,1,2,1,2,3,1,3,1,1,3,3,1,2,3,3,3,2};
{3,2,3,2,1,1,2,1,3,3,1,3,1,1,3,3,1,2,2,3,3,2};
{3,2,3,2,1,1,2,1,3,3,1,3,1,1,3,3,1,2,3,3,3,2};
{3,2,3,2,1,1,2,1,3,3,1,3,1,1,3,3,2,2,3,3,3,2};
{3,2,3,2,1,2,2,1,3,3,1,3,1,1,3,3,1,2,2,3,3,2};
{3,2,3,2,1,2,2,1,3,3,1,3,1,1,3,3,1,2,3,3,3,2};
{3,2,3,2,3,1,2,1,3,3,1,3,1,1,3,3,1,2,2,3,3,2};
{3,2,3,2,3,1,2,1,3,3,1,3,1,1,3,3,1,2,3,3,3,2};
{3,2,3,2,3,2,2,1,3,3,1,3,1,1,3,3,1,2,3,3,3,2};
{3,2,3,3,1,1,2,1,2,3,1,3,1,1,3,3,1,2,3,3,3,2};
{3,2,3,3,1,1,2,1,3,3,1,3,1,1,3,3,1,2,2,3,3,2};
{3,2,3,3,1,1,2,1,3,3,1,3,1,1,3,3,1,2,3,3,3,2};
{3,2,3,3,1,1,2,1,3,3,1,3,1,1,3,3,2,2,3,3,3,2};
{3,2,3,3,1,2,2,1,3,3,1,3,1,1,3,3,1,2,2,3,3,2};
{3,2,3,3,1,2,2,1,3,3,1,3,1,1,3,3,1,2,3,3,3,2};
{3,2,3,3,3,1,2,1,3,3,1,3,1,1,3,3,1,2,2,3,3,2};
{3,2,3,3,3,1,2,1,3,3,1,3,1,1,3,3,1,2,3,3,3,2};
{3,2,3,3,3,2,2,1,3,3,1,3,1,1,3,3,1,2,3,3,3,2};
{3,3,2,2,1,1,2,1,3,3,1,3,1,1,3,3,1,2,2,3,3,2};
{3,3,2,2,1,1,2,1,3,3,1,3,1,1,3,3,1,2,3,3,3,2};
{3,3,2,2,1,2,2,1,3,3,1,3,1,1,3,3,1,2,3,3,3,2};
{3,3,2,2,3,1,2,1,3,3,1,3,1,1,3,3,1,2,3,3,3,2};
{3,3,2,3,1,1,2,1,3,3,1,3,1,1,3,3,1,2,2,3,3,2};
{3,3,2,3,1,1,2,1,3,3,1,3,1,1,3,3,1,2,3,3,3,2};
{3,3,2,3,1,2,2,1,3,3,1,3,1,1,3,3,1,2,3,3,3,2};
{3,3,2,3,3,1,2,1,3,3,1,3,1,1,3,3,1,2,3,3,3,2};
{3,3,3,2,1,1,2,1,2,3,1,3,1,1,3,3,1,2,3,3,3,2};
{3,3,3,2,1,1,2,1,3,3,1,3,1,1,3,3,1,2,2,3,3,2};
{3,3,3,2,1,1,2,1,3,3,1,3,1,1,3,3,1,2,3,3,3,2};
{3,3,3,2,1,1,2,1,3,3,1,3,1,1,3,3,2,2,3,3,3,2};
{3,3,3,2,1,2,2,1,3,3,1,3,1,1,3,3,1,2,2,3,3,2};
{3,3,3,2,1,2,2,1,3,3,1,3,1,1,3,3,1,2,3,3,3,2};
{3,3,3,2,3,1,2,1,3,3,1,3,1,1,3,3,1,2,2,3,3,2};
{3,3,3,2,3,1,2,1,3,3,1,3,1,1,3,3,1,2,3,3,3,2};
{3,3,3,2,3,2,2,1,3,3,1,3,1,1,3,3,1,2,3,3,3,2};
{3,3,3,3,1,1,2,1,2,3,1,3,1,1,3,3,1,2,3,3,3,2};
{3,3,3,3,1,1,2,1,3,3,1,3,1,1,3,3,1,2,2,3,3,2};
{3,3,3,3,1,1,2,1,3,3,1,3,1,1,3,3,1,2,3,3,3,2};
{3,3,3,3,1,1,2,1,3,3,1,3,1,1,3,3,2,2,3,3,3,2};
{3,3,3,3,1,2,2,1,3,3,1,3,1,1,3,3,1,2,2,3,3,2};
{3,3,3,3,1,2,2,1,3,3,1,3,1,1,3,3,1,2,3,3,3,2};
{3,3,3,3,3,1,2,1,3,3,1,3,1,1,3,3,1,2,2,3,3,2};
{3,3,3,3,3,1,2,1,3,3,1,3,1,1,3,3,1,2,3,3,3,2};
{3,3,3,3,3,2,2,1,3,3,1,3,1,1,3,3,1,2,3,3,3,2};
*/
