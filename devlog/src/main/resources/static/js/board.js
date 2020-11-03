'use strict';

if (document.title === 'write') {
    document.getElementById('btn-write-modal').removeAttribute('href');
    document.getElementById('btn-write-modal').setAttribute('data-target', '#write-modal');
    document.getElementById('btn-write-modal').setAttribute('data-toggle', 'modal');
}

const content = new SimpleMDE({
    element: document.getElementById("content"),
    placeholder: "글을 입력해주세요.",
    status : false,
    toolbar: ["bold", "italic", "heading", "|",
        "code", "quote", "unordered-list", "ordered-list", "|",
        "link", "image", "table", "|",
        "guide", "side-by-side", "fullscreen"
    ],
    spellChecker: false
});

content.toggleSideBySide()

document.getElementById('introduction').addEventListener('keyup', ()=>{
    if (document.getElementById('introduction').value.length > 99) {
        document.getElementById('introduction').classList.add('is-invalid')
    } else {
        document.getElementById('introduction').classList.remove('is-invalid')
    }
});


// var loadFile = function(event) {
//     var output = document.getElementById('output');
//     output.src = URL.createObjectURL(event.target.files[0]);
//     output.onload = function() {
//         URL.revokeObjectURL(output.src) // free memory
//     }
// };
// let index = {
//
//     init: function () {
//         $('#btn-board-write').on('click', () => {
//             if (document.getElementById('title').checkValidity()) {
//                 if (document.getElementById('thumbnail').files[0] > 52428800) {
//                     alert('Max Size (50MB)')
//                 } else {
//                     this.boardWrite();
//                 }
//             } else {
//                 document.getElementById('title').classList.add('is-invalid');
//             }
//         });
//
//         $('#btn-board-modify').on('click', () => {
//             if (document.getElementsByClassName('is-valid').length === 1 && confirm("modify?")) {
//                 if (document.getElementById('thumbnail').files[0] > 52428800) {
//                     alert('Max Size (50MB)')
//                 } else {
//                     this.boardModify();
//                 }
//             }
//         });
//
//         $('#btn-board-delete').on('click', () => {
//             if (confirm("delete?")) {
//                 this.boardDelete();
//             }
//         });
//
//         $('#btn-reply-write').on('click', () => {
//             this.replyWrite();
//         });
//
//     },
//
//     boardWrite: function () {
//         let data = new FormData();
//
//         let title = document.getElementById('title').value;
//         let content = document.getElementById('content').value;
//         let description = document.getElementById('description').value;
//         let thumbnail = document.getElementById('thumbnail').files[0];
//
//         data.append('file', thumbnail)
//         data.append('title', title);
//         data.append('content', content);
//         data.append('description', description);
//
//         $.ajax({
//             url: '/boards',
//             type: 'post',
//             data: data,
//             contentType: false,
//             processData: false,
//             dataType: 'json',
//             // contentType: 'multipart/form-data'
//         }).done(function (response) {
//             location.href = response.message;
//         }).fail(function (error) {
//             alert(error.responseJSON.message);
//             location.href = '/';
//         });
//     },
//
//     boardDelete: function () {
//         let id = document.getElementById('boardId').value;
//
//         $.ajax({
//             url: '/boards/' + id,
//             type: 'delete',
//             dataType: 'json',
//         }).done(function () {
//             location.href = '/';
//         }).fail(function (error) {
//             alert(error.responseJSON.message);
//             location.href = '/';
//         });
//     },
//
//     boardModify: function () {
//         let data = new FormData();
//
//         let id = document.getElementById('boardId').value;
//         let title = document.getElementById('title').value;
//         let content = document.getElementById('content').value;
//         let description = document.getElementById('description').value;
//         let thumbnail = document.getElementById('thumbnail').files[0];
//
//         data.append('file', thumbnail)
//         data.append('id', id);
//         data.append('title', title);
//         data.append('content', content);
//         data.append('description', description);
//
//
//         $.ajax({
//             url: '/boards',
//             type: 'put',
//             data: data,
//             contentType: false,
//             processData: false,
//             dataType: 'json',
//             // contentType: 'multipart/form-data'
//         }).done(function (response) {
//             location.href = response.message;
//         }).fail(function (error) {
//             alert(error.responseJSON.message);
//             location.href = '/';
//         });
//     },
//
//     replyWrite: function () {
//         let boardId = document.getElementById('boardId').value;
//
//         let data = {
//             content: document.getElementById('reply-write-content').value
//         };
//
//         $.ajax({
//             url: '/replies/' + boardId,
//             type: 'post',
//             data: JSON.stringify(data),
//             dataType: 'json',
//             contentType: 'application/json; charset=utf-8'
//         }).done(function () {
//             location.href = '/detail/' + boardId;
//         }).fail(function (error) {
//             alert(error.responseJSON.message);
//             location.href = '/';
//         });
//     },
//
// }
//
// function replyModifyForm(replyId, replyModifyButton) {
//     let form = document.getElementById('reply-modify-content-' + replyId);
//     form.readOnly = false;
//     replyModifyButton.style.display = 'none'
//
//     document.getElementById('btn-reply-modify').style.display = 'revert'
//     document.getElementById('btn-reply-cancel').style.display = 'revert'
// }
//
// function replyModify(replyId) {
//     if (confirm('modify?')) {
//         let data = {
//             id: replyId,
//             content: document.getElementById('reply-modify-content-' + replyId).value
//         };
//
//         $.ajax({
//             url: '/replies',
//             type: 'put',
//             data: JSON.stringify(data),
//             dataType: 'json',
//             contentType: 'application/json; charset=utf-8'
//         }).done(function () {
//             location.reload();
//         }).fail(function (error) {
//             alert(error.responseJSON.message);
//             location.href = '/';
//         });
//     }
// }
//
// function replyDelete(replyId) {
//     if (confirm('delete?')) {
//         let id = replyId;
//
//         $.ajax({
//             url: '/replies/' + id,
//             type: 'delete',
//             dataType: 'json',
//         }).done(function () {
//             location.reload();
//         }).fail(function (error) {
//             alert(error.responseJSON.message);
//             location.href = '/';
//         });
//
//     }
// }
//
// function writeFormValidate(target) {
//     if (target.checkValidity()) {
//         target.classList.remove('is-invalid');
//         target.classList.add('is-valid');
//     } else {
//         target.classList.remove('is-valid');
//         target.classList.add('is-invalid');
//     }
// }
//
// index.init();