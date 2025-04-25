import { strings } from '@angular-devkit/core';

export function cmdOptionToString(name: string, value: any, useNo: boolean = false): string {
    name = strings.dasherize(name);

    let dot = name.length == 1 ? '-' : '--';

    if(typeof value === 'boolean') {
        if(value) return `${dot}${name}`;
        else if(useNo) return `--no-${name}`;
        else return `${dot}${name}=${value}`;
    }

    else if(typeof value === 'string') {
        value = value.includes(' ') ? `"${value}"` : value;
        return `${dot}${name}=${value}`;
    }
    else {
        return `${dot}${name}=${value}`;
    }
}