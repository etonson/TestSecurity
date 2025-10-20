# TestSecurity

Spring Security + JWT 練習專案

## 專案簡介

`TestSecurity` 是一個以 Spring Boot + Spring Security 為基礎的練習專案，主要用來學習並實作使用 JWT 驗證機制的流程。
透過此專案，你將可以了解如何使用 JWT 進行使用者登入、授權、以及保護 API 端點。

## 功能概覽

* 使用者登入（Authentication）並取得 JWT token。
* 使用 JWT token 存取受保護（需要授權）之 API。
* 保護 API 路由，只允許已驗證（且擁有權限或角色）的請求。
* Demonstration of how to整合 Spring Security 與 JWT。
* 簡單實作「註冊／登入」或「僅登入取得 token」流程（視專案實作情況而定）。

## 技術棧

* Java
* Spring Boot
* Spring Security
* JWT（使用 io.jsonwebtoken／jjwt 或類似庫）
* Maven
* （可依專案中所用做其他工具：如 Lombok、MapStruct、MySQL／H2 等）

## 快速開始

以下為在本地執行專案的步驟：

1. 將專案 clone 下來：

   ```bash
   git clone https://github.com/etonson/TestSecurity.git
   cd TestSecurity
   ```

2. 編譯與執行（使用 Maven wrapper）：

   ```bash
   ./mvnw clean install
   ./mvnw spring-boot:run
   ```

3. 開啟瀏覽器或 Postman，呼叫登入 API 以取得 JWT token。

   * 範例：`POST /api/auth/login`（根據實作路徑）
   * 請求 body 可能為：

     ```json
     {
       "username": "user1",
       "password": "password123"
     }
     ```
   * 成功回應時會取得 token，例如：

     ```json
     {
       "token": "eyJhbGciOiJIUzI1…"
     }
     ```

4. 使用取得的 JWT token 存取受保護的資源：

   * 在請求標頭中加上：

     ```
     Authorization: Bearer eyJhbGci…
     ```
   * 呼叫例如 `GET /api/user/profile` 或其他受保護的路徑。若 token 有效且權限正確，即可取得資料。

## 架構說明

* `src/main/java/...`：主要程式碼位置。
* `SecurityConfig.java`（或類似名稱）：定義 Spring Security 設定，包括 JWT 過濾器、認證管理、路由授權規則。
* `JwtTokenProvider`（或 TokenUtil、JwtUtils 等）：負責產生與驗證 JWT token。
* `AuthController`：處理登入（及可能的註冊）流程。
* `UserController`（或 ProtectedController 等等）：示範受保護的 API，可用於測試登入後權限存取。
* `pom.xml`：Maven 依賴設定。

## JWT 流程說明

1. 使用者透過 `/login` API 傳送使用者名稱與密碼。
2. 憑證驗證成功後，系統產生 JWT token（內含使用者名稱、角色／權限、過期時間等資訊）。
3. 前端或 API client 儲存該 token （通常存在 localStorage／Memory 等）。
4. 後續每次請求至受保護的 API，需在 Authorization 標頭中加上 `Bearer <token>`。
5. Spring Security 過濾器攔截請求，從標頭取得 token，驗證其合法性、過期狀態、是否遭篡改。
6. 若 token 有效，則建立 Authentication 物件（包含 UserDetails 與 GrantedAuthorities），並讓 SecurityContext 持有此 Authentication。
7. 最後，根據 Authentication 中的權限決定是否允許存取該 API 路徑。

## 使用提示 &注意事項

* JWT 為無狀態驗證方式：伺服器端不需儲存 session 資料，但必須妥善保護 secret 金鑰。
* Token 若被竊取則可能造成漏洞，因此建議：

  * 使用 HTTPS。
  * 設定適當的過期時間。
  * 若需要，可實作 refresh token 機制。
* 權限（Role/Authority）管理要明確，並避免硬編碼敏感邏輯。
* 測試時可搭配 Postman 或 curl 驗證流程：登入 → 拿 token → 帶 token 呼叫受保護資源。
* 若在生產使用，建議實作：黑名單（token 撤銷）、token 版本管理、演算法選擇（如 HS256 vs RS256）等進階機制。

## 專案可延伸功能

* 支援使用者註冊與密碼加密（如：使用 BCrypt）。
* 實作 refresh token 機制，提高安全性。
* 支援 角色管理與動態權限控制（如：角色新增、刪除、修改、授予使用者）。
* 使用 RS256 非對稱演算法產生與驗證 JWT ，提升安全性。
* 將 token 儲存在資料庫或 Redis，以便撤銷或追蹤。
* 使用 Swagger 或 OpenAPI 產生 API 文件。
* 整合 OAuth2／OpenID Connect 以提升驗證能力。
