import { AbstractRunner } from './abstract.runner';

export class SchematicRunner extends AbstractRunner {
  constructor() {
    super(`schematics`);
  }

  // public static getModulePaths() {
  //   return module.paths;
  // }

}

// function findClosestSchematicsBinary(): string {
//   try {
//     return require.resolve(
//       '@angular-devkit/schematics-cli/bin/schematics.js',
//       { paths: this.getModulePaths() },
//     );
//   } catch {
//     throw new Error("'@angular-devkit/schematics-cli' path could not be found!");
//   }
// }