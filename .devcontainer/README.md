# Spring Boot 開発環境（DevContainer）ガイド

このプロジェクトは **VS Code DevContainer** を利用して、Spring Boot の開発環境を Docker 上で構築できるようにしたものです。
Java / Maven / Node などの依存関係をコンテナ内で完結させることで、環境差分なく快適に開発できます。

---

## 📦 使用技術 / DevContainer 構成

本プロジェクトの `.devcontainer/devcontainer.json` は以下の機能を提供します。

### **🔧 開発環境（DevContainer 内）**

* **Java 17**（devcontainers Java Feature 使用）
* **Maven 3.9**（devcontainers Maven Feature v2）
* **Node.js 最新版**（devcontainers Node Feature v2）
* **Git（ベースイメージに標準で含まれるため Feature は未使用）**

### **🛠 DevContainer の特徴**

* ホスト環境に Java や Maven をインストールする必要なし
* Docker の中で VS Code から直接開発可能
* UTF-8 や Java の各種ツール設定をコンテナに固定し再現性が高い
* VS Code 拡張（Java Pack / Spring Boot Extension Pack など）と完全連携

---

## 🚀 DevContainer の使い方

### **1. VS Code でプロジェクトを開く**

```
VSCode → "Open Folder" → 本プロジェクトのフォルダを選択
```

### **2. DevContainer を起動する**

VS Code の左下「><」アイコン →

```
Reopen in Container
```

を選択します。

問題なくセットアップされれば、コンテナ内へ接続されます。

---

## 🏗 プロジェクトの作成方法（Spring Initializr / 推奨）

コンテナ内に入り、Spring Boot プロジェクトを新規作成する方法です。
VS Code 標準の Spring Initializr 生成機能を利用します。

### **① コマンドパレットを開く**

```
Ctrl + Shift + P
```

### **② コマンド検索欄に入力**

```
Spring Initializr
```

### **③ 以下を順番に選択**

1. **Spring Initializr: Create a Maven Project**
2. **Specify Spring Boot version** → `3.5.8`
3. **Specify project language** → `Java`
4. **Input Group Id for your project**  
   （例：`com.example`）
5. **Input Artifact Id for your project**  
   （例：`sample-app`）
6. **Input Package Name for your project**  
   （例：`com.example.sample`）
7. **Specify packaging type** → `Jar`
8. **Specify Java Version** → `17`
9. **依存関係を選択（例）**

   - **Spring Web**  
     REST API や Web アプリケーションを作成するための基本スターター。  
     `@Controller` / `@RestController` / `@RequestMapping` などを使用可能。

   - **Lombok**  
     Getter / Setter / コンストラクタなどのボイラープレートコードを  
     アノテーションで自動生成し、Java コード量を削減するライブラリ。

   - **Spring Data JPA**  
     RDB（PostgreSQL / MySQL / H2 など）を対象に、  
     ORM（Object Relational Mapping）を利用したデータアクセスを実現する仕組み。  
     Repository インターフェース定義だけで CRUD 処理を実装可能。

   - **Validation**  
     入力値チェック（バリデーション）を **宣言的に記述**するための仕組み。  
     `@NotNull`、`@NotBlank`、`@Size` などのアノテーションを DTO に付与することで、  
     Controller で `@Valid` を指定した際に Spring が自動的に入力値を検証し、  
     不正なリクエストは **400 Bad Request** として処理される。

10. 保存先を **このプロジェクトフォルダ内** に指定して生成


### **④ プロジェクトが生成されると自動的に `src/main/java` が現れます**

VS Code が Java プロジェクトとして認識し、コンテナ内でビルド・実行が可能になります。

---

## ▶ Spring Boot アプリの起動

コンテナのターミナルで以下を実行：

```
mvn spring-boot:run
```

アプリが起動するとポート **8080** が自動的にホストへフォワードされます。

---

## 📁 フォルダ構成（プロジェクト生成後の想定）

```
project-root/
 ├ .devcontainer/
 │   ├ devcontainer.json
 │   └ Dockerfile
 ├ src/
 │   ├ main/java/...（Spring Boot のコード）
 │   └ test/java/...（テスト）
 ├ pom.xml
 └ README.md
```

---

## ✨ 補足

* Git Feature は不要（Ubuntu ベースイメージに Git が標準で入っているため）
* DevContainer によって環境差分が無くなるため、チーム開発にも最適
* Node.js が搭載されているため、API＋フロントエンド構成にも対応


