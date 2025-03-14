"use strict";
Object.defineProperty(exports, "__esModule", { value: true });
exports.normalizeOptionsMiddleware = normalizeOptionsMiddleware;
const tslib_1 = require("tslib");
const yargs = tslib_1.__importStar(require("yargs"));
/**
 * A Yargs middleware that normalizes non Array options when the argument has been provided multiple times.
 *
 * By default, when an option is non array and it is provided multiple times in the command line, yargs
 * will not override it's value but instead it will be changed to an array unless `duplicate-arguments-array` is disabled.
 * But this option also have an effect on real array options which isn't desired.
 *
 * See: https://github.com/yargs/yargs-parser/pull/163#issuecomment-516566614
 */
function normalizeOptionsMiddleware(args) {
    // `getOptions` is not included in the types even though it's public API.
    // https://github.com/yargs/yargs/issues/2098
    // eslint-disable-next-line @typescript-eslint/no-explicit-any
    const { array } = yargs.getOptions();
    const arrayOptions = new Set(array);
    for (const [key, value] of Object.entries(args)) {
        if (key !== '_' && Array.isArray(value) && !arrayOptions.has(key)) {
            const newValue = value.pop();
            // eslint-disable-next-line no-console
            console.warn(`Option '${key}' has been specified multiple times. The value '${newValue}' will be used.`);
            args[key] = newValue;
        }
    }
}
