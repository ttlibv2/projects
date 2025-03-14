import { applyEdits, findNodeAtLocation, getNodeValue, modify, parse, parseTree, printParseErrorCode, } from 'jsonc-parser';
import { readFileSync, writeFileSync } from 'node:fs';
import { getEOL } from './eol';
/** @internal */
export class JSONFile {
    path;
    content;
    eol;
    constructor(path) {
        this.path = path;
        const buffer = readFileSync(this.path);
        if (buffer) {
            this.content = buffer.toString();
        }
        else {
            throw new Error(`Could not read '${path}'.`);
        }
        this.eol = getEOL(this.content);
    }
    _jsonAst;
    get JsonAst() {
        if (this._jsonAst) {
            return this._jsonAst;
        }
        const errors = [];
        this._jsonAst = parseTree(this.content, errors, { allowTrailingComma: true });
        if (errors.length) {
            formatError(this.path, errors);
        }
        return this._jsonAst;
    }
    get(jsonPath) {
        const jsonAstNode = this.JsonAst;
        if (!jsonAstNode) {
            return undefined;
        }
        if (jsonPath.length === 0) {
            return getNodeValue(jsonAstNode);
        }
        const node = findNodeAtLocation(jsonAstNode, jsonPath);
        return node === undefined ? undefined : getNodeValue(node);
    }
    modify(jsonPath, value, insertInOrder) {
        if (value === undefined && this.get(jsonPath) === undefined) {
            // Cannot remove a value which doesn't exist.
            return false;
        }
        let getInsertionIndex;
        if (insertInOrder === undefined) {
            const property = jsonPath.slice(-1)[0];
            getInsertionIndex = (properties) => [...properties, property].sort().findIndex((p) => p === property);
        }
        else if (insertInOrder !== false) {
            getInsertionIndex = insertInOrder;
        }
        const edits = modify(this.content, jsonPath, value, {
            getInsertionIndex,
            // TODO: use indentation from original file.
            formattingOptions: {
                insertSpaces: true,
                tabSize: 2,
                eol: this.eol,
            },
        });
        if (edits.length === 0) {
            return false;
        }
        this.content = applyEdits(this.content, edits);
        this._jsonAst = undefined;
        return true;
    }
    save() {
        writeFileSync(this.path, this.content);
    }
}
// eslint-disable-next-line @typescript-eslint/no-explicit-any
export function readAndParseJson(path) {
    const errors = [];
    const content = parse(readFileSync(path, 'utf-8'), errors, { allowTrailingComma: true });
    if (errors.length) {
        formatError(path, errors);
    }
    return content;
}
function formatError(path, errors) {
    const { error, offset } = errors[0];
    throw new Error(`Failed to parse "${path}" as JSON AST Object. ${printParseErrorCode(error)} at location: ${offset}.`);
}
// eslint-disable-next-line @typescript-eslint/no-explicit-any
export function parseJson(content) {
    return parse(content, undefined, { allowTrailingComma: true });
}
