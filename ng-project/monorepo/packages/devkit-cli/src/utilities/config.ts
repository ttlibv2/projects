import { json, workspaces } from "@angular-devkit/core";
import { existsSync, promises as fs } from "node:fs";
import * as os from "node:os";
import * as path from "node:path";
// import { PackageManager } from './package-manager';
// import { findUp } from './helper';
// import { JSONFile, readAndParseJson } from './json-file';
// import {isJsonObject} from "./helper";

// function createWorkspaceHost(): workspaces.WorkspaceHost {
//
//   return {
//     readFile(path) {
//       return fs.readFile(path, 'utf-8');
//     },
//     async writeFile(path, data) {
//       await fs.writeFile(path, data);
//     },
//     async isDirectory(path) {
//       try {
//         const stats = await fs.stat(path);
//
//         return stats.isDirectory();
//       } catch {
//         return false;
//       }
//     },
//     async isFile(path) {
//       try {
//         const stats = await fs.stat(path);
//
//         return stats.isFile();
//       } catch {
//         return false;
//       }
//     },
//   };
// }

export const workspaceSchemaPath = path.join(__dirname, "wc-schema.json");

const configNames = ["project.json"];
const globalFileName = ".ng-dev.json";
const defaultGlobalFilePath = path.join(os.homedir(), globalFileName);

// function xdgConfigHome(home: string, configFile?: string): string {
//   const xdgConfigHome = process.env['XDG_CONFIG_HOME'] || path.join(home, '.config');
//   const xdgAngularHome = path.join(xdgConfigHome, 'ngdev');
//   return configFile ? path.join(xdgAngularHome, configFile) : xdgAngularHome;
// }

// function projectFilePath(projectPath?: string): string | null {
//   // Find the configuration, either where specified, in the Angular CLI project
//   // (if it's in node_modules) or from the current process.
//   return (
//     (projectPath && findUp(projectPath, configNames)) ||
//     findUp(process.cwd(), configNames) ||
//     findUp( __dirname, configNames,)
//   );
// }

// function globalFilePath(): string | null {
//   const home = os.homedir();
//   if (!home) return null;
//
//
//   // follow XDG Base Directory spec
//   // note that createGlobalSettings() will continue creating
//   // global file in home directory, with this user will have
//   // choice to move change its location to meet XDG convention
//   const xdgConfig = xdgConfigHome(home, 'config.json');
//   if (existsSync(xdgConfig)) {
//     return xdgConfig;
//   }
//
//   if (existsSync(defaultGlobalFilePath)) {
//     return defaultGlobalFilePath;
//   }
//
//   return null;
// }

// export class AngularWorkspace {
//   readonly basePath: string;
//
//   constructor(
//     private readonly workspace: workspaces.WorkspaceDefinition,
//     readonly filePath: string,
//   ) {
//     this.basePath = path.dirname(filePath);
//   }
//
//   get extensions(): Record<string, json.JsonValue | undefined> {
//     return this.workspace.extensions;
//   }
//
//   get projects(): workspaces.ProjectDefinitionCollection {
//     return this.workspace.projects;
//   }
//
//   // Temporary helper functions to support refactoring
//
//   // eslint-disable-next-line @typescript-eslint/no-explicit-any
//   getCli(): Record<string, any> | undefined {
//     return this.workspace.extensions['cli'] as Record<string, unknown>;
//   }
//
//   // eslint-disable-next-line @typescript-eslint/no-explicit-any
//   getProjectCli(projectName: string): Record<string, any> | undefined {
//     const project = this.workspace.projects.get(projectName);
//
//     return project?.extensions['cli'] as Record<string, unknown>;
//   }
//
//   save(): Promise<void> {
//     return workspaces.writeWorkspace(
//       this.workspace,
//       createWorkspaceHost(),
//       this.filePath,
//       workspaces.WorkspaceFormat.JSON,
//     );
//   }
//
//   static async load(workspaceFilePath: string): Promise<AngularWorkspace> {
//     const result = await workspaces.readWorkspace(
//       workspaceFilePath,
//       createWorkspaceHost(),
//       workspaces.WorkspaceFormat.JSON,
//     );
//
//     return new AngularWorkspace(result.workspace, workspaceFilePath);
//   }
// }

// const cachedWorkspaces = new Map<string, AngularWorkspace | undefined>();

// export async function getWorkspace(level: 'global'): Promise<AngularWorkspace>;
// export async function getWorkspace(level: 'local'): Promise<AngularWorkspace | undefined>;
// export async function getWorkspace( level: 'local' | 'global'): Promise<AngularWorkspace | undefined>;
// export async function getWorkspace(level: 'local' | 'global'): Promise<AngularWorkspace | undefined> {
//
//   if (cachedWorkspaces.has(level)) {
//     return cachedWorkspaces.get(level);
//   }
//
//   const configPath = level === 'local' ? projectFilePath() : globalFilePath();
//   if (!configPath) {
//     if (level === 'global') {
//       // Unlike a local config, a global config is not mandatory.
//       // So we create an empty one in memory and keep it as such until it has been modified and saved.
//       const globalWorkspace = new AngularWorkspace(
//         { extensions: {}, projects: new workspaces.ProjectDefinitionCollection() },
//         defaultGlobalFilePath,
//       );
//
//       cachedWorkspaces.set(level, globalWorkspace);
//
//       return globalWorkspace;
//     }
//
//     cachedWorkspaces.set(level, undefined);
//
//     return undefined;
//   }
//
//   try {
//     const workspace = await AngularWorkspace.load(configPath);
//     cachedWorkspaces.set(level, workspace);
//     return workspace;
//   } //
//   catch (error) {
//     throw new Error(
//       `Workspace config file cannot be loaded: ${configPath}` +
//         `\n${error instanceof Error ? error.message : error}`,
//     );
//   }
// }
//
// /**
//  * This method will load the workspace configuration in raw JSON format.
//  * When `level` is `global` and file doesn't exists, it will be created.
//  *
//  * NB: This method is intended to be used only for `ng config`.
//  */
// export async function getWorkspaceRaw(
//   level: 'local' | 'global' = 'local',
// ): Promise<[JSONFile | null, string | null]> {
//   let configPath = level === 'local' ? projectFilePath() : globalFilePath();
//
//   if (!configPath) {
//     if (level === 'global') {
//       configPath = defaultGlobalFilePath;
//       // Config doesn't exist, force create it.
//
//       const globalWorkspace = await getWorkspace('global');
//       await globalWorkspace.save();
//     } else {
//       return [null, null];
//     }
//   }
//
//   return [new JSONFile(configPath), configPath];
// }

