import {Component, Input, OnInit, ViewEncapsulation} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {SocialLink} from "../../../models/common";
import {ToastService} from "../../../services/toast.message";
import {AuthService} from "../../../services/auth.service";
import {SignUpDto} from "../../../models/user";
import {ActivatedRoute, Route, Router} from "@angular/router";
import {Objects} from "../../../utils/objects";

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

  socials: SocialLink[] = [
    {label: 'Google', icon: 'fa-brands fa-google', link: '/auth/google'},
    {label: 'Facebook', icon: 'fa-brands fa-facebook', link: '/auth/facebook'},
    {label: 'Instagram', icon: 'fa-brands fa-instagram', link: '/auth/instagram'},
  ];

  constructor(private active: ActivatedRoute,
              private fb:FormBuilder,
              private route: Router,
              private toast:ToastService,
              private auth: AuthService) {
  }

  ngOnInit() {

    if(this.auth.isLogin()) {
      this.route.navigate([this.lastUrl ?? '/']);
      return;
    }

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
      dob: [null, Validators.required]
    });
  }

  onSignUp() {
    if(this.formSignup.invalid) {
      this.toast.warning({summary: 'Vui lòng nhập đầy đủ thông tin'});
      return;
    }

    const dto:SignUpDto = this.formSignup.getRawValue();
    if(dto.password !== dto.re_password) {
      this.toast.warning({summary: 'Mật khẩu không giống nhau.'});
      return;
    }

    this.asyncSignup = true;
    this.auth.signup(dto).subscribe({
      next: res => {
        this.asyncSignup = false;
        this.toast.success({summary: 'Đăng ký thành công.'});
        if(Objects.notBlank(this.lastUrl)) this.route.navigate([this.lastUrl]);
        else this.route.navigate(['/']);
      },
      error: err => this.asyncSignup = false,
      complete: () => this.asyncSignup = false
    });

  }
}
