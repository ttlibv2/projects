import { ChangeDetectorRef, Component, ElementRef, Input, OnInit, ViewChild, ViewEncapsulation } from '@angular/core';
import { catchError, concatMap, delay, interval, Observable, Observer, of, Subscription, switchMap, tap } from 'rxjs';
import { AgTable, TableColumn, TableOption } from 'ts-ui/ag-table';
import { SendKPI } from '../../service/send';
import { ModalService } from 'ts-ui/modal';
import { SelectFileView } from '../select-file/select-file.view';
import { Consumer, Objects } from 'ts-ui/helper';
import { ToastService } from 'ts-ui/toast';
import { IRowNode } from '@ag-grid-community/core';
import { AgStatusRenderer } from '../ag-status/ag-status';
import { Alert } from 'ts-ui/alert';
import { Row, Workbook, Worksheet } from 'exceljs';

const defaultColumns: TableColumn[] = [
  {
    field: 'checked',
    headerCheckboxSelection: true,
    headerName: '',
    checkboxSelection: true,
    width: 50
  },
  {
    field: 'send_status',
    headerName: 'Tình trạng',
    cellRenderer: 'sendStatus',
    width: 98
  }];


interface FileData {
  grid: { columns: any[], data: any[] };
  file: {
    name: string, mime: string, base64: string,
    beginRow: number, endRow: number;
    sheet: string;
  };
}

@Component({
  selector: 'ts-send-form',
  templateUrl: './send-form.view.html',
  styleUrl: './send-form.view.scss',
  encapsulation: ViewEncapsulation.None
})
export class SendFormView implements OnInit {
  logNum: any = { ok: 0, error: 0, total: 0, fileId: 'FILE_ID' };
  isSendKPI: boolean = false;
  isClickSend: boolean = false;
  isLoadData: boolean = false;
  isClickStop: boolean = false;
  execSend: Subscription = undefined;
  agOption: TableOption = {
    rowSelection: 'multiple',
    domLayout: 'autoHeight',
    onRowSelected: this.onRowClicked.bind(this),
    components: {
      'sendStatus': AgStatusRenderer,
    },

  };
  columns: TableColumn[] = defaultColumns;
  data: any[] = [];

  @ViewChild('email', { static: true })
  private emailRef: ElementRef;

  @ViewChild(AgTable, { static: true })
  private table: AgTable;

  @Input({alias: 'email'})
  email: string;


  fileData: FileData;

  get isDisableAction(): boolean {
    return this.isLoadData || this.isSendKPI;
  }

  constructor(public kpi: SendKPI,
    private def: ChangeDetectorRef,
    private toast: ToastService,
    private modal: ModalService) {
    //this.importFile();
  }

  ngOnInit(): void {
    this.emailRef.nativeElement.value = this.email || '';
  }

  onRowClicked() {
    this.logNum.total = this.table.getSelectedNodes().length;
    this.def.detectChanges();
  }

  importFile() {
    const ref = this.modal.open(SelectFileView, {
      header: 'Nhập dữ liệu từ file excel',
      closable: true
    });

    ref.onClose.subscribe({
      next: (data: FileData) => {
        if (Objects.notNull(data)) {
        
          //update column grid
          this.columns = data.grid.columns || [];
          this.columns.splice(0, 0, ...defaultColumns);


          this.fileData = data;
          this.table.deleteAll();

          let rows = data.grid.data;
          let num = 0;

          let all = of(...rows).pipe(concatMap(row => of(row).pipe(delay(100))));
          let exe = all.subscribe(item => {this.table.addRow(item); num++;});

          let checkRun = interval(300).subscribe(_ => {
            if(num == rows.length && exe ) {
              exe.unsubscribe();
              checkRun.unsubscribe();
              //this.table.tableApi.selectAll();
            }
            
          });
         

        }
      }
    })
  }

  exportFile() {
   
    const obse = new Observable((observer: Observer<any>) => {
      const wb = new Workbook();
      wb.creator = 'Nguyen Quoc Tuan';
      wb.description = 'Xlsx By Github';

      const columnDefaultIds = defaultColumns.map((col: any) => col.field);
      const allColumn = this.table.tableApi.getAllGridColumns();
      const columns = allColumn.filter(col => !columnDefaultIds.includes(col.getColId())).map(col => col.getColId());
      const ws = wb.addWorksheet('data', {views: [{state: 'frozen', ySplit: 2}] });

      // create columns
      columns.forEach((col, i) => {
        const column = ws.getColumn(i+1);
        column.header = col;
        column.key = col; 
      });

      // add row label
      ws.addRow(Objects.arrayToJson(columns, col => [col, <any>this.table.getColumnHeader(col)]));

      // add row data
      this.table.tableApi.forEachNode(node => ws.addRow(node.data));

      // hide row id
      ws.getRow(1).hidden = true;

      this.getRow(ws, 2, row => {
       // row.fill = {type: 'pattern', pattern: 'solid', fgColor: {argb: 'a9d08e'}};
        row.font = {bold: true};
        row.eachCell(cell => cell.fill = {type: 'pattern', pattern: 'solid', fgColor: {argb: 'a9d08e'}})

      });

      this.AdjustColumnWidth(ws);

      // return
      observer.next(wb);

    });

    const fileName = `file_data_${Date.now()}.xlsx`;
    obse.subscribe({
      next: wb => {
        const buffer: Promise<any> = wb.xlsx.writeBuffer({
          filename: fileName,
          useSharedStrings: true,
          useStyles: true
        });

        buffer.then(buff => Objects.download(fileName, new Blob([buff])));

      }
    })

  }

