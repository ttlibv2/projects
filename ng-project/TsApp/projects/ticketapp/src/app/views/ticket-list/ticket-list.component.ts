import { Component, OnInit, ViewChild, ViewEncapsulation } from '@angular/core';
import { LoggerService } from 'ts-logger';
import { AgTableComponent, TableColumn, TableOption } from 'ts-ui/ag-table';
import { Ticket } from '../../models/ticket';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { MenuItem } from 'primeng/api';
import { ToastService } from '../../services/toast.service';
import { AgTableService } from '../../services/ag-table.service';
import { TicketService } from '../../services/ticket.service';
import { AgTicketCell } from './ag-ticket-cell';
import { DatePipe } from '@angular/common';
import { Objects } from 'ts-helper';

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
export class TicketListComponent implements OnInit {
  agRows: Ticket[] = [];
  agOption: TableOption = {
    defaultColDef: {
      suppressHeaderMenuButton: true,
      editable: false,
      // wrapText: true,
      // autoHeight: true,
    },
    rowSelection: 'multiple',
    sideBar: { toolPanels: ['columns'] },
    pivotPanelShow: 'always'
  };
  searchForm: FormGroup;

  agColumns: any[] = [
    { "field": "ticket_id", "width": 110, "sortable": true, "headerName": "STT", "checkboxSelection": true, "headerCheckboxSelection": true },
    { "field": "index_stt", "width": 110, "sortable": true, "headerName": "STT", "checkboxSelection": true, "headerCheckboxSelection": true, valueGetter: 'data.ticket_id', hide: true },
    { "field": "stage_text", "width": 120, "headerName": "Tình trạng" },
    { "field": "ticket_on", "width": 110, "headerName": "Ngày Ticket" },
    { "field": "full_name", "headerName": "Họ và Tên" },
    { "field": "chanels", "headerName": "Kênh - Tình trạng"},
    { "field": "support_help", "width": 146, "headerName": "Hình thức hỗ trợ", "valueGetter": "data.support_help.value" },
    { "field": "chanel_status", "width": 146, "headerName": "Hình thức hỗ trợ", "valueGetter": "data.support_help.value", hide: true },
    { "field": "software", "width": 130, "headerName": "Nhóm phần mềm", "valueGetter": "data.software.value" },
    { "field": "group_help", "width": 130, "headerName": "Nhóm hỗ trợ", "valueGetter": "data.software.value" },
    { "field": "soft_name", "width": 130, "headerName": "Tên phần mềm" },
    { "field": "od_team", "width": 130, "headerName": "Support Team", "valueGetter": "data.od_team.name" },
    { "field": "od_assign", "width": 130, "headerName": "Phân công cho", "valueGetter": "data.od_assign.name" },
    { "field": "od_subject_type", "width": 130, "headerName": "Ticket Subject Type", "valueGetter": "data.od_subject_type.name" },
    { "field": "od_repiled", "width": 130, "headerName": "Replied Status", "valueGetter": "data.od_repiled.name" },
    { "field": "od_category", "width": 130, "headerName": "Danh mục", "valueGetter": "data.od_category.name" },
    { "field": "od_category_sub", "width": 130, "headerName": "Danh mục phụ", "valueGetter": "data.od_category_sub.name" },
    { "field": "od_priority", "width": 130, "headerName": "Độ ưu tiên", "valueGetter": "data.od_priority.name" },
    { "field": "od_tags", "width": 130, "headerName": "Thẻ -- Tags", "valueGetter": "data.od_tags?.map(c => c.name)" },
    { "field": "od_topic", "width": 130, "headerName": "od_topic", "valueGetter": "data.od_topic?.name" },
    { "field": "od_partner", "width": 130, "headerName": "Khách hàng ID", "valueGetter": "data.od_partner?.id" },

    { "field": "tax_code", "width": 130, "headerName": "Mã số thuế" },
    { "field": "email", "width": 130, "headerName": "Email " },
    { "field": "content_required", "width": 130, "headerName": "Nội dung hỗ trợ" },
    { "field": "content_help", "width": 130, "headerName": "Nội dung đã hỗ trợ" },
    { "field": "content_support", "width": 130, "headerName": "Nội dung đã hỗ trợ", hide: true, "valueGetter": "data.content_help" },
    { "field": "reception_time", "width": 130, "headerName": "TG tiếp nhận" },
    { "field": "complete_time", "width": 130, "headerName": "TG hoàn thành" },
    { "field": "od_image", "width": 130, "headerName": "Danh sách hình", "valueGetter": "Object.keys(data.od_image)" },
    { "field": "customer_name", "width": 130, "headerName": "Tên khách hàng" },
    { "field": 'phone', "headerName": 'Số điện thoại' },
    { "field": "teamviewer", "width": 130, "headerName": "Teamviewer" },
    { "field": "content_email", "width": 130, "headerName": "Nội dung email" },
    { "field": "subject", "width": 130, "headerName": "Subject" },
    { "field": "body", "width": 130, "headerName": "Hỗ trợ => Body" },
    { "field": "note", "width": 130, "headerName": "Hỗ trợ => Note" },
    { "field": "content_copy", "width": 130, "headerName": "Hỗ trợ => Copy" },
    { "field": "attach_at", "width": 130, "headerName": "Ngày đính kèm" },
    { "field": "cancel_at", "width": 130, "headerName": "Ngày hủy" },
    { "field": "closed_at", "width": 130, "headerName": "Ngày đóng" },
    { "field": "mail_at", "width": 130, "headerName": "Ngày gửi mail" },
    { "field": "note_at", "width": 130, "headerName": "Ngày thêm note" },
    { "field": "send_at", "width": 130, "headerName": "Ngày tạo ticket" },
    { "field": "report_at", "width": 130, "headerName": "Ngày gửi báo cáo" },
    { "field": "ticket_number", "width": 130, "headerName": "Ticket H_ID" },
    { "field": "ticket_text", "width": 130, "headerName": "Số ticket" },

    { "field": "is_note", "width": 87, "headerName": "Đã ghi chú"},
    { "field": "is_closed", "width": 130, "headerName": "Đã đóng"},
    { "field": "is_upfile", "width": 130, "headerName": "Đã UP_FILE" },
    {"field": "send_status", "headerName": "send_status", position: 1}
  ]

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

