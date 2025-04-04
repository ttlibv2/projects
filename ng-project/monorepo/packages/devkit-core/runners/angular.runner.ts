import { AbstractRunner } from "./abstract.runner";

export class AngularRunner extends AbstractRunner {

  constructor() {
    super('ng')
  }
}

// function findClosestBinary(): string {
//   try {
//     // NOTE: As a requirement, every user needs to install the ng lib
//     const globalNodeModulesPath = execSync('pnpm root -g').toString().trim();
//     return require.resolve(globalNodeModulesPath + '/@angular/lib/bin/ng.js');
//   } catch (e) {
//     ngLogger.error(e.message ?? '');
//     throw new Error(
//       `${colors.blue(
//         EMOJIS['BROKEN_HEART'] +
//           "Angular lib doesn't install, please execute:",
//       )} ${colors.green('pnpm i -g @angular/lib')}`,
//     );
//   }
// }