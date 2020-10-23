let index = {
    init: function () {
        $('#btn-user-join').on('click', () => {
            if (document.getElementsByClassName('is-valid').length === 3) {
                this.join();
            }
        });

        $('#btn-user-modify').on('click', () => {
            if (document.getElementsByClassName('is-valid').length === 2) {
                this.userModify();
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
            type: 'post',
            data: JSON.stringify(data), // -> http body
            dataType: 'json', // 응답받는 데이터 형식
            contentType: 'application/json; charset=utf-8' // 보내는 데이터 형식 // http header
        }).done(function (response) { // response
            alert(response.message);
            location.href = '/';
        }).fail(function (error) {
            // error body message (Object -> Json)
            alert(error.responseJSON.message);
            location.href = '/';
        });
    },

    userModify: function () {
        let data = {
            email: document.getElementById('email').value,
            password: document.getElementById('password').value,
            username: document.getElementById('username').value
        };

        $.ajax({
            url: 'users',
            type: 'put',
            data: JSON.stringify(data),
            dataType: 'json',
            contentType: 'application/json; charset=utf-8'
        }).done(function (response) {
            alert(response.message);
            location.href = '/logout';
        }).fail(function (error) {
            // error body message (Object -> Json)
            alert(error.responseJSON.message);
            location.href = '/';
        });
    }

}

function joinFormValidate(target) {

    switch (target.id) {
        case 'email':
            if (target.checkValidity() && (target.value.indexOf(' ') === -1) && (target.value.indexOf('@') > 0)) {
                $.ajax({
                    url: 'users/' + target.value,
                    type: 'GET'
                }).done(function (response) {
                        target.classList.remove('is-invalid');
                        target.classList.add('is-valid');
                        document.getElementById('email-valid-feedback').innerText = response.message;
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

function formValidate(target) {
    if (target.checkValidity()) {
        target.classList.remove('is-invalid');
        target.classList.add('is-valid');
    } else {
        target.classList.remove('is-valid');
        target.classList.add('is-invalid');
    }
}

index.init();