module java.commons {
    requires transitive org.apache.commons.lang3;
    requires transitive org.apache.commons.io;
    //requires transitive org.apache.commons.validator;
    requires com.fasterxml.jackson.databind;
    requires com.fasterxml.jackson.datatype.jsr310;
    requires java.sql;
    requires java.desktop;

    exports vn.conyeu.commons.beans;
    exports vn.conyeu.commons.exception;
    exports vn.conyeu.commons.function;
    exports vn.conyeu.commons.utils;

}