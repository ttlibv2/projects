import { AfterContentInit, AfterViewInit, booleanAttribute, ChangeDetectorRef, Component, HostListener, Inject, Input, OnInit, ViewChild, ViewEncapsulation } from '@angular/core';
import { LoggerService } from 'ts-ui/logger';
import { AgTable as Table, TableOption } from 'ts-ui/ag-table';
import { Ticket } from '../../models/ticket';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { MenuItem } from 'primeng/api';
import { ToastService } from 'ts-ui/toast';
import { AgTableService } from '../../services/ag-table.service';
import { TicketService } from '../../services/ticket.service';
import { AgStatusRenderer, AgTicketCell, AgCheckRenderer } from './ag-ticket-cell';
import { DatePipe, DOCUMENT } from '@angular/common';
import { TagRemoveEvent } from 'ts-ui/tag';
import {AgTable } from '../../models/ag-table';
import { AgTableTemplate } from './ag-table-template';
import { FormsUtil } from './form-util';
import { Alert } from '../../services/ui/alert/alert.service';
import { RxjsUtil } from './rxjs-util';
import { SaveTicketEvent } from '../ticket-form/ticket-form.component';
import {
  catchError,
  concatMap,
  delay,
  forkJoin,
  from,
  Observable,
  of,
  Subscription,
  switchMap,
  tap,
  throwError
} from 'rxjs';
import { animate, state, style, transition, trigger } from '@angular/animations';
import {Objects, Base64, TsMap, Consumer} from 'ts-ui/helper';
import { FileSelectEvent, FileUpload } from 'primeng/fileupload';
import { StorageService } from '../../services/storage.service';
import { AgTemplateCode } from '../../constant';
import { ModalService } from "../../services/ui/model.service";
import { IRowNode } from '@ag-grid-community/core';
import { ActivatedRoute, Router } from '@angular/router';
import { TemplateFile } from '../shared/template-file';

const { isNull, notNull, notBlank, isBlank } = Objects;

export interface SearchOption {
  label: string;
  code: string;
  value: boolean;
}

export interface AgTemplate {
  label: string;
}

export interface SendState {
  total: number;
  success: number;
  error: number;
  stop: boolean;
}

@Component({
  selector: 'ts-ticket-list',
  templateUrl: './ticket-list.component.html',
  styleUrl: './ticket-list.component.scss',
  encapsulation: ViewEncapsulation.None,
  // changeDetection: ChangeDetectionStrategy.OnPush,
  animations: [
    trigger('toggle', [
      state('true', style({ opacity: 1 })),
      state('void', style({ opacity: 0 })),
      transition(':enter', animate('500ms ease-in-out')),
      transition(':leave', animate('500ms ease-in-out'))
    ])
  ]
})
export class TicketListComponent implements OnInit, AfterContentInit, AfterViewInit {
  agRows: Ticket[] = [];
  searchForm: FormGroup;
  agColumns: any[] = [];
  agTemplates: MenuItem[] | any[] = [];
  agTableModel: AgTable;
  dataModel: Ticket[] = [];
  utils: FormsUtil;
  currentTicket: Ticket;
  currentTemplateCode: string; // templateCode
  tableHeight: string = '370px';
  sendState: SendState = { total: 0, success: 0, error: 0, stop: false };
  files: TsMap<string, File> = new TsMap();
  runSendTicket: Subscription;

  agOption: TableOption<Ticket> = {
    rowModelType: 'clientSide',
    components: {
      'sendStatus': AgStatusRenderer,
      'ticketCell': AgTicketCell,
      'checkCell': AgCheckRenderer
    },
    getRowId: fnc => `ROW_ID_${fnc.data.ticket_id}${fnc.data.view_chanel === true ? '_V' + fnc.data.support_help.id : ''}`,
    onRowSelected: event => this.sendState.total = this.agTable.getSelectedRows().length,
  };


