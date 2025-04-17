import { json } from "@angular-devkit/core";
import { NodeWorkflow } from "@angular-devkit/schematics/tools";
import { ISchematic } from "../collection/collection";

export function isNull(object: any) {
  return object === null || object === void 0;
}

/**
 *
 * @param str
 * @returns formated string
 * @description normalizes input to supported path and file name format.
 * Changes camelCase strings to kebab-case, replaces spaces with dash and keeps underscores.
 */
export function normalizeToKebabOrSnakeCase(str: string) {
  const STRING_DASHERIZE_REGEXP = /\s/g;
  const STRING_DECAMELIZE_REGEXP = /([a-z\d])([A-Z])/g;
  return str
    .replace(STRING_DECAMELIZE_REGEXP, "$1-$2")
    .toLowerCase()
    .replace(STRING_DASHERIZE_REGEXP, "-");
}

export function extractListSchematic(collectionName: string, cwd?: string) {
  cwd = cwd ?? process.cwd();

  const workflow = new NodeWorkflow(cwd, {});
  const collection = workflow.engine.createCollection(collectionName);
  const collectionDescriptions = [
    collection.description,
    ...(collection.baseDescriptions ?? []),
  ];

  const usedNames = new Set<string>();
  const schematics: ISchematic[] = [];

  for (const collectionDesc of collectionDescriptions) {
    const lsSchematicsDesc = Object.entries(collectionDesc.schematics);
    for (const [name, { description, aliases = [] }] of lsSchematicsDesc) {
      if (!usedNames.has(name)) {
        usedNames.add(name);
        const alias = aliases.find((a) => !usedNames.has(a)) ?? name;
        for (const alias of aliases) usedNames.add(alias);
        schematics.push({ name, alias, description });
      }
    }
  }
  return schematics.sort((a, b) =>
    a.name < b.name ? -1 : a.name > b.name ? 1 : 0,
  );
}
