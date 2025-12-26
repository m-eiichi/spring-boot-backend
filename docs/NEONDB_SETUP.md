# NeonDB移行ロードマップ

このドキュメントでは、Spring BootアプリケーションをH2データベースからNeonDB（PostgreSQL）に移行する手順を説明します。

## 現在の状態

- **現在のDB**: H2インメモリデータベース
- **PostgreSQLドライバー**: 既に`pom.xml`に含まれている ✓
- **移行先**: NeonDB（サーバーレスPostgreSQL）

---

## ステップ1: NeonDBアカウント作成とデータベースセットアップ

### 1.1 NeonDBアカウント作成

1. https://neon.tech にアクセス
2. GitHubアカウントでサインアップ（推奨）または Email でサインアップ

### 1.2 プロジェクト作成

1. ダッシュボードで「New Project」をクリック
2. プロジェクト設定:
   - **プロジェクト名**: `spring-boot-demo`（任意）
   - **リージョン**: 最寄りのリージョン（例: Asia Pacific (Tokyo)）
   - **PostgreSQLバージョン**: 最新版（デフォルト推奨）
   - **Compute size**: Free tier で開始

### 1.3 データベース作成

- データベース名: `demo_db`
- ブランチ: `main`（デフォルト）

---

## ステップ2: 接続情報の取得と環境変数設定

### 2.1 接続文字列の取得

NeonDBダッシュボードの「Connection Details」から以下の形式の接続文字列を取得:

```
postgresql://[user]:[password]@[endpoint]/[database]?sslmode=require
```

例:
```
postgresql://demo_user:AbCd1234@ep-cool-darkness-123456.us-east-2.aws.neon.tech/demo_db?sslmode=require
```

### 2.2 環境変数ファイル作成

プロジェクトルートに`.env`ファイルを作成:

```properties
NEON_DB_URL=postgresql://user:password@ep-xxx-xxx.region.aws.neon.tech/demo_db?sslmode=require
NEON_DB_USERNAME=your_username
NEON_DB_PASSWORD=your_password
```

**重要**: `.env`ファイルを`.gitignore`に追加してください！

---

## ステップ3: Spring Boot設定ファイルの更新

### 3.1 開発環境用設定ファイル作成

`src/main/resources/application-dev.properties`:

```properties
# NeonDB Configuration (Development)
spring.datasource.url=${NEON_DB_URL}
spring.datasource.username=${NEON_DB_USERNAME}
spring.datasource.password=${NEON_DB_PASSWORD}
spring.datasource.driver-class-name=org.postgresql.Driver

# JPA Configuration
spring.jpa.database-platform=org.hibernate.dialect.PostgreSQLDialect
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.format_sql=true
spring.jpa.properties.hibernate.jdbc.time_zone=UTC

# Connection Pool Settings (NeonDB推奨設定)
spring.datasource.hikari.maximum-pool-size=5
spring.datasource.hikari.minimum-idle=2
spring.datasource.hikari.connection-timeout=20000
spring.datasource.hikari.idle-timeout=300000
spring.datasource.hikari.max-lifetime=600000
```

### 3.2 本番環境用設定ファイル作成

`src/main/resources/application-prod.properties`:

```properties
# NeonDB Configuration (Production)
spring.datasource.url=${NEON_DB_URL}
spring.datasource.username=${NEON_DB_USERNAME}
spring.datasource.password=${NEON_DB_PASSWORD}
spring.datasource.driver-class-name=org.postgresql.Driver

# JPA Configuration
spring.jpa.database-platform=org.hibernate.dialect.PostgreSQLDialect
spring.jpa.hibernate.ddl-auto=validate
spring.jpa.show-sql=false
spring.jpa.properties.hibernate.jdbc.time_zone=UTC

# Connection Pool Settings (Production)
spring.datasource.hikari.maximum-pool-size=10
spring.datasource.hikari.minimum-idle=5
spring.datasource.hikari.connection-timeout=30000
spring.datasource.hikari.idle-timeout=600000
spring.datasource.hikari.max-lifetime=1800000

# Logging
logging.level.org.hibernate.SQL=WARN
logging.level.org.hibernate.type.descriptor.sql.BasicBinder=WARN
```

### 3.3 メイン設定ファイル更新

`src/main/resources/application.properties`:

```properties
spring.application.name=demo
spring.profiles.active=dev
```

---

## ステップ4: データベースマイグレーション戦略の決定

### オプションA: Flyway（推奨）

#### 4.1 依存関係追加

`pom.xml`に以下を追加:

