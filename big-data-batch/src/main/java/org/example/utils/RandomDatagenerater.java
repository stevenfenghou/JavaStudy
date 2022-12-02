package org.example.utils;

import java.util.Random;

public final class RandomDatagenerater {
    private final static Random RANDOM = new Random();
    private final static int ASCII_TABLE_SIZE = 52;
    private final static char[] ASCII_TABLE = new char[ASCII_TABLE_SIZE];
    private final static int CHINESS_CHAR_TABLE_SIZE = 100;
    private final static char[] CHINESS_CHAR_TABLE = new char[CHINESS_CHAR_TABLE_SIZE];

    static {
        intAsciiTable();
        intChinessCharTable();
    }

    private static void intAsciiTable () {
        for (int i = 0; i < 26; i++) {
            ASCII_TABLE[i] = (char) (i + 65);
        }
        for (int i = 0; i < 26; i++) {
            ASCII_TABLE[i + 26] = (char) (i + 97);
        }
    }

    private static void intChinessCharTable () {
        String string = "子曰学而时习之不亦说乎有朋自远方来不亦乐乎人不知而不愠不亦君子乎有" +
                "子曰其为人也孝弟而好犯上者鲜矣不好犯上而好作乱者未之有也君子务本本立而道生孝弟也者其为仁之本与" +
                "子曰巧言令色鲜矣仁" +
                "曾子曰吾日三省吾身为人谋而不忠乎与朋友交而不信乎传不习乎" +
                "子曰道千乘之国敬事而信节用而爱人使民以时";
        for (int i = 0; i < CHINESS_CHAR_TABLE_SIZE; i++) {
            CHINESS_CHAR_TABLE[i] = string.charAt(i);
        }
    }

    private RandomDatagenerater() {
    }

    public static int randomInt(int bound) {
        return RANDOM.nextInt(bound);
    }

    public static String randomAsciiString(int minLength, int maxLength) {
        int size = minLength + RANDOM.nextInt(maxLength - minLength + 1);
        StringBuilder sb = new StringBuilder(size);
        for (int i = 0; i < size; i++) {
            sb.append(randomAsciiChar());
        }
        return sb.toString();
    }

    private static char randomAsciiChar() {
        return ASCII_TABLE[RANDOM.nextInt(ASCII_TABLE_SIZE)];
    }

    public static String randomChineseCharString(int minLength, int maxLength) {
        int size = minLength + RANDOM.nextInt(maxLength - minLength + 1);
        StringBuilder sb = new StringBuilder(size);
        for (int i = 0; i < size; i++) {
            sb.append(randomChineseChar());
        }
        return sb.toString();
    }
    private static char randomChineseChar() {
        return CHINESS_CHAR_TABLE[RANDOM.nextInt(CHINESS_CHAR_TABLE_SIZE)];
    }
}
