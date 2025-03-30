import { NpmRunner } from './npm.runner';
import { Runner } from './runner.enum';
import { SchematicRunner } from './schematic.runner';
import { YarnRunner } from './yarn.runner';
import { PnpmRunner } from './pnpm.runner';
import { AngularRunner } from './angular.runner';
import { NestJsRunner } from './nestjs.runner';
export declare class RunnerFactory {
    static create(runner: Runner): NpmRunner | SchematicRunner | YarnRunner | PnpmRunner | AngularRunner | NestJsRunner | undefined;
}
