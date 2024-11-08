import { IconDesc } from "./icon.interface";

export interface IconHandler {
    supports(description: string): boolean;
    resolve(description: string): IconDesc;
    getFamily(): string;
    getSrc(): string;
}