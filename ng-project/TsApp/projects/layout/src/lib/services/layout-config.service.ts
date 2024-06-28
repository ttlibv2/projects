import { Injectable } from "@angular/core";

@Injectable()
export class LayoutConfigService {
    enableRipple: boolean = true;
    minimal: boolean = false;
    scales: number[] = [];
    componentThemes: any[] = [];
    colorSchemas: any[] = [];
    menuModes: any[] = [];
}