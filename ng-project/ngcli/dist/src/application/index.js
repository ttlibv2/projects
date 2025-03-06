"use strict";
var __awaiter = (this && this.__awaiter) || function (thisArg, _arguments, P, generator) {
    function adopt(value) { return value instanceof P ? value : new P(function (resolve) { resolve(value); }); }
    return new (P || (P = Promise))(function (resolve, reject) {
        function fulfilled(value) { try { step(generator.next(value)); } catch (e) { reject(e); } }
        function rejected(value) { try { step(generator["throw"](value)); } catch (e) { reject(e); } }
        function step(result) { result.done ? resolve(result.value) : adopt(result.value).then(fulfilled, rejected); }
        step((generator = generator.apply(thisArg, _arguments || [])).next());
    });
};
Object.defineProperty(exports, "__esModule", { value: true });
exports.default = default_1;
const schematics_1 = require("@angular-devkit/schematics");
function default_1(options) {
    return (host, context) => __awaiter(this, void 0, void 0, function* () {
        let json = host.readText('.project.json');
        let jv = JSON.parse(json);
        let appDir = jv.appRoot + '/' + options.name;
        const ngAppOption = {
            projectRoot: appDir,
            name: options.name,
            inlineStyle: options.inlineStyle,
            inlineTemplate: options.inlineTemplate,
            prefix: options.prefix,
            viewEncapsulation: options.viewEncapsulation,
            routing: options.routing,
            style: options.style,
            skipTests: options.skipTests,
            skipPackageJson: false,
            skipInstall: true,
            strict: options.strict,
            minimal: options.minimal,
            standalone: options.standalone,
            ssr: options.ssr,
            serverRouting: options.serverRouting,
            experimentalZoneless: options.experimentalZoneless,
        };
        return (0, schematics_1.chain)([
            (0, schematics_1.externalSchematic)('@schematics/angular', 'application', ngAppOption),
            (0, schematics_1.move)(appDir)
        ]);
    });
}
//# sourceMappingURL=index.js.map