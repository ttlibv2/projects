import {BaseModel} from "./base-model";
import {JsonObject} from "./common";
import {Objects} from "../utils/objects";

export class AgTable extends BaseModel{
  table_id?: number;
  code?: string;
  title?: string;
  summary?: string;
  columns?: AgColumn[];

  static from(data: JsonObject): AgTable {
    return new AgTable().update(data);
  }

  override update(data: JsonObject): this {
    super.update(data);
    Objects.ifListNotEmpty(data['columns'],
        items => items.flatMap(item => AgColumn.from(item)) );
    return this;
  }
}

export class AgColumn extends BaseModel {
  column_id?: number;
  field_name?: string;
  header_name?: string;
  column_type?: string;
  visible?: boolean;
  sort?: boolean;
  resizable?: boolean;
  pinned?: boolean;
  position?: number;
  width?: number;
  ag_code?: string;

  static from(data: JsonObject): AgColumn {
    return new AgColumn().update(data);
  }

}
