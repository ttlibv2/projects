import { NodeWorkflow } from '@angular-devkit/schematics/tools';
import { ISchematic } from '../collection';

export function extractListSchematic(collectionName: string, cwd?: string) {
    cwd = cwd ?? process.cwd();

    const workflow = new NodeWorkflow(cwd, {});
    const collection = workflow.engine.createCollection(collectionName);
    const collectionDescriptions = [collection.description, ...(collection.baseDescriptions ?? []),];

    const usedNames = new Set<string>();
    const schematics: ISchematic[] = [];

    for (const collectionDesc of collectionDescriptions) {
        const lsSchematicsDesc = Object.entries(collectionDesc.schematics);
        for (const [name, {description, aliases = []}] of lsSchematicsDesc) {
            if (!usedNames.has(name)) {
                usedNames.add(name);
                const alias = aliases.find((a) => !usedNames.has(a)) ?? name;
                for (const alias of aliases) usedNames.add(alias);
                schematics.push({name, alias, description});
            }
        }
    }
    return schematics.sort((a, b) =>
        a.name < b.name ? -1 : a.name > b.name ? 1 : 0,
    );

}