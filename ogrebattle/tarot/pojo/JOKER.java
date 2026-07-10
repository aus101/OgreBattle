package ogrebattle.tarot.pojo;

/**
 * Also for drawing from Liberation. No redraw effect.
 */
public enum JOKER {//0 hex is 0 base 10, and 16 hex is 22 base 10, meant for hex values
	START(0x0), RANGE(Tarot.DECK_SIZE), FORMAT_HEX(187), FORMAT_DEC(117);
	
	public final int i;
	private JOKER(int ordinal) {
		i = ordinal;
	}
}
