interface JsonHelpOption {
    name: string;
    type?: string;
    deprecated: boolean | string;
    aliases?: string[];
    default?: string;
    required?: boolean;
    positional?: number;
    enum?: string[];
    description?: string;
}
interface JsonHelpDescription {
    shortDescription?: string;
    longDescription?: string;
    longDescriptionRelativePath?: string;
}
interface JsonHelpSubcommand extends JsonHelpDescription {
    name: string;
    aliases: string[];
    deprecated: string | boolean;
}
export interface JsonHelpUsage extends JsonHelpDescription {
    name: string;
    command: string;
    options: JsonHelpOption[];
    subcommands?: JsonHelpSubcommand[];
}
export declare function jsonHelpUsage(localYargs: any): string;
export {};
