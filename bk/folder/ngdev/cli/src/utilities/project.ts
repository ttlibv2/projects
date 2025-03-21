import { normalize } from '@angular-devkit/core';
import * as fs from 'node:fs';
import * as os from 'node:os';
import * as path from 'node:path';
import { findUp } from './find-up';
import { CLI_PKG, ROOT_JSON } from './environment';

interface PackageDependencies {
  dependencies?: Record<string, string>;
  devDependencies?: Record<string, string>;
}

export function findWorkspaceFile(currentDirectory = process.cwd()): string | null {
  const configFilePath = findUp(ROOT_JSON, currentDirectory);
  if (configFilePath === null)   return null;
  

  const possibleDir = path.dirname(configFilePath);

  const homedir = os.homedir();
  if (normalize(possibleDir) === normalize(homedir)) {
    const packageJsonPath = path.join(possibleDir, 'package.json');

    try {
      const packageJsonText = fs.readFileSync(packageJsonPath, 'utf-8');
      const packageJson = JSON.parse(packageJsonText) as PackageDependencies;
      if (!containsCliDep(packageJson)) {
        // No CLI dependency
        return null;
      }
    } catch {
      // No or invalid package.json
      return null;
    }
  }

  return configFilePath;
}

function containsCliDep(obj?: PackageDependencies): boolean {
  const pkgName = CLI_PKG;
  if (!obj)   return false;
  else return !!(obj.dependencies?.[pkgName] || obj.devDependencies?.[pkgName]);
}