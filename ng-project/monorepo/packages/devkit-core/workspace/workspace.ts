import { JsonValue } from '@angular-devkit/core';
import { StringMap, StringMapListener } from './definitions';
import { isJsonValue } from '../utilities/is-json-value';

export interface WorkspaceDefinition {
    readonly extensions: Record<string, JsonValue | undefined>;
    readonly projects: ProjectCollection;
    readonly name: string;
    readonly appsDir: string;
    readonly libsDir: string;
}

export interface ProjectDefinition {
    readonly extensions: Record<string, JsonValue | undefined>;

    root: string;
    prefix?: string;
    sourceRoot?: string;
}



export class ProjectCollection extends StringMap<ProjectDefinition> {

    constructor(
        initial?: Record<string, ProjectDefinition>,
        listener?: StringMapListener<ProjectDefinition>) {
        super(initial, listener);
    }

    add(definition: {
        name: string;
        root: string;
        sourceRoot?: string;
        prefix?: string;
       // targets?: Record<string, TargetDefinition | undefined>;
        [key: string]: unknown;
    }): ProjectDefinition {
        if (this.has(definition.name)) {
            throw new Error('Project name already exists.');
        }
        this._validateName(definition.name);

        const project: ProjectDefinition = {
            root: definition.root,
            prefix: definition.prefix,
            sourceRoot: definition.sourceRoot,
            //targets: new TargetDefinitionCollection(),
            extensions: {},
        };

        // if (definition.targets) {
        //     for (const [name, target] of Object.entries(definition.targets)) {
        //         if (target) {
        //             project.targets.set(name, target);
        //         }
        //     }
        // }

        for (const [name, value] of Object.entries(definition)) {
            switch (name) {
                case 'name':
                case 'root':
                case 'sourceRoot':
                case 'prefix':
                case 'targets':
                    break;
                default:
                    if (isJsonValue(value)) {
                        project.extensions[name] = value;
                    } else {
                        throw new TypeError(`"${name}" must be a JSON value.`);
                    }
                    break;
            }
        }

        super.set(definition.name, project);

        return project;
    }

    override set(name: string, value: ProjectDefinition): this {
        this._validateName(name);

        super.set(name, value);

        return this;
    }

    private _validateName(name: string): void {
        if (typeof name !== 'string' || !/^(?:@\w[\w.-]*\/)?\w[\w.-]*$/.test(name)) {
            throw new Error('Project name must be a valid npm package name.');
        }
    }
}