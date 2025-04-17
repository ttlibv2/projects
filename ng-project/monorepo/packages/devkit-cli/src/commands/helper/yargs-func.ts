import * as yargs from "yargs";

export const yargsUnknownCommands = (argv: yargs.Arguments) => {
  const commandKeys = yargs
    .getInternalMethods()
    .getCommandInstance()
    .getCommands();

  const unknown: string[] = [];
  const currentContext = yargs.getInternalMethods().getContext();
  const usage = yargs.getInternalMethods().getUsageInstance();

  if (currentContext.commands.length > 0 || commandKeys.length > 0) {
    const pos = currentContext.commands.length;
    argv._.slice(pos).forEach((key: string) => {
      if (!key.startsWith("--") && !commandKeys.includes("" + key)) {
        unknown.push("" + key);
      }
    });
  }

  if (unknown.length > 0) {
    //usage.fail(
    // __n(
    //   'Unknown command: %s',
    //   'Unknown commands: %s',
    //   unknown.length,
    //   unknown.join(', ')
    // )
    //);
    return true;
  } else {
    return false;
  }
};
