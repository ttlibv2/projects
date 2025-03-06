import { NodePackageInstallTask, RepositoryInitializerTask} from '@angular-devkit/schematics/tasks';
import { Rule,SchematicContext,Tree, apply, applyTemplates, url, chain, strings, mergeWith, move, empty, schematic, applyToSubtree, externalSchematic, MergeStrategy, asSource} from '@angular-devkit/schematics';
import {Schema as AppOptions, } from '@schematics/angular/application/schema';
import {Schema as NgNewOptions2 } from '../ng-new/schema';
import {latestVersions} from '@schematics/angular/utility/latest-versions';

 
export default function (options: AppOptions): Rule {

  return async (host: Tree, context: SchematicContext) => {
    let json: any = host.readText('.project.json');
    let jv:NgNewOptions2 = JSON.parse(json);
    let appDir = jv.appRoot + '/' + options.name;
    

  
  const ngAppOption: AppOptions = {
    projectRoot: appDir,
    name: options.name,
    inlineStyle: options.inlineStyle,
    inlineTemplate: options.inlineTemplate,
    prefix: options.prefix,
    viewEncapsulation: options.viewEncapsulation,
    routing: options.routing,
    style: options.style,
    skipTests: options.skipTests,
    skipPackageJson: false,
    skipInstall: true,
    strict: options.strict,
    minimal: options.minimal,
    standalone: options.standalone,
    ssr: options.ssr,
    serverRouting: options.serverRouting,
    experimentalZoneless: options.experimentalZoneless,
  };

  return chain([
    externalSchematic('@schematics/angular', 'application', ngAppOption),
    move(appDir)
  ]);

  };


}