```xml
<!-- Flyway for Database Migration -->
<dependency>
    <groupId>org.flywaydb</groupId>
    <artifactId>flyway-core</artifactId>
</dependency>
<dependency>
    <groupId>org.flywaydb</groupId>
    <artifactId>flyway-database-postgresql</artifactId>
</dependency>
```

#### 4.2 Flyway設定

`application-dev.properties`に追加:

```properties
# Flyway Configuration
spring.flyway.enabled=true
spring.flyway.locations=classpath:db/migration
spring.flyway.baseline-on-migrate=true
```

#### 4.3 マイグレーションディレクトリ作成

```bash
mkdir -p src/main/resources/db/migration
```

### オプションB: Liquibase

より複雑なマイグレーション管理が必要な場合は、Liquibaseの使用を検討してください。

---

## ステップ5: スキーマ初期化とテーブル作成

### 5.1 Employeesテーブル作成

`src/main/resources/db/migration/V1__create_employees_table.sql`:

```sql
-- Employees Table
CREATE TABLE IF NOT EXISTS employees (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    email VARCHAR(255) NOT NULL UNIQUE,
    status VARCHAR(20) NOT NULL DEFAULT 'ACTIVE',
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- Indexes for performance
CREATE INDEX idx_employees_status ON employees(status);
CREATE INDEX idx_employees_email ON employees(email);

-- Comments
COMMENT ON TABLE employees IS '従業員テーブル';
COMMENT ON COLUMN employees.id IS '従業員ID';
COMMENT ON COLUMN employees.name IS '従業員名';
COMMENT ON COLUMN employees.email IS 'メールアドレス';
COMMENT ON COLUMN employees.status IS 'ステータス (ACTIVE, INACTIVE, DELETED)';
```

### 5.2 Filmsテーブル作成

`src/main/resources/db/migration/V2__create_films_table.sql`:

```sql
-- Films Table
CREATE TABLE IF NOT EXISTS films (
    id BIGSERIAL PRIMARY KEY,
    title VARCHAR(255) NOT NULL UNIQUE,
    description TEXT,
    release_year INTEGER,
    genre VARCHAR(50),
    rating VARCHAR(10),
    status VARCHAR(20) NOT NULL DEFAULT 'ACTIVE',
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- Indexes for performance
CREATE INDEX idx_films_title ON films(title);
CREATE INDEX idx_films_status ON films(status);
CREATE INDEX idx_films_genre ON films(genre);
CREATE INDEX idx_films_release_year ON films(release_year);

-- Comments
COMMENT ON TABLE films IS '映画テーブル';
COMMENT ON COLUMN films.id IS '映画ID';
COMMENT ON COLUMN films.title IS 'タイトル';
COMMENT ON COLUMN films.description IS '説明';
COMMENT ON COLUMN films.release_year IS '公開年';
COMMENT ON COLUMN films.genre IS 'ジャンル';
COMMENT ON COLUMN films.rating IS 'レーティング (G, PG, PG13, R, NC17)';
COMMENT ON COLUMN films.status IS 'ステータス (ACTIVE, INACTIVE, DELETED)';
```

---

## ステップ6: 接続テストと動作確認

### 6.1 アプリケーション起動

```bash
# 開発環境で起動
./mvnw spring-boot:run -Dspring-boot.run.profiles=dev
```

### 6.2 起動ログ確認

以下のログが表示されることを確認:

```
Flyway Community Edition ... by Redgate
Database: jdbc:postgresql://...
Successfully validated 2 migrations
...
Migrating schema "public" to version "1 - create employees table"
Migrating schema "public" to version "2 - create films table"
Successfully applied 2 migrations
```

### 6.3 APIテスト

#### 従業員API

```bash
# 従業員作成
curl -X POST http://localhost:8080/api/employees \
  -H "Content-Type: application/json" \
  -d '{
    "name": "山田太郎",
    "email": "yamada@example.com"
  }'

# 従業員一覧取得
curl http://localhost:8080/api/employees

# 従業員詳細取得
curl http://localhost:8080/api/employees/1
```

#### 映画API

```bash
# 映画作成
curl -X POST http://localhost:8080/api/films \
  -H "Content-Type: application/json" \
  -d '{
    "title": "スプリングブートの冒険",
    "description": "Spring Bootでアプリケーションを開発する物語",
    "releaseYear": 2024,
    "genre": "Documentary",
    "rating": "G"
  }'

# 映画一覧取得
curl http://localhost:8080/api/films

# 映画詳細取得
curl http://localhost:8080/api/films/1
```

---

## ステップ7: 本番運用設定

### 7.1 SSL設定の確認

NeonDBは**SSL接続が必須**です。接続文字列に`sslmode=require`が含まれていることを確認してください。

```properties
spring.datasource.url=...?sslmode=require
```

### 7.2 接続プール最適化

