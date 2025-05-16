import { ArgOption, CommandModule, CommandScope, LocalArgv, OtherOptions } from './abstract.cmd';
import { memoize } from '@ngdev/devkit-core/utilities';
import { SCNodeWorkflow } from './sc-workflow';
import { Collection } from '../../collection';
import { getSchematicDefaults } from '../../workspace';
import { subscribeToWorkflow } from '../helper/schematic-workflow';
import { UnsuccessfulWorkflowExecution } from '@angular-devkit/schematics';
import { writeErrorToLogFile } from '../../utilities/log-file';
import { assertIsError } from '../../utilities/error';
import { schema } from '@angular-devkit/core';
import { getProjectName, getResolvePaths } from '../helper/helper';
import { Option } from '../helper/json-schema';
import { TsMap } from '@ngdev/utilities';

export interface RunSchematicOption {
    executionOptions: SchematicsExecutionOptions;
    schematicOptions: OtherOptions;
    collectionName: string;
    schematicName: string
}

interface SmartDefaultProviderOption extends SchematicsExecutionOptions {
    collectionName: string;
    providers: Record<string, schema.SmartDefaultProvider<any>>
}


export interface SchematicArg {

    /**Enable interactive input prompts*/
    interactive: boolean;

    /**Force overwriting of existing files.*/
    force: boolean;

    /**Run through and reports activity without writing out results*/
    dryRun: boolean;

    /**Disable interactive input prompts for options with a default.*/
    defaults: boolean;
}

export interface SchematicsExecutionOptions extends ArgOption<SchematicArg> {
    packageRegistry?: string;
    schemaValidation?: boolean;
    workflowRoot?: string; // default context.root
}

export abstract class SchematicsCommand<T extends SchematicArg> extends CommandModule<T> {
    override scope = CommandScope.In;
    allowPrivateSchematics: boolean = false;

    async builder(argv: LocalArgv): Promise<LocalArgv<T>> {
        return argv
            .option('interactive', {
                describe: 'Enable interactive input prompts.',
                type: 'boolean', default: true
            })
            .option('dryRun', {
                describe: 'Run through and reports activity without writing out results.',
                type: 'boolean', alias: ['d'], default: false
            })
            .option('defaults', {
                describe: 'Disable interactive input prompts for options with a default.',
                type: 'boolean', default: false
            })
            .option('force', {
                describe: 'Force overwriting of existing files.',
                type: 'boolean', default: false
            })
            .option('project', {
                describe: 'The project name',
                type: 'string'
            })
            .strict() as LocalArgv<T>;
    }

    protected async runSchematic(options: RunSchematicOption): Promise<number> {
        const { logger } = this.context;
        const { schematicOptions, executionOptions, collectionName, schematicName} = options;
        const workflow = await this.getOrCreateWorkflowExecution(collectionName, executionOptions);

        if (!schematicName) {
            throw new Error("schematicName cannot be undefined.");
        }

        const { unsubscribe, files } = subscribeToWorkflow(workflow, logger);

        try {
            await workflow.execute({
                collection: collectionName,
                schematic: schematicName,
                options: schematicOptions,
                logger: logger.scLogger,
                allowPrivate: this.allowPrivateSchematics,
            })
                .toPromise();

            if (!files.size) {
                logger.info("Nothing to be done.");
            }

            if (executionOptions.dryRun) {
                logger.warn(`\nNOTE: The "--dry-run" option means no changes were made.`,);
            }
        } //
        catch (err) {
            //
            // In case the workflow was not successful, show an appropriate error message.
            if (err instanceof UnsuccessfulWorkflowExecution) {
                // "See above" because we already printed the error.
                logger.fatal("The Schematic workflow failed. See above.");
                writeErrorToLogFile(err, 'schematics.cmd::runSchematic');
            } //
            else {
                assertIsError(err);
                logger.fatal(err.message);
                throw err;
            }

            return 1;
        } finally {
            unsubscribe();
        }

        return 0;
    }

    @memoize
    protected getOrCreateWorkflowBuilder(collectionName: string, workflowRoot?: string): SCNodeWorkflow {
        const options = {collectionName, resolvePaths: this.getResolvePaths(collectionName)};
        return SCNodeWorkflow.forBuilder(workflowRoot ?? this.context.root, options);
    }

    @memoize
    protected async getOrCreateWorkflowExecution(collectionName: string, options: SchematicsExecutionOptions): Promise<SCNodeWorkflow> {
        const { logger, root: contextRoot, packageManager } = this.context;
        const { force, dryRun, packageRegistry, schemaValidation = true, workflowRoot, defaults, interactive} = options;

        const workflowRootDir = workflowRoot || contextRoot;

        if(contextRoot != workflowRootDir) {
            console.warn(`workflowRootDir: `, workflowRootDir);
        }

        return SCNodeWorkflow.forExecution(workflowRootDir, {
            force, dryRun, packageRegistry,
            defaults, interactive, logger,collectionName,
            packageManager: packageManager.name.toLowerCase(),
            projectName: () => this.getProjectName(),
            resolvePaths: this.getResolvePaths(collectionName),
            schematicDefaults: (cn, sn, pn) => getSchematicDefaults(cn, sn, pn)
        });
    }

    @memoize
    protected async getCollectionFromConfig(): Promise<Set<string>> {
       return new Set([Collection.NgDevSC]);
    }

    @memoize
    protected async findCollectionBySchematic(schematic: string, defaultCollection?: string): Promise<string> {
        for (const collectionName of await this.getCollectionFromConfig()) {
            const workflow = this.getOrCreateWorkflowBuilder(collectionName);
            const schematicsInCollection = workflow.listSchematicNames(collectionName, true);
            if (schematicsInCollection.includes(schematic)) return collectionName;
        }
        return defaultCollection;
    }

    @memoize
    protected async getSchematicOption(collection: string, schematic: string): Promise<TsMap<string, Option>> {
        const workflow = this.getOrCreateWorkflowBuilder(collection);
        return await workflow.getSchematicOption(schematic);
    }

    protected getResolvePaths(collectionName: string): string[] {
        return getResolvePaths(this.context, collectionName);
    }

    private getProjectName(): string | undefined {
        return getProjectName(this.context);
    }

















    protected async addSmartDefaultProvider(options: SmartDefaultProviderOption) {
        const {providers, collectionName, ...workflowOptions } = options;
        const workflow = await this.getOrCreateWorkflowExecution(collectionName, workflowOptions);
        Object.entries(providers).forEach(provider => {
            console.log(provider);
            workflow.registry.addSmartDefaultProvider(provider[0], provider[1]);
        });
    }

}