"use strict";
Object.defineProperty(exports, "__esModule", { value: true });
exports.forceAutocomplete = exports.ngDebug = exports.disableVersionCheck = exports.isCI = exports.analyticsDisabled = exports.V_PKG = exports.CLI_SCHEMATIC = exports.CLI_PKG = exports.ROOT_JSON = void 0;
exports.ROOT_JSON = ['project.json'];
exports.CLI_PKG = '@ngdev/cli';
exports.CLI_SCHEMATIC = '@ngdev/sc';
exports.V_PKG = 'ngdev';
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
exports.analyticsDisabled = isDisabled(process.env['NG_CLI_ANALYTICS']);
exports.isCI = isEnabled(process.env['CI']);
exports.disableVersionCheck = isEnabled(process.env['NG_DISABLE_VERSION_CHECK']);
exports.ngDebug = isEnabled(process.env['NG_DEBUG']);
exports.forceAutocomplete = optional(process.env['NG_FORCE_AUTOCOMPLETE']);
