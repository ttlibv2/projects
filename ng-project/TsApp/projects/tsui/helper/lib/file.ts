import { Objects } from "./objects";

export class FileUtil {

    static readonly ALL_ACCEPT = {
        xslx: 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet',
        xsl: 'application/vnd.ms-excel'
    };

    static extractAccept(extension: string, defaultValue: string): string {
        if(extension.includes('.')) {
            const last = extension.lastIndexOf('.');
            extension = extension.substring(last+1);
        }

        const accept = (<any>FileUtil.ALL_ACCEPT)[extension];
        return Objects.notNull(accept) ? accept: defaultValue ?? extension;
    }

}