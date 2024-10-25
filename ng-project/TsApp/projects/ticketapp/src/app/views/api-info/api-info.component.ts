import {Component, Input, numberAttribute, OnDestroy, OnInit, ViewEncapsulation} from '@angular/core';
import { FormControl, Validators } from "@angular/forms";
import { Objects } from "ts-ui/helper";
import { ToastService } from "ts-ui/toast";
import { ApiInfo, UserApi } from "../../models/api-info";
import { ApiInfoService, SaveAction, SaveDto } from "../../services/api-info.service";
import { StorageService } from "../../services/storage.service";
import { DynamicDialogRef } from 'primeng/dynamicdialog';
import { LoggerService } from 'ts-ui/logger';
import { FormsBuilder, FormGroup } from 'ts-ui/forms';
import { ModalService } from 'ts-ui/modal';
import { delay } from 'rxjs';
import { Alert } from 'ts-ui/alert';
import { ClsUser } from '../../models/od-cls';

const { isBlank, notNull, isNull, notBlank, isEmpty, isFalse, booleanValue } = Objects;

interface FormValue {
  api_item?: ApiInfo;
  api_info?: Partial<ApiInfo>;
  user_api?: Partial<UserApi>;
}

interface StateObject {
  loadDs?: boolean;
  disableButton?: boolean;
  hasModal?: boolean;
  visibleBtnLoadApi?: boolean;
  showAction?: boolean;
  showHeader?: boolean;
}

function booleanAttribute(value: any): boolean {
  if (isNull(value)) return true;
  else if (typeof value === 'boolean') return value;
  else if ([1, 0].includes(value)) return value === 1;
  else if (["1", "0"].includes(value)) return value === "1";
  else return undefined;
}

@Component({
  selector: 'ts-api-info',
  encapsulation: ViewEncapsulation.None,
  templateUrl: './api-info.component.html',
  styleUrl: './api-info.component.scss'
})
export class ApiInfoComponent implements OnInit, OnDestroy {
  @Input({ alias: 'uid' }) appUID: string;
  @Input({ alias: 'name' }) appName: string;
  @Input({ alias: 'api_id', transform: numberAttribute }) apiId: number;
  @Input({ transform: booleanAttribute }) divider: boolean = true;
  @Input({transform: booleanAttribute}) showHeader: boolean = true;
  @Input() lsApi: ApiInfo[] = [];

  form: FormGroup<any>;
  state: StateObject = {visibleBtnLoadApi:true, showAction: true};
  action: SaveAction = undefined;
  appApi: ApiInfo;

  private get isEditApi(): boolean {
    return this.action === 'copy_api' || this.action === 'edit_api';
  }

  get isEditTT(): boolean {
    return this.isEditApi ? false : notNull(this.get_apiItem);
  }

  get isAllowCopy(): boolean {
    return this.isEditApi ? false : this.get_apiItem?.allow_copy;
  }

  get actionIsCopy(): boolean {
    return this.action == 'copy_api';
  }

  get actionIsEdit(): boolean {
    return this.action == 'edit_api';
  }

  get formDirty(): boolean {
    return this.fgInfo.dirty || this.fgUser.dirty;
  }

  get fgInfo(): FormGroup {
    return this.form.get('api_info') as FormGroup;
  }

  get fgUser(): FormGroup<{ [K in keyof UserApi]: any }> {
    return this.form.get('user_api') as FormGroup;
  }

  get cApi(): FormControl<ApiInfo> {
    return this.form.get('api_item') as FormControl;
  }

  get get_apiItem(): ApiInfo {
    return this.cApi.getRawValue();
  }

  get visibleResetButton(): boolean {
    return this.isEditApi ? true : this.fgUser.dirty;
  }

  get visibleCheckButton(): boolean {
    return isNull(this.get_apiItem) ? false : (this.isEditApi ? isFalse(this.formDirty) : isFalse(this.fgUser.dirty));
  }

  get visibleSaveButton(): boolean {
    return this.isEditApi ? true : notNull(this.get_apiItem);
  }

  get headerLabel(): string {
    return this.showHeader ? 'Danh sách ứng dụng' : undefined;
  }

  constructor(private fb: FormsBuilder,
    private cfg: StorageService,
    private alert: Alert,
    private logger: LoggerService,
    private toast: ToastService,
    private modal: ModalService,
    private apiSrv: ApiInfoService,
    private dialogRef: DynamicDialogRef) {
  }

