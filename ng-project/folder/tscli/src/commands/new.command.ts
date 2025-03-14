import { Command } from "commander";
import { AbstractCommand } from "./abstract.command";

export class NewCommand extends AbstractCommand {

    load(program: Command): void {
        program
            .command('new <name>').alias('n')
            .description('Generate monorepo workspace.')
            .option('-dir, --directory [directory]', 'Specify the destination directory')
            .option('-d, --dry-run', 'Report actions that would be performed without writing out results.', false)
    }



}