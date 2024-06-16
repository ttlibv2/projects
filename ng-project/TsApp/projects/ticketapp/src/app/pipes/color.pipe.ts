import { Pipe, PipeTransform } from '@angular/core';

@Pipe({standalone: true, name: 'color'})
export class ColorPipe implements PipeTransform {

  transform(color: any, type?: 'hex' | 'rgb'): any {
   if(type === 'rgb') return `rgb(${color.r}, ${color.g}, ${color.b})`;
   else return color;
  }

}
