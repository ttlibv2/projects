import { yellow } from 'ansis';
import { NpmRunner } from './npm.runner';
import { Runner } from './enum.runner';
import { SchematicRunner } from './schematic.runner';
import { YarnRunner } from './yarn.runner';
import { PnpmRunner } from './pnpm.runner';
import { Logger } from '../utilities/logger';
import { AngularRunner } from './angular.runner';
import { NestJsRunner } from './nestjs.runner';
import {AbstractRunner} from "./abstract.runner";

export class RunnerFactory {

  public static create(runner: Runner) : AbstractRunner{
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
          return new NestJsRunner();

      default:
        Logger.create('runner.factory')
          .info(yellow`[WARN] Unsupported runner: ${runner}`);
        throw new Error(`[WARN] Unsupported runner: ${runner}`);
    }
  }
}