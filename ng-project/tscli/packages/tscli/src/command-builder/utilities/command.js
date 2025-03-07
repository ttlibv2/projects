"use strict";
/**
 * @license
 * Copyright Google LLC All Rights Reserved.
 *
 * Use of this source code is governed by an MIT-style license that can be
 * found in the LICENSE file at https://angular.dev/license
 */
Object.defineProperty(exports, "__esModule", { value: true });
exports.demandCommandFailureMessage = void 0;
exports.addCommandModuleToYargs = addCommandModuleToYargs;
const command_module_1 = require("../command-module");
exports.demandCommandFailureMessage = `You need to specify a command before moving on. Use '--help' to view the available commands.`;
function addCommandModuleToYargs(localYargs, commandModule, context) {
    const cmd = new commandModule(context);
    const { args: { options: { jsonHelp } }, workspace } = context;
    const describe = jsonHelp ? cmd.fullDescribe : cmd.describe;
    return localYargs.command({
        command: cmd.command,
        aliases: cmd.aliases,
        describe: typeof describe === 'object' ? JSON.stringify(describe) : describe,
        deprecated: cmd.deprecated,
        builder: (argv) => {
            const isInvalidScope = !jsonHelp &&
                ((cmd.scope === command_module_1.CommandScope.In && !workspace) ||
                    (cmd.scope === command_module_1.CommandScope.Out && workspace));
            if (isInvalidScope) {
                throw new command_module_1.CommandModuleError(`This command is not available when running the CLI ${workspace ? 'inside' : 'outside'} a workspace.`);
            }
            return cmd.builder(argv);
        },
        handler: (args) => cmd.handler(args),
    });
}
//# sourceMappingURL=command.js.map