# JobFusion — Project log

This file tracks implementation progress, architecture decisions, and how to extend the app. **Update this file whenever you add or change meaningful behavior.**

---

## Last updated

- **2026-05-10** — Recruiter dashboard visuals updated with iconography across metric rows: added meaningful icons for top stats (screened, matches, avg score, confidence) and ranking-intelligence cards for better scanability and parity with design references.  
- **2026-05-10** — Recruiter dashboard UI cleanup pass: fixed tab text overflow/wrapping, converted ranking-intelligence cards/panels to responsive horizontal-scroll sections, restored JD card block, and kept candidate ranking as the primary tab with cleaner row expansion behavior.  
- **2026-05-10** — Added full **`recruiter/`** layered module: `RecruiterActivity`, drawer-based `RecruiterRootScreen`, recruiter dashboard state/events/viewmodel, repository contract, API-ready DTO/mappers, and dummy data-backed fake repository.  
- **2026-05-10** — **Ranking comparison tab:** **`JobRankingEntry`** rows derived from **`DashboardDummyData.jobMatches`** (`jobRankingEntries`: same rank/title, score = matchPercent/100). **`RankingComparisonDummyData`** delegates to that list. Header shows **Top N** from list size.  
- **2026-05-10** — **AI Resume Insights tab:** dummy payload in **`AiResumeInsightsDummyData`**, domain models in **`AiResumeInsights.kt`**, **`AiResumeInsightsSection`** 2×2 card grid (missing skills, suggested keywords, strong sections, improvement score).  
- **2026-05-10** — **Dashboard dummy jobs:** `jobseeker/data/dummy/DashboardDummyData.kt` — **10** static `JobMatch` entries; `FakeDashboardRepository` attaches them to `getRecommendations()` and derives **ranked count** + **top match %** from the same list.  
- **2026-05-10** — **Job seeker dashboard** (`DashboardScreen`): resume upload card (idle/loading/success/error), match engine CTAs, recommendations stats card, `TabRow` (matches / ranking / AI), `LazyColumn` + animated **JobMatchCard** list; domain `DashboardRepository` + `FakeDashboardRepository`, DTOs + mappers for API wiring.  
- **2026-05-10** — Job seeker shell: **drawer (hamburger)** for Dashboard, Job preferences, Saved jobs, Sign out; preferences via **`JobSeekerPreferencesBody`** / **`JobSeekerPreferencesRoute`** (no duplicate top app bar).  
- **2026-05-10** — **Session restore:** DataStore keeps **`saveSession(token, role)`**; after splash, **job seeker** with a stored session opens **`JobSeekerActivity`** directly (no auth screen). **Recruiter** session restores the in-app drawer via **`AuthViewModel` init** + `getCurrentSession()`. **Signup** persists a local placeholder token + selected role. Tokens cleared on sign-out.  
- **2026-05-10** — **Access token** persisted with **DataStore** (`DataStoreAuthTokenRepository`); login returns **`LoginResult(accessToken)`** from fake + remote auth; remote login expects JSON field **`access_token`**.  
- **2026-05-10** — Fake login restricted to dummy job seeker **`movva@gmail.com` / `Test@123`** (`FakeAuthRepository`).  
- **2026-05-10** — Job seeker **domain** + **data** layers colocated under `jobseeker/domain/` and `jobseeker/data/` (removed top-level `domain/preferences` and `data/preferences`).  
- **2026-05-10** — Job seeker flow moved to `JobSeekerActivity`; auth removed from back stack after job seeker login/signup (`MainActivity.finish()`).  
- **2026-05-10** — Added job seeker preferences screen and Retrofit-ready preferences API layer.
- **2026-05-10** — Initial log created; documents work through auth, about, splash, and networking foundation.

---

## Tech stack

- Kotlin, Jetpack Compose, Material 3  
- MVVM + Clean-style layering (UI → ViewModel → use cases → repository → data)  
- `StateFlow` for UI state, intent-style events  
- Retrofit (wired; fake repository used by default until APIs are ready)  
- AndroidX SplashScreen for launch theme  

---

## App flow (high level)