NeonDBのサーバーレス特性を考慮した設定:

```properties
# 本番環境推奨設定
spring.datasource.hikari.maximum-pool-size=10
spring.datasource.hikari.minimum-idle=5
spring.datasource.hikari.connection-timeout=30000
spring.datasource.hikari.idle-timeout=600000
spring.datasource.hikari.max-lifetime=1800000
spring.datasource.hikari.leak-detection-threshold=60000
```

### 7.3 タイムゾーン設定

```properties
# UTCで統一（推奨）
spring.jpa.properties.hibernate.jdbc.time_zone=UTC
```

### 7.4 本番環境のddl-auto設定

```properties
# 本番環境では必ずvalidateを使用
spring.jpa.hibernate.ddl-auto=validate
```

**重要**: 本番環境では`update`や`create-drop`を使用しないでください。スキーマ変更はFlywayで管理します。

### 7.5 .gitignore更新

```gitignore
# Environment files
.env
.env.local
.env.*.local

# Spring Boot profiles
application-dev.properties
application-prod.properties
```

---

## 追加の推奨事項

### バックアップ戦略

1. **自動バックアップ**
   - NeonDBダッシュボードで自動バックアップを有効化
   - デフォルトで7日間のバックアップが保持される

2. **手動バックアップ**
   - 重要なマイグレーション前は必ず手動バックアップを作成
   - NeonDBの「Branches」機能を活用

### モニタリング

1. **NeonDBダッシュボード**
   - クエリパフォーマンスの監視
   - 接続数の監視
   - ストレージ使用量の確認

2. **Spring Boot Actuator**

   `pom.xml`に追加:
   ```xml
   <dependency>
       <groupId>org.springframework.boot</groupId>
       <artifactId>spring-boot-starter-actuator</artifactId>
   </dependency>
   ```

   設定:
   ```properties
   management.endpoints.web.exposure.include=health,metrics,info
   management.endpoint.health.show-details=always
   ```

### セキュリティベストプラクティス

1. **環境変数管理**
   - 開発: `.env`ファイル
   - 本番: 環境変数、AWS Secrets Manager、または Azure Key Vault

2. **接続情報の保護**
   - データベース接続情報をコードにハードコーディングしない
   - `.env`ファイルをバージョン管理に含めない
   - CI/CDパイプラインでは環境変数として設定

3. **最小権限の原則**
   - アプリケーション用のデータベースユーザーは必要最小限の権限のみ付与
   - 管理者権限とアプリケーション権限を分離

### パフォーマンス最適化

1. **インデックスの適切な設定**
   - 頻繁に検索されるカラムにインデックスを作成
   - WHERE句、JOIN、ORDER BYで使用されるカラム

2. **クエリの最適化**
   - N+1問題を避けるためにFetchJoinを活用
   - 必要なカラムのみを取得

3. **接続プールのチューニング**
   - アプリケーションの負荷に応じて調整
   - NeonDBの同時接続数制限を考慮

---

## トラブルシューティング

### 接続エラー

**エラー**: `Connection refused` または `Connection timeout`

**解決策**:
1. NeonDBダッシュボードでデータベースが起動しているか確認
2. ファイアウォール設定を確認
3. 接続文字列が正しいか確認

### SSL接続エラー

**エラー**: `SSL error` または `The server does not support SSL`

**解決策**:
1. 接続文字列に`sslmode=require`が含まれているか確認
2. PostgreSQLドライバーが最新版か確認

### Flywayマイグレーションエラー

**エラー**: `Validate failed: Migration checksum mismatch`

**解決策**:
1. マイグレーションファイルを変更しない（新しいマイグレーションを追加）
2. 開発環境でデータベースをクリーンアップして再実行

---

## まとめ

このロードマップに従うことで、Spring BootアプリケーションをNeonDBに正常に移行できます。

### チェックリスト

- [ ] NeonDBアカウント作成
- [ ] データベース作成
- [ ] 接続情報取得
- [ ] `.env`ファイル作成と`.gitignore`追加
- [ ] `application-dev.properties`作成
- [ ] `application-prod.properties`作成
- [ ] Flyway依存関係追加
- [ ] マイグレーションファイル作成
- [ ] アプリケーション起動テスト
- [ ] APIテスト実施
- [ ] 本番環境設定確認

---

## 参考リンク

- [NeonDB公式ドキュメント](https://neon.tech/docs)
- [Spring Boot Data JPA](https://spring.io/guides/gs/accessing-data-jpa/)
- [Flyway Documentation](https://flywaydb.org/documentation/)
- [HikariCP Configuration](https://github.com/brettwooldridge/HikariCP#configuration-knobs-baby)
