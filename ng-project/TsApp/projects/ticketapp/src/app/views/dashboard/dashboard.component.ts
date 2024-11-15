import { Component, ContentChild, inject, OnInit, TemplateRef, ViewChild } from '@angular/core';
import { ViewEncapsulation } from '@angular/core';
import { PrimeTemplate } from 'primeng/api';
import { OverlayPanel } from 'primeng/overlaypanel';
import { Color } from 'ts-ui/color-picker';
import { FormsBuilder } from 'ts-ui/forms';

@Component({
  selector: 'ts-dashboard',
  templateUrl: './dashboard.component.html',
  styleUrl: './dashboard.component.scss',
  encapsulation: ViewEncapsulation.None
})
export class DashboardComponent implements OnInit {
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

  members = [
    { name: 'Amy Elsner', image: 'amyelsner.png', email: 'amy@email.com', role: 'Owner' },
    { name: 'Bernardo Dominic', image: 'bernardodominic.png', email: 'bernardo@email.com', role: 'Editor' },
    { name: 'Ioni Bowcher', image: 'ionibowcher.png', email: 'ioni@email.com', role: 'Viewer' }
  ];

  color: Color = new Color('#eab308').setAlpha(.2);

  form = inject(FormsBuilder).group({
    color: [null]
  });

  ngOnInit(): void {
    this.form.formChange(val => console.log(val))
  }

  toggle($event: MouseEvent, overlay: OverlayPanel) {
    overlay.toggle($event);
  }

  onShow(overlay: OverlayPanel): void {
    console.log(overlay.container)
  }

  copyColor(evt: any) {
    console.log(evt);
  }

}

