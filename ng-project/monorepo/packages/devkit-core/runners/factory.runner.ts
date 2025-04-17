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

  static schematic(): SchematicRunner {
    return new SchematicRunner();
  }

  static npm(): SchematicRunner {
    return new NpmRunner();
  }

  static pnpm(): SchematicRunner {
    return new PnpmRunner();
  }

  static yarn(): SchematicRunner {
    return new YarnRunner();
  }

  static nestjs(): SchematicRunner {
    return new NestJsRunner();
  }

  static angular(): AngularRunner {
    return new AngularRunner();
  }

  static create(runner: Runner) : AbstractRunner{
    switch (runner) {
      case Runner.SCHEMATIC:return RunnerFactory.schematic();
      case Runner.NPM:return RunnerFactory.npm();
      case Runner.YARN:return RunnerFactory.yarn();
      case Runner.PNPM:return RunnerFactory.pnpm();
      case Runner.ANGULAR:return RunnerFactory.angular();
      case Runner.NESTJS:return RunnerFactory.nestjs();
      default:
        Logger.create('runner.factory').info(yellow`[WARN] Unsupported runner: ${runner}`);
        throw new Error(`[WARN] Unsupported runner: ${runner}`);
    }
  }
}