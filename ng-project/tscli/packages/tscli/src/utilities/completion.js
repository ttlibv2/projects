"use strict";
/**
 * @license
 * Copyright Google LLC All Rights Reserved.
 *
 * Use of this source code is governed by an MIT-style license that can be
 * found in the LICENSE file at https://angular.dev/license
 */
var __awaiter = (this && this.__awaiter) || function (thisArg, _arguments, P, generator) {
    function adopt(value) { return value instanceof P ? value : new P(function (resolve) { resolve(value); }); }
    return new (P || (P = Promise))(function (resolve, reject) {
        function fulfilled(value) { try { step(generator.next(value)); } catch (e) { reject(e); } }
        function rejected(value) { try { step(generator["throw"](value)); } catch (e) { reject(e); } }
        function step(result) { result.done ? resolve(result.value) : adopt(result.value).then(fulfilled, rejected); }
        step((generator = generator.apply(thisArg, _arguments || [])).next());
    });
};
Object.defineProperty(exports, "__esModule", { value: true });
exports.considerSettingUpAutocompletion = considerSettingUpAutocompletion;
exports.initializeAutocomplete = initializeAutocomplete;
exports.hasGlobalCliInstall = hasGlobalCliInstall;
const core_1 = require("@angular-devkit/core");
const node_child_process_1 = require("node:child_process");
const node_fs_1 = require("node:fs");
const path = require("node:path");
const node_process_1 = require("node:process");
const color_1 = require("../utilities/color");
const config_1 = require("../utilities/config");
const environment_options_1 = require("../utilities/environment-options");
const tty_1 = require("../utilities/tty");
const error_1 = require("./error");
const prompt_1 = require("./prompt");
/**
 * Checks if it is appropriate to prompt the user to setup autocompletion. If not, does nothing. If
 * so prompts and sets up autocompletion for the user. Returns an exit code if the program should
 * terminate, otherwise returns `undefined`.
 * @returns an exit code if the program should terminate, undefined otherwise.
 */
