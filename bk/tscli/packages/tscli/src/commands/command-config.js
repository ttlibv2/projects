"use strict";
/**
 * @license
 * Copyright Google LLC All Rights Reserved.
 *
 * Use of this source code is governed by an MIT-style license that can be
 * found in the LICENSE file at https://angular.dev/license
 */
Object.defineProperty(exports, "__esModule", { value: true });
exports.RootCommandsAliases = exports.RootCommands = void 0;
exports.RootCommands = {
    //   'add': {
    //     factory: () => import('./add/cli'),
    //   },
    //   'analytics': {
    //     factory: () => import('./analytics/cli'),
    //   },
    //   'build': {
    //     factory: () => import('./build/cli'),
    //     aliases: ['b'],
    //   },
    //   'cache': {
    //     factory: () => import('./cache/cli'),
    //   },
    //   'completion': {
    //     factory: () => import('./completion/cli'),
    //   },
    //   'config': {
    //     factory: () => import('./config/cli'),
    //   },
    //   'deploy': {
    //     factory: () => import('./deploy/cli'),
    //   },
    //   'e2e': {
    //     factory: () => import('./e2e/cli'),
    //     aliases: ['e'],
    //   },
    //   'extract-i18n': {
    //     factory: () => import('./extract-i18n/cli'),
    //   },
    //   'lint': {
    //     factory: () => import('./lint/cli'),
    //   },
    //   'make-this-awesome': {
    //     factory: () => import('./make-this-awesome/cli'),
    //   },
    //   'new': {
    //     factory: () => import('./new/cli'),
    //     aliases: ['n'],
    //   },
    //   'run': {
    //     factory: () => import('./run/cli'),
    //   },
    //   'serve': {
    //     factory: () => import('./serve/cli'),
    //     aliases: ['dev', 's'],
    //   },
    //   'test': {
    //     factory: () => import('./test/cli'),
    //     aliases: ['t'],
    //   },
    //   'update': {
    //     factory: () => import('./update/cli'),
    //   },
    'generate': {
        factory: () => Promise.resolve().then(() => require('./generate/cli')),
        aliases: ['g'],
    },
    'version': {
        factory: () => Promise.resolve().then(() => require('./version/cli')),
        aliases: ['v'],
    },
};
exports.RootCommandsAliases = Object.values(exports.RootCommands).reduce((prev, current) => {
    var _a;
    (_a = current.aliases) === null || _a === void 0 ? void 0 : _a.forEach((alias) => {
        prev[alias] = current;
    });
    return prev;
}, {});
//# sourceMappingURL=command-config.js.map