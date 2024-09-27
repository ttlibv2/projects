export type FieldType = 'text' | 'number' | 'date' | 'combo';

export interface FormField {
    name: string;
    type?: string | FieldType;
    label?: string;
    required?: boolean;
    value?: any;
    options?: any;
}