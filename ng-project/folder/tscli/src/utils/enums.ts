export enum FrameworksEnum {
  ANGULAR = 'Angular',
  NESTJS = 'NestJS',
  SCHEMATICS = 'Schematics',
}

export enum CLI {
  Angular = 'Angular',
  Nestjs = 'Nestjs',
  SCHEMATICS = 'SCHEMATICS',
}

export const packageManagerCommands = {
  npm: 'install',
  yarn: 'add',
  pnpm: 'add',
  cnpm: 'install',
  bun: 'add',
};

export const packageManagerUninstallCommands = {
  npm: 'uninstall',
  yarn: 'remove',
  pnpm: 'remove',
  cnpm: 'uninstall',
  bun: 'remove',
};