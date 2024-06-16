import {Component, OnInit} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {UserService} from "../../services/user.service";
import {ToastService} from "../../services/toast.service";
import {ApiInfo} from "../../models/api-info";
import {ApiInfoService} from "../../services/api-info.service";
import {Observable, of, switchMap} from "rxjs";
import {TranslateService} from "@ngx-translate/core";
import { ConfigService } from '../../services/config.service';

@Component({
  selector: 'ts-api-info',
  templateUrl: './api-info.component.html',
  styleUrl: './api-info.component.scss'
})
export class ApiInfoComponent implements OnInit {
  asyncLoad: boolean = false;
  allowCheck: boolean = false;

  formGroup: FormGroup;
  _asyncSave: boolean = false;
  infos: Observable<ApiInfo[]>;

  constructor(private fb: FormBuilder,
              private userSrv: UserService,
              private config: ConfigService,
              private i18n: TranslateService,
              private apiSrv: ApiInfoService,
              private toast: ToastService) {
  }

  loadData() {
    this.infos = this.apiSrv.getAll()
      .pipe(switchMap(res => of((res.data))));
  }

  ngOnInit() {
    this.loadData();

    this.formGroup = this.fb.group({
      api_item: [null, Validators.required],
      user_name: [null, Validators.required],
      password: [null, Validators.required],
      allow_edit: [false],
      csrf_token: [{value: null, disabled: true}],
      cookie: [{value: null, disabled: true}],
      info: [{value: null, disabled: true}],
      auto_login: [true]
    });

    this.formGroup.valueChanges.subscribe(val => {
      this.allowCheck = false;
    });

  }


  get asyncSave(): boolean {
    return this._asyncSave;
  }

  allowEdit(checked: boolean): void {
    const ck = this.formGroup.get('cookie');
    const cs = this.formGroup.get('csrf_token');

    checked ? ck.enable() : ck.disable();
    checked ? cs.enable() : cs.disable();

  }

  onSelectApi(value: ApiInfo) {

    this.asyncLoad = true;
    this.allowCheck = true;

    this.apiSrv.getUserByCode(value.code).subscribe({
      error: err => this.asyncLoad = false,
      next: res => {
        this.asyncLoad = false;
        value.user_api = res;
        this.formGroup.patchValue(res);
        this.allowCheck = true;
      }
    });
  }

  onSave() {
    if(this.formGroup.invalid) {
      this.toast.warning({summary: this.config.i18n.form_invalid})
      return;
    }

    const value = this.formGroup.value;
    const apiCode = value.api_item?.code;
    this.apiSrv.saveUserApi(apiCode, value).subscribe({
      error: err => this._asyncSave = false,
      next: res => {
        this._asyncSave = false;
        this.allowCheck = true;
        this.toast.success({summary: this.config.i18n.saveOk});
      }
    });
  }

  checkApi(): void {
    this._asyncSave = false;

    this.apiSrv.checkLogin().subscribe({
      error: err => this._asyncSave = false,
      next: res => {
        this._asyncSave = false;
        this.formGroup.get('cookie').patchValue(res.cookie);
        this.formGroup.get('csrf_token').patchValue(res.csrf_token);
        //this.toast.success({summary: this.config.i18n.checkApiOk});
      }
    });
  }
}
