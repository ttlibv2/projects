import { PwdRule } from "./rule";

/**
 * Rule to forbid some strings. For instance, the username.
 */
export class ForbiddenText extends PwdRule {
    private readonly texts: string[];

    constructor(texts: string[], message?: string) {
        super(message);
        this.texts = texts;
    }

    validate(subject: string) {
        const subjectLowerCased = subject.toLowerCase();
        this.completed = this.texts.every(value => !subjectLowerCased.includes(value.toLowerCase()));
    }

    override clone(): ForbiddenText {
        return new ForbiddenText(this.texts, this.message);
    }
}