import { Subscription } from "rxjs";
import { Objects } from "ts-ui/helper";
import { Ticket } from "../../models/ticket";
import { TicketFormComponent, SaveTicketEvent } from "./ticket-form.component";
import { Utils } from "./utils";
const { notNull, isNull, isFalse, isTrue } = Objects;

export class ImportData {
    private readonly utils: Utils;
    private subscribeSave: Subscription;
    private clickButton: 'next' | 'prev' | null = null;

    currentPos: number = -1;
    currentRow: Ticket = null;

    isRunning: boolean = false;
    isClickRunAll: boolean | null = null;

    get size(): number { return this.allData.length; }

    get current(): number { return this.currentPos + 1; }

    get isEmpty(): boolean { return this.size === 0; }

    get totalSave(): number {
        return this.allData.filter(t => notNull(t.ticket_id)).length;
    }

    get disableClickPrev(): boolean {
        return isTrue(this.isClickRunAll) || (this.isEmpty ? true : this.currentPos === 0);
    }

    get disableClickNext(): boolean {
        return isTrue(this.isClickRunAll) || (this.isEmpty ? true : this.currentPos === this.size - 1);
    }

    get isVisibleRunAll(): boolean {
        const { isRunning, totalSave } = this;
        return isFalse(isRunning) && totalSave < this.size;
    }

    get isVisibleStopRunAll(): boolean {
        const { isRunning, isClickRunAll } = this;
        return isRunning && isClickRunAll;
    }

    get currentId(): number {
        return this.currentRow?.ticket_id;
    }

    set isVisibleToolImport(b: boolean) {
        this.comp.state.visibleToolImport = b;
    }

    constructor(
        private comp: TicketFormComponent,
        private allData: Ticket[] = []) {
        this.utils = comp.utils;
    }

    getAt(pos: number): Ticket {
        return this.allData[pos];
    }

    getPos(row: Ticket): number {
        return this.allData.indexOf(row) || -1;
    }

    clickNext(): void {
        this.currentPos++;
        this.clickButton = 'next';
        this.applyRowData();
    }

    clickPrev(): void {
        this.currentPos--;
        this.clickButton = 'prev';
        this.applyRowData();
    }

    /** click run_all */
    beginRunAll(): void {
        this.isClickRunAll = true;
        this.isRunning = true;

        // try save first_row
        this.comp.saveTicket();
    }

    /** click stop_run */
    stopRunAll(): void {
        this.isRunning = false;
        this.isClickRunAll = false;
    }

    /** click abort_data */
    abortData(): void {
        const ref = this.comp.alert.warning({
            title: 'Cảnh báo !!',
            summary: 'Bạn có chắc muốn hủy danh sách dữ liệu không ?',
            actions: [
                { label: 'Hủy Nạp', onClick: e => e.dynamicRef.close('ok') },
                { label: 'Không hủy', onClick: e => e.dynamicRef.close('cancel') }
            ]
        });


        ref.onClose.subscribe(act => {
            if ('ok' === act) {
                this.onDestroy();
            }
        })

    }

    chooseFile(): void {
        this.onDestroy();
        this.comp.idata = null;
        this.comp.openDialogImportXsl();
    }

    onDestroy(): void {
        this.allData = [];
        this.currentPos = -1;
        this.isRunning = false;
        this.isVisibleToolImport = false;
        this.isClickRunAll = null;
        //this.isClickRunOne = null;
        this.currentRow = null;

        // destroy save subscribe
        if (notNull(this.subscribeSave)) {
            this.subscribeSave.unsubscribe();
        }
    }

    private applyRowData(pos?: number, item?: Ticket): void {
        let posNew = pos || this.currentPos;
        let itemNew = item || this.allData[posNew];

        this.currentRow = itemNew;
        this.utils.reset(itemNew);

        // auto searchUser
        if (isNull(itemNew.od_partner_id) && this.isClickRunAll) {
            this.comp.searchUser({
                autoSelect: true, timeDelay: 1000,
                autoSave: this.isClickRunAll,
                trialMST: false, showToatResult: false,
                newEmail: true
            }, 
            {operator: 'equal'});
        }

        // destroy save subscribe
        this.subscribeSave?.unsubscribe();

        // register save subscribe
        this.subscribeSave = this.comp.onSave.subscribe({
            next: (evt: SaveTicketEvent) => {
                itemNew.update(evt.ticket);

                // event isClickRunAll
                if (this.isClickRunAll) {
                    if (this.currentPos === this.size - 1) this.stopRunAll();
                    else this.clickNext()
                }

            }
        });

    }


}
