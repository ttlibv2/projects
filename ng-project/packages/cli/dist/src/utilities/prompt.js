"use strict";
Object.defineProperty(exports, "__esModule", { value: true });
exports.askConfirmation = askConfirmation;
exports.askQuestion = askQuestion;
exports.askChoices = askChoices;
const tty_1 = require("./tty");
async function askConfirmation(message, defaultResponse, noTTYResponse) {
    if (!(0, tty_1.isTTY)()) {
        return noTTYResponse ?? defaultResponse;
    }
    const { confirm } = await Promise.resolve().then(() => require('@inquirer/prompts'));
    const answer = await confirm({
        message,
        default: defaultResponse,
        theme: {
            prefix: '',
        },
    });
    return answer;
}
async function askQuestion(message, choices, defaultResponseIndex, noTTYResponse) {
    if (!(0, tty_1.isTTY)()) {
        return noTTYResponse;
    }
    const { select } = await Promise.resolve().then(() => require('@inquirer/prompts'));
    const answer = await select({
        message,
        choices,
        default: defaultResponseIndex,
        theme: {
            prefix: '',
        },
    });
    return answer;
}
async function askChoices(message, choices, noTTYResponse) {
    if (!(0, tty_1.isTTY)()) {
        return noTTYResponse;
    }
    const { checkbox } = await Promise.resolve().then(() => require('@inquirer/prompts'));
    const answers = await checkbox({
        message,
        choices,
        theme: {
            prefix: '',
        },
    });
    return answers;
}
//# sourceMappingURL=prompt.js.map