"use strict";
Object.defineProperty(exports, "__esModule", { value: true });
exports.readPackageJson = readPackageJson;
exports.findPackageJson = findPackageJson;
exports.getProjectDependencies = getProjectDependencies;
const fs = require("node:fs");
const node_path_1 = require("node:path");
const resolve = require("resolve");
function getAllDependencies(pkg) {
    return new Set([
        ...Object.entries(pkg.dependencies || []),
        ...Object.entries(pkg.devDependencies || []),
        ...Object.entries(pkg.peerDependencies || []),
        ...Object.entries(pkg.optionalDependencies || []),
    ]);
}
async function readPackageJson(packageJsonPath) {
    try {
        return JSON.parse((await fs.promises.readFile(packageJsonPath)).toString());
    }
    catch {
        return undefined;
    }
}
function findPackageJson(workspaceDir, packageName) {
    try {
        // avoid require.resolve here, see: https://github.com/angular/angular-cli/pull/18610#issuecomment-681980185
        const packageJsonPath = resolve.sync(`${packageName}/package.json`, { basedir: workspaceDir });
        return packageJsonPath;
    }
    catch {
        return undefined;
    }
}
async function getProjectDependencies(dir) {
    const pkg = await readPackageJson((0, node_path_1.join)(dir, 'package.json'));
    if (!pkg) {
        throw new Error('Could not find package.json');
    }
    const results = new Map();
    for (const [name, version] of getAllDependencies(pkg)) {
        const packageJsonPath = findPackageJson(dir, name);
        if (!packageJsonPath) {
            continue;
        }
        results.set(name, {
            name,
            version,
            path: (0, node_path_1.dirname)(packageJsonPath),
            package: await readPackageJson(packageJsonPath),
        });
    }
    return results;
}
//# sourceMappingURL=package-tree.js.map