package ogrebattle.joker;

import java.security.SecureRandom;
import java.util.Random;

import ogrebattle.util.Printer;
import ogrebattle.util.Util;

public class Joker {
	protected final int CARDCOUNT;
	protected int countEarlyExit = 0;
	protected int[] tracker = new int[11];//hold counts for 0 to 9 matches each iteration and 10+ on last index
	protected static final int TEN_OR_MORE = 10;//tracker index
	public static final int LOOPS = 1_000_000;//iterations
	public static final int STOP = 6;//early exit on this many matches if calling iterate(int stop)
	//SecureRandom's nextFloat() is twice as fast as nextDouble()
	@Deprecated
	public static final float LOWER_LIMIT = (1/21f);//1 in 21 cards, duplicates not possible
	@Deprecated
	public static final float UPPER_LIMIT = (1/22f);//1 in 22 cards, for first draw only
	public static final float RNG_BIAS_LOWER_LIMIT = (12/244f);//drawing 21 cards out of 256 values, Fool is allocated 12 bits
	public static final float RNG_BIAS_UPPER_LIMIT = (12/256f);//drawing 22 cards out of 256 values, Fool is allocated 12 bits
	
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
			//tally += j.iterate1in22();
		}
		long end = System.nanoTime();
		if (j.getCountEarlyExit() > 0) {
			totalCards = j.getCountEarlyExit();
		} else {
			totalCards = (long) LOOPS * j.getCardCount();
			//totalCards = new BigDecimal(LOOPS).multiply(new BigDecimal(j.getCardCount())).longValueExact();
		}
		System.out.println(Util.nanoToMinutesSeconds(end - start) + " to execute for " + Printer.numberSeparator(LOOPS) + " iterations at "
				+ Printer.numberSeparator(j.getCardCount()) + " cards apiece = " + Printer.numberSeparator(totalCards) + " total cards");
		String s;
		if (tally != 1) {
			s = " matches or 1 for every ";
		} else {
			s = " match or 1 for every ";
		} 
		System.out.println(Printer.percentAlt(tally, totalCards, Printer.PRECISION_PRINT, s) + " Joker/Liberation pulls");
		System.out.println(System.lineSeparator() + "Odds of the expected number of matches for iterations of "
				+ Printer.numberSeparator(j.getCardCount()) + " cards:" + System.lineSeparator());
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
		
		if (r.nextFloat() < RNG_BIAS_UPPER_LIMIT) {//All 22 cards are possible on first draw
			if (STOP == 1) {
				//System.out.println("Found on first card");
				countEarlyExit++;
				iterateTracker(1);
				return 1;//can consolidate for only 1 point of return but harder to understand
			}
			tally++;//no need to use if exiting at 1 match
			lastFound = true;//cannot match next pull
		}
		
		countEarlyExit += CARDCOUNT;//avoid incrementing
		for (int i=2; i<=CARDCOUNT; i++) {//start on second card, i=2 to simplify early exit code
			if (!lastFound) {
				temp = RNG_BIAS_LOWER_LIMIT;
			} else {
				temp = -1;//cannot find a match this time since duplicates are impossible
				lastFound = false;//reset
			}
			if (r.nextFloat() < temp) {
				tally++;
				lastFound = true;
				if (tally == STOP) {
					//System.out.println("Found early with " + i + " cards");
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
	 * A modest 4.4% faster over <code>iterate</code> with same card count attained by:<br>
	 * No code checking for early exit possibility<br>
	 * No calling the RNG for the card after a match guaranteed not to be a duplicate<br>
	 * No temporary variable to hold the rate of finding a match<br>
	 * @return The number of matches<br>
	 */
	public int iterateFaster() {
		int tally = 0;
		boolean lastFound = false;
		
		if (r.nextFloat() < UPPER_LIMIT) {//All 22 cards are possible on first draw
			tally++;
			lastFound = true;
		}
		//loop unrolling for 4 iterates per loop did not run faster
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
		//not a virtual method, not did not run faster with the code in-lined
		iterateTracker(tally);
		return tally;
	}
	
	/**
	 * This is a control to check the way the matching is implemented versus simulating<br>
	 * a binomial distribution here with p = 1/22.<br>
	 * @return The number of matches<br>
	 */
	public int iterate1in22() {
		int tally = 0;
		for (int i=0; i<CARDCOUNT; i++)
		{
			if (r.nextFloat() < RNG_BIAS_UPPER_LIMIT) 
				tally++;
		}
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
					.append(Printer.percentAlt(tracker[i], LOOPS, Printer.PRECISION_PRINT)).append(" iterations"));
		}
		StringBuilder s10 = new StringBuilder("10 or more found: ").append(Printer.percentAlt(tracker[10], LOOPS, Printer.PRECISION_PRINT));//last index
		if (tracker[TEN_OR_MORE] > 0) {
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
times with low tier i5 CPU on Windows 10 with Java 15
-
7 minutes, 49 seconds to execute for 10,000,000 iterations at 60 cards apiece = 600,000,000 total cards
4.5443% (27266055) matches or 1 for every 22 Joker/Liberation pulls

Odds of the expected number of matches for iterations of 60 cards:

0 found: 5.3785% (537853) or 1 in 18.6 iterations
1 found: 16.8548% (1685481) or 1 in 5.9 iterations
2 found: 25.2502% (2525024) or 1 in 4 iterations
3 found: 23.8452% (2384519) or 1 in 4.2 iterations
4 found: 15.9727% (1597270) or 1 in 6.3 iterations
5 found: 8.1077% (810769) or 1 in 12.3 iterations
6 found: 3.2199% (321991) or 1 in 31.1 iterations
7 found: 1.0326% (103259) or 1 in 96.8 iterations
8 found: 0.2669% (26693) or 1 in 374.6 iterations
9 found: 0.0588% (5883) or 1 in 1699.8 iterations
10 or more found: 0.0126% (1258) or 1 in 7949.1 iterations
-
10 minutes, 10 seconds to execute for 10,000,000 iterations at 75 cards apiece = 750,000,000 total cards
4.5450% (34087217) matches or 1 for every 22 Joker/Liberation pulls

Odds of the expected number of matches for iterations of 75 cards:

0 found: 2.5819% (258189) or 1 in 38.7 iterations
1 found: 10.1502% (1015020) or 1 in 9.9 iterations
2 found: 19.1872% (1918719) or 1 in 5.2 iterations
3 found: 23.1294% (2312944) or 1 in 4.3 iterations
4 found: 20.0543% (2005430) or 1 in 5 iterations
5 found: 13.3046% (1330458) or 1 in 7.5 iterations
6 found: 7.0281% (702811) or 1 in 14.2 iterations
7 found: 3.0392% (303921) or 1 in 32.9 iterations
8 found: 1.0874% (108744) or 1 in 92 iterations
9 found: 0.3285% (32852) or 1 in 304.4 iterations
10 or more found: 0.1091% (10912) or 1 in 916.4 iterations

-
 */
