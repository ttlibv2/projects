import { AbstractRunner, CmdInput } from './abstract.runner';
import * as process from 'node:process';

export class AngularRunner extends AbstractRunner {

  constructor() {
    super('ng')
  }

  runNew(inputs: string[], flags: CmdInput[], cwd = process.cwd()) {
    const cmdStr = this.buildCommandLine({command: 'new', inputs, flags});
    return this.run(cmdStr, false, cwd);
  }

  runGen(inputs: string[], flags: CmdInput[], cwd = process.cwd()) {
    const cmdStr = this.buildCommandLine({command: 'g', inputs, flags});
    return this.run(cmdStr, false, cwd);
  }



}

// function findClosestBinary(): string {
//   try {
//
//     if(existsLocalCli('@angular/cli')) {
//       const cwdRequire = createRequire(process.cwd());
//       const projectLocalCli = cwdRequire.resolve("@ngdev/cli");
//       cli = await import(projectLocalCli);
//     }
//     let cliLocalPath = path.join(process.cwd(), 'node_modules', '@angular', 'cli');
//
//     // NOTE: As a requirement, every user needs to install the ng lib
//     const globalNodeModulesPath = execSync('pnpm root -g').toString().trim();
//     return require.resolve(globalNodeModulesPath + '/@angular/cli/bin/ng.js');
//   } catch (e) {
//     ngLogger.error(e.message ?? '');
//     throw new Error(
//       `${colors.blue(
//         EMOJIS['BROKEN_HEART'] +
//           "Angular lib doesn't install, please execute:",
//       )} ${colors.green('pnpm i -g @angular/cli')}`,
//     );
//   }
// }