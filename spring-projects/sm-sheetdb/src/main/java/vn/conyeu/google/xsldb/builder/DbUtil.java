package vn.conyeu.google.xsldb.builder;

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

        builder.editRow(0, c -> c.bold(true).fontFamily(family)).protect(r -> r.description("Only Admin Edit").editorUser(editUser));
        builder.editRow(1, c -> c.fontFamily(family));
        return builder;
    }
}
