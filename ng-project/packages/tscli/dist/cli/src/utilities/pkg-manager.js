"use strict";
Object.defineProperty(exports, "__esModule", { value: true });
exports.PackageManagerUtils = exports.PackageManager = void 0;
const core_1 = require("@angular-devkit/core");
const node_child_process_1 = require("node:child_process");
const node_fs_1 = require("node:fs");
const node_os_1 = require("node:os");
const node_path_1 = require("node:path");
const config_1 = require("./config");
var PackageManager;
(function (PackageManager) {
    PackageManager["Npm"] = "npm";
    PackageManager["Cnpm"] = "cnpm";
    PackageManager["Yarn"] = "yarn";
    PackageManager["Pnpm"] = "pnpm";
    PackageManager["Bun"] = "bun";
})(PackageManager || (exports.PackageManager = PackageManager = {}));
class PackageManagerUtils {
    context;
    constructor(context) {
        this.context = context;
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
    async install(packageName, save = true, extraArgs = [], cwd) {
        const packageManagerArgs = this.getArguments();
        const installArgs = [packageManagerArgs.install, packageName];
        if (save === 'devDependencies') {
            installArgs.push(packageManagerArgs.saveDev);
        }
        return this.run([...installArgs, ...extraArgs], { cwd, silent: true });
    }
    /** Install all packages. */
    async installAll(extraArgs = [], cwd) {
        const packageManagerArgs = this.getArguments();
        const installArgs = [];
        if (packageManagerArgs.installAll) {
            installArgs.push(packageManagerArgs.installAll);
        }
        return this.run([...installArgs, ...extraArgs], { cwd, silent: true });
    }
    /** Install a single package temporary. */
    async installTemp(packageName, extraArgs) {
        const tempPath = await node_fs_1.promises.mkdtemp((0, node_path_1.join)((0, node_fs_1.realpathSync)((0, node_os_1.tmpdir)()), 'angular-cli-packages-'));
        // clean up temp directory on process exit
        process.on('exit', () => {
            try {
                (0, node_fs_1.rmSync)(tempPath, { recursive: true, maxRetries: 3 });
            }
            catch { }
        });
        // NPM will warn when a `package.json` is not found in the install directory
        // Example:
        // npm WARN enoent ENOENT: no such file or directory, open '/tmp/.ng-temp-packages-84Qi7y/package.json'
        // npm WARN .ng-temp-packages-84Qi7y No description
        // npm WARN .ng-temp-packages-84Qi7y No repository field.
        // npm WARN .ng-temp-packages-84Qi7y No license field.
        // While we can use `npm init -y` we will end up needing to update the 'package.json' anyways
        // because of missing fields.
        await node_fs_1.promises.writeFile((0, node_path_1.join)(tempPath, 'package.json'), JSON.stringify({
            name: 'temp-cli-install',
            description: 'temp-cli-install',
            repository: 'temp-cli-install',
            license: 'MIT',
        }));
        // setup prefix/global modules path
        const packageManagerArgs = this.getArguments();
        const tempNodeModules = (0, node_path_1.join)(tempPath, 'node_modules');
        // Yarn will not append 'node_modules' to the path
        const prefixPath = this.name === PackageManager.Yarn ? tempNodeModules : tempPath;
        const installArgs = [
            ...(extraArgs ?? []),
            `${packageManagerArgs.prefix}="${prefixPath}"`,
            packageManagerArgs.noLockfile,
        ];
        return {
            success: await this.install(packageName, true, installArgs, tempPath),
            tempNodeModules,
        };
    }
    getArguments() {
        switch (this.name) {
            case PackageManager.Yarn:
                return {
                    saveDev: '--dev',
                    install: 'add',
                    prefix: '--modules-folder',
                    noLockfile: '--no-lockfile',
                };
            case PackageManager.Pnpm:
                return {
                    saveDev: '--save-dev',
                    install: 'add',
                    installAll: 'install',
                    prefix: '--prefix',
                    noLockfile: '--no-lockfile',
                };
            case PackageManager.Bun:
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
    async run(args, options = {}) {
        const { cwd = process.cwd(), silent = false } = options;
        return new Promise((resolve) => {
            const bufferedOutput = [];
            const childProcess = (0, node_child_process_1.spawn)(`${this.name}`, args, {
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
            childProcess.stdout?.on('data', (data) => bufferedOutput.push({ stream: process.stdout, data: data }));
            childProcess.stderr?.on('data', (data) => bufferedOutput.push({ stream: process.stderr, data: data }));
        });
    }
    //@memoize
    getVersion(name) {
        try {
            return (0, node_child_process_1.execSync)(`${name} --version`, {
                encoding: 'utf8',
                stdio: ['ignore', 'pipe', 'ignore'],
                env: {
                    ...process.env,
                    //  NPM updater notifier will prevents the child process from closing until it timeout after 3 minutes.
                    NO_UPDATE_NOTIFIER: '1',
                    NPM_CONFIG_UPDATE_NOTIFIER: 'false',
                },
            }).trim();
        }
        catch {
            return undefined;
        }
    }
    //@memoize
    getName() {
        const packageManager = this.getConfiguredPackageManager();
        if (packageManager) {
            return packageManager;
        }
        const hasNpmLock = this.hasLockfile(PackageManager.Npm);
        const hasYarnLock = this.hasLockfile(PackageManager.Yarn);
        const hasPnpmLock = this.hasLockfile(PackageManager.Pnpm);
        const hasBunLock = this.hasLockfile(PackageManager.Bun);
        // PERF NOTE: `this.getVersion` spawns the package a the child_process which can take around ~300ms at times.
        // Therefore, we should only call this method when needed. IE: don't call `this.getVersion(PackageManager.Pnpm)` unless truly needed.
        // The result of this method is not stored in a variable because it's memoized.
        if (hasNpmLock) {
            // Has NPM lock file.
            if (!hasYarnLock && !hasPnpmLock && !hasBunLock && this.getVersion(PackageManager.Npm)) {
                // Only NPM lock file and NPM binary is available.
                return PackageManager.Npm;
            }
        }
        else {
            // No NPM lock file.
            if (hasYarnLock && this.getVersion(PackageManager.Yarn)) {
                // Yarn lock file and Yarn binary is available.
                return PackageManager.Yarn;
            }
            else if (hasPnpmLock && this.getVersion(PackageManager.Pnpm)) {
                // PNPM lock file and PNPM binary is available.
                return PackageManager.Pnpm;
            }
            else if (hasBunLock && this.getVersion(PackageManager.Bun)) {
                // Bun lock file and Bun binary is available.
                return PackageManager.Bun;
            }
        }
        if (!this.getVersion(PackageManager.Npm)) {
            // Doesn't have NPM installed.
            const hasYarn = !!this.getVersion(PackageManager.Yarn);
            const hasPnpm = !!this.getVersion(PackageManager.Pnpm);
            const hasBun = !!this.getVersion(PackageManager.Bun);
            if (hasYarn && !hasPnpm && !hasBun) {
                return PackageManager.Yarn;
            }
            else if (hasPnpm && !hasYarn && !hasBun) {
                return PackageManager.Pnpm;
            }
            else if (hasBun && !hasYarn && !hasPnpm) {
                return PackageManager.Bun;
            }
        }
        // TODO: This should eventually inform the user of ambiguous package manager usage.
        //       Potentially with a prompt to choose and optionally set as the default.
        return PackageManager.Npm;
    }
    hasLockfile(packageManager) {
        let lockfileName;
        switch (packageManager) {
            case PackageManager.Yarn:
                lockfileName = 'yarn.lock';
                break;
            case PackageManager.Pnpm:
                lockfileName = 'pnpm-lock.yaml';
                break;
            case PackageManager.Bun:
                lockfileName = 'bun.lockb';
                break;
            case PackageManager.Npm:
            default:
                lockfileName = 'package-lock.json';
                break;
        }
        return (0, node_fs_1.existsSync)((0, node_path_1.join)(this.context.root, lockfileName));
    }
    getConfiguredPackageManager() {
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
                result = getPackageManager(localWorkspace.projects.get(project)?.extensions['cli']);
            }
            result ??= getPackageManager(localWorkspace.extensions['cli']);
        }
        if (!result) {
            result = getPackageManager(globalWorkspace.extensions['cli']);
        }
        return result;
    }
}
exports.PackageManagerUtils = PackageManagerUtils;
