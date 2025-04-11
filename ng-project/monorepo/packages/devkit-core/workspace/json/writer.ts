import { JsonChange, JsonWorkspaceMetadata, JsonWorkspaceSymbol } from './metadata';
import { CliProp, ProjectProp, WorkspaceProp } from '../ws.type';
import { WorkspaceHost } from '../ws.host';
import { applyEdits, modify } from 'jsonc-parser';
import { JsonObject, JsonValue } from '@angular-devkit/core';
import { EOL } from 'node:os';

export async function writeWorkspace(workspace: WorkspaceProp, host: WorkspaceHost,
    path?: string, options: { schema?: string; } = {}): Promise<void> {
    const metadata = (workspace as any)[JsonWorkspaceSymbol];

    if (metadata) {
        if (!metadata.hasChanges) {
            return;
        }
        // update existing JSON workspace
        const data = updateJsonWorkspace(metadata);

        return host.writeFile(path ?? metadata.filePath, data);
    } //
    else {
        // serialize directly
        if (!path) {
            throw new Error('path option is required');
        }

        const obj = convertJsonWorkspace(workspace, options.schema);
        const data = JSON.stringify(obj, null, 2);

        return host.writeFile(path, data);
    }
}


function convertJsonWorkspace(workspace: WorkspaceProp, schema?: string): JsonObject {
    const {name, $schema, appsDir, libsDir, defaultPkg = null, cli, extensions, projects} = workspace;

    return  {
        $schema: schema || $schema || './node_modules/@ngdev/cli/lib/config/schema.json',
        name, appsDir, libsDir, ...extensions, defaultPkg,
        cli: convertCli(cli),
        projects: convertJsonProjectCollection(projects)
    };
}


function convertCli(props: CliProp): JsonObject {
    const {extensions, schematicCollections, packageManager, ngVersion, nestVersion} = props;
    return {...extensions,
        packageManager, ngVersion, nestVersion,
        schematicCollections: [...schematicCollections]
    };

}

function convertJsonProjectCollection(
    collection: Iterable<[string, ProjectProp]>,
): JsonObject {
    const projects = Object.create(null) as JsonObject;
    for (const [projectName, project] of collection) {
        projects[projectName] = convertJsonProject(project);
    }

    return projects;
}


function convertJsonProject(project: ProjectProp): JsonObject {
    const {name, root, framework, extensions, projectType, build = null} = project;

    //let targets: JsonObject | undefined;
    // if (project.targets.size > 0) {
    //     targets = Object.create(null) as JsonObject;
    //     for (const [targetName, target] of project.targets) {
    //         targets[targetName] = convertJsonTarget(target);
    //     }
    // }

    return {
        ...extensions,
        name, root, framework, projectType, build
    };
}


function normalizeValue(value: JsonChange['value'] | undefined, type: JsonChange['type']): JsonValue | undefined {

    if (value === undefined) {
        return undefined;
    }

    switch (type) {
        case 'project':
            return convertJsonProject(value as ProjectProp);

        case 'projectCollection': {
            const projects = convertJsonProjectCollection(value as Iterable<[string, ProjectProp]>);
            return isEmpty(projects) ? undefined : projects;
        }
        // case 'target':
        //     return convertJsonTarget(value as TargetDefinition);
        // case 'targetcollection': {
        //     const targets = convertJsonTargetCollection(value as Iterable<[string, TargetDefinition]>);
        //
        //     return isEmpty(targets) ? undefined : targets;
        // }
        default:
            return value as JsonValue;
    }
}

function updateJsonWorkspace(metadata: JsonWorkspaceMetadata): string {
    let { raw: content } = metadata;
    const { changes, hasLegacyTargetsName } = metadata;

    for (const { jsonPath, value, type } of changes.values()) {
        // Determine which key to use if (architect or targets)
        if (hasLegacyTargetsName && jsonPath[2] === 'targets') {
            jsonPath[2] = 'architect';
        }

        // TODO: `modify` re-parses the content every time.
        // See: https://github.com/microsoft/node-jsonc-parser/blob/35d94cd71bd48f9784453b2439262c938e21d49b/src/impl/edit.ts#L18
        // Ideally this should accept a string or an AST to avoid the potentially expensive repeat parsing operation.
        const edits = modify(content, jsonPath, normalizeValue(value, type), {
            formattingOptions: {
                insertSpaces: true,
                tabSize: 2,
                eol: getEOL(content),
            },
        });

        content = applyEdits(content, edits);
    }

    return content;
}

function getEOL(content: string): string {
    const CRLF = '\r\n';
    const LF = '\n';
    const newlines = content.match(/\r?\n/g);

    if (newlines?.length) {
        const crlf = newlines.filter((l) => l === CRLF).length;
        const lf = newlines.length - crlf;

        return crlf > lf ? CRLF : LF;
    }

    return EOL;
}

function isEmpty(obj: object | undefined): boolean {
    return obj === undefined || Object.keys(obj).length === 0;
}