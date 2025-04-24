import { AbstractRunner, FlagCmd, RunCmd } from "./abstract.runner";
import * as process from 'node:process';

export class AngularRunner extends AbstractRunner {

  constructor() {
    super('ng')
  }

  new(flags: FlagCmd, cwd = process.cwd()) {
    return this.runCommand('new', flags, cwd);
  }

  addLib(flags: FlagCmd, cwd = process.cwd()) {
    return this.gen('lib', flags, cwd);
  }

  gen(schematic: string, flags: FlagCmd, cwd = process.cwd()) {
    return this.runCommand(`g ${schematic}`, flags, cwd);
  }


  private runCommand(schematic: string, flags: FlagCmd, cwd = process.cwd()) {
    const command = `${schematic} ${this.buildFlags(flags)}`;
    return this.run(command, { collect: false, cwd });
  }
}