import { ColorPicker } from './picker';

export type ColorFormatType = 'rgb' | 'hex' | 'hsb';


export interface TransformOffset {
    x: number;
    y: number;
}


/**
 * Custom change event.
 * @see {@link ColorPicker.onChange}
 * @group Events
 */
export interface ColorPickerChangeEvent {
    /**
     * Browser event
     */
    originalEvent: Event;
    /**
     * Selected color value.
     */
    value: string | object;
}

/**
 * Custom change event.
 * @see {@link ColorPicker.onChange}
 * @group Events
 */
export interface ColorPickerChangeFormatEvent {
    /**
     * Selected color value.
     */
    format: ColorFormatType;

    color: string;
}

