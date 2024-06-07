import {Component, OnInit, ViewEncapsulation} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {ToastService} from "../../services/toast.message";
import {User} from "../../models/user";
import {ConfigService} from "../../services/app-config.service";
import {UserService} from "../../services/user.service";

@Component({
  selector: 'ts-user-info',
  templateUrl: './user-info.component.html',
  styleUrl: './user-info.component.scss',
  encapsulation: ViewEncapsulation.None,
  host: {
    'display': 'block'
  }
})
export class UserInfoComponent implements OnInit{
  formGroup: FormGroup;
  asyncSave: boolean = false;
  user: User;

  constructor(private fb: FormBuilder,
              private userSrv: UserService,
              private config: ConfigService,
              private toast: ToastService) {
  }

  ngOnInit() {
    this.user = this.config.get_user();

    this.formGroup = this.fb.group({
      room_code: [this.user?.room_code, Validators.required],
      user_code: [this.user?.user_code, Validators.required],
      email: [this.user?.email, Validators.required],
      name: [this.user?.name, Validators.required],
    });
  }

  onSave() {

    if(this.formGroup.invalid) {
      this.toast.warning({summary: 'Vui lòng nhập đầy đủ thông tin.'});
      return;
    }

    this.asyncSave = true;
    const info: User = this.formGroup.getRawValue();
    this.userSrv.updateById(this.user.user_id, info).subscribe({
      error: err => this.asyncSave = false,
      next: res => {
        this.asyncSave = false;
        this.toast.success({summary: 'Cập nhật thông tin thành công.'});
      }
    });

  }
}
