import { Injectable } from '@angular/core';
import {ClientService} from "./client.service";
import {concatMap, Observable, of, tap} from "rxjs";
import {Objects} from "../utils/objects";
import {AuthToken, SignUpDto} from "../models/user";
import {UserService} from "./user.service";
const {encodeBase64} = Objects;

@Injectable({providedIn: 'root'})
export class AuthService extends ClientService {

  private get user(): UserService {
    return this.inject.get(UserService);
  }

  signup(dto: SignUpDto): Observable<AuthToken> {
      const dtoNew = {...dto, password: encodeBase64(dto.password),
        re_password: encodeBase64(dto.re_password), signup_type: 'email'};

    return this.post(`/auth/signup`, dtoNew)
        .pipe(tap(res => this.config.set_authToken(res)));

  }

  signin(email: string, password: string): Observable<AuthToken> {
    const object = {
      username: email, sign_type: 'email',
      password: encodeBase64(password)
    };

    return this.post(`/auth/signin`, object).pipe(
        tap(res => this.config.set_authToken(res)),
        concatMap(() => this.user.getProfile()),
        concatMap(() => this.user.getConfig())
    );
  }

  isLogin(): boolean {
    return this.config.get_user().isLogin();
  }
}
