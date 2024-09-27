import { Component, Input, OnInit, ViewEncapsulation } from '@angular/core';
import { Validators } from "@angular/forms";
import { Objects } from "ts-ui/helper";
import { ToastService } from "ts-ui/toast";
import { ApiInfo, UserApi } from "../../models/api-info";
import { ApiInfoService } from "../../services/api-info.service";
import { StorageService } from "../../services/storage.service";
import { DynamicDialogRef } from 'primeng/dynamicdialog';
import { LoggerService } from 'ts-ui/logger';
import { ModalService } from "../../services/ui/model.service";
import { FormGroup, FormsBuilder } from 'ts-ui/forms';
import { delay } from 'rxjs';
import { Alert } from 'ts-ui/alert';

const { isBlank, notNull, isNull, isFalse } = Objects;

@Component({
  selector: 'ts-api-info',
  encapsulation: ViewEncapsulation.None,
  templateUrl: './api-info.component.html',
  styleUrl: './api-info.component.scss'
})
export class ApiInfoComponent implements OnInit {
  asyncSave: boolean = false;
  asyncLoad: boolean = false;
  asyncUpdateMenu: boolean = false;
  hasCopy: boolean = false;
  hasDialogRef: boolean = false;
  visibleBtnCopy: boolean = false;
  isSystem: boolean = false;

  @Input({ alias: 'sname' })
  service_name: string = undefined;

  @Input({ alias: 'suid' })
  service_uid: string = undefined;

  formGroup: FormGroup;
  lsApiCode: ApiInfo[];
  fgApiInfo: FormGroup;
  fgUserApi: FormGroup;
  currentApiItem: ApiInfo;

  get hasNew(): boolean {
    return isBlank(this.fgApiInfo.get_value('api_id'));
  }

  get editApiInfoForm(): boolean {
    return this.hasCopy;
  }

  get visibleBtnSave(): boolean {
    return Objects.anyTrue(this.hasCopy, this.fgUserApi.invalid, this.formDirty) 
        && this.formGroup.get('api_item').valid;
  }

  get visibleUpdateLink(): boolean {
    return !this.hasNew && this.fgUserApi.valid  && this.formNoChange;
  }

  get labelSave(): string {
    return this.hasNew ? 'Tạo mới' : 'Cập nhật'
  }

  get formDirty(): boolean {
    return this.fgApiInfo?.dirty || this.fgUserApi?.dirty;
  }

  
  get formNoChange(): boolean {
    return this.fgApiInfo?.pristine && this.fgUserApi?.pristine;
  }

  get enableReset(): boolean {
    return this.formDirty || this.hasCopy;
  }

  constructor(
    private fb: FormsBuilder,
    private logger: LoggerService,
    private storage: StorageService,
    private config: StorageService,
    private apiSrv: ApiInfoService,
    //private userApi: UserApiService,
    private toast: ToastService,
    private modal: ModalService,
    private alert: Alert,
    private dialogRef: DynamicDialogRef) {

    this.fgApiInfo = this.fb.group({
      api_id: [null],
      title: [null, Validators.required],
      summary: [null],
      base_url: [null, Validators.required],
      service_uid: [{ value: null, disabled: true }, Validators.required],
      service_name: [null, Validators.required],
      login_path: [null, Validators.required],
      allow_copy: [false],
      headers: [null],
      queries: [null],
      links: [null]
    });

    this.fgUserApi = this.fb.group({
      user_name: [null, Validators.required],
      password: [null, Validators.required],
      allow_edit: [false],
      csrf_token: [{ value: null, disabled: true }],
      cookie_value: [{ value: null, disabled: true }],
      user_info: [{ value: null, disabled: true }],
      auto_login: [true]
    });

    this.formGroup = this.fb.group({
      api_item: [null, Validators.required],
      api_info: this.fgApiInfo,
      user_api: this.fgUserApi
    });

    this.fgUserApi.controlValueChange('allow_edit', b => this.allowEdit(b));
    this.setState(false);

  }


  ngOnInit() {
    //this.user = this.storage.loginUser;

    const instanceData: any = this.modal.getData(this.dialogRef);
    this.hasDialogRef = notNull(instanceData);
    if (instanceData) {
      const { suid, sname } = instanceData;
      this.hasDialogRef = true;
      this.service_name = sname;
      this.service_uid = suid;
    }

    this.loadApiInfo();

  }

