import { IconDefinition } from "./icon.common";

export class IconUtil {

  /**
   * Split a name with namespace in it into a tuple like [ name, namespace ].
   */
  static getNameAndNamespace(type: string): [string, string] {
    const split = type.split(':');
    switch (split.length) {
      case 1: return [type, ''];
      case 2: return [split[1], split[0]];
      default: throw new Error(`The icon type ${type} is not valid!`);
    }
  }

  /**
   * Parse inline SVG string and replace colors with placeholders. For twotone icons only.
   */
  static replaceFillColor(raw: string): string {
    return raw
      .replace(/['"]#333['"]/g, '"primaryColor"')
      .replace(/['"]#E6E6E6['"]/g, '"secondaryColor"')
      .replace(/['"]#D9D9D9['"]/g, '"secondaryColor"')
      .replace(/['"]#D8D8D8['"]/g, '"secondaryColor"');
  }

  static hasNamespace(type: string): boolean {
    return IconUtil.getNameAndNamespace(type)[1] !== '';
  }

  static cloneSVG(svg: SVGElement): SVGElement {
    return svg.cloneNode(true) as SVGElement;
  }

}