import * as path from "node:path";
import * as os from "node:os";
import { existsSync } from "node:fs";
import { findUp } from "../utilities/find-up";
import { DevWorkspace } from "./ws.type";
import { isJsonObject, json } from "@angular-devkit/core";

const fileConfigName = "project.json";
export const joinGlobal = (dir: any) => path.join(dir, ".ngdev", fileConfigName);

export const defaultGlobalPath = () => joinGlobal(os.homedir());

export const getProjectByCwd = (workspace: DevWorkspace, location?: string): string | null => {
  if (!workspace.projects) return null;
  else if (workspace.projects.size === 1) {
    const projectNames = workspace.projects.keys();
    return Array.from(projectNames)[0];
  } //
  else {
    return findProjectByPath(workspace, location) ?? null;
  }

};

export function getConfigPath(level: "global" | "local"): string | null {
  return level === "global" ? getGlobalFilePath() : getProjectFilePath();
}

export async function getSchematicDefaults(collection: string, schematic: string, project?: string | null,): Promise<{}> {
 //console.log(`ws.help => getSchematicDefaults: `, `${collection}:${schematic}`);

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
  const globalOptions = await DevWorkspace.global();
  mergeOptions(globalOptions?.collections);

  const workspace = await DevWorkspace.project();
  if (workspace) {

    // Workspace level schematic options
    mergeOptions(workspace.collections);

    project = project || getProjectByCwd(workspace);

    if (project) {
      const projectProp = workspace.projects.get(project);
      mergeOptions(projectProp?.collections);
    }

  }

  return result;
}

function getGlobalFilePath(): string | null {
  const join = (dir: any) => (dir ? joinGlobal(dir) : null);
  const dirPaths: string[] = [
    <string>process.env["XDG_CONFIG_HOME"],
    process.cwd(),
    __dirname,
    os.homedir(),
  ];
  return (
    dirPaths.map((dir) => join(dir)).find((file) => file && existsSync(file)) ??
    null
  );
}

function getProjectFilePath(projectPath?: string): string | null {
  return (
    (projectPath && findUp(projectPath, fileConfigName)) ||
    findUp(process.cwd(), fileConfigName) ||
    findUp(__dirname, fileConfigName)
  );
}

function findProjectByPath(
  workspace: DevWorkspace,
  location?: string,
): string | null {
  location = location ?? process.cwd();

  const isInside = (base: string, potential: string): boolean => {
    const absoluteBase = path.resolve(workspace.baseDir, base);
    const absolutePotential = path.resolve(workspace.baseDir, potential);
    const relativePotential = path.relative(absoluteBase, absolutePotential);
    return (
      !relativePotential.startsWith("..") && !path.isAbsolute(relativePotential)
    );
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