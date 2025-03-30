import { CommandModuleConstructor } from "./helper/add-cmd-to-args";
export interface CommandConfig {
    aliases?: string[];
    docMd?: string;
    factory: () => Promise<{
        default: CommandModuleConstructor;
    }>;
}
export declare const RootCommands: Record<string, CommandConfig>;
export declare const RootCommandsAliases: Record<string, CommandConfig>;
