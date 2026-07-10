package ogrebattle.tarot.pojo;

public enum CONSUMABLES {//97 hex is 151 base 10, and 1F hex is 31 base 10, meant for hex values
START(0x97), RANGE(0x1F), FORMAT_HEX(133), FORMAT_DEC(83);//No RNG bias if range is unbugged 0x20 (32)
	
	public final int i;
	private CONSUMABLES(int ordinal) {
		i = ordinal;
	}
}
