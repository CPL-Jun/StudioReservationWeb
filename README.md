# スタジオ予約管理システム v2.0

バンド練習用スタジオ、飲食店、ライブハウスの予約を一元管理するWebシステム

## 機能

- ✅ ユーザー認証（メールアドレス・パスワード）
- ✅ 予約管理（作成・編集・削除・カレンダー表示）
- ✅ ユーザープロフィール管理
- ✅ 掲示板（コピー曲募集）
- ✅ イベント告知
- ✅ 売上集計ダッシュボード（管理者のみ）

## 必要環境

- Java 17以上
- Maven 3.9以上

## 起動方法

```bash
# ビルド
mvn clean install

# 起動
mvn spring-boot:run
```

## アクセス

http://localhost:8080/

## 初期セットアップ

1. ブラウザで `/register` にアクセスして新規ユーザーを作成
2. 管理者権限が必要な場合:

```bash
sqlite3 reservation.db
UPDATE app_users SET role = 'ROLE_ADMIN' WHERE email = 'your-email@example.com';
.quit
```

## 技術スタック

- **言語**: Java 17
- **フレームワーク**: Spring Boot 3.4.1
- **データベース**: SQLite 3.45.2.0
- **認証**: Spring Security 6.4.2
- **フロントエンド**: Thymeleaf + FullCalendar 6.1.15

## ライセンス

MIT License

## 作成者

CPL-Jun
