# Project Done List — motordesk

Date: 2026-08-04

This file lists repository work that appears implemented and is ready to be moved to the Project board "Done" column. For each item I included short description and evidence (representative commits and links). Use these as card titles/descriptions when moving cards.

---

## Recommended Done cards

1. Authentication / Login
   - Description: JWT-based login implemented; supports login by email, CPF/CNPJ. Contains diagrams and event-storming artifacts.
   - Evidence:
     - Create JWT: commit 2ce9b28 — https://github.com/1ChristianAlex/motordesk/commit/2ce9b28298e249d01b2ac13b784e7673af592af4
     - Login with email and CPF: commit 65212d0 — https://github.com/1ChristianAlex/motordesk/commit/65212d0c84cc304b827ffc23362374a92194a9ac
     - Login with CNPJ: commit 805bda9 — https://github.com/1ChristianAlex/motordesk/commit/805bda9a1d59d2df6404bbfee7e031542c8c85de
     - Auth and current user: commit 3665ba4 — https://github.com/1ChristianAlex/motordesk/commit/3665ba47fcbbd76950cc3509c85c82b7b5c04b86
     - Login diagrams/drawio: commits b5c4c55 / 2f65122 — https://github.com/1ChristianAlex/motordesk/commit/b5c4c55bd6a55b79a1f5a1eab5f4d9317686939d

2. User & Company flows
   - Description: Company creation flow and user↔company 1:1 relation implemented; handlers and use-cases for user creation fixed.
   - Evidence:
     - Create company flow: commit 0643a72 — https://github.com/1ChristianAlex/motordesk/commit/0643a72a0b3f93f57ae472c0fc9896b1f4431dd0
     - 1:1 relation to user and company: commit 0f510d5 — https://github.com/1ChristianAlex/motordesk/commit/0f510d502e81f4c33dc065a4f768795b9c33a416
     - Create user handler / fixes: commits e8eb753 / e5494a5 — https://github.com/1ChristianAlex/motordesk/commit/e8eb753436626d76651ced6c027885955de08206

3. Vehicle CRUD & Access Rules
   - Description: Vehicle create/read/update/delete implemented; only manager-type users allowed to perform certain CRUD operations.
   - Evidence:
     - Vehicle CRUD: commit 0864a40 — https://github.com/1ChristianAlex/motordesk/commit/0864a403ddfbef53a92f4b1fe559299274cbee33
     - Create vehicle: commit 63b627f — https://github.com/1ChristianAlex/motordesk/commit/63b627fd64661831518cf0fd0dd5b044780acdb0
     - Restrict CRUD to manager users: commit b6f558a — https://github.com/1ChristianAlex/motordesk/commit/b6f558aab38521372289378c20fef1b15cef7db6

4. Service Orders (core flows)
   - Description: Service order entity, repositories, use-cases, routes and status/tasks handling implemented; update handler and retrieval endpoints present.
   - Evidence:
     - Service order repositories: commit f9d74b6 — https://github.com/1ChristianAlex/motordesk/commit/f9d74b6d640ea206a731a896b244972553f0b091
     - Create use-cases: commit 55b110a — https://github.com/1ChristianAlex/motordesk/commit/55b110aa275469e9657879aaf40073d908a04af3
     - Update handler (domain logic): commit d5a31eb — https://github.com/1ChristianAlex/motordesk/commit/d5a31ebdcef572f837a79d1f98254678bb8aa6db
     - Routes and retrieval: commits 31e4758 / 418ff58 — https://github.com/1ChristianAlex/motordesk/commit/31e47588e6994093b95c1dd705a902b00daec2a6

5. Persistence & Infra
   - Description: DB connections, history DB, Mongo history implementation, Redis streaming for emails, Docker/docs added.
   - Evidence:
     - Connection to history DB: commit f7bbae3 — https://github.com/1ChristianAlex/motordesk/commit/f7bbae3191195d5d90a0a93156a76bd875db0eab
     - Mongo history: commit cb55627 — https://github.com/1ChristianAlex/motordesk/commit/cb55627c993f4b37e6cc713cd8ae61abd706f747
     - Redis streaming impl: commit d15fe8c — https://github.com/1ChristianAlex/motordesk/commit/d15fe8c6526943747b6ae849a8fe4e900be2a325
     - Infra docs & docker: commit ab1287d — https://github.com/1ChristianAlex/motordesk/commit/ab1287d4a597aa80c3b2b055355b1b4912169c94

6. Tests, Lint, Docs, Quality
   - Description: Unit tests added and updated; readme, swagger, lint and sonar config present.
   - Evidence:
     - Unit tests: multiple commits (e.g., bf475e8, 523e46b, 52894c3) — https://github.com/1ChristianAlex/motordesk/commit/bf475e8483a531e11527a668f0473116d6790637
     - Swagger: commit 5f21e7c — https://github.com/1ChristianAlex/motordesk/commit/5f21e7c0fdb33a2bbdd952ff2db67a543911bf90
     - README updates: commits a44ee7a / b2a9976 — https://github.com/1ChristianAlex/motordesk/commit/a44ee7a7b704ff9d5ea9c4214eac071b291fe07c

---

## Suggested CSV for import / quick card creation

You can use this CSV to bulk-create cards (title,description,evidence_link). Adjust columns when importing to your project tool if needed.

"title","description","evidence_link"
"Authentication / Login","JWT login (email/CPF/CNPJ) implemented","https://github.com/1ChristianAlex/motordesk/commit/2ce9b28298e249d01b2ac13b784e7673af592af4"
"User & Company flows","Company creation and user<->company relation implemented","https://github.com/1ChristianAlex/motordesk/commit/0643a72a0b3f93f57ae472c0fc9896b1f4431dd0"
"Vehicle CRUD & Access Rules","Vehicle CRUD + manager-only rules","https://github.com/1ChristianAlex/motordesk/commit/0864a403ddfbef53a92f4b1fe559299274cbee33"
"Service Orders","Core service order flows, routes and status handling","https://github.com/1ChristianAlex/motordesk/commit/f9d74b6d640ea206a731a896b244972553f0b091"
"Persistence & Infra","DB connections, history, Mongo, Redis streaming","https://github.com/1ChristianAlex/motordesk/commit/f7bbae3191195d5d90a0a93156a76bd875db0eab"
"Tests & Docs","Unit tests, swagger, README, linting","https://github.com/1ChristianAlex/motordesk/commit/bf475e8483a531e11527a668f0473116d6790637"

---

Notes:
- I matched items to representative commits (not exhaustive). If you want every related commit listed per card, I can expand each card with the full commit list.
- There is currently only one open GitHub issue (#1) in the repo; most implemented work is tracked by commits rather than issues or project cards.

If you want, I can:
- Create a CSV file alongside this markdown (project-done.csv) in the repo — ready to download/import.
- Attempt to programmatically move cards to the Done column (requires project and column IDs or token with Projects write scope).

