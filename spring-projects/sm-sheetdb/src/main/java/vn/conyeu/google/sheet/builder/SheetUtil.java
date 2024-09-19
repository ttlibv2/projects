package vn.conyeu.google.sheet.builder;

import vn.conyeu.google.xsldb.SheetTb;

import java.time.format.DateTimeFormatter;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SheetUtil {

    /**
     * The character ($) that signifies a row or column value is absolute instead of relative
     */
    public static final char ABSOLUTE_REFERENCE_MARKER = '$';

    /**
     * The character (!) that separates sheet names from cell references
     */
    public static final char SHEET_NAME_DELIMITER = '!';

    /**
     * The character (') used to quote sheet names when they contain special characters
     */
    public static final char SPECIAL_NAME_DELIMITER = '\'';

    /**
     * Matches a run of one or more letters followed by a run of one or more digits.
     * Both the letter and number groups are optional.
     * The run of letters is group 1 and the run of digits is group 2.
     * Each group may optionally be prefixed with a single '$'.
     */
    public static final Pattern CELL_REF_PATTERN = Pattern.compile("(\\$?[A-Z]+)?" + "(\\$?[0-9]+)?", Pattern.CASE_INSENSITIVE);

    /**
     * Matches references only where row and column are included.
     * Matches a run of one or more letters followed by a run of one or more digits.
     * If a reference does not match this pattern, it might match COLUMN_REF_PATTERN or ROW_REF_PATTERN
     * References may optionally include a single '$' before each group, but these are excluded from the Matcher.group(int).
     */
    public static final Pattern STRICTLY_CELL_REF_PATTERN = Pattern.compile("\\$?([A-Z]+)" + "\\$?([0-9]+)", Pattern.CASE_INSENSITIVE);

    /**
     * Matches a run of one or more letters.  The run of letters is group 1.
     * References may optionally include a single '$' before the group, but these are excluded from the Matcher.group(int).
     */
    public static final Pattern COLUMN_REF_PATTERN = Pattern.compile("\\$?([A-Z]+)", Pattern.CASE_INSENSITIVE);

    /**
     * Matches a run of one or more letters.  The run of numbers is group 1.
     * References may optionally include a single '$' before the group, but these are excluded from the Matcher.group(int).
     */
    public static final Pattern ROW_REF_PATTERN = Pattern.compile("\\$?([0-9]+)");

    /**
     * Named range names must start with a letter or underscore.  Subsequent characters may include
     * digits or dot.  (They can even end in dot).
     */
    public static final Pattern NAMED_RANGE_NAME_PATTERN = Pattern.compile("[_A-Z][_.A-Z0-9]*", Pattern.CASE_INSENSITIVE);

    public static final DateTimeFormatter ISO_DATETIME = DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm:ss");
    public static final DateTimeFormatter ISO_DATE = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    public static final DateTimeFormatter ISO_TIME = DateTimeFormatter.ofPattern("hh:mm:ss");

    /**
     * @param colIndex The column index. Use 0-index
     */
    public static String numberToLetter(int colIndex) {
        int excelColNum = colIndex + 1;
        StringBuilder colRef = new StringBuilder();
        int colRemain = excelColNum;

        while (colRemain > 0) {
            int thisPart = colRemain % 26;
            if (thisPart == 0) {
                thisPart = 26;
            }

            // The letter A is at 65
            char colChar = (char) (thisPart + 64);
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
     *
     * @return zero based column index
     */
    public static int letterToNumber(String ref) {
        int pos = 0;
        int retval = 0;
        for (int k = ref.length() - 1; k >= 0; k--) {
            char thechar = ref.charAt(k);
            if (thechar == '$') {
                if (k != 0) throw new IllegalArgumentException("Bad col ref format '" + ref + "'");
                break;
            }

            // Character.getNumericValue() returns the values
            //  10-35 for the letter A-Z
            int shift = (int) Math.pow(26, pos);
            retval += (Character.getNumericValue(thechar) - 9) * shift;
            pos++;
        }
        return retval - 1;
    }


    /**
     * Separates the sheet name, row, and columns from a cell reference string.
     *
     * @param reference is a string that identifies a cell within the sheet or workbook
     *                  reference may not refer to a cell in an external workbook
     *                  reference may be absolute or relative.
     * @return String array of sheetName, column (in ALPHA-26 format), and row
     * output column or row elements will contain absolute reference markers if they
     * existed in the input reference.
     */
    public static String parseSheetName(String reference, int plingPos) {
        if (plingPos < 0) return null;

        boolean isQuoted = reference.charAt(0) == SPECIAL_NAME_DELIMITER;
        if (!isQuoted) {
            // sheet names with spaces must be quoted
            if (!reference.contains(" ")) {
                return reference.substring(0, plingPos);
            } else {
                throw new IllegalArgumentException("Sheet names containing spaces must be quoted: (" + reference + ")");
            }
        }

        int lastQuotePos = plingPos - 1;
        if (reference.charAt(lastQuotePos) != SPECIAL_NAME_DELIMITER) {
            throw new IllegalArgumentException("Mismatched quotes: (" + reference + ")");
        }

        // TODO - refactor cell reference parsing logic to one place.
        // Current known incarnations:
        //   FormulaParser.GetName()
        //   CellReference.parseSheetName() (here)
        //   AreaReference.separateAreaRefs()
        //   SheetNameFormatter.format() (inverse)

        StringBuilder sb = new StringBuilder(plingPos);

        for (int i = 1; i < lastQuotePos; i++) { // Note boundaries - skip outer quotes
            char ch = reference.charAt(i);
            if (ch != SPECIAL_NAME_DELIMITER) {
                sb.append(ch);
                continue;
            }
            if (i + 1 < lastQuotePos && reference.charAt(i + 1) == SPECIAL_NAME_DELIMITER) {
                // two consecutive quotes is the escape sequence for a single one
                i++; // skip this and keep parsing the special name
                sb.append(ch);
                continue;
            }
            throw new IllegalArgumentException("Bad sheet name quote escaping: (" + reference + ")");
        }
        return sb.toString();

    }

    public static void appendFormatSheetName(StringBuilder sb, String sheetName) {
        SheetNameFormatter.appendFormat(sb, sheetName);
    }

    public static class SheetNameFormatter {

        /**
         * Matches a single cell ref with no absolute ('$') markers
         */
        private static final Pattern CELL_REF_PATTERN = Pattern.compile("([A-Za-z]+)([0-9]+)");

        private SheetNameFormatter() {
            // no instances of this class
        }
        /**
         * Used to format sheet names as they would appear in cell formula expressions.
         * @return the sheet name unchanged if there is no need for delimiting.  Otherwise the sheet
         * name is enclosed in single quotes (').  Any single quotes which were already present in the
         * sheet name will be converted to double single quotes ('').
         */
        public static String format(String rawSheetName) {
            StringBuilder sb = new StringBuilder((rawSheetName == null ? 0 : rawSheetName.length()) + 2);
            appendFormat(sb, rawSheetName);
            return sb.toString();
        }

        /**
         * Convenience method for ({@link #format(String)}) when a StringBuffer is already available.
         *
         * @param out - sheet name will be appended here possibly with delimiting quotes
         * @param rawSheetName - sheet name
         */
        public static void appendFormat(Appendable out, String rawSheetName) {
            try {
                boolean needsQuotes = needsDelimiting(rawSheetName);
                if(needsQuotes) {
                    out.append(SPECIAL_NAME_DELIMITER);
                    appendAndEscape(out, rawSheetName);
                    out.append(SPECIAL_NAME_DELIMITER);
                } else {
                    appendAndEscape(out, rawSheetName);
                }
            } catch (Exception e) {
                throw new IllegalStateException(e);
            }
        }

        /**
         * Convenience method for ({@link #format(String)}) when a StringBuffer is already available.
         *
         * @param out - sheet name will be appended here possibly with delimiting quotes
         * @param workbookName - workbook name
         * @param rawSheetName - sheet name
         */
        public static void appendFormat(StringBuilder out, String workbookName, String rawSheetName) {
            try {
                boolean needsQuotes = needsDelimiting(workbookName) || needsDelimiting(rawSheetName);
                if(needsQuotes) {
                    out.append(SPECIAL_NAME_DELIMITER);
                    out.append('[');
                    appendAndEscape(out, workbookName.replace('[', '(').replace(']', ')'));
                    out.append(']');
                    appendAndEscape(out, rawSheetName);
                    out.append(SPECIAL_NAME_DELIMITER);
                } else {
                    out.append('[');
                    appendOrREF(out, workbookName);
                    out.append(']');
                    appendOrREF(out, rawSheetName);
                }
            } catch (Exception e) {
                throw new IllegalStateException(e);
            }
        }

        private static void appendOrREF(StringBuilder out, String name) {
            out.append(name == null ? "#REF" : name);
        }

        static void appendAndEscape(Appendable sb, String rawSheetName) {
            try {
                if (rawSheetName == null) {
                    sb.append("#REF");
                    return;
                }

                int len = rawSheetName.length();
                for (int i = 0; i < len; i++) {
                    char ch = rawSheetName.charAt(i);
                    if (ch == SPECIAL_NAME_DELIMITER) {
                        // single quotes (') are encoded as ('')
                        sb.append(SPECIAL_NAME_DELIMITER);
                    }
                    sb.append(ch);
                }
            } catch (Exception e) {
                throw new IllegalStateException(e);
            }
        }

        /**
         * Tell if the given raw sheet name needs screening/delimiting.
         * @param rawSheetName the sheet name.
         * @return true if the given raw sheet name needs screening/delimiting, false otherwise or
         *          if the sheet name is null.
         */
        static boolean needsDelimiting(String rawSheetName) {
            if(rawSheetName == null) {
                return false;
            }

            int len = rawSheetName.length();
            if(len < 1) {
                return false; // some cases we get missing external references, resulting in empty sheet names
            }
            if(Character.isDigit(rawSheetName.charAt(0))) {
                // sheet name with digit in the first position always requires delimiting
                return true;
            }
            for(int i=0; i<len; i++) {
                char ch = rawSheetName.charAt(i);
                if(isSpecialChar(ch)) {
                    return true;
                }
            }
            if(Character.isLetter(rawSheetName.charAt(0))
                    && Character.isDigit(rawSheetName.charAt(len-1))) {
                // note - values like "A$1:$C$20" don't get this far
               // if(nameLooksLikePlainCellReference(rawSheetName)) {
                    return true;
                //}
            }
            if (nameLooksLikeBooleanLiteral(rawSheetName)) {
                return true;
            }
            return nameStartsWithR1C1CellReference(rawSheetName);
        }

        private static boolean nameLooksLikeBooleanLiteral(String rawSheetName) {
            return switch (rawSheetName.charAt(0)) {
                case 'T', 't' -> "TRUE".equalsIgnoreCase(rawSheetName);
                case 'F', 'f' -> "FALSE".equalsIgnoreCase(rawSheetName);
                default -> false;
            };
        }

        /**
         * @return {@code true} if the presence of the specified character in a sheet name would
         * require the sheet name to be delimited in formulas.  This includes every non-alphanumeric
         * character besides underscore '_' and dot '.'.
         */
        /* package */ static boolean isSpecialChar(char ch) {
            // note - Character.isJavaIdentifierPart() would allow dollars '$'
            if(Character.isLetterOrDigit(ch)) {
                return false;
            }
            return switch (ch) { // dot is OK
                case '.', '_' -> // underscore is OK
                        false;
                case '\n', '\r', '\t' -> throw new IllegalStateException("Illegal character (0x"
                        + Integer.toHexString(ch) + ") found in sheet name");
                default -> true;
            };
        }


        /**
         * Checks if the sheet name starts with R1C1 style cell reference.
         * If this is the case Excel requires the sheet name to be enclosed in single quotes.
         * @return {@code true} if the specified rawSheetName starts with R1C1 style cell reference
         */
        static boolean nameStartsWithR1C1CellReference(String rawSheetName) {
            int len = rawSheetName.length();
            char firstChar = rawSheetName.charAt(0);
            if (firstChar == 'R' || firstChar == 'r') {
                if (len > 1) {
                    char secondChar = rawSheetName.charAt(1);
                    if (secondChar == 'C' || secondChar == 'c') {
                        if (len > 2) {
                            char thirdChar = rawSheetName.charAt(2);
                            return Character.isDigit(thirdChar);
                        } else {
                            return true;
                        }
                    } else {
                        return Character.isDigit(secondChar);
                    }
                } else {
                    return true;
                }
            } else if (firstChar == 'C' || firstChar == 'c') {
                if (len > 1) {
                    char secondChar = rawSheetName.charAt(1);
                    return Character.isDigit(secondChar);
                } else {
                    return true;
                }
            }
            return false;
        }
    }
}