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
exports.askConfirmation = askConfirmation;
exports.askQuestion = askQuestion;
exports.askChoices = askChoices;
const tty_1 = require("./tty");
function askConfirmation(message, defaultResponse, noTTYResponse) {
    return __awaiter(this, void 0, void 0, function* () {
        if (!(0, tty_1.isTTY)()) {
            return noTTYResponse !== null && noTTYResponse !== void 0 ? noTTYResponse : defaultResponse;
        }
        const { confirm } = yield Promise.resolve().then(() => require('@inquirer/prompts'));
        const answer = yield confirm({
            message,
            default: defaultResponse,
            theme: {
                prefix: '',
            },
        });
        return answer;
    });
}
function askQuestion(message, choices, defaultResponseIndex, noTTYResponse) {
    return __awaiter(this, void 0, void 0, function* () {
        if (!(0, tty_1.isTTY)()) {
            return noTTYResponse;
        }
        const { select } = yield Promise.resolve().then(() => require('@inquirer/prompts'));
        const answer = yield select({
            message,
            choices,
            default: defaultResponseIndex,
            theme: {
                prefix: '',
            },
        });
        return answer;
    });
}
function askChoices(message, choices, noTTYResponse) {
    return __awaiter(this, void 0, void 0, function* () {
        if (!(0, tty_1.isTTY)()) {
            return noTTYResponse;
        }
        const { checkbox } = yield Promise.resolve().then(() => require('@inquirer/prompts'));
        const answers = yield checkbox({
            message,
            choices,
            theme: {
                prefix: '',
            },
        });
        return answers;
    });
}
//# sourceMappingURL=prompt.js.map