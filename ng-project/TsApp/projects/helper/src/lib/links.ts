import {NavigationExtras, Router} from "@angular/router";

export class Links {

  static navigateUrl(router: Router, commands: any[], extras?: NavigationExtras): Promise<boolean> {
    return router.navigate(commands, extras);
  }

  static hasLoginAndRedirect(router: Router, isLogin: boolean, redirectUrl: string, initialize: () => void) {
    if(isLogin) router.navigate([redirectUrl ?? '/']).then();
    else initialize();
  }
}