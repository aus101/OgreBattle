package ogrebattle.joker;

import java.security.SecureRandom;
import java.util.Random;

import ogrebattle.printer.Util;

public class Joker {
	protected final int CARDCOUNT;
	protected int countEarlyExit = 0;
	protected int[] tracker = new int[11];//hold counts for 0 to 9 matches each iteration and 10+ on last index
	public static final int LOOPS = 100_000_000;//iterations
	public static final int STOP = 6;//early exit on this many matches if calling iterate(int stop)
	private static final int PRECSISION = 4;//decimal places for results percentages
	//SecureRandom's nextFloat() is twice as fast as nextDouble()
	public static final float LOWER_LIMIT = (1/21f);//1 in 21 cards, duplicates not possible
	public static final float UPPER_LIMIT = (1/22f);//1 in 22 cards, for first draw only
	
	private static final Random r = new SecureRandom();//SecureRandom is many times slower but not glaringly biased or predictable
	
	public Joker() {
		CARDCOUNT = 60;//default to 60 cards to draw and count matches for 1 cycle
	}
	
	public Joker(int cardCount) {
		CARDCOUNT = cardCount;//number of cards to draw and count matches for 1 cycle
	}
	
	public static void main(String[] args) {
		Joker j = new Joker();
		int tally = 0;
		long totalCards;

		long start = System.nanoTime();
		for (int i=0; i<LOOPS; i++) {
			tally += j.iterateFaster();//Start drawing cards and return the matches
			//tally += j.iterate(STOP);//Stop drawing each loop if matches equal STOP
		}
		long end = System.nanoTime();
		if (j.getCountEarlyExit() > 0) {
			totalCards = j.getCountEarlyExit();
		} else {
			totalCards = (long) LOOPS * j.getCardCount();
			//totalCards = new BigDecimal(LOOPS).multiply(new BigDecimal(j.getCardCount())).longValueExact();
		}
		System.out.println(Util.nanoToMinutesSeconds(end - start) + " to execute for " + Util.numberSeparator(LOOPS) + " iterations at "
				+ Util.numberSeparator(j.getCardCount()) + " cards apiece = " + Util.numberSeparator(totalCards) + " total cards");
		String s;
		if (tally != 1) {
			s = " matches or 1 for every ";
		} else {
			s = " match or 1 for every ";
		} 
		System.out.println(Util.percentAlt(tally, totalCards, PRECSISION, s) + " Joker/Liberation pulls");
		System.out.println(System.lineSeparator() + "Odds of the expected number of matches for iterations of "
				+ Util.numberSeparator(j.getCardCount()) + " cards:" + System.lineSeparator());
		j.printTrackerPercent();
	}
	
	/**
	 * Pull cards <code>cardCount</code> number of times<br>
	 * @param STOP Stop at this many matches found to only draw the minimum cards necessary<br>
	 * @return The number of matches<br>
	 */
	public int iterate(int STOP) {
		int tally = 0;
		float temp = 0;
		boolean lastFound = false;
		
		if (r.nextFloat() < UPPER_LIMIT) {//All 22 cards are possible on first draw
			tally++;
			if (STOP == 1) {
				//System.out.println("Found on first card");
				countEarlyExit++;
				return tally;
			}
			lastFound = true;//cannot match next pull
		}
		
		countEarlyExit += CARDCOUNT;//avoid incrementing
		for (int i=2; i<=CARDCOUNT; i++) {//start on second card, i=2 to simplify early exit code
			if (!lastFound) {
				temp = LOWER_LIMIT;
			} else {
				temp = 0;//cannot find a match this time since duplicates are impossible
				lastFound = false;//reset
			}
			if (r.nextFloat() < temp) {
				tally++;
				lastFound = true;
				if (tally == STOP) {
					System.out.println("Found early with " + i + " cards");
					countEarlyExit -= (CARDCOUNT - i);//subtract out cards not drawn
					break;//found the limit, early exit to next iteration
				}
			}
		}
		iterateTracker(tally);
		return tally;
	}
	
	/**
	 * Convenience method that calls <code>iterate(int STOP)</code> with -1 to avoid early exit<br>
	 * @return The number of matches<br>
	 */
	public int iterate() {
		return iterate(-1);
	}
	
