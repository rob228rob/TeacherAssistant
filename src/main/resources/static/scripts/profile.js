document.addEventListener('DOMContentLoaded', function() {
    // URL для запроса к REST API
    const apiUrl = '/api/v1/t.assist/students/get-all';

    // Функция для получения данных студентов
    async function fetchStudents() {
        try {
            const response = await fetch(apiUrl, {
                method: 'GET',
                headers: {
                    'Content-Type': 'application/json',
                },
            });

            if (response.ok) {
                const students = await response.json();
                renderStudents(students);
            } else if (response.status === 204) {
                // Обработка случая, когда данные отсутствуют
                document.querySelector('.students-list').innerHTML = '<p>No students found.</p>';
            } else {
                console.error('Error fetching students:', response.statusText);
            }
        } catch (error) {
            console.error('Fetch error:', error);
        }
    }

    // Функция для отображения данных студентов на странице
    function renderStudents(students) {
        const tableBody = document.querySelector('.students-list tbody');
        tableBody.innerHTML = ''; // Очистить таблицу перед заполнением новыми данными

        students.forEach(student => {
            const row = document.createElement('tr');
            row.innerHTML = `
                <td>${student.name}</td>
                <td>${student.surname}</td>
                <td>${student.phone}</td>
                <td>${student.email}</td>
                <td>${student.grade}</td>
            `;
            tableBody.appendChild(row);
        });
    }

    // Вызов функции для получения и отображения данных при загрузке страницы
    fetchStudents();
});
