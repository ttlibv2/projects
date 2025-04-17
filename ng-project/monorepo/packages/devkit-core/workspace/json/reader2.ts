import { isJsonObject, JsonValue } from '@angular-devkit/core';
import { getNodeValue, parseTree, Node, findNodeAtLocation } from 'jsonc-parser';
import { NotObjectError, NullError, StringMapListener } from '../../utilities';
import { WorkspaceHost } from '../ws.host';
import { BaseChangeInput, ChangeValues, JsonWorkspaceMetadata, JsonWorkspaceSymbol } from './metadata';
import { COMPILER_OPTION_EXTENSIONS, CompilerOptions, ProjectMap, ProjectProp, WorkspaceProp } from '../ws.type';
import { ChangeListener, createVirtualAstObject } from './utilities';

const WORKSPACE_EXTENSIONS = ['name', 'appsDir', 'libsDir', 'cli', 'defaultPkg', '$schema'];
const PROJECT_EXTENSIONS = ['cli', 'schematics', 'projectType', 'i18n'];
const PROJECT_FIELDS = ['name', 'root', 'build', 'framework', 'projectType'];

interface ParserContext {
    readonly host: WorkspaceHost;
    readonly metadata: JsonWorkspaceMetadata;
    readonly trackChanges: boolean;
    error:(message: string, node: JsonValue) => void;
    warn:(message: string, node: JsonValue)=> void;
}

export interface JsonWorkspaceOptions {
    allowedProjectExtensions?: string[];
    allowedWorkspaceExtensions?: string[];
}

export interface ReaderResult {
    workspace: WorkspaceProp;
    host: WorkspaceHost;
    filePath: string;
}

export async function readWorkspace(host: WorkspaceHost, filePath: string): Promise<ReaderResult> {

    const raw = await host.readFile(filePath);
    if (raw === undefined) {
        throw new NullError(`Unable to read workspace file '${filePath}'.`);
    }

    const ast = parseTree(raw, undefined, { allowTrailingComma: true, disallowComments: false });
    if (ast?.type !== 'object' || !ast.children) {
        throw new NotObjectError(`Invalid workspace file '${filePath}'`);
    }

    const context: ParserContext = {
        host, trackChanges: true,
        metadata: new JsonWorkspaceMetadata(filePath, ast, raw),
        error: (message, _node) => {throw new Error(message);},
        warn: (message, _node) => console.warn(message)
    };

    const workspace = parseWorkspace(ast, context);
    return { workspace, filePath, host };
}

function parseWorkspace(workspaceNode: Node, context: ParserContext): WorkspaceProp {
    const jsonMetadata = context.metadata;
    let properties: Record<string, JsonValue> | undefined;
    let extensions: Record<string, JsonValue> | undefined;
    let cliProp: Record<string, JsonValue> | undefined;
    let projects: Record<string, ProjectProp> | undefined;

    if (!context.trackChanges) {
        extensions = Object.create(null);
        properties = Object.create(null);
        cliProp = Object.create(null);
        projects = Object.create(null);
    }

    // Might be something to look at moving forward to optimize.
    const workspaceNodeValue = getNodeValue(workspaceNode);

    for (const [name, value] of Object.entries<JsonValue>(workspaceNodeValue)) {
        if(!WORKSPACE_EXTENSIONS.includes(name)) extensions


        //is projects
        if (name === 'projects') {
            const nodes = findNodeAtLocation(workspaceNode, ['projects']);
            if (!isJsonObject(value) || !nodes) {
                context.error('Invalid "projects" field found; expected an object.', value);
                continue;
            }

            projects = parseProjectsObject(nodes, context);
        } else if (WORKSPACE_EXTENSIONS.includes(name) && properties) {
            properties[name] = value;
        }

        // is extension workspace property
        // else if (!context.unPrefixedWorkspaceExtensions.has(name) && !/^[a-z]{1,3}-.*/.test(name)) {
        //     context.warn(`Workspace extension with invalid name (${name}) found.`, name);
        // }
        // allowedWorkspaceExtensions
        else if (extensions) {
            extensions[name] = value;
        }
    }

    let collectionListener: StringMapListener<ProjectProp> | undefined;
    if (context.trackChanges) {
        collectionListener = (name, newValue) => {
            jsonMetadata.addChange(['projects', name], newValue, 'project');
        };
    }

    const workspaceProp: any = Object.create({});
    workspaceProp.projects = new ProjectMap(projects, collectionListener);
    workspaceProp[JsonWorkspaceSymbol] = jsonMetadata;

    return Object.assign(workspaceProp, {
        extensions: extensions ?? createPropWsExt(workspaceNodeValue, jsonMetadata),
        ...createPropWs(properties, workspaceNodeValue, jsonMetadata)
    });
}

