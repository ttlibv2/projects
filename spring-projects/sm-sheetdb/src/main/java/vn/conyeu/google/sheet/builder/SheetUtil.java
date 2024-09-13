package vn.conyeu.google.sheet.builder;

import java.time.format.DateTimeFormatter;

public class SheetUtil {

    public static final DateTimeFormatter ISO_DATETIME = DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm:ss");
    public static final DateTimeFormatter ISO_DATE = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    public static final DateTimeFormatter ISO_TIME = DateTimeFormatter.ofPattern("hh:mm:ss");

    /**
     * @param colIndex The column index. Use 0-index
     * */
    public static String numberToLetter(int colIndex) {
        int excelColNum = colIndex + 1;
        StringBuilder colRef = new StringBuilder();
        int colRemain = excelColNum;

        while(colRemain > 0) {
            int thisPart = colRemain % 26;
            if(thisPart == 0) { thisPart = 26; }

            // The letter A is at 65
            char colChar = (char)(thisPart+64);
            colRef.insert(0, colChar);
            colRemain = (colRemain - thisPart) / 26;
        }

        return colRef.toString();
    }

    /**
     * takes in a column reference portion of a CellRef and converts it from
     * ALPHA-26 number format to 0-based base 10.
     * 'A' -> 0
     * 'Z' -> 25
     * 'AA' -> 26
     * 'IV' -> 255
     * @return zero based column index
     */
    public static int letterToNumber(String ref) {
        int pos = 0;
        int retval=0;
        for (int k = ref.length()-1; k >= 0; k--) {
            char thechar = ref.charAt(k);
            if (thechar == '$') {
                if (k != 0) throw new IllegalArgumentException("Bad col ref format '" + ref + "'");
                break;
            }

            // Character.getNumericValue() returns the values
            //  10-35 for the letter A-Z
            int shift = (int)Math.pow(26, pos);
            retval += (Character.getNumericValue(thechar)-9) * shift;
            pos++;
        }
        return retval-1;
    }
}