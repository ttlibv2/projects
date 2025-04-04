import { apply, chain, externalSchematic, MergeStrategy, mergeWith, Rule, Tree, url } from '@angular-devkit/schematics';
import { Style, ViewEncapsulation } from '@schematics/angular/application/schema';
import { Schema as NgNewSchema } from '@schematics/angular/ng-new/schema';
import { Schema as SchemaOption } from './schema';
import { getWorkspace } from '../../utility/get-workspace';
import { deleteAll } from '@ngdev/devkit-core';

export default function (options: SchemaOption): Rule {
  return async (host: Tree) => {
    const { appsDir, cli: {ngVersion} } = getWorkspace(host);

    let path = options.name;
    if(options.name.startsWith('@') && options.name.includes('/')) {
      const pos = options.name.indexOf('/');
      path = options.name.substring(pos + 1);
    }

    options.direction =`${appsDir}/${path}`;

    const ngOption: NgNewSchema = {
      name: options.name,
      prefix: options.prefix ||  't',
      strict: options.strict ||  true,
      minimal: options.minimal ||  false,
      standalone: true,
      viewEncapsulation: ViewEncapsulation.None,
      ssr: options.ssr ||  false,
      inlineStyle: options.inlineStyle || false,
      skipInstall: options.skipInstall ||  false,
      routing: options.routing ||  true,
      serverRouting: options.serverRouting || false,
      inlineTemplate: options.inlineTemplate || false,
      style: options.style ||  Style.Scss,
      skipTests: options.skipTests ||  true,
      version: options.ngVersion || ngVersion,
      createApplication: true,
      directory: options.direction
    };

    return chain([
      externalSchematic('@schematics/angular', 'ng-new', ngOption),
      updateAngularJsonFile(options),
      deleteAllFiles(options.direction),
    ]);

  };


}

function updateAngularJsonFile(options: SchemaOption): Rule {
  return (host: Tree) => {
    const path = options.direction + '/angular.json';
    const json: any = JSON.parse(host.readText(path));
    delete json.projects[options.name].architect.test;
    host.overwrite(path, JSON.stringify(json, null, 2));
    return host;
  };
}

function deleteAllFiles(dir: string): Rule {
  return (host: Tree) => {
    const files = ['.vscode', '.gitignore', '.editorconfig', 'README.md'].map(f => `${dir}/${f}`);
    files.forEach(file => deleteAll(host, file));
  };
}


// const ngOption: AppSchema = {
//   name: options.name,
//   prefix: options.prefix ||  't',
//   strict: options.strict ||  true,
//   minimal: options.minimal ||  false,
//   standalone: true,
//   viewEncapsulation: ViewEncapsulation.None,
//   ssr: options.ssr ||  false,
//   inlineStyle: options.inlineStyle || false,
//   skipInstall: options.skipInstall ||  false,
//   routing: options.routing ||  true,
//   serverRouting: options.serverRouting || false,
//   inlineTemplate: options.inlineTemplate || false,
//   style: options.style ||  Style.Scss,
//   skipTests: options.skipTests ||  true,
//   skipPackageJson: options.skipPackageJson ||  false,
//   projectRoot: `${appsDir}/${options.name}`,
// };