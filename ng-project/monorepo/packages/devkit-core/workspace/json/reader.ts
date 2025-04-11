import { isJsonObject, JsonValue } from '@angular-devkit/core';
import { getNodeValue, parseTree, Node, findNodeAtLocation } from 'jsonc-parser';
import { StringMapListener } from '../../utilities';
import { WorkspaceHost } from '../ws.host';
import { JsonWorkspaceMetadata, JsonWorkspaceSymbol } from './metadata';
import { ProjectMap, ProjectProp, WorkspaceProp } from '../ws.type';
import { createVirtualAstObject } from './utilities';

const WORKSPACE_EXTENSIONS = ['name', 'appsDir', 'libsDir', 'cli', 'defaultPkg', '$schema'];
const PROJECT_EXTENSIONS = ['cli', 'schematics', 'projectType', 'i18n'];
const PROJECT_FIELDS = ['name', 'root', 'build', 'framework', 'projectType'];

interface ParserContext {
    readonly host: WorkspaceHost;
    readonly metadata: JsonWorkspaceMetadata;
    readonly trackChanges: boolean;
    readonly unPrefixedWorkspaceExtensions: ReadonlySet<string>;
    readonly unPrefixedProjectExtensions: ReadonlySet<string>;

    error(message: string, node: JsonValue): void;

    warn(message: string, node: JsonValue): void;
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

export async function readWorkspace(host: WorkspaceHost,
   filePath: string, options: JsonWorkspaceOptions = {}): Promise<ReaderResult> {

    const raw = await host.readFile(filePath);
    if (raw === undefined) {
        throw new Error(`Unable to read workspace file '${filePath}'.`);
    }

    const ast = parseTree(raw, undefined, { allowTrailingComma: true, disallowComments: false });
    if (ast?.type !== 'object' || !ast.children) {
        throw new Error(`Invalid workspace file '${filePath}' - expected JSON object.`);
    }

    const context: ParserContext = {
        host, trackChanges: true,
        metadata: new JsonWorkspaceMetadata(filePath, ast, raw),
        unPrefixedWorkspaceExtensions: new Set([...WORKSPACE_EXTENSIONS, ...(options.allowedWorkspaceExtensions ?? [])]),
        unPrefixedProjectExtensions: new Set([...PROJECT_EXTENSIONS, ...(options.allowedProjectExtensions ?? [])]),
        error(message, _node) {
            throw new Error(message);
        },
        warn(message, _node) {
            console.warn(message);
        }
    };

    const workspace = parseWorkspace(ast, context);
    return {workspace, filePath, host};
}

function parseWorkspace(workspaceNode: Node, context: ParserContext): WorkspaceProp {
    const jsonMetadata = context.metadata;
    let wsProperties: Record<string, JsonValue> | undefined;
    let extensions: Record<string, JsonValue> | undefined;
    let projects: Record<string, ProjectProp> | undefined = undefined;

    if (!context.trackChanges) {
        extensions = Object.create(null);
        wsProperties = Object.create(null);
    }

    // Might be something to look at moving forward to optimize.
    const workspaceNodeValue = getNodeValue(workspaceNode);
    for (const [name, value] of Object.entries<JsonValue>(workspaceNodeValue)) {
        //is projects
        if (name === 'projects') {
            const nodes = findNodeAtLocation(workspaceNode, ['projects']);
            if (!isJsonObject(value) || !nodes) {
                context.error('Invalid "projects" field found; expected an object.', value);
                continue;
            }

            projects = parseProjectsObject(nodes, context);
        }
        else if (WORKSPACE_EXTENSIONS.includes(name) && wsProperties) {
            wsProperties[name] = value;
        }

        // is extension workspace property
        else if (!context.unPrefixedWorkspaceExtensions.has(name) && !/^[a-z]{1,3}-.*/.test(name)) {
            context.warn(`Workspace extension with invalid name (${name}) found.`, name);
        }
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
    workspaceProp.projects =  new ProjectMap(projects, collectionListener);
    workspaceProp[JsonWorkspaceSymbol] = jsonMetadata;

   return Object.assign(workspaceProp, {
        extensions:  extensions ?? createPropWsExt(workspaceNodeValue, jsonMetadata),
        ...createPropWs(wsProperties, workspaceNodeValue, jsonMetadata)
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
    })
}

function parseProjectsObject(projectsNode: Node, context: ParserContext): Record<string, ProjectProp> {
    const projects: Record<string, ProjectProp> = Object.create(null);

    for (const [name, value] of Object.entries<JsonValue>(getNodeValue(projectsNode))) {
        const nodes = findNodeAtLocation(projectsNode, [name]);
        if (!isJsonObject(value) || !nodes) {
            context.warn('Skipping invalid project value; expected an object.', value);
            continue;
        }

        projects[name] = parseProject(name, nodes, context);
    }

    return projects;
}

function parseProject(projectName: string, projectNode: Node, context: ParserContext): ProjectProp {
    const jsonMetadata = context.metadata;
    //let targets, asTargets = false;
    let extensions: Record<string, JsonValue> | undefined;
    let properties: Record<string, string> | undefined;

    if (!context.trackChanges) {
        // If not tracking changes, the parser will store the values directly in standard objects
        extensions = Object.create(null);
        properties = Object.create(null);
    }

    const projectNodeValue = getNodeValue(projectNode);
    const errorFields = PROJECT_FIELDS.filter(f => !(f in projectNodeValue));
    if(errorFields.length > 0) {
        let fields = errorFields.join(', ').trim();
        throw new Error(`Project "${projectName}" is missing a required property "[${fields}]".`);
    }

    for (const [name, value] of Object.entries<JsonValue>(projectNodeValue)) {
        switch (name) {
            // case 'targets':
            // case 'architect': {
            //     const nodes = findNodeAtLocation(projectNode, [name]);
            //     if (!isJsonObject(value) || !nodes) {
            //         context.error(`Invalid "${name}" field found; expected an object.`, value);
            //         break;
            //     }
            //     hasTargets = true;
            //     targets = parseTargetsObject(projectName, nodes, context);
            //     jsonMetadata.hasLegacyTargetsName = name === 'architect';
            //     break;
            // }
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
                if (!context.unPrefixedProjectExtensions.has(name) && !/^[a-z]{1,3}-.*/.test(name)) {
                    context.warn(
                        `Project '${projectName}' contains extension with invalid name (${name}).`,
                        name,
                    );
                }
                if (extensions) {
                    extensions[name] = value;
                }
                break;
        }
    }

    // let collectionListener: DefinitionCollectionListener<TargetDefinition> | undefined;
    // if (context.trackChanges) {
    //     collectionListener = (name, newValue, collection) => {
    //         if (hasTargets) {
    //             jsonMetadata.addChange(['projects', projectName, 'targets', name], newValue, 'target');
    //         } else {
    //             jsonMetadata.addChange(
    //                 ['projects', projectName, 'targets'],
    //                 collection,
    //                 'targetcollection',
    //             );
    //         }
    //     };
    // }

    const base = {
        //targets: new TargetDefinitionCollection(targets, collectionListener),

        // If not tracking changes the `extensions` variable will contain the parsed
        // values.  Otherwise the extensions are tracked via a virtual AST object.
        extensions: extensions ??
            createVirtualAstObject(projectNodeValue, {
                //exclude: ['architect', 'prefix', 'root', 'sourceRoot', 'targets'],
                exclude: [...PROJECT_FIELDS],
                listener(path, value) {
                    jsonMetadata.addChange(['projects', projectName, ...path], value);
                },
            }),
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
            },
        });

    return Object.assign(project, base) as ProjectProp;
}