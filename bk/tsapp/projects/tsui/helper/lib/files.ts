import { Objects } from "./objects";

const MIME_TYPES = {
    docx: 'application/vnd.openxmlformats-officedocument.wordprocessingml.document'
};

export class Files {
    static readonly XLSX_TYPE = 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet';
    static readonly XLS_TYPE = 'application/vnd.ms-excel';


    static readonly ALL_ACCEPT = {
        xlsx: Files.XLSX_TYPE,
        xls: Files.XLS_TYPE,
        docx: MIME_TYPES.docx
    };

    static newBlobXlsx(buffer: ArrayBuffer): Blob {
        return new Blob([buffer], {type:Files.XLSX_TYPE});
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