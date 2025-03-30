"use strict";
Object.defineProperty(exports, "__esModule", { value: true });
exports.RootCommandsAliases = exports.RootCommands = void 0;
exports.RootCommands = {
    'generate': {
        factory: () => Promise.resolve().then(() => require('./generate.cmd')),
        aliases: ['g', 'gen'],
    },
    'new': {
        factory: () => Promise.resolve().then(() => require('./new.cmd')),
        aliases: ['n'],
    },
    'version': {
        factory: () => Promise.resolve().then(() => require('./version.cmd')),
        aliases: ['v'],
    }
};
exports.RootCommandsAliases = Object.values(exports.RootCommands).reduce((prev, current) => {
    current.aliases?.forEach((alias) => {
        prev[alias] = current;
    });
    return prev;
}, {});
//# sourceMappingURL=command.list.js.map