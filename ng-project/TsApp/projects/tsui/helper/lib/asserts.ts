import { NoSuchElement, NullPointer } from "./errors";
import {Objects} from "./objects";

export class Asserts {

  static noSuchElement<E>(value: E, message: string): E {
      if(Objects.isNull(value)) throw new NoSuchElement(message);
      else return value;
  }

  static notNull<E>(object: E, message?: string): E {
    if(Objects.isNull(object)) throw new NullPointer(message);
    else return object;
  }

  static notEmpty<E>(object: E, message?: string): E {
    if(Objects.isEmpty(object)) throw new NullPointer(message);
    else return object;
  }

  static isTrue(bool: boolean, message: string): boolean {
    if(bool === false) throw new Error(message);
    else return bool;
  }

  static isNull<E>(object: E, message: string): E {
    if(Objects.notNull(object)) throw new Error(message);
    else return object;
  }

}
