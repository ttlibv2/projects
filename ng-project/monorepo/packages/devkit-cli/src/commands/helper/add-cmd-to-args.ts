import { Argv } from "yargs";
import {
  CommandContext,
  CommandModule,
  CommandModuleError,
  CommandModuleImplementation,
  CommandScope,
} from "../abstract.cmd";

export const demandCommandFailureMessage = `You need to specify a command before moving on. Use '--help' to view the available commands.`;
export type CommandModuleConstructor = Partial<CommandModuleImplementation> & {
  new (
    context: CommandContext,
  ): Partial<CommandModuleImplementation> & CommandModule;
};

export function addCommandModuleToYargs<
  T extends object,
  U extends CommandModuleConstructor,
>(localYargs: Argv<T>, commandModule: U, context: CommandContext): Argv<T> {
  const cmd = new commandModule(context);
  const {workspace, args: {options: {jsonHelp,help}}} = context;

  const describe = jsonHelp ? cmd.fullDescribe : cmd.describe;

  return localYargs.command({
    command: cmd.command,
    aliases: cmd.aliases,
    describe: typeof describe === "object" ? JSON.stringify(describe) : describe,
    deprecated: cmd.deprecated,
    builder: (argv) => {
      // Skip scope validation when running with '--json-utilities' since it's easier to generate the output for all commands this way.
      const isInvalidScope = (!jsonHelp && !help) &&
        ((cmd.scope === CommandScope.In && !workspace) ||
          (cmd.scope === CommandScope.Out && workspace));

      if (isInvalidScope) {
        const side = workspace ? "inside" : "outside";
        throw new CommandModuleError(
          `This command is not available when running the NgDev CLI [${side}] a workspace.`,
        );
      }

      return cmd.builder(argv) as any;
    },
    handler: (args) => cmd.handler(args),
  });
}