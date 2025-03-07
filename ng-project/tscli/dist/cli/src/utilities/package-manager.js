"use strict";
/**
 * @license
 * Copyright Google LLC All Rights Reserved.
 *
 * Use of this source code is governed by an MIT-style license that can be
 * found in the LICENSE file at https://angular.dev/license
 */
var __runInitializers = (this && this.__runInitializers) || function (thisArg, initializers, value) {
    var useValue = arguments.length > 2;
    for (var i = 0; i < initializers.length; i++) {
        value = useValue ? initializers[i].call(thisArg, value) : initializers[i].call(thisArg);
    }
    return useValue ? value : void 0;
};
var __esDecorate = (this && this.__esDecorate) || function (ctor, descriptorIn, decorators, contextIn, initializers, extraInitializers) {
    function accept(f) { if (f !== void 0 && typeof f !== "function") throw new TypeError("Function expected"); return f; }
    var kind = contextIn.kind, key = kind === "getter" ? "get" : kind === "setter" ? "set" : "value";
    var target = !descriptorIn && ctor ? contextIn["static"] ? ctor : ctor.prototype : null;
    var descriptor = descriptorIn || (target ? Object.getOwnPropertyDescriptor(target, contextIn.name) : {});
    var _, done = false;
    for (var i = decorators.length - 1; i >= 0; i--) {
        var context = {};
        for (var p in contextIn) context[p] = p === "access" ? {} : contextIn[p];
        for (var p in contextIn.access) context.access[p] = contextIn.access[p];
        context.addInitializer = function (f) { if (done) throw new TypeError("Cannot add initializers after decoration has completed"); extraInitializers.push(accept(f || null)); };
        var result = (0, decorators[i])(kind === "accessor" ? { get: descriptor.get, set: descriptor.set } : descriptor[key], context);
        if (kind === "accessor") {
            if (result === void 0) continue;
            if (result === null || typeof result !== "object") throw new TypeError("Object expected");
            if (_ = accept(result.get)) descriptor.get = _;
            if (_ = accept(result.set)) descriptor.set = _;
            if (_ = accept(result.init)) initializers.unshift(_);
        }
        else if (_ = accept(result)) {
            if (kind === "field") initializers.unshift(_);
            else descriptor[key] = _;
        }
    }
    if (target) Object.defineProperty(target, contextIn.name, descriptor);
    done = true;
};
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
exports.PackageManagerUtils = void 0;
const core_1 = require("@angular-devkit/core");
const node_child_process_1 = require("node:child_process");
const node_fs_1 = require("node:fs");
const node_os_1 = require("node:os");
const node_path_1 = require("node:path");
const PackageManager_1 = require("../../lib/config/PackageManager");
const config_1 = require("./config");
const memoize_1 = require("./memoize");
let PackageManagerUtils = (() => {
    var _a;
    let _instanceExtraInitializers = [];
    let _getVersion_decorators;
    let _getName_decorators;
    return _a = class PackageManagerUtils {
            constructor(context) {
                this.context = (__runInitializers(this, _instanceExtraInitializers), context);
            }
            /** Get the package manager name. */
            get name() {
                return this.getName();
            }
            /** Get the package manager version. */
            get version() {
                return this.getVersion(this.name);
            }
            /** Install a single package. */
            install(packageName_1) {
                return __awaiter(this, arguments, void 0, function* (packageName, save = true, extraArgs = [], cwd) {
                    const packageManagerArgs = this.getArguments();
                    const installArgs = [packageManagerArgs.install, packageName];
                    if (save === 'devDependencies') {
                        installArgs.push(packageManagerArgs.saveDev);
                    }
                    return this.run([...installArgs, ...extraArgs], { cwd, silent: true });
                });
            }
            /** Install all packages. */
            installAll() {
                return __awaiter(this, arguments, void 0, function* (extraArgs = [], cwd) {
                    const packageManagerArgs = this.getArguments();
                    const installArgs = [];
                    if (packageManagerArgs.installAll) {
                        installArgs.push(packageManagerArgs.installAll);
                    }
                    return this.run([...installArgs, ...extraArgs], { cwd, silent: true });
                });
            }
            /** Install a single package temporary. */
            installTemp(packageName, extraArgs) {
                return __awaiter(this, void 0, void 0, function* () {
                    const tempPath = yield node_fs_1.promises.mkdtemp((0, node_path_1.join)((0, node_fs_1.realpathSync)((0, node_os_1.tmpdir)()), 'angular-cli-packages-'));
                    // clean up temp directory on process exit
                    process.on('exit', () => {
                        try {
                            (0, node_fs_1.rmSync)(tempPath, { recursive: true, maxRetries: 3 });
                        }
                        catch (_b) { }
                    });
                    // NPM will warn when a `package.json` is not found in the install directory
                    // Example:
                    // npm WARN enoent ENOENT: no such file or directory, open '/tmp/.ng-temp-packages-84Qi7y/package.json'
                    // npm WARN .ng-temp-packages-84Qi7y No description
                    // npm WARN .ng-temp-packages-84Qi7y No repository field.
                    // npm WARN .ng-temp-packages-84Qi7y No license field.
                    // While we can use `npm init -y` we will end up needing to update the 'package.json' anyways
                    // because of missing fields.
                    yield node_fs_1.promises.writeFile((0, node_path_1.join)(tempPath, 'package.json'), JSON.stringify({
                        name: 'temp-cli-install',
                        description: 'temp-cli-install',
                        repository: 'temp-cli-install',
                        license: 'MIT',
                    }));
                    // setup prefix/global modules path
                    const packageManagerArgs = this.getArguments();
                    const tempNodeModules = (0, node_path_1.join)(tempPath, 'node_modules');
                    // Yarn will not append 'node_modules' to the path
                    const prefixPath = this.name === PackageManager_1.PackageManager.Yarn ? tempNodeModules : tempPath;
                    const installArgs = [
                        ...(extraArgs !== null && extraArgs !== void 0 ? extraArgs : []),
                        `${packageManagerArgs.prefix}="${prefixPath}"`,
                        packageManagerArgs.noLockfile,
                    ];
                    return {
                        success: yield this.install(packageName, true, installArgs, tempPath),
                        tempNodeModules,
                    };
                });
            }
            getArguments() {
                switch (this.name) {
                    case PackageManager_1.PackageManager.Yarn:
                        return {
                            saveDev: '--dev',
                            install: 'add',
                            prefix: '--modules-folder',
                            noLockfile: '--no-lockfile',
                        };
                    case PackageManager_1.PackageManager.Pnpm:
                        return {
                            saveDev: '--save-dev',
                            install: 'add',
                            installAll: 'install',
                            prefix: '--prefix',
                            noLockfile: '--no-lockfile',
                        };
                    case PackageManager_1.PackageManager.Bun:
                        return {
                            saveDev: '--development',
                            install: 'add',
                            installAll: 'install',
                            prefix: '--cwd',
                            noLockfile: '',
                        };
                    default:
                        return {
                            saveDev: '--save-dev',
                            install: 'install',
                            installAll: 'install',
                            prefix: '--prefix',
                            noLockfile: '--no-package-lock',
                        };
                }
            }
            run(args_1) {
                return __awaiter(this, arguments, void 0, function* (args, options = {}) {
                    const { cwd = process.cwd(), silent = false } = options;
                    return new Promise((resolve) => {
                        var _b, _c;
                        const bufferedOutput = [];
                        const childProcess = (0, node_child_process_1.spawn)(this.name, args, {
                            // Always pipe stderr to allow for failures to be reported
                            stdio: silent ? ['ignore', 'ignore', 'pipe'] : 'pipe',
                            shell: true,
                            cwd,
                        }).on('close', (code) => {
                            if (code === 0) {
                                resolve(true);
                            }
                            else {
                                bufferedOutput.forEach(({ stream, data }) => stream.write(data));
                                resolve(false);
                            }
                        });
                        (_b = childProcess.stdout) === null || _b === void 0 ? void 0 : _b.on('data', (data) => bufferedOutput.push({ stream: process.stdout, data: data }));
                        (_c = childProcess.stderr) === null || _c === void 0 ? void 0 : _c.on('data', (data) => bufferedOutput.push({ stream: process.stderr, data: data }));
                    });
                });
            }
            getVersion(name) {
                try {
                    return (0, node_child_process_1.execSync)(`${name} --version`, {
                        encoding: 'utf8',
                        stdio: ['ignore', 'pipe', 'ignore'],
                        env: Object.assign(Object.assign({}, process.env), { 
                            //  NPM updater notifier will prevents the child process from closing until it timeout after 3 minutes.
                            NO_UPDATE_NOTIFIER: '1', NPM_CONFIG_UPDATE_NOTIFIER: 'false' }),
                    }).trim();
                }
                catch (_b) {
                    return undefined;
                }
            }
            getName() {
                const packageManager = this.getConfiguredPackageManager();
                if (packageManager) {
                    return packageManager;
                }
                const hasNpmLock = this.hasLockfile(PackageManager_1.PackageManager.Npm);
                const hasYarnLock = this.hasLockfile(PackageManager_1.PackageManager.Yarn);
                const hasPnpmLock = this.hasLockfile(PackageManager_1.PackageManager.Pnpm);
                const hasBunLock = this.hasLockfile(PackageManager_1.PackageManager.Bun);
                // PERF NOTE: `this.getVersion` spawns the package a the child_process which can take around ~300ms at times.
                // Therefore, we should only call this method when needed. IE: don't call `this.getVersion(PackageManager.Pnpm)` unless truly needed.
                // The result of this method is not stored in a variable because it's memoized.
                if (hasNpmLock) {
                    // Has NPM lock file.
                    if (!hasYarnLock && !hasPnpmLock && !hasBunLock && this.getVersion(PackageManager_1.PackageManager.Npm)) {
                        // Only NPM lock file and NPM binary is available.
                        return PackageManager_1.PackageManager.Npm;
                    }
                }
                else {
                    // No NPM lock file.
                    if (hasYarnLock && this.getVersion(PackageManager_1.PackageManager.Yarn)) {
                        // Yarn lock file and Yarn binary is available.
                        return PackageManager_1.PackageManager.Yarn;
                    }
                    else if (hasPnpmLock && this.getVersion(PackageManager_1.PackageManager.Pnpm)) {
                        // PNPM lock file and PNPM binary is available.
                        return PackageManager_1.PackageManager.Pnpm;
                    }
                    else if (hasBunLock && this.getVersion(PackageManager_1.PackageManager.Bun)) {
                        // Bun lock file and Bun binary is available.
                        return PackageManager_1.PackageManager.Bun;
                    }
                }
                if (!this.getVersion(PackageManager_1.PackageManager.Npm)) {
                    // Doesn't have NPM installed.
                    const hasYarn = !!this.getVersion(PackageManager_1.PackageManager.Yarn);
                    const hasPnpm = !!this.getVersion(PackageManager_1.PackageManager.Pnpm);
                    const hasBun = !!this.getVersion(PackageManager_1.PackageManager.Bun);
                    if (hasYarn && !hasPnpm && !hasBun) {
                        return PackageManager_1.PackageManager.Yarn;
                    }
                    else if (hasPnpm && !hasYarn && !hasBun) {
                        return PackageManager_1.PackageManager.Pnpm;
                    }
                    else if (hasBun && !hasYarn && !hasPnpm) {
                        return PackageManager_1.PackageManager.Bun;
                    }
                }
                // TODO: This should eventually inform the user of ambiguous package manager usage.
                //       Potentially with a prompt to choose and optionally set as the default.
                return PackageManager_1.PackageManager.Npm;
            }
            hasLockfile(packageManager) {
                let lockfileName;
                switch (packageManager) {
                    case PackageManager_1.PackageManager.Yarn:
                        lockfileName = 'yarn.lock';
                        break;
                    case PackageManager_1.PackageManager.Pnpm:
                        lockfileName = 'pnpm-lock.yaml';
                        break;
                    case PackageManager_1.PackageManager.Bun:
                        lockfileName = 'bun.lockb';
                        break;
                    case PackageManager_1.PackageManager.Npm:
                    default:
                        lockfileName = 'package-lock.json';
                        break;
                }
                return (0, node_fs_1.existsSync)((0, node_path_1.join)(this.context.root, lockfileName));
            }
            getConfiguredPackageManager() {
                var _b;
                const getPackageManager = (source) => {
                    if (source && (0, core_1.isJsonObject)(source)) {
                        const value = source['packageManager'];
                        if (typeof value === 'string') {
                            return value;
                        }
                    }
                    return undefined;
                };
                let result;
                const { workspace: localWorkspace, globalConfiguration: globalWorkspace } = this.context;
                if (localWorkspace) {
                    const project = (0, config_1.getProjectByCwd)(localWorkspace);
                    if (project) {
                        result = getPackageManager((_b = localWorkspace.projects.get(project)) === null || _b === void 0 ? void 0 : _b.extensions['cli']);
                    }
                    result !== null && result !== void 0 ? result : (result = getPackageManager(localWorkspace.extensions['cli']));
                }
                if (!result) {
                    result = getPackageManager(globalWorkspace.extensions['cli']);
                }
                return result;
            }
        },
        (() => {
            const _metadata = typeof Symbol === "function" && Symbol.metadata ? Object.create(null) : void 0;
            _getVersion_decorators = [memoize_1.memoize];
            _getName_decorators = [memoize_1.memoize];
            __esDecorate(_a, null, _getVersion_decorators, { kind: "method", name: "getVersion", static: false, private: false, access: { has: obj => "getVersion" in obj, get: obj => obj.getVersion }, metadata: _metadata }, null, _instanceExtraInitializers);
            __esDecorate(_a, null, _getName_decorators, { kind: "method", name: "getName", static: false, private: false, access: { has: obj => "getName" in obj, get: obj => obj.getName }, metadata: _metadata }, null, _instanceExtraInitializers);
            if (_metadata) Object.defineProperty(_a, Symbol.metadata, { enumerable: true, configurable: true, writable: true, value: _metadata });
        })(),
        _a;
})();
exports.PackageManagerUtils = PackageManagerUtils;
//# sourceMappingURL=package-manager.js.map