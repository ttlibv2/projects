// Please be sure to import this
import prettierPlugin from "eslint-plugin-prettier"

export default [
    // rest of the code
    { plugins: { prettier: prettierPlugin }, rules: { "prettier/prettier": "error" } },
    { ignores: ["node_modules", "eslint.config.mjs", "**/dist", "config/*"], },
];