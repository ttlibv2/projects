import * as path from "node:path";
import { StringMap, StringMapListener, validateSchema } from "@ngdev/devkit-core";
import {isNull} from "../utilities/helper";
import {createHost, defaultGlobalPath, getConfigPath} from "./ws.help";
import {existsSync} from "node:fs";
import {NotFoundError} from "../utilities/error";

export class WorkspaceDev {
  readonly basePath: string;

  constructor(
    private readonly filePath: string,
    private readonly props?: WorkspaceMeta) {
    this.basePath = path.dirname(filePath);
  }

  get projects(): ProjectMap | undefined {
    return this.props?.projects;
  }

  save(): Promise<void> {
    return Promise.resolve();
  }

  static async load(wsPath: string, hasValidate: boolean = true): Promise<WorkspaceDev> {
    const rawJson = await createHost().readFile(wsPath);
    if (isNull(rawJson)) {
      throw new NotFoundError(`Unable to read '${path}' file.`);
    }

    // validate json schema
    if (hasValidate) {
      const schemaPath = path.join(__dirname, "./schema.json");
      await validateSchema(schemaPath, rawJson, false);
    }

    try {
      return new WorkspaceDev(wsPath, JSON.parse(rawJson));
    }   //
    catch (error) {
      throw new Error(
        `Workspace config file cannot be loaded: ${wsPath}` +
          ` ==> ${error instanceof Error ? error.message : error}`,
      );
    }
  }

  static async global(): Promise<WorkspaceDev> {
    const configPath = getConfigPath('global');
    if(!configPath || !existsSync(configPath)) {
      return new WorkspaceDev(defaultGlobalPath());
    }
    return WorkspaceDev.load(configPath);
  }

  static async project(): Promise<WorkspaceDev | undefined> {
    const configPath = getConfigPath('local');
    return configPath ? WorkspaceDev.load(configPath) : undefined;
  }

}

interface WorkspaceMeta {
  name?: string;
  packageManager?: string;
  appsRoot?: string;
  libsRoot?: string;
  ngDefault?: string;
  projects?: ProjectMap;
}

interface ProjectMeta {
  name: string;
  root: string;
}

export class ProjectMap extends StringMap<ProjectMeta>  {

  constructor(
    initial?: Record<string, ProjectMeta>,
    listener?: StringMapListener<ProjectMeta> ) {
    super(initial, listener);
  }

  add(definition: ProjectMeta): ProjectMeta {

    if (this.has(definition.name)) {
      throw new Error('Project name already exists.');
    }

    this._validateName(definition.name);

    const project: ProjectMeta = {
      name: definition.name,
      root: definition.root
    };

    super.set(definition.name, project);

    return project;
  }

  override set(name: string, value: ProjectMeta): this {
    this._validateName(name);

    super.set(name, value);

    return this;
  }

  private _validateName(name: string): void {
    if (!/^(?:@\w[\w.-]*\/)?\w[\w.-]*$/.test(name)) {
      throw new Error('Project name must be a valid pnpm package name.');
    }
  }

}