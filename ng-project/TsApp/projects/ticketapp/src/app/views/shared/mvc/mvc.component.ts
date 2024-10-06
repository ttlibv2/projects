import {
    AfterContentInit,
    Component,
    ContentChildren,
    Input,
    OnDestroy,
    OnInit,
    QueryList,
    TemplateRef
} from '@angular/core';
import {TableColumn, TableOption} from "ts-ui/ag-table";
import {FormGroup, FormsBuilder, FormsModule} from "ts-ui/forms";
import {ToastService} from "ts-ui/toast";
import {ModalService} from "ts-ui/modal";
import {Callback, Objects} from "ts-ui/helper";
import {PrimeTemplate} from "primeng/api";
import {TranslatePipe} from "@ngx-translate/core";

const {notBlank} = Objects;

const columns: TableColumn[] = [
    {field: 'software_id', headerName: 'ID'},
    {field: 'code', headerName: 'Code'},
    {field: 'value', headerName: 'Value'},
    {field: 'soft_names', headerName: 'Names'}
];

export interface Field {
    fieldId: string;
    label: string;
    type: string;
    class?: string;
    placeholder?: string;
    options?: any;
}

@Component({
    selector: 'ts-mvc',
    templateUrl: './mvc.component.html',
    styleUrl: './mvc.component.scss'
})
export class MvcComponent implements AfterContentInit, OnInit, OnDestroy{
    @Input() agColumns: TableColumn[] = [];
    @Input() agOption: TableOption = {
        domLayout: 'normal',
        getRowId: r => r.data['id']
    };

    @Input()
    set form(cb: Callback<FormsBuilder, FormGroup>) {
        this.formGroup = cb(this.fb);
    };

    @Input() fields: Field[];
    @Input() submitLabel: string = 'actions.save';

    @ContentChildren(PrimeTemplate)
    templates: QueryList<PrimeTemplate>;

    formGroup: FormGroup;
    formTemplate: TemplateRef<any> | null;


    constructor(private fb: FormsBuilder,
                private toast: ToastService,
                private modal: ModalService) {
    }

    ngAfterContentInit() {
        this.templates?.forEach(item => {
           switch (item.getType()) {
               case 'formTemplate':
                   this.formTemplate = item.template;
                   break;
           }
        });
    }

    ngOnInit() {
    }

    ngOnDestroy() {
    }


    onSubmit(): void {
        console.log(this.formGroup.getRawValue())

    }
}
