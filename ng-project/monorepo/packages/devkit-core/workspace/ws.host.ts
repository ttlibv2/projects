import { workspaces } from '@angular-devkit/core';
import {promises as fs} from 'node:fs';
import { Tree } from '@angular-devkit/schematics';

export type WorkspaceHost = workspaces.WorkspaceHost;

export function createPromiseHost(): WorkspaceHost {
    return {
        async isDirectory(path: string): Promise<boolean> {
            try {
                const stats = await fs.stat(path);
                return stats.isDirectory();
            }
            catch {
                return false;
            }
        },

        async isFile(path: string): Promise<boolean> {
            try {
                const stats = await fs.stat(path);
                return stats.isFile();
            }
            catch {
                return false;
            }
        },

        async readFile(path: string): Promise<string> {
            return fs.readFile(path, 'utf-8');
        },

         async writeFile(path: string, data: string): Promise<void> {
            await fs.writeFile(path, data);
        }
    }
}

export function createTreeHost(tree: Tree): WorkspaceHost {
    return {
        async readFile(path: string): Promise<string> {
            return tree.readText(path);
        },

        async writeFile(path: string, data: string): Promise<void> {
            if (tree.exists(path)) tree.overwrite(path, data);
            else tree.create(path, data);
        },

        async isDirectory(path: string): Promise<boolean> {
            return !tree.exists(path) && tree.getDir(path).subfiles.length > 0;
        },

        async isFile(path: string): Promise<boolean> {
            return tree.exists(path);
        }
    }
}