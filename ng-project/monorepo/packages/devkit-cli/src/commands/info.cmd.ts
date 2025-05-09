import { Argv } from 'yargs';
import { CommandModule, CommandScope, ArgOption, OtherOptions } from './core/abstract.cmd';
import { RootCommands } from './command.list';
import { AbstractPkgManager } from '@ngdev/devkit-core/pkgmanager';
import { colors, getPackageFile, Logger } from '@ngdev/devkit-core/utilities';
import { BANNER } from '@ngdev/devkit-core/logui';
import {createRequire} from 'node:module';
import { resolve } from 'node:path';
import * as CliTable3 from 'cli-table3';

export interface InfoArg {
    collection: string;
    schematic?: string;
    registry?: string;
}


export default class InfoCmd extends CommandModule<InfoArg> {
    command = 'info <collection> [schematic]';
    aliases = RootCommands['info'].aliases;
    describe = 'Show information about a schematic or a collection, so you can see all the options allowed by a schematic';
    scope = CommandScope.Both;

    get logger(): Logger {
        return this.context.logger;
    }

    get manager(): AbstractPkgManager {
        return this.context.packageManager;
    }

    async builder(yargs: Argv): Promise<Argv<InfoArg>> {
        return yargs
            .positional('collection', {
                describe: 'The [collection] to run. (exp: \'./@repo/dist/collection.json\' )',
                description: '11111',
                type: 'string'
            })
            .positional('schematic', {
                describe: 'The [schematic] to run.',
                type: 'string'
            })
            .option('registry', {
                type: 'string',
                describe: 'The NPM registry to use'
            })
            .strictOptions(false);
    }

    async run(options: ArgOption<InfoArg> & OtherOptions): Promise<any> {
       await this.displayBanner();
       this.logger.info(`\n\n`);
       await this.displaySystemInformation();
       this.logger.info(`\n\n----------------------------------`);

       await this.displayCollection(options);




    }

    private async displayBanner() {
        this.logger.info(colors.red(BANNER));
    }

    private async displaySystemInformation(): Promise<void> {
        let [name, version] = [this.manager.name, process.version];
        this.logger.info(colors.green`[System Information]`);
        this.logger.info(`OS               : ${process.platform} ${process.arch}`);
        this.logger.info(`NodeJS           : ${process.versions.node}`);
        this.logger.info(`Package Manager  : ${colors.blue(`${name.toLowerCase()} ${version}`)}`);
        this.logger.info(`NgDev CLI        : ${await getCliVersion()}`);
        this.logger.info(`Angular CLI      : ${await getAngularVersion()}`);
        this.logger.info(`Nest CLI         : ${await getNestVersion()}`);

    }

    private async displayCollection(options: ArgOption<InfoArg> & OtherOptions) {
        let table = new CliTable3({
            head: ['name', 'alias', 'required', 'desc']
        });


        this.logger.info(table.toString());
    }

}

async function getCliVersion(): Promise<string> {
    const cliPackage = createRequire(resolve(__dirname, '../..'));
    return cliPackage('./package.json').version as string;
}

async function getAngularVersion(): Promise<string> {
    const cliPackage = await getPackageFile('@angular/cli', 'package.json');
    return cliPackage?.version ?? null;
}

async function getNestVersion(): Promise<string> {
    const cliPackage = await getPackageFile('@nestjs/cli', 'package.json');
    return cliPackage?.version ?? null;
}