import { ChangeDetectionStrategy, Component, inject, ViewEncapsulation } from '@angular/core';
import { CommonModule } from '@angular/common';
import { BaseComponent } from 'primeng/basecomponent';
import { UserStyle } from './user.style';

@Component({
  selector: 'ts-user',
  templateUrl: './user.page.html',
  styles: 'ts-user{display: block;}',
  providers: [UserStyle],
  imports: [ CommonModule],
  encapsulation: ViewEncapsulation.None,
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class UserPage extends BaseComponent {
  _componentStyle = inject(UserStyle);
}