  ngOnInit() {
    this.lsApi = this.lsApi || [];

    this.createFormGroup();
    this.changeAction(null);

    const instance = this.modal.getInstance(this.dialogRef);
    if (instance && instance.data) {
      const { uid, name, api_id, lsApi, showAction, divider, showHeader } = <ApiInfoInput>instance.data;
      const hasLs = (lsApi?.length || 0) > 0;
      
      this.state.hasModal = true;
      this.state.visibleBtnLoadApi = hasLs ? false : [uid, name, api_id].some(i => notNull(i));
	  this.showHeader = false;
      if(notNull(showAction))  this.state.showAction = showAction;
      if(notNull(divider)) this.divider = divider;

      this.appUID = uid;
      this.appName = name;
      this.apiId = api_id;
      

      //
      this.lsApi = lsApi || [];
      if(hasLs && lsApi.length == 1)  {
        this.cApi.setValue(lsApi[0]);
        this.onSelectApi(lsApi[0]);
      }
    }

    // Lấy danh sách api
    if (this.lsApi && this.lsApi.length == 0) {
      this.clickLoadApiInfo();
    }

  }

  ngOnDestroy(): void {
  }

  /**
   * Lấy danh sách api
   */
  clickLoadApiInfo(): void {
    this.state.loadDs = true;
    const object = {uid: this.appUID, name: this.appName, api_id: this.apiId };
    this.apiSrv.search(object).pipe(delay(1000)).subscribe({
        error: err => {
          this.logger.error('loadApiInfo', err);
          this.state.loadDs = false;
        },
        next: page => {
          this.state.loadDs = false;
          this.lsApi = page.data || [];
          this.toast.success(this.cfg.i18n.loadApiOk);

          if (isFalse(this.isEditApi) && this.lsApi.length > 0) {
            this.cApi.patchValue(this.lsApi[0]);
            this.onSelectApi(this.lsApi[0]);
          }

        }
      });
  }

  /**
   * Click button edit_api
   */
  clickEditApi(): void {
    this.changeAction('edit_api');
    this.resetForm(this.get_apiItem);
  }

  /**
   * Click button copy_api
   */
  clickCopyApi(): void {
    this.changeAction('copy_api');
    const info = this.get_apiItem;
    this.resetForm({
      ...info,
      api_id: null, app_name: null,
      is_system: false, allow_copy: false,
      base_url: null, links: {},
      headers: {}, queries: {},
      user_api: UserApi.from({
        user_name: info?.user_api?.user_name,
        password: undefined,
        auto_login: true,
        allow_edit: false
      })
    });
  }

  /**
   * Click button reset_api
   */
  clickResetApi(): void {
    const allowReset = !(this.isEditApi && this.formDirty);
    const info = this.get_apiItem;
    if (allowReset) {
      this.resetForm(info);
      this.changeAction(null);
    } else {
      const ref = this.alert.warning({
        title: 'Cảnh báo !!',
        summary: 'Dữ liệu bạn đang chỉnh sửa. Bạn có muốn lấy lại thông tin trước khi thay đổi không ?',
        actions: [
          { label: 'Nhập lại', onClick: e => e.dynamicRef.close('ok') },
          { label: 'Bỏ qua', onClick: e => e.dynamicRef.close('cancel') }
        ]
      });

      ref.onClose.subscribe(act => {
        if ("ok" === act) {
          this.resetForm(info);
          this.changeAction(null);
        }
      });
    }

  }

  /**
   * Click button check_login
   */
  clickCheckLogin(): void {
    const info = this.get_apiItem;
    const loadingRef = this.toast.loading(this.cfg.i18n.awaitHandle);

    const app_name = info.app_name;
    this.apiSrv.checkLoginByAppName(app_name).subscribe({
      error: _ => {
        this.toast.close(loadingRef);
      },
      next: (cls: UserApi) => {
        info.set_user_api(cls);
        this.toast.close(loadingRef);
        this.toast.success(this.cfg.i18n.checkApiOk);
        this.resetForm(info);
      }
    });
  }

  /**
   * Click button menu_api
   */
  clickMenuApi(): void {
    const { app_name } = this.get_apiItem;
    const loadingRef = this.toast.loading(this.cfg.i18n.awaitHandle);
    this.apiSrv.updateMenuLink(app_name).subscribe({
      error: _ => {
        //this.asyncUpdateMenu = false;
        this.toast.close(loadingRef);
        console.error(`clickUpdateMenuLink: `, _);
      },
      next: links => {
        this.fgInfo.patchControl('links', JSON.stringify(links));
        this.toast.close(loadingRef);
        this.toast.success(this.cfg.i18n.updateMenuOk);
        //this.asyncUpdateMenu = false;
      }
    });
  }

