package vn.conyeu.javafx.controls.enums;

/**
 * Enumeration to specify how two predicates should be chained.
 * Also specify how a ChainMode enumeration should be represented in UI.
 */
public enum ChainMode {
    AND('&'),
    OR('|');

    final char charTx;

    ChainMode(char charTx) {
        this.charTx = charTx;
    }

    /**
     * Returns the charTx
     */
    public char getChar() {
        return charTx;
    }

    public boolean chain(boolean first, boolean second) {
        return AND == this ? first && second : first || second;
    }

}