'use strict';

const JOIN_URL = "/signup";
const LOGIN_URL = "/login";

const email_form = document.getElementById('email_form')
const auth = document.getElementsByClassName('auth');

console.log(window.Response.toString())
function _signup_user() {
    axios
        .post(JOIN_URL, {
            email: email_form.value,
        })
        .then((response) => (alert(response.data)))
        .catch((error) => {
            alert(error.response.data.message)
        });
}

function _login_user() {
    axios
        .post(LOGIN_URL, {
            email: email_form.value
        })
        .then((response) => (alert(response.data)))
        .catch((error) => {
            alert(error.response.data.message)
        });
}

function _auth_toggle() {
    Array.from(auth).forEach(element => element.toggleAttribute('hidden'))
}
