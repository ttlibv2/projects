import { AfterContentInit, AfterViewInit, booleanAttribute, ChangeDetectionStrategy, 
Component, ContentChild, ContentChildren, ElementRef, Input, NgZone, OnChanges, 
OnDestroy, Optional, QueryList, SimpleChanges, TemplateRef, ViewChild, ViewEncapsulation } from "@angular/core";

export abstract class AbstractInput {
	@Input() prefixIcons: (string | IconObject)[] = [];
    @Input() suffixIcons: string[] = [];
    @Input() addonAfterIcons: string[] = [];
    @Input() addonBeforeIcons: string[] = [];
	
	@Input({ transform: booleanAttribute })
    compact: boolean = true;

    /**
     * placeholder of the input element.
     * @group Props
     */
    @Input() placeholder: string = '';

    /**
     * Class of the element.
     * @group Props
     */
    @Input() styleClass: string;

    /**
     * Class of the element.
     * @group Props
     */
    @Input() style: { [key: string]: any };

    @Input() flexAddon: string = '.5rem';
    @Input() flexAffix: string = '.5rem';
	
}