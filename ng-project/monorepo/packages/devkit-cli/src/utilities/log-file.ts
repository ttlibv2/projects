import { appendFileSync, mkdtempSync, realpathSync } from 'node:fs';
import { tmpdir } from 'node:os';
import { normalize } from 'node:path';
import { paths } from '../commands/helper/paths';

let logPath: string | undefined;

/**
 * Writes an Error to a temporary log file.
 * If this method is called multiple times from the same process the same log file will be used.
 * @returns The path of the generated log file.
 */
export function writeErrorToLogFile(error: Error, method: string = 'cli'): [string, string] {
    if (!logPath) {
        const tempDirectory = realpathSync(tmpdir() + '/ngdev');
        const now = new Date(), d = now.getUTCDay();
        const m = now.getUTCMonth(), y = now.getUTCFullYear();
        logPath = paths.join(tempDirectory, `err-${d}${m}${y}.log`);
    }

    const line = [
        '-'.repeat(10) + ' ' + method + ' ' + '-'.repeat(10),
        `Date: ${new Date().toISOString()}`,
         `Message: ${error.message ?? error}`,
        error.stack ? `Stack: ${error.stack.split('\n').slice(1).join('\n')}` : '',
        error.cause ? `Cause: ${error.cause}` : ''
    ].filter(l => l != '').join('\n');

    appendFileSync(logPath, line + '\n\n');

    return [logPath, line];
}