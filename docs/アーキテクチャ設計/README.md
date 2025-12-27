# プロジェクト アーキテクチャ設計書
**Spring Boot / Clean Architecture / DDD**

---

## 📖 プロジェクト概要

本プロジェクトは、**Spring Boot をベースにした Web アプリケーションのバックエンド**である。

ドメイン駆動設計（DDD）とクリーンアーキテクチャの思想に基づき、以下を実現することを目的とする：

### 🎯 アーキテクチャの目的

✅ **変更に強い**
- UIの変更がビジネスロジックに影響しない
- データベースの変更がドメインに影響しない
- フレームワークの変更が最小限の影響で済む

✅ **機能追加に強い**
- 新機能の追加が既存機能に影響しにくい
- 各レイヤーが独立して拡張できる
- 明確な責務分離により並行開発が容易

✅ **テストしやすい**
- ビジネスロジックを単体で（モックなしで）テスト可能
- 各レイヤーを独立してテストできる
- 依存関係が明確で、テストの準備が簡単

### 🏗️ アーキテクチャの特徴

本設計では、以下の原則に従う：

1. **依存関係の方向制御**
   - 外側のレイヤーは内側のレイヤーに依存する
   - 内側のレイヤーは外側のレイヤーに依存しない
   - ビジネスロジック（domain）が中心であり、何にも依存しない

2. **関心の分離**
   - domain: ビジネスルール
   - application: ユースケース
   - presentation: 入出力インターフェース
   - infrastructure: 技術的実装

3. **抽象への依存**
   - 具体的な実装ではなく、インターフェース（抽象）に依存
   - 依存性逆転の原則（DIP）による柔軟な設計

---

## 📁 全体構成

```
src/
└ main/
  ├ java/
  │   └ com.example.demo/
  │        ├ domain/                        ← ドメインモデル（ビジネスルール）
  │        │    └ {aggregate}/              ← 集約ごとにフォルダ分け
  │        │        ├ {Aggregate}.java          ← 集約ルート（JPAアノテーションなし）
  │        │        ├ {Aggregate}Id.java        ← 値オブジェクト
  │        │        ├ {Aggregate}Repository.java ← リポジトリインターフェース
  │        │        ├ {Aggregate}DomainService.java ← ドメインサービス（オプション）
  │        │        └ ...                        ← その他の値オブジェクトなど
  │        │
  │        ├ application/                   ← ユースケース層
  │        │    └ usecase/
  │        │         └ {aggregate}/         ← 集約ごとにフォルダ分け
  │        │              ├ Create{Aggregate}UseCase.java
  │        │              ├ Update{Aggregate}UseCase.java
  │        │              └ ...
  │        │
  │        ├ infrastructure/                ← インフラストラクチャ層
  │        │    ├ persistence/              ← 永続化実装
  │        │    │    ├ entity/              ← JPAエンティティ
  │        │    │    │    └ {Aggregate}JpaEntity.java
  │        │    │    ├ mapper/              ← Domain ⇔ JPA Entity 変換
  │        │    │    │    └ {Aggregate}Mapper.java
  │        │    │    └ repository/          ← Repository 実装
  │        │    │         ├ {Aggregate}JpaRepository.java  ← Spring Data JPA
  │        │    │         └ {Aggregate}RepositoryImpl.java ← ドメインIF実装
  │        │    └ config/                   ← 設定ファイル
  │        │
  │        ├ presentation/                  ← プレゼンテーション層
  │        │    ├ controller/
  │        │    │    └ {aggregate}/
  │        │    │         └ {Aggregate}Controller.java
  │        │    └ dto/
  │        │         └ {aggregate}/
  │        │              ├ Create{Aggregate}Request.java
  │        │              ├ Update{Aggregate}Request.java
  │        │              └ {Aggregate}Response.java
  │        │
  │        └ DemoApplication.java           ← エントリーポイント
  │
  └ resources/
      ├ application.properties              ← 共通設定
      ├ application-dev.properties          ← 開発環境設定
      └ application-prod.properties         ← 本番環境設定
```

---

## 🧩 レイヤーごとの役割

### 1. domain（ドメイン層）

ビジネスルールを表す最重要レイヤー。  
アプリケーションやインフラに依存しない「純粋なロジック」。

