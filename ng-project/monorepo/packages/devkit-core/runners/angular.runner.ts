import { AbstractRunner, FlagCmd, RunOptions } from './abstract.runner';

export class AngularRunner extends AbstractRunner {

  constructor() {
    super('ng')
  }

  new(flags: FlagCmd, options?: RunOptions) {
    return this.runCommand('new', flags, options);
  }

  lib(flags: FlagCmd, options?: RunOptions) {
    return this.gen('lib', flags,options);
  }

  app(flags: FlagCmd, options?: RunOptions) {
    return this.gen('app', flags, options);
  }

  gen(schematic: string, flags: FlagCmd, options?: RunOptions) {
    return this.runCommand(`g ${schematic}`, flags, options);
  }

  private runCommand(schematic: string, flags: FlagCmd, options?: RunOptions) {
    const command = `${schematic} ${this.buildFlags(flags)}`;
    return this.run(command, options);
  }
}