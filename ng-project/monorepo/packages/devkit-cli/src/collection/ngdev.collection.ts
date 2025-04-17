import { AbstractCollection } from "./abstract.collection";
import { Collection, ISchematic } from "./collection";
import { AbstractRunner } from "@ngdev/devkit-core/runners";

const schematics: ISchematic[] = [];

export class NgDevCollection extends AbstractCollection {
  constructor(runner: AbstractRunner) {
    super(Collection.NgDevSC, runner);
  }

  get schematics(): ISchematic[] {
    return schematics;
  }
}
