package ogrebattle.util;

import ogrebattle.tarot.pojo.LORD;
import ogrebattle.tarot.pojo.Tarot;

public class Util {
	public final static int NANOSECONDS_IN_1_SECOND = 1_000_000_000;
	public final static int RNG_RANGE = 0x100;//100 in hex is 256 base 10 but used for hex values
	//one answer set for each lord type with multiple optimal answer sets, chosen for max 1's
	protected static final int[] IANUKI_BASE        = new int[]{1,1,2,1,2,2,3,2,2,1,2,2,3,1,2,1,2,3,2,1,2,2};//final can still be modified
	protected static final int[] ICE_CLOUD_BASE     = new int[]{3,1,2,2,1,1,2,1,3,3,1,3,1,1,3,3,1,2,3,3,3,2};//just not reallocated with = new
	protected static final int[] IANUKI_SFC_BASE    = new int[]{2,1,1,2,3,3,2,2,1,2,2,3,1,2,1,2,3,2,1,2,2,2};
	protected static final int[] ICE_CLOUD_SFC_BASE = new int[]{3,1,3,1,1,2,1,3,3,1,3,1,1,3,3,1,2,3,3,3,2,1};
	
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
}
