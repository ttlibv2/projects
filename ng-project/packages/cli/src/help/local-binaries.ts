import { existsSync } from 'fs';
import { join, posix } from 'path';
// import { CommandLoader } from '../commands/base';

const localBinPathSegments = [process.cwd(), 'node_modules', '@ngdev', 'cli'];

export function localBinExists() {
 // console.log(join(...localBinPathSegments))
  return existsSync(join(...localBinPathSegments));
}

// export function loadLocalBinCommandLoader(): typeof CommandLoader {
//   const commandsFile = require(posix.join(...localBinPathSegments, 'commands'));
//   return commandsFile.CommandLoader;
// }