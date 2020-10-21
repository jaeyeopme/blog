let index = {
    init: function () {
        $('#btn-board-write').on('click', () => {
            if (document.getElementById('title').checkValidity()) {
                this.boardWrite();
            } else {
                document.getElementById('title').classList.add('is-invalid');
            }
        });

        $('#btn-board-delete').on('click', () => {
            if (confirm("delete?")) {
                this.boardDelete();
            }
        });

        $('#btn-board-modify').on('click', () => {
            if (document.getElementsByClassName('is-valid').length === 1 && confirm("modify?")) {
                this.boardModify();
            }
        });

        $('#btn-reply-write').on('click', () => {
            this.replyWrite();
        });

    },

    boardWrite: function () {
        let data = new FormData();


        let title = document.getElementById('title').value;
        let content = document.getElementById('content').value;
        let description = document.getElementById('description').value;
        let thumbnail = document.getElementById('thumbnail').files[0];

        data.append('file', thumbnail)
        data.append('title', title);
        data.append('content', content);
        data.append('description', description);

        $.ajax({
            url: '/boards',
            type: 'post',
            data: data,
            contentType: false,
            processData: false,
            dataType: 'json',
            // contentType: 'multipart/form-data'
        }).done(function (response) {
            location.href = response.message;
        }).fail(function (error) {
            alert(error.responseJSON.message);
            location.href = '/';
        });
    },

    boardDelete: function () {
        let boardId = document.getElementById('boardId').value;

        $.ajax({
            url: '/boards/' + boardId,
            type: 'delete',
            dataType: 'json',
        }).done(function () {
            location.href = '/';
        }).fail(function (error) {
            alert(error.responseJSON.message);
            location.href = '/';
        });
    },

    boardModify: function () {
        let data = new FormData();

        let id = document.getElementById('id').value;
        let title = document.getElementById('title').value;
        let content = document.getElementById('content').value;
        let description = document.getElementById('description').value;
        let thumbnail = document.getElementById('thumbnail').files[0];

        data.append('file', thumbnail)
        data.append('id', id);
        data.append('title', title);
        data.append('content', content);
        data.append('description', description);


        $.ajax({
            url: '/boards',
            type: 'put',
            data: data,
            contentType: false,
            processData: false,
            dataType: 'json',
            // contentType: 'multipart/form-data'
        }).done(function (response) {
            location.href = response.message;
        }).fail(function (error) {
            alert(error.responseJSON.message);
            location.href = '/';
        });
    },

    replyWrite: function () {
        let boardId = document.getElementById('boardId').value;
        let data = {
            content: document.getElementById('reply-write-form').value
        };

        $.ajax({
            url: '/replies/' + boardId,
            type: 'post',
            data: JSON.stringify(data),
            dataType: 'json',
            contentType: 'application/json; charset=utf-8'
        }).done(function () {
            location.href = '/detail/' + boardId;
        }).fail(function (error) {
            alert(error.responseJSON.message);
            location.href = '/';
        });
    },

}

function writeFormValidate(target) {
    if (target.checkValidity()) {
        target.classList.remove('is-invalid');
        target.classList.add('is-valid');
    } else {
        target.classList.remove('is-valid');
        target.classList.add('is-invalid');
    }
}

index.init();