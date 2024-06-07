import {CanActivateFn, Router} from '@angular/router';
import {inject} from "@angular/core";
import {ConfigService} from "../services/app-config.service";
import {of} from "rxjs";

export const userGuard: CanActivateFn = (route, state) => {
  const auth = inject(ConfigService);
  if(auth.get_user().isLogin()) return of(true);
  else return inject(Router).createUrlTree(['/auth/signin']);

};