#### 含むもの
- **Aggregate（集約）**: ビジネスルールの境界
- **Entity**: 一意性を持つドメインオブジェクト
- **Value Object**: 値で識別されるオブジェクト
- **Domain Service**: 複数のエンティティにまたがるロジック
- **Domain Event**: ドメイン内で発生するイベント
- **Repository Interface**: データ永続化の抽象（実装は infrastructure）

#### 構成例（実装済み）
```
domain/
  ├ user/
  │   ├ User.java                   ← 集約ルート（JPAアノテーションなし）
  │   ├ UserId.java                 ← 値オブジェクト
  │   ├ UserName.java               ← 値オブジェクト
  │   ├ Email.java                  ← 値オブジェクト
  │   ├ UserStatus.java             ← 列挙型
  │   ├ UserRepository.java         ← インターフェース（永続化の抽象）
  │   └ UserDomainService.java      ← ドメインサービス（オプション）
  ├ film/
  │   ├ Film.java                   ← 集約ルート（JPAアノテーションなし）
  │   ├ FilmId.java                 ← 値オブジェクト
  │   ├ FilmTitle.java              ← 値オブジェクト
  │   ├ FilmDescription.java        ← 値オブジェクト
  │   ├ FilmReleaseYear.java        ← 値オブジェクト
  │   └ FilmRepository.java         ← インターフェース（永続化の抽象）
  └ order/
      ├ Order.java                  ← 集約ルート（JPAアノテーションなし）
      ├ OrderId.java                ← 値オブジェクト
      ├ OrderItem.java              ← エンティティ
      ├ OrderRepository.java        ← インターフェース（永続化の抽象）
      └ OrderDomainService.java     ← ドメインサービス（オプション）
```

#### 重要な原則
- **他のレイヤーに依存しない** - 完全に独立
- **ビジネスルールのみを記述** - UIやDBの知識を持たない
- **不変性を保つ** - Value Objectは不変
- **集約の境界を守る** - 集約ルートからのみアクセス

---

### 2. application（アプリケーション層）

ユースケースを実現するレイヤー。  
ドメインを組み合わせてアプリケーション固有の処理を行う。

#### 含むもの
- **UseCase / Service**: アプリケーションのユースケース実装
- **Application DTO**: 入力/出力データの転送オブジェクト
  - Input DTO: ユースケースへの入力
  - Output DTO: ユースケースからの出力
- **Application Service**: 複数のユースケースをまとめる

#### 構成例（実装済み）
```
application/
  └ usecase/
      └ user/
          ├ CreateUserUseCase.java      ← ユーザー作成
          ├ GetUserUseCase.java         ← ユーザー取得
          ├ ListUsersUseCase.java       ← ユーザー一覧取得
          ├ UpdateUserUseCase.java      ← ユーザー更新
          └ DeleteUserUseCase.java      ← ユーザー削除
```

#### 特徴
- **ドメインに依存する** - ドメインモデルを利用
- **presentation/infrastructure には依存しない**
- **トランザクション境界を定義** - @Transactional
- **DTO変換を行う** - Domain ⇔ DTO

#### 実装例（実装済み）
```java
@Service
@Transactional
public class CreateUserUseCase {
    private final UserRepository userRepository;

    public CreateUserUseCase(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User execute(String name, String email) {
        // 1. Value Objectの生成（バリデーション含む）
        UserName userName = new UserName(name);
        Email userEmail = new Email(email);

        // 2. ドメインルールのチェック
        if (userRepository.existsByEmail(userEmail)) {
            throw new IllegalArgumentException("メールアドレスは既に使用されています: " + email);
        }

        // 3. エンティティの生成
        User user = User.create(userName, userEmail);

        // 4. 永続化
        return userRepository.save(user);
    }
}
```

---

### 3. presentation（API層）

コントローラ層。外部との入り口。

#### 含むもの
- **REST Controller**: RESTful APIエンドポイント
- **Request DTO**: APIリクエストボディ受け取り用
- **Response DTO**: APIレスポンス返却用

#### 構成例（実装済み）
```
presentation/
  ├ controller/
  │   └ user/
  │       └ UserController.java         ← REST API
  └ dto/
      └ user/
          ├ CreateUserRequest.java      ← リクエストDTO
          ├ UpdateUserRequest.java      ← リクエストDTO
          └ UserResponse.java           ← レスポンスDTO
```

