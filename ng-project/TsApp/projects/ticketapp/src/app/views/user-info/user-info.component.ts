import { Component, OnInit, ViewEncapsulation } from '@angular/core';
import { FormControl, Validators } from "@angular/forms";
import { ToastService } from "ts-ui/toast";
import { FormGroup, FormsBuilder } from "ts-ui/forms";
import { Objects } from "ts-ui/helper";
import { UserService } from "../../services/user.service";
import { User } from "../../models/user";
import { ApiInfo } from "../../models/api-info";
import { ApiInfoService } from "../../services/api-info.service";
import { ModalService } from 'ts-ui/modal';
import { ApiInfoComponent } from '../api-info/api-info.component';
import { delay } from 'rxjs';

const { notNull, isNull } = Objects;

interface State {
  save?: boolean;
  lsDs?: boolean;
  loadUser?: boolean;
}

@Component({
  selector: 'ts-user-info',
  templateUrl: './user-info.component.html',
  styleUrl: './user-info.component.scss',
  encapsulation: ViewEncapsulation.None,
})
export class UserInfoComponent implements OnInit {
  form: FormGroup;
  state: State = {};
  lsApi: ApiInfo[] = [];
  apiApp: ApiInfo;


  get cApp(): FormControl<ApiInfo> {
    return this.form?.get('ls_app') as any;
  }

  constructor(private fb: FormsBuilder,
    private userSrv: UserService,
    private apiSrv: ApiInfoService,
    private modal: ModalService,
    private toast: ToastService) {
  }

  ngOnInit() {
    this.createFormGroup();
    this.clickLoadApi({ loadUser: true });
  }

  /**
   * Click button change_app
   * */
  changeApp(info: ApiInfo): void {
    this.apiApp = info;
    if (notNull(info)) {
      this.pathTsApp(info);

      if (isNull(info?.user_api?.user_info)) {
        const appName = info.app_name;
        this.apiSrv.loadUserByAppName(appName).subscribe({
          error: msg => {
            console.error(`changeApp - load UserApi: `, msg);
            this.toast.error(`Đã xảy ra lỗi lấy thông tin tài khoản.`);
            this.state.lsDs = false;
          },
          next: ua => {

            this.state.lsDs = false;

            if (notNull(ua.user_info)) {
              info.set_user_api(ua);
              this.pathTsApp(info);
            }

            // show api_info
            else this.showDialogViewApiInfo(info);
          }
        });
      }
    }
  }


  /**
   * Click button load_app
   * */
  clickLoadApi(options?: { loadUser: boolean }): void {
    this.state.lsDs = true;
    this.cApp.setValue(null);
    this.apiSrv.findByAppUID('TSApp', true).pipe(delay(1000)).subscribe({
      error: msg => {
        console.error(`clickLoadApi: `, msg);
        this.state.lsDs = false;
        this.toast.error(`Lấy danh sách ứng dụng bị lỗi`);
      },
      next: ls => {
        this.state.lsDs = false;
        this.lsApi = ls || [];

        if (options?.loadUser) {
          this.loadUserInfo();
        }


      }
    })
  }

  loadUserInfo() {
    this.state.loadUser = true;
    this.userSrv.getProfile().subscribe({
      error: msg => {
        console.error(`loadUserInfo: `, msg);
        this.state.loadUser = false;
        this.toast.error(`Lấy thông tin người dùng bị lỗi`);
      },
      next: user => {
        this.state.loadUser = false;
        this.form.patchValue(user);

        if (notNull(user.ts_app) && this.lsApi?.length > 0) {
          const api = this.lsApi.find(l => l.api_id == user.ts_app);
          this.form.setValue(api);
        }
      }
    });
  }


  /**
   * Click button save_user
   * */
  clickSave() {

    if (this.form.invalid) {
      this.toast.warning('Vui lòng nhập đầy đủ thông tin.');
      return;
    }

    this.state.save = true;
    const info: any = this.form.getRawValue();

    // remove fields
    ['ls_app'].forEach(k => delete info[k]);

    this.userSrv.updateProfile(info).subscribe({
      error: err => {
        this.state.save = false;
        console.error(`clickSaveUser: `, err);
        this.toast.error(`Đã xảy ra lỗi cập nhật thông tin tài khoản.`);
      },
      next: res => {
        this.state.save = false;
        this.toast.success('Cập nhật thông tin thành công.');
      }
    });

  }

  private pathTsApp(info: ApiInfo) {
    const { name, email, id } = info?.user_api?.user_info || {};
    this.form.patchValue({
      ts_app: info?.api_id, ts_name: name,
      ts_email: email, ts_id: id
    });
  }

  private createFormGroup() {
    this.form = this.fb.group({
      room_code: [null, Validators.required],
      user_code: [null, Validators.required],
      ts_name: [null, Validators.required],
      ls_app: [null],
      ts_email: [null, Validators.required],
      ts_id: [null, Validators.required],
      ts_app: [null, Validators.required]
    });
  }

  private showDialogViewApiInfo(info: ApiInfo) {
    const data = { lsApi: [info], api_id: info.api_id, showAction: false, divider: false, showHeader: false };
    const ref = ApiInfoComponent.showDialog(this.modal, data);
    ref.onClose.subscribe(res => {
      if (res?.data) {
        info.update(res.data);
        this.pathTsApp(info);
      }
    });
  }

}