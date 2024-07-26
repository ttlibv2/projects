import { AfterContentInit, AfterViewInit, booleanAttribute, ChangeDetectorRef, Component, HostListener, Inject, Input, OnInit, ViewChild, ViewEncapsulation } from '@angular/core';
import { LoggerService } from 'ts-ui/logger';
import { AgTableComponent, TableOption } from 'ts-ui/ag-table';
import { Ticket } from '../../models/ticket';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { MenuItem } from 'primeng/api';
import { ToastService } from 'ts-ui/toast';
import { AgTableService } from '../../services/ag-table.service';
import { TicketService } from '../../services/ticket.service';
import { AgTagCell, AgStatusRenderer } from './ag-ticket-cell';
import { DatePipe, DOCUMENT } from '@angular/common';
import { ListUtil } from './list-utils';
import { TagRemoveEvent } from 'ts-ui/tag';
import { AgTable } from '../../models/ag-table';
import { AgTableTemplate } from './ag-table-template';
import { FormsUtil } from './form-util';
import { Alert } from '../../services/ui/alert/alert.service';
import { RxjsUtil } from './rxjs-util';
import { SaveTicketEvent, SetDataInput } from '../ticket-form/ticket-form.component';
import { tap } from 'rxjs';
import { animate, state, style, transition, trigger } from '@angular/animations';
import { Objects } from 'ts-ui/helper';
import { FileSelectEvent, FileUpload } from 'primeng/fileupload';
import { StorageService } from '../../services/storage.service';
import { AgTemplateCode } from '../../constant';
import {ModalService} from "../../services/ui/model.service";

const {isNull, notNull, notBlank, isBlank} = Objects;

export interface SearchOption {
  label: string;
  code: string;
  value: boolean;
}

export interface AgTemplate {
  label: string;
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
  files: File[] = [];

  agOption: TableOption<Ticket> = {
    rowModelType: 'clientSide',
    getRowId: fnc => `ROWID_${fnc.data.ticket_id}${fnc.data.view_chanel === true ? '_V'+fnc.data.support_help.id : ''}`
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
    { label: 'Tệp đính kèm', icon: 'pi-file-excel' }
  ];

  toolOptions: MenuItem[] = [
    { label: 'Xóa ticket', icon: 'pi pi-trash', command: evt => this.deleteTicket() },
    { label: 'Tự động sửa', icon: 'pi pi-user-edit' },
    { label: 'Tạo / Cập nhật mẫu', icon: 'pi pi-sync', command: this.editAgTemple.bind(this) },
    { label: 'Gửi báo cáo', icon: 'pi pi-flag' },
    {label: 'Lấy DS Cột', icon: 'pi pi-cog', command: this.loadAgTable.bind(this)}
  ];

  reportItems: MenuItem[] = [
    { label: '1. Làm tất cả bước', code: 'all', icon: 'fa-regular fa-list-ol', command: this.onSendHelpdesk.bind(this) },
    { label: '2. Gửi ticket', code: 'create_ticket', icon: 'fa-regular fa-ticket', command: this.onSendHelpdesk.bind(this) },
    { label: '3. Thêm ghi chú', code: 'add_note', icon: 'fa-regular fa-note', command: this.onSendHelpdesk.bind(this) },
    { label: '4. Đính kèm hình', code: 'add_note_with_img', icon: 'fa-regular fa-image', command: this.onSendHelpdesk.bind(this) },
    { label: '5. Gửi email', code: 'send_email', icon: 'fa-regular fa-envelope', command: this.onSendHelpdesk.bind(this) },
    { label: '6. Theo dõi và đóng ticket', code: 'close_ticket', icon: 'fa-regular fa-lock', command: this.onSendHelpdesk.bind(this) }
  ];


  @ViewChild(AgTableComponent, { static: true })
  agTable: AgTableComponent<Partial<Ticket>>;

  @ViewChild('fileUpload', {static: true})
  fileUpload: FileUpload;

  @Input({ transform: booleanAttribute })
  set visible(bool: boolean) {
    this.searchForm.get('visibleForm').setValue(bool);
  }

  @Input()
  set agTemplate(templateCode: string) {
    this.currentTemplateCode = templateCode;
  }

  get isVisibleChanel(): boolean {
    return this.searchForm.get('visibleChanel').value;
  }

  get visibleForm(): boolean {
    return this.searchForm.getRawValue()?.visibleForm;
  }

  get visibleReportMenu(): boolean {
    return this.currentTemplateCode === 'send_ticket';
  }

  get totalFile(): number {
    return this.files?.length;
  }
  
  get countSelectRow(): number {
    return this.agTable.tableApi?.getSelectedRows()?.length ?? 0;
  }

