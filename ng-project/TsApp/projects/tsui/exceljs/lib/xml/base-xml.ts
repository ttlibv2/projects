import {XmlFactory} from "../util/XmlFactory";
import {Objects, TsMap} from "ts-ui/helper";
const {notNull, isNull} = Objects;
export abstract class BaseXml<M=any, O=any> {
    protected model: M;
    protected map: TsMap<string, BaseXml>;

    abstract prepare(model: M, options: O):void;
    abstract render(factory: XmlFactory, model: M): void;
    abstract parseOpen(node: string): boolean;
    abstract parseText(text: string): void;
    abstract parseClose(name: string): boolean;
    abstract reconcile(model: M, options: O): void;

    reset() {

        // to make sure parses don't bleed to next iteration
        this.model = null;

        // if we have a map - reset them too
        if(notNull(this.map)) {
            this.map.forEach(value => value.reset());
        }
    }

    mergeModel(object: M): this {
        this.model = Objects.mergeDeep(this.model, object);
        return this;
    }

    async parse(saxParser: any): Promise<M> {
        for await (const events of saxParser) {
            for(const  {eventType, value} of events) {
                if(eventType === 'opentag') this.parseOpen(value);
                else if(eventType === 'text') this.parseText(value);
                else if(eventType === 'closetag') {
                    if(!this.parseClose(value.name)) {
                        return this.model;
                    }
                }
            }
        }
        return this.model;
    }

    async parseStream(stream: any):Promise<M> {
        return this.parse(parseSax(stream));
    }

    get xml() {
        // convenience function to get the xml of this.model
        // useful for manager types that are built during the prepare phase
        return this.toXml(this.model);
    }

    toXml(model: M): string {
        const factory = new XmlFactory();
        this.render(factory, model);
        return factory.xml;
    }


    static toAttribute(value:any, defaultValue:any, always:boolean = false) {
        if (isNull(value)) {
            if (always) {
                return defaultValue;
            }
        } else if (always || value !== defaultValue) {
            return value.toString();
        }
        return undefined;
    }

    static toStringAttribute(value:any, defaultValue:any, always = false) {
        return BaseXml.toAttribute(value, defaultValue, always);
    }

    static toStringValue(attr: string, defaultValue:string):string {
        return isNull(attr) ? defaultValue : attr;
    }

    static toBoolAttribute(value:any, defaultValue:any, always: boolean = false):string {
        if (isNull(value)) {
            if (always) {
                return defaultValue;
            }
        } else if (always || value !== defaultValue) {
            return value ? '1' : '0';
        }
        return undefined;
    }

    static toBoolValue(attr:string, defaultValue:boolean): boolean {
        return isNull(attr) ? defaultValue : attr === '1';
    }

    static toIntAttribute(value: string, defaultValue: number, always: boolean = false): number {
        return BaseXml.toAttribute(value, defaultValue, always);
    }

    static toIntValue(attr:string, defaultValue: number): number {
        return isNull(attr) ? defaultValue : parseInt(attr, 10);
    }

    static toFloatAttribute(value: string, defaultValue: number, always: boolean = false) {
        return BaseXml.toAttribute(value, defaultValue, always);
    }

    static toFloatValue(attr:string, defaultValue: number): number {
        return attr === undefined ? defaultValue : parseFloat(attr);
    }
}