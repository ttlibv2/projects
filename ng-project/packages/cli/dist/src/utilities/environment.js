"use strict";
Object.defineProperty(exports, "__esModule", { value: true });
exports.forceAutocomplete = exports.ngDebug = exports.disableVersionCheck = exports.isCI = exports.analyticsDisabled = exports.INFO_PREFIX = exports.ERROR_PREFIX = void 0;
const color_1 = require("./color");
function isPresent(variable) {
    return typeof variable === 'string' && variable !== '';
}
function isDisabled(variable) {
    return isPresent(variable) && (variable === '0' || variable.toLowerCase() === 'false');
}
function isEnabled(variable) {
    return isPresent(variable) && (variable === '1' || variable.toLowerCase() === 'true');
}
function optional(variable) {
    if (!isPresent(variable)) {
        return undefined;
    }
    return isEnabled(variable);
}
exports.ERROR_PREFIX = color_1.colors.bgRed.bold.rgb(0, 0, 0)(' ERROR ');
exports.INFO_PREFIX = color_1.colors.bgRgb(60, 190, 100).bold.rgb(0, 0, 0)(' INFO ');
exports.analyticsDisabled = isDisabled(process.env['NG_CLI_ANALYTICS']);
exports.isCI = isEnabled(process.env['CI']);
exports.disableVersionCheck = isEnabled(process.env['NG_DISABLE_VERSION_CHECK']);
exports.ngDebug = isEnabled(process.env['NG_DEBUG']);
exports.forceAutocomplete = optional(process.env['NG_FORCE_AUTOCOMPLETE']);
//# sourceMappingURL=environment.js.map