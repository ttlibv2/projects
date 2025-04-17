function _isTruthy(value: undefined | string): boolean {
  // Returns true if value is a string that is anything but 0 or false.
  return (
    value !== undefined && value !== "0" && value.toUpperCase() !== "FALSE"
  );
}

export function isTTY(stream: NodeJS.WriteStream = process.stdout): boolean {
  // If we force TTY, we always return true.
  const force = process.env["NG_FORCE_TTY"];
  if (force !== undefined) {
    return _isTruthy(force);
  }

  return !!stream.isTTY && !_isTruthy(process.env["CI"]);
}
