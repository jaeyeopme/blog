'use strict';

const JOIN_URL = "/api/users/register";
const LOGIN_URL = "/api/login";

const email_form = document.getElementById('email_form')
const auth = document.getElementsByClassName('auth');

function _join_user() {
    axios
        .post(JOIN_URL, {
            email: email_form.value,
        })
        .then(() => (window.location.href = '/'))
        .catch((error) => {
            alert(error.response.data.message)
        });
}

function _login_user() {
    axios
        .post(LOGIN_URL, {
            email: email_form.value
        })
        .then(() => (window.location.href = '/'))
        .catch((error) => {
            alert(error.response.data.message)
        });
}

function _auth_toggle() {
    Array.from(auth).forEach(element => element.toggleAttribute('hidden'))
}
