package vn.conyeu.google.xsldb;

import vn.conyeu.commons.utils.Asserts;
import vn.conyeu.google.core.GoogleException;
import vn.conyeu.google.drives.GFile;
import vn.conyeu.google.drives.GFolder;
import vn.conyeu.google.drives.GPage;
import vn.conyeu.google.sheet.builder.GridBuilder;
import vn.conyeu.google.sheet.builder.SheetBuilder;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SheetDb {
    private final DbApp dbApp;
    private final GFolder folder;
    private final Map<String, GFile> schemas;

    SheetDb(DbApp dbApp, GFolder folder) {
        this.dbApp = Asserts.notNull(dbApp);
        this.folder = Asserts.notNull(folder);
        this.schemas = new HashMap<>();
        this.validateFolder(folder);
    }

    private void validateFolder(GFolder folder) {
        String isDb = folder.getProperty("isDb");
        if(!"true".equals(isDb)) {
            String msg = "The folder %s not database";
            throw new GoogleException(msg, folder.getUrl());
        }
    }

    protected DbApp dbApp() {
        return dbApp;
    }

    public String getId() {
        return folder.getId();
    }

    public String getUrl() {
        return folder.getUrl();
    }

    public String getOwner() {
        return folder.getOwner().getEmailAddress();
    }

    /**
     * Returns the schemas
     */
    public Map<String, GFile> schemas() {
        return schemas;
    }

    /**
     * Load schema
     * */
    public SheetDb loadSchema() {
        schemas.clear();
        GPage<GFile> files = folder.search(q -> q.notFolder().and(q.properties.has("isTable"))).mapTo(GFile::cast);
        if(!files.isEmpty()) {
            schemas.clear();
            for (GFile file : files) schemas.put(file.getId(), file);
        }
        return this;
    }

    /**
     * Create table
     * @param name the name of table
     * */
    public SheetTb createTb(String name, List<Column> columns) {
        SheetBuilder sheet = new SheetBuilder(null)
                .sheetId(0).rowCount(13).frozenRowCount(11)
                .columnCount(Math.max(2, columns.size()))
                .defaultFormat(c -> c.fontFamily("Consolas").fontSize(12));

        GridBuilder grid = sheet.getGrid(0);
        for(Column col:columns) {
          grid.getRange(1, 11).setValues(
                  List.of(col.getColumnName())
          );
        }



    }
}