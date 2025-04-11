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
        readonly prop?: ws.WorkspaceProp) {
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

    get defaultPkg(): string {
        return this.prop.defaultPkg;
    }

    get cli(): ws.CliProp {
        return this.prop.cli;
    }

    get collection(): Set<string> {
        return this.cli?.schematicCollections;
    }

    get projects(): ws.ProjectMap | null {
        return this.prop.projects ?? null;
    }

    async add(prop: ws.ProjectProp, host?: ws.WorkspaceHost): Promise<void> {
        this.projects.add(prop);
        await this.write(host);
    }

    async write(host?: ws.WorkspaceHost): Promise<void> {
        await ws.writeWorkspace(this.prop, host ?? this.host, this.filePath);
    }

    static async load(path: string): Promise<DevWorkspace> {
        const { workspace, host, filePath } = await ws.readWorkspace(ws.createPromiseHost(), path);
        return new DevWorkspace(filePath, host, workspace);
    }

    static async global(): Promise<DevWorkspace> {
        const configPath = getConfigPath('global');
        if (!configPath || !existsSync(configPath)) {
            const globalPath = defaultGlobalPath();
            const host = ws.createPromiseHost();
            return new DevWorkspace(globalPath, host);
        }
        else return DevWorkspace.load(configPath);
    }

    static async project(): Promise<DevWorkspace | undefined> {
        const configPath = getConfigPath('local');
        return configPath ? DevWorkspace.load(configPath) : undefined;
    }

}