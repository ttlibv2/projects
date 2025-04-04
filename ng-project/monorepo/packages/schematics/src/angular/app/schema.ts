import { Style, ViewEncapsulation } from '@schematics/angular/application/schema';

/**
 * Creates a new NgDev App
 */
export interface Schema {
    /**
     * The name for the new application.
     */
    name: string
    /**
     * Generate an application with routing already configured
     */
    routing?: boolean
    /**
     * A prefix to be added to the selectors of components generated within this application.
     */
    prefix?: string
    /**
     * Skip the generation of a unit test files `spec.ts`.
     */
    skipTests?: boolean
    /**
     * Do not add dependencies to the `package.json` file.
     */
    skipPackageJson?: boolean
    /**
     * Generate a minimal project without any testing frameworks. This is intended for learning purposes and simple experimentation, not for production applications.
     */
    minimal?: boolean
    /**
     * Skip the automatic installation of packages. You will need to manually install the dependencies later.
     */
    skipInstall?: boolean
    /**
     * Enable stricter bundle budget settings for the application. This helps to keep your application's bundle size small and improve performance. For more information, see https://angular.dev/tools/cli/template-typecheck#strict-mode
     */
    strict?: boolean
    /**
     * Configure the application for Server-Side Rendering (SSR) and Static Site Generation (SSG/Prerendering).
     */
    ssr?: boolean;

    /**
     * Set up a server application using the Server Routing and App Engine APIs (Developer
     * Preview).
     */
    serverRouting?: boolean;

    style?: Style;

    inlineStyle?: boolean;
    inlineTemplate?: boolean;
    viewEncapsulation?: ViewEncapsulation;

    ngVersion: string;
    direction: string;
}