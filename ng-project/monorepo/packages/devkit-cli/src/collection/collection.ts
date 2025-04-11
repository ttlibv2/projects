export enum Collection {
  NgDevSC = '@ngdev/schematics',
  NestJS = '@nestjs/schematics',
  Angular = '@schematics/angular',
  Custom = ''
}

export interface ISchematic {
  name: string;
  alias: string;
  description: string;
}