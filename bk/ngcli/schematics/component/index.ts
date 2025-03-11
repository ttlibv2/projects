import { Rule,SchematicContext,Tree, apply, chain, mergeWith, template, url} from '@angular-devkit/schematics';

// Instead of `any`, it would make sense here to get a schema-to-dts package and output the
// interfaces so you get type-safe options.
export default function (options: any): Rule {
  // The chain rule allows us to chain multiple rules and apply them one after the other.
  return chain([
    (_tree: Tree, context: SchematicContext) => {
      // Show the options for this Schematics.
      context.logger.info('My Full Schematic: ' + JSON.stringify(options));
    },
    mergeWith(
      apply(url('./files'), [
        template({
          INDEX: options.index,
          name: options.name,
        }),
      ]),
    ),
  ]);
}
