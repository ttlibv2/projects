import { AngularWorkspace } from './config';
export declare enum PackageManager {
    Npm = "npm",
    Cnpm = "cnpm",
    Yarn = "yarn",
    Pnpm = "pnpm",
    Bun = "bun"
}
export interface PackageManagerUtilsContext {
    globalConfiguration: AngularWorkspace;
    workspace?: AngularWorkspace;
    root: string;
}
export declare class PackageManagerUtils {
    private readonly context;
    constructor(context: PackageManagerUtilsContext);
    /** Get the package manager name. */
    get name(): PackageManager;
    /** Get the package manager version. */
    get version(): string | undefined;
    /** Install a single package. */
    install(packageName: string, save?: 'dependencies' | 'devDependencies' | true, extraArgs?: string[], cwd?: string): Promise<boolean>;
    /** Install all packages. */
    installAll(extraArgs?: string[], cwd?: string): Promise<boolean>;
    /** Install a single package temporary. */
    installTemp(packageName: string, extraArgs?: string[]): Promise<{
        success: boolean;
        tempNodeModules: string;
    }>;
    private getArguments;
    private run;
    private getVersion;
    private getName;
    private hasLockfile;
    private getConfiguredPackageManager;
}
//# sourceMappingURL=pkg-manager.d.ts.map