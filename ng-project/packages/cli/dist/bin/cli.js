#!/usr/bin/env node
"use strict";
Object.defineProperty(exports, "__esModule", { value: true });
require("symbol-observable");
const node_module_1 = require("node:module");
const local_binaries_1 = require("../src/utilities/local-binaries");
let forceExit = false;
const bootstrap = async () => {
    let cli = null;
    /**
     * Disable Browserslist old data warning as otherwise with every release we'd need to update this dependency
     * which is cumbersome considering we pin versions and the warning is not user actionable.
     * `Browserslist: caniuse-lite is outdated. Please run next command `npm update`
     * See: https://github.com/browserslist/browserslist/blob/819c4337456996d19db6ba953014579329e9c6e1/node.js#L324
     */
    process.env.BROWSERSLIST_IGNORE_OLD_DATA = '1';
    const rawCommandName = process.argv[2];
    /**
     * Disable CLI version mismatch checks and forces usage of the invoked CLI
     * instead of invoking the local installed version.
     *
     * When running `ng new` always favor the global version. As in some
     * cases orphan `node_modules` would cause the non global CLI to be used.
     * @see: https://github.com/angular/angular-cli/issues/14603
     */
    if (rawCommandName === 'new') {
        return (await Promise.resolve().then(() => require('../lib'))).default;
    }
    if ((0, local_binaries_1.localBinExists)()) {
        try {
            const cwdRequire = (0, node_module_1.createRequire)(process.cwd() + '/');
            const projectLocalCli = cwdRequire.resolve('@ngdev/lib/lib/lib');
            cli = await Promise.resolve(`${projectLocalCli}`).then(s => require(s));
        }
        catch (e) {
            console.error(e);
            cli = await Promise.resolve().then(() => require('../lib'));
        }
    }
    else {
        cli = await Promise.resolve().then(() => require('../lib'));
    }
    if ('default' in cli) {
        cli = cli['default'];
    }
    return cli;
};
bootstrap()
    .then(cli => cli?.({
    cliArgs: process.argv.slice(2)
}))
    .then((exitCode = 0) => {
    if (forceExit) {
        process.exit(exitCode);
    }
    process.exitCode = exitCode;
})
    .catch((err) => {
    console.error('Unknown error: ' + err.toString());
    process.exit(127);
});
//# sourceMappingURL=cli.js.map