import { JsonChange, JsonWorkspaceMetadata, JsonWorkspaceSymbol } from './json/metadata';
import { CliProp, ProjectProp, WorkspaceProp } from './ws.type';
import { WorkspaceHost } from './ws.host';
import { applyEdits, modify } from 'jsonc-parser';
import { JsonObject, JsonValue } from '@angular-devkit/core';
import { EOL } from 'node:os';

export async function writeWorkspace(host: WorkspaceHost, workspace: WorkspaceProp,
    path?: string, options: { schema?: string; override?: boolean } = {}): Promise<void> {
    const metadata = (workspace as any)[JsonWorkspaceSymbol];

    if(!!options?.override || !metadata) {
        if (!path) throw new Error('path option is required');
        const obj = convertJsonWorkspace(workspace, options.schema);
        const data = JSON.stringify(obj, null, 2);
        return host.writeFile(path, data);
    }
    else if (!!metadata?.hasChanges) {
        const data = updateJsonWorkspace(metadata);
        return host.writeFile(path ?? metadata.filePath, data);
    }

}


function convertJsonWorkspace(workspace: WorkspaceProp, schema?: string): JsonObject {
    const exclude = ['$schema', 'projects', 'cli', 'name', 'appsDir', 'libsDir'];
    return copyObject(workspace as any, exclude, () => ({
        $schema: schema || workspace.$schema || './node_modules/@ngdev/cli/lib/config/schema.json',
        name: workspace.name,
        appsDir: workspace.appsDir,
        libsDir: workspace.libsDir,
        cli: convertCli(workspace.cli),
        projects: convertJsonProjectCollection(workspace.projects.entries())
    }));
}

function convertCli(props: CliProp): JsonObject {
    return copyObject(props, [], () => ({}));
}

function convertJsonProjectCollection(
    collection: Iterable<[string, ProjectProp]>
): JsonObject {
    const projects = Object.create(null) as JsonObject;
    for (const [projectName, project] of collection) {
        projects[projectName] = convertJsonProject(project);
    }

    return projects;
}


function convertJsonProject(project: ProjectProp) {
    return copyObject(project, ['compilerOptions'], () => ({
        compilerOptions: copyObject(project.compilerOptions as any)
    }));
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
        const edits = modify(content, jsonPath, normalizeValue(value, type), {
            formattingOptions: {
                insertSpaces: true,
                tabSize: 2,
                eol: getEOL(content)
            }
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

function copyObject(object: Record<string, any>, exclude: string[] = [], other?: () => Record<string, any>): any {
    if(object === undefined || object === null) return undefined;
    exclude = [...exclude, 'extensions'];
    const keys = Object.keys(object).filter(key => !exclude.includes(key));
    const jsonData = keys.reduce((json, property) => ({...json, [property]: convertValue(object[property])}), {});
    return {...jsonData, ...object.extensions, ...(other ? other() : {})};
}

function convertValue(value: any): any {
    if(typeof value !== 'object') return value;
    else if(!Array.isArray(value)) return copyObject(value);
    else return value.map(obj => convertValue(value));
}