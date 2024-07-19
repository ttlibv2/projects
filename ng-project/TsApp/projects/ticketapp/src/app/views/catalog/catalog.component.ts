import {
  AfterViewInit,
  ChangeDetectionStrategy,
  ChangeDetectorRef,
  Component,
  OnInit,
  ViewChild,
  ViewEncapsulation,
} from "@angular/core";
import {
  AgTableComponent,
  TableColumn,
  TableOption,
} from "ts-ui/ag-table";
import { ToastService } from "../../services/toast.service";
import { CatalogService } from "../../services/catalog.service";
import { DynamicDialogComponent, DynamicDialogRef } from "primeng/dynamicdialog";
import { LoggerService } from "ts-logger";
import { Objects } from "ts-helper";
import { delay } from "rxjs";

interface CateView {
  title: string;
  action: string;
  checked?: boolean;
}

@Component({
  selector: "ts-catalog",
  templateUrl: "./catalog.component.html",
  encapsulation: ViewEncapsulation.None,
  changeDetection: ChangeDetectionStrategy.OnPush,
  styleUrls: [`./catalog.component.scss`],
})
export class CatalogComponent implements OnInit, AfterViewInit {
  columns: TableColumn[] = [
    {
      field: "title",
      headerName: "Tiêu đề",
      headerCheckboxSelection: true,
      checkboxSelection: true,
    },
    { field: "action", headerName: "Mã" },
    { field: "version", headerName: "Phiên bản" },
  ];

  rows: CateView[] = [
    { action: "ls_chanel", title: "Kênh - Tình trạng" },
    { action: "ls_software", title: "Nhóm Phần mềm" },
    { action: "ls_group_help", title: "Nhóm Phần mềm" },
    { action: "ls_helpdesk_team", title: "Support Team" },
    { action: "ls_assign", title: "Người dùng" },
    { action: "ls_subject_type", title: "Ticket Subject Type" },
    { action: "ls_repiled_status", title: "Replied Status" },
    { action: "ls_category", title: "Danh mục" },
    { action: "ls_category_sub", title: "Danh mục phụ" },
    { action: "ls_team_head", title: "Team Head" },
    { action: "ls_ticket_type", title: "Loại yêu cầu hỗ trợ" },
    { action: "ls_priority", title: "Độ ưu tiên" },
    { action: "ls_ticket_tag", title: "Thẻ -- Tags" },
    { action: "ls_question", title: "Nội dung mẫu" },
    { action: "ls_template", title: "Danh sách mẫu" },
  ];

  option: TableOption = {
    rowSelection: "multiple",
    domLayout: "normal",
  };

  @ViewChild(AgTableComponent, { static: true })
  agTable: AgTableComponent<CateView>;

  instance: DynamicDialogComponent;

  asyncLoading: boolean = false;
  autoLoad: boolean = false;
  templateCode: string;

  constructor(
    private def: ChangeDetectorRef,
    private logger: LoggerService,
    private toast: ToastService,
    private catalogSrv: CatalogService,
    private ref: DynamicDialogRef) { }

  ngOnInit(): void {
    this.instance = this.toast.getDialogComponentRef(this.ref)?.instance;
    if (Objects.notNull(this.instance) && this.instance.data) {
      const { templateCode, autoLoad } = this.instance.data;
      this.autoLoad = autoLoad;
      this.templateCode = templateCode;
    }


  }

  ngAfterViewInit(): void {
    this.agTable.tableApi.selectAll();
    if (this.autoLoad) {
      this.loadCatalog();
    }
  }

  closeDialog() {
    this.ref.destroy();
  }

  loadCatalog() {
    const ls = this.agTable.getSelectedRows();
    if (ls.length == 0) {
      this.toast.warning({ summary: "Vui lòng chọn ít nhất 1 dòng." });
      return;
    }

    this.asyncLoading = true;

    const catalog = ls.map((i) => i.action).join(",");
    const entities = this.templateCode;

    this.catalogSrv.getAll({ catalog, entities }).pipe(delay(1000)).subscribe({
      next: (res) => {
        this.asyncLoading = false;
        this.toast.success({ summary: "Lấy danh mục thành công." });
        this.ref.close(res);
        this.def.detectChanges();
      },
      error: (err) => {
        this.asyncLoading = false;
        if (err.code !== 'disconnect') {
          this.toast.error({ summary: "Lấy danh mục bị lỗi" });
        }
        this.logger.error("Lấy danh mục bị lỗi: ", err);
        this.def.detectChanges();
      },
    });
  }
}