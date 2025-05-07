import { apply, applyTemplates, chain, filter, mergeWith, move, Rule, SchematicContext, strings, Tree, url } from '@angular-devkit/schematics';
import { latestVersions } from '../utility/last-version';
import { NodePackageInstallTask, RepositoryInitializerTask } from '@angular-devkit/schematics/tasks';
import { Schema as NgAppOption } from './schema';

const packageManager: string = 'pnpm';

export default function(options: NgAppOption): Rule {

    return async (tree: Tree) => {

        if (!options.directory) {
            options.directory = options.name.startsWith('@') ? options.name.slice(1) : options.name;
        }

        options.appsDir = options.appsDir || 'apps';
        options.libsDir = options.libsDir || 'packages';
        options.loggerLevel = options.loggerLevel || 'info';

        return chain([
           copyFiles(options),
           installPackage(options)
        ]);
    };
}

function copyFiles(options: NgAppOption): Rule {
    return (tree: Tree, context: SchematicContext) => {
        latestVersions['@angular/cli'] = options.ngVersion;

        return mergeWith(apply(url('./files'), [
            filter(path => {
                context.logger.warn(`Path: ${path}`)
                return true;
            }),
            applyTemplates({
                ...options,
                utils: strings, dot: '.',
                latestVersions: latestVersions
            }),
            move(options.directory)
        ]));
    };

}

function installPackage(options: NgAppOption) {
    return (tree: Tree, context: SchematicContext) => {

        let packageTask;

        if (!options.skipInstall) {
            packageTask = context.addTask(
                new NodePackageInstallTask({
                    workingDirectory: options.directory,
                    packageManager: packageManager
                })
            );
        }
        if (!options.skipGit) {
            const commit =
                typeof options.commit == 'object' ? options.commit : options.commit ? {} : false;

            context.addTask(
                new RepositoryInitializerTask(options.directory, commit),
                packageTask ? [packageTask] : []
            );
        }
    };
}