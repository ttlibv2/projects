import { AfterContentInit, AfterRenderRef, booleanAttribute, Component, Input, OnInit, ViewChild, ViewEncapsulation } from '@angular/core';
import { LoggerService } from 'ts-logger';
import { AgTableComponent, TableColumn, TableOption } from 'ts-ui/ag-table';
import { Ticket } from '../../models/ticket';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { MenuItem } from 'primeng/api';
import { ToastService } from '../../services/toast.service';
import { AgTableService } from '../../services/ag-table.service';
import { TicketService } from '../../services/ticket.service';
import { AgTagCell, AgStatusRenderer } from './ag-ticket-cell';
import { DatePipe } from '@angular/common';
import { ListUtil } from './list-utils';
import { TagRemoveEvent } from 'ts-ui/tag';
import { AgTable } from '../../models/ag-table';
import { AgTableTemplate } from './ag-table-template';
import { concat, concatAll, concatMap, delay, of, switchMap, take, tap } from 'rxjs';
import { Objects } from 'ts-helper';
import { channel } from 'diagnostics_channel';
import { FormsUtil } from './form-util';
import { Alert } from '../../services/ui/alert/alert.service';
import { RxjsUtil } from './rxjs-util';

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
  encapsulation: ViewEncapsulation.None
})
export class TicketListComponent implements OnInit, AfterContentInit {
  agRows: Ticket[] = [];
  searchForm: FormGroup;
  agColumns: any[] = [];
  agTemplates: MenuItem[] = [];
  agTableModel: AgTable;
  dataModel: Ticket[] = [];
  utils: FormsUtil;
  currentTicket: Ticket;

  agOption: TableOption<Ticket> = {
    getRowId: fnc => `ROWID_${fnc.data.ticket_id}`
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
    { label: 'Xuất dữ liệu', icon: 'pi pi-download' },
    { label: 'Tệp đính kèm', icon: 'pi-file-excel' }
  ];

