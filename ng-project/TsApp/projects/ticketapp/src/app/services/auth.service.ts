import { Injectable } from '@angular/core';
import { ClientService } from "./client.service";
import { concatMap, flatMap, forkJoin, map, mergeMap, Observable, of, switchMap, tap } from "rxjs";
import { Objects } from "../utils/objects";
import { AuthToken, ChkUser, SignUpDto } from "../models/user";
import { UserService } from "./user.service";
const { encodeBase64 } = Objects;

@Injectable({ providedIn: 'root' })
export class AuthService extends ClientService {

  private get user(): UserService {
    return this.inject.get(UserService);
  }

  signup(dto: SignUpDto): Observable<AuthToken> {
    const dtoNew = {
      ...dto, password: encodeBase64(dto.password),
      re_password: encodeBase64(dto.re_password), signup_type: 'email'
    };

    return this.post(`/auth/signup`, dtoNew)
      .pipe(tap(res => this.db.user.saveToken(res.user_id, res)));

  }

  signin(user: ChkUser): Observable<any> {
    const object = {
      username: user.username, sign_type: 'email',
      password: encodeBase64(user.password)
    };

    return this.post(`/auth/signin`, object).pipe(
      concatMap(token => this.config.set_authToken(token)),
      concatMap(_ => this.config.set_rememberUser(user)),
      concatMap(() => this.user.getProfile())
    );
  }

}
