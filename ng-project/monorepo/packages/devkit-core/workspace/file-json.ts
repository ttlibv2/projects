import { Node, parseTree } from 'jsonc-parser';
import { IWorkspace, FileReader, WorkspaceHost, FileWriter } from './definitions';
import { validateSchema } from '../utilities/validate-schema';

export interface ReaderResult {
    workspace: IWorkspace;
    jsonAst: Node;
    schemaPath?: string;
    filePath?: string;
}

export interface FileOption {
    schemaJsonPath?: () => string;
}

export async function readJsonWorkspace(host: WorkspaceHost, filePath: string, options?: FileOption): Promise<ReaderResult> {
    const raw = await host.readFile(filePath);
    if (raw === undefined) throw new Error(`Unable to read file [${filePath}].`);

    const ast: Node | undefined = parseTree(raw, undefined, { allowTrailingComma: true, disallowComments: false });
    if (ast?.type !== 'object' || !ast.children) {
        throw new Error('Invalid json file');
    }

// validate json schema
    const schemaPath = options?.schemaJsonPath ? options.schemaJsonPath() : undefined;
    if (!!schemaPath) await validateSchema(schemaPath, raw, false);

    return {
        workspace: JSON.parse(raw),
        jsonAst: ast,
        schemaPath: schemaPath,
        filePath: filePath
    };

}

async function writeJsonWorkspace(host: WorkspaceHost, workspace: IWorkspace, path?: string, options?: any): Promise<any> {

    const metadata = (workspace as JsonWorkspaceDefinition)[JsonWorkspaceSymbol];

    if (metadata) {
        if (!metadata.hasChanges) {
            return;
        }
        // update existing JSON workspace
        const data = updateJsonWorkspace(metadata);

        return host.writeFile(path ?? metadata.filePath, data);
    } else {
        // serialize directly
        if (!path) {
            throw new Error('path option is required');
        }

        const obj = convertJsonWorkspace(workspace, options.schema);
        const data = JSON.stringify(obj, null, 2);

        return host.writeFile(path, data);
    }

}