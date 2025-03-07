"use strict";
/**
 * @license
 * Copyright Google LLC All Rights Reserved.
 *
 * Use of this source code is governed by an MIT-style license that can be
 * found in the LICENSE file at https://angular.dev/license
 */
Object.defineProperty(exports, "__esModule", { value: true });
exports.assertIsError = assertIsError;
const assert = require("node:assert");
// import assert from 'node:assert';
function assertIsError(value) {
    const isError = value instanceof Error ||
        // The following is needing to identify errors coming from RxJs.
        (typeof value === 'object' && value && 'name' in value && 'message' in value);
    assert(isError, 'catch clause variable is not an Error instance');
}
//# sourceMappingURL=error.js.map