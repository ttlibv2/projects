package vn.conyeu.google.sheetdb;

import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.model.Spreadsheet;
import lombok.extern.slf4j.Slf4j;
import vn.conyeu.commons.utils.MapperHelper;
import vn.conyeu.google.sheetdb.builder.ConsumerReturn;
import vn.conyeu.google.sheetdb.builder.XslBuilder;

import java.io.IOException;

@Slf4j
public class SheetApp {
    private final Sheets sheets;

    public SheetApp(Sheets sheets) {
        this.sheets = sheets;
    }

    /**
     * Creates a new spreadsheet with the given name.
     * @param name The name for the spreadsheet.
     * */
    public Workbook create(String name) throws IOException {
       return create(name, xsl -> {
           xsl.addSheet(name);
           return xsl;
       });
    }

    /**
     * Creates a new spreadsheet with the given name and the specified number of rows and columns.
     * @param name The name for the spreadsheet.
     * @param rows The number of rows for the spreadsheet.
     * @param columns The number of columns for the spreadsheet.
     * */
    public Workbook create(String name, int rows, int columns) throws IOException {
       return create(name, xsl -> {
           xsl.addSheet(name, rows, columns);
           return xsl;
       });
    }

    /**
     * Creates a new spreadsheet with the given name.
     * @param name The name for the spreadsheet.
     * @param custom the consumer custom properties xsl
     * */
    public Workbook create(String name, ConsumerReturn<XslBuilder> custom) throws IOException {
        XslBuilder xslBuilder = custom.accept(new XslBuilder()).title(name);
        Spreadsheet ss = sheets.spreadsheets().create(xslBuilder.build()).execute();
        log.warn("{}", MapperHelper.serializeToString(ss));
        return new Workbook(sheets.spreadsheets(), ss);
    }

    public Workbook openById(String fileId) throws IOException {
        Spreadsheet ss = sheets.spreadsheets().get(fileId).execute();
        return new Workbook(sheets.spreadsheets(), ss);
    }

}