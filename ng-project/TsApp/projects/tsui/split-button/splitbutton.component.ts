import { ChangeDetectionStrategy, Component, ContentChildren, ElementRef, EventEmitter, Input, Output, QueryList, TemplateRef, ViewChild, ViewEncapsulation, booleanAttribute, numberAttribute, signal } from '@angular/core';
import { MenuItem, PrimeTemplate, TooltipOptions } from 'primeng/api';
import { TieredMenu } from 'primeng/tieredmenu';
import { UniqueComponentId } from 'primeng/utils';
import { ButtonProps, MenuButtonProps } from './splitbutton.interface';
import { BehaviorSubject } from 'rxjs';

type SplitButtonIconPosition = 'left' | 'right';

@Component({
    selector: 'ts-splitButton',
    templateUrl: './splitbutton.component.html',
    styleUrl: './splitbutton.component.scss',
    changeDetection: ChangeDetectionStrategy.OnPush,
    encapsulation: ViewEncapsulation.None,
    host: {
        class: 'p-element'
    }
})
export class SplitButton {
    /**
       * MenuModel instance to define the overlay items.
       * @group Props
       */
    @Input() model: MenuItem[] | undefined;
    /**
     * Defines the style of the button.
     * @group Props
     */
    @Input() severity: 'success' | 'info' | 'warning' | 'danger' | 'help' | 'primary' | 'secondary' | 'contrast' | null | undefined;
    /**
     * Add a shadow to indicate elevation.
     * @group Props
     */
    @Input({ transform: booleanAttribute }) raised: boolean = false;
    /**
     * Add a circular border radius to the button.
     * @group Props
     */
    @Input({ transform: booleanAttribute }) rounded: boolean = false;
    /**
     * Add a textual class to the button without a background initially.
     * @group Props
     */
    @Input({ transform: booleanAttribute }) text: boolean = false;
    /**
     * Add a border class without a background initially.
     * @group Props
     */
    @Input({ transform: booleanAttribute }) outlined: boolean = false;
    /**
     * Defines the size of the button.
     * @group Props
     */
    @Input() size: 'small' | 'large' | undefined | null = null;
    /**
     * Add a plain textual class to the button without a background initially.
     * @group Props
     */
    @Input({ transform: booleanAttribute }) plain: boolean = false;
    /**
     * Name of the icon.
     * @group Props
     */
    @Input() icon: string | undefined;
    /**
     * Position of the icon.
     * @group Props
     */
    @Input() iconPos: SplitButtonIconPosition = 'left';
    /**
     * Text of the button.
     * @group Props
     */
    @Input() label: string | undefined;
    /**
     * Tooltip for the main button.
     * @group Props
     */
    @Input() tooltip: string | undefined;
    /**
     * Tooltip options for the main button.
     * @group Props
     */
    @Input() tooltipOptions: TooltipOptions | undefined;
    /**
     * Inline style of the element.
     * @group Props
     */
    @Input() style: { [klass: string]: any } | null | undefined;
    /**
     * Class of the element.
     * @group Props
     */
    @Input() styleClass: string | undefined;
    /**
     * Inline style of the overlay menu.
     * @group Props
     */
    @Input() menuStyle: { [klass: string]: any } | null | undefined;
    /**
     * Style class of the overlay menu.
     * @group Props
     */
    @Input() menuStyleClass: string | undefined;

    /**
     *  Target element to attach the overlay, valid values are "body" or a local ng-template variable of another element (note: use binding with brackets for template variables, e.g. [appendTo]="mydiv" for a div element having #mydiv as variable name).
     * @group Props
     */
    @Input() appendTo: HTMLElement | ElementRef | TemplateRef<any> | string | null | undefined | any;
    /**
     * Indicates the direction of the element.
     * @group Props
     */
    @Input() dir: string | undefined;
    /**
     * Defines a string that labels the expand button for accessibility.
     * @group Props
     */
    @Input() expandAriaLabel: string | undefined;
    /**
     * Transition options of the show animation.
     * @group Props
     */
    @Input() showTransitionOptions: string = '.12s cubic-bezier(0, 0, 0.2, 1)';
    /**
     * Transition options of the hide animation.
     * @group Props
     */
    @Input() hideTransitionOptions: string = '.1s linear';
    /**
     * Button Props
     */
    @Input() buttonProps: ButtonProps | undefined;
    /**
     * Menu Button Props
     */
    @Input() menuButtonProps: MenuButtonProps | undefined;
    /**
     * When present, it specifies that the component should automatically get focus on load.
     * @group Props
     */
    @Input({ transform: booleanAttribute }) autofocus: boolean | undefined;

    @Input({transform: booleanAttribute}) visibleMenuButton: boolean = true;

    @Input() menuTrigger: 'click' | 'hover' = 'click';