#### データフロー
```
外部クライアント
    ↓ (JSON)
Request DTO (presentation)
    ↓ (変換)
Input DTO (application)
    ↓ (UseCase実行)
Output DTO (application)
    ↓ (変換)
Response DTO (presentation)
    ↓ (JSON)
外部クライアント
```

#### 実装例
```java
@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {
    private final CreateOrderUseCase createOrderUseCase;
    
    @PostMapping
    public ResponseEntity<OrderResponse> createOrder(
        @RequestBody @Valid CreateOrderRequest request
    ) {
        // 1. Request DTO → Input DTO
        OrderInputDTO input = request.toInputDTO();
        
        // 2. UseCase実行
        OrderOutputDTO output = createOrderUseCase.execute(input);
        
        // 3. Output DTO → Response DTO
        OrderResponse response = OrderResponse.from(output);
        
        return ResponseEntity.ok(response);
    }
}
```

#### ポイント
- **バリデーション**: Request DTOで実施（@Valid, @NotNull等）
- **エラーハンドリング**: GlobalExceptionHandlerで一元管理
- **認証/認可**: この層で実施（Spring Security）
- **UI固有の形式**: camelCase、表示形式の変換など

---

### 4. infrastructure（インフラ層）

DB・外部API・ファイルなどの実装層。

#### 含むもの
- **JPA Entity**: データベーステーブルと対応
- **Repository 実装**: JPAを使った永続化実装
- **外部APIクライアント**: RestTemplateやWebClient
- **設定ファイル**: DataSource、JPA設定など

#### 構成例（実装済み）
```
infrastructure/
  └ persistence/
      ├ entity/
      │   └ UserJpaEntity.java              ← @Entity（永続化専用）
      ├ mapper/
      │   └ UserMapper.java                 ← Domain ⇔ JPA Entity 変換
      └ repository/
          ├ UserJpaRepository.java          ← Spring Data JPA（package-private）
          └ UserRepositoryImpl.java         ← UserRepository実装
```

#### Repository実装パターン（実装済み）
```java
// domain層のインターフェース
public interface UserRepository {
    User save(User user);
    void delete(User user);
    Optional<User> findById(UserId id);
    Optional<User> findByEmail(Email email);
    List<User> findActiveUsers();
    List<User> findAll();
    boolean existsByEmail(Email email);
}

// infrastructure層の実装
@Repository
public class UserRepositoryImpl implements UserRepository {
    private final UserJpaRepository jpaRepository;
    private final UserMapper mapper;

    public UserRepositoryImpl(UserJpaRepository jpaRepository, UserMapper mapper) {
        this.jpaRepository = jpaRepository;
        this.mapper = mapper;
    }

    @Override
    public User save(User user) {
        // Domain → JPA Entity変換
        UserJpaEntity entity = mapper.toEntity(user);
        UserJpaEntity saved = jpaRepository.save(entity);
        // JPA Entity → Domain変換
        return mapper.toDomain(saved);
    }

    @Override
    public Optional<User> findById(UserId id) {
        return jpaRepository.findById(id.getValue())
            .map(mapper::toDomain);
    }
}

// Spring Data JPA（package-privateで外部から直接使用されないようにする）
interface UserJpaRepository extends JpaRepository<UserJpaEntity, Long> {
    Optional<UserJpaEntity> findByEmail(String email);
    List<UserJpaEntity> findByStatus(String status);
    boolean existsByEmail(String email);
}
```

#### 特徴
- **domain の Repository インターフェースを実装**
- **ドメインモデルとJPAエンティティの変換を担当**
- **外部システムとの通信を隠蔽**
- **技術的詳細を閉じ込める**

---

### 5. shared（共通モジュール）

横断的な共通関心事を扱う。

#### 含むもの
- **exception**: カスタム例外、GlobalExceptionHandler
- **security**: セッション認証・認可、Spring Security設定
- **config**: Bean定義、CORS設定など

#### 構成例
```
shared/
  ├ exception/
  │   ├ BusinessException.java
  │   ├ NotFoundException.java
  │   └ GlobalExceptionHandler.java
  ├ security/
  │   ├ SecurityConfig.java
  │   ├ SessionAuthenticationFilter.java
  │   └ UserDetailsServiceImpl.java
  └ config/
      ├ WebConfig.java
      └ AsyncConfig.java
```

---

## 🧱 Aggregate（集約）の考え方

### フォルダ命名 `{aggregate}/` の意味

`domain/model/{aggregate}/` は**集約ごとにフォルダ分けする**という意味。

