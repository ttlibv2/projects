import { PwdRule } from "./rule";
import { RuleError } from "./rule-error";

/**
 * Rule to require a specific length or range.
 */
export class LengthRule extends PwdRule {
    private readonly minLength: number;
    private readonly maxLength: number;

    /**
     * @param minLength The minimum required length.
     * @param maxLength The maximum required length. If it's undefined, max will get the value of minLength, so the password/PIN will have a required length instead of a range.
     * @param message The message to display for the rule. For example "Must have a special character".
     */
    constructor(minLength: number, maxLength?: number, message?: string) {
        super(message);
        if (minLength <= 0 || maxLength === 0) {
            throw RuleError.POSITIVE_VALUE_REQUIRED;
        } //
        else if (maxLength && (maxLength < minLength)) {
            throw RuleError.MAX_LENGTH_GREATER;
        } //
        else {
            this.minLength = minLength;
            this.maxLength = maxLength ?? minLength;
        }
    }

    validate(subject: string) {
        this.completed = subject.length >= this.minLength && subject.length <= this.maxLength;
    }

    override clone(): LengthRule {
        return new LengthRule(this.minLength, this.maxLength, this.message);
    }
}