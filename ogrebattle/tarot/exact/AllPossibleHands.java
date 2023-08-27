package ogrebattle.tarot.exact;

import static ogrebattle.util.TarotComparators.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.TreeSet;

import ogrebattle.tarot.Tarot;

/**
 * Generate all possible Tarot card combinations from 1 to 7 cards drawn out of the 22 card deck.<br>
 * Choose the number of cards with <code>handSize</code> and set true to <code>doNotSort</code>
 * to have the hands in random order versus sorted. Can generate random hands even if sorted.<br><br>
 * COMBIN(22,6) = 74613 possible sets of Tarot questions by Warren and COMBIN(22,7) = 170544. Let's brute force these hands into existence.<br>
 * Takes less than 1 second on a stock i5 desktop for non-sorted and less than 3 seconds for sorted for hand of 6
 * and less than 2 seconds and 5 seconds respectively for sorted hand size of 7.<br><br>
 * Sorting is unnecessary for generating random hands or calculating exact odds but intelligent contains calls would be faster,
 * i.e. only the first 20349 hands contain Magician and hand 35853 is the last with a Priestess.<br>
 * The order for sets of hands would be deterministic for a HashSet due to bucket hashing even though the hands generate in random order.<br><br>
 * Each hand is itself sorted so must be shuffled for where order does matter such as final Tarot card bonus.<br>
 * Could just as well choose a random card in the set as the final card.<br>
 * Replacing TreeSet for LinkedHashSet would also work but no approach yields all permutations where order for every card matters.<br>
 * Is 22*21*20*19*18*17 = 53,721,360 permutations versus easily brute forced 74613 combinations with 6 cards where order does not matter.<br>
 */
public class AllPossibleHands {
	public static final int DECK_SIZE = Tarot.values().length;//22
	public final int MAX_HAND_SIZE = 7;
	private final int[] COMBIN = new int[]{22, 231, 1540, 7315, 26334, 74613, 170533};
	private final int handSize;
	private final int maxHands;
	private static final int USE_SUBLIST = 55000;
	private Set<TreeSet<Tarot>> ALL_HANDS;
	private List<TreeSet<Tarot>> ALL_HANDS_LIST;
	private Random r = new Random();
	private boolean doNotSort;
	
	public AllPossibleHands(int handSize) {
		this(handSize, false);
	}
	
	public AllPossibleHands(int handSize, boolean doNotSort) {
		if(handSize < 0 || handSize > MAX_HAND_SIZE) {
			throw new IllegalArgumentException("Hand size must be between 1 and 7 inclusive");
		}
		this.handSize = handSize;
		maxHands = COMBIN[handSize-1];
		this.doNotSort = doNotSort;
		if (doNotSort) {
			ALL_HANDS = new LinkedHashSet<TreeSet<Tarot>>();//HashSet would deterministically order
		} else {
			ALL_HANDS = new TreeSet<TreeSet<Tarot>>(new TreeSetTarotComparator());
		}
		
		final Tarot[] holder = Tarot.values();
		//10x faster to add to the list but do not want duplicate hands
		while(ALL_HANDS.size() < maxHands) {
			TreeSet<Tarot> oneHand = new TreeSet<>();
			while(oneHand.size() < this.handSize) {
				oneHand.add(holder[r.nextInt(DECK_SIZE)]);
			}
			ALL_HANDS.add(oneHand);
		}
		ALL_HANDS_LIST = new ArrayList<TreeSet<Tarot>>(ALL_HANDS);
	}
	
	public Set<Tarot> returnRandomHandInSet() {
		return ALL_HANDS_LIST.get(r.nextInt(maxHands));
	}
	
	public List<Tarot> returnRandomHandInList() {
		return new ArrayList<Tarot>(ALL_HANDS_LIST.get(r.nextInt(maxHands)));
	}
	
	public int size() {
		return ALL_HANDS.size();
	}
		
	public List<TreeSet<Tarot>> returnHandsList(int returnHands) {
		if (returnHands > USE_SUBLIST) {
			//likely faster at this point to use a subList versus iterate
			return returnHandsSubList(returnHands);
		}
		int countHands = 0;
		List<TreeSet<Tarot>> holder = new ArrayList<TreeSet<Tarot>>(returnHands);
		for (TreeSet<Tarot> t : ALL_HANDS_LIST) {
			holder.add(t);
			countHands++;
			if (countHands == returnHands) {
				break;
			}
		}
		return holder;
	}
	
