function commentWrite() {
    const boardId = document.getElementById('board-id').value;

    axios({
        method: 'post',
        url: `/boards/${boardId}/comments`,
        headers: {'content-type': 'application/json'},
        data: JSON.stringify({
            content: document.getElementById('comment-content').value
        }),
    }).then(response => window.location.href = response.headers.location)
        .catch(error => {
            alert(error.response.data.message);
            window.location.href = '/'
        });
}

function commentModify(element, commentId) {
    let status = element.innerText;

    switch (status) {
        case '수정':
            document.getElementById(`comment-${commentId}`).removeAttribute('readOnly');
            document.getElementById(`comment-${commentId}`).setAttribute('outline', 'revert');
            document.getElementById(`comment-${commentId}`).style.border = '0.5px solid lightgray';
            status = '완료';
            break;
        case '완료':
            axios({
                method: 'put',
                url: `/comments/${commentId}`,
                headers: {'content-type': 'application/json'},
                data: JSON.stringify({
                    content: document.getElementById(`comment-${commentId}`).value
                }),
            }).then(window.location.reload())
                .catch(error => {
                    alert(error);
                    window.location.href = '/'
                });
            break;
    }
}

function commentDelete(commentId) {
    axios({
        method: 'delete',
        url: `/comments/${commentId}`,
        headers: {'content-type': 'application/json'},
    }).then(window.location.reload())
        .catch(error => {
            alert(error);
            window.location.href = '/'
        });
}