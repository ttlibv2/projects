/**
 * @license
 * Copyright Google LLC All Rights Reserved.
 *
 * Use of this source code is governed by an MIT-style license that can be
 * found in the LICENSE file at https://angular.dev/license
 */
import { Argv } from 'yargs';
import { CommandModule, CommandModuleImplementation } from '../core/command-module';
export default class VersionCommandModule extends CommandModule implements CommandModuleImplementation {
    command: string;
    aliases: string[];
    describe: string;
    longDescriptionPath?: string | undefined;
    builder(localYargs: Argv): Argv;
    run(): Promise<void>;
    private getVersion;
}
//# sourceMappingURL=cli.d.ts.map