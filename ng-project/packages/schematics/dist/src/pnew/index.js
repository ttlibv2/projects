"use strict";
Object.defineProperty(exports, "__esModule", { value: true });
exports.default = default_1;
const schematics_1 = require("@angular-devkit/schematics");
const last_version_1 = require("../utility/last-version");
const tasks_1 = require("@angular-devkit/schematics/tasks");
const packageManager = 'pnpm';
function default_1(options) {
    if (!options.directory) {
        // If scoped project (i.e. "@foo/bar"), convert directory to "foo/bar".
        options.directory = options.name.startsWith('@') ? options.name.slice(1) : options.name;
    }
    options.appsRoot = options.appsRoot || 'apps';
    options.libsRoot = options.libsRoot || 'packages';
    return (0, schematics_1.chain)([
        copyFiles(options),
        (tree, context) => {
            let packageTask;
            if (!options.skipInstall) {
                packageTask = context.addTask(new tasks_1.NodePackageInstallTask({
                    workingDirectory: options.directory,
                    packageManager: packageManager,
                }));
            }
            if (!options.skipGit) {
                const commit = typeof options.commit == 'object' ? options.commit : options.commit ? {} : false;
                context.addTask(new tasks_1.RepositoryInitializerTask(options.directory, commit), packageTask ? [packageTask] : []);
            }
        }
    ]);
}
function copyFiles(options) {
    return (0, schematics_1.mergeWith)((0, schematics_1.apply)((0, schematics_1.url)('./files'), [
        (0, schematics_1.applyTemplates)({ ...options, utils: schematics_1.strings, dot: '.', latestVersions: last_version_1.latestVersions, pnpmVersion: '10.7.0' }),
        (0, schematics_1.move)(options.directory)
    ]));
}
//# sourceMappingURL=index.js.map