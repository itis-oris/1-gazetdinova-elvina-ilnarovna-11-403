// ----------------------------
// ВАЛИДАЦИЯ ФОРМЫ
// ----------------------------
function validateForm(formId) {
    const form = document.getElementById(formId);
    if (!form) return false;

    const inputs = form.querySelectorAll('input[required], textarea[required], select[required]');
    let isValid = true;

    inputs.forEach(input => {
        if (!input.value.trim()) {
            input.classList.add('error');
            isValid = false;
        } else {
            input.classList.remove('error');
        }
    });

    return isValid;
}

// ----------------------------
// ФИЛЬТР ТУРОВ (не исп)
// ----------------------------
function filterTours() {
    const searchInput = document.getElementById('tour-search');
    const tours = document.querySelectorAll('.tour-card');

    if (!searchInput) return;

    const searchTerm = searchInput.value.toLowerCase();
    tours.forEach(tour => {
        const name = tour.querySelector('.tour-name')?.textContent.toLowerCase() || '';
        tour.style.display = name.includes(searchTerm) ? 'block' : 'none';
    });
}

// ----------------------------
// ИНИЦИАЛИЗАЦИЯ
// ----------------------------
document.addEventListener('DOMContentLoaded', () => {
    const searchInput = document.getElementById('tour-search');
    if (searchInput) {
        searchInput.addEventListener('input', filterTours);
    }
});

// ----------------------------
// LOGOUT
// ----------------------------
function logout(event) {
    event?.preventDefault();
    if (confirm('Выйти из аккаунта?')) {
        window.location.href = '/travelplanner/logout';
    }
}
