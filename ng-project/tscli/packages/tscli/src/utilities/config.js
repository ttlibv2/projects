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
exports.NgxWorkspace = exports.workspaceSchemaPath = void 0;
exports.getWorkspace = getWorkspace;
exports.getWorkspaceRaw = getWorkspaceRaw;
exports.validateWorkspace = validateWorkspace;
exports.getProjectByCwd = getProjectByCwd;
exports.getConfiguredPackageManager = getConfiguredPackageManager;
exports.getSchematicDefaults = getSchematicDefaults;
exports.isWarningEnabled = isWarningEnabled;
const core_1 = require("@angular-devkit/core");
const node_fs_1 = require("node:fs");
const os = require("node:os");
const path = require("node:path");
const find_up_1 = require("./find-up");
const json_file_1 = require("./json-file");
const constant_1 = require("./constant");
function isJsonObject(value) {
    return value !== undefined && core_1.json.isJsonObject(value);
}
function createWorkspaceHost() {
    return {
        readFile(path) {
            return node_fs_1.promises.readFile(path, 'utf-8');
        },
        writeFile(path, data) {
            return __awaiter(this, void 0, void 0, function* () {
                yield node_fs_1.promises.writeFile(path, data);
            });
        },
        isDirectory(path) {
            return __awaiter(this, void 0, void 0, function* () {
                try {
                    const stats = yield node_fs_1.promises.stat(path);
                    return stats.isDirectory();
                }
                catch (_a) {
                    return false;
                }
            });
        },
        isFile(path) {
            return __awaiter(this, void 0, void 0, function* () {
                try {
                    const stats = yield node_fs_1.promises.stat(path);
                    return stats.isFile();
                }
                catch (_a) {
                    return false;
                }
            });
        },
    };
}
exports.workspaceSchemaPath = path.join(__dirname, '../../lib/config/schema.json');
const configNames = constant_1.monorepoCfgFileName; //['angular.json', '.angular.json'];
const globalFileName = constant_1.globalFileName;
const defaultGlobalFilePath = path.join(os.homedir(), globalFileName);
function xdgConfigHome(home, configFile) {
    // https://specifications.freedesktop.org/basedir-spec/basedir-spec-latest.html
    const xdgConfigHome = process.env['XDG_CONFIG_HOME'] || path.join(home, '.config');
    const xdgAngularHome = path.join(xdgConfigHome, 'angular');
    return configFile ? path.join(xdgAngularHome, configFile) : xdgAngularHome;
}
function xdgConfigHomeOld(home) {
    // Check the configuration files in the old location that should be:
    // - $XDG_CONFIG_HOME/.angular-config.json (if XDG_CONFIG_HOME is set)
    // - $HOME/.config/angular/.angular-config.json (otherwise)
    const p = process.env['XDG_CONFIG_HOME'] || path.join(home, '.config', 'angular');
    return path.join(p, '.angular-config.json');
}
function projectFilePath(projectPath) {
    // Find the configuration, either where specified, in the Angular CLI project
    // (if it's in node_modules) or from the current process.
    return ((projectPath && (0, find_up_1.findUp)(configNames, projectPath)) ||
        (0, find_up_1.findUp)(configNames, process.cwd()) ||
        (0, find_up_1.findUp)(configNames, __dirname));
}
function globalFilePath() {
    const home = os.homedir();
    if (!home) {
        return null;
    }
    // follow XDG Base Directory spec
    // note that createGlobalSettings() will continue creating
    // global file in home directory, with this user will have
    // choice to move change its location to meet XDG convention
    const xdgConfig = xdgConfigHome(home, 'config.json');
    if ((0, node_fs_1.existsSync)(xdgConfig)) {
        return xdgConfig;
    }
    // NOTE: This check is for the old configuration location, for more
    // information see https://github.com/angular/angular-cli/pull/20556
    const xdgConfigOld = xdgConfigHomeOld(home);
    if ((0, node_fs_1.existsSync)(xdgConfigOld)) {
        /* eslint-disable no-console */
        console.warn(`Old configuration location detected: ${xdgConfigOld}\n` +
            `Please move the file to the new location ~/.config/angular/config.json`);
        return xdgConfigOld;
    }
    if ((0, node_fs_1.existsSync)(defaultGlobalFilePath)) {
        return defaultGlobalFilePath;
    }
    return null;
}
class NgxWorkspace {
    constructor(workspace, filePath) {
        this.workspace = workspace;
        this.filePath = filePath;
        this.basePath = path.dirname(filePath);
    }
    get extensions() {
        return this.workspace.extensions;
    }
    get projects() {
        return this.workspace.projects;
    }
    // Temporary helper functions to support refactoring
    // eslint-disable-next-line @typescript-eslint/no-explicit-any
    getCli() {
        return this.workspace.extensions['cli'];
    }
    // eslint-disable-next-line @typescript-eslint/no-explicit-any
    getProjectCli(projectName) {
        const project = this.workspace.projects.get(projectName);
        return project === null || project === void 0 ? void 0 : project.extensions['cli'];
    }
    save() {
        return core_1.workspaces.writeWorkspace(this.workspace, createWorkspaceHost(), this.filePath, core_1.workspaces.WorkspaceFormat.JSON);
    }
    static load(workspaceFilePath) {
        return __awaiter(this, void 0, void 0, function* () {
            const result = yield core_1.workspaces.readWorkspace(workspaceFilePath, createWorkspaceHost(), core_1.workspaces.WorkspaceFormat.JSON);
            return new NgxWorkspace(result.workspace, workspaceFilePath);
        });
    }
}
exports.NgxWorkspace = NgxWorkspace;
const cachedWorkspaces = new Map();
function getWorkspace(level) {
    return __awaiter(this, void 0, void 0, function* () {
        if (cachedWorkspaces.has(level)) {
            return cachedWorkspaces.get(level);
        }
        const configPath = level === 'local' ? projectFilePath() : globalFilePath();
        //console.log(`getWorkspace: `, configPath);
        if (!configPath) {
            if (level === 'global') {
                // Unlike a local config, a global config is not mandatory.
                // So we create an empty one in memory and keep it as such until it has been modified and saved.
                const globalWorkspace = new NgxWorkspace({ extensions: {}, projects: new core_1.workspaces.ProjectDefinitionCollection() }, defaultGlobalFilePath);
                cachedWorkspaces.set(level, globalWorkspace);
                return globalWorkspace;
            }
            cachedWorkspaces.set(level, undefined);
            return undefined;
        }
        try {
            const workspace = yield NgxWorkspace.load(configPath);
            cachedWorkspaces.set(level, workspace);
            return workspace;
        }
        catch (error) {
            throw new Error(`Workspace config file cannot be loaded: ${configPath}` +
                `\n${error instanceof Error ? error.message : error}`);
        }
    });
}
/**
 * This method will load the workspace configuration in raw JSON format.
 * When `level` is `global` and file doesn't exists, it will be created.
 *
 * NB: This method is intended to be used only for `ng config`.
 */
