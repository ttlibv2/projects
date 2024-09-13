package vn.conyeu.google.xsldb;

import vn.conyeu.commons.utils.DateHelper;
import vn.conyeu.google.sheet.builder.SheetBuilder;

public final class DbUtil {

    public static SheetBuilder buildSheet(String name, String editUser) {
        return buildSheet(name, "Consolas", editUser);
    }

    public static SheetBuilder buildSheet(String name, String family, String editUser) {
        int sheetId = (int) DateHelper.epochMilli();
        SheetBuilder builder = new SheetBuilder(null);
        builder.title(name).rowCount(2).columnCount(2);
        builder.frozenRowCount(1).sheetId(sheetId);
        return builder;
    }
}