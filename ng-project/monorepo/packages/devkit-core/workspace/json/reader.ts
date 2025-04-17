import { isJsonObject, JsonValue } from '@angular-devkit/core';
import { getNodeValue, parseTree, Node, findNodeAtLocation } from 'jsonc-parser';
import { isJsonValue, NotObjectError, NullError, StringMapListener } from '../../utilities';
import { WorkspaceHost } from '../ws.host';
import { BaseChangeInput, ChangeValues, JsonWorkspaceMetadata, JsonWorkspaceSymbol } from './metadata';
import { CLI_EXTENSIONS, CliProp, COMPILER_OPTION_EXTENSIONS, CompilerOptions, ProjectMap, ProjectProp, WorkspaceProp } from '../ws.type';
import { ChangeListener, createVirtualAstObject } from './utilities';

const WORKSPACE_EXTENSIONS = ['name', 'appsDir', 'libsDir', 'cli', 'defaultPkg', '$schema'];
const PROJECT_EXTENSIONS = ['cli', 'schematics', 'projectType', 'i18n'];
const PROJECT_FIELDS = ['name', 'root', 'build', 'framework', 'projectType'];

const WS_EXTENSIONS = ['name', 'appsDir', 'libsDir', 'defaultPkg', '$schema' ];

interface ParserContext {
    readonly host: WorkspaceHost;
    readonly metadata: JsonWorkspaceMetadata;
    readonly trackChanges: boolean;
    error:(message: string, node: JsonValue) => void;
    warn:(message: string, node: JsonValue)=> void;
}

type JsonPathChange = (jsonPath: string[]) => string[];

interface ReaderResult {
    workspace: WorkspaceProp;
    host: WorkspaceHost;
    filePath: string;
}

export async function readWorkspace(host: WorkspaceHost, filePath: string): Promise<ReaderResult> {

    const raw = await host.readFile(filePath);
    if (raw === undefined) {
        throw new NullError(`Unable to read workspace file '${filePath}'.`);
    }

    const workspaceNode = parseTree(raw, undefined, { allowTrailingComma: true, disallowComments: false });
    if (workspaceNode?.type !== 'object' || !workspaceNode.children) {
        throw new NotObjectError(`Invalid workspace file '${filePath}'`);
    }

    const context: ParserContext = {
        host, trackChanges: true,
        metadata: new JsonWorkspaceMetadata(filePath, workspaceNode, raw),
        error: (message, _node) => {throw new Error(message);},
        warn: (message, _node) => console.warn(message)
    };

    // parse workspace
    let cli: Record<string, any>;
    let projects: Record<string, any>;

    const workspaceNodeValue = getNodeValue(workspaceNode);
    for(const [name, value] of Object.entries(workspaceNodeValue)) {

        // node [cli]
        if(name == 'cli') {
            const childNode = findNode(workspaceNode, ['projects'], context);
            if(childNode == null) throw new Error(`Invalid value field 'cli'`);
            cli = parseCli(childNode, context);
        }
        else if(name == 'projects') {
            const childNode = findNode(workspaceNode, ['projects'], context);
            if(childNode == null) throw new Error(`Invalid value field 'project'`);
            projects = parseProjects(childNode, context);
        }



    }



















    return { workspace, filePath, host };
}

function parseCli(node: Node, context: ParserContext): CliProp {
    return parseObjectVirtual(node, context, CLI_EXTENSIONS, path => ['cli', ...path]);
}

function parseProjects(projectsNode: Node, context: ParserContext): Record<string, ProjectProp>  {
    const nodeValue = getNodeValue(projectsNode);
    const projects: Record<string, ProjectProp> = Object.create(null);
    for(const [name, value] of Object.entries(nodeValue)) {
        const nodeName = findNodeAtLocation(projectsNode, [name]);
        if (!nodeName || !isJsonObject(nodeName.value)) {
            throw new Error(`Invalid project value at ${name}; expected an object.`, nodeName?.value);
        }
        projects[name] = parseProject(name, nodeName, context);
    }
    return projects;
}

function parseProject(name: string, projectNode: Node, context: ParserContext): ProjectProp {
    
}

function parseCompilerOption(node: Node, context: ParserContext): CompilerOptions {
    throw new Error(`Method not implement.`);
}

function findNode(parentNode: Node, childNodeName: string[], context: ParserContext) {
    const cliNode = findNodeAtLocation(parentNode, childNodeName);
    if(!cliNode || !isJsonObject(cliNode.value)) {
        context.error(`Invalid "${childNodeName.join('/')}" field found`, cliNode?.value);
        return null;
    }
    return cliNode;
}


function parseObjectVirtual(node: Node, context: ParserContext, includeFields: string[], change: JsonPathChange) {
    const nodeValue = getNodeValue(node);

    const base = createVirtualAstObject(nodeValue, {
        exclude: [...includeFields],
        listener: (path, value) => context.metadata.addChange(change(path), value)
    });

    const properties = createVirtualAstObject(nodeValue, {
        include: [...includeFields, 'extensions'],
        listener: (path, value) => context.metadata.addChange(change(path), value)
    });

    return Object.assign(properties, base) as any;
}