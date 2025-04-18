package ogrebattle.printer;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.NumberFormat;
import java.util.Arrays;
import java.util.Locale;

import ogrebattle.tarot.exact.AllPossibleHands;
import ogrebattle.tarot.pojo.LORD;
import ogrebattle.tarot.pojo.Tarot;
import ogrebattle.tarot.pojo.TarotSorting.AlphabeticalComparator;
import ogrebattle.tarot.simulate.TarotDeck;

/**
 * Print! Print! Print! Or return a String to print!
 */
public class Util {
	public static int PRECISION_PRINT = 4;//4 decimals
	public final static int NANOSECONDS_IN_1_SECOND = 1_000_000_000;
	private static final BigDecimal ONE_HUNDRED = new BigDecimal(100);
	private static final NumberFormat COMMAS = NumberFormat.getInstance(Locale.US);

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
	 * @param separator what to use instead of "or 1 in" for 1 in X iterations
	 * @return String with the odds to print
	 */
	public static String percentAlt(int successes, long totalHands, int precision, String separator) {
		StringBuilder sb = new StringBuilder();
		if (successes > 0) {//avoid awkward "0% (0)" and divide by 0 ArithmeticException
			BigDecimal num = new BigDecimal(successes);
			BigDecimal denom = new BigDecimal(totalHands);
			//PlainString to avoid exponents such as 1E+4 as an output
			sb.append(num.multiply(ONE_HUNDRED).divide(denom, precision, RoundingMode.HALF_UP)
					.stripTrailingZeros().toPlainString()).append("% (").append(successes).append(')').append(separator)
					.append(denom.divide(num, 1, RoundingMode.HALF_UP).stripTrailingZeros().toPlainString());
		} else {//== 0
			sb.append("0");
		}
		return sb.toString();
	}
	
	/**
	 * Print odds of hand combinations using BigDecimal to prevent floating point error with the count second<br>
	 * @param successes hands that matched given criteria
	 * @param totalHands total hands possible that were iterated through
	 * @param precision decimal places, with rounding on last decimal
	 * @return String with the odds to print
	 */
	public static String percentAlt(int successes, long totalHands, int precision) {
		return percentAlt(successes, totalHands, precision, " or 1 in ");
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
	
	/**
	 * @param nanoTime the time as a long in nanoseconds, such as the difference with 2 System.nanoTime() calls
	 * @return String in human readable minutes and seconds with < 1 second as "less than 1 second"
	 */
	public static String nanoToMinutesSeconds(long nanoTime) {
		long seconds = nanoTime / NANOSECONDS_IN_1_SECOND;
		StringBuilder sb = new StringBuilder();
		if (seconds < 60) {
			if (seconds < 1) {
				sb.append("less than 1 second");
			} else if (seconds > 1) {
				sb.append(seconds + " seconds");
			} else {//== 1
				sb.append("1 second");
			} 
		} else {
			long minutes = seconds / 60;
			long remainder = seconds % 60;
			if (minutes > 1) {
				sb.append(minutes).append(" minutes");
			} else {
				sb.append(minutes).append(" minute");
			}
			if (remainder > 0) {
				if (remainder > 1) {
					sb.append(", ").append(remainder).append(" seconds");
				} else {//== 1
					sb.append(", 1 second");
				}
			}
		}
		return sb.toString();
	}
	/*
	 * 43064913176100 717 minutes, 44 seconds
	 * 43141650708600 719 minutes, 1 second
	 * 42960766596100 716 minutes
	 */
	
	/**
	 * @param number the number as an int
	 * @return number as String with commas spacing
	 */
	public static String numberSeparator(int number) {
		return COMMAS.format(number);
	}
	
	/**
	 * @param number the number as a long
	 * @return number as String with commas spacing
	 */
	public static String numberSeparator(long number) {
		return COMMAS.format(number);
	}
	
/**
 * @param number the number as an int
 * @param spacer the spacer every 3 digits, i.e. "." returns 10.000 for 10000
 * @return number as String with custom point spacing
 */
	public static String numberSeparatorCustom(int number, String spacer) {
		return COMMAS.format(number).replaceAll(",", spacer);
	}
	
	/**
	 * @param number the number as a long
	 * @param spacer the spacer every 3 digits, i.e. "." returns 10.000 for 10000
	 * @return number as String with custom point spacing
	 */
	public static String numberSeparatorCustom(long number, String spacer) {
		return COMMAS.format(number).replaceAll(",", spacer);
	}
}
