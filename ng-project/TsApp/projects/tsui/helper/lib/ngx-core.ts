import {Objects} from "./objects";

export function booleanAttribute(value: any, defaultNull?: boolean): boolean {
    if (Objects.isNull(value)) return defaultNull;
    else if (typeof value === 'boolean') return value;
    else if ([1, 0].includes(value)) return value === 1;
    else if (["1", "0"].includes(value)) return value === "1";
    else return undefined;
}
