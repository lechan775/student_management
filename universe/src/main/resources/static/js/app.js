/**
 * 学生信息管理系统 — 宇宙版 前端 SPA
 * Vue 3 CDN + ECharts CDN
 */

const { createApp, ref, reactive, computed, onMounted, nextTick, watch } = Vue;

const app = createApp({
    setup() {
        // ========== 状态 ==========
        const view = ref('login');                 // login | register | forgot
        const loggedIn = ref(false);
        const token = ref('');
        const user = reactive({ username: '', role: '' });
        const message = ref({ text: '', type: '' });

        // 登录/注册表单
        const loginForm  = reactive({ username: '', password: '' });
        const regForm    = reactive({ username: '', password: '', password2: '', personId: '', phoneNumber: '' });
        const forgotForm = reactive({ username: '', personId: '', phone: '', newPassword: '', newPassword2: '' });

        // 导航
        const activeNav = ref('dashboard');

        // 仪表盘
        const dashboardStats = ref(null);

        // 学生管理
        const students = ref([]);
        const searchKeyword = ref('');
        const filterDept = ref('');
        const currentPage = ref(1);
        const pageSize = ref(10);
        const totalStudents = ref(0);

        // 对话框
        const showModal = ref(false);
        const modalMode = ref('add');  // add | edit
        const editId = ref(null);
        const studentForm = reactive({
            studentId: '', name: '', age: 18, sex: '男',
            department: '', className: '', email: '', phone: ''
        });

        // 日志
        const logs = ref([]);

        // ========== 常量 ==========
        const API = '';

        // ========== Toast ==========
        function toast(text, type = 'success') {
            message.value = { text, type };
            setTimeout(() => message.value.text = '', 2500);
        }

        // ========== HTTP 封装 ==========
        function headers() {
            const h = { 'Content-Type': 'application/json' };
            if (token.value) h['Authorization'] = 'Bearer ' + token.value;
            return h;
        }

        async function apiGet(url) {
            const res = await fetch(API + url, { headers: headers() });
            if (res.status === 403) { logout(); throw new Error('请重新登录'); }
            return res.json();
        }

        async function apiPost(url, body) {
            const res = await fetch(API + url, {
                method: 'POST', headers: headers(), body: JSON.stringify(body)
            });
            if (res.status === 403) { logout(); throw new Error('请重新登录'); }
            return res.json();
        }

        async function apiPut(url, body) {
            const res = await fetch(API + url, {
                method: 'PUT', headers: headers(), body: JSON.stringify(body)
            });
            if (res.status === 403) { logout(); throw new Error('请重新登录'); }
            return res.json();
        }

        async function apiDelete(url) {
            const res = await fetch(API + url, { method: 'DELETE', headers: headers() });
            if (res.status === 403) { logout(); throw new Error('请重新登录'); }
            return res.json();
        }

        // ========== 认证 ==========
        async function doLogin() {
            try {
                const data = await apiPost('/api/auth/login', loginForm);
                if (data.code === 200) {
                    token.value = data.data.token;
                    localStorage.setItem('universe_token', token.value);
                    await loadUserInfo();
                    loggedIn.value = true;
                    toast('登录成功');
                    await loadDashboard();
                } else {
                    toast(data.message, 'error');
                }
            } catch (e) { toast(e.message, 'error'); }
        }

        async function doRegister() {
            if (regForm.password !== regForm.password2) {
                toast('两次密码不一致', 'error'); return;
            }
            try {
                const data = await apiPost('/api/auth/register', regForm);
                if (data.code === 200) {
                    toast('注册成功，请登录');
                    view.value = 'login';
                    Object.assign(loginForm, { username: regForm.username, password: '' });
                } else {
                    toast(data.message, 'error');
                }
            } catch (e) { toast(e.message, 'error'); }
        }

        async function doForgotPassword() {
            if (forgotForm.newPassword !== forgotForm.newPassword2) {
                toast('两次密码不一致', 'error'); return;
            }
            try {
                const data = await apiPost('/api/auth/reset-password', forgotForm);
                if (data.code === 200) {
                    toast('密码重置成功，请登录');
                    view.value = 'login';
                } else {
                    toast(data.message, 'error');
                }
            } catch (e) { toast(e.message, 'error'); }
        }

        async function loadUserInfo() {
            try {
                const data = await apiGet('/api/auth/me');
                if (data.code === 200) {
                    Object.assign(user, data.data);
                }
            } catch (e) { /* ignore */ }
        }

        function logout() {
            token.value = '';
            loggedIn.value = false;
            localStorage.removeItem('universe_token');
            view.value = 'login';
            activeNav.value = 'dashboard';
            Object.assign(loginForm, { username: '', password: '' });
        }

        // ========== 自动登录检查 ==========
        onMounted(() => {
            const saved = localStorage.getItem('universe_token');
            if (saved) {
                token.value = saved;
                loadUserInfo().then(() => {
                    loggedIn.value = true;
                    loadDashboard();
                }).catch(() => {
                    logout();
                });
            }
        });

        // ========== 仪表盘 ==========
        async function loadDashboard() {
            try {
                const data = await apiGet('/api/dashboard');
                if (data.code === 200) {
                    dashboardStats.value = data.data;
                    await nextTick();
                    renderCharts(data.data);
                }
            } catch (e) { /* ignore */ }
        }

        function renderCharts(stats) {
            // 院系分布饼图
            const pieDom = document.getElementById('chart-dept');
            if (pieDom) {
                const chart = echarts.init(pieDom);
                chart.setOption({
                    title: { text: '院系分布', left: 'center', textStyle: { fontSize: 14 } },
                    tooltip: { trigger: 'item' },
                    legend: { bottom: 0, textStyle: { fontSize: 11 } },
                    series: [{
                        type: 'pie', radius: ['40%', '70%'],
                        data: Object.entries(stats.deptDistribution || {}).map(([k, v]) => ({ name: k, value: v })),
                        emphasis: { itemStyle: { shadowBlur: 10, shadowOffsetX: 0, shadowColor: 'rgba(0,0,0,0.3)' } }
                    }]
                });
                window.addEventListener('resize', () => chart.resize());
            }

            // 性别分布饼图
            const sexDom = document.getElementById('chart-sex');
            if (sexDom) {
                const chart = echarts.init(sexDom);
                chart.setOption({
                    title: { text: '性别分布', left: 'center', textStyle: { fontSize: 14 } },
                    tooltip: { trigger: 'item' },
                    series: [{
                        type: 'pie', radius: '65%',
                        data: Object.entries(stats.sexDistribution || {}).map(([k, v]) => ({ name: k, value: v })),
                        label: { formatter: '{b}: {c}人\n({d}%)' }
                    }]
                });
                window.addEventListener('resize', () => chart.resize());
            }
        }

        // ========== 学生管理 ==========
        async function loadStudents() {
            try {
                let data;
                if (searchKeyword.value) {
                    data = await apiGet('/api/students/search?name=' + encodeURIComponent(searchKeyword.value));
                } else if (filterDept.value) {
                    data = await apiGet('/api/students/filter?dept=' + encodeURIComponent(filterDept.value));
                } else {
                    data = await apiGet('/api/students');
                }
                if (data.code === 200) {
                    students.value = data.data;
                    totalStudents.value = data.data.length;
                }
            } catch (e) { /* ignore */ }
        }

        function openAddModal() {
            modalMode.value = 'add';
            editId.value = null;
            Object.assign(studentForm, {
                studentId: '', name: '', age: 18, sex: '男',
                department: '', className: '', email: '', phone: ''
            });
            showModal.value = true;
        }

        function openEditModal(stu) {
            modalMode.value = 'edit';
            editId.value = stu.id;
            Object.assign(studentForm, {
                studentId: stu.studentId,
                name: stu.name,
                age: stu.age,
                sex: stu.sex,
                department: stu.department || '',
                className: stu.className || '',
                email: stu.email || '',
                phone: stu.phone || ''
            });
            showModal.value = true;
        }

        async function saveStudent() {
            try {
                let data;
                if (modalMode.value === 'add') {
                    data = await apiPost('/api/students', studentForm);
                } else {
                    data = await apiPut('/api/students/' + editId.value, studentForm);
                }
                if (data.code === 200) {
                    toast(data.message);
                    showModal.value = false;
                    await loadStudents();
                    if (activeNav.value === 'dashboard') await loadDashboard();
                } else {
                    toast(data.message, 'error');
                }
            } catch (e) { toast(e.message, 'error'); }
        }

        async function deleteStudent(id) {
            if (!confirm('确定要删除该学生吗？')) return;
            try {
                const data = await apiDelete('/api/students/' + id);
                if (data.code === 200) {
                    toast('删除成功');
                    await loadStudents();
                    if (activeNav.value === 'dashboard') await loadDashboard();
                } else {
                    toast(data.message, 'error');
                }
            } catch (e) { toast(e.message, 'error'); }
        }

        function exportExcel() {
            window.open(API + '/api/export/excel?token=' + token.value, '_blank');
            toast('正在下载 Excel...');
        }

        // ========== 日志 ==========
        async function loadLogs() {
            try {
                const data = await apiGet('/api/logs');
                if (data.code === 200) logs.value = data.data;
            } catch (e) { /* ignore */ }
        }

        // ========== 导航切换 ==========
        function switchNav(nav) {
            activeNav.value = nav;
            if (nav === 'dashboard') loadDashboard();
            if (nav === 'students') loadStudents();
            if (nav === 'logs') loadLogs();
        }

        // ========== 分页计算 ==========
        const paginatedStudents = computed(() => {
            const start = (currentPage.value - 1) * pageSize.value;
            return students.value.slice(start, start + pageSize.value);
        });

        const totalPages = computed(() => Math.ceil(totalStudents.value / pageSize.value) || 1);

        function goPage(p) {
            if (p >= 1 && p <= totalPages.value) currentPage.value = p;
        }

        // ========== 导出 ==========
        return {
            view, loggedIn, token, user, message,
            loginForm, regForm, forgotForm,
            activeNav,
            dashboardStats,
            students, searchKeyword, filterDept, currentPage, pageSize, totalStudents,
            paginatedStudents, totalPages,
            showModal, modalMode, studentForm,
            logs,
            doLogin, doRegister, doForgotPassword, logout,
            switchNav, loadDashboard, loadStudents, loadLogs,
            openAddModal, openEditModal, saveStudent, deleteStudent, exportExcel,
            goPage
        };
    }
});

app.mount('#app');
