import {chain, noop, Rule, SchematicContext, Tree} from '@angular-devkit/schematics';
import { addModuleImportToModule, buildComponent, findModuleFromOptions, isStandaloneSchematic} from '@angular/cdk/schematics';
import { ChangeDetection, ViewEncapsulation, Style} from '@schematics/angular/component/schema';

import { Schema } from "./schema";

export default function (options: Schema): Rule {
    options = Object.assign({}, options, {
      inlineTemplate: false,
      inlineStyle: true,
      standalone: true,
      displayBlock: true,
      style: Style.None,
      changeDetection: ChangeDetection.OnPush,
      viewEncapsulation: ViewEncapsulation.None
    } as Schema);

 return chain([
  buildComponent({...options},  {
    template: './__path__/__name@dasherize@if-flat__/__name@dasherize__.page.html.template',
    //stylesheet: './__path__/__name@dasherize@if-flat__/__name@dasherize__.component.__style__.template',
  }),
  options.skipImport ? noop() : addFormModulesToModule(options)
 ]);
}


/**
 * Adds the required modules to the relative module.
 */
function addFormModulesToModule(options: Schema) {
  return async (host: Tree, context: SchematicContext) => {
    const isStandalone = await isStandaloneSchematic(host, options);
    
    context.logger.warn('addFormModulesToModule: '+isStandalone)

    if (!isStandalone) {
      const modulePath = (await findModuleFromOptions(host, options))!;
      addModuleImportToModule(host, modulePath, 'MatInputModule', '@angular/material/input');
      addModuleImportToModule(host, modulePath, 'MatButtonModule', '@angular/material/button');
      addModuleImportToModule(host, modulePath, 'MatSelectModule', '@angular/material/select');
      addModuleImportToModule(host, modulePath, 'MatRadioModule', '@angular/material/radio');
      addModuleImportToModule(host, modulePath, 'MatCardModule', '@angular/material/card');
      addModuleImportToModule(host, modulePath, 'ReactiveFormsModule', '@angular/forms');
    }
  };
}