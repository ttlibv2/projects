import { CommandModuleConstructor } from '../command-builder/utilities/command';

export interface CommandConfig {
  aliases?: string[];
  factory: () => Promise<{ default: CommandModuleConstructor }>;
}

export const RootCommands: Record<string,CommandConfig> = {
  // 'add': {
  //   factory: () => import('./add/cli'),
  // },
  // 'analytics': {
  //   factory: () => import('./analytics/cli'),
  // },
  // 'build': {
  //   factory: () => import('./build/cli'),
  //   aliases: ['b'],
  // },
  // 'cache': {
  //   factory: () => import('./cache/cli'),
  // },
  // 'completion': {
  //   factory: () => import('./completion/cli'),
  // },
  // 'config': {
  //   factory: () => import('./config/cli'),
  // },
  // 'deploy': {
  //   factory: () => import('./deploy/cli'),
  // },

  // 'e2e': {
  //   factory: () => import('./e2e/cli'),
  //   aliases: ['e'],
  // },
  // 'extract-i18n': {
  //   factory: () => import('./extract-i18n/cli'),
  // },
  'generate': {
    factory: () => import('./generate/cli'),
    aliases: ['g'],
  },
  // 'lint': {
  //   factory: () => import('./lint/cli'),
  // },
  // 'make-this-awesome': {
  //   factory: () => import('./make-this-awesome/cli'),
  // },
  'new': {
    factory: () => import('./new/cli'),
    aliases: ['n'],
  },
  // 'run': {
  //   factory: () => import('./run/cli'),
  // },
  // 'serve': {
  //   factory: () => import('./serve/cli'),
  //   aliases: ['dev', 's'],
  // },
  // 'test': {
  //   factory: () => import('./test/cli'),
  //   aliases: ['t'],
  // },
  // 'update': {
  //   factory: () => import('./update/cli'),
  // },
  'version': {
    factory: () => import('./version/cli'),
    aliases: ['v'],
  },
};

export const RootCommandsAliases = Object.values(RootCommands).reduce(
  (prev, current) => {
    current.aliases?.forEach((alias) => {
      prev[alias] = current;
    });

    return prev;
  },
  {} as Record<string, CommandConfig>,
);

/**
 * Get the commands that need to be registered.
 * @returns One or more command factories that needs to be registered.
 */
export async function getCommandsToRegister(
  commandName: string | number,
): Promise<CommandModuleConstructor[]> {
  const commands: CommandConfig[] = [];
  if (commandName in RootCommands) {
    commands.push(RootCommands[commandName]);
  } else if (commandName in RootCommandsAliases) {
    commands.push(RootCommandsAliases[commandName]);
  } else {
    // Unknown command, register every possible command.
    Object.values(RootCommands).forEach((c) => commands.push(c));
  }

  return Promise.all(commands.map((command) => command.factory().then((m) => m.default)));
}

