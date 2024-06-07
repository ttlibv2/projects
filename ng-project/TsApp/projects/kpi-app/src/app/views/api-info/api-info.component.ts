import {Component, OnInit} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {UserService} from "../../services/user.service";
import {ConfigService} from "../../services/app-config.service";
import {ToastService} from "../../services/toast.message";
import {ApiInfo} from "../../models/api-info";
import {ApiInfoService} from "../../services/api-info.service";
import {Observable, of, switchMap} from "rxjs";

@Component({
  selector: 'ts-api-info',
  templateUrl: './api-info.component.html',
  styleUrl: './api-info.component.scss'
})
export class ApiInfoComponent implements OnInit{
  formGroup: FormGroup;
  asyncSave: boolean = false;
  infos: Observable<ApiInfo[]>;

  constructor(private fb: FormBuilder,
              private userSrv: UserService,
              private config: ConfigService,
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
      user_name: [null, Validators.required],
      secret: [null, Validators.required],
      csrf_token: [null, Validators.required],
      auto_login: [true]
    });

  }

  onSave() {
    this.loadData();
  }

  onSelectApi(value: ApiInfo) {
    this.formGroup.patchValue(value);
  }
}
