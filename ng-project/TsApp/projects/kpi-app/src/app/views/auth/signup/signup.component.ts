import {Component, OnInit, ViewEncapsulation} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {SocialLink} from "../../../models/common";

@Component({
  selector: 'ts-signup',
  templateUrl: './signup.component.html',
  styleUrl: './signup.component.scss',
  encapsulation:ViewEncapsulation.None
})
export class SignupComponent implements OnInit{
  formSignup: FormGroup;

  socials: SocialLink[] = [
    {label: 'Google', icon: 'fa-brands fa-google', link: '/auth/google'},
    {label: 'Facebook', icon: 'fa-brands fa-facebook', link: '/auth/facebook'},
    {label: 'Instagram', icon: 'fa-brands fa-instagram', link: '/auth/instagram'},
  ];

  constructor(private fb:FormBuilder) {
  }

  ngOnInit() {
    this.formSignup = this.fb.group({
      email: [null, Validators.email],
      phone: [null, Validators.max(20)],
      password: [null, Validators.required],
      re_password: [null, Validators.required],
      first_name: [null, Validators.required],
      last_name: [null, Validators.required],
      bio: [null, Validators.max(300)],
      dob: null
    });
  }

  onSignUp() {

  }
}