  toolOptions: MenuItem[] = [
    { label: 'Xóa ticket', icon: 'pi pi-trash', command: evt => this.deleteTicket() },
    { label: 'Tự động sửa', icon: 'pi pi-user-edit' },
    { label: 'Tạo / Cập nhật mẫu', icon: 'pi pi-sync' },
    { label: 'Gửi báo cáo', icon: 'pi pi-flag' }
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

  @Input({ transform: booleanAttribute })
  visibleForm: boolean = true;

  get isVisibleChanel(): boolean {
    return this.searchForm.get('visibleChanel').value;
  }

  constructor(
    private alert: Alert,
    private toast: ToastService,
    private agService: AgTableService,
    private logger: LoggerService,
    private datePipe: DatePipe,
    private ticketService: TicketService,
    private fb: FormBuilder) {

    this.searchForm = this.fb.group({
      dateOn: [null, Validators.required],
      option: [this.searchOptions[0], Validators.required],
      visibleChanel: [false]
    });

    this.utils = new FormsUtil(this.searchForm);
    this.utils.subscribeControl('visibleChanel', val => {
      this.chanelChecked(val);
    });

  }

  ngOnInit(): void {
    let now = new Date().getTime();
    let from = new Date(now - (10 * 24 * 3600_000));
    let end = new Date(now + 24 * 3600_000);

    console.log(now, from)
    this.searchForm.patchValue({
      dateOn: [from, end],
      option: this.searchOptions[0]
    });

    //
    this.loadAgTable();
    this.searchTicket();

  }

  ngAfterContentInit(): void {
  }

  loadAgTable() {
    this.agService.getByCode('ticket_list', true)
      .pipe(tap(tb => this.agTableModel = tb))
      .subscribe({
        error: err => console.warn(err),
        next: (agTable: AgTable) => {
          //console.log('agTable: ', agTable)

          ListUtil.updateColumn(agTable, {
            'send_status': { cellRenderer: AgStatusRenderer },
            'stage_text': { cellRenderer: AgTagCell },
            'is_note': { cellRenderer: AgStatusRenderer },
            'is_closed': { cellRenderer: AgStatusRenderer },
            'is_upfile': { cellRenderer: AgStatusRenderer }
          });

          // update command for menu item view ag-column
          this.agColumns = agTable.grid_columns;
          this.agTemplates = agTable.menuItems || [];
          this.agTemplates.forEach(item => item.command = this.onSelectColumnView.bind(this))


        }
      })
  }

  exportXsl(): void {
    this.toast.info({ summary: 'Xuất dữ liệu' });
  }

  searchTicket(): void {
    const { dateOn, option } = this.searchForm.getRawValue();

    const created_min = this.datePipe.transform(dateOn[0], 'yyyy-MM-dd') + 'T00:00:00';
    const created_max = this.datePipe.transform(dateOn[1], 'yyyy-MM-dd') + 'T23:59:59';

    const loading = this.toast.loading({
      disableTimeOut: true,
      messageClass: 'p-ticket-msg',
      summary: `Đang lấy dữ liệu - Vui lòng chờ <br/>- Từ ngày: <b>${created_min}</b> <br/> - Đến ngày: <b>${created_max}</b>`
    });

    const searchObject = {
      created_min, created_max,
      [option.code]: option.value
    };

    this.ticketService.search(searchObject).subscribe({
      error: err => {
        this.logger.warn(err);
        this.toast.closeToast(loading);
      },
      next: data => {
        this.dataModel = data.data;
        this.chanelChecked();
        this.toast.closeToast(loading);
        this.toast.success({ summary: `Tìm được <b>[${data.data.length}]</b> dòng dữ liệu` });

      }
    });
  }

  onSelectColumnView(event: any): void {
    const { code, data } = event.item;
    if ('view_default' === code) this.agTable.resetColumnState();
    else if ('save_update_ag' === code) {
      this.toast.openDialog(AgTableTemplate, {
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
    this.agRows = !checked ? this.dataModel : this.dataModel.map(c => c.cloneWithChanel()).reduce((prev, curr) => [...prev, ...curr], [])
  }


  onSendHelpdesk(event: any): void {
    console.log(`Click `, event.item.code);
  }

  deleteTicket(): void {
    const lsTicket = this.agTable.tableApi.getSelectedRows().map(t => t.ticket_id);
    if (lsTicket.length === 0) {
      this.toast.warning({ summary: 'Vui lòng chọn ticket để xóa !!' });
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
          const waitToast = this.toast.help({ summary: `Đang xóa ticket. Vui lòng đợi....` });
          const lsObs = lsTicket.map(id => this.ticketService.deleteById(id));
          
          RxjsUtil.runConcatMap(lsObs, 1000, true, {
            error: msg => {
              this.toast.closeToast(waitToast);
              this.logger.error(msg);
            },
            next: data => {
              this.logger.info(data);
              this.agTable.removeRows({ ticket_id: data['model_id'] })
            },
            complete: () => {
              this.toast.closeToast(waitToast);
              this.logger.info('complete');
            }
          });





        }
      }
    })








    // const waitToast = this.toast.help({ summary: `Đang xóa ticket. Vui lòng đợi....` });

    // const listObs = lsTicket.map(ticket => of(ticket).pipe(
    //   tap(t => this.agTable.updateRows({send_status: 'loading'}))
    // ));





    // of(...listObs).pipe(concatMap(it => it.pipe(delay(1000)))).subscribe({
    //   error: msg => this.logger.error(msg),
    //   next: data =>  this.logger.log(data),
    //   complete: () => this.toast.close(waitToast)
    // })


  }

  selectTicket(ticket: Ticket): void {
    this.currentTicket = ticket;
  }

  demo(): void {
    //   const colDef = (colId: string) => this.agColumns.find(c => c.field === colId);
    //   const list = this.agTable.tableApi.getAllDisplayedColumns().map((c, index) => ({
    //     ...colDef(c.getColId()),
    //     position: index,
    //     width: c.getActualWidth()
    //   }))
    //   console.log(JSON.stringify(list))

    this.agTable.tableApi.selectAll();
    this.deleteTicket();


    // const state = this.agTable.tableApi.getColumnState().filter(s => s.hide === false).map(s => Objects.extractValueNotNull(s));
    // const visibleColumns = this.agTable.tableApi.getAllDisplayedColumns().map(c => c.getColId());
    // console.log(JSON.stringify({states: state}))
    // console.log(visibleColumns)
  }

  removeUser($event: TagRemoveEvent) {
    console.log('remove user', $event.value);
  }
}
