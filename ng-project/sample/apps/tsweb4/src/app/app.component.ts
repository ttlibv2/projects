import { Component } from '@angular/core';
import { RouterOutlet } from '@angular/router';

@Component({
  selector: 't-root',
  imports: [RouterOutlet],
  templateUrl: './app.component.html',
  styleUrl: './app.component.scss'
})
export class AppComponent {
  title = 'tsweb4';
}
