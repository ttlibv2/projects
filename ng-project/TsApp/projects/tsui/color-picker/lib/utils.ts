import { ElementRef } from "@angular/core";
import { Color, ColorValue, HsbaColorType } from "./color";
import { TransformOffset } from "./interface";

export class Utils {
    static readonly defaultColor = Utils.generateColor('#1677ff');

    static generateColor(color: ColorValue): Color {
        if (color instanceof Color) return color;
        else return new Color(color);
    };

    static calculateColor(
        offset: TransformOffset,
        containerRef: ElementRef,
        targetRef: ElementRef,
        color?: Color | null,
        type?: HsbaColorType
    ): Color {
        const { width, height } = containerRef.nativeElement.getBoundingClientRect();
        const { width: targetWidth, height: targetHeight } = targetRef.nativeElement.getBoundingClientRect();
        const centerOffsetX = targetWidth / 2;
        const centerOffsetY = targetHeight / 2;
        const saturation = (offset.x + centerOffsetX) / width;
        const bright = 1 - (offset.y + centerOffsetY) / height;
        const hsb = color?.toHsb() || { a: 0, h: 0, s: 0, b: 0 };
        const alphaOffset = saturation;
        const hueOffset = ((offset.x + centerOffsetX) / width) * 360;

        if (type) {
            switch (type) {
                case 'hue':
                    return Utils.generateColor({
                        ...hsb,
                        h: hueOffset <= 0 ? 0 : hueOffset
                    });
                case 'alpha':
                    return Utils.generateColor({
                        ...hsb,
                        a: alphaOffset <= 0 ? 0 : alphaOffset
                    });
            }
        }

        return Utils.generateColor({
            h: hsb.h,
            s: saturation <= 0 ? 0 : saturation,
            b: bright >= 1 ? 1 : bright,
            a: hsb.a
        });
    }

    static calculateOffset(
        containerRef: ElementRef,
        targetRef: ElementRef,
        color?: Color | null,
        type?: HsbaColorType
    ): TransformOffset | null {
        const { width, height } = containerRef.nativeElement.getBoundingClientRect();
        const { width: targetWidth, height: targetHeight } = targetRef.nativeElement.getBoundingClientRect();
        const centerOffsetX = targetWidth / 2;
        const centerOffsetY = targetHeight / 2;
        const hsb = color?.toHsb() || { a: 0, h: 0, s: 0, b: 0 };

        // Exclusion of boundary cases
        if ((targetWidth === 0 && targetHeight === 0) || targetWidth !== targetHeight) {
            return null;
        }

        if (type) {
            switch (type) {
                case 'hue':
                    return {
                        x: (hsb.h / 360) * width - centerOffsetX,
                        y: -centerOffsetY / 3
                    };
                case 'alpha':
                    return {
                        x: hsb.a * width - centerOffsetX,
                        y: -centerOffsetY / 3
                    };
            }
        }
        return {
            x: hsb.s * width - centerOffsetX,
            y: (1 - hsb.b) * height - centerOffsetY
        };
    };
}