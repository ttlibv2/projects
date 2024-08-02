import { Component } from '@angular/core';
import { NavigationEnd, Route, Router, RoutesRecognized } from '@angular/router';
import { filter, pairwise } from 'rxjs';

@Component({
  selector: 'ts-error',
  templateUrl: './error.component.html',
  styleUrl: './error.component.scss'
})
export class ErrorComponent {

  constructor(private router: Router) {
    this.router.events
    .pipe(filter((evt: any) => evt instanceof RoutesRecognized), pairwise())
    .subscribe((events: RoutesRecognized[]) => {
      console.log('previous url', events[0].urlAfterRedirects);
      console.log('current url', events[1].urlAfterRedirects);
    });
  }
}