1. **Splash** — Full-screen gradient; shown ~3s on cold start (Compose), after system splash.  
2. **Auth** — Single `AuthScreen`: segmented **Log in | Create account**; shared ViewModel.  
3. **About** — Static `AboutScreen`; opened from login footer; **Back to Home** returns to auth.  
4. **Signed-in routes by role** — After auth/session restore, `MainActivity` launches role-specific activities and finishes:
   - **Job seeker** → `JobSeekerActivity` (`JobSeekerRootScreen`)
   - **Recruiter** → `RecruiterActivity` (`RecruiterRootScreen`, smart candidate ranking + recruiter controls)

Root orchestration: `MainActivity` → splash → auth (or About) → role activity (`JobSeekerActivity` / `RecruiterActivity`).

---

## Features implemented

### Splash

- `SplashScreen()` — gradient, header, headline, stats row.  
- Safe area: `WindowInsets.safeDrawing`.  
- Launch theme: `Theme.JobFusion.Launch` + AndroidX SplashScreen (`installSplashScreen()`), transparent icon, gradient `windowBackground`.  
- Dependencies: `androidx.core:core-splashscreen`, Retrofit/OkHttp where applicable (see Gradle).

### Auth module

- **Screens in one surface:** login and signup with top `ToggleTab`.  
- **Signup:** Job Seeker / Recruiter; recruiter **hides** salary expectation block.  
- **Layout:** Full name → Email (stacked); Password → Retype password (stacked).  
- **Salary (job seeker only):** Type (Range / Fixed / Negotiable), Currency (INR, USD, EUR), Period (Per Year / Month / Hour); conditional min/max or expected salary; negotiable hides amount fields.  
- **Validation:** Domain `AuthValidator` returns **per-field** errors (`AuthField`); `OutlinedTextField` `supportingText` + `isError`.  
- **ViewModel:** `AuthViewModel` delegates to `LoginUseCase`, `SignupUseCase`; clears field errors on edit.  
- **Repositories:**  
  - `AuthRepository` interface: `login` → `Result<LoginResult>`, `signup` → `Result<Unit>`.  
  - `AuthTokenRepository` — `saveSession(accessToken, role)`, `clear`, `getCurrentSession()`, `observeSession()`; **`StoredAuth`** model; implementation **`DataStoreAuthTokenRepository`** (preferences DataStore `auth_tokens`: `access_token`, `session_role`).  
  - `FakeAuthRepository` — delays; **login** accepts only dummy job seeker **`movva@gmail.com`** / **`Test@123`** (email compared case-insensitive after trim) and returns a **fake JWT-shaped** `accessToken`. Signup still uses relaxed checks.  
  - `RemoteAuthRepository` — Retrofit `AuthApi`; maps `LoginResponseDto.accessToken` to `LoginResult`.  
- **API readiness:** `AuthApi`, DTOs (`LoginRequestDto`, `LoginResponseDto` with `access_token`, `SignupRequestDto`), `SignupRequest.toDto()` (recruiter omits salary fields).  
- **Switching to real API:** `AuthDependencies.USE_REMOTE_SOURCE = true` (or inject `RemoteAuthRepository(RetrofitProvider.authApi)`).  
- **Retrofit:** `RetrofitProvider` (base URL placeholder, Gson, logging). Adjust `DEFAULT_BASE_URL` when backend is ready.  

### About

- `AboutScreen()` — static copy, feature cards, **Back to Home**, footer tagline.  
- Login-only bottom card: **About JobFusion** → navigates to about (via `MainActivity` state).  
- UI pass: softer page background, smaller typography vs first draft, card surfaces, spacing.

### Job seeker flow (`jobseeker` package)

