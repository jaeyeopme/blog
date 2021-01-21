function _join_user() {
    const username = document.getElementById('username_form').value;
    const password = document.getElementById('password_form').value;
    const name = document.getElementById('name_form').value;

    axios
        .post('/users', {
            username,
            password,
            name,
        })
        .then(() => window.location.href = '/login?join')
        .catch((error) => {
            document.getElementById('invalid_alert').style.display = 'block';
            document.getElementById('invalid_feedback').innerHTML = error.response.data.message;
        });
}