  searchOptions: SearchOption[] = [
    { label: '1. Tất cả', code: 'all', value: null },
    { label: '2. Đã gửi ticket', code: 'is_send', value: true },
    { label: '3. Đã thêm ghi chú', code: 'is_note', value: true },
    { label: '4. Đã gửi đính kèm', code: 'is_attach', value: true },
    { label: '5. Đã đóng ticket', code: 'is_close', value: true },
    { label: '6. Đã gửi báo cáo', code: 'is_report', value: true },
    { label: '7. Email Ticket', code: 'email_ticket', value: true },
    { label: '8. Chưa gửi ticket', code: 'is_send', value: false },
    { label: '9. Chưa thêm ghi chú', code: 'is_note', value: false },
    { label: '10. Chưa gửi đính kèm', code: 'is_attach', value: false },
    { label: '11. Chưa đóng ticket', code: 'is_close', value: false },
    { label: '12. Chưa gửi báo cáo', code: 'is_report', value: false },
  ];

  excelItems: MenuItem[] = [
    { label: 'Nạp dữ liệu', icon: 'pi pi-download' },
    { label: 'Xuất dữ liệu', icon: 'pi pi-download', command: this.exportXsl.bind(this) },
    { label: 'Tệp đính kèm', icon: 'pi-file-excel', command: this.templateFile.bind(this) }
  ];

  toolOptions: MenuItem[] = [
    { label: 'Xóa ticket', icon: 'pi pi-trash', command: evt => this.deleteTicket() },
    { label: 'Tự động sửa', icon: 'pi pi-user-edit' },
    { label: 'Tạo / Cập nhật mẫu', icon: 'pi pi-sync', command: this.editAgTemple.bind(this) },
    { label: 'Gửi báo cáo', icon: 'pi pi-flag' },
    { label: 'Lấy DS Cột', icon: 'pi pi-cog', command: this.loadAgTable.bind(this) }
  ];

  reportItems: MenuItem[] = [
    { label: '1. Làm tất cả bước', code: 'all', icon: 'fa-regular fa-list-ol', command: this.onSendHelpdesk.bind(this) },
    { label: '2. Gửi ticket', code: 'create_ticket', icon: 'fa-regular fa-ticket', command: this.onSendHelpdesk.bind(this) },
    { label: '3. Thêm ghi chú', code: 'add_note', icon: 'fa-regular fa-note', command: this.onSendHelpdesk.bind(this) },
    { label: '4. Đính kèm hình', code: 'attach_image', icon: 'fa-regular fa-image', command: this.onSendHelpdesk.bind(this) },
    { label: '5. Gửi email', code: 'send_email', icon: 'fa-regular fa-envelope', command: this.onSendHelpdesk.bind(this) },
    { label: '6. Theo dõi và đóng ticket', code: 'close_ticket', icon: 'fa-regular fa-lock', command: this.onSendHelpdesk.bind(this) }
  ];


  @ViewChild(Table, { static: true })
  agTable: Table<Partial<Ticket>>;

  @ViewChild(FileUpload)
  fileUpload: FileUpload;

  @Input({ alias: 'visible', transform: booleanAttribute })
  set visibleForm(bool: boolean) {
    this.utils.setPathControlValue('visibleForm', bool);
  }

  @Input({ alias: 'template' })
  set agTemplate(templateCode: string) {
    this.currentTemplateCode = templateCode;
  }


  get isVisibleChanel(): boolean {
    return this.searchForm.get('visibleChanel').value;
  }

  get visibleForm(): boolean {
    return this.utils.getControlValue('visibleForm');
  }

  get visibleReportMenu(): boolean {
    return this.currentTemplateCode === 'send_ticket';
  }

  get totalFile(): number {
    return this.files?.size ?? 0;
  }

  get countSelectRow(): number {
    return this.agTable.tableApi?.getSelectedRows()?.length ?? 0;
  }

  get uploadLabel(): string {
    const size = this.files.size;
    return size == 0 ? 'Chọn tệp đính kèm' : `Bạn đang chọn ${size} tệp`;
  }

  constructor(
    @Inject(DOCUMENT)
    private document: Document,

    private router: Router,
    private activatedRoute: ActivatedRoute,

    private changeDetectorRef: ChangeDetectorRef,

    private alert: Alert,
    private toast: ToastService,
    private modal: ModalService,
    private agService: AgTableService,
    private logger: LoggerService,
    private datePipe: DatePipe,
    private storage: StorageService,
    private ticketService: TicketService,
    private fb: FormBuilder) {

    this.searchForm = this.fb.group({
      dateOn: [null, Validators.required],
      option: [this.searchOptions[0], Validators.required],
      visibleChanel: [false],
      visibleForm: [true]
    });

    this.utils = new FormsUtil(this.searchForm);
    this.utils.subscribeControl('visibleChanel', val => {
      this.chanelChecked(val);
    });

    this.utils.subscribeControl('visibleForm', val => {
      //this.tableHeight = this.calcHeight + 'px';
      this.router.navigate([], { relativeTo: this.activatedRoute, queryParamsHandling: 'merge', queryParams: { visible: val } })

    });

  }

