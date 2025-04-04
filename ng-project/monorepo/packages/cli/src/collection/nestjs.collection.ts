import {AbstractCollection} from "./abstract.collection";
import {Collection, ISchematic} from "./collection";
import {AbstractRunner} from "@ngdev/devkit-core";

const schematics: ISchematic[] = [];

export class NestCollection extends AbstractCollection {

  constructor(runner: AbstractRunner) {
    super(Collection.NestJS, runner);
  }

  get schematics(): ISchematic[] {
    return schematics;
  }

}