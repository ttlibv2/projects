export enum SheetType {
    GRID, OBJECT, DATA_SOURCE
}

export interface Color {
    red: number;
    green: number;
    blue: number;
}

export interface Attribute {
    name: string;
    value: string;
}