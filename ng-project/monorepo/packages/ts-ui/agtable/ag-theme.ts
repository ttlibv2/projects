import { createTheme, iconSetMaterial } from 'ag-grid-enterprise';

// to use myTheme in an application, pass it to the theme grid option
export const myTheme = createTheme().withPart(iconSetMaterial).withParams({
    accentColor: 'red',
    foregroundColor: '#660000',
    iconSize: 18,
});
