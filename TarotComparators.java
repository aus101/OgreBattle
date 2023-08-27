package ogrebattle.util;

import java.util.Comparator;
import java.util.Iterator;
import java.util.TreeSet;

import ogrebattle.tarot.Tarot;

/**
 * Equal length assumed so comparison is simple
 */
public class TarotComparators {
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
	 * Equal length is not assumed, same order with fewer cards comes first
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
}
