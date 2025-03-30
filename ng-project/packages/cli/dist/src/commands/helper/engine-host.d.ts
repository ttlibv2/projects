import { RuleFactory } from '@angular-devkit/schematics';
import { FileSystemCollectionDesc, NodeModulesEngineHost } from '@angular-devkit/schematics/tools';
export declare class EngineHost extends NodeModulesEngineHost {
    protected _resolveReferenceString(refString: string, parentPath: string, collectionDescription?: FileSystemCollectionDesc): {
        ref: RuleFactory<{}>;
        path: string;
    } | null;
}
