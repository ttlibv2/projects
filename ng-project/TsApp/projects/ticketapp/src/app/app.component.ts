import { Component, OnInit, ViewChild } from '@angular/core';
import { PrimeNGConfig } from "primeng/api";
import { TranslateService } from "@ngx-translate/core";
import { ToastrService } from "ngx-toastr";
import { LayoutService } from 'ts-layout';

@Component({
  selector: 'ts-root',
  template: `
    <router-outlet></router-outlet>
   <div aria-live="polite" toastContainer 
    [style]="{position: 'absolute', 'z-index': 9999}"></div>
  `,
  styles: []
})
export class AppComponent implements OnInit {

  // @ViewChild(ToastContainerDirective, { static: true })
  // toastContainer: ToastContainerDirective;

  constructor(
    private primengConfig: PrimeNGConfig,
    private toastService: ToastrService,
    private layoutService: LayoutService,
    private translateService: TranslateService) {

  }

  ngOnInit() {

    this.layoutService.layoutConfig({
      visibleTopBar: false,
      visibleSideBar: false,
      visibleConfig: false,
      appLogo: 'assets/images/logo-dark.svg',
      appName: 'ConYeu.Vn',
      menuTheme: 'light',
      menuMode: 'slim',
      theme: 'aura-light',
      colorScheme: 'indigo',
      themeUrlPrefix: '',

      listTheme: {
        'aura-light': {
          label: 'Aura Light',
          name: 'aura-light',
          colorSchemes: [
            { name: 'amber', color: '#f59e0b', url: 'aura-light-amber' },
            { name: 'blue', color: '#3B82F6', url: 'aura-light-blue' },
            { name: 'cyan', color: '#06b6d4', url: 'aura-light-cyan' },
            { name: 'green', color: '#10b981', url: 'aura-light-green' },
            { name: 'indigo', color: '#6366F1', url: 'aura-light-indigo' },
            { name: 'lime', color: '#84cc16', url: 'aura-light-lime' },
            { name: 'noir', color: '#020617', url: 'aura-light-noir' },
            { name: 'pink', color: '#ec4899', url: 'aura-light-pink' },
            { name: 'purple', color: '#8B5CF6', url: 'aura-light-purple' },
            { name: 'teal', color: '#14b8a6', url: 'aura-light-teal' },

          ]
        }
      },

      // listMenu2: [
      //   {
      //    label: 'Danh mục',
      //    items: [
      //       {label: 'Kênh - Tình trạng', icon: 'pi pi-address-book', routerLink: '/admin/chanels'},
      //       {label: 'Nhóm Phần mềm', icon: 'pi pi-android', routerLink: '/admin/softwares'},
      //       {label: 'Nhóm hỗ trợ', icon: 'pi pi-bookmark-fill', routerLink: '/admin/group-helps'},
      //       {label: 'Nội dung mẫu', icon: 'pi pi-chart-bar', routerLink: '/admin/questions'},
      //       {label: 'Danh mục TS', items: [
      //         {label: 'Support Team'},
      //         {label: 'Phân công cho'},
      //         {label: 'Ticket Subject Type'},
      //         {label: 'Replied Status'},
      //         {label: 'Danh mục'},
      //         {label: 'Danh mục phụ'},
      //         {label: 'Team Head'},
      //         {label: 'Loại yêu cầu hỗ trợ'},
      //         {label: 'Độ ưu tiên'},
      //         {label: 'Thẻ -- Tags'},
      //       ]}
      //    ]
      //  },
      
      // ]
    });

    
    this.layoutService.tryAddTheme();

    this.primengConfig.ripple = true;
    this.translateService.get('primeng').subscribe(s => this.primengConfig.setTranslation(s));
  }
}