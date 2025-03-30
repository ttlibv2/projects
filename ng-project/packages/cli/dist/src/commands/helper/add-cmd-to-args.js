"use strict";
Object.defineProperty(exports, "__esModule", { value: true });
exports.demandCommandFailureMessage = void 0;
exports.addCommandModuleToYargs = addCommandModuleToYargs;
const abstract_cmd_1 = require("../abstract.cmd");
exports.demandCommandFailureMessage = `You need to specify a command before moving on. Use '--help' to view the available commands.`;
function addCommandModuleToYargs(localYargs, commandModule, context) {
    const cmd = new commandModule(context);
    const { args: { options: { jsonHelp }, }, workspace, } = context;
    const describe = jsonHelp ? cmd.fullDescribe : cmd.describe;
    // console.log(cmd);
    return localYargs.command({
        command: cmd.command,
        aliases: cmd.aliases,
        describe: typeof describe === 'object' ? JSON.stringify(describe) : describe,
        deprecated: cmd.deprecated,
        builder: (argv) => {
            // Skip scope validation when running with '--json-help' since it's easier to generate the output for all commands this way.
            const isInvalidScope = !jsonHelp &&
                ((cmd.scope === abstract_cmd_1.CommandScope.In && !workspace) ||
                    (cmd.scope === abstract_cmd_1.CommandScope.Out && workspace));
            if (isInvalidScope) {
                throw new abstract_cmd_1.CommandModuleError(`This command is not available when running the CLI ${workspace ? 'inside' : 'outside'} a workspace.`);
            }
            return cmd.builder(argv);
        },
        handler: (args) => cmd.handler(args),
    });
}
//# sourceMappingURL=add-cmd-to-args.js.map