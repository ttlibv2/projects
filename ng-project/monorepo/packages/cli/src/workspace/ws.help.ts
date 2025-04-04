import * as path from "node:path";
import * as os from "node:os";
import {existsSync, promises as fs} from "node:fs";
import {json, workspaces, isJsonObject} from "@angular-devkit/core";
import {WorkspaceDev} from "./ws.type";
import {findUp} from "../utilities/find-up";

const fileConfigName = "project.json";
export const joinGlobal = (dir: any) => path.join(dir, ".ngdev", fileConfigName);
export const defaultGlobalPath = () => joinGlobal(os.homedir());

export const getProjectByCwd = (workspace: WorkspaceDev, location?: string): string | null => {
  if (!workspace.projects) return null;
  else if (workspace.projects.size === 1) {
    const projectNames = workspace.projects.keys();
    return Array.from(projectNames)[0];
  } else {
    return findProjectByPath(workspace, location) ?? null;
  }
}

export const createHost = (): workspaces.WorkspaceHost => {

  return {
    readFile(path) {
      return fs.readFile(path, 'utf-8');
    },
    async writeFile(path, data) {
      await fs.writeFile(path, data);
    },
    async isDirectory(path) {
      try {
        const stats = await fs.stat(path);

        return stats.isDirectory();
      }
      catch {
        return false;
      }
    },
    async isFile(path) {
      try {
        const stats = await fs.stat(path);

        return stats.isFile();
      }
      catch {
        return false;
      }
    },
  };
}

export async function getSchematicDefaults(collection: string, schematic: string, project?: string | null): Promise<{}> {
  const result = {};

  const mergeOptions = (source: json.JsonValue): void => {
    if (isJsonObject(source)) {

      // Merge options from the qualified name
      Object.assign(result, source[`${collection}:${schematic}`]);

      // Merge options from nested collection collection
      const collectionOptions = source[collection];
      if (isJsonObject(collectionOptions)) {
        Object.assign(result, collectionOptions[schematic]);
      }
    }
  };

  // Global level schematic options
  // const globalOptions = await getWorkspace('global');
  // mergeOptions(globalOptions?.extensions['collection']);

  // const workspace = await getWorkspace('local');
  // if (workspace) {
  //
  //   // Workspace level schematic options
  //   mergeOptions(workspace.extensions['collection']);
  //
  //   project = project || getProjectByCwd(workspace);
  //
  //   if (project) {
  //     // Project level schematic options
  //     mergeOptions(workspace.projects.get(project)?.extensions['collection']);
  //   }
  // }

  return result;
}

export function getConfigPath(level: 'global' | 'local'): string | null {
  return level === 'global' ? getGlobalFilePath() : getProjectFilePath();
}

function getGlobalFilePath(): string | null {
  const join = (dir: any) => dir ? joinGlobal(dir) : null;
  const dirPaths: string[] = [<string>process.env['XDG_CONFIG_HOME'], process.cwd(), __dirname, os.homedir()];
  return dirPaths.map(dir => join(dir)).find(file => file && existsSync(file)) ?? null;
}

function getProjectFilePath(projectPath?: string): string | null {
  return (
    (projectPath && findUp(projectPath, fileConfigName)) ||
    findUp(process.cwd(), fileConfigName) ||
    findUp(__dirname, fileConfigName)
  );
}

function findProjectByPath(workspace: WorkspaceDev, location?: string): string | null {
  location = location ?? process.cwd();

  const isInside = (base: string, potential: string): boolean => {
    const absoluteBase = path.resolve(workspace.basePath, base);
    const absolutePotential = path.resolve(workspace.basePath, potential);
    const relativePotential = path.relative(absoluteBase, absolutePotential);
    return !relativePotential.startsWith('..') && !path.isAbsolute(relativePotential);
  };

  const projects = Array.from(workspace.projects ?? [])
    .map(([name, project]) => [project.root, name] as [string, string])
    .filter((tuple) => isInside(tuple[0], location))
    .sort((a, b) => b[0].length - a[0].length);

  if (projects.length === 0) {
    return null;
  } //
  else if (projects.length > 1) {
    const found = new Set<string>();
    const sameRoots = projects.filter((v) => {
      if (!found.has(v[0])) {
        found.add(v[0]);

        return false;
      }

      return true;
    });

    if (sameRoots.length > 0) {
      // Ambiguous location - cannot determine a project
      return null;
    }

  }

  return projects[0][1];
}