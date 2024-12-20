import { InjectionToken } from "@angular/core";

/**
 * Throws an exception when two MatDrawer are matching the same position.
 * @docs-private
 */
export function throwMatDuplicatedDrawerError(position: string) {
    throw Error(`A drawer was already declared for 'position="${position}"'`);
}

/** Options for where to set focus to automatically on dialog open */
export type AutoFocusTarget = 'dialog' | 'first-tabbable' | 'first-heading';

/** Result of the toggle promise that indicates the state of the drawer. */
export type DrawerToggleResult = 'open' | 'close';

/** Drawer and SideNav display modes. */
export type DrawerMode = 'over' | 'push' | 'side';

/** Drawer and SideNav display position. */
export type DrawerPosition = 'left' | 'right' | 'top' | 'bottom' | 'full';


/**
 * Used to provide a drawer container to a drawer while avoiding circular references.
 * @docs-private
 */
export const DRAWER_CONTAINER = new InjectionToken('DRAWER_CONTAINER');

/** Configures whether drawers should use auto sizing by default. */
export const DRAWER_DEFAULT_AUTOSIZE = new InjectionToken<boolean>(
    'DRAWER_DEFAULT_AUTOSIZE',
    {
        providedIn: 'root',
        factory: DRAWER_DEFAULT_AUTOSIZE_FACTORY,
    },
);


export interface ContentMargin {
    left?: number;
    right?: number;
    top?: number;
    bottom?: number;
}


/** @docs-private */
export function DRAWER_DEFAULT_AUTOSIZE_FACTORY(): boolean {
    return false;
}
