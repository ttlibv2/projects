import { spawnAsync } from './commands';
import { findPackageJson, isDependencyInstalled } from './dependencies';
import { logger } from './logger';

export async function checkCollection(
  collection: string,
  path: string = '',
  packageManager: string = 'pnpm',
  command: string = 'add',
  dryRun: boolean = false,
  registry?: string,
): Promise<boolean> {
  try {
    const doesPackageJSONExist = await findPackageJson(path ?? process.cwd());
    const isInstalled = await isDependencyInstalled(
      collection,
      path ?? __dirname,
    );

    if (!isInstalled) {
      logger.info('Temporal installation package: ' + collection, [
        `command executed: ${packageManager} ${command} ${!doesPackageJSONExist ? '-g' : ''} ${collection}`,
      ]);

      if (!dryRun) {
        await spawnAsync(
          packageManager,
          [
            command,
            !doesPackageJSONExist ? '-g' : '',
            collection,
            registry ? `--registry=${registry}` : '',
          ],
          {
            cwd: path ?? process.cwd(),
            stdio: 'inherit',
            shell: true,
          },
        );
      }
    }

    return isInstalled;
  } catch (error) {
    if (error?.message) {
      logger.error(error?.message);
    }
    process.exit(1);
  }
}

export async function uninstallCollection(
  collection: string,
  path?: string,
  packageManager: string = 'pnpm',
  command: string = 'remove',
  dryRun: boolean = false,
) {
  try {
    logger.info('Uninstalling of temporal package: ' + collection, [
      'command executed: ' +
        packageManager +
        ` ${command} ` +
        collection,
    ]);

    if (!dryRun) {
      await spawnAsync(
        packageManager,
        [command, collection],
        {
          cwd: path ?? process.cwd(),
          stdio: 'inherit',
          shell: true,
        },
      );
    }
  } catch (error) {
    if (error?.message) {
      logger.error(error?.message);
    }
    process.exit(1);
  }
}