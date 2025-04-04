import { JsonValue } from '@angular-devkit/core';

export function isJsonValue(value: unknown): value is JsonValue {
    const visited = new Set();

    switch (typeof value) {
        case 'boolean':
        case 'number':
        case 'string':
            return true;
        case 'object':
            if (value === null) {
                return true;
            }
            visited.add(value);
            for (const property of Object.values(value)) {
                if (typeof value === 'object' && visited.has(property)) {
                    continue;
                }
                if (!isJsonValue(property)) {
                    return false;
                }
            }

            return true;
        default:
            return false;
    }
}