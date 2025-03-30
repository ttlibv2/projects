import {WorkspaceDefinition, workspaceFiles, WorkspaceFormat, WorkspaceHost} from './ws.type';
import {getSystemPath, normalize, join, basename} from '@angular-devkit/core';
import {readJsonWorkspace} from './json-reader';

const formatLookup = new WeakMap<WorkspaceDefinition, WorkspaceFormat>();

export async function readWorkspace(path: string, host: WorkspaceHost, format?: WorkspaceFormat): Promise<WorkspaceDefinition> {
  if (await host.isDirectory(path)) {
    const directory = normalize(path);
    let found = false;
    for (const [name, nameFormat] of Object.entries(workspaceFiles)) {
      if (format !== undefined && format !== nameFormat) {
        continue;
      }

      const potential = getSystemPath(join(directory, name));
      if (await host.isFile(potential)) {
        path = potential;
        format = nameFormat;
        found = true;
        break;
      }
    }
    if (!found) {
      throw new Error(
        'Unable to locate a workspace file for workspace path. Are you missing an `angular.json`' +
        ' or `.angular.json` file?',
      );
    }
  } else if (format === undefined) {
    const filename = basename(normalize(path));
    if (filename in workspaceFiles) {
      format = workspaceFiles[filename];
    }
  }

  if (format === undefined) {
    throw new Error('Unable to determine format for workspace path.');
  }

  let workspace;
  switch (format) {
    case WorkspaceFormat.JSON:
      workspace = await readJsonWorkspace(path, host);
      break;
    default:
      throw new Error('Unsupported workspace format.');
  }

  formatLookup.set(workspace, WorkspaceFormat.JSON);

  return workspace;
}
