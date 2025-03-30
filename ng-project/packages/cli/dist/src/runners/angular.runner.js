"use strict";
Object.defineProperty(exports, "__esModule", { value: true });
exports.AngularRunner = void 0;
const abstract_runner_1 = require("./abstract.runner");
class AngularRunner extends abstract_runner_1.AbstractRunner {
    constructor() {
        super('ng');
    }
}
exports.AngularRunner = AngularRunner;
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
//# sourceMappingURL=angular.runner.js.map