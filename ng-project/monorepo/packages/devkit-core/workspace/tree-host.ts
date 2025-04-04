import { WorkspaceHost } from './definitions';
import { Tree } from '@angular-devkit/schematics';

/**
 * A {@link WorkspaceHost} backed by a Schematics {@link Tree} instance.
 */
export class TreeWorkspaceHost implements WorkspaceHost {
    constructor(private readonly tree: Tree) {}

    async readFile(path: string): Promise<string> {
        return this.tree.readText(path);
    }

    async writeFile(path: string, data: string): Promise<void> {
        if (this.tree.exists(path)) {
            this.tree.overwrite(path, data);
        } else {
            this.tree.create(path, data);
        }
    }

    async isDirectory(path: string): Promise<boolean> {
        return !this.tree.exists(path) && this.tree.getDir(path).subfiles.length > 0;
    }

    async isFile(path: string): Promise<boolean> {
        return this.tree.exists(path);
    }
}