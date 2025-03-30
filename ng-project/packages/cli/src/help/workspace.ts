import * as path from 'node:path';

export class Workspace {
  readonly basePath: string;

  constructor(readonly filePath: string) {
    this.basePath = path.dirname(filePath);
  }


}
