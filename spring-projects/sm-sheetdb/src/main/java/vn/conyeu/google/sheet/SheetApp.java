package vn.conyeu.google.sheet;

import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.model.Spreadsheet;
import lombok.extern.slf4j.Slf4j;
import vn.conyeu.google.sheet.builder.ConsumerReturn;
import vn.conyeu.google.sheet.builder.XslBuilder;

@Slf4j
public class SheetApp {
    private final XslSheetService service;

    public SheetApp(Sheets sheets) {
        this(new XslSheetService(sheets));
    }

    public SheetApp(XslSheetService service) {
        this.service = service;
    }

    /**
     * Creates a new spreadsheet with the given name.
     * @param name The name for the spreadsheet.
     * */
    public XslBook create(String name) {
       return create(name, xsl -> xsl.addSheet(name));
    }

    /**
     * Creates a new spreadsheet with the given name and the specified number of rows and columns.
     * @param name The name for the spreadsheet.
     * @param rows The number of rows for the spreadsheet.
     * @param columns The number of columns for the spreadsheet.
     * */
    public XslBook create(String name, int rows, int columns) {
       return create(name, xsl -> xsl.addSheet(name, rows, columns));
    }

    /**
     * Creates a new spreadsheet with the given name.
     * @param name The name for the spreadsheet.
     * @param custom the consumer custom properties xsl
     * */
    public XslBook create(String name, ConsumerReturn<XslBuilder> custom) {
        Spreadsheet ss = service.createXsl(name, custom);
        return new XslBook(service, ss);
    }

    public XslBook openById(String fileId) {
        Spreadsheet ss = service.openXsl(fileId, "*");
        return new XslBook(service, ss);
    }

}