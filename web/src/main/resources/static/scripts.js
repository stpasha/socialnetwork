function toggleForm() {
    const form = document.getElementById('postForm');
    form.style.display = form.style.display === 'none' ? 'block' : 'none';
}


function addTextareaListeners() {
    document.querySelectorAll('.editable-textarea').forEach(textarea => {
        textarea.addEventListener('click', () => {
            textarea.removeAttribute('readonly');
        });

        textarea.addEventListener('keydown', event => {
            if (event.ctrlKey && event.key === 'Enter') {
                event.preventDefault();
                const form = textarea.closest('form');
                if (form) {
                    form.submit();
                }
            }
        });
    });
}

function addLikeButtonListener () {
    const likeButton = document.getElementById('like-button');
    const likesCountElement = document.getElementById('likes_count');
    const postIdElement = document.getElementById('like-post-id');

    likeButton.addEventListener('click', function (event) {
        event.preventDefault();

        const postId = postIdElement.value;

        fetch(`/posts/${postId}/like`, {
                    method: 'POST',
                    headers: {
                        'Content-Type': 'application/json',
                        'Accept': 'application/json'
                    }
                })
                .then(response => {
                    if (!response.ok) {
                        throw new Error('Failed to add like');
                    }
                    return response.json();
                })
                .then(data => {
                    likesCountElement.textContent = data;  // Обновляем количество лайков
                    likeButton.classList.add('btn-success');  // Визуальная обратная связь
                })
                .catch(error => {
                    console.error('Error:', error);
                    alert('Error adding like');
                });
    });
}

document.addEventListener('DOMContentLoaded', addTextareaListeners);
document.addEventListener('DOMContentLoaded', addLikeButtonListener);
