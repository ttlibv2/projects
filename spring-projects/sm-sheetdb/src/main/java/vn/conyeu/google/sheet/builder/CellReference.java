package vn.conyeu.google.sheet.builder;

import vn.conyeu.commons.utils.Objects;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * <p>Common conversion functions between Excel style A1, C27 style
 *  cell references, and POI usermodel style row=0, column=0
 *  style references. Handles sheet-based and sheet-free references
 *  as well, eg "Sheet1!A1" and "$B$72"</p>
 */
public class CellReference {

    /**
     * Used to classify identifiers found in formulas as cell references or not.
     */
    public enum NameType {
        CELL,
        NAMED_RANGE,
        COLUMN,
        ROW
    }
    /** The character ($) that signifies a row or column value is absolute instead of relative */
    private static final char ABSOLUTE_REFERENCE_MARKER = '$';
    /** The character (!) that separates sheet names from cell references */
    private static final char SHEET_NAME_DELIMITER = '!';
    /** The character (') used to quote sheet names when they contain special characters */
    private static final char SPECIAL_NAME_DELIMITER = '\'';

    /**
     * Matches a run of one or more letters followed by a run of one or more digits.
     * Both the letter and number groups are optional.
     * The run of letters is group 1 and the run of digits is group 2.
     * Each group may optionally be prefixed with a single '$'.
     */
    private static final Pattern CELL_REF_PATTERN = Pattern.compile("(\\$?[A-Z]+)?" + "(\\$?[0-9]+)?", Pattern.CASE_INSENSITIVE);
    /**
     * Matches references only where row and column are included.
     * Matches a run of one or more letters followed by a run of one or more digits.
     * If a reference does not match this pattern, it might match COLUMN_REF_PATTERN or ROW_REF_PATTERN
     * References may optionally include a single '$' before each group, but these are excluded from the Matcher.group(int).
     */
    private static final Pattern STRICTLY_CELL_REF_PATTERN = Pattern.compile("\\$?([A-Z]+)" + "\\$?([0-9]+)", Pattern.CASE_INSENSITIVE);
    /**
     * Matches a run of one or more letters.  The run of letters is group 1.
     * References may optionally include a single '$' before the group, but these are excluded from the Matcher.group(int).
     */
    private static final Pattern COLUMN_REF_PATTERN = Pattern.compile("\\$?([A-Z]+)", Pattern.CASE_INSENSITIVE);
    /**
     * Matches a run of one or more letters.  The run of numbers is group 1.
     * References may optionally include a single '$' before the group, but these are excluded from the Matcher.group(int).
     */
    private static final Pattern ROW_REF_PATTERN = Pattern.compile("\\$?([0-9]+)");
    /**
     * Named range names must start with a letter or underscore.  Subsequent characters may include
     * digits or dot.  (They can even end in dot).
     */
    private static final Pattern NAMED_RANGE_NAME_PATTERN = Pattern.compile("[_A-Z][_.A-Z0-9]*", Pattern.CASE_INSENSITIVE);

    // FIXME: _sheetName may be null, depending on the entry point.
    // Perhaps it would be better to declare _sheetName is never null, using an empty string to represent a 2D reference.
    private final String sheetName;
    private final Integer rowIndex;
    private final Integer colIndex;
    private final boolean isRowAbs;
    private final boolean isColAbs;

    /**
     * Create an cell ref from a string representation.  Sheet names containing special characters should be
     * delimited and escaped as per normal syntax rules for formulas.
     * @throws IllegalArgumentException if cellRef is not valid
     */
    public CellReference(String cellRef) {
        if(cellRef.toUpperCase().endsWith("#REF!")) {
            throw new IllegalArgumentException("Cell reference invalid: " + cellRef);
        }

        CellRefParts parts = separateRefParts(cellRef);
        sheetName = parts.sheetName;

        String colRef = parts.colRef;
        isColAbs = (colRef.length() > 0) && colRef.charAt(0) == '$';
        if (isColAbs) {
            colRef = colRef.substring(1);
        }
        if (colRef.length() == 0) {
            colIndex = -1;
        } else {
            colIndex = SheetUtil.letterToNumber(colRef);
        }

        String rowRef=parts.rowRef;
        isRowAbs = (rowRef.length() > 0) && rowRef.charAt(0) == '$';
        if (isRowAbs) {
            rowRef = rowRef.substring(1);
        }
        if (rowRef.length() == 0) {
            rowIndex = -1;
        } else {
            // throws NumberFormatException if rowRef is not convertible to an int
            rowIndex = Integer.parseInt(rowRef)-1; // -1 to convert 1-based to zero-based
        }
    }

