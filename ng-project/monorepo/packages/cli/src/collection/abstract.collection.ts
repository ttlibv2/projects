import {AbstractRunner} from "@ngdev/devkit-core";
import {ISchematic} from "./collection";
import {SchematicOption} from "./schematic.option";
import {NodeWorkflow} from "@angular-devkit/schematics/tools";

export abstract class AbstractCollection {

  constructor(
    protected collection: string,
    protected runner: AbstractRunner) {
  }

  abstract get schematics(): ISchematic[];

  async execute(name: string, options: SchematicOption[], extraFlags?: string): Promise<void> {
    const schematic: string = this.validate(name);
    let command = this.buildCommandLine(schematic, options);
    if(extraFlags) command = command.concat(` ${extraFlags}`);
    await this.runner.run(command);
  }

  protected buildCommandLine(name: string, options: SchematicOption[]): string {
    return `${this.collection}:${name}${this.buildOptions(options)}`;
  }

  protected buildOptions(options: SchematicOption[]): string {
    return options.reduce((line, option) => line.concat(` ${option.toCommandString()}`), '');
  }

  protected validate(name: string) {
    const schematic = this.schematics.find(s => s.name === name || s.alias === name);

    if (schematic === undefined || schematic === null) {
      throw new Error(`Invalid schematic "${name}". Please, ensure that "${name}" exists in this collection.`);
    }
    return schematic.name;
  }

}