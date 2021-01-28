'use strict';

const email_form = document.getElementById('email_form')
const auth = document.getElementsByClassName('auth');

function _join_user() {
    axios
        .post('/users', {
            email: email_form.value
        })
        .then(() => (window.location.href = '/'))
        .catch((error) => {
            document.getElementById('invalid_alert').style.display = 'block';
            document.getElementById('invalid_feedback').innerHTML =
                error.response.data.message;
        });
}

function _login_user() {
    axios
        .post('/login', {
            email: email_form.value
        })
        .then(() => (window.location.href = '/'))
        .catch((error) => {
            document.getElementById('invalid_alert').style.display = 'block';
            document.getElementById('invalid_feedback').innerHTML =
                error.response.data.message;
        });
}

function _auth_toggle() {
    Array.from(auth).forEach(element => element.toggleAttribute('hidden'))
}