  @HostListener('window:resize', ['$event'])
  private changeWindowResize($event: any): void {
    //this.tableHeight = this.calcHeight + 'px';
  }

  ngOnInit(): void {
    console.log(this.visibleForm)

    if (isBlank(this.currentTemplateCode)) {
      this.currentTemplateCode = this.storage.currentTemplate.get(AgTemplateCode.agTicket);
    }

    let now = new Date().getTime();
    let from = new Date(now - (10 * 24 * 3600_000));
    let end = new Date(now + 24 * 3600_000);

    this.searchForm.patchValue({
      dateOn: [from, end],
      option: this.searchOptions[0],
      visibleForm: this.visibleForm ?? true
    });

    //
    this.loadAgTable(null, () => this.searchTicket());
  }

  ngAfterContentInit(): void {
  }

  ngAfterViewInit(): void {

  }

  loadAgTable(event?: any, consumerAfter?: Consumer<any>): void {

      const loadingRef = this.toast.loading({
        title: 'Thông báo',
        message: 'Đang xử lý dữ liệu....',
        detail: 'Vui lòng chờ đến khi hoàn thành',
        closeButton: true,
      });

      this.agService.getByCode('ticket_list', true)
          .pipe(tap(tb => this.agTableModel = tb))
          .subscribe({
            error: err => {
              this.logger.error('loadAgTable', err);
              this.toast.close(loadingRef);
            },
            next: (agTable: AgTable) => {
              this.toast.close(loadingRef);

              // update command for menu item view ag-column
              this.agColumns = agTable.columns.map(c => c.asColumn());
              this.agTable.setColumns(this.agColumns);

              this.agTemplates = agTable.menuItems || [];
              this.agTemplates.forEach(item => item.command = this.onSelectColumnView.bind(this))

              // view for current template
              if (notNull(this.currentTemplateCode)) {
                const template = this.agTemplates.find(t => t.code === this.currentTemplateCode);
                if (isNull(template)) this.currentTemplateCode = null;
                else this.onSelectColumnView({ item: template });
              }

              // apply consumer
              if(notNull(consumerAfter)) {
                consumerAfter({});
              }


            }
          })

  }

  templateFile(): void {
    this.modal.open(TemplateFile, {
      header: 'Danh sách mẫu excel',
      dismissableMask: false,
      closable: true,
      data: {
        files: [
          {icon: 'pi pi-user', name: 'ticket.xlsx', label: 'Nạp dữ liệu ticket', path: '/assets/files/ticket.xlsx'},
          {icon: 'pi pi-home', name: 'ticket_loi.xlsx', label: 'Nạp mẫu báo cáo lỗi', path: '/assets/files/ticket_loi.xlsx'}
        ]
      }
      
    })
  }

  exportXsl(): void {
    const hasSelect = this.agTable.getSelectedNodes().length;
    if (!hasSelect) {
      this.toast.warning('Vui lòng chọn dữ liệu muốn xuất excel');
      return;
    }

    const fileName = (this.currentTemplateCode || 'file')+'.xlsx';
    this.agTable.exportXsl({ fileName }).subscribe({
      error: reason => console.log(reason),
      next: json => Objects.download(json.fileName, json.blob)
    });


  }

  searchTicket(): void {
    const { dateOn, option } = this.searchForm.getRawValue();

    const created_min = this.datePipe.transform(dateOn[0], 'yyyy-MM-dd') + 'T00:00:00';
    const created_max = this.datePipe.transform(dateOn[1], 'yyyy-MM-dd') + 'T23:59:59';

    const label = option.label.substring(option.label.indexOf('.') + 2);

    const loading = this.toast.loading({
      width: '400px',
      disableTimeOut: true,
      messageClass: 'p-ticket-msg',
      message: `Đang lấy dữ liệu - Vui lòng chờ <br/>
        - Tình trạng: <b>${label}</b><br/>
        - Từ ngày: <b>${created_min}</b> <br/> 
        - Đến ngày: <b>${created_max}</b>
      `
    });

    const searchObject = {
      created_min, created_max,
      [option.code]: option.value
    };

    this.ticketService.search(searchObject).subscribe({
      error: err => {
        this.logger.warn(err);
        this.toast.close(loading);
      },
      next: data => {
        this.dataModel = data.data;
        this.chanelChecked();
        this.toast.close(loading);
        this.toast.success(`Tìm được <b>[${data.data.length}]</b> dòng dữ liệu`);

      }
    });
  }

