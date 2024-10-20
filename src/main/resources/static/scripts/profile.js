document.addEventListener('DOMContentLoaded', async function () {
    const apiUrl = '/api/v1/t.assist/students';
    const salaryApiUrl = '/api/v1/t.assist/teachers/get-salary';

    async function fetchStudents() {
        try {
            const response = await fetch(`${apiUrl}/get-all`, {
                method: 'GET',
                headers: {
                    'Content-Type': 'application/json',
                },
            });
            if (response.status === 204) {
                document.querySelector('.students-list tbody').innerHTML = '<tr><td colspan="7">No students found.</td></tr>';
            } else if (response.ok) {
                const students = await response.json();
                renderStudents(students);
            } else if (response.status === 204) {
                document.querySelector('.students-list tbody').innerHTML = '<tr><td colspan="7">No students found</td></tr>';
            } else {
                console.error('Error fetching students:', response.statusText);
                document.querySelector('.students-list tbody').innerHTML = `<tr><td colspan="7">Error fetching data with status: ${response.statusText}.</td></tr>`;
            }
        } catch (error) {
            console.error('Fetch error:', error);
            document.querySelector('.students-list tbody').innerHTML = '<tr><td colspan="7">Error fetching data.</td></tr>';
        }
    }

    const defaultAvatarUrl = 'https://hostenko.com/wpcafe/wp-content/uploads/wpavatar.webp';

    function renderStudents(students) {
        const tableBody = document.querySelector('.students-list tbody');
        tableBody.innerHTML = '';

        students.forEach(async student => {
            const photoUrl = student.imageIds && student.imageIds.length > 0
                ? await fetchPhoto(student.imageIds[0])
                : defaultAvatarUrl;

            const row = document.createElement('tr');
            row.innerHTML = `
            <td>${student.id}</td>
            <td><img src="${photoUrl}" alt="Photo" style="width: 50px; height: 50px; border-radius: 50%; object-fit: cover;"></td>
            <td>${student.name} ${student.surname}</td>
            <td>${student.phone}</td>
            <td>${student.grade} класс</td>
            <td>
                <a href="/profile/info?phone=${student.phone}&id=${student.id}">
                    <button>View Details</button>
                </a>
                <button onclick="deleteStudent('${student.id}')">Delete</button>
            </td>
        `;
            tableBody.appendChild(row);
        });
    }

    async function fetchPhoto(imageId) {
        try {
            const response = await fetch(`/download/${imageId}`);
            if (response.ok) {
                const blob = await response.blob();
                console.log('Photo fetched successfully:', URL.createObjectURL(blob));
                return URL.createObjectURL(blob);
            } else {
                console.error('Error fetching photo:', response.statusText);
                return defaultAvatarUrl; // Используем стандартную аватарку при ошибке
            }
        } catch (error) {
            console.error('Fetch error:', error);
            return defaultAvatarUrl; // Используем стандартную аватарку при ошибке
        }
    }

    window.deleteStudent = async function(id) {
        if (confirm('Are you sure you want to delete this student?')) {
            try {
                const response = await fetch(`${apiUrl}/delete?id=${id}`, {
                    method: 'DELETE',
                    headers: {
                        'Content-Type': 'application/json',
                    },
                });

                if (response.ok) {
                    alert('Student deleted successfully.');
                    await fetchStudents();
                } else {
                    alert('Failed to delete student.');
                }
            } catch (error) {
                console.error('Error deleting student:', error);
            }
        }
    };

    async function fetchSalaryInfo() {
        try {
            const response = await fetch(salaryApiUrl, {
                method: 'GET',
                headers: {
                    'Content-Type': 'application/json',
                },
            });
            if (response.ok) {
                const salaryInfo = await response.json();
                renderSalaryInfo(salaryInfo);
            } else {
                console.error('Error fetching salary info:', response.statusText);
                document.querySelector('.salary-info').innerHTML = `<p>Error fetching salary data: ${response.statusText}</p>`;
            }
        } catch (error) {
            console.error('Fetch error:', error);
            document.querySelector('.salary-info').innerHTML = '<p>Error fetching salary data ${response.statusText}</p>';
        }
    }

    function renderSalaryInfo(info) {
        const monthlySalary = info.monthlySalary;//(info.pricePerHour * info.* info.minutesPerLesson / 60 * info.lessonsPerWeek * 4).toFixed(2);
        const weeklySalary = info.weeklySalary;//(info.pricePerHour * info.minutesPerLesson / 60 * info.lessonsPerWeek).toFixed(2);
        const lessonsPerMonth = info.lessonsPerMonth; //info.lessonsPerWeek * 4;
        const lessonsPerWeek = info.lessonsPerMonth / 4;

        document.getElementById('monthly-salary').textContent = `${monthlySalary} рублей`;
        document.getElementById('weekly-salary').textContent = `${weeklySalary} рублей`;
        document.getElementById('lessons-per-month').textContent = lessonsPerMonth;
        document.getElementById('lessons-per-week').textContent = lessonsPerWeek;
    }

    const currentTeacherApiUrl = '/api/v1/t.assist/teachers/current';
    const lessonsApiUrl = '/api/v1/t.assist/lessons/get-all-by-teacher';
    const lessonHiddenApuUrl = '/api/v1/t.assist/lessons/get-all-hidden-by-teacher';

    async function fetchCurrentTeacher() {
        try {
            const response = await fetch(currentTeacherApiUrl, {
                method: 'GET',
                headers: {
                    'Content-Type': 'application/json',
                },
            });

            if (response.ok) {
                const teacher = await response.json();
                await fetchLessons(teacher.id);
                await fetchHiddenLessons(teacher.id);
            } else {
                console.error('Error fetching current teacher:', response.statusText);
                document.querySelector('.lessons-list').innerHTML = `<p>Error fetching teacher data: ${response.statusText}</p>`;
            }
        } catch (error) {
            console.error('Fetch error:', error);
            document.querySelector('.lessons-list').innerHTML = '<p>Error fetching teacher data.</p>';
        }
    }

    async function hideLesson(lessonId) {
        try {
            const response = await fetch(`/api/v1/t.assist/lessons/hide/${lessonId}`, {
                method: 'PUT',
                headers: {
                    'Content-Type': 'application/json',
                },
            });

            if (response.ok) {
                alert('Lesson successfully hidden!');
                await fetchCurrentTeacher();
            } else {
                alert(`Failed to hide lesson. Error: ${response.statusText}`);
            }
        } catch (error) {
            alert(`Error hiding lesson: ${error.message}`);
        }
    }

    const hiddenLessonsApiUrl = '/api/v1/t.assist/lessons/get-all-hidden-by-teacher';

    async function fetchHiddenLessons(teacherId) {
        try {
            const response = await fetch(`${hiddenLessonsApiUrl}/${teacherId}`, {
                method: 'GET',
                headers: {
                    'Content-Type': 'application/json',
                },
            });

            if (response.ok) {
                const hiddenLessons = await response.json();
                renderHiddenLessons(hiddenLessons);
            } else if (response.status === 404) {
                renderNoHiddenLessonsFound();
            } else {
                console.error('Error fetching hidden lessons:', response.statusText);
                document.querySelector('.hidden-lessons-list').innerHTML = `<p>Error fetching data: ${response.statusText}</p>`;
            }
        } catch (error) {
            console.error('Fetch error:', error);
            document.querySelector('.hidden-lessons-list').innerHTML = '<p>Error fetching data.</p>';
        }
    }

    function renderHiddenLessons(hiddenLessons) {
        const hiddenLessonsSection = document.querySelector('.hidden-lessons-section');
        const hiddenLessonsList = hiddenLessonsSection.querySelector('.hidden-lessons-list');
        hiddenLessonsList.innerHTML = ''; // Clear previous data

        hiddenLessons.forEach(lesson => {
            const lessonCard = document.createElement('div');
            lessonCard.className = 'lesson-card hidden-lesson-card'; // Add class for styling

            let statusColor;
            switch (lesson.lessonStatus) {
                case 'PENDING':
                    statusColor = 'orange';
                    break;
                case 'IN_PROGRESS':
                    statusColor = 'blue';
                    break;
                case 'COMPLETED':
                    statusColor = 'green';
                    break;
                case 'CANCELLED':
                    statusColor = 'red';
                    break;
                default:
                    statusColor = 'gray';
            }

            lessonCard.innerHTML = `
            <h3>${lesson.title}</h3>
            <p><strong>Subject:</strong> ${lesson.subject}</p>
            <p><strong>Start Time:</strong> ${lesson.startTime}</p>
            <p><strong>Finish Time:</strong> ${lesson.endTime}</p>
            <p><strong>Description:</strong> ${lesson.description}</p>
            <p><strong>Status:</strong> <span style="color: ${statusColor};">${lesson.lessonStatus}</span></p>
            <p><strong>Date:</strong> ${lesson.date}</p>
            <button class="unhide-lesson-button" data-lesson-id="${lesson.id}">Unhide Lesson</button>
        `;

            hiddenLessonsList.appendChild(lessonCard);
        });

        document.querySelectorAll('.unhide-lesson-button').forEach(button => {
            button.addEventListener('click', async function () {
                const lessonId = this.getAttribute('data-lesson-id');
                try {
                    console.log("teacher id: !!!{ " + " }");
                    await unHideLesson(lessonId).then(
                        () => fetchCurrentTeacher(),
                    )
                } catch (error) {
                    console.error('Error unhiding lesson:', error);
                }
            });
        });

    }

    async function unHideLesson(lessonId) {
        try {
            const response = await fetch(`/api/v1/t.assist/lessons/unhide/${lessonId}`, {
                method: 'PUT',
                headers: {
                    'Content-Type': 'application/json',
                },
            });

            if (response.ok) {
                alert('Lesson successfully unhidden!');
            } else {
                alert(`Failed to unhide lesson. Error: ${response.statusText}`);
            }
        } catch (error) {
            alert(`Error unhiding lesson: ${error.message}`);
        }
    }


    function renderNoHiddenLessonsFound() {
        const hiddenLessonsSection = document.querySelector('.hidden-lessons-section');
        hiddenLessonsSection.querySelector('.hidden-lessons-list').innerHTML = `
                    <div class="lesson-card">
                        <h3>No Hidden Lessons Found</h3>
                        <p>There are no hidden lessons at the moment.</p>
                    </div>
                `;
    }

    function toggleHiddenLessonsSection() {
        const hiddenLessonsList = document.querySelector('.hidden-lessons-list');
        hiddenLessonsList.classList.toggle('hidden');
    }

    document.getElementById('toggle-hidden-lessons').addEventListener('click', toggleHiddenLessonsSection);

    async function fetchLessons(teacherId) {
        try {
            const response = await fetch(`${lessonsApiUrl}/${teacherId}`, {
                method: 'GET',
                headers: {
                    'Content-Type': 'application/json',
                },
            });

            if (response.ok) {
                const lessons = await response.json();
                await renderLessons(lessons);
            } else if (response.status === 404) {
                renderNoLessonsFound();
            } else {
                console.error('Error fetching lessons:', response.statusText);
                document.querySelector('.lessons-list').innerHTML = `<p>Error fetching data: ${response.statusText}</p>`;
            }
        } catch (error) {
            console.error('Fetch error:', error);
            document.querySelector('.lessons-list').innerHTML = '<p>Error fetching data.</p>';
        }
    }

    async function renderLessons(lessons) {
        const lessonsSection = document.querySelector('.lessons-section');
        lessonsSection.innerHTML = '';

        lessons.forEach(lesson => {
            if (lesson.isHidden) {
                return;
            }

            const lessonCard = document.createElement('div');
            lessonCard.className = 'lesson-card ' + getStatusClass(lesson.lessonStatus);

            let statusColor;
            switch (lesson.lessonStatus) {
                case 'PENDING':
                    statusColor = 'orange';
                    break;
                case 'IN_PROGRESS':
                    statusColor = 'blue';
                    break;
                case 'COMPLETED':
                    statusColor = 'green';
                    break;
                case 'CANCELLED':
                    statusColor = 'red';
                    break;
                default:
                    statusColor = 'gray';
            }

            lessonCard.innerHTML = `
            <h3>${lesson.title}</h3>
            <p><strong>Subject:</strong> ${lesson.subject}</p>
            <p><strong>Start Time:</strong> ${lesson.startTime}</p>
            <p><strong>Finish Time:</strong> ${lesson.endTime}</p>
            <p><strong>Description:</strong> ${lesson.description}</p>
            <p><strong>Status:</strong> <span style="color: ${statusColor};">${lesson.lessonStatus}</span></p>
            <p><strong>Date:</strong> ${lesson.date}</p>
            <label>
                <input type="checkbox" class="hide-lesson-checkbox" data-lesson-id="${lesson.id}" ${lesson.isHidden ? 'checked' : ''}>
                Hide Lesson
            </label>
            <button class="cancel-lesson-button" data-lesson-id="${lesson.id}" ${lesson.lessonStatus === 'CANCELLED' ? 'disabled' : ''}>
                Cancel Lesson
            </button>
        `;

            lessonsSection.appendChild(lessonCard);
        });

        document.querySelectorAll('.cancel-lesson-button').forEach(button => {
            button.addEventListener('click', async function () {
                const lessonId = this.getAttribute('data-lesson-id');
                if (confirm('Are you sure you want to cancel this lesson?')) {
                    await cancelLesson(lessonId);
                }
            });
        });

        document.querySelectorAll('.hide-lesson-checkbox').forEach(checkbox => {
            checkbox.addEventListener('change', async function () {
                const lessonId = this.getAttribute('data-lesson-id');
                const isHidden = this.checked;
                await hideLesson(lessonId, isHidden);

                if (isHidden) {
                    const lessonCard = this.closest('.lesson-card');
                    lessonCard.remove();
                }
            });
        });
    }


    function getStatusClass(status) {
        switch (status) {
            case 'PENDING':
                return 'status-pending';
            case 'IN_PROGRESS':
                return 'status-in-progress';
            case 'COMPLETED':
                return 'status-completed';
            case 'CANCELLED':
                return 'status-cancelled';
            default:
                return '';
        }
    }


    async function cancelLesson(lessonId) {
        try {
            const response = await fetch(`/api/v1/t.assist/lessons/cancel/${lessonId}`, {
                method: 'PATCH',
                headers: {
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify({lessonStatus: 'CANCELLED'}),
            });

            if (response.ok) {
                alert('Lesson successfully cancelled!');
                const studentId = document.getElementById('studentId').textContent;
                await fetchLessons(studentId);
            } else {
                alert(`Failed to cancel lesson. Error: ${response.statusText}`);
            }
        } catch (error) {
            alert(`Error cancelling lesson: ${error.message}`);
        }
    }

    function renderNoLessonsFound() {
        const lessonsContainer = document.querySelector('.lessons-list');
        lessonsContainer.innerHTML = ''; // Очищаем предыдущие данные

        const noLessonsCard = document.createElement('div');
        noLessonsCard.className = 'lesson-card';
        noLessonsCard.innerHTML = `
            <h3>No Lessons Found</h3>
            <p>It seems that there are no scheduled lessons at the moment.</p>
            <p>Once new lessons are added, they will appear here.</p>
        `;

        lessonsContainer.appendChild(noLessonsCard);
    }

    await fetchStudents();
    await fetchSalaryInfo();
    await fetchCurrentTeacher();
});

