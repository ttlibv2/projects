import { JsonValue } from '@angular-devkit/core';
import {
    Node,
    ParseError,
    applyEdits,
    findNodeAtLocation,
    getNodeValue,
    modify,
    parse,
    parseTree,
    printParseErrorCode
} from 'jsonc-parser';
import { readFileSync, writeFileSync } from 'node:fs';
import { getEOL } from './eol';
import { Tree } from '@angular-devkit/schematics';

export type InsertionIndex = (properties: string[]) => number;
export type JSONPath = (string | number)[];

function bufferToString(path: string): string {
    const buffer = readFileSync(path);
    if (buffer) {
        return buffer.toString();
    } else {
        throw new Error(`Could not read '${path}'.`);
    }
}

interface JsonFunc {
    readText: (path: string) => string;
    writeText: (path: string, text: string) => any;
}

/** @internal */
export class JSONFile {
    content: string;
    private readonly eol: string;

    static read(path: string, tree?: Tree): JSONFile {
        const func: JsonFunc = tree ?
            {
                readText: p => tree.readText(p),
                writeText: (p, txt) => tree?.overwrite(p, txt)
            } :
            {
                readText: p => bufferToString(p),
                writeText: (p, tx) => writeFileSync(p, tx)
            };

        return new JSONFile(path, func);
    }

    constructor(private readonly path: string,
                private readonly func: JsonFunc) {
        this.content = this.func.readText(path);
        this.eol = getEOL(this.content);
    }

    private _jsonAst: Node | undefined;

    private get JsonAst(): Node | undefined {
        if (this._jsonAst) {
            return this._jsonAst;
        }

        const errors: ParseError[] = [];
        this._jsonAst = parseTree(this.content, errors, { allowTrailingComma: true });
        if (errors.length) {
            formatError(this.path, errors);
        }

        return this._jsonAst;
    }

    get(jsonPath: JSONPath): any {
        const jsonAstNode = this.JsonAst;
        if (!jsonAstNode) return undefined;
        else if (jsonPath.length === 0) return getNodeValue(jsonAstNode);
        else {
            const node = findNodeAtLocation(jsonAstNode, jsonPath);
            return node === undefined ? undefined : getNodeValue(node);
        }
    }

    modify(
        jsonPath: JSONPath,
        value: JsonValue | undefined,
        insertInOrder?: InsertionIndex | false): boolean {

        if (value === undefined && this.get(jsonPath) === undefined) {
            // Cannot remove a value which doesn't exist.
            return false;
        }

        let getInsertionIndex: InsertionIndex | undefined;
        if (insertInOrder === undefined) {
            const property = jsonPath.slice(-1)[0];
            getInsertionIndex = (properties) => [...properties, property].sort().findIndex((p) => p === property);
        } //
        else if (insertInOrder !== false) {
            getInsertionIndex = insertInOrder;
        }

        const edits = modify(this.content, jsonPath, value, { getInsertionIndex,
            formattingOptions: { insertSpaces: true, tabSize: 2, eol: this.eol } });

        if (edits.length === 0) {
            return false;
        }

        this.content = applyEdits(this.content, edits);
        this.func.writeText(this.path, this.content);
        this._jsonAst = undefined;
        return true;
    }

    save(): void {
        writeFileSync(this.path, this.content);
    }

    remove(jsonPath: JSONPath): void {
        if (this.get(jsonPath) !== undefined) {
            this.modify(jsonPath, undefined);
        }
    }
}

// eslint-disable-next-line @typescript-eslint/no-explicit-any
export function readAndParseJson(path: string): any {
    const errors: ParseError[] = [];
    const content = parse(readFileSync(path, 'utf-8'), errors, { allowTrailingComma: true });
    if (errors.length) {
        formatError(path, errors);
    }
    return content;
}

function formatError(path: string, errors: ParseError[]): never {
    const { error, offset } = errors[0];
    throw new Error(
        `Failed to parse "${path}" as JSON AST Object. ${printParseErrorCode(
            error
        )} at location: ${offset}.`
    );
}

// eslint-disable-next-line @typescript-eslint/no-explicit-any
export function parseJson(content: string): any {
    return parse(content, undefined, { allowTrailingComma: true });
}