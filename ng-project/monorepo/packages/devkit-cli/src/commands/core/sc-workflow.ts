import { FileSystemCollectionDescription, FileSystemSchematicDescription, NodeWorkflow, NodeWorkflowOptions } from '@angular-devkit/schematics/tools';
import { Collection as NgCollection, UnsuccessfulWorkflowExecution, formats} from "@angular-devkit/schematics";
import { Logger } from '@ngdev/devkit-core/utilities';
import { EngineHost } from '../helper/engine-host';
import { JsonValue, schema } from '@angular-devkit/core';
import { normalize as devkitNormalize } from '@angular-devkit/core';
import { relative } from 'node:path';
import { isTTY } from '../../utilities/tty';
import { Option, parseJsonSchemaToOptions } from '../helper/json-schema';
import { TsMap } from '@ngdev/utilities';

type OmitOption = 'registry' | 'engineHostCreator' | 'optionTransforms' | 'schemaValidation';
type SchematicDefaultsFunc = (collectionName: string, schematicName: string, projectName: string) => Promise<Record<string, any>>;

type SchematicCollection = NgCollection<FileSystemCollectionDescription, FileSystemSchematicDescription>;


export interface SCExecutionOptions extends Omit<NodeWorkflowOptions, OmitOption> {

    resolvePaths: string[];
   // schemaValidation: boolean;

    /**Enable interactive input prompts*/
    interactive: boolean;

    /**Force overwriting of existing files.*/
    force: boolean;

    /**Run through and reports activity without writing out results*/
    dryRun: boolean;

    /**Disable interactive input prompts for options with a default.*/
    defaults: boolean;

    projectName: () => string;
    schematicDefaults: SchematicDefaultsFunc;
    collectionName: string;
    logger: Logger;
}

export interface SCBuilderOption extends NodeWorkflowOptions {
    collectionName: string;
}

export class SCNodeWorkflow extends NodeWorkflow {
    protected readonly collectionName: string;

    constructor(workflowRoot: string, options: NodeWorkflowOptions & {collectionName: string}) {
        super(workflowRoot, options);
        this.collectionName = options.collectionName;
        //this.createCollection(this.collectionName);
    }

    createCollection(collectionName?: string): SchematicCollection {
        return this.engine.createCollection(collectionName ?? this.collectionName);
    }

    listSchematicNames(collectionName?: string, includeHidden: boolean = true): string[] {
        return this.createCollection(collectionName).listSchematicNames(includeHidden);
    }

    existCollection(collection: string): boolean {
        try{this.createCollection(collection); return true;}
        catch (err) { return false; }
    }

    existSchematic(schematicName: string, collectionName?: string): boolean {
        return this.listSchematicNames(collectionName).includes(schematicName);
    }

    async getSchematicOption(schematicName: string, collectionName?: string): Promise<TsMap<string, Option>> {
        const collection = this.createCollection(collectionName);
        const {description: {schemaJson}} = collection.createSchematic(schematicName, true);
        const options: Option[] = schemaJson ? await parseJsonSchemaToOptions(this.registry, schemaJson) : [];
        return new TsMap(options.map(opt => [opt.name, opt]));
    }
















    static forBuilder(workflowRoot: string, options: {resolvePaths: string[], collectionName: string}) {
        return new SCNodeWorkflow(workflowRoot, {
            collectionName: options.collectionName,
            resolvePaths: options.resolvePaths,
            engineHostCreator: options => new EngineHost(options.resolvePaths)
        });
    }

    static forExecution(workflowRoot: string, options: SCExecutionOptions) {
        const {interactive, defaults, projectName: getProjectName, logger, schematicDefaults, ...nodeOptions} = options;


        const workflow = new SCNodeWorkflow(workflowRoot, {
            ...nodeOptions,
            schemaValidation: true,
            registry: new schema.CoreSchemaRegistry(formats.standardFormats),
            engineHostCreator: (options) => new EngineHost(options.resolvePaths),
            optionTransforms: [
                async (schematic, current) => {
                    const projectName = typeof current?.project === "string" ? current.project : getProjectName();
                    const scDefaults = await schematicDefaults(schematic.collection.name, schematic.name, projectName);
                    return { ...scDefaults, ...current};
                }
            ]
        });

        workflow.registry.addPostTransform(schema.transforms.addUndefinedDefaults);
        workflow.registry.useXDeprecatedProvider((msg) => logger.warn(msg));

        const workingDir = devkitNormalize(relative(workflowRoot, process.cwd()),);
        workflow.registry.addSmartDefaultProvider("workingDirectory", () => workingDir === "" ? undefined : workingDir );
        workflow.registry.addSmartDefaultProvider("projectName", () => getProjectName());
        workflow.engineHost.registerOptionsTransform(async (schematic, options) => options);

        if (options.interactive !== false && isTTY()) {
            applyInquirerPrompts(workflow, {defaults});
        }

        return workflow;
    }


}

function applyInquirerPrompts(workflow: SCNodeWorkflow, options: {defaults: boolean}) {
    workflow.registry.usePromptProvider(
        async (definitions: Array<schema.PromptDefinition>) => {
            let prompts: typeof import("@inquirer/prompts") | undefined;
            const answers: Record<string, JsonValue> = {};

            for (const definition of definitions) {
                if (options.defaults && definition.default !== undefined) {
                    continue;
                }

                // Only load prompt package if needed
                prompts ??= await import("@inquirer/prompts");

                switch (definition.type) {
                    case "confirmation":
                        answers[definition.id] = await prompts.confirm({
                            message: definition.message,
                            default: definition.default as boolean | undefined,
                        });
                        break;
                    case "list":
                        if (!definition.items?.length) {
                            continue;
                        }

                        answers[definition.id] = await (
                            definition.multiselect ? prompts.checkbox : prompts.select
                        )({
                            message: definition.message,
                            validate: (values) => {
                                if (!definition.validator) {
                                    return true;
                                }

                                return definition.validator(
                                    Object.values(values).map(({ value }) => value),
                                );
                            },
                            default: definition.multiselect
                                ? undefined
                                : definition.default,
                            choices: definition.items?.map((item) =>
                                typeof item == "string"
                                    ? {
                                        name: item,
                                        value: item,
                                    }
                                    : {
                                        ...item,
                                        name: item.label,
                                        value: item.value,
                                    },
                            ),
                        });
                        break;
                    case "input": {
                        let finalValue: JsonValue | undefined;
                        answers[definition.id] = await prompts.input({
                            message: definition.message,
                            default: definition.default as string | undefined,
                            async validate(value) {
                                if (definition.validator === undefined) {
                                    return true;
                                }

                                let lastValidation: ReturnType<
                                    typeof definition.validator
                                > = false;
                                for (const type of definition.propertyTypes) {
                                    let potential;
                                    switch (type) {
                                        case "string":
                                            potential = String(value);
                                            break;
                                        case "integer":
                                        case "number":
                                            potential = Number(value);
                                            break;
                                        default:
                                            potential = value;
                                            break;
                                    }
                                    lastValidation = await definition.validator(potential);

                                    // Can be a string if validation fails
                                    if (lastValidation === true) {
                                        finalValue = potential;

                                        return true;
                                    }
                                }

                                return lastValidation;
                            },
                        });

                        // Use validated value if present.
                        // This ensures the correct type is inserted into the final schema options.
                        if (finalValue !== undefined) {
                            answers[definition.id] = finalValue;
                        }
                        break;
                    }
                }
            }

            return answers;
        },
    );
}