    public CellReference(Integer pRow, Integer pCol) {
        this(pRow, pCol, false, false);
    }

    public CellReference(Integer pRow, Integer pCol, boolean pAbsRow, boolean pAbsCol) {
        this(null, pRow, pCol, pAbsRow, pAbsCol);
    }
    public CellReference(String pSheetName, Integer pRow, Integer pCol, boolean pAbsRow, boolean pAbsCol) {
        //Asserts.isTrue(pRow >=0, "row index may not be negative, but had " + pRow);
        //Asserts.isTrue(pCol >=0, "column index may not be negative, but had " + pCol);
        this.sheetName = pSheetName;
        this.rowIndex =pRow;
        this.colIndex =pCol;
        this.isRowAbs = pAbsRow;
        this.isColAbs =pAbsCol;
    }

    public Integer getRow(){return rowIndex;}
    public Integer getCol(){return colIndex;}
    public boolean isRowAbsolute(){return isRowAbs;}
    public boolean isColAbsolute(){return isColAbs;}
    /**
     * @return possibly {@code null} if this is a 2D reference.  Special characters are not
     * escaped or delimited
     */
    public String getSheetName(){
        return sheetName;
    }

    public static boolean isPartAbsolute(String part) {
        return part.charAt(0) == ABSOLUTE_REFERENCE_MARKER;
    }


    /**
     * Separates the sheet name, row, and columns from a cell reference string.
     *
     * @param reference is a string that identifies a cell within the sheet or workbook
     * reference may not refer to a cell in an external workbook
     * reference may be absolute or relative.
     * @return String array of sheetName, column (in ALPHA-26 format), and row
     * output column or row elements will contain absolute reference markers if they
     * existed in the input reference.
     */
    private static CellRefParts separateRefParts(String reference) {
        int plingPos = reference.lastIndexOf(SHEET_NAME_DELIMITER);
        final String sheetName = parseSheetName(reference, plingPos);
        String cell = reference.substring(plingPos+1).toUpperCase();
        Matcher matcher = CELL_REF_PATTERN.matcher(cell);
        if (!matcher.matches()) {
            throw new IllegalArgumentException("Invalid CellReference: " + reference);
        }
        String col = matcher.group(1);
        String row = matcher.group(2);

        return new CellRefParts(sheetName, row, col);
    }

    private static String parseSheetName(String reference, int indexOfSheetNameDelimiter) {
        if(indexOfSheetNameDelimiter < 0) {
            return null;
        }

        boolean isQuoted = reference.charAt(0) == SPECIAL_NAME_DELIMITER;
        if(!isQuoted) {
            // sheet names with spaces must be quoted
            if (! reference.contains(" ")) {
                return reference.substring(0, indexOfSheetNameDelimiter);
            } else {
                throw new IllegalArgumentException("Sheet names containing spaces must be quoted: (" + reference + ")");
            }
        }
        int lastQuotePos = indexOfSheetNameDelimiter-1;
        if(reference.charAt(lastQuotePos) != SPECIAL_NAME_DELIMITER) {
            throw new IllegalArgumentException("Mismatched quotes: (" + reference + ")");
        }

        // TODO - refactor cell reference parsing logic to one place.
        // Current known incarnations:
        //   FormulaParser.GetName()
        //   CellReference.parseSheetName() (here)
        //   AreaReference.separateAreaRefs()
        //   SheetNameFormatter.format() (inverse)

        StringBuilder sb = new StringBuilder(indexOfSheetNameDelimiter);

        for(int i=1; i<lastQuotePos; i++) { // Note boundaries - skip outer quotes
            char ch = reference.charAt(i);
            if(ch != SPECIAL_NAME_DELIMITER) {
                sb.append(ch);
                continue;
            }
            if(i+1 < lastQuotePos && reference.charAt(i+1) == SPECIAL_NAME_DELIMITER) {
                // two consecutive quotes is the escape sequence for a single one
                i++; // skip this and keep parsing the special name
                sb.append(ch);
                continue;
            }
            throw new IllegalArgumentException("Bad sheet name quote escaping: (" + reference + ")");
        }
        return sb.toString();
    }

