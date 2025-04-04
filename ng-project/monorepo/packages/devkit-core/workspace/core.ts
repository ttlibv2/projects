import { IWorkspace, WorkspaceHost } from './definitions';
import { getSystemPath, join, normalize } from '@angular-devkit/core';
import { readJsonWorkspace, writeJsonWorkspace } from './file-json';

export async function readWorkspace(host: WorkspaceHost, path: string, workspaceFiles?: string[]) {

    // path is directory
    if(await host.isDirectory(path) && workspaceFiles?.length) {
        const directory = normalize(path);
        let found = false;

        for (const name of workspaceFiles) {

            const potential = getSystemPath(join(directory, name));
            if (await host.isFile(potential)) {
                path = potential;
                found = true;
                break;
            }
        }
        if (!found) {
            let files = workspaceFiles.join(' or ');
            throw new Error(`Unable to locate a workspace file for workspace path. Are you missing an ${files} file?`);
        }
    }

    const {} = await readJsonWorkspace( host, path);
}

export async function writeWorkspace(host: WorkspaceHost, workspace: IWorkspace, path?: string) {
    return writeJsonWorkspace(host, workspace, path);
}