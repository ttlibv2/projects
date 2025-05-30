import { Tree } from "@angular-devkit/schematics";
import { JSONFile } from "./json-file";

const PKG_JSON_PATH = '/package.json';

export enum NodeDependencyType {
  Default = 'dependencies',
  Dev = 'devDependencies',
  Peer = 'peerDependencies',
  Optional = 'optionalDependencies',
}

export interface NodeDependency {
  type: NodeDependencyType;
  name: string;
  version: string | 'latest';
  overwrite?: boolean;
}

const ALL_DEPENDENCY_TYPE = [
  NodeDependencyType.Default,
  NodeDependencyType.Dev,
  NodeDependencyType.Optional,
  NodeDependencyType.Peer,
];

export function addPackageJsonDependency(tree: Tree, dependency: NodeDependency, pkgJsonPath = PKG_JSON_PATH): void {
  const json = JSONFile.read(pkgJsonPath,tree);

  const { overwrite, type, name, version } = dependency;
  const path = [type, name];
  if (overwrite || !json.get(path)) {
    json.modify(path, version);
  }
}

export function removePackageJsonDependency(
  tree: Tree, name: string,
  pkgJsonPath = PKG_JSON_PATH): void {
  const json = JSONFile.read(pkgJsonPath,tree);

  for (const depType of ALL_DEPENDENCY_TYPE) {
    json.remove([depType, name]);
  }
}

export function getPackageJsonDependency(tree: Tree, name: string, pkgJsonPath = PKG_JSON_PATH): NodeDependency | null {
  const json = JSONFile.read(pkgJsonPath, tree);

  for (const depType of ALL_DEPENDENCY_TYPE) {
    const version = json.get([depType, name]);
    if (typeof version === 'string') {
      return { type: depType, name: name, version, };
    }
  }

  return null;
}