import { JsonValue } from '@angular-devkit/core';
import { StringMap, StringMapListener } from '../utilities';

export function defaultWorkspace():WorkspaceProp {
    return {
        $schema: './node_modules/@ngdev/cli/lib/config.schema.json',
        appsDir: 'apps',
        libsDir: 'packages',
        projects: new ProjectMap(),
        cli: {
            packageManager: 'pnpm',
            schematicCollections: new Set<string>(),
            ngVersion: 'ANGULAR::VERSION',
            nestVersion: 'NESTJS::VERSION'
        }
    } as WorkspaceProp;
}

export const COMPILER_OPTION_EXTENSIONS = ['tsConfigPath', 'plugin', 'assets', 'deleteOutDir', 'manualRestart', 'builder'];
export const WORKSPACE_EXTENSIONS = ['$schema','name','appsDir', 'libsDir', 'cli', 'projects', 'defaultProject'];
export const CLI_EXTENSIONS = ['packageManager', 'collections', 'ngVersion', 'nestVersion'];
export const PROJECT_EXTENSIONS = ['framework', 'type', 'name', 'root', 'sourceRoot', 'entryFile', 'exec'];



export interface SwcBuilderOptions {
    swcPath?: string;
    outDir?: string;
    filenames?: string[];
    sync?: boolean;
    extensions?: string[];
    copyFiles?: boolean;
    includeDotfiles?: boolean;
    quiet?: boolean;
}

export interface WebpackBuilderOptions {
    configPath?: string;
}

export interface TscBuilderOptions {
    configPath?: string;
}

export type BuilderVariant = 'tsc' | 'swc' | 'webpack';
export type Builder =
    | BuilderVariant
    | { type: 'webpack'; options?: WebpackBuilderOptions; }
    | { type: 'swc'; options?: SwcBuilderOptions; }
    | { type: 'tsc'; options?: TscBuilderOptions; };


export interface CompilerOptions {
    tsConfigPath?: string;
    plugins?: string[] | PluginOptions[];
    assets?: string[];
    deleteOutDir?: boolean;
    manualRestart?: boolean;
    builder?: Builder;
    extensions?: Record<string, JsonValue>;
}

export interface PluginOptions {
    name: string;
    options: Record<string, any>[];
}

export interface CliProp {
    extensions?: Record<string, JsonValue>;
    packageManager: string;
    collections: Set<string>;
    ngVersion: string;
    nestVersion: string;
}
export interface ProjectProp {
    framework: 'angular' | 'nestjs';
    type: 'app' | 'lib';
    name: string;
    root: string;
    sourceRoot?: string;
    entryFile?: string;
    exec?: string;
    extensions?: Record<string, JsonValue>;
    compilerOptions?: CompilerOptions;
}

export interface WorkspaceProp {
    $schema: string;
    name: string;
    appsDir: string;
    libsDir: string;
    defaultPkg?: string;
    cli: CliProp;
    projects: ProjectMap;
    extensions?: Record<string, JsonValue>;
}

export class ProjectMap extends StringMap<ProjectProp> {

    constructor(
        initial?: Record<string, ProjectProp>,
        listener?: StringMapListener<ProjectProp>) {
        super(initial, listener);
    }

    add(definition: ProjectProp) {
        const {name, root} = definition;

        if (this.has(name)) {
            throw new Error(`Project name [${name}] already exists.`);
        }

        this._validateName(name);

        const project: ProjectProp = {...definition};
        super.set(definition.name, project);
        return project;
    }

    override set(name: string, value: ProjectProp): this {
        this._validateName(name);
        super.set(name, value);
        return this;
    }

    private _validateName(name: string): void {
        if (name === undefined || !/^(?:@\w[\w.-]*\/)?\w[\w.-]*$/.test(name)) {
            throw new Error(`Project name must be a valid 'pnpm' package name.`);
        }
    }

}