function considerSettingUpAutocompletion(command, logger) {
    return __awaiter(this, void 0, void 0, function* () {
        // Check if we should prompt the user to setup autocompletion.
        const completionConfig = yield getCompletionConfig();
        if (!(yield shouldPromptForAutocompletionSetup(command, completionConfig))) {
            return undefined; // Already set up or prompted previously, nothing to do.
        }
        // Prompt the user and record their response.
        const shouldSetupAutocompletion = yield promptForAutocompletion();
        if (!shouldSetupAutocompletion) {
            // User rejected the prompt and doesn't want autocompletion.
            logger.info(`
Ok, you won't be prompted again. Should you change your mind, the following command will set up autocompletion for you:

    ${color_1.colors.yellow(`ng completion`)}
    `.trim());
            // Save configuration to remember that the user was prompted and avoid prompting again.
            yield setCompletionConfig(Object.assign(Object.assign({}, completionConfig), { prompted: true }));
            return undefined;
        }
        // User accepted the prompt, set up autocompletion.
        let rcFile;
        try {
            rcFile = yield initializeAutocomplete();
        }
        catch (err) {
            (0, error_1.assertIsError)(err);
            // Failed to set up autocompeletion, log the error and abort.
            logger.error(err.message);
            return 1;
        }
        // Notify the user autocompletion was set up successfully.
        logger.info(`
Appended \`source <(ng completion script)\` to \`${rcFile}\`. Restart your terminal or run the following to autocomplete \`ng\` commands:

    ${color_1.colors.yellow(`source <(ng completion script)`)}
    `.trim());
        if (!(yield hasGlobalCliInstall())) {
            logger.warn('Setup completed successfully, but there does not seem to be a global install of the' +
                ' Angular CLI. For autocompletion to work, the CLI will need to be on your `$PATH`, which' +
                ' is typically done with the `-g` flag in `npm install -g @angular/cli`.' +
                '\n\n' +
                'For more information, see https://angular.dev/cli/completion#global-install');
        }
        // Save configuration to remember that the user was prompted.
        yield setCompletionConfig(Object.assign(Object.assign({}, completionConfig), { prompted: true }));
        return undefined;
    });
}
function getCompletionConfig() {
    return __awaiter(this, void 0, void 0, function* () {
        var _a;
        const wksp = yield (0, config_1.getWorkspace)('global');
        return (_a = wksp === null || wksp === void 0 ? void 0 : wksp.getCli()) === null || _a === void 0 ? void 0 : _a['completion'];
    });
}
function setCompletionConfig(config) {
    return __awaiter(this, void 0, void 0, function* () {
        var _a;
        var _b;
        const wksp = yield (0, config_1.getWorkspace)('global');
        if (!wksp) {
            throw new Error(`Could not find global workspace`);
        }
        (_a = (_b = wksp.extensions)['cli']) !== null && _a !== void 0 ? _a : (_b['cli'] = {});
        const cli = wksp.extensions['cli'];
        if (!core_1.json.isJsonObject(cli)) {
            throw new Error(`Invalid config found at ${wksp.filePath}. \`extensions.cli\` should be an object.`);
        }
        cli.completion = config;
        yield wksp.save();
    });
}
function shouldPromptForAutocompletionSetup(command, config) {
    return __awaiter(this, void 0, void 0, function* () {
        // Force whether or not to prompt for autocomplete to give an easy path for e2e testing to skip.
        if (environment_options_1.forceAutocomplete !== undefined) {
            return environment_options_1.forceAutocomplete;
        }
        // Don't prompt on `ng update`, 'ng version' or `ng completion`.
        if (['version', 'update', 'completion'].includes(command)) {
            return false;
        }
        // Non-interactive and continuous integration systems don't care about autocompletion.
        if (!(0, tty_1.isTTY)()) {
            return false;
        }
        // Skip prompt if the user has already been prompted.
        if (config === null || config === void 0 ? void 0 : config.prompted) {
            return false;
        }
        // `$HOME` variable is necessary to find RC files to modify.
        const home = node_process_1.env['HOME'];
        if (!home) {
            return false;
        }
        // Get possible RC files for the current shell.
        const shell = node_process_1.env['SHELL'];
        if (!shell) {
            return false;
        }
        const rcFiles = getShellRunCommandCandidates(shell, home);
        if (!rcFiles) {
            return false; // Unknown shell.
        }
        // Don't prompt if the user is missing a global CLI install. Autocompletion won't work after setup
        // anyway and could be annoying for users running one-off commands via `npx` or using `npm start`.
        if ((yield hasGlobalCliInstall()) === false) {
            return false;
        }
        // Check each RC file if they already use `ng completion script` in any capacity and don't prompt.
        for (const rcFile of rcFiles) {
            const contents = yield node_fs_1.promises.readFile(rcFile, 'utf-8').catch(() => undefined);
            if (contents === null || contents === void 0 ? void 0 : contents.includes('ng completion script')) {
                return false;
            }
        }
        return true;
    });
}
function promptForAutocompletion() {
    return __awaiter(this, void 0, void 0, function* () {
        const autocomplete = yield (0, prompt_1.askConfirmation)(`
Would you like to enable autocompletion? This will set up your terminal so pressing TAB while typing
Angular CLI commands will show possible options and autocomplete arguments. (Enabling autocompletion
will modify configuration files in your home directory.)
        `
            .split('\n')
            .join(' ')
            .trim(), true);
        return autocomplete;
    });
}
/**
 * Sets up autocompletion for the user's terminal. This attempts to find the configuration file for
 * the current shell (`.bashrc`, `.zshrc`, etc.) and append a command which enables autocompletion
 * for the Angular CLI. Supports only Bash and Zsh. Returns whether or not it was successful.
 * @return The full path of the configuration file modified.
 */
