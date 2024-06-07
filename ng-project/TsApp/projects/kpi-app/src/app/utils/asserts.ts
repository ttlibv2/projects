import {Objects} from "./objects";

export class Asserts {

  static notNull<E>(object: E, msg: string): E {
    if(Objects.isNull(object)) throw new Error(msg);
    else return object;
  }

}
