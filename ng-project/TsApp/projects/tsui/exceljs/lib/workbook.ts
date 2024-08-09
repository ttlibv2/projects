import {from, Observable, of} from "rxjs";
import {Workbook as EWB, WorkbookProperties, CalculationProperties, WorkbookView, Worksheet} from "exceljs";
import {Sheet} from "./sheet";
import {BiConsumer} from "ts-ui/helper";

export class Workbook {

    static openByBuffer(buffer: ArrayBuffer): Observable<Workbook> {
        const wb = new EWB().xlsx.load(buffer);
        return from(wb.then(iwb => new Workbook(iwb)));
    }

    static createNew(): Observable<Workbook> {
        return of(new Workbook(new EWB()));
    }

    private constructor(private readonly wb: EWB) {
    }

    /** get the category */
    get category(): string {
        return this.wb.category;
    }

    /** Set the category */
    set category(value: string) {
        this.wb.category = value;
    }

    /** get the company */
    get company(): string {
        return this.wb.company;
    }

    /** Set the company */
    set company(value: string) {
        this.wb.company = value;
    }

    /** get the creator */
    get creator(): string {
        return this.wb.creator;
    }

    /** Set the creator */
    set creator(value: string) {
        this.wb.creator = value;
    }

    /** get the description */
    get description(): string {
        return this.wb.description;
    }

    /** Set the description */
    set description(value: string) {
        this.wb.description = value;
    }

    /** get the keywords */
    get keywords(): string {
        return this.wb.keywords;
    }

    /** Set the keywords */
    set keywords(value: string) {
        this.wb.keywords = value;
    }

    /** get the lastModifiedBy */
    get lastModifiedBy(): string {
        return this.wb.lastModifiedBy;
    }

    /** Set the lastModifiedBy */
    set lastModifiedBy(value: string) {
        this.wb.lastModifiedBy = value;
    }

    /** get the created */
    get created(): Date {
        return this.wb.created;
    }

    /** Set the created */
    set created(value: Date) {
        this.wb.created = value;
    }

    /** get the manager */
    get manager(): string {
        return this.wb.manager;
    }

    /** Set the manager */
    set manager(value: string) {
        this.wb.manager = value;
    }

    /** get the modified */
    get modified(): Date {
        return this.wb.modified;
    }

    /** Set the modified */
    set modified(value: Date) {
        this.wb.modified = value;
    }

    /** get the lastPrinted */
    get lastPrinted(): Date {
        return this.wb.lastPrinted;
    }

    /** Set the lastPrinted */
    set lastPrinted(value: Date) {
        this.wb.lastPrinted = value;
    }

    /** get the properties */
    get properties(): WorkbookProperties {
        return this.wb.properties;
    }

    /** Set the properties */
    set properties(value: WorkbookProperties) {
        this.wb.properties = value;
    }

    /** get the subject */
    get subject(): string {
        return this.wb.subject;
    }

    /** Set the subject */
    set subject(value: string) {
        this.wb.subject = value;
    }

    /** get the title */
    get title(): string {
        return this.wb.title;
    }

    /** Set the title */
    set title(value: string) {
        this.wb.title = value;
    }

    /** get the calcProperties */
    get calcProperties(): CalculationProperties {
        return this.wb.calcProperties;
    }

    /** Set the calcProperties */
    set calcProperties(value: CalculationProperties) {
        this.wb.calcProperties = value;
    }

    /** get the views */
    get views(): WorkbookView[] {
        return this.wb.views;
    }

    /** Set the views */
    set views(value: WorkbookView[]) {
        this.wb.views = value;
    }

    /** get the worksheets */
    getSheets(): Sheet[] {
        return this.wb.worksheets.map(ws => this.newSheet(ws));
    }

    getSheetByName(sheetName: string): Sheet {
        const ws = this.wb.getWorksheet(sheetName);
        return this.newSheet(ws);
    }

    getSheetByIndex(sheetIndex: number): Sheet {
        const ws = this.wb.getWorksheet(sheetIndex);
        return this.newSheet(ws);
    }

    removeSheetByName(sheetName: string): void {
        this.wb.removeWorksheet(sheetName);
    }

    removeSheetByIndex(sheetIndex: number): void {
        this.wb.removeWorksheet(sheetIndex);
    }

    addSheet(sheetName: string): Sheet {
        const ws = this.wb.addWorksheet(sheetName);
        return this.newSheet(ws);
    }

    getSheetNames(): string[] {
        return this.wb.worksheets.map(s => s.name);
    }

    eachSheet(callback: BiConsumer<Sheet, number>): void {
        this.getSheets().forEach((sheet, index) => callback(sheet, index));
    }

    private newSheet(ws: Worksheet) {
        return Sheet['from'](this, ws);
    }

}