package ogrebattle.printer;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Arrays;

import ogrebattle.tarot.exact.AllPossibleHands;
import ogrebattle.tarot.pojo.LORD;
import ogrebattle.tarot.pojo.Tarot;
import ogrebattle.tarot.pojo.TarotSorting.AlphabeticalComparator;
import ogrebattle.tarot.simulate.TarotDeck;

/**
 * Print! Print! Print! Or return a String to print!
 */
public class Util {
	public final static int NANOSECONDS_IN_1_SECOND = 1_000_000_000;
	/**
	 * Print odds of hand combinations without regard to precision or floating point error<br>
	 * @param successes hands that matched given criteria
	 * @param totalHands total hands possible that were iterated through
	 * @return String with the odds to print
	 */
	public static String percent(int successes, int totalHands) {
		return String.valueOf((double) 100 * successes / TarotDeck.getTotalHands()) + '%';
	}
	
	/**
	 * Print odds of hand combinations without regard to precision or floating point error<br>
	 * @param successes hands that matched given criteria
	 * @param totalHands total hands possible that were iterated through
	 */
	public static void percentPrint(int successes, int totalHands) {
		System.out.println(percent(successes, totalHands));
	}
	
	/**
	 * Print odds of hand combinations using BigDecimal to prevent floating point error<br>
	 * @param successes hands that matched given criteria
	 * @param totalHands total hands possible that were iterated through
	 * @param precision decimal places, with rounding on last decimal
	 * @return String with the odds to print
	 */
	public static String percent(int successes, int totalHands, int precision) {
		System.out.println(successes + " / " + totalHands);
		BigDecimal num = new BigDecimal(successes);
		BigDecimal denom = new BigDecimal(totalHands);
		return (num.multiply(new BigDecimal(100)).divide(denom, precision, RoundingMode.HALF_UP) + "%");
	}
	
	/**
	 * Print odds of hand combinations using BigDecimal to prevent floating point error with the count second<br>
	 * @param successes hands that matched given criteria
	 * @param totalHands total hands possible that were iterated through
	 * @param precision decimal places, with rounding on last decimal
	 * @return String with the odds to print
	 */
	public static String percentAlt(int successes, int totalHands, int precision) {
		BigDecimal num = new BigDecimal(successes);
		BigDecimal denom = new BigDecimal(totalHands);
		return (num.multiply(new BigDecimal(100)).divide(denom, precision, RoundingMode.HALF_UP) + "%" + " (" + successes + ")");
	}
	
	/**
	 * Print odds of hand combinations using BigDecimal to prevent floating point error<br>
	 * @param successes hands that matched given criteria
	 * @param totalHands total hands possible that were iterated through
	 * @param precision decimal places, with rounding on last decimal
	 */
	public static void percentPrint(int successes, int totalHands, int precision) {
		System.out.println(percent(successes, totalHands, precision));
	}
	
	/**
	 * Print odds of hand combinations using BigDecimal to prevent floating point error with the count second<br>
	 * @param successes hands that matched given criteria
	 * @param totalHands total hands possible that were iterated through
	 * @param precision decimal places, with rounding on last decimal
	 */
	public static void percentPrintAlt(int successes, int totalHands, int precision) {
		System.out.println(percentAlt(successes, totalHands, precision));
	}
	
	public static void printIndex(int[] answers) {
		System.out.println("Ianuki:    " + answers[LORD.IANUKI.O]);
		System.out.println("Phantom:   " + answers[LORD.PHANTOM.O]);
		System.out.println("Ice Cloud: " + answers[LORD.ICE_CLOUD.O]);
		System.out.println("Thunder:   " + answers[LORD.THUNDER.O]);
	}
	
	public static void printAnswers(int[] answers, int deckSize) {
		System.out.print("{");
		for(int i=0; i<deckSize-1; i++) {
			System.out.print(answers[i] + ",");
		}
		System.out.print(answers[deckSize-1]+"};"+System.lineSeparator());
	}
	
	public static void printAnswersByTarot(int[] answers) {
		Tarot[] values = Tarot.values();
		for(int i=0; i<values.length; i++) {
			System.out.println(//pad right
			String.format("%-" + 12 + "." + 12 + "s", String.valueOf(values[i])) + ": " + answers[i]);
			//String.format(String.valueOf(values[i])) + ": " + answers[i]);
		}
	}
	
	public static void printAnswersByTarotAlphabetical(int[] answers) {
		Tarot[] values = Tarot.values();
		Arrays.sort(values, new AlphabeticalComparator());
		for(int i=0; i<values.length; i++) {
			System.out.println(//pad right
			String.format("%-" + 12 + "." + 12 + "s", String.valueOf(values[i])) + ": " + answers[values[i].ordinal()]);			
		}
	}
	
	/**
	 * Ordered by normal Tarot card ordering so must randomize order in deep copy if dealing as individual cards<br>
	 */
	public static void printRandomHand(AllPossibleHands allHands) {
		System.out.println(allHands.returnRandomHandInList());
	}
	
	/**
	 * Ordered by normal Tarot card ordering so must randomize order in deep copy if dealing as individual cards<br>
	 */
	public static void printRandomHands(int hands, AllPossibleHands allHands) {
		for (int i = 0; i < hands; i++)
			System.out.println(allHands.returnRandomHandInList());
	}
}
