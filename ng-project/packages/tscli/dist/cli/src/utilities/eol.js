import { EOL } from 'node:os';
const CRLF = '\r\n';
const LF = '\n';
export function getEOL(content) {
    const newlines = content.match(/(?:\r?\n)/g);
    if (newlines?.length) {
        const crlf = newlines.filter((l) => l === CRLF).length;
        const lf = newlines.length - crlf;
        return crlf > lf ? CRLF : LF;
    }
    return EOL;
}
