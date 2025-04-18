import { appendFileSync, mkdtempSync, realpathSync } from "node:fs";
import { tmpdir } from "node:os";
import { normalize } from "node:path";

let logPath: string | undefined;

/**
 * Writes an Error to a temporary log file.
 * If this method is called multiple times from the same process the same log file will be used.
 * @returns The path of the generated log file.
 */
export function writeErrorToLogFile(error: Error): string {
  if (!logPath) {
    const tempDirectory = mkdtempSync(realpathSync(tmpdir()) + "/ngdev-");
    logPath = normalize(tempDirectory + "/msg.log");
  }

  appendFileSync(logPath, "[error] " + (error.stack || error) + "\n\n");

  return logPath;
}
