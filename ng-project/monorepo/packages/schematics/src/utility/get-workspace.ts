import { SchematicsException, Tree } from '@angular-devkit/schematics';
import { parse } from 'jsonc-parser';

export function getWorkspace(host: Tree) {
    const json = host.read('project.json');
    if (!json) throw new SchematicsException(`Could not find "project.json"`);
    else return parse(json.toString(), [], { allowTrailingComma: true });
}