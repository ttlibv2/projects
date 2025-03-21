import {AbstractRunner} from '../runners/abstract.runner';

export interface PkgManagerCmd {
  install: string;
  installAll: string;
  add: string;
  update: string;
  remove: string;
  save: string;
  saveDev: string;
  silent: string;
  prefix?: string;
  noLockfile?: string;
}

export abstract class AbstractPkgManager {
  constructor(protected runner: AbstractRunner) {}

  abstract get name(): string;

  abstract get cli(): PkgManagerCmd;

  version(): Promise<string> {
    return this.runner.run('--version', true) as Promise<string>;
  }

  


}