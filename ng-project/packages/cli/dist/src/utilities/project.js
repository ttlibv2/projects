"use strict";
Object.defineProperty(exports, "__esModule", { value: true });
exports.findWorkspaceFile = findWorkspaceFile;
const core_1 = require("@angular-devkit/core");
const fs = require("node:fs");
const os = require("node:os");
const path = require("node:path");
const find_up_1 = require("./find-up");
function findWorkspaceFile(currentDirectory = process.cwd()) {
    const possibleConfigFiles = ['angular.json', '.angular.json'];
    const configFilePath = (0, find_up_1.findUp)(possibleConfigFiles, currentDirectory);
    if (configFilePath === null) {
        return null;
    }
    const possibleDir = path.dirname(configFilePath);
    const homedir = os.homedir();
    if ((0, core_1.normalize)(possibleDir) === (0, core_1.normalize)(homedir)) {
        const packageJsonPath = path.join(possibleDir, 'package.json');
        try {
            const packageJsonText = fs.readFileSync(packageJsonPath, 'utf-8');
            const packageJson = JSON.parse(packageJsonText);
            if (!containsCliDep(packageJson)) {
                // No CLI dependency
                return null;
            }
        }
        catch {
            // No or invalid package.json
            return null;
        }
    }
    return configFilePath;
}
function containsCliDep(obj) {
    const pkgName = '@angular/lib';
    if (!obj) {
        return false;
    }
    return !!(obj.dependencies?.[pkgName] || obj.devDependencies?.[pkgName]);
}
//# sourceMappingURL=project.js.map