'use strict';

const content_form = new SimpleMDE({
    element: document.getElementById('content_form'),
    status: false,
    toolbar: [
        'bold',
        'italic',
        'heading',
        '|',
        'code',
        'quote',
        'unordered-list',
        'ordered-list',
        '|',
        'link',
        'image',
        'table',
        '|',
        'guide',
        'side-by-side',
        'fullscreen',
    ],
    spellChecker: false,
});
content_form.toggleSideBySide();

const photo_form = document.getElementById('photo_form');
const preview = document.getElementById('preview');
const defaultImage =
    'https://jaeyeop-blog-project.s3.ap-northeast-2.amazonaws.com/images/default_image.jpg';
const before_upload = document.getElementById('before_upload');
const after_upload = document.getElementById('after_upload');

function _add_board_photo() {
    const newPhoto = photo_form.files[0];

    if (newPhoto === undefined) {
        // before
        before_upload.style.display = 'block';
        after_upload.style.display = 'none';

        preview.src = defaultImage;
    } else {
        // after
        before_upload.style.display = 'none';
        after_upload.style.display = 'block';

        preview.src = window.URL.createObjectURL(newPhoto);
        preview.onload = () => window.URL.revokeObjectURL(preview.src);
    }
}

function _delete_board_photo() {
    photo_form.value = '';
    preview.src = defaultImage;
    before_upload.style.display = 'block';
    after_upload.style.display = 'none';
}

function _write_board() {
    const title_form = document.getElementById('title_form');
    const title = title_form.value;
    const content = content_form.value();
    const introduce = document.getElementById('introduce_form').value;
    const newPhoto = photo_form.files[0];

    if (title_form.checkValidity()) {
        const data = new FormData();
        data.append('title', title);
        data.append('content', content);
        data.append('introduce', introduce);
        data.append('newPhoto', newPhoto);

        axios
            .post(`/api/boards`, data)
            .then(() => (window.location.href = '/'))
            .catch((error) => alert(error.response.data.message));
    }
}

function _edit_board(board_id) {
    const title_form = document.getElementById('title_form');
    const title = title_form.value;
    const content = content_form.value();
    const introduce = document.getElementById('introduce_form').value;
    const newPhoto = photo_form.files[0];

    if (title_form.checkValidity()) {
        const data = new FormData();

        data.append('title', title);
        data.append('content', content);
        data.append('introduce', introduce);
        data.append('newPhoto', newPhoto);

        axios
            .put(`/api/boards/${board_id}`, data)
            .then(() => (window.location.href = `/board/${board_id}`))
            .catch((error) => alert(error.response.data.message));
    }
}

function _delete_board(board_id) {
    axios
        .delete(`/boards/${board_id}`)
        .then(() => (window.location.href = '/'))
        .catch((error) => alert(error.response.data.message));
}
