import {Schema} from './schema';
import {apply, applyTemplates, chain, empty, mergeWith, move, Rule, strings, url} from '@angular-devkit/schematics';
import {latestVersions} from '../utility/last-version';
import {NodePackageInstallTask, RepositoryInitializerTask} from '@angular-devkit/schematics/tasks';

const packageManager: string = 'pnpm';

export default function (options: Schema): Rule {

  if (!options.directory) {
    // If scoped project (i.e. "@foo/bar"), convert directory to "foo/bar".
    options.directory = options.name.startsWith('@') ? options.name.slice(1) : options.name;
  }

  options.appsDir = options.appsDir || 'apps';
  options.libsDir = options.libsDir || 'packages';

  return chain([
    copyFiles(options),
    (tree, context) => {
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
  ]);
}

function copyFiles(options: Schema): Rule {
  return mergeWith(apply(url('./files'), [
    applyTemplates({...options, utils: strings,  dot: '.', latestVersions: latestVersions, pnpmVersion: '10.7.0'}),
    move(options.directory)
  ]));
}