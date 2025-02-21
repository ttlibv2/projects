import { Injectable } from "@angular/core";
import { BaseStyle } from "primeng/base";

type TAny =  (dt: any) => string;

const theme: TAny = ({ dt }) => `
    .ts-user { }
`;

const classes = {
    root: 'ts-user p-page'
};

@Injectable()
export class UserStyle extends BaseStyle {
    override name: string = 'user';
    override theme: (dt: any) => string = theme;
    override classes = classes;
}