function createPropWs(wsProperties: any, workspaceNodeValue: any, jsonMetadata: any) {
    return wsProperties ?? createVirtualAstObject(workspaceNodeValue, {
        include: [...WORKSPACE_EXTENSIONS],
        listener(path, value) {
            jsonMetadata.addChange(path, value);
        }
    });
}

function createPropWsExt(workspaceNodeValue: any, jsonMetadata: any) {
    return createVirtualAstObject(workspaceNodeValue, {
        exclude: ['$schema', 'projects', ...WORKSPACE_EXTENSIONS],
        listener(path, value) {
            jsonMetadata.addChange(path, value);
        }
    });
}

function parseProjectsObject(projectsNode: Node, context: ParserContext): Record<string, ProjectProp> {
    const projects: Record<string, ProjectProp> = Object.create(null);

    for (const [name, value] of Object.entries<JsonValue>(getNodeValue(projectsNode))) {
        const nodes = findNodeAtLocation(projectsNode, [name]);
        if (!isJsonObject(value) || !nodes) {
            context.warn('Skipping invalid project value; expected an object.', value);
            continue;
        }

        projects[name] = parseProject(nodes, context, name);
    }

    return projects;
}

function parseProject(projectNode: Node, context: ParserContext, projectName: string): ProjectProp {
    const jsonMetadata = context.metadata;
    let extensions: Record<string, JsonValue> | undefined;
    let properties: Record<string, string> | undefined;

    if (!context.trackChanges) {
        extensions = Object.create(null);
        properties = Object.create(null);
    }

    const projectNodeValue = getNodeValue(projectNode);
    const errorFields = PROJECT_FIELDS.filter(f => !(f in projectNodeValue));
    if (errorFields.length > 0) {
        let fields = errorFields.join(', ').trim();
        throw new Error(`Project "${projectName}" is missing a required property "[${fields}]".`);
    }

    for (const [name, value] of Object.entries<JsonValue>(projectNodeValue)) {
        switch (name) {
            case 'name':
            case 'root':
            case 'build':
            case 'framework':
            case 'projectType':
                if (typeof value !== 'string') {
                    context.warn(`Project property "${name}" should be a string.`, value);
                }
                if (properties) {
                    properties[name] = value as string;
                }
                break;
            default:
                // if (!context.unPrefixedProjectExtensions.has(name) && !/^[a-z]{1,3}-.*/.test(name)) {
                //     context.warn(
                //         `Project '${projectName}' contains extension with invalid name (${name}).`,
                //         name
                //     );
                // }
                if (extensions) {
                    extensions[name] = value;
                }
                break;
        }
    }

    const base = {
        extensions: extensions ??
            createVirtualAstObject(projectNodeValue, {
                exclude: [...PROJECT_FIELDS],
                listener(path, value) {
                    jsonMetadata.addChange(['projects', projectName, ...path], value);
                }
            })
    };

    const baseKeys = new Set(Object.keys(base));
    const project =
        properties ??
        createVirtualAstObject<ProjectProp>(projectNodeValue, {
            include: [...PROJECT_FIELDS, ...baseKeys],
            listener(path, value) {
                if (!baseKeys.has(path[0])) {
                    jsonMetadata.addChange(['projects', projectName, ...path], value);
                }
            }
        });

    return Object.assign(project, base) as ProjectProp;
}

function parseCompilerOption(node: Node, context: ParserContext): CompilerOptions {
    return parseObject(node, context, COMPILER_OPTION_EXTENSIONS, (jsonPath, value) => ({
        jsonPath: ['compilerOptions', ...jsonPath],
        value: value
    }));
}


function parseObject<T>(node: Node, context: ParserContext, fieldsInclude: string[], changes: BaseChangeInput): T {
    let properties: Record<string, JsonValue> | undefined;
    let extensions: Record<string, JsonValue> | undefined;

    if (!context.trackChanges) {
        properties = Object.create(null);
        extensions = Object.create(null);
    }

    const object = getNodeValue(node);
    for (const [name, value] of Object.entries(object)) {
        if (fieldsInclude.includes(name)) {
            if (properties) {
                properties[name] = value as JsonValue;
            }
        } else if (extensions) {
            extensions[name] = value as JsonValue;
        }
    }

    const base = {
        extensions: extensions ?? createVirtualAstObject(object, {
            exclude: fieldsInclude,
            listener: (path, pValue) => {
                // const { jsonPath, value, type } = changes.extListener(path, pValue);
                // context.metadata.addChange(jsonPath, value, type);
            }
        })
    };

    const baseKeys = new Set(Object.keys(base));
    const props = properties ?? createVirtualAstObject(object, {
        include: [...fieldsInclude, ...baseKeys],
        listener: (path, pValue) => {
            // const { jsonPath, value, type } = changes.extListener(path, pValue);
            // context.metadata.addChange(jsonPath, value, type);
        }
    });

    return Object.assign(props, base) as any;
}