    /**
     * Takes in a 0-based base-10 column and returns a ALPHA-26
     *  representation.
     * eg {@code convertNumToColString(3)} returns {@code "D"}
     */
    public static String convertNumToColString(int col) {
        // Excel counts column A as the 1st column, we
        //  treat it as the 0th one
        int excelColNum = col + 1;

        StringBuilder colRef = new StringBuilder(2);
        int colRemain = excelColNum;

        while(colRemain > 0) {
            int thisPart = colRemain % 26;
            if(thisPart == 0) { thisPart = 26; }
            colRemain = (colRemain - thisPart) / 26;

            // The letter A is at 65
            char colChar = (char)(thisPart+64);
            colRef.insert(0, colChar);
        }

        return colRef.toString();
    }

    /**
     * Returns a text representation of this cell reference.
     * <p>
     *  Example return values:
     *  <table>
     *    <caption>Example return values</caption>
     *    <tr><th>Result</th><th>Comment</th></tr>
     *    <tr><td>A1</td><td>Cell reference without sheet</td></tr>
     *    <tr><td>Sheet1!A1</td><td>Standard sheet name</td></tr>
     *    <tr><td>'O''Brien''s Sales'!A1'&nbsp;</td><td>Sheet name with special characters</td></tr>
     *  </table>
     * @return the text representation of this cell reference as it would appear in a formula.
     * @see #formatAsString(boolean)
     */
    public String formatAsString() {
        return formatAsString(true);
    }

    /**
     * Returns a text representation of this cell reference in R1C1 format.
     * <p>
     *  Example return values:
     *  <table>
     *    <caption>Example return values</caption>
     *    <tr><th>Result</th><th>Comment</th></tr>
     *    <tr><td>R1C1</td><td>Cell reference without sheet</td></tr>
     *    <tr><td>Sheet1!R1C1</td><td>Standard sheet name</td></tr>
     *    <tr><td>'O''Brien''s Sales'!R1C1'&nbsp;</td><td>Sheet name with special characters</td></tr>
     *  </table>
     * @return the text representation of this cell reference as it would appear in a formula.
     * @see #formatAsString()
     * @see #formatAsR1C1String(boolean)
     */
    public String formatAsR1C1String() {
        return formatAsR1C1String(true);
    }

    /**
     * Returns a text representation of this cell reference and allows to control
     * if the sheetname is included in the reference.
     *
     * <p>
     *  Example return values:
     *  <table>
     *    <caption>Example return values</caption>
     *    <tr><th>Result</th><th>Comment</th></tr>
     *    <tr><td>A1</td><td>Cell reference without sheet</td></tr>
     *    <tr><td>Sheet1!A1</td><td>Standard sheet name</td></tr>
     *    <tr><td>'O''Brien''s Sales'!A1'&nbsp;</td><td>Sheet name with special characters</td></tr>
     *  </table>
     * @param   includeSheetName If true and there is a sheet name set for this cell reference,
     *                           the reference is prefixed with the sheet name and '!'
     * @return the text representation of this cell reference as it would appear in a formula.
     * @see #formatAsString()
     */
    public String formatAsString(boolean includeSheetName) {
        StringBuilder sb = new StringBuilder(32);
        if(includeSheetName && sheetName != null) {
            SheetUtil.appendFormatSheetName(sb, sheetName);
            sb.append(SHEET_NAME_DELIMITER);
        }
        appendCellReference(sb);
        return sb.toString();
    }

