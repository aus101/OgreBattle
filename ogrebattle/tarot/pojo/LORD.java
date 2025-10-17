package ogrebattle.tarot.pojo;

public enum LORD {
	IANUKI(0), PHANTOM(1), ICE_CLOUD(2), THUNDER(3);//equivalent to <ENUM>.ordinal() but being explicit
	
	public final int O;
	private LORD(int ordinal) {
		O = ordinal;
	}
	
	public static LORD reverse(int o) {
		LORD reverse;
		switch (o)//Java 12 can return in the switch and not need breaks but let's not force Java 12+
		{
		    case 0: reverse = IANUKI;
		        break;
		    case 1: reverse = PHANTOM;
		        // code to execute if expression equals value2
		        break;
		    case 2: reverse = ICE_CLOUD;
		    	break;
		    case 3: reverse = THUNDER;
		    	break;
		    default: reverse = null;
		}
		return reverse;
	}
	
	public static void main(String[] args) {
		LORD i = IANUKI;
		System.out.println(i);
		System.out.println(i.O);
		System.out.println(reverse(i.O));
		LORD j = IANUKI;
		System.out.println(i == j);
	}
	/**
	 *IANUKI
     *0
     *IANUKI
     *true
	 */
}