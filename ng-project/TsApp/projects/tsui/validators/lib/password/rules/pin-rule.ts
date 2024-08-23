import { PwdRule } from "./rule";

/**
 * Rule to allow only digits.
 */
export class PINRule extends PwdRule {
    private readonly onlyDigitsPattern = /^\d+$/;

    /**
     * @param message The message to display for the rule. For example "Must have a special character".
     */
    constructor(message?: string) {
        super(message);
    }

    validate(subject: string) {
        this.completed = this.onlyDigitsPattern.test(subject);
    }

    override clone(): PINRule {
        return new PINRule(this.message);
    }
}