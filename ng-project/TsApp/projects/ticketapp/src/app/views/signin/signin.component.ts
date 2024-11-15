import {Component, Input, OnInit, ViewEncapsulation, booleanAttribute} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {AutoCompleteCompleteEvent} from "primeng/autocomplete";
import { Objects } from 'ts-ui/helper';
import {ChkUser, RememberUser, User} from "../../models/user";
import {AuthService} from "../../services/auth.service";
import {Router} from "@angular/router";
import {ToastService} from "ts-ui/toast";
import {TranslateService} from "@ngx-translate/core";
import {StorageService} from "../../services/storage.service";
import {HOME_PAGE, routerUrl} from "../../constant";


@Component({
  selector: 'ts-signin',
  templateUrl: './signin.component.html',
  styleUrl: './signin.component.scss',
  encapsulation: ViewEncapsulation.None
})
export class SigninComponent implements OnInit {

  @Input({transform: booleanAttribute, alias: 'dev'}) 
  isDev: boolean = false;

  @Input() lastUrl: string;

  signinForm: FormGroup;
  cache: RememberUser;
  searchUser: string[] = [];
  asyncLogin: boolean = false;
  asyncView: boolean = false;

  cacheUsers: string[] = [];

  get signupUrl(): string {
    return routerUrl.signupUrl;
  }

  constructor(private fb: FormBuilder,
              private translate: TranslateService,
              private router: Router,
              private cfg: StorageService,
              private auth: AuthService,
              private toast: ToastService) {  }

  ngOnInit() {
    if (this.cfg.isLogin) this.router.navigate([this.lastUrl ?? '/']).then();
    else this.initialize();
  }

  initialize() {
    this.cache = this.cfg.rememberUser;
    this.asyncView = true;

    this.cacheUsers = Object.keys(this.cache?.users ?? {});
    const info = this.cache?.users[this.cache?.currentUser] || {};
    info.url_dev = 'https://tsapi.konect24.ts24.com.vn';

    this.signinForm = this.fb.group({
      username: [info?.username, [Validators.required, Validators.email]],
      password: [info?.password, Validators.required],
      url_dev: [info?.url_dev ?? this.cfg.baseUrl],
      remember: [info?.remember]
    });
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
    if(this.signinForm.invalid) {
      this.toast.error( this.cfg.i18n.form_invalid)
      return;
    }

    const obj: ChkUser = this.signinForm.getRawValue();
    if (this.isDev && Objects.notBlank(obj.url_dev)) {
      this.cfg.set_baseUrl(obj.url_dev).subscribe({
        error: obj => console.error(`update baseUrl error -> ${obj}`),
        next: _ => this.loginImpl(obj)
      });
    }
    else {
      this.loginImpl(obj);
    }
  }

  private loginImpl(obj: ChkUser): void {
    this.asyncLogin = true;
    this.auth.signin(obj).subscribe({
      error: err => {
        //console.log(err);
        this.asyncLogin = false;
        //this.toast.error('error <=> console');
      },
      next: (user: User) => {
        this.asyncLogin = false;
        this.toast.success('Đăng nhập thành công.');

        if (user.required_update === true) this.router.navigate(['/user-info']);
        else this.router.navigate([this.lastUrl ?? HOME_PAGE]);

      }
    })
  }
}