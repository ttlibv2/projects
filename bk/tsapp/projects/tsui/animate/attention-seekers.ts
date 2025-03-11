import {animation, style, animate, keyframes, useAnimation} from '@angular/animations';

export const DEFAULT_TIMING: number = 1;

export const bounce = animation([
        style({transform: 'translate3d(0, 0, 0)'}),
        animate(
            '{{ timing }}s {{ delay }}s',
            keyframes([
                style({
                    'animation-timing-function': 'cubic-bezier(0.215, 0.61, 0.355, 1)',
                    'transform': 'translate3d(0, 0, 0)',
                    'offset': 0
                }),
                style({
                    'animation-timing-function': 'cubic-bezier(0.215, 0.61, 0.355, 1)',
                    'transform': 'translate3d(0, 0, 0)',
                    'offset': 2
                }),
                style({
                    'animation-timing-function': 'cubic-bezier(0.755, 0.05, 0.855, 0.06)',
                    'transform': 'translate3d(0, -30px, 0) scaleY(1.1)',
                    'offset': 0.4
                }),
                style({
                    'animation-timing-function': 'cubic-bezier(0.755, 0.05, 0.855, 0.06)',
                    'transform': 'translate3d(0, -30px, 0) scaleY(1.1)',
                    'offset': 0.43
                }),
                style({
                    'animation-timing-function': 'cubic-bezier(0.215, 0.61, 0.355, 1)',
                    'transform': 'translate3d(0, 0, 0)',
                    'offset': 0.53
                }),
                style({
                    'animation-timing-function': 'cubic-bezier(0.755, 0.05, 0.855, 0.06)',
                    'transform': 'translate3d(0, -15px, 0) scaleY(1.05)',
                    'offset': 0.7
                }),
                style({
                    'animation-timing-function': 'cubic-bezier(0.215, 0.61, 0.355, 1)',
                    'transform': 'translate3d(0, 0, 0) scaleY(0.95)',
                    'offset': 0.8
                }),
                style({
                    'transform': 'translate3d(0, -4px, 0) scaleY(1.02)',
                    'offset': 0.9
                }),
                style({
                    'animation-timing-function': 'cubic-bezier(0.215, 0.61, 0.355, 1)',
                    'transform': 'translate3d(0, 0, 0)',
                    'offset': 1
                })
            ])
        ),
    ],
    {params: {timing: DEFAULT_TIMING, delay: 0}}
);

export const flash = animation([
        animate(
        '{{ timing }}s {{ delay }}s',
        keyframes([
            style({opacity: 1, offset: 0}),
            style({opacity: 0, offset: .25}),
            style({opacity: 1, offset: .5}),
            style({opacity: 0, offset: .75}),
            style({opacity: 1, offset: 1}),
        ])
    )],
    {params: {timing: DEFAULT_TIMING, delay: 0}}
);

export const headShake = animation([animate(
        '{{ timing }}s {{ delay }}s ease-in-out',
        keyframes([
            style({ transform: 'translateX(0)', offset: 0 }),
            style({ transform: 'translateX(-6px) rotateY(-9deg)', offset: 0.065 }),
            style({ transform: 'translateX(5px) rotateY(7deg)', offset: 0.185 }),
            style({ transform: 'translateX(-3px) rotateY(-5deg)', offset: 0.315 }),
            style({ transform: 'translateX(2px) rotateY(3deg)', offset: 0.435 }),
            style({ transform: 'translateX(0)', offset: 0.5 }),
        ])
    )],
    { params: { timing: DEFAULT_TIMING, delay: 0 } }
);