  onSelectColumnView(event: any): void {
    console.log('onSelectColumnView: ', event);

    const { code, data } = event.item;

    this.currentTemplateCode = code;
    this.storage.set_currentTemplate(AgTemplateCode.agTicket, code);


    if ('view_default' === code) {
      this.agTable.resetColumnState();
    }
    else if ('save_update_ag' === code) {
      this.modal.open(AgTableTemplate, {
        header: 'Tạo và cập nhật thông tin hiển thị',
        dismissableMask: true,
        resizable: true,
        draggable: false,
        data: {
          model: this.agTableModel
        }
      })
    }
    else if (data?.length) {
      this.agTable.hideAllColumn();
      console.log(data);
      this.agTable.tableApi.applyColumnState({
        state: data,
        applyOrder: true
      });

      // view_kpi
      if ('view_kpi' === code) {
        this.utils.setControlValue('visibleChanel', true);
        //this.chanelChecked();
      }
      else if ('send_ticket' === code) {
        this.utils.setControlValue('visibleForm', false);
      }

    }
  }

  chanelChecked(checked: boolean = this.isVisibleChanel) {
    this.agRows = !checked ? this.dataModel : this.dataModel.map(c => c.cloneWithChanel()).reduce((prev, curr) => [...prev, ...curr], []);
    this.changeDetectorRef.detectChanges();
  }


  onSendHelpdesk(event: any): void {

    const action = event.item.code;
    const allRow = this.agTable.tableApi.getSelectedNodes();
    if (allRow.length === 0) {
      this.toast.warning('Vui lòng chọn dòng dữ liệu để thực hiện gừi báo cáo.');
      //return;
    }

    if (this.isVisibleChanel === true) {
      this.toast.warning({
        title: 'Cảnh báo !!',
        message: 'Bạn đang chọn <b class="text-danger">[Hiển thị kênh]</b> nên không được phép thực hiện gửi. ',
        disableTimeOut: true
      });
      //return;
    }

    // validate attach images
    const isCheckImage: boolean = ['all', 'add_note_with_img'].includes(action);
    if (isCheckImage && this.totalFile === 0 && allRow.some(node => notBlank(node.data.images))) {
      this.toast.warning('Dòng dữ liệu đang chọn có yêu cầu hình. Vui lòng <b>[chọn tệp đính kèm]</b>');
      //return;
    }


    // chuẩn bị
    let allOf = allRow.map(node => of(node).pipe(
      tap(_ => this.refreshRow(node, { send_status: 'loading' })),
      switchMap(_ => this.sendTicketById(node, action))
    ));

    // concatMap
    let exec = of(...allOf).pipe(concatMap(it => it.pipe(delay(1000))));

    this.runSendTicket = exec.subscribe({
      error: obj => {
        console.log('error', obj)
        this.sendState.error++;
        this.refreshRow(obj.node, { send_status: 'error' });
        // this.clickStopSend(false);
      },

      next: data => {
        this.sendState.success++;
        console.log('next', data);
        this.refreshRow(data.node, { send_status: 'success' });
        if (this.sendState.stop === true) this.clickStopSend(false);
      },

      complete: () => console.log('complete')

    });

  }

  clickStopSend(fromHtml: boolean = true): void {
    if (fromHtml) this.sendState.stop = true;
    else {
      this.sendState.stop = false;
      this.runSendTicket?.unsubscribe();
      this.runSendTicket = undefined;
    }

  }







  i: number = 0;

