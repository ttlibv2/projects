import { execSync } from "child_process";
import { AbstractCli } from "./abstract.cli";
import { colors, logger } from "../utils";
import { CmdInput } from "../commands";

export class AngularCli extends AbstractCli {

    constructor() {
        super('node', [AngularCli.findBin()]);
    }


    getGenerateCommand(inputs: string[] = [], flags: CmdInput[] = [],): string {
        return this.buildCmd({ command: 'generate', inputs, flags });
    }

    getNgNewCommand(inputs: string[] = [], flags: CmdInput[] = []): string {
        return this.buildCmd({ command: 'new', inputs, flags });
    }

    private static findBin(): string {
        try {
            const globalNodeModulesPath = execSync('pnpm root -g').toString().trim();
            return require.resolve(globalNodeModulesPath + '/@angular/cli/bin/ng.js');
        }// 
        catch (error) {
            logger.error(error.message ?? '');
            const m1 = colors.blue("Angular cli doesn't install, please execute:");
            throw new Error(`${m1} ${colors.green('pnpm i -g @angular/cli')}`);
        }
    }
}