  stopSend() {
    this.isClickStop = true;
    this.toast.closeAll();

    if (Objects.notNull(this.execSend)) {
      this.execSend.unsubscribe();
      this.execSend = null;
      this.isSendKPI = false;
    }
  }


  send() {
    let email = this.emailRef.nativeElement.value;
    if (Objects.isEmpty(email)) {
      this.toast.error({ message: `Vui lòng nhập e-mail` });
      return;
    }

    if (Objects.isNull(this.fileData)) {
      this.toast.error({ message: `Bạn chưa chọn tệp gửi` });
      return;
    }

    let allNode = this.table?.getSelectedNodes() || [];
    if (allNode.length === 0) {
      this.toast.error({ message: `Vui lòng chọn dòng dữ liệu để gửi báo cáo` });
      return;
    }


    let { base64, mime } = this.fileData.file;
    let fileName = this.fileData.file.name;
    let fileDrive = this.buildFileNameDrive();
    let runNum: number = 0;


    //========================
    this.isClickStop = false;
    this.isClickSend = true;
    this.isSendKPI = true;

    this.logNum.ok = 0;
    this.logNum.error = 0;
    this.logNum.fileId = null;

    this.toast.loading(`Đang xử lý dữ liệu. Vui lòng đợi...`);

    this.kpi.createFile(email, mime, base64, fileName, fileDrive).subscribe({
      error: msg =>{ 
        console.error(msg);
        this.toast.error({title: 'Đã xảy ra lỗi tạo tệp gửi', message: msg});
        this.toast.closeAll();
      },
      next: data => {

        // Đã upload file xong
        this.logNum.fileId = data.file_id;
        console.log(`Đã upload file xong => ${JSON.stringify(data)}`);
        this.toast.success(`Đã chuẩn bị xong -> ${data.file_id}`);

        // gửi dữ liệu
        let all = allNode.map(node => of(node.data.ticket_id).pipe(
          tap(_ => this.refreshRow(node, { send_status: 'loading' })),
          switchMap(_ => this.handlerSendOneRow(email, node))
        ));

        // concatMap
        let exec = of(...all).pipe(concatMap(it => it.pipe(delay(2000))));

        // execute
        this.execSend = exec.subscribe({
          next: (it: any) => {



            this.refreshRow(it.node, { ...it.data, send_status: it.send_status });
            runNum++;

            if (it.status === 'error') {
              this.logNum['error']++;
              this.showErrorAndStop(it['data']['error']);
            }
            else {
              this.logNum['ok']++;
            }
          },
          error: e => {
            this.logNum['error']++;
            this.refreshRow(e.node, { send_status: 'error' });
            this.showErrorAndStop(e['error']);
            runNum++;
          }
        });

        // check + stop
        let abc = interval(1000).subscribe(_ => {
          if (this.isClickStop || (this.logNum.total == runNum && this.execSend)) {
            abc.unsubscribe();
            this.execSend?.unsubscribe();
            this.execSend = null;
            this.isSendKPI = false;
            this.toast.closeAll();
          }
        });
        
      }
    })



  }


  private handlerSendOneRow(email: string, node: IRowNode) {
    return this.kpi.sendRow(email, node.data).pipe(
      switchMap(data => of({ send_status: 'success', node, data })),
      catchError(error => of({ send_status: 'error', node, data: { error } }))
    );
  }

  private refreshRow(node: IRowNode | IRowNode[], data: any = {}): void {
    node = Array.isArray(node) ? node : [node];
    node.forEach(n => n.data = Object.assign(n.data, data));
    this.table.tableApi.redrawRows({ rowNodes: node });
  }

  private showErrorAndStop(error: any) {
    this.toast.error(error);
    this.stopSend();
  }

  
  private buildFileNameDrive() {
    const { name, sheet, beginRow, endRow } = this.fileData.file;
    return `${name}_${sheet}_${beginRow}_${endRow}_${Date.now()}`;
  }

  private AdjustColumnWidth(ws: Worksheet) {
    ws.columns.forEach(column => {
      const lengths = column.values.map(v => v.toString().length);
      const maxLength = Math.max(...lengths.filter(v => typeof v === 'number'));
      column.width = maxLength + 5;
    });
  }

  private getRow(ws: Worksheet, rowIndex: number, cb: Consumer<Row>) {
    cb(ws.getRow(rowIndex));
  }

}
