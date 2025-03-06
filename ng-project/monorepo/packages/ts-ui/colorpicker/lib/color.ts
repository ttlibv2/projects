import { ColorInput, HSVA, Numberify, TinyColor } from '@ctrl/tinycolor';

export interface HSB {
    h: number | string;
    s: number | string;
    b: number | string;
}

export interface RGB {
    r: number | string;
    g: number | string;
    b: number | string;
}

export interface HSBA extends HSB {
    a: number;
}

export interface RGBA extends RGB {
    a: number;
}

export type ColorValue<T = Color> = string | number | RGB | RGBA | HSB | HSBA | T;

export type HsbaColorType = 'hue' | 'alpha' | 'select';

export const getRoundNumber = (value: number): number => Math.round(Number(value || 0));

const convertHsb2Hsv = (color: ColorValue): ColorInput => {
    if (color && typeof color === 'object' && 'h' in color && 'b' in color) {
        const { b, ...resets } = color as HSB;
        return {
            ...resets,
            v: b
        };
    }
    if (typeof color === 'string' && /hsb/.test(color)) {
        return color.replace(/hsb/, 'hsv');
    }
    return color as ColorInput;
};

export class Color extends TinyColor {

   
    constructor(color: ColorValue) {
        super(convertHsb2Hsv(color));
    }

    toHsbString(): string {
        const hsb = this.toHsb();
        const saturation = getRoundNumber(hsb.s * 100);
        const lightness = getRoundNumber(hsb.b * 100);
        const hue = getRoundNumber(hsb.h);
        const alpha = hsb.a;
        const hsbString = `hsb(${hue}, ${saturation}%, ${lightness}%)`;
        const hsbaString = `hsba(${hue}, ${saturation}%, ${lightness}%, ${alpha.toFixed(alpha === 0 ? 0 : 2)})`;
        return alpha === 1 ? hsbString : hsbaString;
    }

    toHsb(): Numberify<HSBA> {
        let hsv = this.toHsv();
        if (typeof this.originalInput === 'object' && this.originalInput) {
            if ('h' in this.originalInput) {
                hsv = this.originalInput as Numberify<HSVA>;
            }
        }

        const { v, ...resets } = hsv;
        return {  ...resets,   b: hsv.v  };
    }

    override toString(format: 'hsb' | 'rgb' | 'hex'): string {
        switch (format) {
            case 'hsb': return this.toHsbString();
            case 'rgb': return this.toRgbString();
            default: return this.getAlpha() < 1 ? this.toHex8String() : this.toHexString();
        }
    }   
    
}