import * as fs from 'node:fs';
import { AbstractPkgManager, EnumPkg } from "./abstract.pkg";
import { NpmPackageManager } from "./npm.pkg";
import { PNpmPackageManager } from "./pnpm.pkg";
import { YarnPackageManager } from "./yarn.pkg";

export class PkgManagerFactory {

  static create(name: EnumPkg | string): AbstractPkgManager {
    name = typeof name === 'string' ? name.toLowerCase() : name;
    switch (name) {
      case EnumPkg.NPM:
        return new NpmPackageManager();
      case EnumPkg.YARN:
        return new YarnPackageManager();
      case EnumPkg.PNPM:
        return new PNpmPackageManager();
      default:
        throw new Error(`Package manager ${name} is not managed.`);
    }
  }

  static async find(): Promise<AbstractPkgManager> {
    const DEFAULT_PACKAGE_MANAGER = EnumPkg.PNPM;

    try {
      const files = await fs.promises.readdir(process.cwd());

      const hasYarnLockFile = files.includes('yarn.lock');
      if (hasYarnLockFile) {
        return this.create(EnumPkg.YARN);
      }

      const hasPnpmLockFile = files.includes('pnpm-lock.yaml');
      if (hasPnpmLockFile) {
        return this.create(EnumPkg.PNPM);
      }

      const hasnpmLockFile = files.includes('package-lock.json');
      if (hasnpmLockFile) {
        return this.create(EnumPkg.NPM);
      }

      const hasBunLockFile = files.includes('bun.lockb');
      if (hasBunLockFile) {
        return this.create(EnumPkg.BUN);
      }

      else {
        return this.create(DEFAULT_PACKAGE_MANAGER);
      }
    } catch (error) {
      return this.create(DEFAULT_PACKAGE_MANAGER);
    }
  }
}