import { ComponentRef } from "@angular/core";
import { Observable } from "rxjs";

export abstract class DrawerRef<T=any, R=any> {
    abstract afterClose: Observable<R>;
    abstract afterOpen: Observable<void>;
    abstract close(result?: R): void;
    abstract open(): void;
    abstract getContentComponent(): T;
    abstract getContentComponentRef(): Readonly<ComponentRef<T>>;
}