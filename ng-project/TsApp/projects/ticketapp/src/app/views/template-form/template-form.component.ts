import {Component, OnInit, ViewEncapsulation} from '@angular/core';
import {DialogService, DynamicDialogComponent, DynamicDialogRef} from "primeng/dynamicdialog";
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import { TemplateObj, Ticket } from '../../models/ticket';

@Component({
  selector: 'ts-template-form',
  templateUrl: './template-form.component.html',
  styleUrl: './template-form.component.scss',
  encapsulation:ViewEncapsulation.None
})
export class TemplateFormComponent implements OnInit {
  form: FormGroup;
  template: TemplateObj;
  ticket: Ticket;

  instance: DynamicDialogComponent | undefined;

  constructor(
    private ref: DynamicDialogRef,
     private dialogSrv: DialogService,
     private fb: FormBuilder) {

  }

  ngOnInit() {

    if(this.instance && this.instance.data) {
      this.ticket = this.instance.data['ticket'];
    }

    this.form = this.fb.group({
      code: [null],
      title: [null, Validators.required],
      summary: [null, Validators.max(300)],
      bgColor: [null, Validators.required],
      textColor: [null, Validators.required]
    });

  }

  closeDialog(): void {
    this.ref.close(this.ticket);
  }

  onSave() {


  }

}
