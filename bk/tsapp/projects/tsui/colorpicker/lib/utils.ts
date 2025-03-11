import { ElementRef } from "@angular/core";
import { Color, ColorValue, HsbaColorType } from "./color";
import { TransformOffset } from "./interface";
import { ValidatorFn } from "@angular/forms";
import { Objects } from "ts-ui/helper";

export class Utils {
    static readonly defaultColor = Utils.generateColor('#22bc65');

    static validatorColorHexFn(): ValidatorFn {
        return control => {
            const REGEXP = /^[0-9a-fA-F]{6}$/;
            if (!control.value) return { error: true };
            else if (!REGEXP.test(control.value)) return { error: true };
            else return null;
        };
    }

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

    static clientRect(ref: ElementRef<any>) {
        return ref?.nativeElement?.getBoundingClientRect() || { x: 0, y: 0, width: 0, height: 0 };
    }


    static calculateOffset(containerRef: ElementRef, targetRef: ElementRef, color?: Color, type?: HsbaColorType): TransformOffset | null {
        const { width, height } = Utils.clientRect(containerRef);
        const { width: targetWidth, height: targetHeight } = Utils.clientRect(targetRef);

        // console.log(`calculateOffset`, targetWidth, targetHeight)

        // Exclusion of boundary cases
        if ((targetWidth === 0 && targetHeight === 0) || targetWidth !== targetHeight) {
            return null;
        }


        const centerOffsetY = targetHeight / 2;
        const hsb = color?.toHsb() || { a: 0, h: 0, s: 0, b: 0 };

        if (Objects.isNull(type)) {
            return {
                x: hsb.s * width - targetWidth,
                y: Math.max(0, (1 - hsb.b) * height - centerOffsetY)
            };
        }

        // HsbaColorType
        else {
            const offsetY = targetHeight > height ? (targetHeight - height) / 2 : centerOffsetY / 3;
            switch (type) {
                case 'hue':
                    return {
                        x: Math.max(0, (hsb.h / 360) * width - targetWidth / 2),
                        y: -offsetY
                    };
                case 'alpha':
                    return {
                        x: Math.max(0, hsb.a * width - targetWidth),
                        y: -offsetY
                    };
            }

        }
    };
}