import {AbstractPkgManager, EnumPkg, PkgManagerCmd} from "./abstract.pkg";
import {RunnerFactory} from "../runners/factory.runner";
import { Runner } from '../runners/enum.runner';

export class PNpmPackageManager extends AbstractPkgManager {

  constructor() {
    super(RunnerFactory.create(Runner.PNPM));
  }

  get name(): string {
    return EnumPkg.PNPM.toUpperCase();
  }

  get cli(): PkgManagerCmd {
    return {
      install: 'install',
      add: 'add',
      update: 'update',
      remove: 'remove',
      saveFlag: '--save',
      saveDevFlag: '--save-dev',
      silentFlag: '--reporter=silent',
      prefix: '--prefix',
      noLockfile: '--no-lockfile',
    };
  }


}