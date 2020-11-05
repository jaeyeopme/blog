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

function commentModify(element, commentId) {
    if (element.innerText === '수정') {
        document.getElementById('comment-' + commentId).removeAttribute('readOnly');
        document.getElementById('comment-' + commentId).setAttribute('outline', 'revert');
        document.getElementById('comment-' + commentId).style.border = '0.5px solid lightgray';
        element.innerText = '완료';
    } else if (element.innerText === '완료') {
        $.ajax({
            url: '/comments/' + commentId,
            type: 'PUT',
            contentType: 'application/json; charset=UTF-8',
            data: JSON.stringify({
                content: document.getElementById('comment-' + commentId).value
            }),
            dataType: 'json'
        }).done(window.location.reload())
            .fail(error => {
                alert(error.message);
                window.location.href = '/';
            });
    }
}

function commentDelete(commentId) {
    $.ajax({
        url: '/comments/' + commentId,
        type: 'DELETE',
    }).done(window.location.reload())
        .fail(error => {
            alert(error.message);
            window.location.href = '/';
        });
}