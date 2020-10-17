let index = {
    init: function () {
        $('#btn-join').on('click', () => {
            if (document.getElementsByClassName('is-valid').length === 3) {
                this.join();
            }
        });

        $('#btn-login').on('click', () => {
            if (document.getElementsByClassName('is-valid').length === 2) {
                document.getElementById('form-login').submit();
            }
        });
    },

    join: function () {
        let data = {
            email: document.getElementById('email').value,
            password: document.getElementById('password').value,
            username: document.getElementById('username').value
        };

        $.ajax({
            url: 'users',
            type: 'POST',
            data: JSON.stringify(data),
            dataType: 'json',
            contentType: 'application/json; charset=utf-8'
        }).done(function (response) {
            alert(response.message);
            location.href = '/';
        }).fail(function (error) {
            // error body message (Object -> Json)
            alert(error.responseJSON.message);
            location.href = '/';
        });
    },

    // login: function () {
    //     let email = document.getElementById('email');
    //     let password = document.getElementById('password');
    //
    //     let data = {
    //         email: email.value,
    //         password: password.value
    //     };
    //
    //     $.ajax({
    //         url: '/users/login',
    //         type: 'POST',
    //         data: JSON.stringify(data),
    //         dataType: 'json',
    //         contentType: 'application/json; charset=utf-8'
    //     }).done(function () {
    //         location.href = '/';
    //     }).fail(function (error) {
    //         email.value = "";
    //         email.classList.add('is-invalid');
    //         document.getElementById('email-invalid-feedback').innerText = error.responseJSON.message;
    //         password.value = "";
    //         password.classList.add('is-invalid');
    //         document.getElementById('password-invalid-feedback').innerText = error.responseJSON.message;
    //     });
    // }
}

function joinFormValidate(target) {

    switch (target.id) {
        case 'email':
            if (target.checkValidity() && (target.value.indexOf(' ') === -1) && (target.value.indexOf('@') > 0)) {
                $.ajax({
                    url: 'users/' + target.value,
                    type: 'GET'
                }).done(function () {
                        target.classList.remove('is-invalid');
                        target.classList.add('is-valid');
                        document.getElementById('email-valid-feedback').innerText = "Email " + target.value + " is available";
                    }
                ).fail(function (error) {
                    target.classList.remove('is-valid');
                    target.classList.add('is-invalid');
                    document.getElementById('email-invalid-feedback').innerText = error.responseJSON.message;
                });
            } else {
                target.classList.remove('is-valid');
                target.classList.add('is-invalid');
                document.getElementById('email-invalid-feedback').innerText = 'Email ' + target.value + ' is invalid or already taken';
            }
            break;
        case 'username' :
            if (target.checkValidity() && (target.value.indexOf(' ')) === -1) {
                target.classList.remove('is-invalid');
                target.classList.add('is-valid');
                document.getElementById('username-valid-feedback').innerText = 'Username ' + target.value + ' is available';
            } else {
                target.classList.remove('is-valid');
                target.classList.add('is-invalid');
                document.getElementById('username-invalid-feedback').innerText = 'Username ' + target.value + ' is not available.';
            }
            break;
        case 'password':
            if (target.checkValidity() && (target.value.indexOf(' ')) === -1) {
                target.classList.remove('is-invalid');
                target.classList.add('is-valid');
                document.getElementById('password-valid-feedback').innerText = "Password is available";
            } else {
                target.classList.remove('is-valid');
                target.classList.add('is-invalid');
                document.getElementById('password-invalid-feedback').innerText = "Password is not available.";
            }
            break;
    }

}

function loginFormValidate(target) {

    switch (target.id) {
        case 'email':
            if (target.checkValidity() && (target.value.indexOf(' ') < 0) && (target.value.indexOf('@') > 0)) {
                target.classList.remove('is-invalid');
                target.classList.add('is-valid');
            } else {
                target.classList.remove('is-valid');
                target.classList.add('is-invalid');
            }
            break;
        case 'password':
            if (target.checkValidity() && (target.value.indexOf(' ')) < 0) {
                target.classList.remove('is-invalid');
                target.classList.add('is-valid');
            } else {
                target.classList.remove('is-valid');
                target.classList.add('is-invalid');
            }
            break;
    }

}

index.init();