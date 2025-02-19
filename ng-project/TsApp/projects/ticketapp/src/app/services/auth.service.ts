import { Injectable } from "@angular/core";
import { ClientService } from "./client.service";
import { concatMap, EMPTY, map, Observable, of, switchMap, tap } from "rxjs";
import { Objects } from "ts-ui/helper";
import { AuthToken, ChkUser, SignUpDto } from "../models/user";
import { UserService } from "./user.service";
const { encodeBase64 } = Objects;

@Injectable({ providedIn: "root" })
export class AuthService extends ClientService {
  private get user(): UserService {
    return this.inject2.get(UserService);
  }

  signup(dto: SignUpDto): Observable<AuthToken> {
    const dtoNew = {
      ...dto,
      password: encodeBase64(dto.password),
      re_password: encodeBase64(dto.re_password),
      signup_type: "email",
    };

    const responseObs = this.post(`/auth/signup`, dtoNew);
    return this.saveAndGetInfo(responseObs, {
      password: dto.password,
      username: dto.email,
      remember: true,
      url_dev: this.storage.baseUrl,
    });
  }

  signin(user: ChkUser): Observable<any> {
    const responseObs = this.post(`/auth/signin`, {
      username: user.username,
      sign_type: "email",
      password: encodeBase64(user.password),
    });

    return this.saveAndGetInfo(responseObs, user);
  }

  signout(): Observable<any> {
    this.storage.set_loginToken(undefined);
    this.storage.set_loginUser(undefined);
    return this.post('/auth/signout', {});
  }

  private saveAndGetInfo(
    responseToken: Observable<AuthToken>,
    user: ChkUser
  ): Observable<any> {
    return responseToken.pipe(
      concatMap((token) => this.storage.set_loginToken(token)),
      concatMap((_) => this.storage.set_rememberUser(user)),
      concatMap(() =>
        this.user.getConfig().pipe(switchMap(u => this.storage.set_loginUser(u)))
      )
    );
  }
}