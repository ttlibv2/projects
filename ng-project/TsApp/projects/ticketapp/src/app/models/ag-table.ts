import { ColumnPinnedType, ColumnState, IAggFunc } from "ag-grid-community";
import { BaseModel } from "./base-model";
import { AssignObject } from "./common";

export class AgColumn extends BaseModel{
  position: number = 0;
  field: string;
  colId: string;
  headerName: string;
  width: number;
  hide: boolean;
  valueGetter: any;

  asColumn(): any {
    return Object.assign({}, this, {position: undefined});
  }

  static from(data: AssignObject<AgColumn>):AgColumn {
    return BaseModel.fromJson(AgColumn, data);
  }

}

export class AgMenuItem extends BaseModel {
  position: number;
  state: AgColumnState[];

  set_children(data: AssignObject<AgColumnState>[]) {
    this.state = (data || []).map(c => AgColumnState.from(c)).sort((c1, c2) => c1.position - c2.position);
  }

  
  static from(data: AssignObject<AgMenuItem>):AgMenuItem {
    return BaseModel.fromJson(AgMenuItem, data);
  }
}

export class AgColumnState extends BaseModel implements ColumnState {
  position: number;
  colId: string;
  hide: boolean;
  width: number;
  flex: number;
  sort: "asc" | "desc";
  sortIndex: number;
  aggFunc: string | IAggFunc<any, any>;
  pivot: boolean;
  pivotIndex: number;
  pinned: ColumnPinnedType;
  rowGroup: boolean;
  rowGroupIndex: number;

  asColumnState(): ColumnState {
    return Object.assign({}, this, {position: undefined});
  }

  static from(data: AssignObject<AgColumnState>):AgColumnState {
    return BaseModel.fromJson(AgColumnState, data);
  }

}

export class AgTable extends BaseModel {
  table_id: number;
  code: string;
  summary: string;
  title: string;
  position: number;
  is_system: boolean;

  columns: AgColumn[] = [];
  menuItems: AgMenuItem[] = [];

  set_columns(data: AssignObject<AgColumn>[]) {
    this.columns = (data || []).map(c => AgColumn.from(c)).sort((c1, c2) => c1.position - c2.position);
  }

  set_menuItems(data: AssignObject<AgMenuItem>[]) {
    this.menuItems = (data || []).map(c => AgMenuItem.from(c)).sort((c1, c2) => c1.position - c2.position);
  }

  static from(data: AssignObject<AgTable>):AgTable {
    console.log({...data, columns: undefined});
    return BaseModel.fromJson(AgTable, data);
  }

}







