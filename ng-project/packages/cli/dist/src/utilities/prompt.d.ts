export declare function askConfirmation(message: string, defaultResponse: boolean, noTTYResponse?: boolean): Promise<boolean>;
export declare function askQuestion(message: string, choices: {
    name: string;
    value: string | null;
}[], defaultResponseIndex: number, noTTYResponse: null | string): Promise<string | null>;
export declare function askChoices(message: string, choices: {
    name: string;
    value: string;
    checked?: boolean;
}[], noTTYResponse: string[] | null): Promise<string[] | null>;
