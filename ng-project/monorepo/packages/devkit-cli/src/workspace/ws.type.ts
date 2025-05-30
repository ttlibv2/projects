import { workspaces as ws } from '@ngdev/devkit-core';
import * as path from 'node:path';
import { defaultGlobalPath, getConfigPath } from './ws.help';
import { existsSync } from 'node:fs';

function applyWorkspaceProp(prop: ws.WorkspaceProp) {
  return Object.assign({}, ws.defaultWorkspace(), prop);
}

export class DevWorkspace {
  readonly baseDir: string;

  constructor(
    readonly filePath: string,
    readonly host: ws.WorkspaceHost,
    readonly prop?: ws.WorkspaceProp,
  ) {
    this.baseDir = path.dirname(filePath);
    this.prop = applyWorkspaceProp(prop);
  }

  /** The name monorepo project */
  get name(): string {
    return this.prop.name;
  }

  /** Path where new application will be created. */
  get appsDir(): string {
    return this.prop.appsDir;
  }

  /** Path where new library will be created. */
  get libsDir(): string {
    return this.prop.libsDir;
  }

  get defaultProject(): string {
    return this.prop.defaultProject;
  }

  get cli(): ws.CliProp {
    return this.prop.cli;
  }

  getSchematics(schematic: string) {
    const schematics = this.prop?.schematics ?? {};
    return schematics[schematic] ?? {};
  }

  getProject(name: string): ws.ProjectProp | null {
    return this.projects?.get(name) ?? null;
  }

  get collections(): Record<string, string[]> {
    return this.cli?.collections;
  }

  get projects(): ws.ProjectMap | null {
    return this.prop.projects ?? null;
  }

  async add(prop: ws.ProjectProp, host?: ws.WorkspaceHost): Promise<void> {
    this.projects.add(prop);
    await this.write({ host });
  }

  async write(options?: {host?: ws.WorkspaceHost, path?: string}): Promise<void> {
    let host = options?.host ?? this.host;
    let path = options?.path ?? this.filePath;
    await ws.writeWorkspace(host, this.prop, path, {override: true});
  }

  hasProject(name: string): boolean {
    return this.projects?.has(name) ?? false;
  }

  getCollectionProject(name: string): string[] {
    return this.projects?.get(name)?.collections ?? [];
  }

  static async load(path: string): Promise<DevWorkspace> {
    const { workspace, host, filePath } = await ws.readWorkspace(ws.createPromiseHost(), path);
    return new DevWorkspace(filePath, host, workspace);
  }

  static async global(): Promise<DevWorkspace> {
    const configPath = getConfigPath("global");
    if (!configPath || !existsSync(configPath)) {
      const globalPath = defaultGlobalPath();
      const host = ws.createPromiseHost();
      return new DevWorkspace(globalPath, host);
    } else return DevWorkspace.load(configPath);
  }

  static async project(): Promise<DevWorkspace | undefined> {
    const configPath = getConfigPath("local");
    return configPath ? DevWorkspace.load(configPath) : undefined;
  }


  toJson() {
    return {
      ...this.prop,
      cli: { ...this.prop?.cli },
      projects: {
        ...this.prop.projects
      }
    }
  }
}