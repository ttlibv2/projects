"use strict";
Object.defineProperty(exports, "__esModule", { value: true });
exports.default = default_1;
const schematics_1 = require("@angular-devkit/schematics");
// Instead of `any`, it would make sense here to get a schema-to-dts package and output the
// interfaces so you get type-safe options.
function default_1(options) {
    // The chain rule allows us to chain multiple rules and apply them one after the other.
    return (0, schematics_1.chain)([
        (_tree, context) => {
            // Show the options for this Schematics.
            context.logger.info('My Full Schematic: ' + JSON.stringify(options));
        },
        (0, schematics_1.mergeWith)((0, schematics_1.apply)((0, schematics_1.url)('./files'), [
            (0, schematics_1.template)({
                INDEX: options.index,
                name: options.name,
            }),
        ])),
    ]);
}
//# sourceMappingURL=index.js.map