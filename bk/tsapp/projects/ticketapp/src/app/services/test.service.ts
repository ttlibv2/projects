import { effect, Injectable, signal } from "@angular/core";

@Injectable({providedIn: 'root'})
export class TestService {
    name = signal<string>(undefined);

    constructor() {
       effect(() => {
            console.log(`Test Name: `, this.name());
       })
    }
}