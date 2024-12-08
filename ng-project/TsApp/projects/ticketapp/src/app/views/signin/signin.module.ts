import {NgModule} from '@angular/core';
import {CommonModule} from '@angular/common';
import {SigninComponent} from './signin.component';
import {FormsModule, ReactiveFormsModule} from "@angular/forms";
import {SigninRouting} from "./signin.routing";
import {AutoCompleteModule} from "primeng/autocomplete";
import {CheckboxModule} from "primeng/checkbox";
import {ButtonModule} from "primeng/button";
import {CardModule} from "ts-ui/card";
import {MessageService} from "primeng/api";
import {PasswordModule} from "primeng/password";
import {InputGroupModule} from 'ts-ui/input-group';
import {ImageModule} from "primeng/image";
import {FormField} from "ts-ui/forms";
import {Divider} from "ts-ui/divider";
import { ColDirective } from 'ts-ui/common';
import { TranslatePipe } from '@ngx-translate/core';
import { SocialItem } from '../shared/social-item';
import {Ribbon} from "ts-ui/ribbon";


@NgModule({
    declarations: [
        SigninComponent
    ],
    imports: [
        CommonModule,
        FormsModule,
        ReactiveFormsModule,
        TranslatePipe,
        SigninRouting,
        ImageModule,
        FormField,
        ColDirective,
        InputGroupModule,
        AutoCompleteModule,
        CheckboxModule,
        ButtonModule,
        CardModule,
        PasswordModule,
        Divider,
        SocialItem,
        Ribbon
    ],
    providers: [
        MessageService
    ]
})
export class SigninModule {
}
