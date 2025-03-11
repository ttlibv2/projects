import { Injectable } from "@angular/core";
import { BaseStyle } from "primeng/base";

type TAny =  (dt: any) => string;

const theme: TAny = ({ dt }) => `
    .p-dashboard { }
`;

const classes = {
    root: 'p-dashboard p-page'
};

@Injectable()
export class DashboardStyle extends BaseStyle {
    override name: string = 'dashboard';
    override theme: (dt: any) => string = theme;
    override classes = classes;
}