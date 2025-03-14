"use strict";
Object.defineProperty(exports, "__esModule", { value: true });
exports.assertIsError = assertIsError;
const tslib_1 = require("tslib");
const node_assert_1 = tslib_1.__importDefault(require("node:assert"));
function assertIsError(value) {
    const isError = value instanceof Error ||
        // The following is needing to identify errors coming from RxJs.
        (typeof value === 'object' && value && 'name' in value && 'message' in value);
    (0, node_assert_1.default)(isError, 'catch clause variable is not an Error instance');
}