	/**
	 * Pull cards <code>cardCount</code> number of times<br> with no early exit
	 * A modest 5.5% faster over <code>iterate</code> with same card count attained by:<br>
	 * No code checking for early exit possibility<br>
	 * No calling the RNG for the card after a match guaranteed not to be a duplicate<br>
	 * No temporary variable to hold the rate of finding a match<br>
	 * @return The number of matches<br>
	 */
	public int iterateFaster() {
		int tally = 0;
		boolean lastFound = true;
		
		if (r.nextFloat() < UPPER_LIMIT) {//All 22 cards are possible on first draw
			tally++;
		}
		for (int i=1; i<CARDCOUNT; i++) {//on second card: int i=0; i<CARDCOUNT-1; and int i=2; i<=CARDCOUNT; also work
			if (!lastFound) {
				if (r.nextFloat() < LOWER_LIMIT) {
					tally++;
					lastFound = true;//cannot match next pull
				}
			} else {//do not really draw the card guaranteed not to be a match to save time
				lastFound = false;//this way is not deterministic with iterate() due to not advancing the RNG
			}
		}
		//not trying to avoid overhead of a single call
		iterateTracker(tally);
		return tally;
	}
	
	private void iterateTracker(int tally) {
		if (tally <= 9) {
			tracker[tally]++;
		} else {//10 or more are shared on the last index
			tracker[10]++;
		}
	}
	
	public void printTracker() {
		for (int i=0; i<tracker.length-1; i++) {
			System.out.println(i + " found: " + tracker[i]);
		}
		System.out.println("10 or more found: " + tracker[10]);
	}
	
	public void printTrackerPercent() {
		for (int i=0; i<tracker.length-1; i++) {
			System.out.println(new StringBuilder().append(i).append(" found: ")
					.append(Util.percentAlt(tracker[i], LOOPS, PRECSISION)).append(" iterations"));
		}
		StringBuilder s10 = new StringBuilder("10 or more found: ").append(Util.percentAlt(tracker[10], LOOPS, PRECSISION));//last index
		if (tracker[10] > 0) {
			s10.append(" iterations");
		}
		System.out.println(s10.toString());
	}
	
	public int getCardCount() {
		return CARDCOUNT;
	}
	
