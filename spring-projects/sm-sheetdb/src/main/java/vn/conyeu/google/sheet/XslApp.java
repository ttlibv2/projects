package vn.conyeu.google.sheet;

import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.model.Spreadsheet;
import lombok.extern.slf4j.Slf4j;
import vn.conyeu.google.sheet.builder.ConsumerReturn;
import vn.conyeu.google.sheet.builder.XslBuilder;

import java.util.ArrayList;
import java.util.List;

@Slf4j
public class XslApp {
    private final XslService service;

    public XslApp(Sheets sheets) {
        this(new XslService(sheets));
    }

    public XslApp(XslService service) {
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
        return openById(fileId, new ArrayList<>());
    }

    public XslBook openById(String fileId, List<String> ranges ) {
        Spreadsheet ss = service.openXsl(fileId, "*", ranges);
        return new XslBook(service, ss);
    }

}