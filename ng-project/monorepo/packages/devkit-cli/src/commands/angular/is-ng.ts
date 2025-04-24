
export const ngSchematics: string[] = ['app'];

export function isGenAngular(framework: string, collectionName: string, schematicName: string): boolean {
    return (!framework || framework == 'ng') && ngSchematics.includes(schematicName);
}