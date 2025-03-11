import { TemplateRef } from "@angular/core";
import { Objects } from "ts-ui/helper";
const { isString, notNull, isNull, isArray, notEmpty } = Objects;

export type IconDesc = string | IconObject | string[] | IconObject[] | TemplateRef<any>;

export function hasBool(input: IconDesc, template: TemplateRef<any>): boolean {
    if (notNull(template)) return true;
    else if (isNull(input)) return false;
    else if (isString(input)) return input.length > 0;
    else if (isArray(input)) return input.length > 0;
    else return notEmpty(input);
}

export interface IconObject {
    name: string;
    color?: string;
    hoverColor?: string;
    focusColor?: string;
    onClick?: () => void;
}

