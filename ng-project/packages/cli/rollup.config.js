import typescript from '@rollup/plugin-typescript';
import terser from '@rollup/plugin-terser';

export default {
    input: 'bin/lib.ts',
    output: {
        dir: 'dist',
        format: 'esm',
        entryFileNames: '[name].mjs'
    },
    plugins: [
        typescript(),
        terser({
            format: {
                comments: 'some',
                beautify: true,
                ecma: '2022',
            },
            compress: false,
            mangle: false,
            module: true,
        }),
    ]
};
