
import { WriteStream } from 'node:tty';

export * as colors from 'ansis';

export function supportColor(stream: NodeJS.WritableStream = process.stdout): boolean {
  if (stream instanceof WriteStream) {
    return stream.hasColors();
  }

  try {
    // The hasColors function does not rely on any instance state and should ideally be static
    return WriteStream.prototype.hasColors();
  } catch {
    return process.env['FORCE_COLOR'] !== undefined && process.env['FORCE_COLOR'] !== '0';
  }
}
