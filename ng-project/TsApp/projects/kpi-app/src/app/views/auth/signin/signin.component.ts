import {Component, inject, Input, OnInit, signal, ViewEncapsulation} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {AutoCompleteCompleteEvent} from "primeng/autocomplete";
import {Objects} from "../../../utils/objects";
import {ConfigService} from "../../../services/app-config.service";
import {ChkUser, RememberUser} from "../../../models/user";
import {AuthService} from "../../../services/auth.service";
import {Toast} from "primeng/toast";
import {MessageService} from "primeng/api";
import {Messages} from "primeng/messages";
import {Router} from "@angular/router";
import {tap} from "rxjs";
import {ToastService} from "../../../services/toast.message";

const {notBlank} = Objects;

@Component({
  selector: 'ts-signin',
  templateUrl: './signin.component.html',
  styleUrl: './signin.component.scss',
  encapsulation: ViewEncapsulation.None
})
export class SigninComponent implements OnInit {
  @Input() dev: boolean = false;
  @Input() lastUrl: string;

  signinForm: FormGroup;
  cache: RememberUser;
  searchUser: string[] = [];
  asyncLogin: boolean = false;
  asyncView: boolean = false;

  cacheUsers: string[] = [];

  constructor(private fb: FormBuilder,
              private router: Router,
              private config: ConfigService,
              private auth: AuthService,
              private toast: ToastService) {
  }

  ngOnInit() {

    if (this.auth.isLogin()) {
      this.router.navigate([this.lastUrl ?? '/']);
      return;
    }

    this.asyncView = true;
    this.cache = this.config.get_remember();
    this.cacheUsers = Object.keys(this.cache?.users ?? {});
    const info = this.cache?.users[this.cache?.currentUser];
    this.signinForm = this.fb.group({
      username: [info?.username, [Validators.required, Validators.email]],
      password: [info?.password, Validators.required],
      remember: [!!info],
      url_dev: [info?.url_dev ?? 'http://localhost:8888']
    });
  }

  get isDevBool(): boolean {
    return Objects.booleanValue(this.dev);
  }

  onSearchUser(event: AutoCompleteCompleteEvent) {
    this.searchUser = this.cacheUsers
      .filter(user => user.includes(event.query));
  }

  selectUser(value: string) {
    let user = this.cache?.users[value];
    this.signinForm.patchValue(user);
  }

  onSignIn() {
    const obj: ChkUser = this.signinForm.getRawValue();

    if (this.isDevBool && notBlank(obj.url_dev)) {
      this.config.set_baseUrl(obj.url_dev);
    }

    this.asyncLogin = true;

    this.auth.signin(obj.username, obj.password).subscribe({
      error: err => this.asyncLogin = false,
      next: res => {
        this.asyncLogin = false;
        this.config.set_remember(obj);
        this.toast.success({summary: 'Đăng nhập thành công.'});
        this.router.navigate([this.lastUrl ?? '/']);
      }
    })


  }
}
