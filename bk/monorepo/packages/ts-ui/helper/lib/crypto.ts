import crypto from "crypto-js";

export class CryptoUtil {

  static encodeBase64(string: string): string {
    const wa = crypto.enc.Utf8.parse(string);
    return crypto.enc.Base64.stringify(wa);
  }

  static decodeBase64(string: string): string {
    const wa = crypto.enc.Base64.parse(string);
    return wa.toString(CryptoJS.enc.Utf8);
  }

}