import { PwdRule } from "./rule";

/**
 * Validates the maximum number of times any character can appear in the password consecutively.
 */
export class MaxRepeatedInRowRule extends PwdRule {
    private readonly repeatedCharsInRowRegex = /(.)\1*/g;
    private readonly totalCharsInRowAllowed: number;
    private readonly ignoreCase: boolean;

    /**
     * If
     * @param count Maximum number of times any character can appear in the password consecutively
     * @param ignoreCase If true the rule will compare the characters without having into account the case differences. False by default.
     * @param message The message to display for the rule. For example "Must have a special character".
     */
    constructor(count: number, ignoreCase = false, message?: string) {
        super(message);
        this.totalCharsInRowAllowed = count;
        this.ignoreCase = ignoreCase;
    }

    validate(subject: string) {
        this.completed = (this.ignoreCase ? subject : subject.toLowerCase())
            .match(this.repeatedCharsInRowRegex)
            ?.every((group: string) => group.length <= this.totalCharsInRowAllowed);
    }

    override clone(): MaxRepeatedInRowRule {
        return new MaxRepeatedInRowRule(this.totalCharsInRowAllowed, this.ignoreCase, this.message);
    }
}
