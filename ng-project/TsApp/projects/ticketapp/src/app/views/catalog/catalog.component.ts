import {
  AfterViewInit,
  ChangeDetectionStrategy,
  ChangeDetectorRef,
  Component,
  OnInit,
  ViewEncapsulation,
} from "@angular/core";
import { ToastService } from "ts-ui/toast";
import { CatalogService } from "../../services/catalog.service";
import { DynamicDialogComponent, DynamicDialogRef } from "primeng/dynamicdialog";
import { LoggerService } from "ts-ui/logger";
import { Objects } from "ts-ui/helper";
import { delay } from "rxjs";
import { ModalService } from "ts-ui/modal";
import { FormGroup, FormsBuilder } from "ts-ui/forms";

const { isTrue, notNull, arrayToJson } = Objects;



@Component({
  selector: "ts-catalog",
  templateUrl: "./catalog.component.html",
  encapsulation: ViewEncapsulation.None,
  changeDetection: ChangeDetectionStrategy.OnPush,
  styleUrls: [`./catalog.component.scss`],
})
export class CatalogComponent implements OnInit, AfterViewInit {
  checkboxes: any[] = [
    { field: "ls_chanel", title: "Kênh - Tình trạng" },
    { field: "ls_software", title: "Nhóm Phần mềm" },
    { field: "ls_group_help", title: "Nhóm Phần mềm" },
    { field: "ls_helpdesk_team", title: "Support Team" },
    { field: "ls_assign", title: "Người dùng" },
    { field: "ls_subject_type", title: "Ticket Subject Type" },
    { field: "ls_replied_status", title: "Replied Status" },
    { field: "ls_category", title: "Danh mục" },
    { field: "ls_category_sub", title: "Danh mục phụ" },
    { field: "ls_team_head", title: "Team Head" },
    { field: "ls_ticket_type", title: "Loại yêu cầu hỗ trợ" },
    { field: "ls_priority", title: "Độ ưu tiên" },
    { field: "ls_ticket_tag", title: "Thẻ -- Tags" },
    { field: "ls_question", title: "Nội dung mẫu" },
    { field: "ls_ticket_template", title: "DS Ticket" },
    { field: "ls_email_template", title: "DS Email" },
  ];



  asyncLoading: boolean = false;
  autoLoad: boolean = false;
  hasDialog: boolean = false;
  templateCode: string[] = [];
  form: FormGroup;

  get cChecked(): FormGroup {
    return this.form?.get('all') as any;
  }

  constructor(
    private def: ChangeDetectorRef,
    private logger: LoggerService,
    private toast: ToastService,
    private fb: FormsBuilder,
    private modal: ModalService,
    private catalogSrv: CatalogService,
    private ref: DynamicDialogRef) { }

  ngOnInit(): void {

    this.form = this.fb.group({
      selectAll: [false],
      all: this.fb.group({})
    });

    this.checkboxes.forEach(item => this.cChecked
      .addControl(item.field, this.fb.control(item.check ?? false)));

    this.form.controlChange('selectAll', (c, b) => this.handleSelectAll(b));


    this.hasDialog = notNull(this.modal.getInstance(this.ref));

    const dialogData: any = this.modal.getData(this.ref);
    if (notNull(dialogData)) {
      const { templateCode, autoLoad } = dialogData;
      this.autoLoad = autoLoad;
      this.templateCode = templateCode;
    }
  }

  ngAfterViewInit(): void {
    this.form.path_value('selectAll', true);
    if (this.autoLoad)this.loadCatalog();
  }

  closeDialog() {
    this.ref.destroy();
  }

  loadCatalog() {
    const all = this.cChecked.getRawValue();
    const data = Object.keys(all).filter(k => isTrue(all[k]));

    if (data.length == 0) {
      this.toast.warning("Vui lòng chọn ít nhất 1 danh mục để tải.");
      return;
    }

    this.asyncLoading = true;

    const catalog = data.join(",");
    const entities = this.templateCode.join(',');

    this.catalogSrv.getAll({ catalog, entities }).pipe(delay(1000)).subscribe({
      next: (res) => {
        this.asyncLoading = false;
        this.toast.success("Lấy danh mục thành công.");
        this.ref.close(res);
        this.def.detectChanges();
      },
      error: (err) => {
        this.asyncLoading = false;
        if (err.code !== 'e_server') {
          this.toast.error("Lấy danh mục bị lỗi");
        }
        this.logger.error("Lấy danh mục bị lỗi: ", err);
        this.def.detectChanges();
      },
    });
  }

  handleSelectAll(checked: boolean): void {
    const controls = this.cChecked.controls;
    Object.values(controls).forEach(c => c.setValue(checked));
  }








}