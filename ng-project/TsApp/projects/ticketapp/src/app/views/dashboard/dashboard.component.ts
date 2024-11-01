import { Component } from '@angular/core';
import { ViewEncapsulation } from '@angular/core';

@Component({
  selector: 'ts-dashboard',
  templateUrl: './dashboard.component.html',
  styleUrl: './dashboard.component.scss',
  encapsulation: ViewEncapsulation.None
})
export class DashboardComponent {
  prefix: any[] = [
    {
      name: 'pi pi-user',
      onClick: () => console.log('click')
    }
  ];

  suffix: any[] = [
    {
      name: 'pi pi-times',
      onClick: () => console.log('click')
    }
  ];
}

