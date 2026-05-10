<!-- markdownlint-disable -->
<div align="center">

<a href="https://github.com/lechan775/student_management">
  <img src="docs/images/banner.png" alt="学生管理システム バナー" width="100%">
</a>

<h1>学生管理システム</h1>

<p><strong>コンソールからクラウドへ — フルスタック進化</strong></p>

[English](README.md) | [中文](README.zh-CN.md) | 日本語

<p>
  <a href="https://github.com/lechan775/student_management/actions">
    <img src="https://img.shields.io/github/actions/workflow/status/lechan775/student_management/.github/workflows/ci.yml?branch=main&label=CI&logo=github&style=flat-square" alt="CI ステータス">
  </a>
  <a href="https://github.com/lechan775/student_management/blob/main/LICENSE">
    <img src="https://img.shields.io/github/license/lechan775/student_management?style=flat-square&color=blue" alt="MIT ライセンス">
  </a>
  <img src="https://img.shields.io/badge/Java-17%2B-orange?style=flat-square&logo=openjdk" alt="Java 17+">
  <img src="https://img.shields.io/badge/Spring%20Boot-3.2.5-brightgreen?style=flat-square&logo=springboot" alt="Spring Boot 3.2.5">
  <img src="https://img.shields.io/badge/Vue-3.4-4FC08D?style=flat-square&logo=vue.js" alt="Vue 3.4">
  <img src="https://img.shields.io/badge/Docker-対応-2496ED?style=flat-square&logo=docker" alt="Docker Ready">
  <img src="https://img.shields.io/badge/MySQL-8.0-4479A1?style=flat-square&logo=mysql" alt="MySQL 8.0">
  <img src="https://img.shields.io/badge/Redis-7-DC382D?style=flat-square&logo=redis" alt="Redis 7">
  <a href="https://github.com/lechan775/student_management/stargazers">
    <img src="https://img.shields.io/github/stars/lechan775/student_management?style=flat-square&color=yellow" alt="Stars">
  </a>
</p>

</div>

---

## 📖 概要

**学生管理システム**は、シンプルな Java コンソールアプリケーションから本番環境対応の Web プラットフォームへの完全な進化を示す、プログレッシブなフルスタック教育プロジェクトです。CS 学部生向けの学習ロードマップとして設計されており、各バージョンでエンタープライズレベルのエンジニアリング手法を段階的に導入しています。

---

## 🗺 バージョン進化

| バージョン | ディレクトリ | インターフェース | データベース | セキュリティ | デプロイ |
|-----------|-------------|-----------------|-------------|-------------|---------|
| 🏘️ **Novice** | `Student_manage/` | コンソール | `ArrayList` | なし | `javac *.java` |
| 🏙️ **Advanced** | `src/main/java/` | コンソール | SQLite | BCrypt | `mvn exec:java` |
| 🚀 **Universe** | `universe/` | ブラウザ SPA | H2 | JWT | `mvn spring-boot:run` |
| ☄️ **BigBang** | `bigbang/` | 完全 Web アプリ | MySQL 8 + Redis | JWT デュアルトークン | **Docker Compose** |

---

## ☄️ BigBang — クイックスタート

```bash
# ワンコマンド起動
cd bigbang
docker-compose up -d

# アクセス
open http://localhost          # Web アプリ
open http://localhost:8080/doc.html  # Swagger API ドキュメント

# デフォルト管理者
ユーザー名: admin    パスワード: Admin@123
```

---

## 🛠 技術スタック

| カテゴリ | 技術 |
|----------|------|
| **フレームワーク** | Spring Boot 3.2.5 |
| **データベース** | MySQL 8.0 |
| **キャッシュ** | Redis 7 |
| **ORM** | Spring Data JPA (Hibernate 6.4) |
| **マイグレーション** | Flyway |
| **マッピング** | MapStruct 1.5.5 |
| **セキュリティ** | Spring Security + JWT (jjwt 0.12) |
| **API ドキュメント** | Knife4j (Swagger) |
| **フロントエンド** | Vue 3 + Vite + TypeScript |
| **UI ライブラリ** | Element Plus 2.6 |
| **チャート** | ECharts 5.5 |
| **テスト** | JUnit 5 + Mockito |
| **CI/CD** | GitHub Actions |
| **コンテナ** | Docker + Compose |

---

## 📡 API リファレンス

サーバー起動後、`http://localhost:8080/doc.html` で完全な API ドキュメントを参照できます。

| メソッド | エンドポイント | 権限 |
|---------|---------------|------|
| `POST` | `/api/auth/login` | 公開 |
| `POST` | `/api/auth/register` | 公開 |
| `GET` | `/api/students?page=&size=` | 全ユーザー |
| `POST` | `/api/students` | 管理者/教師 |
| `PUT` | `/api/students/{id}` | 管理者/教師 |
| `DELETE` | `/api/students/{id}` | 管理者/教師 |
| `GET` | `/api/dashboard` | 管理者/教師 |
| `GET` | `/api/export/excel` | 管理者/教師 |
| `GET` | `/api/logs` | 管理者 |

---

## 📄 ライセンス

MIT License — 詳細は [LICENSE](LICENSE) を参照してください。

---

<div align="center">
  <sub><a href="https://github.com/lechan775">lechan775</a> によって ❤️ で構築 | Spring Boot & Vue 3 搭載</sub>
</div>
