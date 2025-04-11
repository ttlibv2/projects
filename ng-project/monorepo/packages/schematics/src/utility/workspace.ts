import { noop, Rule, Tree } from '@angular-devkit/schematics';
import { workspaces } from '@ngdev/devkit-core';

const DEFAULT_WORKSPACE_PATH = '/project.json';

type WorkspaceProp = workspaces.WorkspaceProp;

export const workspace = {

    /**
     * Reads a workspace file (`project.json`) from the provided {@link Tree} instance.
     *
     * @param tree A schematics {@link Tree} instance used to access the workspace file.
     * @param path The path where a workspace file should be found. If a file is specified, the file
     * path will be used. If a directory is specified, the file `angular.json` will be used from
     * within the specified directory. Defaults to `/angular.json`.
     * @returns A {@link WorkspaceDefinition} representing the workspace found at the specified path.
     */
    get: async (tree: Tree, path = DEFAULT_WORKSPACE_PATH): Promise<WorkspaceProp> => {
        const host = workspaces.createTreeHost(tree);
        const { workspace } = await workspaces.readWorkspace(host, path);
        return workspace;
    },

    /**
     * Writes a workspace file (`angular.json`) to the provided {@link Tree} instance.
     * The spacing and overall layout of an existing file (including comments) will be maintained where
     * possible when writing the file.
     *
     * @param tree A schematics {@link Tree} instance used to access the workspace file.
     * @param workspace The {@link WorkspaceProp} to write.
     * @param path The path where a workspace file should be written. If a file is specified, the file
     * path will be used. If not provided, the definition's underlying file path stored during reading
     * will be used.
     */
    write: async (tree: Tree, workspace: WorkspaceProp, path?: string): Promise<void> => {
        const host = workspaces.createTreeHost(tree);
        return workspaces.writeWorkspace(workspace, host, path);
    },

    /**
     * Updates the workspace file (`angular.json`) found within the root of the schematic's tree.
     * The workspace object model can be directly modified within the provided updater function
     * with changes being written to the workspace file after the updater function returns.
     * The spacing and overall layout of the file (including comments) will be maintained where
     * possible when updating the file.
     *
     * @param updater An update function that can be used to modify the object model for the
     * workspace. A {@link WorkspaceProp} is provided as the first argument to the function.
     */
    update: (updater: (workspace: WorkspaceProp) => void | Rule | PromiseLike<void | Rule>): Rule => {
        return async (tree: Tree) => {
            const host = workspaces.createTreeHost(tree);
            const ws = await workspace.get(tree);
            const result = await updater(ws);
            await workspaces.writeWorkspace(ws, host);
            return result || noop;
        };
    }


}