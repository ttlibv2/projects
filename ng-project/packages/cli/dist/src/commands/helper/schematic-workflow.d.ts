import { NodeWorkflow } from '@angular-devkit/schematics/tools';
import { Logger } from '../../utilities/logger';
export declare function subscribeToWorkflow(workflow: NodeWorkflow, logger: Logger): {
    files: Set<string>;
    error: boolean;
    unsubscribe: () => void;
};
