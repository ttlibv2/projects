import { Component, OnInit, ViewEncapsulation } from "@angular/core";
import { FormArray, FormBuilder, FormGroup, Validators } from "@angular/forms";
import { DynamicDialogRef } from "primeng/dynamicdialog";
import { Objects } from "ts-ui/helper";
import { ModalService } from "ts-ui/modal";

@Component({
    selector: 'ts-email-template',
    encapsulation: ViewEncapsulation.None,
    styleUrl: './email-template.view.scss',
    templateUrl: './email-template.view.html'
})
export class EmailTemplateView implements OnInit {
    formGroup: FormGroup;
    fields: FormArray<FormGroup>;

    lsInputType: string[] = ['text'];


    constructor(
        private dialogRef: DynamicDialogRef,
        private modal: ModalService,
        private fb: FormBuilder) { 
            this.createFG();
        }

    ngOnInit(): void {
        const instanceData: any = this.modal.getData(this.dialogRef);
        const {fields, html} = instanceData?.template?.data || {fields: [], html: null};
        this.addFields(Objects.isEmpty(fields) ? 1 : fields.length);
        this.formGroup.patchValue({html: {html}, fields});
    }

    private createFG() {
        this.fields = this.fb.array([]) as any;
        this.formGroup = this.fb.group({
            html: [null, Validators.required],
            fields: this.fields
        });
    }

    private addFields(total: number) {
        for(let i=0;i<total;i++) {
            this.addField();
        }
    }

    /**
     * Add field to array
     */
    addField() {
        this.fields.push(this.fb.group({
            name: [null, Validators.required],
            label: [null, Validators.required],
            type: ['text', Validators.required],
            value: [null], required: [true]
        }));
    }

    /** 
     * Remove field at index
     * @param {number} fieldIndex the index to delete
     * */
    removeField(fieldIndex: number) {
        if (this.fields.length > 1) {
            this.fields.removeAt(fieldIndex);
        }
    }

    /**
     * Close dialog
     */
    closeDialog(): void {
        this.dialogRef.destroy();
    }

    /**
     * Save form
     */
    saveForm(): void {
        if (!this.formGroup.invalid) {
            let {fields, html} = this.formGroup.getRawValue();
            html = typeof html === 'string' ? html : html.html;
            this.dialogRef.close({data: {fields, html}});
        }


    }
}