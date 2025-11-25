import {useState} from 'react';
import {TextField, IconButton, InputAdornment} from '@mui/material';
import {Visibility, VisibilityOff} from '@mui/icons-material';

export default function PasswordField({
                                          label = 'Password',
                                          value,
                                          onChange,
                                          name = 'password',
                                          required = true,
                                      }) {
    const [show, setShow] = useState(false);
    return (
        <TextField
            label={label}
            name={name}
            value={value}
            onChange={onChange}
            type={show ? 'text' : 'password'}
            required={required}
            fullWidth
            InputProps={{
                endAdornment: (
                    <InputAdornment position="end">
                        <IconButton
                            edge="end"
                            onClick={() => setShow((v) => !v)}
                            aria-label="toggle password visibility"
                        >
                            {show ? <VisibilityOff/> : <Visibility/>}
                        </IconButton>
                    </InputAdornment>
                ),
            }}
        />
    );
}