- **`JobSeekerActivity`** — `exported="false"`, theme `Theme.JobFusion`; hosts **`JobSeekerRootScreen`** (drawer + scaffold). **Sign out** clears the auth token and starts **`MainActivity`** with cleared task.  
- **`JobSeekerRootScreen`** — hamburger menu: **Dashboard**, Job preferences, Saved jobs, Sign out.  
- **`DashboardScreen`** — job seeker home: resume upload (step 1), match engine (step 2), recommendations stats card, tabbed matches / ranking / AI insights, `LazyColumn` job cards with expand + feedback (`DashboardViewModel` + `FakeDashboardRepository`).  
- **`JobSeekerPreferencesRoute` / `JobSeekerPreferencesBody`** — preferences form (simplified layout: inputs, dividers, weight sliders, Save); no standalone back bar (parent top bar shows title).  
- Presentation: `jobseeker/ui/preferences/` — `PreferencesScreen.kt` (route + body), `PreferencesViewModel`, `PreferencesContract`, `PreferencesDependencies`, `components/` (`PreferenceInputCard`, `WeightSlider`, etc.).  
- Domain: `jobseeker/domain/model/`, `repository/`, `usecase/` — `JobSeekerPreferences`, `UpdatePreferencesRequest`, `PreferencesRepository`, get/update use cases.  
- Data: `jobseeker/data/remote/` (`PreferencesApi`, DTOs), `mapper/`, `repository/` (`FakePreferencesRepository`, `RemotePreferencesRepository`). `RetrofitProvider.preferencesApi` targets `jobseeker.data.remote.PreferencesApi`.  
- `PreferencesDependencies.USE_REMOTE_SOURCE` — switch to Retrofit when backend is ready.  
- **Auth:** `sessionRole == JOB_SEEKER` shows a brief loading UI and `LaunchedEffect` → `onJobSeekerAuthenticated()` → `startActivity(JobSeekerActivity)` + **`MainActivity.finish()`**. `sessionRole == RECRUITER` → drawer + **Sign out** only. `AuthEvent.SignOut` clears session.

### UI / theming notes

- Auth reusable components live under `ui/auth/components/` (`InputField`, `PasswordField`, `ToggleTab`, `PrimaryButton`, `DropdownField`, etc.).  
- Dropdown selected value: single line + ellipsis to avoid wrap.  
- `AuthDimens` / `AuthColors` in components file for consistent spacing and colors on auth flows.  

---

## Key files (reference)

| Area | Path |
|------|------|
| App shell | `MainActivity.kt` |
| Job seeker activity | `jobseeker/JobSeekerActivity.kt` |
| Job seeker root UI | `jobseeker/ui/JobSeekerRootScreen.kt` |
| Job seeker dashboard | `jobseeker/ui/dashboard/DashboardScreen.kt`, `DashboardViewModel.kt`, `DashboardContract.kt`, `DashboardDependencies.kt`, `jobseeker/ui/dashboard/components/` |
| Splash UI | `ui/splash/SplashScreen.kt` |
| Launch theme | `res/values/themes.xml`, `res/drawable/launch_splash_background.xml` |
| Auth UI | `ui/auth/AuthScreen.kt` |
| Auth VM / contract | `ui/auth/AuthViewModel.kt`, `ui/auth/AuthContract.kt` |
| Auth DI toggle | `ui/auth/AuthDependencies.kt` |
| Auth components | `ui/auth/components/AuthComponents.kt` |
| Domain models | `domain/auth/model/AuthModels.kt` |
| Repository interface | `domain/auth/repository/AuthRepository.kt` |
| Auth session storage | `domain/auth/repository/AuthTokenRepository.kt`, `domain/auth/model/StoredAuth.kt`, `data/auth/local/DataStoreAuthTokenRepository.kt` |
| Login result model | `domain/auth/model/LoginResult.kt` |
| Use cases | `domain/auth/usecase/LoginUseCase.kt`, `SignupUseCase.kt` |
| Validation | `domain/auth/validation/AuthValidator.kt` |
| Fake / remote repo | `data/auth/repository/FakeAuthRepository.kt`, `RemoteAuthRepository.kt` |
| Retrofit API | `data/auth/remote/AuthApi.kt`, `data/auth/remote/model/AuthRequestDtos.kt` |
| Mappers | `data/auth/mapper/AuthMappers.kt` |
| Retrofit setup | `core/network/RetrofitProvider.kt` |
| Dispatchers | `core/dispatcher/DispatcherProvider.kt` |
| About | `ui/about/AboutScreen.kt` |
| Job seeker preferences UI | `jobseeker/ui/preferences/PreferencesScreen.kt`, `PreferencesViewModel.kt`, `PreferencesContract.kt`, `PreferencesDependencies.kt` |
| Preferences UI components | `jobseeker/ui/preferences/components/PreferencesComponents.kt` |
| Job seeker domain | `jobseeker/domain/model/`, `repository/`, `usecase/` |
| Dashboard domain | `jobseeker/domain/model/DashboardModels.kt`, `jobseeker/domain/repository/DashboardRepository.kt` |
| AI resume insights | `jobseeker/domain/model/AiResumeInsights.kt`, `jobseeker/data/dummy/AiResumeInsightsDummyData.kt`, `jobseeker/ui/dashboard/components/AiResumeInsightsSection.kt` |
| Ranking comparison | `jobseeker/domain/model/JobRankingEntry.kt`, `jobseeker/data/dummy/RankingComparisonDummyData.kt`, `jobseeker/ui/dashboard/components/RankingComparisonSection.kt` |
| Dashboard data (fake + DTOs) | `jobseeker/data/repository/FakeDashboardRepository.kt`, `jobseeker/data/dummy/DashboardDummyData.kt` (static **job matches**), `jobseeker/data/remote/model/DashboardDtos.kt`, `jobseeker/data/mapper/DashboardMappers.kt` |
| Job seeker data | `jobseeker/data/remote/` (+ `remote/model/`), `mapper/`, `repository/` |