  /**
   * Click button save_api
   */
  clickSaveApi(): void {
    if (this.form.invalid) {
      this.toast.warning(this.cfg.i18n.form_invalid)
      return;
    }

    const { api_item, api_info, user_api }: any = this.form.getRawValue();
    const action = isNull(this.action) ? 'edit_user' : this.action;
    const apiInfo: Partial<ApiInfo> = !this.isEditApi ? null : {
      ...api_info,
      headers: JSON.parse(api_info?.headers || '{}'),
      queries: JSON.parse(api_info?.queries || '{}'),
      links: JSON.parse(api_info?.links || '{}')
    };


    const saveDto: SaveDto = {
      action: action,
      source_id: api_item?.api_id,
      user_api: user_api,
      api_info: apiInfo
    };

    this.apiSrv.saveAll(saveDto).subscribe({
      error: _ => {
        //this.asyncSave = false;
        console.error(`onSaveUserApi: `, _);
      },
      next: data => {
        if (this.actionIsCopy) this.lsApi.push(data);
        else this.lsApi.find(l => l.api_id === data.api_id).update(data);
        this.toast.success(this.cfg.i18n.saveApiOk);
        this.changeAction(null);
        this.resetForm(data);

        console.log(this.formDirty, this.fgUser.dirty)

      }
    });
  }

  clickCloseModal(): void {
    this.dialogRef.close({data: this.appApi});
  }

  /**
   * Event change select api
   */
  onSelectApi(info: ApiInfo): void {
    this.appApi = info;

    if (isNull(info)) this.resetForm({});
    else if (notNull(info.user_api)) this.resetForm(info);
    else {
      this.apiSrv.loadUserByAppName(info.app_name).subscribe({
        error: _ => {
          console.error(`onSelectApi: `, _)
        },
        next: (user: UserApi) => {
          info.user_api = user;
          this.resetForm(info);
        }
      });
    }


  }

  private changeAction(action: SaveAction | null): void {
    this.action = action;

    //api_item
    notNull(action) ? this.cApi.disable() : this.cApi.enable();

    // fg_info
    const { app_uid, app_name } = this.fgInfo.controls;
    if (isNull(action)) this.fgInfo.disable();
    else {
      this.fgInfo.enable();

      // app_uid
      app_uid.disable();

      // app_name
      this.actionIsCopy ? app_name.enable() : app_name.disable();
    }


  }

  private resetForm(info?: Partial<ApiInfo>): void {
    info = this.fixApiInfoToForm({ ...info });
    this.fgInfo.reset({ ...info });

    // user_api
    const user = info?.user_api?.user_info;
    this.fgUser.reset({ 
      ...info?.user_api, 
      ts_name: user?.display_name, 
      ts_email: user?.email 
    });
  }

  private fixApiInfoToForm(info: any) {
    if (notNull(info.headers)) info.headers = JSON.stringify(info?.headers);
    if (notNull(info.queries)) info.queries = JSON.stringify(info?.queries);
    if (notNull(info.links)) info.links = JSON.stringify(info?.links);
    return info;
  }

  private createFormGroup(): void {

    this.form = this.fb.group({
      api_item: [null, Validators.required],
      api_info: this.fb.group({
        api_id: [null],
        title: [null, Validators.required],
        summary: [null],
        base_url: [null, Validators.required],
        app_uid: [{ value: null, disabled: true }, Validators.required],
        app_name: [null, Validators.required],
        login_path: [null, Validators.required],
        allow_copy: [false, Validators.required],
        headers: [null],
        queries: [null],
        links: [null]
      }),
      user_api: this.fb.group({
        user_name: [null, Validators.required],
        password: [null, Validators.required],
        allow_edit: [false],
        csrf_token: [{ value: null, disabled: true }],
        cookie_value: [{ value: null, disabled: true }],
        user_info: [{ value: null, disabled: true }],
        ts_name: [null], ts_email: [null],
        auto_login: [true]
      })
    });
  }


  static showDialog(modal: ModalService, data: ApiInfoInput): DynamicDialogRef<any> {
    return modal.open(ApiInfoComponent, {
      header: 'Cấu hình thông tin xác thực',
      width: '750px',
      data: data
    })

  }

}

export interface ApiInfoInput {
  uid?: string;
  name?: string;
  api_id?: number;
  lsApi?: ApiInfo[];
  showAction?: boolean; // 
  divider?: boolean;
  showHeader?: boolean;
}