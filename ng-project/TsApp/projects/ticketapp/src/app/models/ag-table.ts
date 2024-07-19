import { TableColumn } from "ts-ui/ag-table";
import { BaseModel } from "./base-model";
import { AssignObject } from "./common";
import { MenuItem } from "primeng/api";
import { Objects } from "ts-helper";

export interface AgColumnState {
  colId: string;
}

export class AgTable extends BaseModel {
  table_id: number;
  code: string;
  config: any;
  position: number;
  title: string;
  user_id: number;
  svg_icon: string;
  summary: string;
  parent_id: number;
  children: AgTable[];
  columns: AgColumn[];
  states: AgColumnState[];

  grid_columns: TableColumn[] = [];

  private _menu_items: MenuItem[];

  get(fieldCol: string): AgColumn {
    return this.columns.find(c => c.field === fieldCol);
  }

  set_children(data: AssignObject<AgTable>[]): this {
    this.children = (data||[]).sort((c1, c2) => c1.position - c2.position).map(c => AgTable.from(c));
    this._menu_items = undefined;
    return this;
  }

  set_columns(data: AssignObject<AgColumn>[]): this {
    this.columns = (data || []).sort((c1, c2) => c1.position - c2.position).map(c => AgColumn.from(c));
    this.grid_columns = this.columns.map(c =>  c.asColumn());
    return this;
  }


  get menuItems(): MenuItem[] {
    if(Objects.notNull(this._menu_items)) return this._menu_items;
    else if(Objects.notNull(this.parent_id)) return [];
    else {
          const children: MenuItem[] = (this.children || []).map(t => ({
            label: `${t.position}. ${t.title}`, 
            code: t.code, 
            data: t.states,
            icon: t.svg_icon
          }));
  
          const view_default:MenuItem = {
            label: '1. Hiển thị cột mặc định', 
            code: 'view_default', icon: this.svg_icon
          };

          const save_update_ag =  {
            label: `${children.length + 2}. Tạo và cập nhật mẫu`, 
            code: 'save_update_ag',
            icon: 'far fa-floppy-disk'
          };

          return this._menu_items = [view_default, ...children, save_update_ag];
    }
  
  }

  override update(object: AssignObject<this>): this {
    return super.update(object);
  }




  static from(data: AssignObject<AgTable>):AgTable {
    return BaseModel.fromJson(AgTable, data);
  }

}



export class AgColumn extends BaseModel {
  field: string;
  width: number;
  hide: boolean;
  headerName: string;
  valueGetter: string;
  cellRenderer: any;
  position: number;

  asColumn(): TableColumn {
    return Objects.assign({}, this, ['position']);
  }
  
  static from(data: AssignObject<AgColumn>):AgColumn {
    return BaseModel.fromJson(AgColumn, data);
  }
}



// export class AgColumn extends BaseModel{
//   position: number = 0;
//   field: string;
//   colId: string;
//   headerName: string;
//   width: number;
//   hide: boolean;
//   valueGetter: any;
//   cellRenderer: any;

//   asColumn(): any {
//     return Object.assign({}, this, {position: undefined});
//   }

//   static from(data: AssignObject<AgColumn>):AgColumn {
//     return BaseModel.fromJson(AgColumn, data);
//   }

// }

// export class AgMenuItem extends BaseModel {
//   position: number;
//   label: string;
//   code: string;
//   state: AgColumnState[];
//   command: (event: any) => void;

//   set_children(data: AssignObject<AgColumnState>[]) {
//     this.state = (data || []).sort((c1, c2) => c1.position - c2.position).map(c => AgColumnState.from(c));
//   }

//   override update(object: AssignObject<this>): this {
//     if('title' in object) object['label'] = object['title'];
//     return super.update(object);
//   }
  
//   static from(data: AssignObject<AgMenuItem>):AgMenuItem {
//     return BaseModel.fromJson(AgMenuItem, data);
//   }

//   static createUpdate(position: number): AgMenuItem {
//     return AgMenuItem.from({
//       position: position,
//       label: `${position}. Tạo và cập nhật mẫu`,
//       code: 'save_update_ag'
//     });
//   }
// }

// export class AgColumnState extends BaseModel implements ColumnState {
//   position: number;
//   colId: string;
//   hide: boolean;
//   width: number;
//   flex: number;
//   sort: "asc" | "desc";
//   sortIndex: number;
//   aggFunc: string | IAggFunc<any, any>;
//   pivot: boolean;
//   pivotIndex: number;
//   pinned: ColumnPinnedType;
//   rowGroup: boolean;
//   rowGroupIndex: number;

//   asColumnState(): ColumnState {
//     return Object.assign({}, this, {position: undefined});
//   }

//   static from(data: AssignObject<AgColumnState>):AgColumnState {
//     return BaseModel.fromJson(AgColumnState, data);
//   }

// }

// export class AgTable extends BaseModel {
//   table_id: number;
//   code: string;
//   summary: string;
//   title: string;
//   position: number;
//   is_system: boolean;

//   columns: AgColumn[] = [];
//   children: AgMenuItem[] = [];

//   get(columnName: string): AgColumn {
//     return this.columns.find(col => col.field === columnName);
//   }

//   set_columns(data: AssignObject<AgColumn>[]) {
//     this.columns = (data || []).map(c => AgColumn.from(c)).sort((c1, c2) => c1.position - c2.position);
//   }

//   set_children(data: AssignObject<AgMenuItem>[]) {
//     this.children = (data || []).map(c => AgMenuItem.from(c)).sort((c1, c2) => c1.position - c2.position);
//   }


//   asMenuItems(): MenuItem[] {

//     const items: MenuItem[] =  [ 
//       {label: '1. Hiển thị cột mặc định', code: 'view_default'} ,
//       ...(this.children??[]).map(c => ({label: `${c.position}. ${c.title}`, code: '', states: c.state})),
//       {label: '2. Tạo và cập nhật mẫu', code: 'save_update_ag'}
//     ];

//     return items;
//   }







//   static from(data: AssignObject<AgTable>):AgTable {
//     console.log({...data, columns: undefined});
//     return BaseModel.fromJson(AgTable, data);
//   }

// }







