import {
  apply,
  applyTemplates,
  chain,
  empty,
  externalSchematic,
  mergeWith,
  move,
  Rule,
  strings,
  url
} from '@angular-devkit/schematics';
import {Schema as AppSchema} from '@schematics/angular/application/schema';

export default function (options: any): Rule {
  const ngOption: AppSchema = {
    name: options.name ?? 'web',
    prefix: options.prefix ?? 't',
    strict: options.strict ?? true,
    minimal: options.minimal ?? false,
    standalone: true,
    viewEncapsulation: options.viewEncapsulation ?? 'None',
    ssr: options.ssr ?? false,
    inlineStyle: options.inlineStyle ?? false,
    skipInstall: options.skipInstall ?? false,
    //skipGit: options.skipGit ?? true,
    routing: options.routing ?? true,
    inlineTemplate: options ??false,
    style: options.style ?? 'scss',
    experimentalZoneless: options.experimentalZoneless ?? false,
    serverRouting: options.serverRouting ?? false,
    skipTests: options.skipTests ?? true,
    skipPackageJson: options.skipPackageJson ?? false,
    projectRoot: 'apps/web',
  };

  return chain([
    externalSchematic('@schematics/angular', 'application', ngOption)
  ]);
}


