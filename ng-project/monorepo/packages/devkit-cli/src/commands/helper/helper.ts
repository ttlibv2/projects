import { SchematicInfo } from '../core/cmd-type';
import { Collection } from '../../collection';
import { CommandContext } from '../core/abstract.cmd';
import { getProjectByCwd } from '../../workspace';

export function parseSchematicInfo(schematic: string): SchematicInfo  {
    if (!schematic?.includes(":")) return {};
    else {
        const [collectionName, schematicName] = schematic.split(":", 2);
        return { collectionName, schematicName };
    }
}

export function getResolvePaths(context: CommandContext, collectionName: string): string[] {
    const { workspace, root } = context;

    // Resolve relative collections from the location of `ngdev-cli.json`
    if (collectionName[0] == ".") return [root];

    // Global
    else if(!workspace) return [__dirname, process.cwd()];

    // Workspace: Favor __dirname for @ngdev/schematics to use the build-in version
    else if(collectionName == Collection.NgDevSC) return [__dirname, process.cwd(), root];

    // Workspace: other => collectionName != Collection.NgDevSC
    else return [process.cwd(), root, __dirname];
}

export function getProjectName(context: CommandContext): string | undefined {
    const { workspace } = context;
    if (!workspace) return undefined;

    const projectName = getProjectByCwd(workspace);
    if (projectName) return projectName;

    return undefined;
}