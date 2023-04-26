package untilz;


import static untilz.HelpMethods.nextInt;
public class Talk {
    private static final String[][] TEXTVIE;
    private static final String[] TEXTVIETASK;

    private static final String[] TEXTFINISH;

    private static final String[] TEXTGETTASK;

    public static String get(final int type, final int num) {
        try {
            if (type == 0) {
                return Talk.TEXTVIE[num][nextInt(Talk.TEXTVIE[num].length)];
            }
        } catch (Exception ex) {
        }
        return String.format("ERROR[%d:%d]", type, num);
    }

    public static String getTask(final int type, final int num) {
        try {
            if (type == 0) {
                return Talk.TEXTVIETASK[num];
            }
            if (type == 1) {
                return Talk.TEXTFINISH[num];
            }
            if (type == 2) {
                return Talk.TEXTGETTASK[num];
            }
        } catch (Exception ex) {
        }
        return String.format("ERROR[%d:%d]", type, num);
    }

    static {
        TEXTVIE = new String[][]{new String[0], new String[0], new String[0], {}, {}};
        TEXTVIETASK = new String[]{
                "Kill 2 NightBorne"//0
        };
        TEXTFINISH = new String[]{
                "Good job",// 0

        };
        TEXTGETTASK = new String[]{
                "",// 0
        };
    }
}