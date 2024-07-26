import { Component } from '@angular/core';
import { TableColumn, TableOption } from 'ts-ui/ag-table';

@Component({
  selector: 'ts-table-info',
  templateUrl: './table-info.component.html',
  styleUrl: './table-info.component.scss'
})
export class TableInfoComponent {
  columns: TableColumn[] = [
    {field: 'field_name', headerName: 'Field Name'},
    {field: 'header_name', headerName: 'Header Name'},
    {field: 'type', headerName: 'Column Type', cellDataType: 'text'},
    {field: 'hide', headerName: 'Hide', cellDataType: 'boolean'},
    {field: 'width', headerName: 'Width', cellDataType: 'number'},
    {field: 'pinned', headerName: 'Pinned', cellDataType: 'boolean'},
    {field: 'position', headerName: 'Position', cellDataType: 'number'},
    {field: 'resizable', headerName: 'Resizeable', cellDataType: 'boolean'},
    {field: 'sort', headerName: 'Sort', cellDataType: 'boolean'},
    {field: 'checkboxSelection', headerName: 'checkboxSelection', cellDataType: 'boolean'}
  ];

  rows: any[] = [
    {field: '123333'}
  ];

  options: TableOption = {
    defaultColDef: {
      editable: true,
      singleClickEdit: true
    }
  };

}
