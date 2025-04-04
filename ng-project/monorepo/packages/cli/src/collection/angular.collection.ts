import {AbstractCollection} from "./abstract.collection";
import {Collection, ISchematic} from "./collection";
import {AbstractRunner} from "@ngdev/devkit-core";

const schematics: ISchematic[] = [];

export class AngularCollection extends AbstractCollection {

  constructor(runner: AbstractRunner) {
    super(Collection.Angular, runner);
  }

  get schematics(): ISchematic[] {
    return schematics;
  }

}