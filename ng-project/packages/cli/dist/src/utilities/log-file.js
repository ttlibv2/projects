"use strict";
Object.defineProperty(exports, "__esModule", { value: true });
exports.writeErrorToLogFile = writeErrorToLogFile;
const node_fs_1 = require("node:fs");
const node_os_1 = require("node:os");
const node_path_1 = require("node:path");
let logPath;
/**
 * Writes an Error to a temporary log file.
 * If this method is called multiple times from the same process the same log file will be used.
 * @returns The path of the generated log file.
 */
function writeErrorToLogFile(error) {
    if (!logPath) {
        const tempDirectory = (0, node_fs_1.mkdtempSync)((0, node_fs_1.realpathSync)((0, node_os_1.tmpdir)()) + '/ngdev-');
        logPath = (0, node_path_1.normalize)(tempDirectory + '/msg.log');
    }
    (0, node_fs_1.appendFileSync)(logPath, '[error] ' + (error.stack || error) + '\n\n');
    return logPath;
}
//# sourceMappingURL=log-file.js.map