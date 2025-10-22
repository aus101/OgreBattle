package ogrebattle.tarot.pojo;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.TreeSet;

/**
 * Equal length assumed so comparison is simple
 */
public class TarotSorting {
	public static class IntArrayComparator implements Comparator<int[]> {
		@Override 
		public int compare(int[] ar1, int[] ar2) {
			if (ar1 == ar2)
				return 0;
			for(int i=0; i< ar1.length; i++) {
			if (ar1[i] > ar2[i])
				return 1;
			if (ar1[i] < ar2[i])
				return -1;//else equal so continue
			}
			return 0;
		}
	}

	/**
	 * Equal length is not assumed, a set with same order but fewer cards comes first
	 */
	public static class TreeSetTarotComparator implements Comparator<TreeSet<Tarot>> {
		@Override
		public int compare(TreeSet<Tarot> ts1, TreeSet<Tarot> ts2) {
			if (ts1 == ts2)
				return 0;
			Iterator<Tarot> it1 = ts1.iterator();
			Iterator<Tarot> it2 = ts2.iterator();
			while (it1.hasNext()) {
				if (!it2.hasNext())
					return 1;//ts2 with less cards but otherwise equal means it is lower
				int card1 = it1.next().ordinal();
				int card2 = it2.next().ordinal();
				if (card1 > card2)
					return 1;
				else if (card1 < card2)
					return -1;
				else
					continue;
			}
			if (ts1.size() == (ts2.size()))//same size and no early exit means they are equal
				return 0;
			else
				return -1;//ts1 with less cards but otherwise equal means it is lower
		}
	}
	
	/**
	 * Enum natural order doesn't work by default when comparing ordered sets to each other.<br>
	 * Order goes from Magician(1) to World(22) with Fool(21) due to that being the order they are stored in the game ROM.<br>
	 * In-game numbering of Fool as 0 is irrelevant and historically it had no numbering at all.<br>
	 */
    public static class NaturalOrderComparator implements Comparator<Tarot> {
    	@Override
        public int compare(Tarot t1, Tarot t2) {
          return t1.ordinal() - t2.ordinal();
        }
    }
    
	public static class AlphabeticalComparator implements Comparator<Tarot> {
    	@Override
		public int compare(Tarot t1, Tarot t2) {
			return t1.toString().compareTo(t2.toString());	
		}
	}
	
	/**
	 * Equal length is assumed
	 */
	public static class SolutionsComparator implements Comparator<ArrayList<Integer>> {
		@Override
		public int compare(ArrayList<Integer> ts1, ArrayList<Integer> ts2) {
			if (ts1 == ts2)
				return 0;
			
			for(int i=0; i<Tarot.DECK_SIZE; i++) {
//				int result = ts1.get(i).compareTo(ts2.get(i));
//				if (result == 0)
//					continue;
//				else
//					return result;
//			}
             // Integer compare above appears 5% faster than unboxing for int compare
				int card1 = ts1.get(i);
				int card2 = ts2.get(i);
				if (card1 == card2)
					continue;
				else return card1 - card2;
		}
			return 0;//all numbers are equal
		}
	}
	
	/**
	 * Equal length is assumed
	 */
	public static class SolutionsHelperComparator implements Comparator<SolutionsHelper> {
		@Override
		public int compare(SolutionsHelper ts1, SolutionsHelper ts2) {
			if (ts1 == ts2) 
				return 0;
			int[] array1 = ts1.getComparableValues();
			int[] array2 = ts2.getComparableValues();
			
			for(int i=0; i<Tarot.DECK_SIZE; i++) {
				if (array1[i] == array2[i])
					continue;
				else return array1[i] - array2[i];
			}
			return 0;//all numbers are equal
		}
	}
}