  private setState(enable: boolean): void {
    if(isFalse(enable)) {
      this.fgApiInfo.disable();
      this.hasCopy = false;
    }
    else {
      this.fgApiInfo.enable();
      this.fgApiInfo.get('service_uid').disable();

      const {is_system} = this.fgApiInfo.getRawValue();
      if(is_system) this.fgApiInfo.get('service_name').disable();
    }
  }

  /** Click checkbox allow_edit */
  allowEdit(checked: boolean): void {
    this.fgUserApi.enableOrDisableControl('cookie_value', checked);
    this.fgUserApi.enableOrDisableControl('csrf_token', checked);
  }

  /** Click button copy */
  onCopyApi(): void {
    let apiItem: ApiInfo = this.formGroup.get('api_item').getRawValue();
    if (notNull(apiItem)) {
      const newItem = apiItem.clone();
      newItem.update({
        api_id: undefined,
        service_name: undefined,
        base_url: undefined,
        is_system: false,
        allow_copy: true,
        links: undefined,
        user_api: {
          user_name: newItem.user_api?.user_name,
          password: undefined,
          auto_login: false,
          allow_edit: false
        }
      });

      newItem.user_api.password = undefined;
      this.formPathValue(newItem, { emitEvent: true });
      this.setState(true);
      this.hasCopy = true;
    }
  }

  /** Click `Lấy DS` */
  loadApiInfo(): void {
    this.asyncLoad = true;
    this.apiSrv
      .search({ sname: this.service_name, suid: this.service_uid })
      .pipe(delay(1000)).subscribe({
        error: err => {
          this.logger.error('loadApiInfo', err);
          this.asyncLoad = false;
        },
        next: page => {
          this.asyncLoad = false;
          this.lsApiCode = page.data || [];
          this.toast.success(this.config.i18n.loadApiOk);

          if(this.hasCopy === false && this.lsApiCode.length > 0) {
            this.formGroup.pathControl('api_item', this.lsApiCode[0]);
            this.onSelectApi(this.lsApiCode[0]);
          }

        }
      })
  }

  onSelectApi(value: ApiInfo, hasCheck: boolean = true): void {

    if(this.currentApiItem !== value) {
      this.currentApiItem = value;
      this.formGroup.pathControl('api_item', value);
    }


    this.visibleBtnCopy = value && value.allow_copy == true;

    // data is new
    if (hasCheck && this.hasCopy === true && this.formDirty) {
      if (notNull(value)) {

        const ref = this.alert.warning({
          summary: `Bạn đang tạo mới thông tin API. Bạn có muốn thay đổi thông tin khác ?`,
          title: 'Cảnh báo !!',
          actions: [
            { label: 'Thay đổi', onClick: e => e.dynamicRef.close('ok') },
            { label: 'Hủy bỏ', onClick: e => e.dynamicRef.close('cancel') }
          ]
        });

        ref.onClose.subscribe(act => {
          if ("ok" == act) {
            this.hasCopy = false;
            this.onSelectApi(value);
          }
        })

      }
    }

    // handle select api
    else if (isNull(value)) {
      this.formGroup.reset({}, { emitEvent: false, onlySelf: true });
      this.onResetForm(false);
    }
    else if (notNull(value.user_api)) {
      this.formPathValue(value);
    }
    else {
      this.asyncLoad = true;
      this.apiSrv.loadUserBySName(value.service_name).subscribe({
        error: _ => this.asyncLoad = false,
        next: (user: UserApi) => {
          value.user_api = user;
          this.asyncLoad = false;
          this.formPathValue(value);
        }
      });

    }


  }

  checkLoginApi(close: boolean = false): void {
    if (this.validateActionWithoutSave()) { }
    else {
      this.asyncSave = true;

      const loadingRef = this.toast.loading(this.config.i18n.awaitHandle);
      const { service_name } = this.formGroup.getRawValue()?.api_info;

      this.apiSrv.checkLoginApiBySName(service_name).subscribe({
        error: _ => {          
          this.toast.close(loadingRef);
          this.asyncSave = false;
        },
        next: (cls: UserApi) => {       
          this.toast.close(loadingRef);
          this.toast.success(this.config.i18n.checkApiOk);
          this.asyncSave = false;
          this.formGroup.patchValue({
            cookie_value: cls.cookie_value,
            csrf_token: cls.csrf_token
          });

          if (close) {
            this.closeDialogRef();
          }
        }
      });
    }

  }

