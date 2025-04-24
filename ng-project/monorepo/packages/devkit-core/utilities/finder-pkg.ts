import { execSync } from 'child_process';
import { join, isAbsolute } from 'path';
import { existsSync } from 'fs';
import axios from 'axios';

interface GetPackageOption {
    remotePackageUrl?: string;
    fileName?: string;
    packageName: string;
    showLog?: Function;
}

export async function getVersionPackage(...packageNames: string[]): Promise<{[name: string]: string | undefined}>{
    return packageNames
      .map(pkg => [pkg, getPackageFile(pkg, 'package.json')])
      .reduce((json, arr: any[]) => ({ ...json, [arr[0]]: arr[1]?.version}), {});
}

/**
 * get a file based on package name and file name (with or without internal path)
 * @param packageName the package name to find
 * @param [fileName='']
 * @param remotePackageUrl
 * */
export async function getPackageFile<T = any>(packageName: string, fileName: string = '', remotePackageUrl?: string): Promise<T | null> {

    // Step 1: Check if packageName is a local path
    if (isLocalPath(packageName)) {
        const localFilePath = join(process.cwd(), packageName, fileName);
        if (existsSync(localFilePath)) {
            return require(localFilePath);
        }
        else {
        //   if(isShowLog) logger.error(`Local file not found: ${localFilePath}`);
            return null;
        }
    }

    else {
        // Step 2: Try to get the file locally or globally
        const filePath = getLocalPackageFilePath(packageName, fileName);
        if (filePath && existsSync(filePath)) {
            // If the file exists locally/globally, read and return it
            return require(filePath);
        }

        // Step 3: If not found locally/globally, attempt to fetch it from a remote URL
        const url = remotePackageUrl || `https://unpkg.com/${packageName}/${fileName}`;
        try {
            const response = await axios.get(url);

            return response.data;
        } //
        catch (error) {
           console.error(`Failed to fetch ${fileName} for ${packageName} from ${url}: ${error.message ?? error}`);
            return null;
        }

    }

}

/**
 * Find local or global package path
 * @param packageName the package name to find
 * @param cwd current directory to find
 * */
export function getLocalPackageUrl(packageName: string, cwd?: string): string | null {

    // Step 1: Get the current working directory
    const projectRoot =  cwd ?? process.cwd();

    // Step 2: Check if `node_modules` exists locally
    const nodeModulesPath = join(projectRoot, 'node_modules');

    if (existsSync(nodeModulesPath)) {
        // Step 3: Check if the package is installed locally
        const packagePath = join(nodeModulesPath, packageName);
        if (existsSync(packagePath)) {
            return packagePath; // Return the local path to the package
        }
    }

    // Step 4: Check globally if not found locally
    const globalNodeModulesPath = getGlobalNodeModulesPath(packageName); // Implement this based on the environment
    if(globalNodeModulesPath != null) {
        const globalPackagePath = join(globalNodeModulesPath, packageName);
        if (existsSync(globalPackagePath)) {
            return globalPackagePath; // Return the global path to the package
        }
    }

    // Step 5: Return null if the package is not found
    return null;
}

/**
 * Determine if the package name is a local path
 * @param packageName the package name to check
 * */
export function isLocalPath(packageName: string): boolean {
    const path = packageName.replace(/\\/g, '/');
    return path.startsWith('./') || path.startsWith('../') || isAbsolute(path);
}

// Reusing the utility function to get the local or global path to the file
function getLocalPackageFilePath(packageName: string, fileName: string) {
    const packagePath = getLocalPackageUrl(packageName);
    return packagePath ? join(packageName, fileName) : null;
}

function getGlobalNodeModulesPath(packageName: string): string | null {
    try {
        // Attempt to resolve the module path
        return require.resolve(packageName, {
            paths: [globalNodeModulesPath()],
        });
    } catch (error) {
        // If the module isn't found, return null or handle the error
       // console.error(`Module ${packageName} not found globally.`);

        return null;
    }
}

/** Helper to determine the global node_modules path based on the current Node.js environment */
function globalNodeModulesPath(): string {
    return execSync('pnpm root -g').toString().trim();
}