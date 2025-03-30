import { AbstractRunner } from '../runners';
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
export declare abstract class AbstractPkgManager {
    protected runner: AbstractRunner;
    protected constructor(runner: AbstractRunner);
    abstract get name(): string;
    abstract get cli(): PkgManagerCmd;
    version(): Promise<string>;
}
