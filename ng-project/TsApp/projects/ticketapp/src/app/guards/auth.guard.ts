import {CanActivateFn, Router} from '@angular/router';
import {inject} from "@angular/core";
import { ConfigService } from '../services/config.service';
import { Objects } from '../utils/objects';

export const userGuard: CanActivateFn = (route, state) => {
  const isLogin = Objects.notNull(inject(ConfigService).get_authToken());
  return isLogin ? true : inject(Router).navigate(['/auth/signin']);
};

