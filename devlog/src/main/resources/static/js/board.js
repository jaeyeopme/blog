let index = {
    init: function () {
        $('#btn-write').on('click', () => {
            this.write();
        });
    },

    write: function () {
        let data = {
            title: document.getElementById('title').value,
            content: document.getElementById('content').value,
        };

        $.ajax({
            url: 'boards',
            type: 'POST',
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
    }

}

index.init();