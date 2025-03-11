import { PwdRule } from "./rule";

/**
 * Rule to require lowercase characters.
 */
export class LowerCaseRule extends PwdRule {
    private readonly lowerCaseRegex = /[a-z]/;

    /**
     * @param message The message to display for the rule. For example "Must have a special character".
     */
    constructor(message?: string) {
        super(message);
    }

    validate(subject: string) {
        this.completed = this.lowerCaseRegex.test(subject);
    }

    override clone(): LowerCaseRule {
        return new LowerCaseRule(this.message);
    }
}

/**
 * Rule to forbid lowercase characters.
 */
export class NoLowerCaseRule extends LowerCaseRule {

    constructor(message?: string) {
        super(message);
    }

   override validate(subject: string) {
        super.validate(subject);
        this.completed = !this.completed;
    }
}
