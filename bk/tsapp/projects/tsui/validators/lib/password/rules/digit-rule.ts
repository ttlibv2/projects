import { Asserts } from "ts-ui/helper";
import { PwdRule } from "./rule";
import { RuleError } from "./rule-error";

/**
 * Rule to require X digits. Where X is the count param from the constructor.
 */
export class DigitRule extends PwdRule {
   private readonly digitRegex = /[^0-9]/g;
   private readonly digitsCount: number;

   constructor(count: number = 1, message?: string) {
       super(message);
       Asserts.isTrue(count >=1, RuleError.POSITIVE_VALUE_REQUIRED);
       this.digitsCount = count;
   }

   validate(subject: string) {
       this.completed = subject.replace(this.digitRegex, '').length >= this.digitsCount;
   }

   override clone(): DigitRule {
       return new DigitRule(this.digitsCount, this.message);
   }

}

/**
 * Rule to forbid digits.
 */
export class NoDigitRule extends DigitRule {

    constructor(message?: string) {
        super(1, message);
    }

    override validate(subject: string) {
        super.validate(subject);
        this.completed = !this.completed;
    }
}
