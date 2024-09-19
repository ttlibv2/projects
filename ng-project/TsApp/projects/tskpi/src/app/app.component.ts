import { Component, OnInit } from '@angular/core';
import { PrimeNGConfig } from 'primeng/api';
import { LayoutService } from 'ts-ui/app-layout';

@Component({
  selector: 'ts-root',
  templateUrl: './app.component.html',
  styleUrl: './app.component.scss'
})
export class AppComponent implements OnInit {
  title = 'tskpi';

  constructor(  
    private primengConfig: PrimeNGConfig,
    private layoutService: LayoutService) {

    }

    ngOnInit() {
      this.layoutService.tryAddTheme();
      this.primengConfig.ripple = true;
    }
    
}
