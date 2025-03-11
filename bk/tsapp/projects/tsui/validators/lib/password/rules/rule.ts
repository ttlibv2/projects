import { Objects } from "ts-ui/helper";

export abstract class PwdRule {
    public readonly name: string;
    protected completed?: boolean;

    /**
     * @param message The message to display for the rule. For example "Must have a special character".
     */
    constructor(protected readonly message?: string) {
        this.name = this.constructor.name;
    }

    /**
     * Validates the password/PIN against the rule.
     * @param subject The password/PIN
     */
    abstract validate(subject: string): void;
    abstract clone(): PwdRule;

    /**
     * Returns the completed value.
     * - true if the password/PIN meets the rule.
     * - false if the password/PIN doesn't meet the rule
     * - undefined if the rules was not validated yet, or if {@link reset} was called.
     */
    isCompleted(): boolean | undefined {
        return Objects.isNull(this.completed) ? undefined : this.completed;
    }

    /**
     * Sets the completed field to undefined. This is the same as if {@link validate} hadn't been called yet.
     */
    reset(): void {
        this.completed = undefined;
    }

    /**
     * Returns the message (if exists) passed in the constructor when creating the rule.
     */
    getMessage(): string {
        return this.message;
    }



}