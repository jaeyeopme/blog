let index = {
    init: function () {
        $('#btn-save').on('click', () => {
            this.save();
        });
    },
    save: function () {
        let data = {
            username: $('#username').val(),
            password: $('#password').val(),
            email: $('#email').val()
        };

        $.ajax({
            url: '/blog/api/users',
            type: 'POST',
            data: JSON.stringify(data),
            dataType: 'json',
            contentType: 'application/json; charset=utf-8'
        }).done(function (response) {
            console.log(response)
        }).fail(function (error) {
            console.log(error)
        });
    }
}

index.init();