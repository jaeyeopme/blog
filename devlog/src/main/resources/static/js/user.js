let index = {
    init: function () {
        $('#btn-save').on('click', () => {
            this.join();
        });
    },
    // join: function () {
    //     let data = {
    //         username: $('#username').val(),
    //         password: $('#password').val(),
    //         email: $('#email').val()
    //     };
    //
    //     $.ajax({
    //         url: '/blog/api/users',
    //         type: 'POST',
    //         data: JSON.stringify(data),
    //         dataType: 'json',
    //         contentType: 'application/json; charset=utf-8'
    //     }).done(function (response) {
    //         alert(response.message);
    //     }).fail(function (error) {
    //         // error body message (Object -> Json)
    //         alert(error.responseJSON.message);
    //     });
    // }
}

function onblurValidate(target) {

    switch (target.id) {
        case 'email':
            $.ajax({
                url: '/blog/api/users/' + target.value,
                type: 'GET',
            }).done(function () {
                alert(target.value + " is available");
            }).fail(function () {
                alert("Email " + target.value + " is not available.")
            });
            break;
        case 'username':

    }



}

index.init();