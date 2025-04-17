import { AbstractCollection } from "./abstract.collection";
import { ISchematic } from "./collection";
import { extractListSchematic } from "../utilities/helper";

export class CustomCollection extends AbstractCollection {
  get schematics(): ISchematic[] {
    return extractListSchematic(this.collection, process.cwd());
  }
}
