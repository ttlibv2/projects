import {AbstractPkgManager, EnumPkg, PkgManagerCmd} from "./abstract.pkg";
import {RunnerFactory} from "../runners/factory.runner";
import { Runner } from '../runners/enum.runner';

export class NpmPackageManager extends AbstractPkgManager {

  constructor() {
    super(RunnerFactory.create(Runner.NPM));
  }

  get name(): string {
    return EnumPkg.NPM.toUpperCase();
  }

  get cli(): PkgManagerCmd {
    return {
      install: 'install',
      add: 'install',
      update: 'update',
      remove: 'uninstall',
      saveFlag: '--save',
      saveDevFlag: '--save-dev',
      silentFlag: '--silent',
      prefix: '--prefix',
      noLockfile: '--no-package-lock'
    };
  }


}