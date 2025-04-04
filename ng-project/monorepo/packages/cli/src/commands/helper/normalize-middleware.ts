import * as yargs from 'yargs';
import {Logger} from "@ngdev/devkit-core";

export function normalizeMiddleware(args: yargs.Arguments, logger: Logger): void {
  const option = (yargs as any).getOptions();
 const { array } = option;

  const arrayOptions = new Set(array);

  for (const [key, value] of Object.entries(args)) {
    if (key !== '_' && Array.isArray(value) && !arrayOptions.has(key)) {
      const newValue = value.pop();
      logger.warn(
        `Option '${key}' has been specified multiple times. The value '${newValue}' will be used.`,
      );
      args[key] = newValue;
    }
  }
}