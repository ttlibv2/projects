import { Component, DoCheck, ElementRef, inject, Renderer2 } from '@angular/core';
import { FieldWrapper, FormlyFieldConfig, FormlyFieldProps as CoreFormlyFieldProps } from '@ngx-formly/core';
import { DomHandler } from 'ts-ui/common';

export interface FormlyFieldProps extends CoreFormlyFieldProps {
    hideRequiredMarker?: boolean;
    hideLabel?: boolean;
    fieldClass?: string;
}

@Component({
    selector: 'formly-wrapper-primeng-custom form-field',
    host: {  'class': 'p-field p-fluid'  },
    template: `
    <!-- <div class="p-field" [ngClass]="props.fieldClass"> -->
      <label *ngIf="props.label && props.hideLabel !== true" [for]="id">
        {{ props.label }}
        <span *ngIf="props.required && props.hideRequiredMarker !== true" aria-hidden="true">*</span>
      </label>
      <ng-container #fieldComponent></ng-container>

      <small *ngIf="showError" class="p-error">
        <formly-validation-message class="ui-message-text" [field]="field"></formly-validation-message>
      </small>
    <!-- </div> -->
  `,
})
export class FormlyWrapperFormField extends FieldWrapper<FormlyFieldConfig<FormlyFieldProps>> implements DoCheck { 
    private _renderder = inject(Renderer2);
    private _elementRef = inject(ElementRef);

    ngDoCheck(): void {
        DomHandler.addClass(this._elementRef.nativeElement, this.props.fieldClass);
    }



    
}