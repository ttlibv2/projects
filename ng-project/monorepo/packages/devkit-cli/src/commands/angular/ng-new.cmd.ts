import { SchematicArg, SchematicsCommand } from '../core/schematics.cmd';
import { ArgOption, CommandScope, LocalArgv } from '../core/abstract.cmd';
import { Option } from '../helper/json-schema';

export interface NgNewArg extends SchematicArg {
    collection?: string;
    tailwind?: boolean;
    primeng?: boolean;
}

export abstract class NgNewCommand<T extends NgNewArg> extends SchematicsCommand<T>  {
    readonly scope = CommandScope.Both;
    //readonly command = 'ng <cmd_name>';

    async builder(argv: LocalArgv): Promise<LocalArgv<T>> {
        argv = await super.builder(argv);
        return argv
            .positional('name', {type: 'string'})
            .option('collection', { type: 'string', alias: ['c'], describe: 'A collection of collection to use in generating the initial.' })
            .option('primeng', { type: 'boolean', default: true, describe: 'Use PrimeNG framework' })
            .option('tailwind', { type: 'boolean', default: true, describe: 'Use Tailwind CSS' }) as any;
    }

    async run(options: ArgOption<T>): Promise<number | void> {
        //await this.getSchemaOption('@ngdev/schematics','ng-new-app', ['application', 'library']);
        return Promise.resolve(undefined);
    }


}