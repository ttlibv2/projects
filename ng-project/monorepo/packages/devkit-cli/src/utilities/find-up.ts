import * as path from "node:path";
import {existsSync} from "node:fs";

export function findUp(dirPath: string, names: string | string[]) {
  names = !Array.isArray(names) ? [names] : names;

  const root = path.parse(dirPath).root;

  let currentDir = dirPath;
  while (currentDir && currentDir !== root) {
    for (const name of names) {
      const p = path.join(currentDir, name);
      if (existsSync(p)) {
        return p;
      }
    }

    currentDir = path.dirname(currentDir);
  }

  return null;
}