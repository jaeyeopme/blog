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

function validate(element) {
    if (element.checkValidity() && element.value.search(' ') === -1) {
        element.classList.remove('is-invalid');
        element.classList.add('is-valid');
    } else {
        element.classList.remove('is-valid');
        element.classList.add('is-invalid');
    }
}

function changeModal(toggle) {
    if (toggle.innerText === '회원가입') {
        document.getElementById('footer').innerHTML =
            '계정이 이미 있으신가요? <a class="text-success" onclick="changeModal(this)" style="cursor: pointer">로그인</a>';
        document.getElementById('modal-header').innerText = '회원가입';
        document.getElementById('btn-submit').innerText = '회원가입';
        toggle.innerText = '로그인';
    } else if (toggle.innerText === '로그인') {
        document.getElementById('footer').innerHTML =
            '아직 회원이 아니신가요? <a class="text-success" onclick="changeModal(this)" style="cursor: pointer">회원가입</a>';
        document.getElementById('modal-header').innerText = '로그인';
        document.getElementById('btn-submit').innerText = '로그인';
        toggle.innerText = '회원가입';
    }
}

function submitData(actionType) {
    const action = actionType.innerHTML;
    const email = document.getElementById('email').value;
    const password = document.getElementById('password').value;

    switch (action) {
        case '회원가입':
            fetch('signup', {
                method: 'POST',
                headers: {
                    'Accept': 'application/json; charset=UTF-8',
                    'Content-Type': 'application/json; charset=UTF-8'
                },
                body: JSON.stringify({
                    email: email,
                    password: password
                })
            }).then(res => res.json())
                .catch(res => console.log(res));
            break;

        case '로그인':
            fetch('signin', {
                method: 'POST',
                headers: {
                    'Accept': 'application/json; charset=UTF-8',
                    'Content-Type': 'application/json; charset=UTF-8'
                },
                body: JSON.stringify({
                    email: email,
                    password: password
                })
            }).then(res => {
                return res;
            })
                .then((res) => console.log(res))
                .catch(error => console.log(error));
            break;
    }

}