  sendTicketById(node: IRowNode, action: string): Observable<any> {
    const { images, ticket_id } = <Ticket>node.data;


    action = action ?? 'all';

    const imageList = images.split(';');
    const files = imageList.map(n => n + '.png').filter(n => this.files.has(n)).map(n => this.files.get(n));

    // check image list size
    if (imageList.length !== files.length) {
      this.toast.error({ title: 'Thông báo !!', message: `Ticket ${ticket_id} chưa chọn đầy đủ tệp đính kèm -> ${imageList.join(';')}` })
      return throwError(() => ({ node, status: 'error' }));
    }


    const base64 = Objects.arrayToJson(files, file => [file.name, from(Base64.encode(file))]);

    return forkJoin(base64)
      .pipe(switchMap(images => this.ticketService.sendOd(action, ticket_id, { images }))//)
      //.pipe(
        ,catchError(error => throwError(() => ({ node, error, status: 'error' }))),
        switchMap(data => of({ status: 'success', node, data }))
      );

  }






  refreshRow(node: IRowNode, data: any): void {
    node.data = Objects.assign(node.data, data);
    this.agTable.tableApi.refreshCells({ rowNodes: [node], force: true });
    //this.agTable.updateRows({...node.data, ...data});
  }

  private showErrorAndStop(run: Subscription, id: any, error: any) {
    console.log('showErrorAndStop', error);
    run.unsubscribe();

    // if (error['code'] === 'SessionExpired') {
    // 	run.unsubscribe();
    //   this.toast.error({message: error['message'], title: 'Thông báo !!'});
    // }
    // else {
    // 	this.alert.danger(`[${id}] > ${error['message']}`);
    // 	if (this.isStopTicketIfError) {
    // 		run.unsubscribe();
    // 	}
    // }
  }


  deleteTicket(): void {


    const lsTicket = this.agTable.tableApi.getSelectedRows().map(t => t.ticket_id);
    if (lsTicket.length === 0) {
      this.toast.warning('Vui lòng chọn ticket để xóa !!');
      return;
    }

    if (this.isVisibleChanel === true) {
      this.toast.warning('Vui lòng bỏ chọn <b class="text-danger">[Hiển thị kênh]</b> trước khi xóa');
      return;
    }

    const dynamicRef = this.alert.warning({
      title: 'Thông báo !!',
      summary: `Bạn có chắc muốn xóa ${lsTicket.length} dòng ?`,
      okLabel: 'Đồng ý xóa',
      cancelLabel: 'Hủy không xóa',
      okClick: evt => evt.dynamicRef.close('ok'),
      cancelClick: evt => evt.dynamicRef.close('cancel')
    });

    dynamicRef.onClose.subscribe({
      next: result => {
        if (result === "ok") {
          const waitToast = this.toast.help(`Đang xóa ticket. Vui lòng đợi....`);
          const lsObs = lsTicket.map(id => this.ticketService.deleteById(id));

          RxjsUtil.runConcatMap(lsObs, 1000, true, {
            error: msg => {
              this.toast.close(waitToast);
              this.logger.error(msg);
            },
            next: data => {
              this.logger.info(data);
              this.agTable.removeRows({ ticket_id: data['model_id'] })
            },
            complete: () => {
              this.toast.close(waitToast);
              this.logger.info('complete');
            }
          });





        }
      }
    })

  }

  selectTicket(ticket: Ticket): void {
    if (this.isVisibleChanel === false) {
      this.currentTicket = ticket;
    }
  }

  demo(): void {
    // this.agTable.tableApi.selectAll();
    //this.onSendHelpdesk({ item: { code: 'all' } });
    this.loadAgTable();
  }

  removeUser($event: TagRemoveEvent) {
    console.log('remove user', $event.value);
  }

  saveTicket(event: SaveTicketEvent): void {
    if (event.state === 'new') this.agTable.addRows(event.ticket);
    else if (event.state === 'update') this.agTable.updateRows(event.ticket);
  }

  editAgTemple(event: any): void {
    const columnState = this.agTable.tableApi.getColumnState().filter(o => o.hide === false).map(o => Objects.extractValueNotNull(o));
    console.log(this.currentTemplateCode, JSON.stringify({ states: columnState }));
  }

  selectFileUpload(event: FileSelectEvent): void {
    this.fileUpload?.clear();
    this.files.setAll(event.currentFiles, f => f.name);
  }

  get calcHeight() {
    const formEl = this.document.getElementById('ticketListForm')?.getBoundingClientRect();
    const toolEl = this.document.getElementById('ticketListTool').getBoundingClientRect();
    const formHeight = this.visibleForm && formEl ? (formEl.height + formEl.top) : 0;
    const toolHeight = toolEl.height + toolEl.top;
    return this.document.body.getBoundingClientRect().height - toolHeight - formHeight;
  }
}