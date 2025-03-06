"use strict";
Object.defineProperty(exports, "__esModule", { value: true });
exports.default = default_1;
const schematics_1 = require("@angular-devkit/schematics");
const schema_1 = require("@schematics/angular/ng-new/schema");
const latest_versions_1 = require("@schematics/angular/utility/latest-versions");
function deleteAngularJson(options) {
    return (host) => {
        let path = options.directory + '/' + 'angular.json';
        if (host.exists(path)) {
            host.delete(path);
        }
    };
}
function updateNgCliJson(options) {
    return (host) => {
        let path = options.directory + '/' + '.project.json';
        host.create(path, JSON.stringify(options, null, 2));
    };
}
// Instead of `any`, it would make sense here to get a schema-to-dts package and output the
// interfaces so you get type-safe options.
function default_1(options) {
    var _a, _b, _c, _d;
    if (!options.directory) {
        // If scoped project (i.e. "@foo/bar"), convert directory to "foo/bar".
        options.directory = options.name.startsWith('@') ? options.name.slice(1) : options.name;
    }
    const ngNewOption = {
        name: options.name,
        version: options.version,
        commit: (_a = options.commit) !== null && _a !== void 0 ? _a : false,
        createApplication: (_b = options.createApplication) !== null && _b !== void 0 ? _b : false,
        directory: options.directory,
        experimentalZoneless: options.experimentalZoneless,
        inlineStyle: options.inlineStyle,
        inlineTemplate: options.inlineTemplate,
        minimal: options.minimal,
        newProjectRoot: (_c = options.appRoot) !== null && _c !== void 0 ? _c : options.packageRoot,
        packageManager: schema_1.PackageManager.Pnpm,
        prefix: (_d = options.prefix) !== null && _d !== void 0 ? _d : 't',
        routing: options.routing,
        serverRouting: options.serverRouting,
        ssr: options.ssr,
        skipGit: options.skipGit,
        skipInstall: options.skipInstall,
        skipTests: options.skipTests,
        standalone: options.standalone,
        strict: options.strict,
        style: options.style,
        viewEncapsulation: options.viewEncapsulation
    };
    const template = (0, schematics_1.apply)((0, schematics_1.url)('./files'), [
        (0, schematics_1.applyTemplates)(Object.assign(Object.assign(Object.assign({}, schematics_1.strings), options), { dot: '.', latestVersions: latest_versions_1.latestVersions })),
        (0, schematics_1.move)(options.directory)
    ]);
    return (0, schematics_1.chain)([
        (0, schematics_1.externalSchematic)('@schematics/angular', 'ng-new', ngNewOption),
        updateNgCliJson(options),
        (0, schematics_1.mergeWith)(template, schematics_1.MergeStrategy.Overwrite),
        //deleteAngularJson(options)
    ]);
}
//# sourceMappingURL=index.js.map