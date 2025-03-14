"use strict";
Object.defineProperty(exports, "__esModule", { value: true });
exports.isTTY = isTTY;
function _isTruthy(value) {
    // Returns true if value is a string that is anything but 0 or false.
    return value !== undefined && value !== '0' && value.toUpperCase() !== 'FALSE';
}
function isTTY(stream = process.stdout) {
    // If we force TTY, we always return true.
    const force = process.env['NG_FORCE_TTY'];
    if (force !== undefined) {
        return _isTruthy(force);
    }
    return !!stream.isTTY && !_isTruthy(process.env['CI']);
}
