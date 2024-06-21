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

}
