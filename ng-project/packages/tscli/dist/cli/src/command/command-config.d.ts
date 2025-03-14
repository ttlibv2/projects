/**
 * @license
 * Copyright Google LLC All Rights Reserved.
 *
 * Use of this source code is governed by an MIT-style license that can be
 * found in the LICENSE file at https://angular.dev/license
 */
import { CommandModuleConstructor } from './core/command';
export type CommandNames = 'new' | 'version';
export interface CommandConfig {
    aliases?: string[];
    factory: () => Promise<{
        default: CommandModuleConstructor;
    }>;
}
export declare const RootCommands: Record<CommandNames & string, CommandConfig>;
export declare const RootCommandsAliases: Record<string, CommandConfig>;
//# sourceMappingURL=command-config.d.ts.map