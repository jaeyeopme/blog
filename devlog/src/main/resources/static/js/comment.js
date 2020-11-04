document.getElementById('btn-comment-write').addEventListener('click', () => {
    const boardId = document.getElementById('board-id').value;

    $.ajax({
        url: '/boards/' + boardId + '/comments',
        type: 'POST',
        contentType: 'application/json; charset=UTF-8',
        data: JSON.stringify({
            content: document.getElementById('comment-content').value
        }),
        dataType: 'json'
    }).done((data, status, xhr) => {
        window.location.href = xhr.getResponseHeader('Location');
    }).fail(error => {
        alert(error.message)
    });
})