// function findProjectByPath(workspace: AngularWorkspace, location: string): string | null {

//   const isInside = (base: string, potential: string): boolean => {
//     const absoluteBase = path.resolve(workspace.basePath, base);
//     const absolutePotential = path.resolve(workspace.basePath, potential);
//     const relativePotential = path.relative(absoluteBase, absolutePotential);
//     return !relativePotential.startsWith('..') && !path.isAbsolute(relativePotential);
//   };

//   const projects = Array.from(workspace.projects)
//     .map(([name, project]) => [project.root, name] as [string, string])
//     .filter((tuple) => isInside(tuple[0], location))
//     .sort((a, b) => b[0].length - a[0].length);

//   if (projects.length === 0) {
//     return null;
//   } else if (projects.length > 1) {
//     const found = new Set<string>();
//     const sameRoots = projects.filter((v) => {
//       if (!found.has(v[0])) {
//         found.add(v[0]);

//         return false;
//       }

//       return true;
//     });
//     if (sameRoots.length > 0) {
//       // Ambiguous location - cannot determine a project
//       return null;
//     }
//   }

//   return projects[0][1];
// }

// export function getProjectByCwd(workspace: AngularWorkspace): string | null {
//   if (workspace.projects.size === 1) {
//     // If there is only one project, return that one.
//     return Array.from(workspace.projects.keys())[0];
//   }

//   const project = findProjectByPath(workspace, process.cwd());
//   if (project) {
//     return project;
//   }

//   return null;
// }

// export async function getConfiguredPackageManager(): Promise<PackageManager | null> {
//   const getPackageManager = (source: json.JsonValue | undefined): PackageManager | null => {
//     if (isJsonObject(source)) {
//       const value = source['packageManager'];
//       if (value && typeof value === 'string') {
//         return value as PackageManager;
//       }
//     }

//     return null;
//   };

//   let result: PackageManager | null = null;
//   const workspace = await getWorkspace('local');
//   if (workspace) {
//     const project = getProjectByCwd(workspace);
//     if (project) {
//       result = getPackageManager(workspace.projects.get(project)?.extensions['cli']);
//     }

//     result ??= getPackageManager(workspace.extensions['cli']);
//   }

//   if (!result) {
//     const globalOptions = await getWorkspace('global');
//     result = getPackageManager(globalOptions?.extensions['cli']);
//   }

//   return result;
// }

// export async function getSchematicDefaults(
//   collection: string,
//   schematic: string,
//   project?: string | null,
// ): Promise<{}> {
//   const result = {};

//   const mergeOptions = (source: json.JsonValue | undefined): void => {
//     if (isJsonObject(source)) {
//       // Merge options from the qualified name
//       Object.assign(result, source[`${collection}:${schematic}`]);

//       // Merge options from nested collection collection
//       const collectionOptions = source[collection];
//       if (isJsonObject(collectionOptions)) {
//         Object.assign(result, collectionOptions[schematic]);
//       }
//     }
//   };

//   // Global level schematic options
//   const globalOptions = await getWorkspace('global');
//   mergeOptions(globalOptions?.extensions['collection']);

//   const workspace = await getWorkspace('local');
//   if (workspace) {
//     // Workspace level schematic options
//     mergeOptions(workspace.extensions['collection']);

//     project = project || getProjectByCwd(workspace);
//     if (project) {
//       // Project level schematic options
//       mergeOptions(workspace.projects.get(project)?.extensions['collection']);
//     }
//   }

//   return result;
// }

// export async function isWarningEnabled(warning: string): Promise<boolean> {
//   const getWarning = (source: json.JsonValue | undefined): boolean | undefined => {
//     if (isJsonObject(source)) {
//       const warnings = source['warnings'];
//       if (isJsonObject(warnings)) {
//         const value = warnings[warning];
//         if (typeof value == 'boolean') {
//           return value;
//         }
//       }
//     }
//   };
//
//   let result: boolean | undefined;
//
//   const workspace = await getWorkspace('local');
//   if (workspace) {
//     const project = getProjectByCwd(workspace);
//     if (project) {
//       result = getWarning(workspace.projects.get(project)?.extensions['cli']);
//     }
//
//     result = result ?? getWarning(workspace.extensions['cli']);
//   }
//
//   if (result === undefined) {
//     const globalOptions = await getWorkspace('global');
//     result = getWarning(globalOptions?.extensions['cli']);
//   }
//
//   // All warnings are enabled by default
//   return result ?? true;
// }
