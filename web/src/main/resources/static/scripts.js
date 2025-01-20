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

document.addEventListener('DOMContentLoaded', addTextareaListeners);
