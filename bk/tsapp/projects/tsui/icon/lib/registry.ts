import { DOCUMENT } from "@angular/common";
import { HttpClient } from "@angular/common/http";
import { ErrorHandler, Inject, Injectable, OnDestroy, Optional } from "@angular/core";
import { DomSanitizer } from "@angular/platform-browser";

@Injectable({providedIn: 'root'})
export class IconRegistry implements OnDestroy {
    constructor(
        @Optional() 
        private http: HttpClient,

        @Optional() @Inject(DOCUMENT) 
        private document: Document,

        private sanitizer: DomSanitizer,
        private errorHandler: ErrorHandler


    ){}


    ngOnDestroy(): void {
        
    }
}