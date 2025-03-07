"use strict";
/**
 * @license
 * Copyright Google LLC All Rights Reserved.
 *
 * Use of this source code is governed by an MIT-style license that can be
 * found in the LICENSE file at https://angular.dev/license
 */
var __awaiter = (this && this.__awaiter) || function (thisArg, _arguments, P, generator) {
    function adopt(value) { return value instanceof P ? value : new P(function (resolve) { resolve(value); }); }
    return new (P || (P = Promise))(function (resolve, reject) {
        function fulfilled(value) { try { step(generator.next(value)); } catch (e) { reject(e); } }
        function rejected(value) { try { step(generator["throw"](value)); } catch (e) { reject(e); } }
        function step(result) { result.done ? resolve(result.value) : adopt(result.value).then(fulfilled, rejected); }
        step((generator = generator.apply(thisArg, _arguments || [])).next());
    });
};
Object.defineProperty(exports, "__esModule", { value: true });
const node_path_1 = require("node:path");
const command_module_1 = require("../../command-builder/command-module");
const color_1 = require("../../utilities/color");
const command_config_1 = require("../command-config");
// import nodeModule from 'node:module';
const nodeModule = require("node:module");
/**
 * Major versions of Node.js that are officially supported by Angular.
 */
const SUPPORTED_NODE_MAJORS = [20, 22];
const PACKAGE_PATTERNS = [
    /^@angular\/.*/,
    /^@angular-devkit\/.*/,
    /^@ngtools\/.*/,
    /^@schematics\/.*/,
    /^rxjs$/,
    /^typescript$/,
    /^ng-packagr$/,
    /^webpack$/,
    /^zone\.js$/,
];
class VersionCommandModule extends command_module_1.CommandModule {
    constructor() {
        super(...arguments);
        this.command = 'version';
        this.aliases = command_config_1.RootCommands['version'].aliases;
        this.describe = 'Outputs Angular CLI version.';
    }
    builder(localYargs) {
        return localYargs;
    }
    run() {
        return __awaiter(this, void 0, void 0, function* () {
            var _a;
            const { packageManager, logger, root } = this.context;
            const localRequire = nodeModule.createRequire((0, node_path_1.resolve)(__filename, '../../../'));
            // Trailing slash is used to allow the path to be treated as a directory
            const workspaceRequire = nodeModule.createRequire(root + '/');
            const cliPackage = localRequire('./package.json');
            let workspacePackage;
            try {
                workspacePackage = workspaceRequire('./package.json');
            }
            catch (_b) { }
            const [nodeMajor] = process.versions.node.split('.').map((part) => Number(part));
            const unsupportedNodeVersion = !SUPPORTED_NODE_MAJORS.includes(nodeMajor);
            const packageNames = new Set(Object.keys(Object.assign(Object.assign(Object.assign(Object.assign({}, cliPackage.dependencies), cliPackage.devDependencies), workspacePackage === null || workspacePackage === void 0 ? void 0 : workspacePackage.dependencies), workspacePackage === null || workspacePackage === void 0 ? void 0 : workspacePackage.devDependencies)));
            const versions = {};
            for (const name of packageNames) {
                if (PACKAGE_PATTERNS.some((p) => p.test(name))) {
                    versions[name] = this.getVersion(name, workspaceRequire, localRequire);
                }
            }
            const ngCliVersion = cliPackage.version;
            let angularCoreVersion = '';
            const angularSameAsCore = [];
            if (workspacePackage) {
                // Filter all angular versions that are the same as core.
                angularCoreVersion = versions['@angular/core'];
                if (angularCoreVersion) {
                    for (const [name, version] of Object.entries(versions)) {
                        if (version === angularCoreVersion && name.startsWith('@angular/')) {
                            angularSameAsCore.push(name.replace(/^@angular\//, ''));
                            delete versions[name];
                        }
                    }
                    // Make sure we list them in alphabetical order.
                    angularSameAsCore.sort();
                }
            }
            const namePad = ' '.repeat(Object.keys(versions).sort((a, b) => b.length - a.length)[0].length + 3);
            const asciiArt = `
     _                      _                 ____ _     ___
    / \\   _ __   __ _ _   _| | __ _ _ __     / ___| |   |_ _|
   / △ \\ | '_ \\ / _\` | | | | |/ _\` | '__|   | |   | |    | |
  / ___ \\| | | | (_| | |_| | | (_| | |      | |___| |___ | |
 /_/   \\_\\_| |_|\\__, |\\__,_|_|\\__,_|_|       \\____|_____|___|
                |___/
    `
                .split('\n')
                .map((x) => color_1.colors.red(x))
                .join('\n');
            logger.info(asciiArt);
            logger.info(`
      Angular CLI: ${ngCliVersion}
      Node: ${process.versions.node}${unsupportedNodeVersion ? ' (Unsupported)' : ''}
      Package Manager: ${packageManager.name} ${(_a = packageManager.version) !== null && _a !== void 0 ? _a : '<error>'}
      OS: ${process.platform} ${process.arch}

      Angular: ${angularCoreVersion}
      ... ${angularSameAsCore
                .reduce((acc, name) => {
                // Perform a simple word wrap around 60.
                if (acc.length == 0) {
                    return [name];
                }
                const line = acc[acc.length - 1] + ', ' + name;
                if (line.length > 60) {
                    acc.push(name);
                }
                else {
                    acc[acc.length - 1] = line;
                }
                return acc;
            }, [])
                .join('\n... ')}

      Package${namePad.slice(7)}Version
      -------${namePad.replace(/ /g, '-')}------------------
      ${Object.keys(versions)
                .map((module) => `${module}${namePad.slice(module.length)}${versions[module]}`)
                .sort()
                .join('\n')}
    `.replace(/^ {6}/gm, ''));
            if (unsupportedNodeVersion) {
                logger.warn(`Warning: The current version of Node (${process.versions.node}) is not supported by Angular.`);
            }
        });
    }
    getVersion(moduleName, workspaceRequire, localRequire) {
        let packageInfo;
        let cliOnly = false;
        // Try to find the package in the workspace
        try {
            packageInfo = workspaceRequire(`${moduleName}/package.json`);
        }
        catch (_a) { }
        // If not found, try to find within the CLI
        if (!packageInfo) {
            try {
                packageInfo = localRequire(`${moduleName}/package.json`);
                cliOnly = true;
            }
            catch (_b) { }
        }
        // If found, attempt to get the version
        if (packageInfo) {
            try {
                return packageInfo.version + (cliOnly ? ' (cli-only)' : '');
            }
            catch (_c) { }
        }
        return '<error>';
    }
}
exports.default = VersionCommandModule;
//# sourceMappingURL=cli.js.map