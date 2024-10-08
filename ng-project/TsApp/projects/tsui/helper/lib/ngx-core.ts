import {Objects} from "./objects";

export function booleanAttribute(value: any): boolean {
    if (Objects.isNull(value)) return null;
    else if (typeof value === 'boolean') return value;
    else if ([1, 0].includes(value)) return value === 1;
    else if (["1", "0"].includes(value)) return value === "1";
    else return undefined;
}
