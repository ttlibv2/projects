import { SchematicsCommand } from './core/schematics.cmd';
import { ArgOption, CommandScope, LocalArgv, OtherOptions } from './core/abstract.cmd';
import { RootCommands } from './command.list';

export default class TestCmd extends SchematicsCommand<any> {
    command = 'test [schematic]';
    aliases = RootCommands['test'].aliases;
    describe = 'test';
    scope = CommandScope.Both;

    async builder(argv: LocalArgv): Promise<LocalArgv<any>> {
        return argv.option('name', {});
    }

    run(options: ArgOption<any>): Promise<number | void> {
        const cn = '@schematics/angular';
        const workflow = this.createWorkflowBuilder(cn);
        const collection = workflow.createCollection(cn);
        console.warn(`wf.context`, collection.description, this.getResolvePaths(cn));

        throw new Error(`test`);
    }


}