function getWorkspaceRaw() {
    return __awaiter(this, arguments, void 0, function* (level = 'local') {
        let configPath = level === 'local' ? projectFilePath() : globalFilePath();
        if (!configPath) {
            if (level === 'global') {
                configPath = defaultGlobalFilePath;
                // Config doesn't exist, force create it.
                const globalWorkspace = yield getWorkspace('global');
                yield globalWorkspace.save();
            }
            else {
                return [null, null];
            }
        }
        return [new json_file_1.JSONFile(configPath), configPath];
    });
}
function validateWorkspace(data, isGlobal) {
    return __awaiter(this, void 0, void 0, function* () {
        const schema = (0, json_file_1.readAndParseJson)(exports.workspaceSchemaPath);
        // We should eventually have a dedicated global config schema and use that to validate.
        const schemaToValidate = isGlobal
            ? {
                '$ref': '#/definitions/global',
                definitions: schema['definitions'],
            }
            : schema;
        const { formats } = yield Promise.resolve().then(() => require('@angular-devkit/schematics'));
        const registry = new core_1.json.schema.CoreSchemaRegistry(formats.standardFormats);
        const validator = yield registry.compile(schemaToValidate);
        const { success, errors } = yield validator(data);
        if (!success) {
            throw new core_1.json.schema.SchemaValidationException(errors);
        }
    });
}
function findProjectByPath(workspace, location) {
    const isInside = (base, potential) => {
        const absoluteBase = path.resolve(workspace.basePath, base);
        const absolutePotential = path.resolve(workspace.basePath, potential);
        const relativePotential = path.relative(absoluteBase, absolutePotential);
        if (!relativePotential.startsWith('..') && !path.isAbsolute(relativePotential)) {
            return true;
        }
        return false;
    };
    const projects = Array.from(workspace.projects)
        .map(([name, project]) => [project.root, name])
        .filter((tuple) => isInside(tuple[0], location))
        // Sort tuples by depth, with the deeper ones first. Since the first member is a path and
        // we filtered all invalid paths, the longest will be the deepest (and in case of equality
        // the sort is stable and the first declared project will win).
        .sort((a, b) => b[0].length - a[0].length);
    if (projects.length === 0) {
        return null;
    }
    else if (projects.length > 1) {
        const found = new Set();
        const sameRoots = projects.filter((v) => {
            if (!found.has(v[0])) {
                found.add(v[0]);
                return false;
            }
            return true;
        });
        if (sameRoots.length > 0) {
            // Ambiguous location - cannot determine a project
            return null;
        }
    }
    return projects[0][1];
}
function getProjectByCwd(workspace) {
    if (workspace.projects.size === 1) {
        // If there is only one project, return that one.
        return Array.from(workspace.projects.keys())[0];
    }
    const project = findProjectByPath(workspace, process.cwd());
    if (project) {
        return project;
    }
    return null;
}
function getConfiguredPackageManager() {
    return __awaiter(this, void 0, void 0, function* () {
        var _a;
        const getPackageManager = (source) => {
            if (isJsonObject(source)) {
                const value = source['packageManager'];
                if (value && typeof value === 'string') {
                    return value;
                }
            }
            return null;
        };
        let result = null;
        const workspace = yield getWorkspace('local');
        if (workspace) {
            const project = getProjectByCwd(workspace);
            if (project) {
                result = getPackageManager((_a = workspace.projects.get(project)) === null || _a === void 0 ? void 0 : _a.extensions['cli']);
            }
            result !== null && result !== void 0 ? result : (result = getPackageManager(workspace.extensions['cli']));
        }
        if (!result) {
            const globalOptions = yield getWorkspace('global');
            result = getPackageManager(globalOptions === null || globalOptions === void 0 ? void 0 : globalOptions.extensions['cli']);
        }
        return result;
    });
}
function getSchematicDefaults(collection, schematic, project) {
    return __awaiter(this, void 0, void 0, function* () {
        var _a;
        const result = {};
        const mergeOptions = (source) => {
            if (isJsonObject(source)) {
                // Merge options from the qualified name
                Object.assign(result, source[`${collection}:${schematic}`]);
                // Merge options from nested collection schematics
                const collectionOptions = source[collection];
                if (isJsonObject(collectionOptions)) {
                    Object.assign(result, collectionOptions[schematic]);
                }
            }
        };
        // Global level schematic options
        const globalOptions = yield getWorkspace('global');
        mergeOptions(globalOptions === null || globalOptions === void 0 ? void 0 : globalOptions.extensions['schematics']);
        const workspace = yield getWorkspace('local');
        if (workspace) {
            // Workspace level schematic options
            mergeOptions(workspace.extensions['schematics']);
            project = project || getProjectByCwd(workspace);
            if (project) {
                // Project level schematic options
                mergeOptions((_a = workspace.projects.get(project)) === null || _a === void 0 ? void 0 : _a.extensions['schematics']);
            }
        }
        return result;
    });
}
function isWarningEnabled(warning) {
    return __awaiter(this, void 0, void 0, function* () {
        var _a;
        const getWarning = (source) => {
            if (isJsonObject(source)) {
                const warnings = source['warnings'];
                if (isJsonObject(warnings)) {
                    const value = warnings[warning];
                    if (typeof value == 'boolean') {
                        return value;
                    }
                }
            }
        };
        let result;
        const workspace = yield getWorkspace('local');
        if (workspace) {
            const project = getProjectByCwd(workspace);
            if (project) {
                result = getWarning((_a = workspace.projects.get(project)) === null || _a === void 0 ? void 0 : _a.extensions['cli']);
            }
            result = result !== null && result !== void 0 ? result : getWarning(workspace.extensions['cli']);
        }
        if (result === undefined) {
            const globalOptions = yield getWorkspace('global');
            result = getWarning(globalOptions === null || globalOptions === void 0 ? void 0 : globalOptions.extensions['cli']);
        }
        // All warnings are enabled by default
        return result !== null && result !== void 0 ? result : true;
    });
}
//# sourceMappingURL=config.js.map