	//TODO make protected
	public List<TreeSet<Tarot>> returnHandsSubList(int returnHands) {
		List<TreeSet<Tarot>> holder = new ArrayList<TreeSet<Tarot>>(ALL_HANDS);
		return holder.subList(0, returnHands);
	}
	
	public Set<TreeSet<Tarot>> returnAllHandsSet() {
		//probably should be unmodifiableSet but the underlying set could still be modified
		return ALL_HANDS;
	}
	
	public List<TreeSet<Tarot>> returnAllHandsArrayList() {
		//probably should be unmodifiableList but the underlying set could still be modified
		return ALL_HANDS_LIST;
	}
	
	public List<TreeSet<Tarot>> returnAllHandsLinkedList() {
		//mutable to underlying ALL_HANDS
		return new LinkedList<TreeSet<Tarot>>(ALL_HANDS);
	}
	
	public List<TreeSet<Tarot>> returnHandsSorted(int returnHands) {
		List<TreeSet<Tarot>> holder = new ArrayList<TreeSet<Tarot>>(ALL_HANDS_LIST);
		//ALL_HANDS_LIST remains unsorted
		if (doNotSort) {//forcing sort, else is already sorted so can skip
			Collections.sort(holder, new TreeSetTarotComparator());
		}
		//mutable to underlying ALL_HANDS_LIST
		return holder.subList(0, returnHands);
	}

	public int countContains(Tarot tarot) {
		int count = 0;
		for(TreeSet<Tarot> ts : ALL_HANDS) {
			if (ts.contains(tarot)) {
				count++;
			}
		}
		return count;
	}
	
	public int countContainsAll(Tarot... tarot) {
		return countContainsAll(Arrays.asList(tarot));
	}
	
	public int countContainsAll(List<Tarot> tarot) {
		int count = 0;
		for(TreeSet<Tarot> ts : ALL_HANDS) {
			if (ts.containsAll(tarot)) {
				count++;
			}
		}
		return count;
	}
	
	public int countContainsAny(Tarot... tarot) {
		return countContainsAny(Arrays.asList(tarot));
	}
	
	public int countContainsAny(List<Tarot> tarot) {
		int count = 0;
		for (TreeSet<Tarot> ts : ALL_HANDS) {
			for (Tarot current : tarot) {
				if (ts.contains(current)) {
					count++;
					break;//go to next hand since current hand contains at least 1
				}
			}
		}
		return count;
	}

	public int countContainsAndContainsAny(Tarot singleton, Tarot... group) {
		return countContainsAndContainsAny(singleton, Arrays.asList(group));
	}
	
	public int countContainsAndContainsAny(Tarot singleton, List<Tarot> tarot) {
		int count = 0;
		for (TreeSet<Tarot> ts : ALL_HANDS) {
			if (ts.contains(singleton)) {
				for (Tarot current : tarot) {
					if (ts.contains(current)) {
						count++;
						break;//go to next hand since current hand contains at least 1
					}
				}
			}
		}
		return count;
	}

	public int countContainsAndContainsAll(Tarot singleton, Tarot... group) {
		return countContainsAndContainsAll(singleton, Arrays.asList(group));
	}
	
	public int countContainsAndContainsAll(Tarot singleton, List<Tarot> tarot) {
		int count = 0;
		for (TreeSet<Tarot> ts : ALL_HANDS) {
			if (ts.contains(singleton)) {
				if (ts.containsAll(tarot)) {
					count++;
				}
			}
		}
		return count;
	}

	public int countContainsAllContainsAny(List<Tarot> group1, List<Tarot> group2) {
		return countContainsAnyContainsAll(group2, group1);
	}
	
	public int countContainsAnyContainsAll(List<Tarot> group1, List<Tarot> group2) {
		int count = 0;
		for (TreeSet<Tarot> ts : ALL_HANDS) {
			for (Tarot current : group1) {
				if (ts.contains(current)) {
					if (ts.containsAll(group2)) {
						count++;
					}
					break;//go to next hand since current hand contains at least 1
				}
			}
		}
		return count;
	}
	
	public int countContainsAnyContainsAny(List<Tarot> group1, List<Tarot> group2) {
		int count = 0;
		for (TreeSet<Tarot> ts : ALL_HANDS) {
			for (Tarot current1 : group1) {
				if (ts.contains(current1)) {
					for (Tarot current2 : group2) {
						if (ts.contains(current2)) {
							count++;
							break;//end group2 check early since 1 was found
						}
						break;//end group1 check early since 1 was found
					}
				}
			}
		}
		return count;
	}
}
