import {JsonWorkspaceOptions, WorkspaceDefinition, workspaceFiles, WorkspaceFormat, WorkspaceHost} from './ws.type';
import {getSystemPath, normalize, join, basename} from '@angular-devkit/core';
import { Node, findNodeAtLocation, getNodeValue, parseTree } from 'jsonc-parser';

export async function readJsonWorkspace(path: string, host: WorkspaceHost, options: JsonWorkspaceOptions = {}): Promise<WorkspaceDefinition> {
  const raw = await host.readFile(path);
  if (raw === undefined) {
    throw new Error('Unable to read workspace file.');
  }

  const ast = parseTree(raw, undefined, { allowTrailingComma: true, disallowComments: false });
  if (ast?.type !== 'object' || !ast.children) {
    throw new Error('Invalid workspace file - expected JSON object.');
  }

  // Version check
  const versionNode = findNodeAtLocation(ast, ['version']);
  if (!versionNode) {
    throw new Error('Unknown format - version specifier not found.');
  }
  const version = versionNode.value;
  if (version !== 1) {
    throw new Error(`Invalid format version detected - Expected:[ 1 ] Found: [ ${version} ]`);
  }

  const context: ParserContext = {
    host,
    metadata: new JsonWorkspaceMetadata(path, ast, raw),
    trackChanges: true,
    unprefixedWorkspaceExtensions: new Set([
      ...ANGULAR_WORKSPACE_EXTENSIONS,
      ...(options.allowedWorkspaceExtensions ?? []),
    ]),
    unprefixedProjectExtensions: new Set([
      ...ANGULAR_PROJECT_EXTENSIONS,
      ...(options.allowedProjectExtensions ?? []),
    ]),
    error(message, _node) {
      // TODO: Diagnostic reporting support
      throw new Error(message);
    },
    warn(message, _node) {
      // TODO: Diagnostic reporting support
      // eslint-disable-next-line no-console
      console.warn(message);
    },
  };

  const workspace = parseWorkspace(ast, context);

  return workspace;
}
