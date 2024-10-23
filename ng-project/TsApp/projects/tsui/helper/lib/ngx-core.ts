import {Objects} from "./objects";

export function booleanAttribute(value: any, valueDefault: boolean=false): boolean {
    if (Objects.isNull(value)) return valueDefault;
    else if (typeof value === 'boolean') return value;
    else if ([1, 0].includes(value)) return value === 1;
    else if (["1", "0"].includes(value)) return value === "1";
    else if (["false", "true"].includes(value)) return value === "true";
    else return valueDefault;
}