function initializeAutocomplete() {
    return __awaiter(this, void 0, void 0, function* () {
        var _a, _b;
        // Get the currently active `$SHELL` and `$HOME` environment variables.
        const shell = node_process_1.env['SHELL'];
        if (!shell) {
            throw new Error('`$SHELL` environment variable not set. Angular CLI autocompletion only supports Bash or' +
                " Zsh. If you're on Windows, Cmd and Powershell don't support command autocompletion," +
                ' but Git Bash or Windows Subsystem for Linux should work, so please try again in one of' +
                ' those environments.');
        }
        const home = node_process_1.env['HOME'];
        if (!home) {
            throw new Error('`$HOME` environment variable not set. Setting up autocompletion modifies configuration files' +
                ' in the home directory and must be set.');
        }
        // Get all the files we can add `ng completion` to which apply to the user's `$SHELL`.
        const runCommandCandidates = getShellRunCommandCandidates(shell, home);
        if (!runCommandCandidates) {
            throw new Error(`Unknown \`$SHELL\` environment variable value (${shell}). Angular CLI autocompletion only supports Bash or Zsh.`);
        }
        // Get the first file that already exists or fallback to a new file of the first candidate.
        const candidates = yield Promise.allSettled(runCommandCandidates.map((rcFile) => node_fs_1.promises.access(rcFile).then(() => rcFile)));
        const rcFile = (_b = (_a = candidates.find((result) => result.status === 'fulfilled')) === null || _a === void 0 ? void 0 : _a.value) !== null && _b !== void 0 ? _b : runCommandCandidates[0];
        // Append Angular autocompletion setup to RC file.
        try {
            yield node_fs_1.promises.appendFile(rcFile, '\n\n# Load Angular CLI autocompletion.\nsource <(ng completion script)\n');
        }
        catch (err) {
            (0, error_1.assertIsError)(err);
            throw new Error(`Failed to append autocompletion setup to \`${rcFile}\`:\n${err.message}`);
        }
        return rcFile;
    });
}
/** Returns an ordered list of possible candidates of RC files used by the given shell. */
function getShellRunCommandCandidates(shell, home) {
    if (shell.toLowerCase().includes('bash')) {
        return ['.bashrc', '.bash_profile', '.profile'].map((file) => path.join(home, file));
    }
    else if (shell.toLowerCase().includes('zsh')) {
        return ['.zshrc', '.zsh_profile', '.profile'].map((file) => path.join(home, file));
    }
    else {
        return undefined;
    }
}
/**
 * Returns whether the user has a global CLI install.
 * Execution from `npx` is *not* considered a global CLI install.
 *
 * This does *not* mean the current execution is from a global CLI install, only that a global
 * install exists on the system.
 */
function hasGlobalCliInstall() {
    // List all binaries with the `ng` name on the user's `$PATH`.
    return new Promise((resolve) => {
        (0, node_child_process_1.execFile)('which', ['-a', 'ng'], (error, stdout) => {
            if (error) {
                // No instances of `ng` on the user's `$PATH`
                // `which` returns exit code 2 if an invalid option is specified and `-a` doesn't appear to be
                // supported on all systems. Other exit codes mean unknown errors occurred. Can't tell whether
                // CLI is globally installed, so treat this as inconclusive.
                // `which` was killed by a signal and did not exit gracefully. Maybe it hung or something else
                // went very wrong, so treat this as inconclusive.
                resolve(false);
                return;
            }
            // Successfully listed all `ng` binaries on the `$PATH`. Look for at least one line which is a
            // global install. We can't easily identify global installs, but local installs are typically
            // placed in `node_modules/.bin` by NPM / Yarn. `npx` also currently caches files at
            // `~/.npm/_npx/*/node_modules/.bin/`, so the same logic applies.
            const lines = stdout.split('\n').filter((line) => line !== '');
            const hasGlobalInstall = lines.some((line) => {
                // A binary is a local install if it is a direct child of a `node_modules/.bin/` directory.
                const parent = path.parse(path.parse(line).dir);
                const grandparent = path.parse(parent.dir);
                const localInstall = grandparent.base === 'node_modules' && parent.base === '.bin';
                return !localInstall;
            });
            return resolve(hasGlobalInstall);
        });
    });
}
//# sourceMappingURL=completion.js.map