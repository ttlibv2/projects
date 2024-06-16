import { Injectable, OnInit } from "@angular/core";
import { EMPTY, Observable, map, tap } from "rxjs";
import { AuthToken, ChkUser, RememberUser, User } from "../models/user";
import { Translation } from "../models/translation";
import { NavigationExtras, Router } from "@angular/router";
import { AppConfig, IAppConfig } from "../models/app-config";
import { LoggerService } from "../logger/logger.service";
import { Objects } from "../utils/objects";

const DEFAULT_CONFIG: IAppConfig = {
  baseUrl: 'http://localhost:8888',
  rememberUser: new RememberUser()
};

@Injectable({ providedIn: 'root' })
export class ConfigService  {
  private _i18n: Translation = new Translation();
  private config: AppConfig = new AppConfig().update(DEFAULT_CONFIG);


  constructor(//private db: LocalDbService,
    private logger: LoggerService,
    private router: Router) { }

    private save(code: string, data: any, selfUpdate: boolean = true): Observable<any> {
      //const observable = this.db.tbAppCfg.set(code, data);
     // return !selfUpdate ? observable : observable.pipe(
     //   tap(_ => this.config[code] = data)
     // );
      return EMPTY;
    }

  get i18n(): Translation {
    return this._i18n;
  }

  get_baseUrl(): string {
    return this.config.baseUrl;
  }

  set_baseUrl(url: string): Observable<string> {
    return this.save('baseUrl', url);
  }

  get_authToken(): AuthToken {
    return this.config.authToken;
  }

  set_authToken(token: AuthToken): Observable<AuthToken> {
    return this.save('authToken', token);
  }

  get_rememberUser(): RememberUser {
    return this.config.rememberUser ?? new RememberUser();
  }

  set_rememberUser(user: ChkUser): Observable<RememberUser> {
    const object = this.get_rememberUser();
    return this.save('rememberUser', object.set(user));
  }


















  get_user(): Observable<User> {
    //return this.db.user.read();
    return EMPTY;
  }


 


  navigateUrl(commands: any[], extras?: NavigationExtras): Promise<boolean> {
    return this.router.navigate(commands, extras);
  }

  hasLoginAndRedirect(redirectUrl: string, initialize: () => void) {
    if(Objects.notNull(this.get_authToken())) this.router.navigate([redirectUrl ?? '/']);
    else initialize();
  }

  
  read(): Observable<AppConfig> {
    // return this.db.tbAppCfg.read().pipe(
    //   tap(cfg => this.config = cfg.updateDefault(DEFAULT_CONFIG))
    // );
    return EMPTY;
  }

}