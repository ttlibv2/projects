
import { Command } from 'commander';
import { AbstractAction } from '../actions/abstract.action';

export enum CommandScope {
  /** Command can only run inside an Angular workspace. */
  In,

  /** Command can only run outside an Angular workspace. */
  Out,

  /** Command can run inside and outside an Angular workspace. */
  Both,
}

export abstract class AbstractCommand {
  abstract readonly command: string;
  abstract readonly aliases: string;
  abstract readonly describe: string | false;
  abstract readonly longDescriptionPath?: string;
  readonly scope: CommandScope = CommandScope.Both;

  constructor(protected action: AbstractAction) {}

  




  abstract load(program: Command): void;
}