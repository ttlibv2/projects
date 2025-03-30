"use strict";
Object.defineProperty(exports, "__esModule", { value: true });
exports.normalizeMiddleware = normalizeMiddleware;
const yargs = require("yargs");
function normalizeMiddleware(args, logger) {
    const option = yargs.getOptions();
    const { array } = option;
    const arrayOptions = new Set(array);
    for (const [key, value] of Object.entries(args)) {
        if (key !== '_' && Array.isArray(value) && !arrayOptions.has(key)) {
            const newValue = value.pop();
            logger.warn(`Option '${key}' has been specified multiple times. The value '${newValue}' will be used.`);
            args[key] = newValue;
        }
    }
}
//# sourceMappingURL=normalize-middleware.js.map