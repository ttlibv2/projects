import { ChangeDetectionStrategy, Component, inject, ViewEncapsulation } from '@angular/core';
import { BaseComponent } from 'primeng/basecomponent';
import { DashboardStyle } from './dashboard.style';
import { CommonModule } from '@angular/common';
import {Button, ButtonModule} from 'primeng/button';

@Component({
  selector: 'ts-dashboard',
  imports: [CommonModule, ButtonModule],
  templateUrl: './dashboard.page.html',
  styles: `ts-dashboard { display: block}`,
  encapsulation: ViewEncapsulation.None,
  changeDetection: ChangeDetectionStrategy.OnPush,
  providers: [DashboardStyle]
})
export class DashboardPage extends BaseComponent {
  _componentStyle = inject(DashboardStyle);
}