    /**
     * When present, it specifies that the element should be disabled.
     * @group Props
     */
    @Input({ transform: booleanAttribute }) set disabled(v: boolean | undefined) {
        this._disabled = v;
        this._buttonDisabled = v;
        this.menuButtonDisabled = v;
    }
    public get disabled(): boolean | undefined {
        return this._disabled;
    }
    /**
     * Index of the element in tabbing order.
     * @group Props
     */
    @Input({ transform: numberAttribute }) tabindex: number | undefined;
    /**
     * When present, it specifies that the menu button element should be disabled.
     * @group Props
     */
    @Input('menuButtonDisabled') set menuButtonDisabled(v: boolean | undefined) {
        if (this.disabled) {
            this._menuButtonDisabled = this.disabled;
        } else this._menuButtonDisabled = v;
    }
    public get menuButtonDisabled(): boolean | undefined {
        return this._menuButtonDisabled;
    }
    /**
     * When present, it specifies that the button element should be disabled.
     * @group Props
     */
    @Input() set buttonDisabled(v: boolean | undefined) {
        if (this.disabled) {
            this.buttonDisabled = this.disabled;
        } else this._buttonDisabled = v;
    }
    public get buttonDisabled(): boolean {
        return this._buttonDisabled;
    }
    /**
     * Callback to invoke when default command button is clicked.
     * @param {MouseEvent} event - Mouse event.
     * @group Emits
     */
    @Output() onClick: EventEmitter<MouseEvent> = new EventEmitter<MouseEvent>();
    /**
     * Callback to invoke when overlay menu is hidden.
     * @group Emits
     */
    @Output() onMenuHide: EventEmitter<any> = new EventEmitter<any>();
    /**
     * Callback to invoke when overlay menu is shown.
     * @group Emits
     */
    @Output() onMenuShow: EventEmitter<any> = new EventEmitter<any>();
    /**
     * Callback to invoke when dropdown button is clicked.
     * @param {MouseEvent} event - Mouse event.
     * @group Emits
     */
    @Output() onDropdownClick: EventEmitter<MouseEvent> = new EventEmitter<MouseEvent>();

    @ViewChild('container') containerViewChild: ElementRef | undefined;

    @ViewChild('defaultbtn') buttonViewChild: ElementRef | undefined;

    @ViewChild('menu') menu: TieredMenu | undefined;

    @ContentChildren(PrimeTemplate) templates: QueryList<PrimeTemplate> | undefined;

    contentTemplate: TemplateRef<any> | undefined;

    dropdownIconTemplate: TemplateRef<any> | undefined;

    ariaId: string | undefined;

    isExpanded = signal<boolean>(false);

    private _disabled: boolean | undefined;
    private _buttonDisabled: boolean | undefined;
    private _menuButtonDisabled: boolean | undefined;

    ngOnInit() {
        this.ariaId = UniqueComponentId();
    }

    ngAfterContentInit() {
        this.templates?.forEach((item) => {
            switch (item.getType()) {
                case 'content':
                    this.contentTemplate = item.template;
                    break;

                case 'dropdownicon':
                    this.dropdownIconTemplate = item.template;
                    break;

                default:
                    this.contentTemplate = item.template;
                    break;
            }
        });
    }

    get containerClass() {
        const cls = {
            'p-splitbutton p-component': true,
            'p-button-raised': this.raised,
            'p-button-rounded': this.rounded,
            'p-button-outlined': this.outlined,
            'p-button-text': this.text,
            'p-not-menu-button': this.visibleMenuButton === false,
            [`p-button-${this.size === 'small' ? 'sm' : 'lg'}`]: this.size
        };

        return { ...cls };
    }

    onContainerMouse(event: MouseEvent, visible: boolean) {
        if(this.menuTrigger === 'hover') {
            const menuEvent = { currentTarget: this.containerViewChild?.nativeElement, relativeAlign: this.appendTo == null };
           if(visible === true)this.menu.show(menuEvent) ;
           else this.menu.hide(menuEvent);
        }

    }

    onDefaultButtonClick(event: MouseEvent) {
        if(this.visibleMenuButton === false) {
            this.onDropdownButtonClick();
        }
        else{
            this.onClick.emit(event);
            this.menu.hide();
        }
    }

    onDropdownButtonClick(event?: MouseEvent) {
        this.onDropdownClick.emit(event);
        this.menu?.toggle({ currentTarget: this.containerViewChild?.nativeElement, relativeAlign: this.appendTo == null });
    }

    onDropdownButtonKeydown(event: KeyboardEvent) {
        if (event.code === 'ArrowDown' || event.code === 'ArrowUp') {
            this.onDropdownButtonClick();
            event.preventDefault();
        }
    }

    onHide() {
        this.isExpanded.set(false);
        this.onMenuHide.emit();
    }

    onShow() {
        this.isExpanded.set(true);
        this.onMenuShow.emit();
    }
}
