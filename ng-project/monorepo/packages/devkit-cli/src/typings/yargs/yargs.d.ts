import { Argv } from "./index";

export = Yargs;

declare function Yargs(
  processArgs?: readonly string[] | string,
  cwd?: string,
  parentRequire?: NodeRequire,
): Argv;
