import { WriteStream } from 'node:tty';
export { color as colors, figures } from 'listr2';
export function supportColor(stream = process.stdout) {
    if (stream instanceof WriteStream) {
        return stream.hasColors();
    }
    try {
        // The hasColors function does not rely on any instance state and should ideally be static
        return WriteStream.prototype.hasColors();
    }
    catch {
        return process.env['FORCE_COLOR'] !== undefined && process.env['FORCE_COLOR'] !== '0';
    }
}
