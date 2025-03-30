"use strict";
Object.defineProperty(exports, "__esModule", { value: true });
exports.findUp = findUp;
const node_fs_1 = require("node:fs");
const path = require("node:path");
function findUp(names, from) {
    if (!Array.isArray(names)) {
        names = [names];
    }
    const root = path.parse(from).root;
    let currentDir = from;
    while (currentDir && currentDir !== root) {
        for (const name of names) {
            const p = path.join(currentDir, name);
            if ((0, node_fs_1.existsSync)(p)) {
                return p;
            }
        }
        currentDir = path.dirname(currentDir);
    }
    return null;
}
//# sourceMappingURL=find-up.js.map