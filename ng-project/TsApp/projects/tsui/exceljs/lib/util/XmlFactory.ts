import {Utils} from "./utils";
import {Attribute} from "../doc/common";
import {Asserts, Objects} from "ts-ui/helper";

const {notNull} = Objects;

interface RollBackItem {
    xmlLength: number;
    stackLength: number;
    leaf: boolean;
    open: boolean;
}

export class XmlFactory {
    static readonly OPEN_ANGLE = '<';
    static readonly  CLOSE_ANGLE = '>';
    static readonly  OPEN_ANGLE_SLASH = '</';
    static readonly  CLOSE_SLASH_ANGLE = '/>';
    static readonly StdDocAttributes = {
        version: '1.0',
        encoding: 'UTF-8',
        standalone: 'yes',
    }

    private lsXml: string[];
    private stack: string[];
    private rollbacks: RollBackItem[];
    private open: boolean = false;
    private leaf: boolean = false;

    get tos(): string {
        return this.stack.length ? this.stack[this.stack.length-1] : undefined;
    }

    get cursor(): number {
        return this.lsXml.length;
    }

    get xml(): string {
        this.closeAll();
        return this.lsXml.join('');
    }

    openXml(attributes: Attribute[]): this {
        this.lsXml.push('<?xml');
        this.pushAttributes(attributes);
        this.lsXml.push('?>');
        return this;
    }

    openNode(name: string, attributes: Attribute[]): this {
        const parent = this.tos;
        if(parent && this.open) {
            this.lsXml.push(XmlFactory.CLOSE_ANGLE);
        }

        this.stack.push(name);

        // start streaming node
        this.lsXml.push(XmlFactory.OPEN_ANGLE);
        this.lsXml.push(name);
        this.pushAttributes(attributes);
        this.leaf = true;
        this.open = true;

        return this;
    }

    addAttr(name: string, value: string): this {
        Asserts.isTrue(this.open, 'Cannot write attributes to node if it is not open');
        if(notNull(value)) this.pushAttribute(name, value);
        return this;
    }

    addAttrs(attributes: Attribute[]): this {
        Asserts.isTrue(this.open, 'Cannot write attributes to node if it is not open');
        this.pushAttributes(attributes);
        return this;
    }

    writeText(text:string):this {
        if (this.open) {
            this.lsXml.push(XmlFactory.CLOSE_ANGLE);
            this.open = false;
        }

        this.leaf = false;
        this.lsXml.push(Utils.xmlEncode(text));
        return this;
    }

    writeXml(xml:string): this {
        if (this.open) {
            this.lsXml.push(XmlFactory.CLOSE_ANGLE);
            this.open = false;
        }
        this.leaf = false;
        this.lsXml.push(xml);
        return this;
    }

    closeNode(): this {
        const node = this.stack.pop();
        if (this.leaf) {
            this.lsXml.push(XmlFactory.CLOSE_SLASH_ANGLE);
        } else {
            this.lsXml.push(XmlFactory.OPEN_ANGLE_SLASH);
            this.lsXml.push(node);
            this.lsXml.push(XmlFactory.CLOSE_ANGLE);
        }
        this.open = false;
        this.leaf = false;
        return this;
    }

    leafNode(name:string, attributes:Attribute[], text:string):this {
        this.openNode(name, attributes);
        if(notNull(text))this.writeText(text);
        this.closeNode();
        return this;
    }

    closeAll():this {
        while (this.stack.length) {
            this.closeNode();
        }
        return this;
    }

    addRollback(): number {
        this.rollbacks.push({
            xmlLength: this.lsXml.length,
            stackLength: this.stack.length,
            leaf: this.leaf,
            open: this.open,
        });
        return this.cursor;
    }

    commit() {
        this.rollbacks.pop();
    }

    rollback() {
        const r = this.rollbacks.pop();
        if (this.lsXml.length > r.xmlLength) {
            this.lsXml.splice(r.xmlLength, this.lsXml.length - r.xmlLength);
        }
        if (this.stack.length > r.stackLength) {
            this.stack.splice(r.stackLength, this.stack.length - r.stackLength);
        }
        this.leaf = r.leaf;
        this.open = r.open;
    }

    private pushAttribute(name: string, value: string) {
        const valueEncode = Utils.xmlEncode(value.toString());
        this.lsXml.push(` ${name}="${valueEncode}"`);
    }

    private pushAttributes(attributes: Attribute[]) {
        if(Objects.notEmpty(attributes)) {
            attributes.filter(s => Objects.notNull(s.value))
                .forEach(s => this.pushAttribute(s.name, s.value));
        }
    }


}