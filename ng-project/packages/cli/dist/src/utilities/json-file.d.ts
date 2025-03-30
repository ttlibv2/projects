import { JsonValue } from '@angular-devkit/core';
export type InsertionIndex = (properties: string[]) => number;
export type JSONPath = (string | number)[];
/** @internal */
export declare class JSONFile {
    private readonly path;
    content: string;
    private eol;
    constructor(path: string);
    private _jsonAst;
    private get JsonAst();
    get(jsonPath: JSONPath): unknown;
    modify(jsonPath: JSONPath, value: JsonValue | undefined, insertInOrder?: InsertionIndex | false): boolean;
    save(): void;
}
export declare function readAndParseJson(path: string): any;
export declare function parseJson(content: string): any;
