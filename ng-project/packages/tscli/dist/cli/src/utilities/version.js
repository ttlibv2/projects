"use strict";
Object.defineProperty(exports, "__esModule", { value: true });
exports.VERSION = void 0;
class Version {
    full;
    major;
    minor;
    patch;
    constructor(full) {
        this.full = full;
        const [major, minor, patch] = full.split('-', 1)[0].split('.', 3);
        this.major = major;
        this.minor = minor;
        this.patch = patch;
    }
}
exports.VERSION = new Version('0.0.0-PLACEHOLDER');
