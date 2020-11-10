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
                    url: '/users',
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
