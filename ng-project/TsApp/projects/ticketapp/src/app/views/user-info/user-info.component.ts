import {Component, OnInit, ViewEncapsulation} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {ToastService} from "ts-ui/toast";
import {UserService} from "../../services/user.service";
import {StorageService} from "../../services/storage.service";
import {User} from "../../models/user";

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
              private config: StorageService,
              private toast: ToastService) {
  }

  ngOnInit() {
    

    this.formGroup = this.fb.group({
      room_code: [null, Validators.required],
      user_code: [null, Validators.required],
      full_name: [null, Validators.required]
    });

    this.loadUserInfo();
  }

  private loadUserInfo() {
    this.userSrv.getConfig().subscribe({
      next: res => {
        this.user = res;
        this.formGroup.patchValue(res);
      }
    })
  }

  onSave() {

    if(this.formGroup.invalid) {
      this.toast.warning('Vui lòng nhập đầy đủ thông tin.');
      return;
    }

    this.asyncSave = true;
    const info: User = this.formGroup.getRawValue();
    this.userSrv.updateById(this.user.user_id, info).subscribe({
      error: err => this.asyncSave = false,
      next: res => {
        this.asyncSave = false;
        this.toast.success('Cập nhật thông tin thành công.');
      }
    });

  }
}