    /**
     * Returns a text representation of this cell reference in R1C1 format and allows to control
     * if the sheetname is included in the reference.
     *
     * <p>
     *  Example return values:
     *  <table>
     *    <caption>Example return values</caption>
     *    <tr><th>Result</th><th>Comment</th></tr>
     *    <tr><td>R1C1</td><td>Cell reference without sheet</td></tr>
     *    <tr><td>Sheet1!R1C1</td><td>Standard sheet name</td></tr>
     *    <tr><td>'O''Brien''s Sales'!R1C1'&nbsp;</td><td>Sheet name with special characters</td></tr>
     *  </table>
     * @param   includeSheetName If true and there is a sheet name set for this cell reference,
     *                           the reference is prefixed with the sheet name and '!'
     * @return the text representation of this cell reference as it would appear in a formula.
     * @see #formatAsString(boolean)
     * @see #formatAsR1C1String()
     */
    public String formatAsR1C1String(boolean includeSheetName) {
        StringBuilder sb = new StringBuilder(32);
        if(includeSheetName && sheetName != null) {
            SheetUtil.appendFormatSheetName(sb, sheetName);
            sb.append(SHEET_NAME_DELIMITER);
        }
        appendR1C1CellReference(sb);
        return sb.toString();
    }

    @Override
    public String toString() {
        return getClass().getName() + " [" + formatAsString() + "]";
    }

    /**
     * Returns the three parts of the cell reference, the
     *  Sheet name (or null if none supplied), the 1 based
     *  row number, and the A based column letter.
     * This will not include any markers for absolute
     *  references, so use {@link #formatAsString()}
     *  to properly turn references into strings.
     *  @return String array of { sheetName, rowString, colString }
     */
    public String[] getCellRefParts() {
        return new String[] {
                sheetName,
                Integer.toString(rowIndex +1),
                convertNumToColString(colIndex)
        };
    }

    /**
     * Appends cell reference with '$' markers for absolute values as required.
     * Sheet name is not included.
     */
    protected void appendCellReference(StringBuilder sb) {
        if (colIndex != null && colIndex != -1) {
            if(isColAbs) {
                sb.append(ABSOLUTE_REFERENCE_MARKER);
            }

            sb.append( convertNumToColString(colIndex));
        }
        if (rowIndex != null && rowIndex != -1) {
            if(isRowAbs) {
                sb.append(ABSOLUTE_REFERENCE_MARKER);
            }
            sb.append(rowIndex +1);
        }
    }

    /**
     * Appends R1C1 cell reference with '$' markers for absolute values as required.
     * Sheet name is not included.
     */
   protected void appendR1C1CellReference(StringBuilder sb) {
        if (rowIndex != null && rowIndex != -1) {
            sb.append('R').append(rowIndex +1);
        }
        if (colIndex != null && colIndex != -1) {
            sb.append('C').append(colIndex +1);
        }
    }

    /**
     * Checks whether this cell reference is equal to another object.
     * <p>
     *  Two cells references are assumed to be equal if their string representations
     *  ({@link #formatAsString()}  are equal.
     * </p>
     */
    @Override
    public boolean equals(Object o){
        if (this == o) {
            return true;
        }
        if(!(o instanceof CellReference cr)) {
            return false;
        }
        return rowIndex == cr.rowIndex
                && colIndex == cr.colIndex
                && isRowAbs == cr.isRowAbs
                && isColAbs == cr.isColAbs
                && Objects.equals(sheetName, cr.sheetName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(rowIndex, colIndex, isRowAbs, isColAbs, sheetName);
    }


    private static final class CellRefParts {
        final String sheetName;
        final String rowRef;
        final String colRef;

        private CellRefParts(String sheetName, String rowRef, String colRef) {
            this.sheetName = sheetName;
            this.rowRef = (rowRef != null) ? rowRef : "";
            this.colRef = (colRef != null) ? colRef : "";
        }
    }
}