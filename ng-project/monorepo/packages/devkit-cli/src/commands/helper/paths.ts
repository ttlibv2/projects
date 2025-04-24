import { rmSync, rename, mkdirSync } from "fs";
import { dirname, join, relative, resolve } from "node:path";
import { existsSync, PathLike } from "node:fs";

export const paths = {

  existsSync: (path: PathLike) => {
    return existsSync(path);
},

  del: (path: string, parent?: string) => {
    const pDel = parent == undefined ? path : join(parent, path);
    rmSync(pDel, {recursive: true, force: true});
  },

  rename: (oldPath: string, newName: string) => {
    const parent = dirname(oldPath);
    const newPath = join(parent, newName);
    rename(oldPath, newPath, err => console.log(err));
  },

  relative: (from: string, to: string) => {
    return relative(from, to);
  },

  /**
   * Join all arguments together and normalize the resulting path.
   * @param paths paths to join.
   */
  join: (...paths: string[]) => {
    return join(...paths);
  },

  mkdir: (...paths: string[]) => {
    const newPath = join(...paths);
    if(!existsSync(newPath)) mkdirSync(newPath);
    return newPath;
  }

};