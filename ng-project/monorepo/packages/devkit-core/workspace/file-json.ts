import { Node, parseTree } from 'jsonc-parser';
import { IWorkspace, FileReader, WorkspaceHost, FileWriter } from './definitions';
import { validateSchema } from '../utilities/validate-schema';

export interface ReadResult {
    workspace: IWorkspace;
    jsonAst: Node;
    schemaPath?: string;
    filePath?: string;
}

export interface FileOption {
    schemaJsonPath?: () => string;
}

export const jsonFile: FileReader<any> & FileWriter = {

    async read(filePath: string, host: WorkspaceHost, options?: FileOption): Promise<ReadResult> {
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

}