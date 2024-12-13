import { Component, Input } from '@angular/core';
import { MenuItem } from 'primeng/api';

@Component({
  selector: 'ts-app-breadcrumb',
  templateUrl: './app-breadcrumb.component.html'
})
export class AppBreadcrumbComponent {

  @Input()
  model: MenuItem[] = [
    { label: 'Electronics' }, 
    { label: 'Computer' }, 
    { label: 'Accessories' }, 
    { label: 'Keyboard' }, 
    { label: 'Wireless' }
  ];

  @Input()
  home: MenuItem = { 
    icon: 'pi pi-home', routerLink: '/' 
  };

  
}