---

## Gradle highlights (`app/build.gradle.kts`)

- Compose BOM, Material3, activity-compose  
- `lifecycle-viewmodel-ktx`, `lifecycle-viewmodel-compose`  
- `material-icons-extended`  
- `core-splashscreen`  
- `retrofit`, `converter-gson`, `logging-interceptor`  
- `androidx.datastore:datastore-preferences` (auth access token)  

---

## Changelog (append new entries here)

### 2026-05-10

- **Recruiter dashboard icons:** added image-like icons to all top metric cards and ranking-intelligence metric cards (`NDCG`, `Lift`, `AI confidence`, `Spearman`, `Reordered`) to match visual density in target mockups.  
- **Recruiter dashboard polish:** fixed visual breakages on narrow widths (vertical tab/label text), made **Candidate Ranking / Ranking Intelligence** tabs stable, changed intelligence metrics + before/after lists to horizontal scroll cards with fixed widths, and kept JD editor/upload in a dedicated card section.  
- **Job seeker dashboard:** Compose **Dashboard** (`DashboardScreen`, MVVM, `DashboardRepository` + `FakeDashboardRepository`, DTOs/mappers for future API): resume upload states, match engine actions, stats row, tabs, job match cards (expand/collapse animation, sliders, submit feedback). Drawer label **Dashboard**.  
- **Dashboard dummy data:** `DashboardDummyData.jobMatches` (10 rows) wired in `FakeDashboardRepository`; stats **rankedJobCount** / **topMatchPercent** synced to that list.  
- **Job seeker navigation:** `JobSeekerRootScreen` + drawer; main **Dashboard** screen; **Job preferences** and **Saved jobs** from menu; **Sign out** clears DataStore token and returns to auth via `MainActivity`.  
- **Auth token:** `LoginResult`, `login` → `Result<LoginResult>`, `LoginResponseDto` / fake token string, `AuthTokenRepository` + DataStore persistence from `AuthViewModel` on login and clear on sign-out (recruiter + job seeker).  
- Added and documented **PROJECT_LOG.md** (this file).  
- Captured: splash + launch theme, 3s splash → auth, auth module (MVVM, use cases, validation, fake/remote repos, Retrofit scaffolding), about page + login footer entry, UI typography/spacing polish, per-field errors, recruiter salary hide, stacked name/email and password fields.  
- **Job seeker preferences:** (moved to `jobseeker/` package) `PreferencesScreen`, ViewModel, UI state/events, `PreferencesDependencies`, fake + remote `PreferencesRepository`, Retrofit `PreferencesApi` + DTOs, mappers, use cases, components, preview.  
- **Job seeker activity & back stack:** `JobSeekerActivity` in manifest; job seeker auth success starts it and **`MainActivity.finish()`**; recruiter remains on auth with drawer. **PROJECT_LOG** updated.  
- **Job seeker domain/data colocation:** preferences API, DTOs, mappers, repositories, and domain models/use cases moved from `domain/preferences` + `data/preferences` into **`jobseeker/domain/**` and **`jobseeker/data/**`**.  
- **Dummy job seeker login:** `FakeAuthRepository` login success only for **`movva@gmail.com`** + **`Test@123`**; error message hints credentials when using fake repo.  

---

## How to update this file

1. Bump **Last updated** date.  
2. Add a bullet list under **Changelog** for the day (or a new `### YYYY-MM-DD` section).  
3. If you add routes, env flags, or new modules, extend **Key files** or **App flow** so newcomers stay oriented.  
