import { Objects } from "./objects";
const { isObject, isString, isNull, notNull } = Objects;

export class DomUtils {

    static isElement(element: any): element is HTMLElement {
        if(typeof HTMLElement === 'object') return element instanceof HTMLElement;
        else return isObject(element) && element['nodeType'] === 1 && isString(element['nodeName']);
    }

    /**
     * Sets the value of element's first attribute whose qualified name is qualifiedName to value.
     * [MDN Reference](https://developer.mozilla.org/docs/Web/API/Element/setAttribute)
     */
    static setAttribute(element: HTMLElement, attribute: string, value: any): void {
        if(isNull(value)) element.removeAttribute(attribute);
        else element.setAttribute(attribute, value);
    }

    static setAttributes(element: HTMLElement, attributes: { [key: string]: any } = {}): void {
        if (DomUtils.isElement(element)) {
            const computedStyles = (rule: string, value: any): string[] => {
                const styles = (element as any)?.$attrs?.[rule] ? [(element as any)?.$attrs?.[rule]] : [];

                return [value].flat().reduce((cv, v) => {
                    if (v !== null && v !== undefined) {
                        const type = typeof v;

                        if (type === 'string' || type === 'number') {
                            cv.push(v);
                        } else if (type === 'object') {
                            const _cv = Array.isArray(v) ? computedStyles(rule, v) : Object.entries(v).map(([_k, _v]) => (rule === 'style' && (!!_v || _v === 0) ? `${_k.replace(/([a-z])([A-Z])/g, '$1-$2').toLowerCase()}:${_v}` : !!_v ? _k : undefined));

                            cv = _cv.length ? cv.concat(_cv.filter((c) => !!c)) : cv;
                        }
                    }

                    return cv;
                }, styles);
            };

            Object.entries(attributes).forEach(([key, value]) => {

                //remove attribute if value is null
                if(isNull(value)) element.removeAttribute(key);

                // update attribute
                else {
                    
                    const matchedEvent = key.match(/^on(.+)/);

                    if (matchedEvent) {
                        element.addEventListener(matchedEvent[1].toLowerCase(), value);
                    } //
                    else if (key === 'p-bind' || key === 'pBind') {
                        DomUtils.setAttributes(element, value);
                    } //
                    else {
                        value = key === 'class' ? [...new Set(computedStyles('class', value))].join(' ').trim() : key === 'style' ? computedStyles('style', value).join(';').trim() : value;
                        ((element as any).$attrs = (element as any).$attrs || {}) && ((element as any).$attrs[key] = value);
                        element.setAttribute(key, value);
                    }
                }
            });
        }
    }

    static insertElement(parent: Element, child: Node, position: number): void {
        const childSize = parent.childNodes.length;

        // insert last
        if (position < 0 || position >= childSize|| isNull(parent.firstChild)){
            parent.appendChild(child);
        }

        // insert first
        else if(position === 0 ) {
            parent.insertBefore(parent.firstChild, child);
        }

        // insert bettween
        else if(parent.childNodes.length) {
            let cnode = parent.childNodes.item(position);
            parent.insertBefore(cnode, child);
        }
    }

    static getStyleElement(document: Document, attribute: [string, string], styleId: string, hasCreate: boolean = true ): HTMLStyleElement {
        let selector = `style[${attribute[0]}=${attribute[1]}}]`;
        let element = document.querySelector(selector) || document.getElementById(styleId);
        if(hasCreate == false || notNull(element)) return <any>element;
        else {
            element = document.createElement('style');
            element.setAttribute('rel', 'stylesheet');
            return <any>element;
        }
    }

    static minifyCSS(css?: string): string | undefined {
        return css
            ? css
                .replace(/\/\*(?:(?!\*\/)[\s\S])*\*\/|[\r\n\t]+/g, '')
                .replace(/ {2,}/g, ' ')
                .replace(/ ([{:}]) /g, '$1')
                .replace(/([;,]) /g, '$1')
                .replace(/ !/g, '!')
                .replace(/: /g, ':')
            : css;
    }

    

}