import { json, workspaces } from '@angular-devkit/core';

export type WorkspaceHost = workspaces.WorkspaceHost;
export type WorkspaceDefinition = workspaces.WorkspaceDefinition;

/**
 * Supported workspace formats
 */
export enum WorkspaceFormat {
  JSON,
}

export interface JsonWorkspaceOptions {
  allowedProjectExtensions?: string[];
  allowedWorkspaceExtensions?: string[];
}


// NOTE: future additions could also perform content analysis to determine format/version
export const workspaceFiles: Record<string, WorkspaceFormat> = {
  'project.json': WorkspaceFormat.JSON,
};

