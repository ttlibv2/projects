export class AgTable {
  table_id?: number;
  code?: string;
  title?: string;
  summary?: string;
  columns?: AgColumn[];
}

export class AgColumn {
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


}
