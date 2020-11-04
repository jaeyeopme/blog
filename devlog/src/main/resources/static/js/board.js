'use strict';

const modalButton = document.getElementById('btn-modal')

switch (document.title) {
    case 'bucket':
        modalButton.setAttribute('href', '/write');
        break;
    case 'write':
        const writeContent = new SimpleMDE({
            element: document.getElementById("board-content"),
            placeholder: "글을 입력해주세요.",
            status: false,
            toolbar: ["bold", "italic", "heading", "|",
                "code", "quote", "unordered-list", "ordered-list", "|",
                "link", "image", "table", "|",
                "guide", "side-by-side", "fullscreen"
            ],
            spellChecker: false
        });
        writeContent.toggleSideBySide();

        modalButton.setAttribute('data-target', '#write-modal');
        modalButton.setAttribute('data-toggle', 'modal');
        modalButton.removeAttribute('href')

        document.getElementById('btn-board-write').addEventListener('click', () => {
            const thumbnail = document.getElementById('btn-thumbnail');

            if (thumbnail.size < 52428800) {
                const data = new FormData();
                data.append("content", writeContent.value())
                data.append('title', document.getElementById('title').value)
                data.append('thumbnail', thumbnail.files[0])
                data.append('introduction', document.getElementById('introduction').value)

                $.ajax({
                    url: '/boards',
                    type: 'POST',
                    processData: false,
                    contentType: false,
                    data: data,
                    dataType: 'json'
                }).done((data, status, xhr) => {
                    window.location.href = xhr.getResponseHeader('Location');
                }).fail(error => {
                    alert(error.message)
                    window.location.href = '/';
                })
            }
        });
        break;
    case 'modify':
        const modifyContent = new SimpleMDE({
            element: document.getElementById("board-content"),
            placeholder: "글을 입력해주세요.",
            status: false,
            toolbar: ["bold", "italic", "heading", "|",
                "code", "quote", "unordered-list", "ordered-list", "|",
                "link", "image", "table", "|",
                "guide", "side-by-side", "fullscreen"
            ],
            spellChecker: false
        });
        modifyContent.toggleSideBySide();

        const modifyId = document.getElementById('board-id').value;
        modalButton.setAttribute('data-target', '#modify-modal');
        modalButton.setAttribute('data-toggle', 'modal');
        modalButton.removeAttribute('href')
        modalButton.innerText = '글 수정';

        document.getElementById('btn-board-modify').addEventListener('click', () => {
            const thumbnail = document.getElementById('btn-thumbnail');

            if (thumbnail.size < 52428800) {
                const data = new FormData();
                data.append('content', modifyContent.value())
                data.append('title', document.getElementById('title').value)
                data.append('thumbnail', thumbnail.files[0])
                data.append('introduction', document.getElementById('introduction').value)

                $.ajax({
                    url: '/boards/' + modifyId,
                    type: 'PUT',
                    processData: false,
                    contentType: false,
                    data: data,
                    dataType: 'json'
                }).done((data, status, xhr) => {
                    window.location.href = xhr.getResponseHeader('Location');
                }).fail(error => {
                    alert(error.message)
                    window.location.href = '/';
                })
            }
        })
        break;
    case 'detail':
        const detailContent = new SimpleMDE({
            element: document.getElementById('board-content'),
            status : false,
            toolbar: false,
        });
        detailContent.togglePreview();

        const deleteId = document.getElementById('board-id').value;

        document.getElementById('btn-board-delete').addEventListener('click', () => {
            $.ajax({
                url: '/boards/' + deleteId,
                type: 'DELETE',
                dataType: 'json'
            }).done(location.href = '/')
                .fail(error => {
                    alert(error.message)
                    window.location.href = '/';
                })
        });
        break;
}

function introductionValidate(element) {
    element.value.length > 99 ? element.classList.add('is-invalid') : element.classList.remove('is-invalid');
}

function preview(element) {
    const thumbnail = document.getElementById('thumbnail');
    thumbnail.src = window.URL.createObjectURL(element.files[0]);
    thumbnail.onload = () => window.URL.revokeObjectURL(thumbnail.src)
}