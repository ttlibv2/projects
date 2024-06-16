import { Injectable } from "@angular/core";
import { NGXLoggerMapperService } from "ngx-logger";
import { LoggerPosition } from "./ilogger";

@Injectable({providedIn: "root"})
export class LoggerMapperService extends NGXLoggerMapperService {

    protected override getSourceMapLocation(stackLine: string): string {
        return super.getSourceMapLocation(stackLine);
    }

    /* Get position of caller without using sourceMaps
    * @param stackLine
    * @returns
    */
    protected override getLocalPosition(stackLine: string): LoggerPosition {
        // strip base path, then parse filename, line, and column, stackline looks like this :
        // Firefox
        // handleLog@http://localhost:4200/main.js:1158:29

        // Chrome and Edge
        // at AppComponent.handleLog (app.component.ts:38)

        let className, methodName;

        const firstPos = stackLine.indexOf('(');
        if(firstPos !== -1){
            const paths = stackLine.substring(3, firstPos).split('.');
            if(paths.length === 2) {
                className = paths[0].trim().split(' ')[1].substring(1);
                methodName = paths[1];
            }

        }




        const positionStartIndex = stackLine.lastIndexOf('\/');
        let positionEndIndex = stackLine.indexOf(')');
        
        if (positionEndIndex < 0) {
            positionEndIndex = undefined;
        }

        const position = stackLine.substring(positionStartIndex + 1, positionEndIndex);
        const dataArray = position.split(':');

        if (dataArray.length === 3) {
            return { 
                fileName: dataArray[0], 
                lineNumber: +dataArray[1], 
                columnNumber: +dataArray[2] ,
                className: className,
                methodName: methodName
            };
        }

        return { 
            fileName: 'unknown', 
            lineNumber: 0, 
            columnNumber: 0 
        };
    }


}