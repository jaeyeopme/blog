let index = {
    init: function () {
        $('#btn-write').on('click', () => {
            if (document.getElementsByClassName('is-valid').length === 1) {
                this.write();
            }
        });

        $('#btn-delete').on('click', () => {
            if (confirm("delete?")) {
                this.deleteById();
            }
        });

        $('#btn-modify').on('click', () => {
            if (document.getElementsByClassName('is-valid').length === 1 && confirm("modify?")) {
                this.modify();
            }
        });

    },


    write: function () {
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

    deleteById: function () {
        let boardId = document.getElementById('id').value;

        $.ajax({
            url: '/boards/' + boardId,
            type: 'delete',
            dataType: 'json',
        }).done(function (response) {
            alert(response.message);
            location.href = '/';
        }).fail(function (error) {
            alert(error.responseJSON.message);
            location.href = '/';
        });
    },

    modify: function () {
        let data = {
            id: document.getElementById('id').value,
            title: document.getElementById('title').value,
            content: document.getElementById('content').value
        };

        $.ajax({
            url: '/boards',
            type: 'put',
            data: JSON.stringify(data),
            dataType: 'json',
            contentType: 'application/json; charset=utf-8'
        }).done(function (response) {
            alert(response.message);
            location.href = '/detail/' + data.id;
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