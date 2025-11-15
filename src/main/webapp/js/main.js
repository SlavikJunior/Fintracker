// Функция для обновления категорий в форме добавления транзакции
function updateCategories() {
    const typeSelect = document.getElementById('type');
    const categorySelect = document.getElementById('category');
    const incomeGroup = document.getElementById('income-categories');
    const expenseGroup = document.getElementById('expense-categories');

    // Сбрасываем выбор категории
    categorySelect.selectedIndex = 0;

    if (typeSelect.value === 'INCOME') {
        incomeGroup.style.display = 'block';
        expenseGroup.style.display = 'none';
    } else {
        incomeGroup.style.display = 'none';
        expenseGroup.style.display = 'block';
    }
}

// Модальное окно для тегов
const modal = document.getElementById('tagModal');
const manageTagBtns = document.querySelectorAll('.manage-tags-btn');
const closeBtn = document.querySelector('.close');
const closeModalBtn = document.getElementById('closeModal');

// Открытие модального окна
manageTagBtns.forEach(btn => {
    btn.addEventListener('click', function() {
        const transactionId = this.getAttribute('data-transaction-id');
        const transactionType = this.getAttribute('data-transaction-type');

        document.getElementById('modalTransactionId').value = transactionId;
        document.getElementById('modalTransactionType').value = transactionType;

        // Загружаем текущие теги транзакции и отмечаем их
        loadCurrentTags(transactionId, transactionType);

        modal.style.display = 'block';
    });
});

// Закрытие модального окна
closeBtn.addEventListener('click', () => modal.style.display = 'none');
closeModalBtn.addEventListener('click', () => modal.style.display = 'none');
window.addEventListener('click', (e) => {
    if (e.target === modal) modal.style.display = 'none';
});

// Загрузка текущих тегов транзакции
function loadCurrentTags(transactionId, transactionType) {
    // Сбрасываем все чекбоксы
    document.querySelectorAll('input[name="tagIds"]').forEach(cb => cb.checked = false);

    // Здесь можно добавить AJAX запрос для загрузки текущих тегов
    // Пока просто сбрасываем - пользователь выберет заново
    console.log('Loading tags for transaction:', transactionId, transactionType);
}

// Инициализация при загрузке
document.addEventListener('DOMContentLoaded', function() {
    updateCategories();

    // Добавляем обработчики для всех кнопок управления тегами
    document.querySelectorAll('.manage-tags-btn').forEach(btn => {
        btn.addEventListener('click', function() {
            const transactionId = this.getAttribute('data-transaction-id');
            const transactionType = this.getAttribute('data-transaction-type');

            document.getElementById('modalTransactionId').value = transactionId;
            document.getElementById('modalTransactionType').value = transactionType;

            loadCurrentTags(transactionId, transactionType);
            modal.style.display = 'block';
        });
    });
});