	public int getCountEarlyExit() {
		return countEarlyExit;
	}
}
/*
times with low tier i5 CPU on Windows 10
-
less than 1 second to execute for 100 iterations at 60 cards apiece = 6,000 total cards
4.4667% (268) matches or 1 for every 22.4 Joker/Liberation pulls

Odds of the expected number of matches for iterations of 60 cards:

0 found: 6% (6) or 1 in 16.7 iterations
1 found: 21% (21) or 1 in 4.8 iterations
2 found: 20% (20) or 1 in 5 iterations
3 found: 26% (26) or 1 in 3.8 iterations
4 found: 13% (13) or 1 in 7.7 iterations
5 found: 9% (9) or 1 in 11.1 iterations
6 found: 3% (3) or 1 in 33.3 iterations
7 found: 2% (2) or 1 in 50 iterations
8 found: 0
9 found: 0
10 or more found: 0
-
less than 1 second to execute for 1,000 iterations at 60 cards apiece = 60,000 total cards
4.4217% (2653) matches or 1 for every 22.6 Joker/Liberation pulls

Odds of the expected number of matches for iterations of 60 cards:

0 found: 5.5% (55) or 1 in 18.2 iterations
1 found: 19.2% (192) or 1 in 5.2 iterations
2 found: 24.3% (243) or 1 in 4.1 iterations
3 found: 23.6% (236) or 1 in 4.2 iterations
4 found: 15.5% (155) or 1 in 6.5 iterations
5 found: 8% (80) or 1 in 12.5 iterations
6 found: 3% (30) or 1 in 33.3 iterations
7 found: 0.7% (7) or 1 in 142.9 iterations
8 found: 0.1% (1)
9 found: 0
10 or more found: 0.1% (1) iterations
-
less than 1 second to execute for 10,000 iterations at 60 cards apiece = 600,000 total cards
4.453% (26718) matches or 1 for every 22.5 Joker/Liberation pulls

Odds of the expected number of matches for iterations of 60 cards:

0 found: 5.46% (546) or 1 in 18.3 iterations
1 found: 17.63% (1763) or 1 in 5.7 iterations
2 found: 25.81% (2581) or 1 in 3.9 iterations
3 found: 23.64% (2364) or 1 in 4.2 iterations
4 found: 15.92% (1592) or 1 in 6.3 iterations
5 found: 7.35% (735) or 1 in 13.6 iterations
6 found: 3.01% (301) or 1 in 33.2 iterations
7 found: 0.97% (97) or 1 in 103.1 iterations
8 found: 0.17% (17) or 1 in 588.2 iterations
9 found: 0.03% (3) or 1 in 3333.3 iterations
10 or more found: 0.01% (1) iterations
-
4 seconds to execute for 100,000 iterations at 60 cards apiece = 6,000,000 total cards
4.4586% (267514) matches or 1 for every 22.4 Joker/Liberation pulls

Odds of the expected number of matches for iterations of 60 cards:

0 found: 5.654% (5654) or 1 in 17.7 iterations
1 found: 17.51% (17510) or 1 in 5.7 iterations
2 found: 25.762% (25762) or 1 in 3.9 iterations
3 found: 23.693% (23693) or 1 in 4.2 iterations
4 found: 15.427% (15427) or 1 in 6.5 iterations
5 found: 7.729% (7729) or 1 in 12.9 iterations
6 found: 2.908% (2908) or 1 in 34.4 iterations
7 found: 1.006% (1006) or 1 in 99.4 iterations
8 found: 0.254% (254) or 1 in 393.7 iterations
9 found: 0.046% (46) or 1 in 2173.9 iterations
10 or more found: 0.011% (11) or 1 in 9090.9 iterations
-
45 seconds to execute for 1,000,000 iterations at 60 cards apiece = 60,000,000 total cards
4.471% (2682595) matches or 1 for every 22.4 Joker/Liberation pulls

Odds of the expected number of matches for iterations of 60 cards:

0 found: 5.7091% (57091) or 1 in 17.5 iterations
1 found: 17.4003% (174003) or 1 in 5.7 iterations
2 found: 25.5381% (255381) or 1 in 3.9 iterations
3 found: 23.6723% (236723) or 1 in 4.2 iterations
4 found: 15.6144% (156144) or 1 in 6.4 iterations
5 found: 7.7426% (77426) or 1 in 12.9 iterations
6 found: 3.0421% (30421) or 1 in 32.9 iterations
7 found: 0.9772% (9772) or 1 in 102.3 iterations
8 found: 0.2455% (2455) or 1 in 407.3 iterations
9 found: 0.0481% (481) or 1 in 2079 iterations
10 or more found: 0.0103% (103) or 1 in 9708.7 iterations
-
7 minutes, 33 seconds to execute for 10,000,000 iterations at 60 cards apiece = 600,000,000 total cards
4.4733% (26839947) matches or 1 for every 22.4 Joker/Liberation pulls

Odds of the expected number of matches for iterations of 60 cards:

0 found: 5.6319% (563188) or 1 in 17.8 iterations
1 found: 17.3926% (1739264) or 1 in 5.7 iterations
2 found: 25.5641% (2556407) or 1 in 3.9 iterations
3 found: 23.7367% (2373666) or 1 in 4.2 iterations
4 found: 15.6357% (1563573) or 1 in 6.4 iterations
5 found: 7.749% (774902) or 1 in 12.9 iterations
6 found: 3.0331% (303309) or 1 in 33 iterations
7 found: 0.9496% (94964) or 1 in 105.3 iterations
8 found: 0.2437% (24367) or 1 in 410.4 iterations
9 found: 0.0525% (5252) or 1 in 1904 iterations
10 or more found: 0.0111% (1108) or 1 in 9025.3 iterations
-
78 minutes, 9 seconds to execute for 100,000,000 iterations at 60 cards apiece = 6,000,000,000 total cards
4.4731% (268386067) matches or 1 for every 22.4 Joker/Liberation pulls

Odds of the expected number of matches for iterations of 60 cards:

0 found: 5.6362% (5636173) or 1 in 17.7 iterations
1 found: 17.4122% (17412164) or 1 in 5.7 iterations
2 found: 25.5582% (25558207) or 1 in 3.9 iterations
3 found: 23.7123% (23712349) or 1 in 4.2 iterations
4 found: 15.6204% (15620425) or 1 in 6.4 iterations
5 found: 7.7684% (7768443) or 1 in 12.9 iterations
6 found: 3.033% (3032962) or 1 in 33 iterations
7 found: 0.9517% (951705) or 1 in 105.1 iterations
8 found: 0.2441% (244098) or 1 in 409.7 iterations
9 found: 0.0525% (52549) or 1 in 1903 iterations
10 or more found: 0.0109% (10925) or 1 in 9153.3 iterations
 */
