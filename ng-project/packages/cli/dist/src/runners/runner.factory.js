"use strict";
Object.defineProperty(exports, "__esModule", { value: true });
exports.RunnerFactory = void 0;
const ansis_1 = require("ansis");
const npm_runner_1 = require("./npm.runner");
const runner_enum_1 = require("./runner.enum");
const schematic_runner_1 = require("./schematic.runner");
const yarn_runner_1 = require("./yarn.runner");
const pnpm_runner_1 = require("./pnpm.runner");
const logger_1 = require("../utilities/logger");
const angular_runner_1 = require("./angular.runner");
const nestjs_runner_1 = require("./nestjs.runner");
class RunnerFactory {
    static create(runner) {
        switch (runner) {
            case runner_enum_1.Runner.SCHEMATIC:
                return new schematic_runner_1.SchematicRunner();
            case runner_enum_1.Runner.NPM:
                return new npm_runner_1.NpmRunner();
            case runner_enum_1.Runner.YARN:
                return new yarn_runner_1.YarnRunner();
            case runner_enum_1.Runner.PNPM:
                return new pnpm_runner_1.PnpmRunner();
            case runner_enum_1.Runner.ANGULAR:
                return new angular_runner_1.AngularRunner();
            case runner_enum_1.Runner.NESTJS:
                return new nestjs_runner_1.NestJsRunner();
            default:
                logger_1.Logger.create('runner.factory')
                    .info((0, ansis_1.yellow) `[WARN] Unsupported runner: ${runner}`);
        }
    }
}
exports.RunnerFactory = RunnerFactory;
//# sourceMappingURL=runner.factory.js.map