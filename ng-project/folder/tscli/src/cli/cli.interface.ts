import { CmdInput } from '../commands';

export interface CmdOptions {
    command?: string;
    inputs: string[];
    flags: CmdInput[];
  }
  
  export interface SchematicCmdOptions extends CmdOptions {
    collection: string;
    schematic: string;
  }