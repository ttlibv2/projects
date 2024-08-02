import CryptoJS from "crypto-js";
// declare let Buffer: any;

export class Base64 {

   static encode(str: string): string;
   static encode(str: ArrayBuffer): string;
   static encode(str: File): Promise<string>;

    static encode(str: any): any {

      if(str instanceof File) {
         return str.arrayBuffer().then(buffer => {
            const wd = CryptoJS.lib.WordArray.create(buffer);
            return CryptoJS.enc.Base64.stringify(wd);
         })
         
      }

      else {
         const wd = CryptoJS.lib.WordArray.create(str);
         return CryptoJS.enc.Base64.stringify(wd);
      }
     
    }




    static decode(str: string): any {
        //return Buffer.from(str, 'base64');
        return null;
     }

}