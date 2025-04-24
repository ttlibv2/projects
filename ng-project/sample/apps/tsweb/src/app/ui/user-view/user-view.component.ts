import { ChangeDetectionStrategy, Component } from '@angular/core';

@Component({
  selector: 't-user-view',
  imports: [],
  template: `
    <p>
      user-view works!
    </p>
  `,
  styles: ``,
  changeDetection: ChangeDetectionStrategy.OnPush
})
export class UserViewComponent {

}
