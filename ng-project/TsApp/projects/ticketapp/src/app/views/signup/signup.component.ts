import {Component, Input, OnChanges, OnInit, SimpleChanges, ViewEncapsulation} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {ALL_SOCIAL, SocialLink} from "../../models/common";
import {ToastService} from "ts-ui/toast";
import {AuthService} from "../../services/auth.service";
import {SignUpDto} from "../../models/user";
import {ActivatedRoute, Router} from "@angular/router";
import { Objects } from 'ts-ui/helper';
import { I18N_KEY } from '../../models/constant';
import {StorageService} from "../../services/storage.service";
import {Urls} from "../../constant";

@Component({
  selector: 'ts-signup',
  templateUrl: './signup.component.html',
  styleUrl: './signup.component.scss',
  encapsulation:ViewEncapsulation.None
})
export class SignupComponent implements OnInit, OnChanges {
  @Input() lastUrl: string = '/';
  @Input() termsUrl: string = '/terms-url';
  @Input() policyUrl: string = '/policy';

  formSignup: FormGroup;
  asyncSignup: boolean = false;
  asyncView: boolean = false;
  i18n = I18N_KEY;

  socials: SocialLink[] = ALL_SOCIAL.map(s => ({
    ...s, responsive: {span: 6, sm: 3}
  }));

  constructor(private active: ActivatedRoute,
              private fb:FormBuilder,
              private router: Router,
              private cfg: StorageService,
              private toast:ToastService,
              private auth: AuthService) {
  }

  ngOnChanges(changes: SimpleChanges): void {
    const { termsUrl, policyUrl } = changes;
    if (termsUrl) this.termsUrl = this.termsUrl ?? '/terms-url';
    if (policyUrl) this.policyUrl = this.policyUrl ?? '/policy';
  }

  ngOnInit() {
    if(this.cfg.isLogin) this.router.navigate([this.lastUrl??'/']).then();
    else this.initialize();
  }
  
  initialize() {
    this.asyncView = true;
    this.formSignup = this.fb.group({
      email: [null, [Validators.required, Validators.email]],
      phone: [null, [Validators.required]],
      password: [null, Validators.required],
      re_password: [null, Validators.required],
      first_name: [null, Validators.required],
      last_name: [null, Validators.required],
      bio: [null, Validators.max(300)],
      allow_term: [null, Validators.required],
      dob: [null, Validators.required],
      gender: [null]
    });
  }

  onSignUp() {
    if(this.formSignup.invalid) {
      this.toast.warning( this.cfg.i18n.form_invalid);
      return;
    }

    const dto:SignUpDto = this.formSignup.getRawValue();
    if(dto.password !== dto.re_password) {
      this.toast.warning(this.cfg.i18n.repeatPassInvalid);
      return;
    }

    this.asyncSignup = true;
    this.auth.signup(dto).subscribe({
      next: _ => {
        this.asyncSignup = false;
        this.toast.success(this.cfg.i18n.signupOk);
        if(Objects.notBlank(this.lastUrl)) this.router.navigate([this.lastUrl]).then();
        else this.router.navigate(['/']).then();
      },
      error: _ => {
        this.asyncSignup = false;
        console.log(_);
      },
      complete: () => this.asyncSignup = false
    });

  }

  clickSignin():void {
    this.router.navigateByUrl(Urls.signinUrl).then();
  }

  dobMaxDate(): Date {
    const now = new Date();
    now.setFullYear(now.getFullYear() - 15);
    return now;
  }
}