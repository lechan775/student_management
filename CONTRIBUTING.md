# Contributing to Student Management System

感谢你对本项目的关注！以下指南将帮助你高效参与贡献。

## 📋 目录

- [行为准则](#行为准则)
- [如何贡献](#如何贡献)
- [开发环境搭建](#开发环境搭建)
- [提交规范](#提交规范)
- [代码风格](#代码风格)
- [测试要求](#测试要求)
- [Pull Request 流程](#pull-request-流程)

## 行为准则

本项目遵循 [Contributor Covenant 行为准则](CODE_OF_CONDUCT.md)。参与即视为同意遵守。

## 如何贡献

### 🐛 报告 Bug

1. 先在 [Issues](https://github.com/lechan775/student_management/issues) 中搜索是否已有相同问题
2. 如果没有，创建新 Issue，包含：
   - 使用的版本（新手村/进阶版/宇宙版/爆炸版）
   - 操作系统和 Java 版本
   - 复现步骤
   - 预期行为和实际行为
   - 截图或日志（如有）

### 💡 功能建议

1. 先在 Issues 中搜索是否已有类似建议
2. 创建新 Issue，描述：
   - 功能的使用场景
   - 期望的行为
   - 是否愿意自己实现

### 🔧 代码贡献

适合新手的任务会标记为 `good first issue`。我们特别欢迎以下方向的贡献：

| 方向 | 说明 |
|------|------|
| **前端增强** | 学生头像上传预览、批量导入 Excel、移动端适配 |
| **后端增强** | 接口限流、操作日志导出、数据统计报表 |
| **测试** | 补充单元测试和集成测试，提高代码覆盖率 |
| **文档** | README 翻译（韩语/俄语/法语）、API 文档完善 |
| **DevOps** | Kubernetes 部署配置、监控告警集成 |

## 开发环境搭建

### 宇宙爆炸版（推荐）

```bash
# 1. 克隆仓库
git clone https://github.com/lechan775/student_management.git
cd student_management

# 2. 启动基础设施
cd bigbang
docker-compose up -d mysql redis

# 3. 启动后端
mvn spring-boot:run

# 4. 启动前端（新终端）
cd ../bigbang-frontend
npm install
npm run dev
```

### 新手村版本（零依赖）

```bash
cd Student_manage
javac *.java
java Application
```

## 提交规范

本项目使用 [Conventional Commits](https://www.conventionalcommits.org/) 规范：

```
<type>(<scope>): <description>

[optional body]
```

**类型 (type)：**

| 类型 | 说明 |
|------|------|
| `feat` | 新功能 |
| `fix` | Bug 修复 |
| `docs` | 文档更新 |
| `style` | 代码格式（不影响功能） |
| `refactor` | 代码重构 |
| `test` | 测试相关 |
| `chore` | 构建/工具/依赖更新 |

**示例：**

```bash
git commit -m "feat(bigbang): add rate limiting interceptor"
git commit -m "fix(bigbang): resolve JWT refresh token rotation bug"
git commit -m "docs: add Korean README translation"
git commit -m "test(student): add pagination edge case tests"
```

## 代码风格

- **Java**: 遵循 Google Java Style Guide
- **Vue/TypeScript**: 遵循项目已有的 ESLint 配置
- 类名：`PascalCase`
- 方法名/变量名：`camelCase`
- 常量：`UPPER_SNAKE_CASE`
- 数据库表名/列名：`snake_case`

## 测试要求

```bash
# 运行所有测试
cd bigbang && mvn test

# 运行特定测试类
mvn test -Dtest=StudentServiceTest

# 生成覆盖率报告
mvn verify
# 报告位置: target/site/jacoco/index.html
```

- 新功能必须包含对应测试
- Bug 修复应该添加回归测试
- 目标覆盖率：> 60%

## Pull Request 流程

1. 🍴 **Fork** 本仓库
2. 🌿 创建特性分支：`git checkout -b feat/your-feature`
3. ✏️ 编写代码 + 测试
4. ✅ 确保 `mvn test` 全部通过
5. 💾 **Commit** 遵循 Conventional Commits 规范
6. 📤 **Push** 到你的 Fork
7. 🔃 发起 **Pull Request** 到 `main` 分支
8. 👀 等待 Review，根据反馈修改

**PR 标题示例：** `feat: add rate limiting for API endpoints`

**PR 描述模板：**

```markdown
## 改动描述
简要描述改了什么、为什么改。

## 相关 Issue
Closes #123

## 测试
- [ ] 单元测试通过
- [ ] 集成测试通过
- [ ] 手动测试通过

## 截图（如有 UI 改动）
```

---

再次感谢你的贡献！每一个 Star、Issue、PR 都很有价值 💙
