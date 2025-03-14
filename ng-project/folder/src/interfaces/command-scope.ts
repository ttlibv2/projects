export enum CommandScope {
  /** Command can only run inside an Angular workspace. */
  In,

  /** Command can only run outside an Angular workspace. */
  Out,

  /** Command can run inside and outside an Angular workspace. */
  Both,
}
