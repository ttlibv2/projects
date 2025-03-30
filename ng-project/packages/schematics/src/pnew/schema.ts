export interface Schema {
  /**
   * The name for the new workspace
   */
  name: string;
  /**
   * The name for the new workspace
   */
  appsRoot?: string;
  /**
   * The name for the new workspace
   */
  libsRoot?: string;
  /**
   * Enable stricter type checking and bundle budget settings for projects created within the workspace. This helps improve maintainability and catch bugs early on
   */
  strict?: boolean;
  /**
   * Skip the automatic installation of packages. You will need to manually install the dependencies later.
   */
  skipInstall?: boolean;
  /**
   * Do not initialize a Git repository in the new workspace. By default, a Git repository is initialized to help you track changes to your project.
   */
  skipGit?: boolean;

  directory: string;

  commit?: boolean;
}
