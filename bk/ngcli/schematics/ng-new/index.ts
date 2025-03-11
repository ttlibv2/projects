import { NodePackageInstallTask, RepositoryInitializerTask} from '@angular-devkit/schematics/tasks';
import { Rule,SchematicContext,Tree, apply, applyTemplates, url, chain, strings, mergeWith, move, empty, schematic, applyToSubtree, externalSchematic, MergeStrategy} from '@angular-devkit/schematics';
import {Schema as NgNewOptions, PackageManager} from '@schematics/angular/ng-new/schema';
import {latestVersions} from '@schematics/angular/utility/latest-versions';
import {Schema } from './schema';

function deleteAngularJson(options: Schema): Rule {
  return (host: Tree) => {
    let path = options.directory + '/' + 'angular.json';
    if(host.exists(path)) {
      host.delete(path);
    }

  };
}

function updateNgCliJson(options: Schema): Rule {
  return (host: Tree) => {
    let path = options.directory + '/' + '.project.json';
    host.create(path, JSON.stringify(options, null, 2));
  }
}


// Instead of `any`, it would make sense here to get a schema-to-dts package and output the
// interfaces so you get type-safe options.
export default function (options: Schema): Rule {

  if (!options.directory) {
    // If scoped project (i.e. "@foo/bar"), convert directory to "foo/bar".
    options.directory = options.name.startsWith('@') ? options.name.slice(1) : options.name;
  }

  const ngNewOption: NgNewOptions = {
    name: options.name,
    version: options.version,
    commit: options.commit ?? false,
    createApplication: options.createApplication ?? false,
    directory: options.directory,
    experimentalZoneless: options.experimentalZoneless,
    inlineStyle: options.inlineStyle,
    inlineTemplate: options.inlineTemplate,
    minimal: options.minimal,
    newProjectRoot: options.appRoot ?? options.packageRoot,
    packageManager: PackageManager.Pnpm,
    prefix: options.prefix ?? 't',
    routing: options.routing,
    serverRouting: options.serverRouting,
    ssr: options.ssr,
    skipGit: options.skipGit,
    skipInstall: options.skipInstall,
    skipTests: options.skipTests,
    standalone: options.standalone,
    strict: options.strict,
    style: options.style,
    viewEncapsulation: options.viewEncapsulation
  };

  

  const template = apply(url('./files'), [
    applyTemplates({
      ...strings, ...options, dot: '.', latestVersions,
    }),
    move(options.directory)
  ]);


  return chain([
    externalSchematic('@schematics/angular', 'ng-new', ngNewOption),
    updateNgCliJson(options),
    mergeWith(template, MergeStrategy.Overwrite),
    //deleteAngularJson(options)
  


  ]);
}
