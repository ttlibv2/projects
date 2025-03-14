import { access, readFile } from 'node:fs/promises';
import path from 'node:path';
import { logger } from './logger';

export async function isDependencyInstalled(dependencyName: string, startDir: string) {
  try {
    // Read the package.json file
    const packageJsonPath = await findPackageJson(startDir);
    if (typeof packageJsonPath === 'boolean' || packageJsonPath === undefined) {
      return false;
    }
    const packageJsonString = await readFile(packageJsonPath as string, 'utf8');
    const packageJson = JSON.parse(packageJsonString);

    // Check if the dependency exists in any of the relevant objects
    const isInstalled =
      (packageJson.dependencies && packageJson.dependencies[dependencyName]) ||
      (packageJson.devDependencies &&
        packageJson.devDependencies[dependencyName]) ||
      (packageJson.peerDependencies &&
        packageJson.peerDependencies[dependencyName]) ||
      (packageJson.optionalDependencies &&
        packageJson.optionalDependencies[dependencyName]);

    return !!isInstalled;
  } catch (error) {
    console.error('Error checking dependency:', error);

    return false;
  }
}

export async function findPackageJson( startDir: string): Promise<string | boolean | undefined> {
  let currentDir = startDir;
  let continueLoop: boolean = true;

  while (continueLoop) {
    const packageJsonPath = path.join(currentDir, 'package.json');

    try {
      await access(packageJsonPath);
      return packageJsonPath; // File found
    }//
     catch (error) {
      continueLoop = false;
      // File not found in the current directory, move up
      const parentDir = path.dirname(currentDir);

      // Check if we've reached the filesystem root
      if (currentDir === parentDir) {
        logger.error('package.json not found in any parent directory.');

        return false;
        // throw new Error('package.json not found in any parent directory.');
      }

      currentDir = parentDir;
    }
  }
}