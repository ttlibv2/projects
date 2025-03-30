"use strict";
Object.defineProperty(exports, "__esModule", { value: true });
exports.colors = void 0;
exports.supportColor = supportColor;
const node_tty_1 = require("node:tty");
exports.colors = require("ansis");
function supportColor(stream = process.stdout) {
    if (stream instanceof node_tty_1.WriteStream) {
        return stream.hasColors();
    }
    try {
        // The hasColors function does not rely on any instance state and should ideally be static
        return node_tty_1.WriteStream.prototype.hasColors();
    }
    catch {
        return process.env['FORCE_COLOR'] !== undefined && process.env['FORCE_COLOR'] !== '0';
    }
}
//# sourceMappingURL=color.js.map