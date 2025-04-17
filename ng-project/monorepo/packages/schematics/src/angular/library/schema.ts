import { Schema as NgSchema } from '@schematics/angular/library/schema';

export interface Schema extends NgSchema {
    projectRoot: string;
}