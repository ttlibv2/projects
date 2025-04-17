import { normalizeToKebabOrSnakeCase } from "../utilities/helper";

export class SchematicOption {
  constructor(
    private name: string,
    private value: boolean | string | null,
  ) {}

  get normalizedName() {
    return normalizeToKebabOrSnakeCase(this.name);
  }

  toCommandString(): string {
    const { name, value, normalizedName } = this;

    if (typeof value === "string") {
      if (name === "name") {
        return `--${normalizedName}=${this.format()}`;
      } else if (name === "version" || name === "path") {
        return `--${normalizedName}=${this.value}`;
      } else {
        return `--${normalizedName}="${value}"`;
      }
    } else if (typeof value === "boolean") {
      const str = normalizedName;
      return value ? `--${str}` : `--no-${str}`;
    } else {
      return `--${normalizedName}=${value}`;
    }
  }

  private format() {
    return normalizeToKebabOrSnakeCase(this.value as string)
      .split("")
      .reduce((content, char) => {
        if (char === "(" || char === ")" || char === "[" || char === "]") {
          return `${content}\\${char}`;
        }
        return `${content}${char}`;
      }, "");
  }
}
