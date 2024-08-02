import {Component, Type} from '@angular/core';
import {DBService} from "ts-ui/local-db";
@Component({
  selector: 'ts-root',
  templateUrl: './app.component.html',
  styleUrl: './app.component.scss'
})
export class AppComponent {
  title = 'demo-app';


  constructor(private service: DBService) {
  }



  clickDemo() {
    console.log('aaa: ', DBService instanceof Type)
  }

}