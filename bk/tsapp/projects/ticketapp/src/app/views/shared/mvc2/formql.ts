import { CommonModule } from "@angular/common";
import { ChangeDetectionStrategy, Component, EventEmitter, Input, NgModule, Output, ViewEncapsulation } from "@angular/core";
import { FormGroup, ReactiveFormsModule } from "@angular/forms";
import { FormlyFieldConfig, FormlyFormOptions, FormlyModule } from "@ngx-formly/core";
import { TranslateModule } from "@ngx-translate/core";
import { ButtonModule } from "primeng/button";
import { CardModule } from "ts-ui/card";
import { Consumer, Objects } from "ts-ui/helper";
import { FormlyWrapperFormField } from "./field-wrapper";
import { Severity } from "ts-ui/common";
import { AgTableModule, TableColumn } from "ts-ui/agtable";
import { ColDef, GridOptions } from "@ag-grid-community/core";
import { Divider } from "ts-ui/divider";

export interface FormlyFieldConfig2 extends FormlyFieldConfig {
    grid?: ColDef;
    visible?: 'grid' | 'form' | 'all';
}

export interface ActionProp {
    icon?: string;
    severity?: Severity;
    loadingIcon?: string;
    hide?: boolean;
    click?: Consumer<any>;
}

export interface FormTableConfig {
    i18n?: {
        form_title?: string;
        form_subtitle?: string;
        tb_title?: string;
        save?: string;
        create?: string;
        delete?: string;
        delete_all?: string;
        get_all?: string;
        import_xsl?: string;
        export_xsl?: string;
    };
    save?: ActionProp;
    








    form_fields?: FormlyFieldConfig2[]
    table_prop?: {
        fields?: TableColumn[];
        options?: GridOptions;
    }
}

const defaultConfig: FormTableConfig = {
    i18n: {
        form_title: 'Thông tin dữ liệu',
        tb_title: 'Danh sách dữ liệu',
        save: 'actions.save',
        create: 'actions.create',
        delete: 'actions.delete',
        delete_all: 'actions.delete_all',
        get_all: 'actions.get_all',
        import_xsl: 'actions.import_xsl',
        export_xsl: 'actions.export_xsl'
    },
    save: { icon: 'pi pi-save', severity: 'primary', loadingIcon: 'pi pi-sync' },



    // action_prop: { 
    //     save: { icon: 'pi pi-save', severity: 'primary', loadingIcon: 'pi pi-sync'},
    //     create: { icon: 'pi pi-folder-plus'}
    //  },
    form_fields: [],
    table_prop: {
        options: {
            rowSelection: {
                checkboxes: true,
                mode: 'multiRow',
                headerCheckbox: true,
                copySelectedRows: true,
                enableClickSelection: true,
                enableSelectionWithoutKeys: true,
                groupSelects: 'filteredDescendants',
                selectAll: 'all'
            }
        }
    }


};

@Component({
    // standalone: true,
    encapsulation: ViewEncapsulation.None,
    changeDetection: ChangeDetectionStrategy.OnPush,
    styles: ['ts-view-mvc { display: block; }'],
    selector: 'ts-view-mvc',
    templateUrl: './view.html'
})
export class FormTable {
    @Input() model: any = {};
    @Input() save: ActionProp;

    @Input() set config(cfg: FormTableConfig) {
        this._config = Objects.mergeDeep({}, defaultConfig, cfg);
        this.save = this._config.save;




        

        this._form_fields.fieldGroup = this._config.form_fields;
        this._grid_fields = this._config.form_fields.map(f => {
            const colDef: ColDef = f['grid'] || {};
            return { ...colDef, headerName: f.props?.label, field: f.key } as ColDef
        });
    }

    get i18n() { return this._config.i18n; }
    // get btnProps() { return this._config.action_prop; }





    get grid() { return this._config.table_prop; }
    get form_fields(): FormlyFieldConfig[] { return [this._form_fields] };
    get table_fields() {
        return this.grid.fields ?? this._grid_fields;
    }

    get options(): FormlyFormOptions { return {}; }


    //+++++++++++++++++++++++++++++++
    readonly form = new FormGroup({});
    readonly state: any = {};
    private _config: FormTableConfig = defaultConfig;
    private _grid_fields: ColDef[] = [];
    private _form_fields: FormlyFieldConfig = {
        fieldGroupClassName: 'grid sm',
        fieldGroup: this._config.form_fields
    };

    constructor() { }



    clickSave(): void {
        this._config.save.click({});
    }

    clickNew(): void { }
    clickDelete(): void { }
    clickLoadData(): void { }
    clickImport(): void { }
    clickExport(): void { }
}

@NgModule({
    declarations: [FormTable, FormlyWrapperFormField],
    exports: [FormTable, FormlyWrapperFormField],
    imports: [
        CommonModule,
        CardModule,
        TranslateModule,
        ReactiveFormsModule,
        AgTableModule,
        ButtonModule,
        Divider,
        FormlyModule.forChild({
            wrappers: [{
                name: 'form-field',
                component: FormlyWrapperFormField
            }]
        })]
})
export class FormTableModule {

}