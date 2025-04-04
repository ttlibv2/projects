import { WorkspaceHost } from './definitions';
import { promises as fs } from 'node:fs';

export class FsWorkspaceHost implements WorkspaceHost {

    async isDirectory(path: string): Promise<boolean> {
        try {
            const stats = await fs.stat(path);
            return stats.isDirectory();
        }
        catch {
            return false;
        }
    }

    async isFile(path: string): Promise<boolean> {
        try {
            const stats = await fs.stat(path);
            return stats.isFile();
        }
        catch {
            return false;
        }
    }

    readFile(path: string): Promise<string> {
        return fs.readFile(path, 'utf-8');
    }

    async writeFile(path: string, data: string): Promise<void> {
        await fs.writeFile(path, data);
    }

}