import { json } from '@angular-devkit/core';
import { parseJson, readAndParseJson } from './json-file';

/**
 * Validate data without schema json file
 * @param schemaPath file schema json
 * @param data content validate
 * @param isGlobal boolean
 * */
export async function validateSchema(schemaPath: string, data: json.JsonObject | string, isGlobal: boolean): Promise<void> {
    const schema = readAndParseJson(schemaPath);

    // We should eventually have a dedicated global config schema and use that to validate.
    const schemaToValidate: json.schema.JsonSchema = !isGlobal ? schema
        : {'$ref': '#/definitions/global', definitions: schema['definitions']};

    const {formats} = await import('@angular-devkit/schematics');
    const registry = new json.schema.CoreSchemaRegistry(formats.standardFormats);
    const validator = await registry.compile(schemaToValidate);
    const newData: json.JsonObject = typeof data === 'string' ? parseJson(data) : data;
    const {success, errors} = await validator(newData);
    if (!success) throw new json.schema.SchemaValidationException(errors);
}