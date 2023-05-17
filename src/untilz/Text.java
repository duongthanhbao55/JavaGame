package untilz;

public class Text {
	private static final String[] TEXTVIE;

	public static String get(final int type, final int num) {
		try {
			if (type == 0) {
				return Text.TEXTVIE[num];
			}
		} catch (Exception ex) {
		}
		return String.format("ERROR[%d:%d]", type, num);
	}

	static {
		TEXTVIE = new String[] {
				"Accept", // 0
				"Refuse", // 1
				"Receive",// 2
				"unequip",//3
				"drop",//4
				"use",//5
				"sell"//6
		};
	}
}
