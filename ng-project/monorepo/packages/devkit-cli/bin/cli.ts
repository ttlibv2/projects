#!/usr/bin/env node

import "symbol-observable";
import { createRequire } from "node:module";
import { loadLocalCli, localBinExists } from "../src/utilities/local-binaries";
import * as path from "node:path";

type BReturn = typeof import("../lib").default | null;

let forceExit = false;

const bootstrap = async (): Promise<BReturn> => {
  let cli = null;

  /**
   * Disable Browserslist old data warning as otherwise with every release we'd need to update this dependency
   * which is cumbersome considering we pin versions and the warning is not user actionable.
   * `Browserslist: caniuse-lite is outdated. Please run next command `npm update`
   * See: https://github.com/browserslist/browserslist/blob/819c4337456996d19db6ba953014579329e9c6e1/node.js#L324
   */
  process.env.BROWSERSLIST_IGNORE_OLD_DATA = "1";
  const rawCommandName = process.argv[2];

  /**
   * Disable CLI version mismatch checks and forces usage of the invoked CLI
   * instead of invoking the local installed version.
   *
   * When running `ngdev new` always favor the global version. As in some
   * cases orphan `node_modules` would cause the non global CLI to be used.
   * @see: https://github.com/angular/angular-cli/issues/14603
   */
  if (rawCommandName === "new") {
    return (await import("../lib")).default;
  }

  try {
    const cwdRequire = createRequire(process.cwd());
    const projectLocalCli = cwdRequire.resolve("@ngdev/cli");
    cli = await import(projectLocalCli);
  } catch (e) {
    //
    //console.error(e instanceof Error ? e.message : e);
    cli = await import("../lib");
  }

  if ("default" in cli) {
    cli = cli["default"];
  }

  return cli;
};

bootstrap()
  .then((cli) =>
    cli?.({
      cliArgs: process.argv.slice(2),
    }),
  )
  .then((exitCode = 0) => {
    if (forceExit) {
      process.exit(exitCode);
    }
    process.exitCode = exitCode;
  })
  .catch((err: Error) => {
    console.error("Unknown error: " + err.toString());
    process.exit(127);
  });
