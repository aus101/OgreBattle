package ogrebattle.printer;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Arrays;

import ogrebattle.tarot.pojo.Tarot;
import ogrebattle.tarot.pojo.TarotSorting.AlphabeticalComparator;
import ogrebattle.tarot.simulate.TarotDeck;

/**
 * Print! Print! Print! Or return a String to print!
 */
public class Util {
	
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
	 * Print odds of hand combinations using BigDecimal to prevent floating point error<br>
	 * @param successes hands that matched given criteria
	 * @param totalHands total hands possible that were iterated through
	 * @param precision decimal places, with rounding on last decimal
	 */
	public static void percentPrint(int successes, int totalHands, int precision) {
		System.out.println(percent(successes, totalHands, precision));
	}
	
	public static void printIndex(int[] answers) {
		System.out.println("Ianuki:    " + answers[0]);
		System.out.println("Phantom:   " + answers[1]);
		System.out.println("Ice Cloud: " + answers[2]);
		System.out.println("Thunder:   " + answers[3]);
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
}
