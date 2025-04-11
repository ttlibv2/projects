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

export interface CliProp {
    extensions?: Record<string, JsonValue>;
    packageManager: string;
    schematicCollections: Set<string>;
    ngVersion: string;
    nestVersion: string;
}

export interface ProjectProp {
    name: string;
    root: string;
    build?: string;
    framework: 'angular' | 'nestjs';
    projectType: 'app' | 'lib';
    extensions?: Record<string, JsonValue>;
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