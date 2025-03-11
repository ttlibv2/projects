import { Pipe, PipeTransform } from '@angular/core';

@Pipe({standalone: true, name: 'json'})
export class JsonPipe implements PipeTransform {

  transform(object: any, space?: number | string): string {
   return JSON.stringify(object, null, space);
  }

}
