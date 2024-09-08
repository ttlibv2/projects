package vn.conyeu.google.xsldb;

import vn.conyeu.google.drives.DriveApp;
import vn.conyeu.google.sheet.XslApp;
import vn.conyeu.google.sheet.XslSheet;

public class SheetTb<E> extends AbstractTb<E> {

    SheetTb(DriveApp drives, XslApp sheets, SheetDb sheetDb, XslSheet sheet) {
        super(drives, sheets, sheetDb, sheet);
    }


}
