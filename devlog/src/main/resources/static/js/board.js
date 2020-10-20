let index = {
    init: function () {
        $('#btn-board-write').on('click', () => {
            if (document.getElementsByClassName('is-valid').length === 1) {
                this.boardWrite();
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
        let data = {
            title: document.getElementById('title').value,
            content: document.getElementById('content').value
        };

        $.ajax({
            url: '/boards',
            type: 'post',
            data: JSON.stringify(data),
            dataType: 'json',
            contentType: 'application/json; charset=utf-8'
        }).done(function (response) {
            alert(response.message);
            location.href = '/';
        }).fail(function (error) {
            // error body message (Object -> Json)
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
        let data = {
            id: document.getElementById('boardId').value,
            title: document.getElementById('title').value,
            content: document.getElementById('content').value
        };

        $.ajax({
            url: '/boards',
            type: 'put',
            data: JSON.stringify(data),
            dataType: 'json',
            contentType: 'application/json; charset=utf-8'
        }).done(function () {
            location.href = '/detail/' + data.id;
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