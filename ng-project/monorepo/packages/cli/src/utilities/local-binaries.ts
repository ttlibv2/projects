import { existsSync } from 'fs';
import { join, posix } from 'path';
// import { CommandLoader } from '../commands/base';

const localBinPathSegments = [process.cwd(), 'node_modules', '@ngdev', 'cli'];

export function localBinExists() {
  return existsSync(join(...localBinPathSegments));
}

export function loadLocalCli() {
  return posix.join(...localBinPathSegments, "lib").replaceAll("\/", "\\");
 // return require(path);
}