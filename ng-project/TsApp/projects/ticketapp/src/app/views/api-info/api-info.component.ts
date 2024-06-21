import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from "@angular/forms";
import { UserService } from "../../services/user.service";
import { ToastService } from "../../services/toast.service";
import { ApiInfo } from "../../models/api-info";
import { ApiInfoService } from "../../services/api-info.service";
import { Observable, of, switchMap } from "rxjs";
import { TranslateService } from "@ngx-translate/core";
import { ConfigService } from '../../services/config.service';
import { Objects } from "ts-helper";

@Component({
  selector: 'ts-api-info',
  templateUrl: './api-info.component.html',
  styleUrl: './api-info.component.scss'
})
export class ApiInfoComponent implements OnInit {

  asyncLoad: boolean = false;

  formGroup: FormGroup;
  asyncSave: boolean = false;
  hasChangeData: boolean = false;
  infos: Observable<ApiInfo[]>;

  constructor(private fb: FormBuilder,
    private userSrv: UserService,
    private config: ConfigService,
    private i18n: TranslateService,
    private apiSrv: ApiInfoService,
    private toast: ToastService) {
  }

  loadData() {
    this.formGroup.disable();
    this.infos = this.apiSrv.findAll().pipe(switchMap(res => {
      this.toast.success({ summary: this.config.i18n.loadApiOk })
      this.formGroup.enable();
      this.formGroup.get('api_item').patchValue(res?.data[0]);
      this.onSelectApi(res?.data[0]);
      this.allowEdit(false);
      return of((res.data));
    }));
  }

  ngOnInit() {

    this.formGroup = this.fb.group({
      api_item: [null, Validators.required],
      user_name: [null, Validators.required],
      password: [null, Validators.required],
      allow_edit: [false],
      csrf_token: [{ value: null, disabled: true }],
      cookie: [{ value: null, disabled: true }],
      info: [{ value: null, disabled: true }],
      auto_login: [true]
    });



    this.formGroup.valueChanges.subscribe(value => {
      this.hasChangeData = true;
    });

    this.loadData();
  }

  allowEdit(checked: boolean): void {
    const ck = this.formGroup.get('cookie');
    const cs = this.formGroup.get('csrf_token');

    checked ? ck.enable() : ck.disable();
    checked ? cs.enable() : cs.disable();

  }

  get disabledCheckApi(): boolean {
    const { api_item, user_name, password } = this.formGroup.getRawValue();
    return this.hasChangeData === true || (Objects.isNull(api_item) || Objects.anyBlank(user_name, password));
  }

  onSelectApi(value: ApiInfo) {
    if (Objects.isNull(value)) {
      this.formGroup.reset();
    }
    else if(Objects.notNull(value.user_api)) {
      this.formGroup.patchValue(value.user_api);
      this.hasChangeData = false;
    }
    else {
      this.asyncLoad = true;
      this.apiSrv.getUserByCode(value.code).subscribe({
        error: err => this.asyncLoad = false,
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

    const value = this.formGroup.value;
    const apiCode = value.api_item?.code;
    this.apiSrv.saveUserApi(apiCode, value).subscribe({
      error: err => this.asyncSave = false,
      next: res => {
        this.asyncSave = false;
        this.hasChangeData = false;
        this.toast.success({ summary: this.config.i18n.saveOk });
      }
    });
  }

  checkApi(): void {
    this.asyncSave = false;

    const loadingRef = this.toast.loading({ summary: this.config.i18n.awaitHandle })

    this.apiSrv.checkLogin().subscribe({
      error: err => {
        this.asyncSave = false;
        this.toast.close(loadingRef.toastId);
      },
      next: res => {
        this.asyncSave = false;
        this.toast.close(loadingRef.toastId);
        this.formGroup.get('cookie').patchValue(res.cookie);
        this.formGroup.get('csrf_token').patchValue(res.csrf_token);
        this.toast.success({ summary: this.config.i18n.checkApiOk });
      }
    });
  }
}