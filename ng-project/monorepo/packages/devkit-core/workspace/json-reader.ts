import { isJsonObject, JsonValue } from '@angular-devkit/core';
import { getNodeValue, parseTree, Node, findNodeAtLocation } from 'jsonc-parser';
import { NotObjectError, NullError, StringMapListener } from '../utilities';
import { WorkspaceHost } from './ws.host';
import { JsonWorkspaceMetadata, JsonWorkspaceSymbol } from './json/metadata';
import { Builder, CliProp, CompilerOptions, PluginOptions, ProjectMap, ProjectProp, WorkspaceProp } from './ws.type';
import { createVirtualAstObject } from './json-proxy';

const defaultContext: Partial<ParserContext> = {
    trackChanges: true,
    extensionFields: {},
    error: (message, _node) => {throw new Error(message);},
    warn: (message, _node) => console.warn(message)
};

interface ParserContext {
    readonly host: WorkspaceHost;
    readonly metadata: JsonWorkspaceMetadata;
    readonly trackChanges: boolean;
    readonly extensionFields: Record<string, any>
    error: (message: string, node: JsonValue) => void;
    warn: (message: string, node: JsonValue) => void;
}

interface ReaderResult {
    workspace: WorkspaceProp;
    host: WorkspaceHost;
    filePath: string;
}

interface ObjectVirtualInput {
    supportFields?: string[] | 'all';
    excludeFields?: string[];
    pathChange: (jsonPath: string[]) => string[];
    loopItem?: (parent: Node, name: string, value: any) => any | undefined;
    extensionKey?: string;
    trackChanges?: boolean;
}

export async function readWorkspace(host: WorkspaceHost, filePath: string, options?: Omit<ParserContext, 'host' | 'metadata'>): Promise<ReaderResult> {

    const raw = await host.readFile(filePath);
    if (raw === undefined) {
        throw new NullError(`Unable to read workspace file '${filePath}'.`);
    }

    const workspaceNode = parseTree(raw, undefined, { allowTrailingComma: true, disallowComments: false });
    if (workspaceNode?.type !== 'object' || !workspaceNode.children) {
        throw new NotObjectError(`Invalid workspace file '${filePath}'`);
    }

    // context
    const context: any = Object.assign({...defaultContext, host}, options);
    context.metadata = new JsonWorkspaceMetadata(filePath, workspaceNode, raw);

    let node: Node;

    // parse workspace
    const workspace: any = {
        ...parseWorkspace(workspaceNode, context),
        [JsonWorkspaceSymbol]: context.metadata
    };

    return {filePath, host, workspace} as any;
}

function parseWorkspace(workspaceNode: Node, context: ParserContext): WorkspaceProp {
    return parseObjectVirtual(workspaceNode, context, {
        supportFields: 'all' , //[...WORKSPACE_FIELDS],
        trackChanges: false,
        pathChange: jsonPath => jsonPath,
        loopItem: (parent, name, value) => {
            if('cli' == name) {
                let cliNode = findObjectNode(parent, context, value, 'cli');
                return parseCli(cliNode, context);
            }
            else if('projects' == name) {
                let projectsNode = findObjectNode(parent, context, value, 'projects');
                let projects = parseProjects(projectsNode, context);
                let listener: StringMapListener<ProjectProp> | undefined;

                if(context.trackChanges) {
                    listener = (n, newValue, collection) => context.metadata
                        .addChange(['projects', n], newValue, 'project');
                }

                //console.log(`parseWorkspace: `, projects['tsweb']);
                return new ProjectMap(projects, listener);
            }
        }
    })
}

function parseProjects(projectsNode: Node, context: ParserContext): Record<string, ProjectProp> {
    return parseObjectVirtual(projectsNode, context, {
        supportFields: 'all',
        pathChange: jsonPath => [],
        loopItem: (parent, name, value) => {
            const childNode = findObjectNode(parent, context, value, name);
            if (childNode == null) throw new Error(`Invalid value field 'project'`);
            return parseProject(name, childNode, context)
        }
    });
}

function parseProject(name: string, projectNode: Node, context: ParserContext): ProjectProp {
    return parseObjectVirtual(projectNode, context, {
        supportFields: 'all', //PROJECT_EXTENSIONS,
        //excludeFields: PROJECT_EXTENSIONS,
        pathChange: jsonPath => ['projects', name, ...jsonPath ],
        loopItem: (node, name, value) => {
            if('compilerOptions' == name) {
                const childNode = findObjectNode(node, context,value, 'compilerOptions');
                if(childNode == null) throw new Error(`Invalid value field 'compilerOptions'`);
                return parseCompilerOption(childNode, context, ['projects', name]);
            }
        }
    })
}