  clickUpdateMenuLink(): void {
    if (this.validateActionWithoutSave()) { }
    else {    
      const loadingRef = this.toast.loading(this.config.i18n.awaitHandle);
      const { service_name } = this.formGroup.getRawValue()?.api_info;

      this.asyncUpdateMenu = true;

      this.apiSrv.getMenuLink(service_name).subscribe({
        error: _ => {
          this.asyncUpdateMenu = false;
          this.toast.close(loadingRef);
          console.error(`clickUpdateMenuLink: `, _);
        },
        next: links => {
          this.fgApiInfo.pathControl('links', JSON.stringify(links));
          this.toast.close(loadingRef);
          this.toast.success(this.config.i18n.updateMenuOk);
          this.asyncUpdateMenu = false;
        }
      });
    }

  }

  onResetForm(hasCheck: boolean = true): void {
    const api_item = this.formGroup.get_value('api_item');
    if(isFalse(hasCheck) || this.formNoChange)  {
      this.onSelectApi(api_item);
      this.setState(false);
    }
    else if(this.fgApiInfo.dirty || this.fgUserApi.dirty) {
      const ref = this.alert.warning({
        title: 'Cảnh báo !!', 
        summary: 'Bạn có muốn lấy lại thông tin trước khi sửa không ?',
        actions: [
          {label: 'Lấy lại', onClick: e => e.dynamicRef.close('ok')},
          {label: 'Bỏ qua', onClick: e => e.dynamicRef.close('cancel')}
        ]
      });

      ref.onClose.subscribe(act => {
        if(act === "ok") {
          const api_item = this.formGroup.get_value('api_item');
          this.onSelectApi(api_item, false);
          this.setState(false);
        }
      });
    }

  }

  onSaveUserApi() {
   
    if (this.formGroup.invalid) {
      this.toast.warning(this.config.i18n.form_invalid)
      return;
    }

    const {api_info, user_api} = this.formGroup.getRawValue();

    let apiInfo: Partial<ApiInfo> = undefined;
    if(this.fgApiInfo.dirty) {
      apiInfo = api_info;
      apiInfo.headers = JSON.parse(api_info.headers || '{}');
      apiInfo.queries = JSON.parse(api_info.queries || '{}');
      apiInfo.links = JSON.parse(api_info.links || '{}');
    }

    const saveData = {
      api_id: this.hasCopy ? null : apiInfo?.api_id,
      api_info: Objects.extractValueNotNull(apiInfo), 
      user_api: this.fgUserApi.dirty ? Objects.extractValueNotNull(user_api) : null
    };

    this.apiSrv.saveAll(saveData).subscribe({
      error: _ => {
        this.asyncSave = false;
        console.error(`onSaveUserApi: `,_);
      },
      next: data => {
        if(this.hasCopy) this.lsApiCode.push(data);
        this.toast.success(this.config.i18n.saveApiOk);
        this.onSelectApi(data, false);
        this.setState(false);
        this.hasCopy = false;
      }
    });

  }

  private validateActionWithoutSave() {
    if (this.visibleBtnSave) {
      this.toast.warning({
        message: `Bạn đã thay đổi dữ liệu. Vui lòng <b>[lưu]</b> trước khi thực hiện chức năng này`,
        width: '400px'
      });
    }
    return this.visibleBtnSave;
  }

  private formPathValue(info: Partial<ApiInfo>, options?: any) {
    this.fgApiInfo.enableOrDisableControl('service_name', info?.is_system === false);

    if (info?.user_api) {
      this.fgUserApi.reset({
        ...info.user_api,
        api_id: info?.api_id
      }, options);
    }

    const upJson: any = { ...info };
    if (info?.headers) upJson.headers = JSON.stringify(info?.headers);
    if (info?.queries) upJson.queries = JSON.stringify(info?.queries);
    if (info?.links) upJson.links = JSON.stringify(info?.links);
    this.fgApiInfo.reset(upJson, options);
    this.hasCopy = false;
  }



  closeDialogRef(): void {
    if (this.hasDialogRef) {
      this.dialogRef.close();
    }
  }

  static showDialog(modal: ModalService, apiCode: string) {
    modal.open(ApiInfoComponent, {
      header: 'Cấu hình thông tin xác thực',
      width: '500px',
      data: { apiCode }
    })

  }

}