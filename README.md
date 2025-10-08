# スタジオ予約管理システム 仕様書 v2.1

## 📋 目次

1. [システム概要](#システム概要)
2. [技術スタック](#技術スタック)
3. [機能一覧](#機能一覧)
4. [データベース設計](#データベース設計)
5. [画面仕様](#画面仕様)
6. [API仕様](#api仕様)
7. [セキュリティ](#セキュリティ)
8. [デプロイ・運用](#デプロイ運用)

---

## システム概要

### 目的
バンド練習用スタジオ、飲食店、ライブハウスの予約を一元管理し、メンバー間のコミュニケーションを促進するWebシステム

### 主要機能
- ✅ ユーザー認証（メールアドレス・パスワード）
- ✅ 予約管理（作成・編集・削除・カレンダー表示）
- ✅ ユーザープロフィール管理（統合版）
- ✅ 掲示板（コピー曲募集）
- ✅ イベント告知
- ✅ 売上集計ダッシュボード（管理者のみ）

### アクセスURL
- **開発環境**: http://localhost:8080/
- **本番環境**: （未定）

---

## 技術スタック

### バックエンド
- **言語**: Java 17
- **フレームワーク**: Spring Boot 3.4.1
- **データベース**: SQLite 3.45.2.0
- **認証**: Spring Security 6.4.2
- **ビルドツール**: Maven 3.9+

### フロントエンド
- **テンプレートエンジン**: Thymeleaf
- **カレンダーライブラリ**: FullCalendar 6.1.15 (Scheduler対応)
- **スタイル**: CSS (カスタム)

### 開発環境
- **JDK**: 17以上
- **IDE**: IntelliJ IDEA / Eclipse / VS Code
- **ブラウザ**: Chrome / Firefox / Safari (最新版)

---

## 機能一覧

### 1. 認証機能

#### 1.1 ログイン
- **URL**: `/` または `/login`
- **機能**: メールアドレスとパスワードによる認証
- **デフォルトアカウント**:
  - 一般ユーザー: `test@example.com` / `password`
  - 管理者: `admin@example.com` / `admin`

#### 1.2 新規登録
- **URL**: `/register`
- **入力項目**:
  - メールアドレス（必須・重複チェック）
  - パスワード（必須・8文字以上）
  - 表示名（必須・50文字以内）
- **デフォルト権限**: `ROLE_USER`

#### 1.3 ログアウト
- **URL**: `/logout` (GET)
- **処理**: セッション破棄してログイン画面へリダイレクト

---

### 2. 予約管理機能

#### 2.1 予約一覧
- **URL**: `/manage`
- **表示形式**:
  - PC: テーブル形式
  - スマートフォン: カード形式
- **表示項目**: ID、部屋、予約者、開始時刻、終了時刻、操作ボタン

#### 2.2 予約登録
- **URL**: `/reservations` (POST)
- **入力項目**:
  - 部屋（選択: スタジオA、スタジオB、はまじるし）
  - 予約者名（選択: 登録ユーザーの表示名から選択）
  - 開始時刻（日時入力）
  - 終了時刻（日時入力）
- **自動設定**: venue（部屋から自動判定）

#### 2.3 予約編集
- **URL**: `/reservations/{id}/edit` (GET)
- **URL**: `/reservations/{id}` (POST)
- **編集可能項目**: 部屋、予約者名、開始時刻、終了時刻

#### 2.4 予約削除
- **URL**: `/reservations/{id}/delete` (POST)
- **確認**: JavaScript confirmダイアログで確認

---

### 3. カレンダー機能

#### 3.1 スタジオカレンダー
- **URL**: `/calendar/studio`
- **対象**: スタジオA、スタジオB
- **表示形式**:
  - PC: 週ビュー（resourceTimeGridWeek）
  - スマートフォン: 日ビュー（resourceTimeGridDay）
- **時間範囲**: 00:00〜24:00（24時間表示）
- **操作**:
  - ドラッグ＆ドロップで予約作成
  - イベントクリックで削除
  - ドラッグで時間・部屋変更
  - リサイズで時間調整

#### 3.2 はまじるしカレンダー
- **URL**: `/calendar/hamajirushi`
- **対象**: はまじるし（飲食店）
- **時間範囲**: 10:00〜23:00
- **操作**: スタジオカレンダーと同様

#### 3.3 Roxetteカレンダー
- **URL**: `/calendar/roxette`
- **対象**: Roxette（ライブハウス）
- **時間範囲**: 10:00〜23:00
- **制限**: 閲覧のみ（編集・削除不可）
- **用途**: 予約状況の確認のみ

#### 3.4 カレンダー色分け
- スタジオA: `#ff6b6b` (赤)
- スタジオB: `#4ecdc4` (シアン)
- はまじるし: `#f7b731` (黄)
- Roxette: `#a29bfe` (紫)

---

### 4. プロフィール機能（統合版）

#### 4.1 プロフィール表示
- **URL**: `/profile`
- **表示内容**:
  - **上部**: マイプロフィール（編集ボタン付き）
  - **下部**: 他のメンバー一覧（カード形式）

#### 4.2 プロフィール編集
- **URL**: `/profile/edit` (GET/POST)
- **入力項目**:
  
  **音楽情報**
  - メインパート（必須・選択: Vo., Gt., Ba., Dr., Key., Per.）
  - サブパート（複数選択可）
  - 好きなバンド（カンマ区切りテキスト）
  
  **基本情報**
  - 性別（選択: 男性/女性/その他/選択しない）
  - 年齢（数値入力・1〜120）
  
  **自己紹介**
  - コメント（テキストエリア・改行対応）

#### 4.3 プロフィール表示項目
- ユーザー名、メールアドレス
- メインパート（バッジ表示）
- サブパート（バッジ表示）
- 好きなバンド（バッジ表示）
- 性別、年齢
- 自己紹介・コメント（背景色付きボックス）

---

### 5. 掲示板機能

#### 5.1 掲示板表示
- **URL**: `/bbs`
- **表示形式**: 投稿一覧（新しい順）

#### 5.2 投稿作成
- **URL**: `/bbs` (POST)
- **入力項目**:
  - 曲名（必須）
  - バンド名（必須）
  - コメント（任意）
- **投稿者名**: ログインユーザーの表示名（自動設定）

#### 5.3 投稿表示項目
- 投稿者名
- 曲名、バンド名
- コメント
- 投稿日時（yyyy-MM-dd HH:mm）

---

### 6. イベント告知機能

#### 6.1 イベント一覧
- **URL**: `/events`
- **表示形式**: カード形式（グリッドレイアウト）
- **表示項目**: 
  - イベント画像（または背景グラデーション）
  - タイトル、説明
  - 開催日時、会場
  - 定員情報（現在数/定員）
  - 編集・削除ボタン（管理者のみ）

#### 6.2 イベント作成
- **URL**: `/events/new` (GET)
- **URL**: `/events` (POST)
- **権限**: 管理者のみ
- **入力項目**:
  - イベント名（必須）
  - 説明（必須）
  - 開催日時（必須）
  - 開催場所（必須）
  - 画像URL（任意）
  - 定員（数値・任意）

#### 6.3 イベント編集
- **URL**: `/events/{id}/edit` (GET)
- **URL**: `/events/{id}` (POST)
- **権限**: 管理者のみ

#### 6.4 イベント削除
- **URL**: `/events/{id}/delete` (POST)
- **権限**: 管理者のみ
- **確認**: JavaScript confirmダイアログ

---

### 7. 管理機能

#### 7.1 売上集計ダッシュボード
- **URL**: `/admin`
- **権限**: 管理者（ROLE_ADMIN）のみ
- **集計項目**:
  - **今日の予約件数**: Roxette除外
  - **今日の売上**: 予約時間×1,200円
  - **今月の予約件数**: Roxette除外
  - **今月の売上**: 予約時間×1,200円
- **計算方法**: 予約時間（時間）× 1,200円
- **除外条件**: Roxetteの予約は集計対象外

#### 7.2 ダッシュボード
- **URL**: `/dashboard`
- **表示内容**: 機能へのショートカットカード
- **カード一覧**:
  - 予約管理
  - カレンダー
  - マイプロフィール
  - プロフィール一覧（削除予定）
  - 掲示板
  - イベント
  - 管理画面（管理者のみ）

---

## データベース設計

### テーブル構成

#### app_users（ユーザー認証）
```sql
CREATE TABLE app_users (
  id            INTEGER PRIMARY KEY AUTOINCREMENT,
  email         TEXT    NOT NULL UNIQUE,
  password_hash TEXT    NOT NULL,
  display_name  TEXT    NOT NULL,
  role          TEXT    NOT NULL DEFAULT 'ROLE_USER',
  created_at    TEXT    NOT NULL DEFAULT (datetime('now'))
);
```

#### user_profiles（ユーザープロフィール）
```sql
CREATE TABLE user_profiles (
  id              INTEGER PRIMARY KEY AUTOINCREMENT,
  user_id         INTEGER NOT NULL UNIQUE,
  main_instrument TEXT,
  sub_instruments TEXT,
  favorite_bands  TEXT,
  gender          TEXT,
  age             INTEGER,
  comment         TEXT,
  updated_at      TEXT NOT NULL DEFAULT (datetime('now')),
  FOREIGN KEY (user_id) REFERENCES app_users(id) ON DELETE CASCADE
);
```

#### profiles（予約用プロフィール - レガシー）
```sql
CREATE TABLE profiles (
  id         INTEGER PRIMARY KEY AUTOINCREMENT,
  name       TEXT NOT NULL,
  instrument TEXT,
  genre      TEXT,
  bio        TEXT
);
```

#### reservations（予約）
```sql
CREATE TABLE reservations (
  id         INTEGER PRIMARY KEY AUTOINCREMENT,
  venue      TEXT    NOT NULL DEFAULT 'STUDIO',
  room       TEXT    NOT NULL,
  start_time TEXT    NOT NULL,
  end_time   TEXT    NOT NULL,
  name       TEXT    NOT NULL
);
```

#### bbs_posts（掲示板投稿）
```sql
CREATE TABLE bbs_posts (
  id         INTEGER PRIMARY KEY AUTOINCREMENT,
  name       TEXT NOT NULL,
  song       TEXT NOT NULL,
  band       TEXT NOT NULL,
  comment    TEXT,
  created_at TEXT NOT NULL
);
```

#### events（イベント告知）
```sql
CREATE TABLE events (
  id            INTEGER PRIMARY KEY AUTOINCREMENT,
  title         TEXT    NOT NULL,
  description   TEXT    NOT NULL,
  event_date    TEXT    NOT NULL,
  venue         TEXT    NOT NULL,
  image_url     TEXT,
  capacity      INTEGER DEFAULT 0,
  current_count INTEGER DEFAULT 0,
  created_by    TEXT    NOT NULL,
  created_at    TEXT    NOT NULL DEFAULT (datetime('now')),
  updated_at    TEXT    NOT NULL DEFAULT (datetime('now'))
);
```

---

## 画面仕様

### レスポンシブデザイン対応

#### ブレークポイント
- **PC**: 769px以上
- **スマートフォン**: 768px以下

#### 画面別対応

| 画面 | PC | スマートフォン |
|------|-----|----------------|
| ログイン | 中央配置フォーム | 同左 |
| ダッシュボード | グリッド（3-4列） | グリッド（1列） |
| 予約管理 | テーブル | カード形式 |
| カレンダー | 週ビュー | 日ビュー |
| プロフィール | 2列グリッド | 1列グリッド |
| 掲示板 | カード（2-3列） | カード（1列） |
| イベント | カード（3列） | カード（1列） |

### カラースキーム
- **プライマリ**: `#667eea` → `#764ba2` (グラデーション)
- **成功**: `#4CAF50`
- **警告**: `#ffc107`
- **エラー**: `#f44336`
- **背景**: `#f5f5f5`
- **カード**: `#ffffff`

---

## API仕様

### REST API エンドポイント

#### 予約API

**GET /api/events**
- 説明: 予約イベント一覧取得
- パラメータ:
  - `start` (optional): 開始日時
  - `end` (optional): 終了日時
  - `room` (optional): 部屋名
- レスポンス:
```json
[
  {
    "id": 1,
    "title": "予約者名",
    "start": "2025-10-08T10:00:00",
    "end": "2025-10-08T12:00:00",
    "resourceId": "A",
    "color": "#ff6b6b"
  }
]
```

**POST /api/events**
- 説明: 予約作成
- リクエストボディ:
```json
{
  "resourceId": "A",
  "name": "予約者名",
  "start": "2025-10-08T10:00:00",
  "end": "2025-10-08T12:00:00"
}
```

**PUT /api/events/{id}**
- 説明: 予約更新
- リクエストボディ:
```json
{
  "start": "2025-10-08T11:00:00",
  "end": "2025-10-08T13:00:00",
  "resourceId": "B"
}
```

**DELETE /api/events/{id}**
- 説明: 予約削除
- レスポンス:
```json
{
  "status": "ok"
}
```

---

## セキュリティ

### 認証・認可

#### Spring Security設定
- **ログインURL**: `/login` (POST)
- **ログアウトURL**: `/logout` (GET)
- **セッション管理**: Cookie-based
- **CSRF保護**: 有効（API除外: `/api/**`）

#### 権限制御
- **ROLE_USER**: 基本機能（予約、掲示板、プロフィール）
- **ROLE_ADMIN**: 管理機能（イベント作成・編集・削除、売上集計）

#### パスワード暗号化
- **アルゴリズム**: BCrypt
- **ストレングス**: デフォルト（10ラウンド）

### アクセス制御

| URL | 権限 |
|-----|------|
| `/`, `/login`, `/register` | 全員 |
| `/dashboard` | 認証済み |
| `/manage`, `/calendar/**`, `/profile` | 認証済み |
| `/bbs` | 認証済み |
| `/events` | 全員（閲覧のみ） |
| `/events/new`, `/events/*/edit`, `/events/*/delete` | ROLE_ADMIN |
| `/admin` | ROLE_ADMIN |

---

## デプロイ・運用

### ビルド・起動

#### 開発環境
```bash
# ビルド
mvn clean install

# 起動
mvn spring-boot:run
```

#### 本番環境
```bash
# JARファイル作成
mvn clean package

# 起動
java -jar target/studio-reservation-web-2.0.0.jar
```

### 環境変数
```properties
# application.properties
server.port=8080
spring.datasource.url=jdbc:sqlite:reservation.db
spring.thymeleaf.cache=false  # 開発時のみ
```

### データベース管理

#### 初期化
```bash
# データベースファイル削除
rm reservation.db

# 起動時に自動作成
mvn spring-boot:run
```

#### バックアップ
```bash
# SQLiteデータベースコピー
cp reservation.db reservation_backup_$(date +%Y%m%d).db
```

#### 管理者権限付与
```sql
sqlite3 reservation.db
UPDATE app_users SET role = 'ROLE_ADMIN' WHERE email = 'user@example.com';
.quit
```

---

## 今後の拡張予定

### 将来実装機能
- [ ] Google Calendar API連携
- [ ] メール通知機能
- [ ] 予約のリマインダー機能
- [ ] 予約統計・レポート機能
- [ ] ユーザーアバター画像アップロード
- [ ] プロフィール公開/非公開設定
- [ ] モバイルアプリ（PWA化）

### 技術的改善
- [ ] PostgreSQL/MySQL対応
- [ ] Docker化
- [ ] CI/CD導入
- [ ] ユニットテスト充実
- [ ] REST API のSwagger文書化

---

## ライセンス
MIT License

## 作成者
CPL-Jun

## バージョン履歴
- **v2.1** (2025-10-08): プロフィール統合版、コメント機能追加
- **v2.0** (2024): 初版リリース