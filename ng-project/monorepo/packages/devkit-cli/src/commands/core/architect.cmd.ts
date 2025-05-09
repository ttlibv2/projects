import { Target } from "@angular-devkit/architect";
import { workspaces } from "@angular-devkit/core";
import { Argv } from "yargs";
import { ArchitectBaseCommandModule } from "./architectbase.cmd";
import {
  ICommandModule,
  ArgOption,
  OtherOptions,
} from "./abstract.cmd";

export interface ArchitectCommandArgs {
  configuration?: string;
  project?: string;
}

export abstract class ArchitectCommandModule
  extends ArchitectBaseCommandModule<ArchitectCommandArgs>
  implements ICommandModule<ArchitectCommandArgs>
{
  abstract readonly multiTarget: boolean;

  findDefaultBuilderName?(
    project: workspaces.ProjectDefinition,
    target: Target,
  ): Promise<string | undefined>;

  async builder(argv: Argv): Promise<Argv<ArchitectCommandArgs>> {
    throw new Error(`ArchitectCommandModule::builder`);
    // const target = this.getArchitectTarget();

    // // Add default builder if target is not in project and a command default is provided
    // if (this.findDefaultBuilderName && this.context.workspace) {
    //   for (const [project, projectDefinition] of this.context.workspace.projects) {
    //     if (projectDefinition.targets.has(target)) {
    //       continue;
    //     }

    //     const defaultBuilder = await this.findDefaultBuilderName(projectDefinition, {
    //       project,
    //       target,
    //     });
    //     if (defaultBuilder) {
    //       projectDefinition.targets.set(target, {
    //         builder: defaultBuilder,
    //       });
    //     }
    //   }
    // }

    // const project = this.getArchitectProject();
    // const { jsonHelp, getYargsCompletions, utilities } = this.context.args.options;

    // const localYargs: Argv<ArchitectCommandArgs> = argv
    //   .positional('project', {
    //     describe: 'The name of the project to build. Can be an app or a library.',
    //     type: 'string',
    //     // Hide choices from JSON utilities so that we don't display them in AIO.
    //     choices: jsonHelp ? undefined : this.getProjectChoices(),
    //   })
    //   .option('configuration', {
    //     describe:
    //       `One or more named builder configurations as a comma-separated ` +
    //       `list as specified in the "configurations" section in angular.json.\n` +
    //       `The builder uses the named configurations to run the given target.\n` +
    //       `For more information, see https://angular.dev/reference/configs/workspace-config#alternate-build-configurations.`,
    //     alias: 'c',
    //     type: 'string',
    //     // Show only in when using --utilities and auto completion because otherwise comma seperated configuration values will be invalid.
    //     // Also, hide choices from JSON utilities so that we don't display them in AIO.
    //     choices:
    //       (getYargsCompletions || utilities) && !jsonHelp && project
    //         ? this.getConfigurationChoices(project)
    //         : undefined,
    //   })
    //   .strict();

    // if (!project) {
    //   return localYargs;
    // }

    // const schemaOptions = await this.getArchitectTargetOptions({
    //   project,
    //   target,
    // });

    // return this.addSchemaOptionsToCommand(localYargs, schemaOptions);
  }

  async run(
    options: ArgOption<ArchitectCommandArgs> & OtherOptions,
  ): Promise<number | void> {
    const target = this.getArchitectTarget();

    const { configuration = "", project, ...architectOptions } = options;

    if (!project) {
      // This runs each target sequentially.
      // Running them in parallel would jumble the log messages.
      let result = 0;
      const projectNames = this.getProjectNamesByTarget(target);
      if (!projectNames) {
        return this.onMissingTarget(
          "Cannot determine project or target for command.",
        );
      }

      for (const project of projectNames) {
        result |= await this.runSingleTarget(
          { configuration, target, project },
          architectOptions,
        );
      }

      return result;
    } else {
      return await this.runSingleTarget(
        { configuration, target, project },
        architectOptions,
      );
    }
  }

  private getArchitectProject(): string | undefined {
    const { options, positional } = this.context.args;
    const [, projectName] = positional;

    if (projectName) {
      return projectName;
    }

    // Yargs allows positional args to be used as flags.
    if (typeof options["project"] === "string") {
      return options["project"];
    }

    const target = this.getArchitectTarget();
    const projectFromTarget = this.getProjectNamesByTarget(target);

    return projectFromTarget?.length ? projectFromTarget[0] : undefined;
  }

  // @memoize
  private getProjectNamesByTarget(target: string): string[] | undefined {
    // const workspace = this.getWorkspaceOrThrow();
    // const allProjectsForTargetName: string[] = [];

    // for (const [name, project] of workspace.projects) {
    //   if (project.targets.has(target)) {
    //     allProjectsForTargetName.push(name);
    //   }
    // }

    // if (allProjectsForTargetName.length === 0) {
    //   return undefined;
    // }

    // if (this.multiTarget) {
    //   // For multi target commands, we always list all projects that have the target.
    //   return allProjectsForTargetName;
    // } else {
    //   if (allProjectsForTargetName.length === 1) {
    //     return allProjectsForTargetName;
    //   }

    //   const maybeProject = getProjectByCwd(workspace);
    //   if (maybeProject) {
    //     return allProjectsForTargetName.includes(maybeProject) ? [maybeProject] : undefined;
    //   }

    //   const { getYargsCompletions, utilities } = this.context.args.options;
    //   if (!getYargsCompletions && !utilities) {
    //     // Only issue the below error when not in utilities / completion mode.
    //     throw new CommandModuleError(
    //       'Cannot determine project for command.\n' +
    //         'This is a multi-project workspace and more than one project supports this command. ' +
    //         `Run "ng ${this.command}" to execute the command for a specific project or change the current ` +
    //         'working directory to a project directory.\n\n' +
    //         `Available projects are:\n${allProjectsForTargetName
    //           .sort()
    //           .map((p) => `- ${p}`)
    //           .join('\n')}`,
    //     );
    //   }
    // }

    // return undefined;
    throw new Error(`architect.cmd.ts::getProjectNamesByTarget`);
  }

  // /** @returns a sorted list of project names to be used for auto completion. */
  // private getProjectChoices(): string[] | undefined {
  //   const { workspace } = this.context;

  //   return workspace ? [...workspace.projects.keys()].sort() : undefined;
  // }

  // /** @returns a sorted list of configuration names to be used for auto completion. */
  // private getConfigurationChoices(project: string): string[] | undefined {
  //   const projectDefinition = this.context.workspace?.projects.get(project);
  //   if (!projectDefinition) {
  //     return undefined;
  //   }

  //   const target = this.getArchitectTarget();
  //   const configurations = projectDefinition.targets.get(target)?.configurations;

  //   return configurations ? Object.keys(configurations).sort() : undefined;
  // }
}