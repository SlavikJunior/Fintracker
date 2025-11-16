let contextPath = document.body.dataset.contextPath || '';

document.addEventListener('DOMContentLoaded', () => {
    updateCategories();

    document.querySelectorAll('.manage-tags-btn').forEach(btn => {
        btn.addEventListener('click', () => {
            const transactionId = btn.dataset.transactionId;
            document.getElementById('modalTransactionId').value = transactionId;

            document.querySelectorAll('input[name="tagIds"]').forEach(cb => cb.checked = false);

            fetch(contextPath + '/api/transaction-tags?transactionId=' + transactionId)
                .then(r => r.json())
                .then(tagIds => {
                    tagIds.forEach(id => {
                        const cb = document.querySelector(`input[name="tagIds"][value="${id}"]`);
                        if (cb) cb.checked = true;
                    });
                })
                .catch(() => {});

            document.getElementById('tagModal').style.display = 'block';
        });
    });

    const typeSelect = document.getElementById('type');
    if (typeSelect) typeSelect.addEventListener('change', updateCategories);
});

function updateCategories() {
    const type = document.getElementById('type')?.value || 'EXPENSE';
    document.getElementById('income-categories').style.display = type === 'INCOME' ? 'block' : 'none';
    document.getElementById('expense-categories').style.display = type === 'EXPENSE' ? 'block' : 'none';
}

document.querySelector('.modal .close')?.addEventListener('click', () => {
    document.getElementById('tagModal').style.display = 'none';
});
document.getElementById('closeModal')?.addEventListener('click', () => {
    document.getElementById('tagModal').style.display = 'none';
});
window.addEventListener('click', e => {
    if (e.target.classList.contains('modal')) {
        e.target.style.display = 'none';
    }
});