import { Objects } from "./objects";

export class Files {

    static readonly ALL_ACCEPT = {
        xslx: 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet',
        xsl: 'application/vnd.ms-excel'
    };

    static newBlobXslx(buffer: ArrayBuffer): Blob {
        return new Blob([buffer], {type:Files.ALL_ACCEPT.xslx});
    }

    static extractAccept(extension: string, defaultValue: string): string {
        if(extension.includes('.')) {
            const last = extension.lastIndexOf('.');
            extension = extension.substring(last+1);
        }

        const accept = (<any>Files.ALL_ACCEPT)[extension];
        return Objects.notNull(accept) ? accept: defaultValue ?? extension;
    }

}