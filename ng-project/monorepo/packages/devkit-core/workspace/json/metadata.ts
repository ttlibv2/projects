import { JSONPath, Node, findNodeAtLocation, getNodeValue } from 'jsonc-parser';
import { JsonValue } from '@angular-devkit/core';
import { ProjectProp, WorkspaceProp } from '../ws.type';
import { WorkspaceHost } from '../ws.host';

function escapeKey(key: string): string | number {
    return key.replace('~', '~0').replace('/', '~1');
}

export interface ChangeValues {
    json: JsonValue;
    project: ProjectProp;
    projectCollection: Iterable<[string, ProjectProp]>;
}

export type BaseChangeInput<T extends keyof ChangeValues = any> = (jsonPath: string[], value: any) => {
    jsonPath: string[];
    value: ChangeValues[T] | undefined;
    type?: T;
}

export interface JsonChange {
    value?: unknown;
    type?: keyof ChangeValues;
    jsonPath: string[];
}

export const JsonWorkspaceSymbol: unique symbol = Symbol.for('@ngdev/devkit-core:workspace-json');

export interface JsonWorkspaceProp extends WorkspaceProp {
    [JsonWorkspaceSymbol]: JsonWorkspaceMetadata;
}

export class JsonWorkspaceMetadata {
    readonly changes = new Map<string, JsonChange>();

    hasLegacyTargetsName = true;

    constructor(
        readonly filePath: string,
        private readonly ast: Node,
        readonly raw: string) {}

    get hasChanges(): boolean {
        return this.changes.size > 0;
    }

    get changeCount(): number {
        return this.changes.size;
    }

    getNodeValueFromAst(path: JSONPath): unknown {
        const node = findNodeAtLocation(this.ast, path);
        return node && getNodeValue(node);
    }

    findChangesForPath(path: string): JsonChange | undefined {
        return this.changes.get(path);
    }

    addChange<T extends keyof ChangeValues = keyof ChangeValues>(jsonPath: string[], value: ChangeValues[T] | undefined, type?: T): void {
        let currentPath = '';
        for (let index = 0; index < jsonPath.length - 1; index++) {
            currentPath = currentPath + '/' + escapeKey(jsonPath[index]);
            if (this.changes.has(currentPath)) {
                // Ignore changes on children as parent is updated.
                return;
            }
        }

        const pathKey = '/' + jsonPath.map((k) => escapeKey(k)).join('/');
        for (const key of this.changes.keys()) {
            if (key.startsWith(pathKey + '/')) {
                // changes on the same or child paths are redundant.
                this.changes.delete(key);
            }
        }

        this.changes.set(pathKey, { jsonPath, type, value });
    }
}