import { Inject, Injectable, InjectionToken, Optional, Renderer2, RendererFactory2 } from "@angular/core";
import { HttpBackend, HttpClient } from "@angular/common/http";
import { DOCUMENT } from "@angular/common";
import { DomSanitizer } from "@angular/platform-browser";
import { TsMap } from "ts-ui/helper";

// export const ICONS = new InjectionToken<IconDefinition>('icons');

@Injectable()
export class IconService {
    // private customIcons = new TsMap<string, IconDefinition>();

    constructor(
        private renderer: Renderer2,
        private sanitizer: DomSanitizer,
        @Optional() private client: HttpClient,
        @Optional() @Inject(DOCUMENT) private document: Document,
        // @Optional() @Inject(ICONS) private icons: IconDefinition[]
    ) { }



}