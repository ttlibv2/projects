import ora from "ora";
import {normalize, join} from "node:path";
import {readFile} from "node:fs";
import {AbstractRunner} from '../runners/abstract.runner';
import {memoize} from "../utilities/memoize";
import {MESSAGES} from "../logui/messages";
import  {colors} from '../utilities/color';
import { Logger } from "../utilities/logger";

export enum EnumPkg {
  BUN = "bun",
  NPM = 'npm',
  PNPM = 'pnpm',
  YARN = 'yarn'
}

export interface PkgManagerCmd {
  install: string;
  add: string;
  update: string;
  remove: string;
  saveFlag: string;
  saveDevFlag: string;
  silentFlag: string;
  prefix?: string;
  noLockfile?: string;
}

export interface ProjectDependency {
  name: string;
  version: string;
}

export abstract class AbstractPkgManager {
  logger: Logger;

  protected constructor(
    protected runner: AbstractRunner) {}

  abstract get cli(): PkgManagerCmd;

  /** Get the package manager name. */
  abstract get name(): string;

  /** Get the package manager version. */
  @memoize
  get version(): Promise<string> {
    return this.runner.run('--version', { collect: true }) as Promise<string>;
  }

  async install(directory?: string) {

    const spinner = ora({
      spinner: { interval: 120,
        frames: ['▹▹▹▹▹', '▸▹▹▹▹', '▹▸▹▹▹', '▹▹▸▹▹', '▹▹▹▸▹', '▹▹▹▹▸'],
      },
      text: MESSAGES.PACKAGE_MANAGER_INSTALLATION_IN_PROGRESS,
    });

    spinner.start();

    try {
      const commandArgs = `${this.cli.install} ${this.cli.silentFlag}`;
      const cwd = directory ? normalize(directory) : process.cwd();
      await this.runner.run(commandArgs, { collect: true, cwd });

      spinner.succeed();

      console.info();
      console.info(MESSAGES.PACKAGE_MANAGER_INSTALLATION_SUCCEED(cwd));
      console.info(MESSAGES.GET_STARTED_INFORMATION);
      console.info();
      console.info(colors.gray(MESSAGES.CHANGE_DIR_COMMAND(cwd)));
      console.info(colors.gray(MESSAGES.START_COMMAND(this.name)));
      console.info();
    } //
    catch {
      spinner.fail();
      const commandArgs = this.cli.install;
      const commandToRun = this.runner.rawFullCommand(commandArgs);
      const msg = MESSAGES.PACKAGE_MANAGER_INSTALLATION_FAILED(colors.bold(commandToRun));
      console.error(colors.red(msg));
    }

  }

  /** install package devDependencies */
  async addDev(dependencies: string[], tag: string) {
    const command = `${this.cli.add} ${this.cli.saveDevFlag}`;
    const args: string = dependencies.map((dependency) => `${dependency}@${tag}`).join(' ');
    await this.add(`${command} ${args}`);
  }

  /** install package dependencies */
  async addPro(dependencies: string[], tag: string, cwd?: string): Promise<boolean> {
    const command: string = [this.cli.add, this.cli.saveFlag].filter((i) => i).join(' ');
    const args: string = dependencies.map((dependency) => `${dependency}@${tag}`).join(' ');
    const spinner = ora({
      spinner: {
        interval: 120,
        frames: ['▹▹▹▹▹', '▸▹▹▹▹', '▹▸▹▹▹', '▹▹▸▹▹', '▹▹▹▸▹', '▹▹▹▹▸'],
      },
      text: MESSAGES.PACKAGE_MANAGER_PRODUCTION_INSTALLATION_IN_PROGRESS,
    });

    spinner.start();
    try {
      await this.add(`${command} ${args}`, cwd);
      spinner.succeed();
      return true;
    }//
    catch {
      spinner.fail();
      return false;
    }
  }

  async updateDev(dependencies: string[]): Promise<void> {
    const commandArguments = `${this.cli.update} ${dependencies.join(' ')}`;
    await this.update(commandArguments);
  }

  async updateProduction(dependencies: string[]): Promise<void> {
    const commandArguments = `${this.cli.update} ${dependencies.join(' ')}`;
    await this.update(commandArguments);
  }

  async upgradePro(dependencies: string[], tag: string): Promise<void> {
    await this.deletePro(dependencies);
    await this.addPro(dependencies, tag);
  }

  async upgradeDev(dependencies: string[], tag: string): Promise<void> {
    await this.deleteDev(dependencies);
    await this.addDev(dependencies, tag);
  }

  async deletePro(dependencies: string[]): Promise<void>{
    const command: string = [this.cli.remove, this.cli.saveFlag].filter((i) => i).join(' ');
    const args: string = dependencies.join(' ');
    await this.delete(`${command} ${args}`);
  }

  async deleteDev(dependencies: string[]) : Promise<void>{
    const commandArguments = `${this.cli.remove} ${this.cli.saveDevFlag} ${dependencies.join(' ')}`;
    await this.delete(commandArguments);
  }

  async getProduction(): Promise<ProjectDependency[]> {
    const packageJsonContent = await this.readPackageJson();
    const packageJsonDependencies: any = packageJsonContent.dependencies;
    const dependencies = [];

    for (const [name, version] of Object.entries(packageJsonDependencies)) {
      dependencies.push({ name, version });
    }

    return dependencies as ProjectDependency[];
  }

  async getDevelopment(): Promise<ProjectDependency[]> {
    const packageJsonContent = await this.readPackageJson();
    const packageJsonDevDependencies: any = packageJsonContent.devDependencies;
    const dependencies = [];

    for (const [name, version] of Object.entries(packageJsonDevDependencies)) {
      dependencies.push({ name, version });
    }

    return dependencies as ProjectDependency[];
  }

  private async delete(commandArguments: string, cwd?: string): Promise<void> {
    await this.runner.run(commandArguments, { collect: true, cwd });
  }

  private async add(commandArguments: string, cwd?: string) {
    await this.runner.run(commandArguments, { collect: true, cwd });
  }

  private async readPackageJson(): Promise<any> {
    return new Promise<any>((resolve, reject) => {
      readFile(
        join(process.cwd(), 'package.json'),
        (error: NodeJS.ErrnoException | null, buffer: Buffer) => {
          if (error !== undefined && error !== null) {
            reject(error);
          } else {
            resolve(JSON.parse(buffer.toString()));
          }
        },
      );
    });
  }

  private async update(commandArguments: string) {
    await this.runner.run(commandArguments, { collect: true });
  }
}