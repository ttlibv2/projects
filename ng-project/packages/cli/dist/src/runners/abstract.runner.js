"use strict";
Object.defineProperty(exports, "__esModule", { value: true });
exports.AbstractRunner = void 0;
exports.createRunnerLogger = createRunnerLogger;
const ansis_1 = require("ansis");
const child_process_1 = require("child_process");
const messages_1 = require("../logui/messages");
const logger_1 = require("../utilities/logger");
function createRunnerLogger(nameOrLogger = 'runner') {
    if (nameOrLogger instanceof logger_1.Logger)
        return nameOrLogger;
    else {
        const newName = nameOrLogger == 'runner' || nameOrLogger.endsWith('-runner') ? nameOrLogger : `${nameOrLogger}-runner`;
        return logger_1.Logger.create(newName);
    }
}
class AbstractRunner {
    binary;
    args;
    logger;
    constructor(binary, args = [], logger) {
        this.binary = binary;
        this.args = args;
        this.logger = logger;
        this.logger = createRunnerLogger(logger);
    }
    async run(command, collect, cwd) {
        collect = collect ?? false;
        cwd = cwd ?? process.cwd();
        const options = {
            cwd, shell: true,
            stdio: collect ? 'pipe' : 'inherit'
        };
        //return await spawnAsync(`node`, [this.binary, command], options, collect);
        return await spawnAsync(this.binary, [...this.args, command], options, collect);
    }
    /**
    * @param command
    * @returns The entire command that will be ran when calling `run(command)`.
    */
    rawFullCommand(command) {
        const commandArgs = [...this.args, command];
        return `${this.binary} ${commandArgs.join(' ')}`;
    }
    buildCommandLine({ command, inputs, flags }) {
        return `${command} ${this.buildInputs(inputs)} ${this.buildFlags(flags)}`;
    }
    buildInputs(inputs = []) {
        return inputs.join(' ');
    }
    buildFlags(flags = []) {
        return flags.map(({ name, value }) => `--${name}${value !== undefined ? '=' + value : ''}`).join(' ');
    }
}
exports.AbstractRunner = AbstractRunner;
function spawnAsync(command, args, options, collect = false) {
    return new Promise((resolve, reject) => {
        const child = (0, child_process_1.spawn)(command, args, options);
        if (collect) {
            child.stdout.on('data', (data) => resolve(data.toString().replace(/\r\n|\n/, '')));
        }
        child.on('close', (code) => {
            if (code === 0) {
                resolve(null);
            }
            else {
                const msg = messages_1.MESSAGES.RUNNER_EXECUTION_ERROR(`${command}`);
                //writeErrorToLogFile(msg);
                console.error((0, ansis_1.red)(msg));
                reject();
            }
        });
        child.on('error', (error) => {
            console.error(`Spawn error: ${error.message}`);
            process.exit(1);
        });
    });
}
//# sourceMappingURL=abstract.runner.js.map