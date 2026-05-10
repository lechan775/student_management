# student_management

管理员登录界面 + 内部学生信息管理
该仓库旨在按照不同等级，完善全套学生信息管理系统
新手村 + 进阶版 + 宇宙版

---

## 🏘️ 新手村版本（控制台 / 无数据库）

> 路径：`Student_manage/` — 纯 Java 命令行，无外部依赖，ArrayList 内存存储

### 一、登录界面

| 功能 | 说明 |
|------|------|
| 登录 | 用户名查找 + 验证码验证 |
| 注册 | 用户名唯一性检查 / 身份证号合法性校验 / 手机号合法性校验 / 两次密码一致 |
| 忘记密码 | 校验用户名 + 手机号 + 身份证号 三者匹配后方可改密码 |

### 二、学生信息管理系统

| 功能 | 说明 |
|------|------|
| 添加学生 | 学号、姓名、年龄、性别（4 项） |
| 删除学生 | 按学号删除 |
| 查询学生 | 按学号精确查询 |
| 更新学生 | 先查学号再覆盖 |
| 遍历全部 | 列出所有学生信息 |
| 退出 | 返回上级 |

---

## 🏙️ 进阶版（控制台 / SQLite 持久化）

> 路径：`src/main/java/com/studentmanage/` — Maven 项目，SQLite + BCrypt

### 🆚 相比新手村的核心升级

| 维度 | 新手村 | 进阶版 |
|------|--------|--------|
| 存储 | `ArrayList` 内存（重启即丢） | **SQLite 数据库**（持久化） |
| 密码安全 | 明文存储 | **BCrypt 加盐哈希** |
| 架构 | 单文件平铺 | **MVC 分层**（model/dao/service/ui） |
| 项目结构 | 裸 `.java` 文件 | **Maven 工程**（pom.xml） |
| 学生字段 | 4 项（id/name/age/sex） | **8 项**（+院系/班级/邮箱/手机） |
| 查询方式 | 只按学号 | **多维度**：学号 / 姓名模糊 / 院系过滤 |
| 统计 | 无 | **学生总数统计** |

### 技术栈

- **Java 17+**
- **Maven** — 依赖管理 & 打包
- **SQLite (JDBC)** — 零配置嵌入式数据库
- **jBCrypt** — 密码加盐哈希

### 快速开始

```bash
# 1. 编译
mvn clean compile

# 2. 运行
mvn exec:java -Dexec.mainClass="com.studentmanage.MainApp"

# 3. 打包为可执行 jar
mvn clean package
java -jar target/student-management-2.0.0-advanced-jar-with-dependencies.jar
```

### 项目结构

```
student_management/
├── Student_manage/          ← 新手村（原版，已冻结）
│   ├── Application.java
│   ├── User.java
│   ├── Student.java
│   └── cecha.java
├── src/main/java/com/studentmanage/
│   ├── MainApp.java         ← 入口
│   ├── model/
│   │   ├── User.java        ← 用户实体
│   │   └── Student.java     ← 学生实体（8字段）
│   ├── dao/
│   │   ├── DatabaseManager.java  ← SQLite 连接 & 建表
│   │   ├── UserDAO.java          ← 用户 CRUD
│   │   └── StudentDAO.java       ← 学生 CRUD + 模糊搜索
│   ├── service/
│   │   ├── AuthService.java      ← 登录/注册/忘记密码
│   │   └── StudentService.java   ← 学生管理业务逻辑
│   └── ui/
│       ├── LoginMenu.java        ← 认证界面
│       └── StudentMenu.java      ← 学生管理界面
├── pom.xml                 ← Maven 配置
└── README.md
```

### 功能清单

#### 认证模块

- ✅ 登录（用户名 + 密码 + 验证码）
- ✅ 注册（用户名/身份证/手机号校验 → BCrypt 哈希入库）
- ✅ 忘记密码（三重身份验证）

#### 学生管理模块

- ✅ 添加学生（8 项字段）
- ✅ 删除学生（按学号）
- ✅ 查询学生（按学号精确匹配）
- ✅ **搜索学生（按姓名模糊匹配）** 🆕
- ✅ **过滤学生（按院系）** 🆕
- ✅ 更新学生信息（全字段覆盖）
- ✅ 显示全部学生（格式化表格）
- ✅ **学生总数统计** 🆕

---

## 🚀 宇宙版（计划中）

> 下一阶段规划，欢迎贡献！

| 方向 | 技术选型 | 功能 |
|------|---------|------|
| Web 化 | Spring Boot + Vue/React | 前后端分离，RESTful API |
| 可视化 | ECharts | 院系分布 / 性别比例 图表 |
| 权限 | Spring Security + JWT | 多角色（管理员/教师/学生） |
| 导入导出 | Apache POI | Excel 批量导入导出 |
| 日志 | 操作日志表 | 审计追踪 |

---

## License

MIT
