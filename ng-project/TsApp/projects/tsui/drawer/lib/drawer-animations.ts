import { animate, AnimationTriggerMetadata, state, style, transition, trigger } from '@angular/animations';

/**
 * Animation that slides a drawer in and out. We remove the `transform` here completely, rather than setting it to zero, because:
 * - 1. Having a transform can cause elements with ripples or an animated transform to shift around in Chrome with an RTL layout (see #10023).
 * - 2. 3d transforms causes text to appear blurry on IE and Edge.
 */
export const matDrawerAnimations = trigger('transform', [

    state('open', style({ 'transform': 'none', 'visibility': 'visible' })),
    state('void', style({ 'box-shadow': 'none', 'visibility': 'hidden', })),
    transition(
        'void <=> open',
        animate('400ms cubic-bezier(0.25, 0.8, 0.25, 1)'),
    ),
]);

// export const matDrawerAnimations = trigger('transform', [

//     state('open, open-instant', style({ 'transform': '{{transform}}', 'visibility': 'visible' })),
//     state('void', style({ 'box-shadow': 'none', 'visibility': 'hidden', })),
//     transition('void => open-instant', animate('{{transition}}')),
//     transition(
//         'void <=> open, open-instant => void',
//         animate('{{transition}}'),
//     ),
// ]);


export const drawerMaskMotion: AnimationTriggerMetadata = trigger('drawerMaskMotion', [
    transition(':enter', [style({ opacity: 0 }), animate(`0.3s`, style({ opacity: 1 }))]),
    transition(':leave', [style({ opacity: 1 }), animate(`0.3s`, style({ opacity: 0 }))])
]);