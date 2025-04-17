import { Rule,Tree } from '@angular-devkit/schematics';
import { workspace } from '../../utility/workspace';
import { Schema } from './schema';
import { CmdInput, RunnerFactory } from '@ngdev/devkit-core/runners';
import * as path from 'node:path';

export default function(options: Schema): Rule {
    return async (tree: Tree) => {
        const ws = await workspace.get(tree);

        const flags: CmdInput[] = [
            {name: 'create-application', value: false},
            {name: 'package-manager', value: ws.cli.packageManager ?? 'pnpm'}
        ];

        let ngCli = RunnerFactory.angular();

        let cwd = path.join(process.cwd(), ws.libsDir);
        await ngCli.runNew([options.name], flags, cwd);

        cwd = path.join(cwd, options.name);
        await ngCli.runGen([options.name], [], cwd);

    };


}