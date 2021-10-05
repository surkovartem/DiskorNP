package ru.rzd.discor.diskorReportConstReportBack;
import static java.lang.Math.pow;

public class ConvertColumnNameFunctions {
    public static String convertNumToColString(int num) {
        int excelColNum = num + 1;
        StringBuilder colRef = new StringBuilder(2);
        int colRemain = excelColNum;

        while (colRemain > 0) {
            int thisPart = colRemain % 26;
            if (thisPart == 0) {
                thisPart = 26;
            }
            colRemain = (colRemain - thisPart) / 26;

            // The letter A is at 65
            char colChar = (char) (thisPart + 64);
            colRef.insert(0, colChar);
        }

        return colRef.toString();
    }

    public static Integer convertColToNumString(String col) {
        char[] charArray = col.toCharArray();
        int columnNumber = 0;
        for (int i = 0; i < charArray.length; i++) {
            // The letter A is at 65
            columnNumber += (charArray[i] - 64) * pow (26, charArray.length - i - 1);
        }
        return --columnNumber;
    }
}
