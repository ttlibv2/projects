class Version {
    full;
    major;
    minor;
    patch;
    constructor(full) {
        this.full = full;
        const [major, minor, patch] = full.split('-', 1)[0].split('.', 3);
        this.major = major;
        this.minor = minor;
        this.patch = patch;
    }
}
export const VERSION = new Version('0.0.0-PLACEHOLDER');
