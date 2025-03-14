"use strict";
/**
 * @license
 * Copyright Google LLC All Rights Reserved.
 *
 * Use of this source code is governed by an MIT-style license that can be
 * found in the LICENSE file at https://angular.dev/license
 */
var __createBinding = (this && this.__createBinding) || (Object.create ? (function(o, m, k, k2) {
    if (k2 === undefined) k2 = k;
    var desc = Object.getOwnPropertyDescriptor(m, k);
    if (!desc || ("get" in desc ? !m.__esModule : desc.writable || desc.configurable)) {
      desc = { enumerable: true, get: function() { return m[k]; } };
    }
    Object.defineProperty(o, k2, desc);
}) : (function(o, m, k, k2) {
    if (k2 === undefined) k2 = k;
    o[k2] = m[k];
}));
var __setModuleDefault = (this && this.__setModuleDefault) || (Object.create ? (function(o, v) {
    Object.defineProperty(o, "default", { enumerable: true, value: v });
}) : function(o, v) {
    o["default"] = v;
});
var __importStar = (this && this.__importStar) || (function () {
    var ownKeys = function(o) {
        ownKeys = Object.getOwnPropertyNames || function (o) {
            var ar = [];
            for (var k in o) if (Object.prototype.hasOwnProperty.call(o, k)) ar[ar.length] = k;
            return ar;
        };
        return ownKeys(o);
    };
    return function (mod) {
        if (mod && mod.__esModule) return mod;
        var result = {};
        if (mod != null) for (var k = ownKeys(mod), i = 0; i < k.length; i++) if (k[i] !== "default") __createBinding(result, mod, k[i]);
        __setModuleDefault(result, mod);
        return result;
    };
})();
Object.defineProperty(exports, "__esModule", { value: true });
exports.RootCommandsAliases = exports.RootCommands = void 0;
exports.RootCommands = {
    // 'add': {
    //   factory: () => import('./add/cli'),
    // },
    // 'analytics': {
    //   factory: () => import('./analytics/cli'),
    // },
    // 'build': {
    //   factory: () => import('./build/cli'),
    //   aliases: ['b'],
    // },
    // 'cache': {
    //   factory: () => import('./cache/cli'),
    // },
    // 'completion': {
    //   factory: () => import('./completion/cli'),
    // },
    // 'config': {
    //   factory: () => import('./config/cli'),
    // },
    // 'deploy': {
    //   factory: () => import('./deploy/cli'),
    // },
    // 'e2e': {
    //   factory: () => import('./e2e/cli'),
    //   aliases: ['e'],
    // },
    // 'extract-i18n': {
    //   factory: () => import('./extract-i18n/cli'),
    // },
    // 'generate': {
    //   factory: () => import('./generate/cli'),
    //   aliases: ['g'],
    // },
    // 'lint': {
    //   factory: () => import('./lint/cli'),
    // },
    // 'make-this-awesome': {
    //   factory: () => import('./make-this-awesome/cli'),
    // },
    'new': {
        factory: () => Promise.resolve().then(() => __importStar(require('./new/cli'))),
        aliases: ['n'],
    },
    // 'run': {
    //   factory: () => import('./run/cli'),
    // },
    // 'serve': {
    //   factory: () => import('./serve/cli'),
    //   aliases: ['dev', 's'],
    // },
    // 'test': {
    //   factory: () => import('./test/cli'),
    //   aliases: ['t'],
    // },
    // 'update': {
    //   factory: () => import('./update/cli'),
    // },
    'version': {
        factory: () => Promise.resolve().then(() => __importStar(require('./version/cli'))),
        aliases: ['v'],
    },
};
exports.RootCommandsAliases = Object.values(exports.RootCommands).reduce((prev, current) => {
    current.aliases?.forEach((alias) => {
        prev[alias] = current;
    });
    return prev;
}, {});
