import {Component, OnInit, ViewEncapsulation} from '@angular/core';
import {FormBuilder, FormGroup} from "@angular/forms";
import {MenuItem, MenuItemCommandEvent} from "primeng/api";
import {TemplateMap, TemplateObj, Ticket} from "../../models/ticket";
import {CheckboxChangeEvent} from "primeng/checkbox";
import {DialogService, DynamicDialogRef} from "primeng/dynamicdialog";
import {TemplateFormComponent} from "../template-form/template-form.component";
import {TicketOption} from "../../models/ticket-option";
import {FindPartnerComponent} from "../find-partner/find-partner.component";

@Component({
  selector: 'ts-ticket-form',
  templateUrl: './ticket-form.component.html',
  styleUrl: './ticket-form.component.scss',
  encapsulation: ViewEncapsulation.None
})
export class TicketFormComponent implements OnInit{
  ticketForm: FormGroup;
  ref: DynamicDialogRef | undefined;
  ticket: Ticket = new Ticket();
  templates: TemplateMap;

  toolActions: MenuItem[] = [
    {label: 'Lấy mã kích hoạt', icon: 'pi pi-key', command: event => this.onLoadLicense(event) },
    {label: 'Tải danh mục', icon: 'pi pi-home', command: event => this.onLoadCatalogs(event) },
    {label: 'Tạo mẫu ticket', icon: 'pi pi-home',  command: event => this.onCreateTemplate(event) }
  ];

  constructor(private fb: FormBuilder,
              private dialogService: DialogService) {
  }

  ngOnInit() {
    this.ticketForm = this.fb.group({
    });
  }

  get isEditTicket(): boolean {
    return false;
  }

  get isEmailTicket(): boolean {
    return this.options.emailTicket ?? false;
  }

  get isViewAll(): boolean {
    return this.options.viewAll ?? false;
  }

  get isViewTs24(): boolean {
    return this.options.viewTs24 ?? false;
  }

  get options(): TicketOption {
    return this.ticket.get_options();
  }

  onLoadLicense(event: MenuItemCommandEvent) {

  }

  onLoadCatalogs(event: MenuItemCommandEvent) {

  }

  onCreateTemplate(event: MenuItemCommandEvent) {
    this.ref = this.dialogService.open(TemplateFormComponent, {
      header: 'Tạo mẫu',
      data: {ticket: this.ticket}
    });

    this.ref.onClose.subscribe({
      next: value => {
        if(value != undefined) {
          this.templates.save(value.data);
        }
      }
    });
  }

  selectTemplate(title: string) {
    //console.log(this.listTemplate.getTicket(title));
  }

  changeOptions(optionKey: string, $event: CheckboxChangeEvent) {
    this.options[optionKey] = $event.checked;
  }

  onSaveTicket() {
    console.log(this.options);
  }

  onSearchUser() {
    this.dialogService.open(FindPartnerComponent, {
      header: 'Tìm kiếm khách hàng'
    });
  }
}
