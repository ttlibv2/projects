"use strict";
Object.defineProperty(exports, "__esModule", { value: true });
exports.SchematicRunner = void 0;
const abstract_runner_1 = require("./abstract.runner");
class SchematicRunner extends abstract_runner_1.AbstractRunner {
    constructor() {
        super(`schematics`);
    }
}
exports.SchematicRunner = SchematicRunner;
// function findClosestSchematicsBinary(): string {
//   try {
//     return require.resolve(
//       '@angular-devkit/schematics-lib/bin/schematics.js',
//       { paths: this.getModulePaths() },
//     );
//   } catch {
//     throw new Error("'@angular-devkit/schematics-lib' path could not be found!");
//   }
// }
//# sourceMappingURL=schematic.runner.js.map