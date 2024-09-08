package vn.conyeu.google.db;

import com.google.api.services.sheets.v4.model.Sheet;
import vn.conyeu.google.sheet.builder.ConsumerReturn;
import vn.conyeu.google.sheet.builder.RowDataBuilder;
import vn.conyeu.google.sheet.builder.SheetBuilder;
import vn.conyeu.google.sheet.builder.XmlBuilder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TableBuilder implements XmlBuilder<Sheet> {
    private final SheetBuilder sheet = new SheetBuilder(null);
    private final Map<String, ColumnBuilder> columns = new HashMap<>();

    private String fontFamily = "Consolas";


    @Override
    public Sheet build() {
        return sheet.build();
    }

}