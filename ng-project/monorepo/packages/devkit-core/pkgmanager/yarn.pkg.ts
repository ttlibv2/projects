import {AbstractPkgManager, EnumPkg, PkgManagerCmd} from "./abstract.pkg";
import {RunnerFactory} from "../runners/factory.runner";
import { Runner } from '../runners/enum.runner';


export class YarnPackageManager extends AbstractPkgManager {

  constructor() {
    super(RunnerFactory.create(Runner.YARN));
  }

  get name(): string {
    return EnumPkg.YARN.toUpperCase();
  }

  get cli(): PkgManagerCmd {
    return {
      install: 'install',
      add: 'add',
      update: 'up',
      remove: 'remove',
      saveFlag: '',
      saveDevFlag: '--dev',
      silentFlag: '--reporter=silent',
       prefix: '--modules-folder',
      noLockfile: '--no-lockfile',
    };
  }


}