/**
 * A decorator that memoizes methods and getters.
 *
 * **Note**: Be cautious where and how to use this decorator as the size of the cache will grow unbounded.
 *
 * @see https://en.wikipedia.org/wiki/Memoization
 */
export declare function memoize<This, Args extends unknown[], Return>(target: (this: This, ...args: Args) => Return, context: ClassMemberDecoratorContext): (this: This, ...args: Args) => Return;
//# sourceMappingURL=memoize.d.ts.map