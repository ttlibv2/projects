import { PwdRule } from "./rule";

/**
 * Rule to require uppercase characters.
 */
export class UpperCaseRule extends PwdRule {
    private readonly upperCaseRegex = /[A-Z]/;

    /**
     * @param message The message to display for the rule. For example "Must have a special character".
     */
    constructor(message?: string) {
        super(message);
    }

    validate(subject: string) {
        this.completed = this.upperCaseRegex.test(subject);
    }
}

/**
 * Rule to forbid uppercase characters.
 */
export class NoUpperCaseRule extends UpperCaseRule {

    constructor(message?: string) {
        super(message);
    }

    override validate(subject: string) {
        super.validate(subject);
        this.completed = !this.completed;
    }
}
