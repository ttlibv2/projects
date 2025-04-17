import { strings } from '@angular-devkit/core';

export function cmdOptionToString(name: string, value: any): string {
    name = strings.dasherize(name);

    let dot = name.length == 1 ? '-' : '--';

    if(typeof value === 'boolean') {
       return value ? `${dot}${name}` : `--no-${name}`;
    }

    else if(typeof value === 'string') {
        value = value.includes(' ') ? `"${value}"` : value;
        return `${dot}${name}=${value}`;
    }
    else {
        return `${dot}${name}=${value}`;
    }
}