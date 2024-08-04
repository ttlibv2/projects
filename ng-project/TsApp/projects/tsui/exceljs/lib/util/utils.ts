import {Objects} from "ts-ui/helper";

export interface Path {
    path: string;
    name: string;
}
export class Utils {
    static readonly xmlDecodeRegex = /[<>&'"\x7F\x00-\x08\x0B-\x0C\x0E-\x1F]/;

    static dateToExcel(date: Date, date1904: boolean): number {
        return 25569 + date.getTime() / (24 * 3600 * 1000) - (date1904 ? 1462 : 0);
    }

    static excelToDate(value: number, date1904: boolean): Date {
        const millisecondSinceEpoch = Math.round((value - 25569 + (date1904 ? 1462 : 0)) * 24 * 3600 * 1000);
        return new Date(millisecondSinceEpoch);
    }

    static parsePath(filepath:string): Path {
        const last = filepath.lastIndexOf('/');
        return {
            path: filepath.substring(0, last),
            name: filepath.substring(last + 1),
        }
    }

    static getRelsPath(filepath:string):string {
        const path = Utils.parsePath(filepath);
        return `${path.path}/_rels/${path.name}.rels`;
    }

    static xmlEncode(text:string):string {
        const regexResult = Utils.xmlDecodeRegex.exec(text);
        if (!regexResult) return text;

        let result = '';
        let escape = '';
        let lastIndex = 0;
        let i = regexResult.index;
        for (; i < text.length; i++) {
            const charCode = text.charCodeAt(i);
            switch (charCode) {
                case 34: // "
                    escape = '&quot;';
                    break;
                case 38: // &
                    escape = '&amp;';
                    break;
                case 39: // '
                    escape = '&apos;';
                    break;
                case 60: // <
                    escape = '&lt;';
                    break;
                case 62: // >
                    escape = '&gt;';
                    break;
                case 127:
                    escape = '';
                    break;
                default: {
                    if (charCode <= 31 && (charCode <= 8 || (charCode >= 11 && charCode !== 13))) {
                        escape = '';
                        break;
                    }
                    continue;
                }
            }
            if (lastIndex !== i) {
                result += text.substring(lastIndex, i);
            }

            if (escape) {
                result += escape;
            }

            lastIndex = i + 1;
        }

        return lastIndex !== i ? result + text.substring(lastIndex, i): result;
    }

    static xmlDecode(text:string):string {
        return text.replace(/&([a-z]*);/g, c => {
            switch (c) {
                case '&lt;':
                    return '<';
                case '&gt;':
                    return '>';
                case '&amp;':
                    return '&';
                case '&apos;':
                    return '\'';
                case '&quot;':
                    return '"';
                default:
                    return c;
            }
        });
    }

    static  validInt(value:string):number {
        const i = parseInt(value, 10);
        return !Number.isNaN(i) ? i : 0;
    }

    static  isDateFmt(fmt:string): boolean {
        if (Objects.isBlank(fmt)) {
            return false;
        }

        // must remove all chars inside quotes and []
        fmt = fmt.replace(/\[[^\]]*]/g, '');
        fmt = fmt.replace(/"[^"]*"/g, '');
        // then check for date formatting chars
        return fmt.match(/[ymdhMsb]+/) !== null;
    }

    static  toIsoDateString(dt: Date):string {
        return dt.toISOString().substring(0, 10);
    }

    static parseBoolean(value: any): boolean {
        return value === true || value === 'true' || value === 1 || value === '1';
    }

    static toSortedArray(values: number[]): number[] {
        const result = Array.from(values);

        // Note: per default, `Array.prototype.sort()` converts values
        // to strings when comparing. Here, if we have numbers, we use
        // numeric sort.
        if (result.every(item => Number.isFinite(item))) {
            const compareNumbers = (a:number, b:number) => a - b;
            return result.sort(compareNumbers);
        }

        return result.sort();
    }


}