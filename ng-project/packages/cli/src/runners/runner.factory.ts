import { yellow } from 'ansis';
import { NpmRunner } from './npm.runner';
import { Runner } from './runner';
import { SchematicRunner } from './schematic.runner';
import { YarnRunner } from './yarn.runner';
import { PnpmRunner } from './pnpm.runner';
import { Logger } from '../utilities/logger';
import { AngularRunner } from './angular.runner';
import { NestRunner } from './nest.runner';

export class RunnerFactory {

  public static create(runner: Runner) {
    switch (runner) {
      case Runner.SCHEMATIC:
        return new SchematicRunner();

      case Runner.NPM:
        return new NpmRunner();

      case Runner.YARN:
        return new YarnRunner();

      case Runner.PNPM:
        return new PnpmRunner();

      case Runner.ANGULAR:
        return new AngularRunner();

        case Runner.NESTJS:
          return new NestRunner();

      default:
        Logger.create('runner.factory')
          .info(yellow`[WARN] Unsupported runner: ${runner}`);
    }
  }
}