#### 集約とは？
- ビジネスルールの境界を表す単位
- 一貫性を保つべき関連オブジェクトのまとまり
- 外部から集約ルート（Aggregate Root）を通してのみアクセス

#### 例: 注文集約
```
domain/model/order/
  ├ Order.java              ← 集約ルート
  ├ OrderId.java            ← Value Object
  ├ OrderStatus.java        ← Enum
  ├ OrderItem.java          ← Entity（集約内部）
  └ OrderItemId.java        ← Value Object
```

#### 集約の境界設計
```
注文集約（Order Aggregate）
  - Order（ルート）
  - OrderItem（内部エンティティ）
  - OrderStatus（値オブジェクト）

ユーザー集約（User Aggregate）
  - User（ルート）
  - UserId（値オブジェクト）
  - UserProfile（値オブジェクト）

商品集約（Product Aggregate）
  - Product（ルート）
  - ProductId（値オブジェクト）
  - Price（値オブジェクト）
```

#### 集約間の参照
- **IDによる参照**: 他の集約は IDのみで参照
- **直接参照しない**: Order → User は UserId で参照

```java
// ✅ 良い例: IDで参照
public class Order {
    private OrderId id;
    private UserId userId;  // IDのみ保持
    // ...
}

// ❌ 悪い例: 直接参照
public class Order {
    private OrderId id;
    private User user;  // Userオブジェクト全体を保持
    // ...
}
```

---

## 🧳 DTOの役割と必要性

### なぜDTOが複数必要か？

| 層 | DTO名 | 目的 | 主な役割 |
|---|---|---|---|
| **presentation** | Request/Response DTO | API入出力の型 | JSON ⇔ DTO変換、バリデーション |
| **application** | Input/Output DTO | ユースケースの入出力 | ビジネス的意味を持つデータ |
| **domain** | Entity/Value Object | ビジネスルールの中心 | 不変性・整合性・集約ルール |

### DTOの変換フロー

```
クライアント
    ↓
CreateOrderRequest (presentation)
    ├ フィールド: camelCase
    ├ バリデーション: @NotNull, @Size
    └ 役割: JSON受け取り、入力検証
    ↓ toInputDTO()
OrderInputDTO (application)
    ├ フィールド: ビジネス用語
    ├ 必要最小限のデータ
    └ 役割: ユースケースへの入力
    ↓ UseCase実行
Domain Model (Order, OrderItem)
    ├ ビジネスルール
    ├ 不変条件の保証
    └ 役割: ビジネスロジックの実行
    ↓ toDomain() / from()
OrderOutputDTO (application)
    ├ ユースケース結果
    └ 役割: ユースケースからの出力
    ↓ from()
OrderResponse (presentation)
    ├ フィールド: camelCase
    ├ 表示用フォーマット
    └ 役割: JSON返却
    ↓
クライアント
```

### 具体例

```java
// presentation層: Request DTO
@Data
public class CreateOrderRequest {
    @NotNull
    private String userId;
    
    @NotEmpty
    private List<OrderItemRequest> items;
    
    public OrderInputDTO toInputDTO() {
        return new OrderInputDTO(
            new UserId(UUID.fromString(userId)),
            items.stream()
                .map(OrderItemRequest::toInputDTO)
                .collect(Collectors.toList())
        );
    }
}

// application層: Input DTO
@Value
public class OrderInputDTO {
    UserId userId;
    List<OrderItemInputDTO> items;
}

// domain層: Entity
public class Order {
    private OrderId id;
    private UserId userId;
    private List<OrderItem> items;
    private OrderStatus status;
    
    // ビジネスルール
    public void validate() {
        if (items.isEmpty()) {
            throw new InvalidOrderException("注文には1つ以上の商品が必要");
        }
        // その他のビジネスルール...
    }
}

// application層: Output DTO
@Value
public class OrderOutputDTO {
    String orderId;
    String userId;
    List<OrderItemOutputDTO> items;
    String status;
    LocalDateTime createdAt;
    
    public static OrderOutputDTO from(Order order) {
        return new OrderOutputDTO(
            order.getId().getValue().toString(),
            order.getUserId().getValue().toString(),
            // ...
        );
    }
}

// presentation層: Response DTO
@Data
public class OrderResponse {
    private String orderId;
    private String userId;
    private List<OrderItemResponse> items;
    private String status;
    private String createdAt;  // ISO-8601形式
    
    public static OrderResponse from(OrderOutputDTO dto) {
        OrderResponse response = new OrderResponse();
        response.setOrderId(dto.getOrderId());
        // ...
        return response;
    }
}
```

