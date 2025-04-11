import { Schema } from './schema';
import { apply, applyTemplates, chain, mergeWith, move, Rule, SchematicContext, strings, Tree, url } from '@angular-devkit/schematics';
import { latestVersions } from '../utility/last-version';
import { NodePackageInstallTask, RepositoryInitializerTask } from '@angular-devkit/schematics/tasks';

const packageManager: string = 'pnpm';

export default function (options: Schema): Rule {

  if (!options.directory) {
    // If scoped project (i.e. "@foo/bar"), convert directory to "foo/bar".
    options.directory = options.name.startsWith('@') ? options.name.slice(1) : options.name;
  }

  console.log(options)

  options.appsDir = options.appsDir || 'apps';
  options.libsDir = options.libsDir || 'packages';

  return chain([
  //  copyFiles(options),
    // installPackage(options),
  ]);
}

function copyFiles(options: Schema): Rule {
  return (tree: Tree, context: SchematicContext) => {
    context.logger.warn(`copyFiles`);

    return mergeWith(apply(url('./files'), [
      applyTemplates({
        ...options,
        utils: strings, dot: '.',
        latestVersions: latestVersions, pnpmVersion: '10.7.0'
      }),
      move(options.directory)
    ]));
  }

}

function installPackage(options: Schema) {
  return(tree: Tree, context: SchematicContext) => {
    context.logger.warn(`installPackage`);

    let packageTask;

    if (!options.skipInstall) {
      packageTask = context.addTask(
        new NodePackageInstallTask({
          workingDirectory: options.directory,
          packageManager: packageManager,
        }),
      );
    }
    if (!options.skipGit) {
      const commit =
        typeof options.commit == 'object' ? options.commit : options.commit ? {} : false;

      context.addTask(
        new RepositoryInitializerTask(options.directory, commit),
        packageTask ? [packageTask] : [],
      );
    }
  }
}