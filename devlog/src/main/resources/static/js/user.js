'use strict';

// let index = {
//     init: function () {
//         $('#btn-user-join').on('click', () => {
//             if (document.getElementsByClassName('is-valid').length === 3) {
//                 this.join();
//             }
//         });
//
//         $('#btn-user-modify').on('click', () => {
//             if (document.getElementsByClassName('is-valid').length === 2 && confirm('modify?')) {
//                 this.userModify();
//             }
//         });
//     },
//
//     join: function () {
//         let data = {
//             email: document.getElementById('email').value,
//             password: document.getElementById('password').value,
//             username: document.getElementById('username').value
//         };
//
//         $.ajax({
//             url: 'users',
//             type: 'post',
//             data: JSON.stringify(data),
//             dataType: 'json',
//             contentType: 'application/json; charset=utf-8'
//         }).done(function (response) { // response
//             alert(response.message);
//             location.href = '/';
//         }).fail(function (error) {
//             // error body message (Object -> Json)
//             alert(error.responseJSON.message);
//             location.href = '/';
//         });
//     },
//
//     userModify: function () {
//         let data = {
//             email: document.getElementById('email').value,
//             password: document.getElementById('password').value,
//             username: document.getElementById('username').value
//         };
//
//         $.ajax({
//             url: 'users',
//             type: 'put',
//             data: JSON.stringify(data),
//             dataType: 'json',
//             contentType: 'application/json; charset=utf-8'
//         }).done(function (response) {
//             alert(response.message);
//             location.href = '/logout';
//         }).fail(function (error) {
//             // error body message (Object -> Json)
//             alert(error.responseJSON.message);
//             location.href = '/';
//         });
//     },
//
// }
//
// function joinFormValidate(target) {
//
//     switch (target.id) {
//         case 'email':
//             if (target.checkValidity() && (target.value.indexOf(' ') === -1) && (target.value.indexOf('@') > 0)) {
//                 $.ajax({
//                     url: 'users/' + target.value,
//                     type: 'GET'
//                 }).done(function (response) {
//                         target.classList.remove('is-invalid');
//                         target.classList.add('is-valid');
//                         document.getElementById('email-valid-feedback').innerText = response.message;
//                     }
//                 ).fail(function (error) {
//                     target.classList.remove('is-valid');
//                     target.classList.add('is-invalid');
//                     document.getElementById('email-invalid-feedback').innerText = error.responseJSON.message;
//                 });
//             } else {
//                 target.classList.remove('is-valid');
//                 target.classList.add('is-invalid');
//                 document.getElementById('email-invalid-feedback').innerText = 'Email ' + target.value + ' is invalid or already taken';
//             }
//             break;
//         case 'username' :
//             if (target.checkValidity() && (target.value.indexOf(' ')) === -1) {
//                 target.classList.remove('is-invalid');
//                 target.classList.add('is-valid');
//                 document.getElementById('username-valid-feedback').innerText = 'Username ' + target.value + ' is available';
//             } else {
//                 target.classList.remove('is-valid');
//                 target.classList.add('is-invalid');
//                 document.getElementById('username-invalid-feedback').innerText = 'Username ' + target.value + ' is not available.';
//             }
//             break;
//         case 'password':
//             if (target.checkValidity() && (target.value.indexOf(' ')) === -1) {
//                 target.classList.remove('is-invalid');
//                 target.classList.add('is-valid');
//                 document.getElementById('password-valid-feedback').innerText = "Password is available";
//             } else {
//                 target.classList.remove('is-valid');
//                 target.classList.add('is-invalid');
//                 document.getElementById('password-invalid-feedback').innerText = "Password is not available.";
//             }
//             break;
//     }
//
// }

window.addEventListener('scroll', () => {
    if (window.scrollY > 10) {
        document.getElementById('navbar').classList.add('shadow-sm');
    } else {
        document.getElementById('navbar').classList.remove('shadow-sm');
    }
})

function validate(element) {
    if (element.checkValidity() && element.value.search(' ') === -1) {
        element.classList.remove('is-invalid');
        element.classList.add('is-valid');
    } else {
        document.getElementById('email-feedback').innerText = '이메일을 확인해주세요.';
        element.classList.remove('is-valid');
        element.classList.add('is-invalid');
    }
}

function changeModal(element) {
    let text = element.innerText;
    let footer = document.getElementById('footer').innerHTML;

    if (text === '회원가입') {
        footer = '계정이 이미 있으신가요? <a class="text-success" onclick="changeModal(this)" style="cursor: pointer">로그인</a>';
        element.innerText = '로그인';
    } else if (text === '로그인') {
        footer = '아직 회원이 아니신가요? <a class="text-success" onclick="changeModal(this)" style="cursor: pointer">회원가입</a>';
        element.innerText = '회원가입';
    }

    document.getElementById('modal-header').innerText = text;
    document.getElementById('btn-sign').innerText = text;

}

function submitData(action) {
    const emailForm = document.getElementById('email')
    const passwordForm = document.getElementById('password');
    const emailFeedback = document.getElementById('email-feedback');

    if (emailForm.checkValidity() && passwordForm.checkValidity()) {
        switch (action) {
            case '회원가입':
                $.ajax({
                    url: '/users',
                    type: 'POST',
                    contentType: 'application/json; charset=UTF-8',
                    data: JSON.stringify({
                        email: emailForm.value,
                        password: passwordForm.value
                    }),
                    dataType: 'json'
                }).done(() => {
                    location.href = '/';
                }).fail(error => {
                    emailFeedback.innerText = error.responseJSON.message;
                    emailForm.classList.add('is-invalid');
                });
                break;
            case '로그인':
                document.getElementById('signinForm').submit();
                break;
        }
    }
}