### DTOを分ける理由

#### ✅ メリット
1. **責務の分離**: 各層が独立して変更可能
2. **変更の影響範囲を限定**: UIの変更がドメインに影響しない
3. **ドメインモデルの純粋性**: UIの都合に汚染されない
4. **テスタビリティ**: 各層を独立してテスト可能
5. **再利用性**: ドメインモデルを複数のUIで共有可能

#### ❌ 1つのDTOで済ませた場合の問題
```java
// アンチパターン: 全てを1つのDTOで
@Entity
@Data
public class Order {
    @Id
    private UUID id;
    
    // JPA用のアノテーション
    @ManyToOne
    private User user;
    
    // JSON用のアノテーション
    @JsonProperty("order_id")
    private String orderId;
    
    // バリデーション用のアノテーション
    @NotNull
    private String status;
    
    // ビジネスロジックが書きにくい
    // UI変更でドメインが影響を受ける
    // テストが複雑になる
}
```

### 開発コストの真実

**短期的**: DTO変換のコードが増える → 一見コスト増

**長期的**: 
- 変更が局所化され影響範囲が小さい
- バグの混入が減る
- チーム開発で並行作業がしやすい
- 結果として **総コストは激減**

これがクリーンアーキテクチャとDDDの本質。

---

## 📌 依存関係のルール

### 依存の方向

```
presentation
    ↓ 依存OK
application
    ↓ 依存OK
domain ← これが中心（何にも依存しない）
    ↑ 実装
infrastructure
```

### 原則
1. **外側のレイヤーは内側に依存できる**
   - presentation → application → domain はOK

2. **内側のレイヤーは外側に依存してはいけない**
   - domain → application はNG
   - domain → infrastructure はNG

3. **infrastructure は domain のインターフェースに依存**
   - Dependency Inversion Principle（依存性逆転の原則）

### 実装例: 依存性の逆転

```java
// domain層: インターフェース定義（抽象に依存）
package com.example.domain.repository;

public interface OrderRepository {
    Order save(Order order);
    Optional<Order> findById(OrderId id);
}

// domain層: ドメインサービス（インターフェースに依存）
package com.example.domain.service;

@RequiredArgsConstructor
public class OrderDomainService {
    private final OrderRepository orderRepository;  // インターフェース
    
    public Order createOrder(/*...*/) {
        // ビジネスロジック
        return orderRepository.save(order);
    }
}

// infrastructure層: 実装（インターフェースを実装）
package com.example.infrastructure.repository;

@Repository
public class OrderJpaRepository implements OrderRepository {
    // JPA実装の詳細
}
```

この構造により:
- **domainは具体的な実装を知らない**
- **infrastructureがdomainのインターフェースを実装**
- **依存の方向が逆転**（High-level → Low-level ではなく、両方が抽象に依存）

---

## 🎯 アーキテクチャが守るもの

### 1. ドメインロジックの純粋性
- ビジネスルールがフレームワークやライブラリに依存しない
- テストが容易（モックなしでドメインロジックをテスト可能）

### 2. 変更の影響範囲の最小化
- UIの変更がドメインに影響しない
- DBの変更がビジネスロジックに影響しない
- 各レイヤーが独立して進化できる

### 3. 技術的選択の柔軟性
- JPAからMyBatisへの移行が容易
- セッション認証からJWT認証への移行が容易
- RDBからNoSQLへの移行が容易

### 4. 大規模開発への対応
- チーム間の並行開発がしやすい
- 責務が明確で新メンバーの理解が早い
- コードの再利用性が高い

---

## 📚 実装時のチェックリスト

### Domain層
- [ ] 他のレイヤーへの依存がないか？
- [ ] ビジネスルールが適切に表現されているか？
- [ ] 集約の境界は適切か？
- [ ] Value Objectは不変か？
- [ ] Repository はインターフェースのみか？

### Application層
- [ ] ドメインロジックを含んでいないか？
- [ ] トランザクション境界は適切か？
- [ ] DTO変換を行っているか？
- [ ] presentation/infrastructure に依存していないか？

### Presentation層
- [ ] ビジネスロジックを含んでいないか？
- [ ] バリデーションは実施しているか？
- [ ] 適切にDTO変換を行っているか？
- [ ] エラーハンドリングは適切か？

