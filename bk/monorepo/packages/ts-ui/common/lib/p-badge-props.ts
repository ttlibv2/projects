import { INgStyle, Severity } from "./interface";

export interface BadgeProps {
    
    /**
     * When specified, disables the component.
     * @group Props
     */
    disabled?: boolean;
    /**
     * Size of the badge, valid options are "large" and "xlarge".
     * @group Props
     */
    badgeSize?: 'large' | 'xlarge' | undefined;

    /**
     * Severity type of the badge.
     * @group Props
     */
    severity?: Severity;
    /**
     * Value to display inside the badge.
     * @group Props
     */
    value?: string | number;
    /**
     * Inline style of the element.
     * @group Props
     */
    badgeStyle?: INgStyle;
    /**
     * Class of the element.
     * @group Props
     */
    badgeStyleClass?: string;
}