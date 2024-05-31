import { Injectable } from '@angular/core';
import * as crypto from 'crypto-js';

@Injectable({providedIn: 'root'})
export class StorageService {
    secretKey: string = 'encryptSecretKey';

    private encrypt(json: string): string {
       return crypto.AES.encrypt(json, this.secretKey)
           .toString(crypto.format.Hex);
    }

    private decrypt(json: string): string {
        return crypto.AES.decrypt(json, this.secretKey)
            .toString(crypto.enc.Utf8);
    }

    set(field: string, object: any): void {
        const json = typeof object === 'object' ? JSON.stringify(object) : object;
        localStorage.setItem(field, this.encrypt(json));
    }

    get<E> (field: string, update?: any): E | null {
        const item = localStorage.getItem(field);
        if(item == null) return null;
        else {
            const object: any = this.decrypt(item);
            return typeof update == 'object' ? Object.assign(update, object)
                : object;
        }
    }


}