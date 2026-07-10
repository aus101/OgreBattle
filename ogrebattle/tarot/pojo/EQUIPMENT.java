package ogrebattle.tarot.pojo;

public enum EQUIPMENT {//8 hex is 8 base 10, and 5C hex is 92 base 10, meant for hex values
START(0x8), RANGE(0x5C), FORMAT_HEX(45), FORMAT_DEC(28);
	
	public final int i;
	private EQUIPMENT(int ordinal) {
		i = ordinal;
	}
}
