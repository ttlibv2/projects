import {Component, Input, OnInit, ViewEncapsulation, booleanAttribute} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {AutoCompleteCompleteEvent} from "primeng/autocomplete";
import {Objects} from "../../../utils/objects";
import {ChkUser, RememberUser, User} from "../../../models/user";
import {AuthService} from "../../../services/auth.service";
import {Router} from "@angular/router";
import {ToastService} from "../../../services/toast.service";
import { ConfigService } from '../../../services/config.service';
import { LocalDbService } from '../../../services/local-db.service';
import { LoggerService } from '../../../logger/logger.service';
import { switchMap, tap } from 'rxjs';


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

  constructor(private fb: FormBuilder,
              private router: Router,
              private cfg: ConfigService,
              private auth: AuthService,
              private toast: ToastService) {  }

  ngOnInit() {
    this.cfg.hasLoginAndRedirect(this.lastUrl, () => this.initialize());
  }

  initialize() {
    this.cache = this.cfg.get_rememberUser();
   
    this.asyncView = true;

    this.cacheUsers = Object.keys(this.cache?.users ?? {});
    const info = this.cache?.users[this.cache?.currentUser];
    this.signinForm = this.fb.group({
      username: [info?.username, [Validators.required, Validators.email]],
      password: [info?.password, Validators.required],
      url_dev: [info?.url_dev ?? this.cfg.get_baseUrl()],
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
    const obj: ChkUser = this.signinForm.getRawValue();

    if (this.isDev && Objects.notBlank(obj.url_dev)) {
      this.cfg.set_baseUrl(obj.url_dev);
    }

    this.asyncLogin = true;

    this.auth.signin(obj).subscribe({
      error: err => {this.asyncLogin = false; console.log(err)},
      next: (user: User) => {
        this.asyncLogin = false;
        this.toast.success({summary: 'Đăng nhập thành công.'});

        if(user.required_update === true) this.router.navigate(['/user-info']);
        else  this.router.navigate([this.lastUrl ?? '/']);

      }
    })


  }
}
