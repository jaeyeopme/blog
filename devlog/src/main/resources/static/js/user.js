window.addEventListener('scroll', () => {
    if (window.scrollY > 10) {
        document.getElementById('navbar').classList.add('shadow-sm');
    } else {
        document.getElementById('navbar').classList.remove('shadow-sm');
    }
});

function validate(element) {
    if (element.checkValidity() && element.value.search(' ') === -1) {
        element.classList.remove('is-invalid');
        element.classList.add('is-valid');
    } else {
        document.getElementById('email-feedback').innerText =
            '이메일을 확인해주세요.';
        element.classList.remove('is-valid');
        element.classList.add('is-invalid');
    }
}

function changeModal(element) {
    let text = element.innerText;
    let footer = document.getElementById('footer').innerHTML;

    if (text === '회원가입') {
        footer =
            '계정이 이미 있으신가요? <a class="text-success" onclick="changeModal(this)" style="cursor: pointer">로그인</a>';
        element.innerText = '로그인';
    } else if (text === '로그인') {
        footer =
            '아직 회원이 아니신가요? <a class="text-success" onclick="changeModal(this)" style="cursor: pointer">회원가입</a>';
        element.innerText = '회원가입';
    }

    document.getElementById('modal-header').innerText = text;
    document.getElementById('btn-sign').innerText = text;
}

function submitData(action) {
    const emailForm = document.getElementById('email');
    const passwordForm = document.getElementById('password');
    const emailFeedback = document.getElementById('email-feedback');

    if (emailForm.checkValidity() && passwordForm.checkValidity()) {
        switch (action) {
            case '회원가입':
                axios({
                    method: 'post',
                    url: '/api/users',
                    headers: {'content-type': 'application/json'},
                    data: JSON.stringify({
                        email: emailForm.value,
                        password: passwordForm.value,
                    }),
                }).then(location.href = '/')
                    .catch(error => {
                        emailFeedback.innerText = error.response.data.message;
                        emailForm.classList.add('is-invalid');
                    });
                break;
            case '로그인':
                document.getElementById('signinForm').submit();
                break;
        }
    }
}

function putPhoto(element) {
    const userId = document.getElementById('user-id').value
    const photo = document.getElementById('photo');
    const data = new FormData();
    data.append('photo', element.files[0]);

    axios({
        method: 'put',
        url: `/users/photo/${userId}`,
        headers: {'content-type': 'application/json'},
        data: data
    }).then(response =>
        photo.src = response.data
    ).catch(error => {
        alert(error.response.data.message);
        window.location.href = '/';
    })
}

function deletePhoto() {
    const userId = document.getElementById('user-id').value
    const photo = document.getElementById('photo');

    axios({
        method: 'delete',
        url: `/users/photo/${userId}`,
        headers: {'content-type': 'application/json'},
    }).then(response =>
        photo.src = response.data
    ).catch(error => {
        alert(error.response.data.message);
        window.location.href = '/';
    })
}

function userModify() {
    const userId = document.getElementById('user-id').value

    axios({
        method: 'put',
        url: `/users/${userId}`,
        headers: {'content-type': 'application/json'},
        data: JSON.stringify({
            introduction: document.getElementById('user-introduction').value
        })
    }).then(window.location.href = '/'
    ).catch(error => {
        alert(error.response.data.message);
        window.location.href = '/';
    })

}

function userDeleteModal() {
    $('#setting-modal').modal('hide');
}

function userDelete() {
    const userId = document.getElementById('user-id').value;

    axios({
        method: 'delete',
        url: `/users/${userId}`,
        headers: {'content-type': 'application/json'},
    }).then(location.href = '/')
        .catch(error => {
            alert(error.response.data.message);
            window.location.href = '/';
        });
}