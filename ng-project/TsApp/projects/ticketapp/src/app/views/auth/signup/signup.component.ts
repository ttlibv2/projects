import {Component, Input, OnInit, ViewEncapsulation} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {SocialLink} from "../../../models/common";
import {ToastService} from "../../../services/toast.service";
import {AuthService} from "../../../services/auth.service";
import {SignUpDto} from "../../../models/user";
import {ActivatedRoute, Route, Router} from "@angular/router";
import { Objects } from 'ts-helper';
import { ConfigService } from '../../../services/config.service';
import { I18N_KEY } from '../../../models/constant';

@Component({
  selector: 'ts-signup',
  templateUrl: './signup.component.html',
  styleUrl: './signup.component.scss',
  encapsulation:ViewEncapsulation.None
})
export class SignupComponent implements OnInit{
  @Input() lastUrl: string = '/';
  formSignup: FormGroup;
  asyncSignup: boolean = false;
  asyncView: boolean = false;
  i18n = I18N_KEY;

  socials: SocialLink[] = [
    {label: 'Google', icon: 'fa-brands fa-google', link: '/auth/google'},
    {label: 'Facebook', icon: 'fa-brands fa-facebook', link: '/auth/facebook'},
    {label: 'Instagram', icon: 'fa-brands fa-instagram', link: '/auth/instagram'},
  ];

  constructor(private active: ActivatedRoute,
              private fb:FormBuilder,
              private route: Router,
              private cfg: ConfigService,
              private toast:ToastService,
              private auth: AuthService) {
  }

  ngOnInit() {
    this.cfg.hasLoginAndRedirect(this.lastUrl, () => this.initialize());
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
      this.toast.warning({summary: this.cfg.i18n.form_invalid});
      return;
    }

    const dto:SignUpDto = this.formSignup.getRawValue();
    if(dto.password !== dto.re_password) {
      this.toast.warning({summary: this.cfg.i18n.repeatPassInvalid});
      return;
    }

    this.asyncSignup = true;
    this.auth.signup(dto).subscribe({
      next: res => {
        this.asyncSignup = false;
        this.toast.success({summary: this.cfg.i18n.signupOk});
        if(Objects.notBlank(this.lastUrl)) this.route.navigate([this.lastUrl]);
        else this.route.navigate(['/']);
      },
      error: err => this.asyncSignup = false,
      complete: () => this.asyncSignup = false
    });

  }
}