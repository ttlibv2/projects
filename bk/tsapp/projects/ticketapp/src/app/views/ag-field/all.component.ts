import { ChangeDetectionStrategy, Component, NgZone, OnInit, ViewEncapsulation } from '@angular/core';
import { TableColumn, TableOption } from 'ts-ui/agtable';
import { FormGroup, FormsBuilder } from 'ts-ui/forms';
import { TreeNode } from 'primeng/api';

@Component({
  selector: 'ts-ag-field-all',
  encapsulation: ViewEncapsulation.None,
  changeDetection: ChangeDetectionStrategy.OnPush,
  templateUrl: './html.html',
  styleUrl: './field.component.scss'
})
export class AllView {
  agFieldOption: TableOption<any> = { sideBar: false };

  fieldColumns: TableColumn[] = [
    { field: 'field', headerName: 'Mã cột' },
    { field: 'headerName', headerName: 'Tên cột' },
    { field: 'width', headerName: 'Độ rộng' },
    { field: 'type', headerName: 'type' },
    { field: 'cellDataType' },
    { field: 'valueGetter' },
    { field: 'valueFormatter' },
    { field: 'checkboxSelection' },
    { field: 'hide' },
    { field: 'editable' },
    { field: 'valueSetter' },
    { field: 'cellEditor' },
    { field: 'singleClickEdit' },
    { field: 'floatingFilter' },
    { field: 'wrapHeaderText' },
    { field: 'autoHeaderHeight' },
    { field: 'headerComponent' },
    { field: 'menuTabs' },
    { field: 'pinned' },
    { field: 'pivot' },
    { field: 'cellClass' },
    { field: 'cellRenderer' },
    { field: 'autoHeight' },
    { field: 'wrapText' },
    { field: 'rowDrag' },
    { field: 'sort' },
    { field: 'minWidth' },
    { field: 'maxWidth' },
    { field: 'flex' },
    { field: 'resizable' }
  ];

  treeValue: TreeNode[] = [
    {
      key: '0',
      label: 'Documents',
      data: 'Documents Folder',
      icon: 'pi pi-fw pi-inbox',
      expanded: true,
      children: [
        {
          key: '0-0',
          label: 'Work',
          data: 'Work Folder',
          icon: 'pi pi-fw pi-cog',
        },
        {
          key: '0-1',
          label: 'Home',
          data: 'Home Folder',
          icon: 'pi pi-fw pi-home',
        }
      ]
    }
  ];

  fgTable: FormGroup;
  fgColumn: FormGroup;

  constructor(private fb: FormsBuilder) { }














  selectTable(node: any) {
    console.log(`selectTable`, node?.data);
  }



}