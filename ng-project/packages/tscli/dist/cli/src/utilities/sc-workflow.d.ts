import { logging } from '@angular-devkit/core';
import { NodeWorkflow } from '@angular-devkit/schematics/tools';
export declare function subscribeToWorkflow(workflow: NodeWorkflow, logger: logging.LoggerApi): {
    files: Set<string>;
    error: boolean;
    unsubscribe: () => void;
};
//# sourceMappingURL=sc-workflow.d.ts.map