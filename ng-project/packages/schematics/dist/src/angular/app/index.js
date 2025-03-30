"use strict";
Object.defineProperty(exports, "__esModule", { value: true });
exports.default = default_1;
const schematics_1 = require("@angular-devkit/schematics");
function default_1(options) {
    const ngOption = {
        name: options.name ?? 'web',
        prefix: options.prefix ?? 't',
        strict: options.strict ?? true,
        minimal: options.minimal ?? false,
        standalone: true,
        viewEncapsulation: options.viewEncapsulation ?? 'None',
        ssr: options.ssr ?? false,
        inlineStyle: options.inlineStyle ?? false,
        skipInstall: options.skipInstall ?? false,
        //skipGit: options.skipGit ?? true,
        routing: options.routing ?? true,
        inlineTemplate: options ?? false,
        style: options.style ?? 'scss',
        experimentalZoneless: options.experimentalZoneless ?? false,
        serverRouting: options.serverRouting ?? false,
        skipTests: options.skipTests ?? true,
        skipPackageJson: options.skipPackageJson ?? false,
        projectRoot: 'apps/web',
    };
    return (0, schematics_1.chain)([
        (0, schematics_1.externalSchematic)('@schematics/angular', 'application', ngOption)
    ]);
}
//# sourceMappingURL=index.js.map