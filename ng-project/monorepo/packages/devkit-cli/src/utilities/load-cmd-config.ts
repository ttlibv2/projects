import { getLocalPackageUrl } from "@ngdev/devkit-core/utilities";
import { paths } from "../commands/helper/paths";

export function loadCmdConfig(currentDir: string) {
  const packageUrl = getLocalPackageUrl('@schematics/angular', currentDir);
  if(!packageUrl) throw new Error(`The package [@schematics/angular] not install`);
  else return require(paths.join(packageUrl, 'src/commands/command-config'));

}