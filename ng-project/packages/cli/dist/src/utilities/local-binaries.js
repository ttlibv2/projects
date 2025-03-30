"use strict";
Object.defineProperty(exports, "__esModule", { value: true });
exports.localBinExists = localBinExists;
const fs_1 = require("fs");
const path_1 = require("path");
// import { CommandLoader } from '../commands/base';
const localBinPathSegments = [process.cwd(), 'node_modules', '@ngdev', 'cli'];
function localBinExists() {
    // console.log(join(...localBinPathSegments))
    return (0, fs_1.existsSync)((0, path_1.join)(...localBinPathSegments));
}
// export function loadLocalBinCommandLoader(): typeof CommandLoader {
//   const commandsFile = require(posix.join(...localBinPathSegments, 'commands'));
//   return commandsFile.CommandLoader;
// }
//# sourceMappingURL=local-binaries.js.map