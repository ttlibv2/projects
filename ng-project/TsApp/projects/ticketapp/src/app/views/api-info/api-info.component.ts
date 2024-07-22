import { Component, inject, Input, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from "@angular/forms";
import { Observable, of, switchMap } from "rxjs";
import { Objects } from "ts-helper";
import { UserService } from "../../services/user.service";
import { ToastService } from "../../services/toast.service";
import { ApiInfo } from "../../models/api-info";
import { ApiInfoService } from "../../services/api-info.service";
import { StorageService } from "../../services/storage.service";
import { DynamicDialogRef } from 'primeng/dynamicdialog';
import { LoggerConfig, LoggerService } from 'ts-logger';
import { FormUtil } from '../../helper/form-util';
import { User } from '../../models/user';

const { notBlank, isBlank } = Objects;



@Component({
  selector: 'ts-api-info',
  templateUrl: './api-info.component.html',
  styleUrl: './api-info.component.scss'
})
export class ApiInfoComponent implements OnInit {

  get disabledCheckApi(): boolean {
    const { api_item, username, password } = this.formGroup.getRawValue();
    return (Objects.isNull(api_item) || Objects.anyBlank(username, password));
  }

  get hasSetApiCode(): boolean {
    return notBlank(this.apiCode);
  }

  hasChangeData: boolean = false;
  asyncSave: boolean = false;
  asyncLoad: boolean = false;
  form: FormUtil = undefined;
  user: User = undefined;
  hasDialogRef: boolean = false;
  formGroup: FormGroup;

  lsApiCode: ApiInfo[];

  @Input()
  apiCode: string = undefined;

  constructor(
    private fb: FormBuilder,
    private logger: LoggerService,
    private storage: StorageService,
    private userSrv: UserService,
    private config: StorageService,
    private apiSrv: ApiInfoService,
    private toast: ToastService,
    private dialogRef: DynamicDialogRef) {
    this.user = this.storage.loginUser;
    this.form = FormUtil.create(

      () => this.fb.group({
        api_item: [null, Validators.required],
        username: [null, Validators.required],
        password: [null, Validators.required],
        allow_edit: [false],
        csrf_token: [{ value: null, disabled: true }],
        cookie: [{ value: null, disabled: true }],
        user_info: [{ value: null, disabled: true }],
        auto_login: [true]
      }),

      form => {
        this.formGroup = form.fg;
        form.formValueChange(_ => this.hasChangeData = true);
        form.controlValueChange('api_item', val => this.onSelectApi(val));
      }

    );
  }


  ngOnInit() {

    const instanceRef = this.toast.getDialogComponentRef(this.dialogRef).instance;
    if(instanceRef && instanceRef.data) {
      this.apiCode = instanceRef.data['apiCode'];
      this.hasDialogRef = true;
    }


    if (isBlank(this.apiCode)) this.loadApiCode();
    else this.findApiCode(this.apiCode);
  }

  allowEdit(checked: boolean): void {
    const ck = this.formGroup.get('cookie');
    const cs = this.formGroup.get('csrf_token');

    checked ? ck.enable() : ck.disable();
    checked ? cs.enable() : cs.disable();

  }

  onSelectApi(value: ApiInfo): void {
    if (Objects.isNull(value)) {
      this.formGroup.reset();
    }
    else if (Objects.notNull(value.user_api)) {
      this.formGroup.patchValue(value.user_api);
      this.hasChangeData = false;
    }
    else {
      this.asyncLoad = true;
      this.apiSrv.getByCode(value.code).subscribe({
        error: _ => this.asyncLoad = false,
        next: res => {
          this.asyncLoad = false;
          value.user_api = res;
          this.formGroup.patchValue(res);
          this.hasChangeData = false;
        }
      });
    }
  }

  onSave() {

    if (this.formGroup.invalid) {
      this.toast.warning({ summary: this.config.i18n.form_invalid })
      return;
    }

    const value = this.form.rawValue();
    const apiCode = value.api_item?.code;

    ['api_item'].forEach(k => delete value[k]);

    this.apiSrv.saveUserApi(apiCode, value).subscribe({
      error: _ => this.asyncSave = false,
      next: _ => {
        this.asyncSave = false;
        this.hasChangeData = false;
        this.toast.success({ summary: this.config.i18n.saveOk });
      }
    });
  }

  checkApi(close: boolean = false): void {
    if (this.disabledCheckApi === false) {
      this.asyncSave = false;

      const loadingRef = this.toast.loading({ summary: this.config.i18n.awaitHandle })

      this.apiSrv.checkLogin().subscribe({
        error: _ => {
          this.asyncSave = false;
          this.toast.closeToast(loadingRef.toastId);
        },
        next: res => {
          this.asyncSave = false;
          this.toast.closeToast(loadingRef.toastId);
          this.formGroup.get('cookie').patchValue(res.cookie);
          this.formGroup.get('csrf_token').patchValue(res.csrf_token);
          this.toast.success({ summary: this.config.i18n.checkApiOk });

          if(close){
            this.closeDialogRef();
          }
        }
      });
    }
  }

  findApiCode(code: string = this.apiCode): void {
    this.apiSrv.getByCode(code, this.user.user_id).subscribe({
      error: err => this.logger.error('findApiCode', err),
      next: data => {
        this.lsApiCode = this.lsApiCode ?? [];

        const api = this.lsApiCode.find(a => a.api_id === data.api_id);
        if (Objects.isNull(api)) this.lsApiCode.push(data);
        else api.update(data);

        this.form.pathControl('api_item', data);

      }
    });
  }

  loadApiCode() {
    this.formGroup.disable();
    this.apiSrv.findAll().subscribe({
      error: err => this.logger.error('loadApiCode', err),
      next: page => {
        this.toast.success({ summary: this.config.i18n.loadApiOk });
        this.form.pathControl('api_item', page.data[0]);



        //this.formGroup.get('api_item').patchValue(res?.data[0]);
        //this.onSelectApi(res?.data[0]);
        //this.allowEdit(false);
        //return of((res.data));
      }

    })
  }

  closeDialogRef(): void {
    if(this.hasDialogRef){
      this.dialogRef.close();
    }
  }

  static showDialog(modal: ToastService, apiCode: string) {
    modal.openDialog(ApiInfoComponent, {
     // header: 'Cấu hình thông tin xác thực',
      data: { apiCode }
    })

  }

}