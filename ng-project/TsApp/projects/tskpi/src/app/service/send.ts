import { Injectable } from "@angular/core";
import { KpiConfig } from "./kpi-config";
import { Observable, of, switchMap, throwError } from "rxjs";


export interface IDataSend {
    action: string,
    params?: { [p: string]: any },
    body?: { [p: string]: any },
}


@Injectable({ providedIn: 'root' })
export class SendKPI {


    constructor(public cfig: KpiConfig) { }

    private send(method: 'POST' | 'GET', data: IDataSend) {
        let body = JSON.stringify(data.body || {});
        let params = Object.assign({}, data.params, { action: data.action });
        let url = this.cfig.get().server_api;

        return this.cfig.http.request(method, url, { params, body }).pipe(switchMap((resp: any) => {
            if (resp['status'] === 'error') return throwError(() => resp);
            else return of(resp['result']);
        }));
    }

    createFile(email: string, mimeType: string, base64: string, fileNameUser: string, fileName: string) {
        //let data = dataUrl.replace(/^.*,/, '');
        //let contentType = dataUrl.match(/^.*(?=;)/)[0];
        return this.send("POST", {
            action: "createFile",
            params: { email },
            body: { data: base64, mimeType: mimeType, fileNameUser, fileName }
        })
    }

    sendRow(email: string, data: { [id: string]: any }) {
        return this.send("POST", {
            action: "appendRow",
            params: { email },
            body: { item: data }
        });
    }

    getFiles(email: string) {
        return this.send("GET", {
            action: "getAllFile",
            params: { email }
        })
    }

    getXslIdByEmail(email: string): Observable<any> {
        return this.send('POST', {
            action: 'getXslIdByEmail', 
            body: {email}, 
            params: {email}
        });
    }

}