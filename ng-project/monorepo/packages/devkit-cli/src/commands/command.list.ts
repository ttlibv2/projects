import { CommandModuleConstructor } from "./helper/add-cmd-to-args";

export interface CommandConfig {
  aliases?: string[];
  docMd?: string;
  factory: () => Promise<{ default: CommandModuleConstructor }>;
}

export const RootCommands: Record<string, CommandConfig> = {
  gen: {
    factory: () => import("./generate.cmd"),
    aliases: ["g"],
  },
  new: {
    factory: () => import("./new.cmd"),
    aliases: ["n"],
  },
  exec: {
    factory: () => import("./exec.cmd"),
    aliases: ["exec"],
  },
  info: {
    factory: () => import("./info.cmd"),
    aliases: ["inf"],
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