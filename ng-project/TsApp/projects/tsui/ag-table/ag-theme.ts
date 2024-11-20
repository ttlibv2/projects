import { createTheme, iconSetMaterial, themeQuartz } from '@ag-grid-community/theming';

// to use myTheme in an application, pass it to the theme grid option
export const myTheme = createTheme().withPart(iconSetMaterial).withParams({
    accentColor: 'red',
    foregroundColor: '#660000',
    iconSize: 18,
});
