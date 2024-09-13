package vn.conyeu.google.sheet.builder;

public enum ErrorType {
    /** Corresponds to the #ERROR! error. **/
    ERROR,
    /** Corresponds to the #NULL! error. **/
    NULL_VALUE,
    /** Corresponds to the #DIV/0 error. **/
    DIVIDE_BY_ZERO,
    /** Corresponds to the #VALUE! error. **/
    VALUE,
    /** Corresponds to the #REF! error. **/
    REF,
    /** Corresponds to the #NAME? error. **/
    NAME,
    /** Corresponds to the #NUM! error. **/
    NUM,
    /** Corresponds to the #N/A error. **/
    N_A,
    /** Corresponds to the Loading... state. **/
    LOADING,

}