### Infrastructure層
- [ ] domain のインターフェースを実装しているか？
- [ ] ドメインモデルとエンティティの変換は適切か？
- [ ] 技術的詳細が漏れていないか？

---

## 🔐 セッション認証の実装

### Spring Security設定

```java
@Configuration
@EnableWebSecurity
public class SecurityConfig {
    
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf
                .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
            )
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/api/auth/login", "/api/auth/register").permitAll()
                .requestMatchers("/api/**").authenticated()
                .anyRequest().permitAll()
            )
            .sessionManagement(session -> session
                .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
                .maximumSessions(1)
                .maxSessionsPreventsLogin(false)
            )
            .formLogin(form -> form.disable())
            .httpBasic(basic -> basic.disable());
        
        return http.build();
    }
    
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
```

### ログインコントローラー

```java
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthenticationManager authenticationManager;
    private final LoginUseCase loginUseCase;
    
    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(
        @RequestBody @Valid LoginRequest request,
        HttpServletRequest httpRequest
    ) {
        // 認証実行
        Authentication authentication = authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(
                request.getUsername(),
                request.getPassword()
            )
        );
        
        // セキュリティコンテキストに設定
        SecurityContextHolder.getContext().setAuthentication(authentication);
        
        // セッション作成
        HttpSession session = httpRequest.getSession(true);
        session.setAttribute(
            HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY,
            SecurityContextHolder.getContext()
        );
        
        // ユーザー情報取得
        LoginOutputDTO output = loginUseCase.execute(request.toInputDTO());
        
        return ResponseEntity.ok(LoginResponse.from(output));
    }
    
    @PostMapping("/logout")
    public ResponseEntity<Void> logout(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }
        SecurityContextHolder.clearContext();
        
        return ResponseEntity.ok().build();
    }
}
```

### UserDetailsService実装

```java
@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {
    private final UserRepository userRepository;
    
    @Override
    public UserDetails loadUserByUsername(String username) 
            throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username)
            .orElseThrow(() -> new UsernameNotFoundException(
                "User not found: " + username
            ));
        
        return org.springframework.security.core.userdetails.User
            .withUsername(user.getUsername())
            .password(user.getPassword())
            .roles(user.getRoles().toArray(new String[0]))
            .build();
    }
}
```

### application.yml設定

```yaml
server:
  servlet:
    session:
      timeout: 30m  # セッションタイムアウト
      cookie:
        http-only: true
        secure: true  # HTTPS環境ではtrue
        same-site: strict
        name: SESSIONID

spring:
  session:
    store-type: redis  # または jdbc, hazelcast等
  
  # Redis使用の場合
  redis:
    host: localhost
    port: 6379
    password: ${REDIS_PASSWORD}
```

### セッション情報の取得

```java
@RestController
@RequestMapping("/api/users")
public class UserController {
    
    @GetMapping("/me")
    public ResponseEntity<UserResponse> getCurrentUser(
        @AuthenticationPrincipal UserDetails userDetails
    ) {
        // 現在ログイン中のユーザー情報取得
        String username = userDetails.getUsername();
        // ...
        return ResponseEntity.ok(response);
    }
}
```

### セッションストレージの選択肢

#### 1. インメモリ（開発環境）
```yaml
spring:
  session:
    store-type: none  # デフォルト
```

#### 2. Redis（推奨）
```yaml
spring:
  session:
    store-type: redis
  redis:
    host: localhost
    port: 6379
```

依存関係：
```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-data-redis</artifactId>
</dependency>
<dependency>
    <groupId>org.springframework.session</groupId>
    <artifactId>spring-session-data-redis</artifactId>
</dependency>
```

#### 3. JDBC（RDB）
```yaml
spring:
  session:
    store-type: jdbc
    jdbc:
      initialize-schema: always
```

依存関係：
```xml
<dependency>
    <groupId>org.springframework.session</groupId>
    <artifactId>spring-session-jdbc</artifactId>
</dependency>
```

### セッション管理のベストプラクティス

1. **タイムアウトの設定**: 適切なセッションタイムアウトを設定
2. **HTTPS使用**: 本番環境ではHTTPSを使用
3. **CSRF対策**: CSRFトークンを有効化
4. **セッション固定攻撃対策**: ログイン時にセッションIDを再生成
5. **分散環境対応**: RedisやJDBCでセッション共有

