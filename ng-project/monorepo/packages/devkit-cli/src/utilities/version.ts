// Same structure as used in framework packages
class Version {
  readonly major: string;
  readonly minor: string;
  readonly patch: string;

  constructor(readonly full: string) {
    const [major, minor, patch] = full.split("-", 1)[0].split(".", 3);
    this.major = major;
    this.minor = minor;
    this.patch = patch;
  }
}

export const VERSION = new Version("0.0.0-PLACEHOLDER");
