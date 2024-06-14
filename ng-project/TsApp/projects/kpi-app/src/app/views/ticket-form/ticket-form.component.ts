import {Component, OnInit, ViewEncapsulation} from '@angular/core';
import {FormBuilder, FormGroup} from "@angular/forms";
import {MenuItem, MenuItemCommandEvent} from "primeng/api";
import {TemplateMap, Ticket} from "../../models/ticket";
import {CheckboxChangeEvent} from "primeng/checkbox";
import {DialogService} from "primeng/dynamicdialog";
import {TemplateFormComponent} from "../template-form/template-form.component";
import {TicketOption} from "../../models/ticket-option";
import {FindPartnerComponent} from "../find-partner/find-partner.component";
import {CatalogService} from "../../services/catalog.service";

import { Catalog } from '../../models/catalog';
import { ToastService } from '../../services/toast.service';
import { Objects } from '../../utils/objects';
import { Software } from '../../models/software';

const {notNull} = Objects;

@Component({
  selector: 'ts-ticket-form',
  templateUrl: './ticket-form.component.html',
  styleUrl: './ticket-form.component.scss',
  encapsulation: ViewEncapsulation.None
})
export class TicketFormComponent implements OnInit{

  ticketForm: FormGroup;
  ticket: Ticket = new Ticket();
  asyncLoadCate: boolean = false;

  templates: TemplateMap;
  catalog: Catalog = new Catalog();
  lsSoftName: string[] = [];

  toolActions: MenuItem[] = [
    {label: 'Lấy mã kích hoạt', icon: 'pi pi-key', command: event => this.onLoadLicense(event) },
    {label: 'Tải danh mục', icon: 'pi pi-home', command: event => this.onLoadCatalogs() },
    {label: 'Tạo mẫu ticket', icon: 'pi pi-home',  command: event => this.onCreateTemplate(event) }
  ];

  constructor(private fb: FormBuilder,
    private toast: ToastService,
              private catalogSrv: CatalogService,
              private dialogService: DialogService) {
  }

  ngOnInit() {
    this.onLoadCatalogs();
    this.ticketForm = this.fb.group({
      full_name: [null],
      tax_code: [null],
      company_name: [null],
      phone: [null],
      teamviewer: [null],
      customer_name: [null],
      content_required: [null],
      content_help: [null],
      reception_time: [null],
      complete_time: [null],
      content_copy: [null],
      email: [null],
      subject: [null],
      body: [null],
      note: [null],
      reply: [null],
      content_email: [null],
      group_help: [null],
      question: [null],
      software: [null],
      chanels: [null],
      support_help: [null],
      soft_name: [null],
      od_image: [null],
      od_assign: [null],
      od_category_sub: [null],
      od_category: [null],
      od_partner: [null],
      od_priority: [null],
      od_replied: [null],
      od_subject_type: [null],
      od_tags: [null],
      od_team: [null],
      od_team_head: [null],
      od_ticket_type: [null],
      od_topic: [null]
    });
  }

  get isEditTicket(): boolean {
    return false;
  }

  get isEmailTicket(): boolean {
    return this.options.emailTicket ?? false;
  }

  get isViewAll(): boolean {
    return this.options.viewAll ?? true;
  }

  get isViewTs24(): boolean {
    return this.options.viewTs24 ?? false;
  }

  get options(): TicketOption {
    return this.ticket.get_options();
  }

  get formRawValue(): Ticket {
    return this.ticketForm.getRawValue();
  }


  onLoadLicense(event: MenuItemCommandEvent) {

  }

  onLoadCatalogs() {
    this.asyncLoadCate = true;
    this.toast.loading({summary: 'Đang lấy danh mục. Vui lòng chờ'});

    this.catalogSrv.getAll().subscribe({
      error: err => {
        this.asyncLoadCate = false;
        this.toast.clearAll();
      },
      next: res => {
        this.catalog = res; 
        this.toast.clearAll();
        console.log(res);
      },
      
    });

  }

  onCreateTemplate(event: MenuItemCommandEvent) {
    const ref = this.dialogService.open(TemplateFormComponent, {
      header: 'Tạo mẫu',
      data: {ticket: this.ticket}
    });

    ref.onClose.subscribe({
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

  changeSelectedItem(field: string, data: any) {
    if('#software' === field ){
      const item: Software = data;
      this.lsSoftName = notNull(data) ? item.soft_names : [];
      this.pathValue('soft_name', this.lsSoftName[0]);
    }

    else throw new Error('Method not implemented.');
  }

  private pathValue(path: string, value: any) {
    this.ticketForm.get(path).patchValue(value);
  }
}