  agTemplates: MenuItem[] = [
    { label: '1. Hiển thị tất cả', code: 'all', command: this.onSelectColumnView.bind(this) },
    {
      label: '2. Hiển thị cột KPI', code: 'view_kpi',
      columns: ['index_stt', 'full_name', 'soft_name', 'ticket_text', 'chanel_status', 'content_support'],
      command: this.onSelectColumnView.bind(this)
    },
    {
      label: '3. Hiển thị cột gửi ticket',
      command: this.onSelectColumnView.bind(this),
      code: 'send_ticket', columns: ["index_stt", "send_status", "ticket_text", 
        "is_note", "is_close", "is_upfile", "email", "subject", "od_assign", "od_category", "od_partner", 
        "od_team", "body", "note", "od_image", "od_topic"],
      
    }
  ];

  toolOptions: MenuItem[] = [
    { label: 'Xóa ticket', icon: 'pi pi-trash' },
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

  @ViewChild(AgTableComponent)
  agTable: AgTableComponent;

  constructor(
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

  }

  ngOnInit(): void {
    let now = new Date().getTime();
    let from = new Date(now - (10*24 * 3600_000)); 
    let end = new Date(now + 24 * 3600_000);

    console.log(now, from)
    this.searchForm.patchValue({
      dateOn: [from, end],
      option: this.searchOptions[0]
    });


    //
    this.loadAgTable();
    this.loadData();








  }

  loadAgTable() {
    this.agService.getByCode('ticket_list', true).subscribe({
      error: err => console.warn(err),
      next: res => {
        //this.agColumns = res.get_columns();
      }
    })
  }

  exportXsl(): void {
    this.toast.info({ summary: 'Xuất dữ liệu' });
  }

  loadData(): void {
    const { dateOn } = this.searchForm.getRawValue();

    const created_min = this.datePipe.transform(dateOn[0], 'yyyy-MM-dd') + 'T00:00:00';
    const created_max = this.datePipe.transform(dateOn[1], 'yyyy-MM-dd') + 'T23:59:59';

    const loading = this.toast.loading({
      disableTimeOut: true,
      messageClass: 'p-ticket-msg',
      summary: `Đang lấy dữ liệu - Vui lòng chờ <br/>- Từ ngày: <b>${created_min}</b> <br/> - Đến ngày: <b>${created_max}</b>`
    })


    this.ticketService.search({ created_min, created_max }).subscribe({
      error: err => {
        this.logger.warn(err);
        this.toast.close(loading);
      },
      next: data => {
        this.agRows = data.data;
        this.toast.close(loading);
        this.toast.success({ summary: `Tìm được <b>[${data.data.length}]</b> dòng dữ liệu` })
      }
    });
  }

  onSendHelpdesk(event: any): void {
    console.log(`Click `, event.item.code);
  }

  onSelectColumnView(event: any): void {
    const { code, columns } = event.item;
    if ('all' === code) this.agTable.resetColumnState();
    else {
      this.agTable.hideAllColumn();
      this.agTable.setColumnsVisible(columns, true);
    }
  }

  demo(): void {
    // const colDef = (colId: string) => this.agColumns.find(c => c.field === colId);
    // const list = this.agTable.tableApi.getAllDisplayedColumns().map((c, index) => ({
    //   ...colDef(c.getColId()),
    //   position: index,
    //   width: c.getActualWidth()
    // }))
    // console.log(JSON.stringify(list))
    this.loadAgTable();


    // const state = this.agTable.tableApi.getColumnState().filter(s => s.hide === false).map(s => Objects.extractValueNotNull(s));
    // console.log(JSON.stringify({states: state}))
  }
}
