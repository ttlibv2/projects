import { Injectable } from "@angular/core";
import { ClientService } from "./client.service";
import { map } from "rxjs";

const synthaReact2Angular: string = 'https://syntha.ai/api/ai-public/converter';

@Injectable({ providedIn: 'root' })
export class CorsService extends ClientService {

    react2Angular(source: string) {
        const body = {
            apiUrl: synthaReact2Angular,
            payload: { "languageFrom": "React", "languageTo": "Angular", prompt: source }
        };

        return this.clientPost('cors-api/post', body, { responseType: 'text' }).pipe(
            map((res:string) => res.split('\n').map(seg => {
                let pos = seg.indexOf(':');
                let str = seg.substring(pos + 2, seg.length - 1);
                return str.replaceAll(/\\n/gm, '\n').replaceAll(/\\"/gm, "\"");
            }).join('')
            )
        );

    }
} 