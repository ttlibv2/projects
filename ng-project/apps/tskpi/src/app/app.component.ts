import { Component } from '@angular/core';
import { RouterOutlet } from '@angular/router';

@Component({
  selector: 't-root',
  imports: [RouterOutlet],
  templateUrl: './app.component.html',
  styles: [],
})
export class AppComponent {
  title = 'tskpi';
}
