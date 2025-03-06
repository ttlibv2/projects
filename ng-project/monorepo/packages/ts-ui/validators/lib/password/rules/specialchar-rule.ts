import { PwdRule } from "./rule";

/**
 * Rule to require a special character.
 */
export class SpecialCharRule extends PwdRule {
    private readonly specialCharRegex = new RegExp(/[^A-Za-z0-9]/g);

    /**
     * @param message The message to display for the rule. For example "Must have a special character".
     */
    constructor(message?: string) {
        super(message);
    }

    validate(subject: string) {
        this.completed = this.specialCharRegex.test(subject);
    }

    override clone(): SpecialCharRule {
        return new SpecialCharRule(this.message);
    }
}

 /**
* Rule to forbid special characters.
*/
export class NoSpecialCharRule extends SpecialCharRule {

   constructor(message?: string) {
       super(message);
   }

   override validate(subject: string) {
       super.validate(subject);
       this.completed = !this.completed;
   }
}