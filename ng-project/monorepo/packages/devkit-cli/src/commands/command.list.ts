import { CommandModuleConstructor } from "./helper/add-cmd-to-args";

export interface CommandConfig {
  aliases?: string[];
  docMd?: string;
  factory: () => Promise<{ default: CommandModuleConstructor }>;
}

export const RootCommands: Record<string, CommandConfig> = {
  // generate: {
  //   factory: () => import("./generate.cmd"),
  //   aliases: ["g", "gen"],
  // },
  new: {
    factory: () => import("./new.cmd"),
    aliases: ["n"],
  },
  'ng-app': {
    factory: () => import("./angular/ng-app.cmd"),
    aliases: ['app']
  },
  'ng-lib': {
    factory: () => import("./angular/ng-lib.cmd"),
    aliases: ['lib']
  },

  exec: {
    factory: () => import("./exec.cmd"),
    aliases: ["exec"],
  },
  info: {
    factory: () => import("./info.cmd"),
    aliases: ["inf"],
  },
  test: {
    factory: () => import("./test.cmd"),
    aliases: [],
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