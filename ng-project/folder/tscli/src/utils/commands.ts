
import { SpawnOptions, spawn } from 'child_process';
import { logger } from './logger';
import { MESSAGES } from './messages';

export function spawnAsync(
  command: string,
  args: string[],
  options: SpawnOptions,
  collect: boolean = false,
): Promise<void> {
  return new Promise((resolve, reject) => {
    const child = spawn(command, args, options);

    if (collect) {
      child.stdout!.on('data', (data) =>
        resolve(data.toString().replace(/\r\n|\n/, '')),
      );
    }

    try {
      child.on('close', (code) => {
        if (code === 0) {
          resolve();
        } else {
          // TODO: Remove unused messages
          logger.error(MESSAGES.RUNNER_EXECUTION_ERROR(`${command}`));
          reject();
        }
      });
      child.on('error', (error) => {
        logger.error('Spawn error:', [error.message ?? '']);
        process.exit(1);
      });
    } catch (e) {
      logger.error(e.message ?? '');
      process.exit(1);
    }
  });
}