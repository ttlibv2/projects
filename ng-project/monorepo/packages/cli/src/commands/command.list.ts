import {CommandModuleConstructor} from "./helper/add-cmd-to-args";

export interface CommandConfig {
  aliases?: string[];
  docMd?: string;
  factory: () => Promise<{ default: CommandModuleConstructor }>;
}

export const RootCommands: Record<string, CommandConfig> = {
  'generate': {
    factory: () => import('./generate.cmd'),
    aliases: ['g', 'gen'],
  },
  'new': {
    factory: () => import('./new.cmd'),
    aliases: ['n'],
  },
  'version': {
    factory: () => import('./version.cmd'),
    aliases: ['v'],
  },
  'execute': {
    factory: () => import('./exec.cmd'),
    aliases: ['exec'],
  }
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
