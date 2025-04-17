import { existsSync} from 'fs';
import { join } from 'path';
import { execSync } from 'child_process';

export function existsCli(...paths: string[]) {
    return existsSync(join(...paths));
}

export function existsLocalCli(pkgName: string, cwd = process.cwd()): boolean {
    return existsSync(join(cwd, 'node_modules', pkgName));
}

export function existsGlobalCli(pkgName: string): boolean {
    const modulesPath = execSync('pnpm root -g').toString().trim();
    return existsSync(join(modulesPath, pkgName));
}