---

## 🔧 補足: JPA実装の詳細

### Domain Model と JPA Entity の分離（実装済み）

```java
// domain層: ドメインモデル（純粋なJava、JPAアノテーションなし）
public class User {
    private UserId id;
    private UserName name;
    private Email email;
    private UserStatus status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // ファクトリーメソッド
    public static User create(UserName name, Email email) {
        User user = new User();
        user.name = name;
        user.email = email;
        user.status = UserStatus.ACTIVE;
        user.createdAt = LocalDateTime.now();
        user.updatedAt = LocalDateTime.now();
        return user;
    }

    // ビジネスルール
    public void changeName(UserName newName) {
        if (this.status == UserStatus.DELETED) {
            throw new IllegalStateException("削除済みユーザーの名前は変更できません");
        }
        this.name = newName;
        this.updatedAt = LocalDateTime.now();
    }

    public void delete() {
        if (this.status == UserStatus.DELETED) {
            throw new IllegalStateException("既に削除済みです");
        }
        this.status = UserStatus.DELETED;
        this.updatedAt = LocalDateTime.now();
    }
}

// infrastructure層: JPAエンティティ（データベースマッピング専用）
@Entity
@Table(name = "users")
public class UserJpaEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(nullable = false, unique = true, length = 255)
    private String email;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private String status;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    // JPAが必要とするデフォルトコンストラクタ
    protected UserJpaEntity() {}

    // ゲッター・セッター（JPAが必要とする）
    // ...
}

// infrastructure層: マッパー（Domain ⇔ JPA Entity 変換）
@Component
public class UserMapper {
    public UserJpaEntity toEntity(User domain) {
        if (domain == null) return null;
        return UserJpaEntity.of(
            domain.getId() != null ? domain.getId().getValue() : null,
            domain.getName().getValue(),
            domain.getEmail().getValue(),
            domain.getStatus().name(),
            domain.getCreatedAt(),
            domain.getUpdatedAt()
        );
    }

    public User toDomain(UserJpaEntity entity) {
        if (entity == null) return null;
        return User.reconstruct(
            new UserId(entity.getId()),
            new UserName(entity.getName()),
            new Email(entity.getEmail()),
            UserStatus.valueOf(entity.getStatus()),
            entity.getCreatedAt(),
            entity.getUpdatedAt()
        );
    }
}
```

### なぜ分離するのか？

1. **永続化の詳細をドメインから隠蔽**
   - @Entityアノテーションがドメインを汚染しない
   
2. **ドメインモデルの柔軟性**
   - JPAの制約（デフォルトコンストラクタ必須等）から解放
   
3. **テストの容易性**
   - ドメインモデルをJPA無しでテスト可能
   
4. **永続化戦略の変更が容易**
   - JPAから別の技術へ移行しやすい

---

## 📖 参考資料

### 推奨書籍
- 『ドメイン駆動設計』エリック・エヴァンス
- 『実践ドメイン駆動設計』ヴァーン・ヴァーノン
- 『Clean Architecture』ロバート・C・マーチン

### 実装パターン
- Repository パターン
- Factory パターン
- Specification パターン
- Domain Event パターン

---

## 🎓 よくある質問

### Q1: DTOの変換コードが煩雑では？
**A**: MapStructやModelMapperなどのライブラリを使うと自動化できます。ただし、明示的な変換コードも保守性の観点で推奨されます。

### Q2: 小規模プロジェクトには過剰では？
**A**: プロジェクト規模に応じて簡略化は可能です。ただし、成長を見越すなら最初から構造化することを推奨します。

### Q3: パフォーマンスへの影響は？
**A**: DTO変換のオーバーヘッドは微小です。それよりも保守性・拡張性のメリットが大きいです。

### Q4: 既存プロジェクトへの適用は？
**A**: 段階的な移行が可能です。新機能から適用し、徐々にリファクタリングしていく戦略を推奨します。

---

## 📝 まとめ

このアーキテクチャは：

✅ **ビジネスロジックの保護** - ドメイン層の独立性  
✅ **変更への強さ** - 影響範囲の限定  
✅ **テスト容易性** - 各層の独立したテスト  
✅ **チーム開発** - 明確な責務分担  
✅ **長期保守性** - 技術的負債の削減  

を実現します。

**短期的な開発コストより、長期的な保守性を重視する設計思想**です。