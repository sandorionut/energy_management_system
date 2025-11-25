import {createTheme} from '@mui/material/styles';

const theme = createTheme({
    palette: {
        mode: 'light',
        primary: {main: '#2563eb'},
        secondary: {main: '#22c55e'},
    },
    shape: {borderRadius: 12},
    typography: {
        fontFamily: ['Inter', 'Roboto', 'Helvetica', 'Arial', 'sans-serif'].join(
            ','
        ),
        h4: {fontWeight: 700},
        button: {textTransform: 'none', fontWeight: 600},
    },
    components: {
        MuiPaper: {styleOverrides: {root: {borderRadius: 16}}},
    },
});

export default theme;