function parseCompilerOption(node: Node, context: ParserContext, parentPath: string[]): CompilerOptions {
    return parseObjectVirtual(node, context, {
        supportFields: 'all',//COMPILER_OPTION_EXTENSIONS,
        //excludeFields: COMPILER_OPTION_EXTENSIONS,
        pathChange: jsonPath => [...parentPath, 'compilerOptions', ...jsonPath ],
        loopItem: (node, name, value) => {
            if('builder' == name) {
                const childNode = findObjectNode(node, context,value, 'builder');
                if(childNode == null) throw new Error(`Invalid value field 'builder'`);
                return parseBuilder(childNode, context);
            }
            else if('plugins' == name) {
                const childNode = findObjectNode(node, context, value, 'plugins');
                if(childNode == null) throw new Error(`Invalid value field 'plugins'`);
                return parsePlugin(childNode, context, parentPath);
            }
        }
    })
}

function parsePlugin(pluginNode: Node, context: ParserContext, parentPath: string[]): PluginOptions {
    return parseObjectVirtual(pluginNode, context, {
        supportFields: 'all', // ['name', 'options'],
        excludeFields: ['name', 'options'],
        pathChange: jsonPath => [...parentPath, 'plugins', ...jsonPath],
        loopItem: (parent, name, value) => {
            if('options' == name) {
                const childNode = findNodeAtLocation(parent, ['options']);
                if(!childNode || childNode.type !== 'array') {
                    throw new Error(`The node ${name} invalid array.`);
                }
            }
        }
    });
}

function parseBuilder(builderNode: Node, context: ParserContext, jsonParentPath: string[] = []): Builder {
    return parseObjectVirtual(builderNode, context, {
        supportFields: 'all', //['type', 'options'],
        pathChange: jsonPath => [...jsonParentPath, 'builder', ...jsonPath]
    });
}

function parseCli(cliNode: Node, context: ParserContext): CliProp {
    return parseObjectVirtual(cliNode, context, {
        supportFields: 'all', //CLI_EXTENSIONS,
        //excludeFields: CLI_EXTENSIONS,
        pathChange: path => ['cli', ...path]
    });
}

function parseObjectVirtual(node: Node, context: ParserContext, options: ObjectVirtualInput) {
    const { supportFields = 'all', excludeFields = [], pathChange, loopItem , extensionKey = '', trackChanges} = options;

    let properties: Record<string, JsonValue> | undefined;
    let extensions: Record<string, JsonValue> | undefined;


    let trackChanges2 = trackChanges ?? context.trackChanges;

    if (!trackChanges2) {
        properties = Object.create(null);
        extensions = Object.create(null);
    }

    const nodeValue = getNodeValue(node);
    const extensionFields = extensionKey ? (context.extensionFields[extensionKey] || []) : [];
    const includeFields: string[] = supportFields == 'all' ? Object.keys(nodeValue) : supportFields;

    for (const [name, value] of Object.entries(nodeValue)) {

        if (supportFields == 'all' || supportFields.includes(name)) {
            let childNode = findNodeAtLocation(node, [name]);
            let object = loopItem ? loopItem(node, name, value) : null;
            if (properties) properties[name] = object || value;
        } //
        else {
            if (!extensionFields.has(name) && !/^[a-z]{1,3}-.*/.test(name)) {
                context.warn(`Workspace extension with invalid name (${name}) found.`, name);
            }

            if (extensions) {
                extensions[name] = value as JsonValue;
            }

        }
    }
    
    const changePage = (path: string[], value: any) => {
        const newPath = pathChange ? pathChange(path) : [];
        if(newPath?.length) context.metadata.addChange(newPath, value);
    }
    
    const base = supportFields == 'all' ? {} : {
        extensions: extensions ?? createVirtualAstObject(nodeValue, {
            exclude: [...excludeFields, ...supportFields],
            listener: (path, value) => changePage(path, value)
        })
    };

    const baseKeys = new Set(Object.keys(base));
    const include = [...includeFields, ...baseKeys];

    const nodeObject = properties ?? createVirtualAstObject(nodeValue, {
        include: include.length ? include : undefined,
        listener: (path, value) => changePage(path, value)
    });

    return Object.assign(nodeObject, base) as any;
}


function findObjectNode(parentNode: Node, context: ParserContext, value: any, ...childNodeName: string[]) {
    const cliNode = findNodeAtLocation(parentNode, [...childNodeName]);
    if (!cliNode || !isJsonObject(value)) {
        throw new Error(`Invalid "${childNodeName}" field found`, value);
    }
    return cliNode;
}