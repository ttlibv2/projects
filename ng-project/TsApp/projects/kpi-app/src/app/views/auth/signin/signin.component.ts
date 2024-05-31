import {Component, Input, OnInit, signal, ViewEncapsulation} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {AutoCompleteCompleteEvent} from "primeng/autocomplete";
import {Objects} from "../../../utils/objects";
import {StorageService} from "../../../services/storage.service";
import {AppConfig} from "../../../models/app-config";

@Component({
  selector: 'ts-signin',
  templateUrl: './signin.component.html',
  styleUrl: './signin.component.scss',
  encapsulation: ViewEncapsulation.None
})
export class SigninComponent implements OnInit {
  @Input() dev: boolean = false;

  private appCfg = signal<AppConfig>(new AppConfig());


  signinForm: FormGroup;
  listSearchUser: string[] = [];

  constructor(private fb: FormBuilder,
              private storage: StorageService) {
  }

  ngOnInit() {





    this.signinForm = this.fb.group({
      username: [null, Validators.required],
      password: [null, Validators.required],
      rememberUser: [true],
      url_dev: [null]
    });
  }

  get isDevBool(): boolean {
    return Objects.booleanValue(this.dev);
  }

  onSearchUser(event: AutoCompleteCompleteEvent) {
    this.listSearchUser = [];
  }

  onSignIn() {}



}
