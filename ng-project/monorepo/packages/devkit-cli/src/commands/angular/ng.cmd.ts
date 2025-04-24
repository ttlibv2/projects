// import { LocalArgv, OtherOptions, RunOptions } from "./abstract.cmd";
// import { RootCommands } from "./command.list";
// import { SchematicsCommandArgs, SchematicsCommandModule } from "./schematics.cmd";
// import { CommandModule } from "yargs";
//
// interface NgCommandArgs extends SchematicsCommandArgs {
//   schematic?: string;
//   cmdName?: string;
// }
//
// const CmdName: string[] = ['run', 'generate', 'g'];
//
// export default class NgGenCommandModule extends SchematicsCommandModule<NgCommandArgs> {
//   command = "ng <cmdName>";
//   aliases = RootCommands["ng"].aliases;
//   describe = "Run Angular CLI";
//
//   async builder(argv: LocalArgv): Promise<LocalArgv<NgCommandArgs> | any> {
//     let sArgv = await super.builder(argv);
//     return sArgv
//       .positional('cmdName', {type: 'string', describe: 'The schematic angular cli', choices: CmdName})
//       .command('generate <schematic>', '', b => b.positional('schematic', {type: 'string'}))
//       .strict(false).strictCommands(false).strictOptions(false);
//   }
//
//   async run(options: RunOptions<NgCommandArgs> & OtherOptions): Promise<number | void> {
//     console.log(`NgGenCommandModule => run: `, options);
//     const { cmdName } = options;
//     const cwd = process.cwd();
//
//     //if(cmdName == 'new') {
//     //   throw new Error(`Please use command: [app or lib] `);
//     // }
//
//     // await RunnerFactory.angular().run(cmdName, {cwd});
//
//     return Promise.resolve();
//   }
//
//
// }