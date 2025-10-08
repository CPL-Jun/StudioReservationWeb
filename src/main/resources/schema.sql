-- ユーザー認証テーブル
CREATE TABLE IF NOT EXISTS app_users (
  id            INTEGER PRIMARY KEY AUTOINCREMENT,
  email         TEXT    NOT NULL UNIQUE,
  password_hash TEXT    NOT NULL,
  display_name  TEXT    NOT NULL,
  role          TEXT    NOT NULL DEFAULT 'ROLE_USER',
  created_at    TEXT    NOT NULL DEFAULT (datetime('now'))
);

-- ユーザープロフィールテーブル
CREATE TABLE IF NOT EXISTS user_profiles (
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

-- 予約用プロフィールテーブル（予約時の名前選択用）
CREATE TABLE IF NOT EXISTS profiles (
  id         INTEGER PRIMARY KEY AUTOINCREMENT,
  name       TEXT NOT NULL,
  instrument TEXT,
  genre      TEXT,
  bio        TEXT
);

-- 予約テーブル
CREATE TABLE IF NOT EXISTS reservations (
  id         INTEGER PRIMARY KEY AUTOINCREMENT,
  venue      TEXT    NOT NULL DEFAULT 'STUDIO',
  room       TEXT    NOT NULL,
  start_time TEXT    NOT NULL,
  end_time   TEXT    NOT NULL,
  name       TEXT    NOT NULL
);

-- 掲示板テーブル
CREATE TABLE IF NOT EXISTS bbs_posts (
  id         INTEGER PRIMARY KEY AUTOINCREMENT,
  name       TEXT NOT NULL,
  song       TEXT NOT NULL,
  band       TEXT NOT NULL,
  comment    TEXT,
  created_at TEXT NOT NULL
);

-- イベント告知テーブル
CREATE TABLE IF NOT EXISTS events (
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

-- はまじるし参加者テーブル
CREATE TABLE IF NOT EXISTS hamajirushi_participants (
  id         INTEGER PRIMARY KEY AUTOINCREMENT,
  date       TEXT    NOT NULL,
  user_id    INTEGER NOT NULL,
  created_at TEXT    NOT NULL DEFAULT (datetime('now')),
  FOREIGN KEY (user_id) REFERENCES app_users(id) ON DELETE CASCADE
);