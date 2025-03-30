"use strict";
Object.defineProperty(exports, "__esModule", { value: true });
exports.AbstractPkgManager = void 0;
class AbstractPkgManager {
    runner;
    constructor(runner) {
        this.runner = runner;
    }
    version() {
        return this.runner.run('--version', true);
    }
}
exports.AbstractPkgManager = AbstractPkgManager;
//# sourceMappingURL=pkg.abstract.js.map