package ogrebattle.printer;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.NumberFormat;
import java.util.Arrays;
import java.util.Locale;

import ogrebattle.tarot.generator.AllPossibleHands;
import ogrebattle.tarot.pojo.LORD;
import ogrebattle.tarot.pojo.Tarot;
import ogrebattle.tarot.pojo.TarotAnswers;
import ogrebattle.tarot.pojo.TarotBonusCardStats;
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
	public static final String newLine = System.lineSeparator();
	private static final Tarot[] VALUES = Tarot.values();
	//one answer set for each lord type with multiple optimal answer sets, chosen for max 1's
	protected static final int[] IANUKI_BASE        = new int[]{1,1,2,1,2,2,3,2,2,1,2,2,3,1,2,1,2,3,2,1,2,2};//final can still be modified
	protected static final int[] ICE_CLOUD_BASE     = new int[]{3,1,2,2,1,1,2,1,3,3,1,3,1,1,3,3,1,2,3,3,3,2};//just not reallocated with = new
	protected static final int[] IANUKI_SFC_BASE    = new int[]{2,1,1,2,3,3,2,2,1,2,2,3,1,2,1,2,3,2,1,2,2,2};
	protected static final int[] ICE_CLOUD_SFC_BASE = new int[]{3,1,2,2,1,1,2,1,3,3,1,3,1,1,3,3,1,2,3,3,3,2};
	
	public static String matrixStringBuilder(int[][] holder) {
		StringBuilder sb = new StringBuilder();
		for (int[] i : holder) {
			arrayStringBuilder(i, sb);
		}
		return sb.toString();
	}
	
	public static void arrayStringBuilder(int[] holder, StringBuilder builder) {
		builder.append("[");
		for(int i=0; i<holder.length-1; i++) {
			builder.append(holder[i]).append(",");
		}
		builder.append(holder[holder.length-1]).append("]").append(newLine);
	}
	
	public static void printArray(int[] holder) {
		StringBuilder sb = new StringBuilder("[");
		for(int i=0; i<holder.length-1; i++) {
			sb.append(holder[i]).append(",");
		}
		sb.append(holder[holder.length-1]).append("]");
		System.out.println(sb.toString());
	}
	
	public static int[] IanukiBaseDeepCopy() {
		int[] temp = new int[Tarot.DECK_SIZE];
		System.arraycopy(IANUKI_BASE, 0, temp, 0, Tarot.DECK_SIZE);
		return temp;
	}
	
	public static int[] IceCloudBaseDeepCopy() {
		int[] temp = new int[Tarot.DECK_SIZE];
		System.arraycopy(ICE_CLOUD_BASE, 0, temp, 0, Tarot.DECK_SIZE);
		return temp;
	}
	
	public static int[] IanukiSFCBaseDeepCopy() {
		int[] temp = new int[Tarot.DECK_SIZE];
		System.arraycopy(IANUKI_SFC_BASE, 0, temp, 0, Tarot.DECK_SIZE);
		return temp;
	}

	public static int[] IceCloudSFCBaseDeepCopy() {
		int[] temp = new int[Tarot.DECK_SIZE];
		System.arraycopy(ICE_CLOUD_SFC_BASE, 0, temp, 0, Tarot.DECK_SIZE);
		return temp;
	}
	
	public static int[] lordToInt(LORD... lordOrder) {
		int[] order = null;
		if (lordOrder != null) {
			order = new int[lordOrder.length];
			for(int i=0; i<lordOrder.length; i++) {
				order[i] = lordOrder[i].O;
			}
		}
		return order;
	}
	
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
		return percentCalc(successes, totalHands, precision);
	}
	
	/**
	 * Exact odds of hand combinations using BigDecimal to prevent floating point error.<br>
	 * Compare to simulated odds in <code>ogrebattle.tarot.simulate.OddsExample.java<code>.<br>
	 * @param successes hands that matched given criteria
	 * @param totalHands total hands possible that were iterated through
	 * @param precision decimal places, with rounding on last decimal
	 * @return the percentage as a String to print
	 */
	public static String percentCalc(int successes, int totalHands, int precision) {
		String percent;
		if (successes != totalHands) {
			BigDecimal num = new BigDecimal(successes);
			BigDecimal denom = new BigDecimal(totalHands);
			percent = (num.multiply(ONE_HUNDRED).divide(denom, precision, RoundingMode.HALF_UP) + "%");
			if (percent.equals("100.00%")) percent = "99.99%";//prevents 99.995 and greater from rounding up to 100%, overriding precision
		} else percent = "100%";//display 100% instead of 100.00%
		return percent;
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
	
	public static void printIndex(TarotAnswers solution) {
		printIndex(solution.getAnswers());
	}
	
	public static void printIndex(int[] answers) {
		String s = newLine;
		System.out.println(new StringBuilder
				       ("Ianuki:    ").append(answers[LORD.IANUKI.O]).append(s)
				.append("Phantom:   ").append(answers[LORD.PHANTOM.O]).append(s)
				.append("Ice Cloud: ").append(answers[LORD.ICE_CLOUD.O]).append(s)
				.append("Thunder:   ").append(answers[LORD.THUNDER.O]).append(s));
	}
	
	public static void printAnswers(TarotAnswers solution, int deckSize) {
		printAnswers(solution.getAnswers());
	}
	
	public static void printAnswers(int[] answers) {
		StringBuilder sb = new StringBuilder("{");
		for(int i=0; i<Tarot.DECK_SIZE-1; i++) {
			sb.append(answers[i]).append(",");
		}
		sb.append(answers[Tarot.DECK_SIZE-1]).append("};").append(newLine);
		System.out.println(sb.toString());
	}
	
	public static void printAnswers(int[] answers, int solutionSize) {
		StringBuilder sb = new StringBuilder("{");
		for(int i=0; i<solutionSize-1; i++) {
			sb.append(answers[i]).append(",");
		}
		sb.append(answers[solutionSize-1]).append("};").append(newLine);
		System.out.println(sb.toString());
	}

	
	public static void printAnswersByTarot(TarotAnswers solution) {
		printAnswersByTarot(solution.getAnswers());
	}
	
	public static void printAnswersByTarot(int[] answers) {
		StringBuilder sb = new StringBuilder();
		for(int i=0; i<Tarot.DECK_SIZE; i++) {
			sb.append(//pad right
					String.format("%-" + 12 + "." + 12 + "s", String.valueOf(VALUES[i])))
					.append(": ").append(answers[i]).append(Util.newLine);
			//String.format(String.valueOf(values[i])) + ": " + answers[i]);
		}
		System.out.println(sb.toString());
	}
	
	public static void printAnswersByGroup(TarotAnswers solution) {
		printAnswersByGroup(solution.getAnswers());
	}

	public static void printAnswersByGroup(int[] answers) {
		int[] ones = new int[Tarot.DECK_SIZE];//in case all answers are the same
		int[] twos = new int[Tarot.DECK_SIZE];
		int[] threes = new int[Tarot.DECK_SIZE];
		boolean onesInit = false;//whole reason is to prevent the println when there are no answers to print
		boolean twosInit = false;
		boolean threesInit = false;
		//4x loops in linear time are good enough versus making a new comparator on new data type for (n)(log n) sort + 1 print loop
		for(int i=0; i<Tarot.DECK_SIZE; i++) {
			if (answers[i] == 1) {
				ones[i] = answers[i];
				if (!onesInit) {
					onesInit = true;
				}
			} else if (answers[i] == 2) {
				twos[i] = answers[i];
				if (!twosInit) {
					twosInit = true;
				}
			} else {
				threes[i] = answers[i];
				if (!threesInit) {
					threesInit = true;
				}
			}
		}
		
		StringBuilder sb = new StringBuilder();
		if (onesInit) {
			sb.append(Util.newLine);
			for(int i=0; i<Tarot.DECK_SIZE; i++) {
				if (ones[i] != 0) {
					sb.append(//pad right
							String.format("%-" + 12 + "." + 12 + "s", String.valueOf(VALUES[i])))
							.append(": ").append(ones[i]).append(Util.newLine);
				}
			}
		}
		if (twosInit) {
			sb.append(Util.newLine);
			for(int i=0; i<Tarot.DECK_SIZE; i++) {
				if (twos[i] != 0) {
					sb.append(//pad right
							String.format("%-" + 12 + "." + 12 + "s", String.valueOf(VALUES[i])))
							.append(": ").append(twos[i]).append(Util.newLine);
				}
			}
		}
		if (threesInit) {
			sb.append(Util.newLine);
			for(int i=0; i<Tarot.DECK_SIZE; i++) {
				if (threes[i] != 0) {
					sb.append(//pad right
							String.format("%-" + 12 + "." + 12 + "s", String.valueOf(VALUES[i])))
							.append(": ").append(threes[i]).append(Util.newLine);
				}
			}
		}
		System.out.println(sb.toString());
	}
	
	public static void printTarotHexValues() {
		StringBuilder sb = new StringBuilder();
		for(int i=0; i<VALUES.length; i++) {
			sb.append("0x");
			if (i < 16) // < 0x10
			{
				sb.append("0");
			}
			sb.append(Integer.toHexString(i).toUpperCase()).append(" ")
				.append(String.valueOf(VALUES[i])).append(newLine);
		}
		System.out.println(sb.toString());
	}
	
	public static void printTarotBonusCardStats(Tarot tarot) {
		printTBCS(TarotBonusCardStats.getValues(tarot), ":  ", tarot.toString());
	}
	
	public static void printTarotBonusCardStatsPlus(Tarot tarot) {
		printTBCS(TarotBonusCardStats.getValues(tarot), ": +", tarot.toString());
	}
	
	public static void printTarotBonusCardStats(TarotBonusCardStats tarot) {
		printTBCS(tarot.getValues(), ":  ", tarot.toString());
	}
	
	public static void printTarotBonusCardStatsPlus(TarotBonusCardStats tarot) {
		printTBCS(tarot.getValues(), ": +", tarot.toString());
	}
	
	public static void printAllTarotBonusCardStats() {
		printAllTBCS(":  ");
	}
	
	public static void printAllTarotBonusCardStatsPlus() {
		printAllTBCS(": +");
	}
	
	private static void printTBCS(int[] tarot, String symbol, String name) {
		StringBuilder sb = new StringBuilder(name).append(newLine);
		for(int t=0; t<tarot.length; t++) {
			String spacer = (tarot[t] >= 0) ? symbol : ": ";//alignment of positive and negative numbers
			if (t == 0) spacer = " " + spacer;//for HP having 2 letters
			sb.append(TarotBonusCardStats.STATS.get(t)).append(spacer).append(tarot[t]).append(newLine);
		}
		//want ending line separator to have a line between each Tarot card
		System.out.println(sb.toString());
	}
	
	//could just call the single print in a for loop with the enum's .values() array but 1 StringBuilder for 1 println is cleaner
	private static void printAllTBCS(String symbol) {
		TarotBonusCardStats[] allCards = TarotBonusCardStats.values();
		StringBuilder sb = new StringBuilder();
		
		for(int c=0; c<Tarot.DECK_SIZE; c++) {
			sb.append(allCards[c].toString()).append(newLine);//appending the name
			int tarot[] = allCards[c].getValues();
			
			for(int t=0; t<tarot.length; t++) {
				String spacer = (tarot[t] >= 0) ? symbol : ": ";//alignment of positive and negative numbers
				if (t == 0) spacer = " " + spacer;//for HP having 2 letters
				sb.append(TarotBonusCardStats.STATS.get(t)).append(spacer).append(tarot[t]).append(newLine);
			}
			//want ending line separator to have a line between each Tarot card
			if (c < Tarot.DECK_SIZE-1)//except not at World the last card
				sb.append(newLine);
		}
		sb.replace(sb.length()-2, sb.length(), "");//remove ending new line
		System.out.println(sb.toString());
	}
	
	public static void printAnswersByTarotAlphabetical(TarotAnswers solution) {
		printAnswersByTarotAlphabetical(solution.getAnswers());
	}
	
	public static void printAnswersByTarotAlphabetical(int[] answers) {
		Tarot[] values = Tarot.values();//deep copy due to sorting
		Arrays.sort(values, new AlphabeticalComparator());
		StringBuilder sb = new StringBuilder();
		for(int i=0; i<Tarot.DECK_SIZE; i++) {
			sb.append(//pad right
			String.format("%-" + 12 + "." + 12 + "s", String.valueOf(values[i])))
			.append(": ").append(answers[values[i].ordinal()]).append(Util.newLine);			
		}
		System.out.println(sb.toString());
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
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < hands; i++) {
			 sb.append(allHands.returnRandomHandInList());
		}
		System.out.println(sb.toString());
	}
	
	public String lordOrder(int... order) {//loop is overkill and more complicated given text difference
		StringBuilder sb = new StringBuilder();
		if (order.length >= 1)
			sb.append("First Match: ").append(LORD.reverse(order[0]).toString());
		if (order.length >= 2)
			sb.append(", Second Match: ").append(LORD.reverse(order[1]).toString());
		if (order.length >= 3)
			sb.append(", Third Match: ").append(LORD.reverse(order[2]).toString());
		if (order.length >= 4)
			sb.append(", Fourth Match: ").append(LORD.reverse(order[3]).toString());
		
		return sb.toString();
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
