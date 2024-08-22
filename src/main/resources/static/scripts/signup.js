document.addEventListener('DOMContentLoaded', () => {
    const togglePassword = document.querySelector('#togglePassword');
    const passwordField = document.querySelector('#password');

    togglePassword.addEventListener('click', () => {
        // Переключаем тип поля между password и text
        const type = passwordField.getAttribute('type') === 'password' ? 'text' : 'password';
        passwordField.setAttribute('type', type);

        // Переключаем иконку между 'eye' и 'eye-slash'
        togglePassword.querySelector('svg').setAttribute('d', type === 'password'
            ? 'M12 5C6.48 5 2 12 2 12s4.48 7 10 7 10-7 10-7-4.48-7-10-7zm0 12c-2.21 0-4.21-1.27-5.23-3.23l10.46-5.2C16.19 8.27 14.19 9.5 12 9.5c-2.48 0-4.5-2.02-4.5-4.5S9.52 0 12 0s4.5 2.02 4.5 4.5c0 1.99-1.49 3.63-3.4 4.07l-6.78 4.23C7.21 16.73 9.21 18 12 18z'
            : 'M12 3c-4.55 0-8.64 2.58-11.04 6.56C3.32 11.53 6.62 15 12 15c4.47 0 8.68-3.43 10.03-7.51C21.63 5.63 17.54 3 12 3zM12 13c-2.61 0-4.71-2.1-4.71-4.71S9.39 3.57 12 3.57c2.61 0 4.71 2.1 4.71 4.71S14.61 13 12 13z');
    });
});