  constructor(
    @Inject(DOCUMENT)
    private document: Document,

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
      this.tableHeight = this.calcHeight + 'px';
    })

  }

  @HostListener('window:resize', ['$event'])
  private changeWindowResize($event: any): void {
    this.tableHeight = this.calcHeight + 'px';
  }

  ngOnInit(): void {

    if(isBlank(this.currentTemplateCode)) {
      this.currentTemplateCode = this.storage.currentTemplate[AgTemplateCode.agTicket];
    }
    
    let now = new Date().getTime();
    let from = new Date(now - (10 * 24 * 3600_000));
    let end = new Date(now + 24 * 3600_000);

    this.searchForm.patchValue({
      dateOn: [from, end],
      option: this.searchOptions[0]
    });

    //
    this.searchTicket();
    this.loadAgTable();
    

  }

  ngAfterContentInit(): void {
  }

  ngAfterViewInit(): void {
   
  }
  
  loadAgTable(event?: any) {
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
          //this.toast.close(loadingRef);
        },
        next: (agTable: AgTable) => {
          console.log(this.storage.config())

         // this.toast.close(loadingRef);

          ListUtil.updateColumn(agTable, {
            'send_status': { cellRenderer: AgStatusRenderer },
            'stage_text': { cellRenderer: AgTagCell },
            'is_note': { cellRenderer: AgStatusRenderer },
            'is_closed': { cellRenderer: AgStatusRenderer },
            'is_upfile': { cellRenderer: AgStatusRenderer }
          });

          // update command for menu item view ag-column
          this.agColumns = agTable.grid_columns;
          this.agTable.setColumns(this.agColumns);

          this.agTemplates = agTable.menuItems || [];
          this.agTemplates.forEach(item => item.command = this.onSelectColumnView.bind(this))

    
           // view for current template
           if(notNull(this.currentTemplateCode))  {
           const template = this.agTemplates.find(t => t.code === this.currentTemplateCode);
            if(isNull(template)) this.currentTemplateCode = null;
             else this.onSelectColumnView({item: template});
           }


        }
      })
  }

  exportXsl(): void {
    console.log(this.agTable.tableApi.getSheetDataForExcel())
    
  }

  searchTicket(): void {
    const { dateOn, option } = this.searchForm.getRawValue();

    const created_min = this.datePipe.transform(dateOn[0], 'yyyy-MM-dd') + 'T00:00:00';
    const created_max = this.datePipe.transform(dateOn[1], 'yyyy-MM-dd') + 'T23:59:59';

    const loading = this.toast.loading({
      disableTimeOut: true,
      messageClass: 'p-ticket-msg',
      message: `Đang lấy dữ liệu - Vui lòng chờ <br/>- Từ ngày: <b>${created_min}</b> <br/> - Đến ngày: <b>${created_max}</b>`
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

    
    if ('view_default' === code) this.agTable.resetColumnState();
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

    }
  }

  chanelChecked(checked: boolean = this.isVisibleChanel) {
    this.agRows = !checked ? this.dataModel : this.dataModel.map(c => c.cloneWithChanel()).reduce((prev, curr) => [...prev, ...curr], []);
    this.changeDetectorRef.detectChanges();
  }


  onSendHelpdesk(event: any): void {

    const allRow = this.agTable.tableApi.getSelectedRows();
    if(allRow.length === 0) {
      this.toast.warning('Vui lòng chọn dòng dữ liệu để thực hiện gừi báo cáo.');
      return;
    }

    if(this.isVisibleChanel === true)  {
      this.toast.warning({
        title: 'Cảnh báo !!',
        message: 'Bạn đang chọn <b class="text-danger">[Hiển thị kênh]</b> nên không được phép thực hiện gửi. ',
        disableTimeOut: true
      });
      return;
    }    

    // validate attach images
    if(allRow.some(ticket => notBlank(ticket.images))) {
      this.toast.warning('Dòng dữ liệu đang chọn có yêu cầu hình. Vui lòng <b>[chọn tệp đính kèm]</b>');
      return;
    }

    console.log(`Click `, event.item.code);
  }

  deleteTicket(): void {
    const lsTicket = this.agTable.tableApi.getSelectedRows().map(t => t.ticket_id);
    if (lsTicket.length === 0) {
      this.toast.warning('Vui lòng chọn ticket để xóa !!' );
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
          const waitToast = this.toast.help(`Đang xóa ticket. Vui lòng đợi....` );
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
    if(this.isVisibleChanel === false){
      this.currentTicket = ticket;
    }
  }

  demo(): void {
    this.loadAgTable()
  }

  removeUser($event: TagRemoveEvent) {
    console.log('remove user', $event.value);
  }

  saveTicket(event: SaveTicketEvent): void {
    if(event.state === 'new') this.agTable.addRows(event.ticket);
    else if(event.state === 'update') this.agTable.updateRows(event.ticket);
  }

  editAgTemple(event: any): void {
    const columnState= this.agTable.tableApi.getColumnState().filter(o => o.hide === false).map(o => Objects.extractValueNotNull(o));
    console.log(this.currentTemplateCode, JSON.stringify({states: columnState}));
  }

  selectFileUpload(event: FileSelectEvent): void {
    this.files = event.currentFiles;
    this.fileUpload.clear();
  }

  get calcHeight() {
    const formEl = this.document.getElementById('ticketListForm')?.getBoundingClientRect();
    const toolEl = this.document.getElementById('ticketListTool').getBoundingClientRect();
    const formHeight = this.visibleForm && formEl ? (formEl.height + formEl.top ) : 0;
    const toolHeight = toolEl.height + toolEl.top ;
    return this.document.body.getBoundingClientRect().height - toolHeight - formHeight;
  }
}