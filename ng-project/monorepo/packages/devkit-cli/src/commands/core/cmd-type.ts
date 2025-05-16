import { schema } from '@angular-devkit/core';
import { SchematicsExecutionOptions } from './schematics.cmd';

export interface SchematicInfo {
    collectionName?: string;
    schematicName?: string;
}