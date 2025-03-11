import { existsSync } from 'fs';
import { join, posix } from 'path';
import { CommandLoader } from '../commands/command.loader';

const localBinPathSegments = [process.cwd(), 'node_modules', '@tsui', 'cli'];

export function localBinExists() {
  return existsSync(join(...localBinPathSegments));
}

export function loadLocalBinCommandLoader(): typeof CommandLoader {
  const commandsFile = require(posix.join(...localBinPathSegments, 'commands'));
  return commandsFile.CommandLoader;
} 