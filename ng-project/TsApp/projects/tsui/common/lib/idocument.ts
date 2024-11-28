

export type DocumentEvents = { [K in keyof DocumentEventMap]?: (
    this: Document, ev: DocumentEventMap[K]) => any
};

export class Events {

   
    static addDocumentEvent(document: Document, events: DocumentEvents): void {
        Object.keys(events).forEach(evtKey => document.addEventListener(evtKey, (<any>events)[evtKey]));
    }

    static removeDocumentEvent(document: Document, events: DocumentEvents): void {
        Object.keys(events).forEach(evtKey => document.removeEventListener(evtKey, (<any>events)[evtKey]));
    }
}