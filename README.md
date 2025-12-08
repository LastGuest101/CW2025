# Tetris Game Coursework - Jacob Villegas (20647052)

A modern Tetris clone built with **JavaFX**, featuring:

- A custom **“Time Freeze”** mechanic
- A cozy, pastel aesthetic inspired by **Suika Game** 🍉

GitHub Repository: <https://github.com/LastGuest101/CW2025>

---

## 📋 Table of Contents

1. [Compilation Instructions](#compilation)
2. [Implemented and Working Properly](#implemented-and-working-properly)
3. [Implemented but Not Working Properly](#implemented-but-not-working-properly)
4. [Features Not Implemented](#-features-not-implemented)
5. [New Java Classes](#-new-java-classes)
6. [Modified Java Classes](#modified-java-classes)
7. [Unexpected Problems](#unexpected-problems-and-solutions)

---

## Compilation

Before running the game, ensure you have the correct tools installed.

- **Java:** **JDK 23** or higher
- **Maven:** Not required globally (project uses Maven Wrapper), but if installed, use **3.8+**

### Check your Java version

Open a terminal (Command Prompt / PowerShell / Terminal) and run:

```sh
java -version
```

If the output starts with `23` (or higher), you are good to go.

If it shows `1.8`, `17`, `21`, or anything below `23`, the game will not compile.  
Download and install **JDK 23** from:

- [Oracle JDK](https://www.oracle.com/java/technologies/downloads/)
- [Adoptium Temurin](https://adoptium.net/)

---

## Running the Game

You can run the game either from the **command line** (recommended) or via **IntelliJ IDEA**.

---

### Option 1: Command Line (Recommended)

This uses the bundled **Maven Wrapper** (`mvnw` / `mvnw.cmd`), which automatically downloads dependencies.

1. **Clone the repository**

```sh
git clone https://github.com/LastGuest101/CW2025
cd CW2025
```

Make sure you are in the folder that contains `mvnw` and `pom.xml`.

2. **Run the game**

#### On Windows

```sh
mvnw.cmd clean javafx:run
```

#### On macOS / Linux

```sh
chmod +x mvnw
./mvnw clean javafx:run
```

> Note: The first run may take a minute while Maven downloads dependencies.

---

### Option 2: Running in IntelliJ IDEA

1. **Clone from Version Control**

- Open **IntelliJ IDEA**
- On the Welcome screen, click **“Get from VCS”**  
  (or go to **File > New > Project from Version Control**)
- Use this URL:

```text
https://github.com/LastGuest101/CW2025
```

- Click **Clone**

2. **Configure JDK 23**

- Go to **File > Project Structure…** (or press `Ctrl+Alt+Shift+S`)
- Select **Project** in the left sidebar
- Set:
  - **Project SDK:** `23` (e.g. _"23 Oracle OpenJDK"_)
  - **Project language level:** `23 – String templates...`
- If JDK 23 is missing:
  - Click the SDK dropdown → **Add SDK** → **Download JDK**
  - Choose **Version 23**
- Click **Apply** and **OK**

3. **Sync Maven**

- Open the **Maven** tool window (right sidebar)
- Click **Reload All Maven Projects** (the spinning arrows icon)
- Wait until indexing and syncing complete

4. **Run the game**

- Navigate to:  
  `src/main/java/com/tetris/Main.java`
- Click the green **Run (▶)** button next to the `Main` class

---

## Troubleshooting Issues

### `mvnw.cmd` fails with a JAVA_HOME error

If you see something like:

```text
Error: JAVA_HOME is set to an invalid directory.
JAVA_HOME = "C:\Program Files\Eclipse Adoptium\jdk-17.0.14.7-hotspot\"
Please set the JAVA_HOME variable in your environment to match the
location of your Java installation.
```

follow these steps.

#### 1. Find your installed JDK (23 or higher)

1. Open **PowerShell**.
2. Run:

   ```ps1
   where.exe java
   ```

3. Look for a path like:

   ```text
   C:\Program Files\Java\jdk-23\bin\java.exe
   ```

4. Your JDK home is everything **before** `\bin\java.exe`, e.g.:

   ```text
   C:\Program Files\Java\jdk-23
   ```

#### 2. Fix `JAVA_HOME` in Environment Variables

1. Press **Start** → type **Environment Variables** → open  
   **“Edit the system environment variables”** → click **Environment Variables…**
2. In **User variables for \<your-user\>**:
   - If `JAVA_HOME` exists: **Edit** it and set **Value** to your JDK path, e.g.  
     `C:\Program Files\Java\jdk-23`
   - If it doesn’t exist: click **New…** and create:
     - **Name:** `JAVA_HOME`
     - **Value:** `C:\Program Files\Java\jdk-23`
3. In **System variables**:
   - Find `JAVA_HOME` (often pointing to an old JDK like `...jdk-17...`).
   - Either **Edit** it to the same JDK path (e.g. `C:\Program Files\Java\jdk-23`)  
     or **Delete** it if you only want to use the user variable.
4. Still in **User variables**, edit **Path**:

   - Click **Edit…** → **New** → add:

     ```text
     %JAVA_HOME%\bin
     ```

5. Click **OK** on all dialogs.

#### 3. Restart the terminal and retry

1. Close all PowerShell/terminal windows.
2. Open a **new** PowerShell.
3. Verify:

   ```ps1
   echo $env:JAVA_HOME
   java -version
   ```

   You should see your JDK path (e.g. `C:\Program Files\Java\jdk-23`) and Java version 23+.

4. Run the game again from the project folder:

   ```ps1
   cd "C:\Users\olive\CW2025"
   .\mvnw.cmd clean javafx:run
   ```

### Dependencies

All dependencies are defined in `pom.xml` and are downloaded automatically by Maven/Maven Wrapper.

#### Runtime dependencies

- `org.openjfx:javafx-controls:21.0.6`  
  Used for JavaFX UI controls such as buttons, labels, panes, and the main game window.

- `org.openjfx:javafx-fxml:21.0.6`  
  Used to load and work with `.fxml` layout files (e.g. `gameLayout.fxml`) for defining the UI structure.

#### Test dependencies

- `org.junit.jupiter:junit-jupiter-api:5.12.1`  
  JUnit 5 testing API (annotations like `@Test`, assertions, etc.) used to write unit tests.

- `org.junit.jupiter:junit-jupiter-engine:5.12.1`  
  JUnit 5 test engine that actually runs the tests when you execute `mvn test` or use the IDE test runner.

#### Build plugins (tools used during build)

- `org.apache.maven.plugins:maven-compiler-plugin:3.13.0`  
  Configures Java compilation; here it compiles the code with Java **23** (source/target 23).

- `org.openjfx:javafx-maven-plugin:0.0.8`  
  Maven plugin that makes it easy to run the JavaFX app with `mvn javafx:run` and build JavaFX runtime images.

## Project Packages

The project is organised into packages under `com.tetris`:

- `com.tetris`  
  Application entry point and high-level wiring.
- `com.tetris.board`  
  Contains the board/grid representation and logic for placing bricks and clearing rows.

- `com.tetris.bricks`  
  Defines all Tetris brick (Tetromino) types, their shapes, and how new bricks are generated.

- `com.tetris.data`  
  Simple data / view-model classes that expose game state to the UI (e.g. what to draw, drop timing).

- `com.tetris.gameLogic`  
  Core game rules and systems: movement, rotation, collision checks, scoring, levels, high scores, and the Time Freeze mechanic.

- `com.tetris.ui`  
  JavaFX user interface: drawing the board, handling player input, showing notifications, game over screen, and playing sounds.

- `src/main/resources`  
  Non-code resources used by the game, such as `gameLayout.fxml`, CSS styles, images, and sound files.

- `src/test/java`  
  Unit tests for bricks, row-clearing, scoring, and other game logic.

## Version Control Practices

This project is managed with **Git** and hosted on **GitHub**. The repository uses a simple branching strategy to separate new features from cleanup and improvements.

### Branches

- **`features`**  
  Main branch for implementing new gameplay and UI functionality. New mechanics (like Time Freeze), visual changes, sound integration, and other major additions were first built and tested here.

  **Sub-branches:** For each individual feature, a dedicated sub-branch was created off `features` to allow isolated development. Examples include:

  - `features/score` – scoring system and display
  - `features/sound` – sound effects and music integration
  - `features/highscore` – high score saving and loading
  - `features/time-freeze` – Time Freeze mechanic and UI
  - `features/levels` – level progression and difficulty scaling

  Once a feature was complete and tested, its sub-branch was merged back into `features`.

- **`refactoring`**  
  Branch focused on improving and cleaning up existing code after features were added. Changes here aimed to make the codebase easier to read, maintain, and extend without changing gameplay behaviour.

- **`refactoring2`**  
  Created midway through the project to perform a second round of refactoring on newer features. This allowed more aggressive restructuring (e.g. splitting classes, improving package structure, reducing duplication) while keeping the `features` branch stable.

### Commit Structure

Commits were created in two layers of detail:

#### 1. High‑level feature commits

These describe the main functionality added, for example:

- “Add Time Freeze mechanic and UI button”
- “Implement high score saving and loading”

#### 2. More detailed follow‑up commits

After the initial feature commit, smaller commits documented and refined specific changes, such as:

- “Refactor `GameController` into separate logic and UI helper classes”
- “Extract brick rotation into `BrickRotator` and `MatrixOperations`”

This approach keeps the history readable: you can see what major feature was added in one commit, then inspect later commits for precise details of how the implementation was improved or refactored.

## Project Structure

```
CW2025/
├── src/
│   ├── main/
│   │   ├── java/com/tetris/
│   │   │   ├── Main.java                    # Application entry point
│   │   │   ├── board/                       # Board logic & state management
│   │   │   ├── bricks/                      # Tetromino shapes & generation
│   │   │   ├── data/                        # Data transfer objects
│   │   │   ├── gameLogic/                   # Core game rules & mechanics
│   │   │   └── ui/                          # User interface & rendering
│   │   └── resources/
│   │       ├── gameLayout.fxml              # JavaFX UI layout
│   │       ├── window_style.css             # Styling & theme
│   │       ├── images/                      # Visual assets (logo, etc.)
│   │       └── sounds/                      # Audio files (SFX, music)
│   └── test/
│        java/com/tetris/
│        ├── board/
│        │   └── SimpleBoardTest.java       # Board logic tests (51 tests)
│        │
│        ├── bricks/
│        │   ├── BrickGeneratorTest.java    # 7-bag system tests (27 tests)
│        │   └── BricksTest.java            # Brick shape tests
│        │
│        └── gameLogic/
│            ├── ClearRowTest.java          # Row clearing tests
│            ├── FreezeManagerTest.java     # Freeze mechanic tests
│            ├── LevelManagerTest.java      # Level progression tests
│            ├── MatrixOperationsTest.java  # Matrix utility tests
│            ├── ScoreTest.java             # Scoring tests
│
│
├── pom.xml                                  # Maven dependencies
└── README.md                                # Documentation
```

## Implemented and Working Properly

### Features Added

#### 1. **Time Freeze Mechanic**

**Implementation:**

- Created `FreezeManager` class to handle all freeze-related logic

  - Tracks freeze state (active/inactive) via `isFrozen` boolean
  - **One-time use per game**: `hasUsedFreeze` flag prevents reactivation until game restarts
  - Controls freeze duration using JavaFX `Timeline`.
  - Provides methods: `tryActivate(Runnable onFreezeEnd)`, `isFrozen()`, `reset()`

- **Integration with game loop** (`GameController`):

  - When freeze is active, automatic downward movement is paused
  - Player can still move left/right and rotate the brick during freeze
  - `Timeline` automatically counts down freeze duration
  - After expiry, `onFreezeEnd` callback executes to resume normal gameplay
  - On new game, `FreezeManager.reset()` re-enables the ability

- **Configuration** (`GameConfig`):
  - `FREEZE_DURATION` – how many milliseconds freeze lasts (e.g. 3000ms = 3 seconds)
  - No cooldown system: ability is **single-use per game**

---

#### 2. **Level Progression System**

**Implementation:**

- Created `LevelManager` class to handle difficulty scaling

  - Tracks current level (starts at 1)
  - Counts total lines cleared
  - Calculates when to level up (e.g. every 10 lines)

- **Integration with game loop** (`GameController`):

  - On each row clear, `LevelManager.addClearedLines(count)` is called
  - If threshold reached, `levelUp()` triggers:
    - Level increments
    - Drop speed increases (brick falls faster)
    - Notification sent to UI

**Speed progression calculation**:

- Formula: `Speed = BASE_SPEED × (MULTIPLIER ^ (level - 1))`
- Where `MULTIPLIER = 0.9` (from `GameConfig`)
- Speed is clamped to never go below `MAX_SPEED_CAP` (100ms minimum)

- **Speed progression**:
  ```
  Level 1:  1000ms (1000 × 0.9^0 = 1000)
  Level 2:  900ms  (1000 × 0.9^1 = 900)
  Level 3:  810ms  (1000 × 0.9^2 = 810)
  Level 10: 387ms  (1000 × 0.9^9 = 387)
  Level 15: 206ms  (1000 × 0.9^14 = 206)
  Level 22: 100ms  (reaches minimum cap)
  ...
  Level 10+: 100ms (max difficulty)
  ```

---

#### 3. **High Score**

**Implementation:**

- Created `HighScoreManager` class for saving/loading scores

  - `saveHighScore(int score)` – writes to `highscore.dat` file in user directory
  - `loadHighScore()` – reads saved score on game start
  - Uses Java's `FileWriter`/`BufferedReader` for simple text-based storage

- **UI display** (`GuiController`):
  - "High Score: XXXX" label always visible
  - Updates immediately when new record set.

---

#### 4. **Sound System & Audio Feedback**

**Implementation:**

- Created `SoundManager` class to handle all audio

  - Uses JavaFX `MediaPlayer` and `Media` classes
  - Preloads sound files on initialization to avoid lag
  - Methods: `playMoveSound()`, `playRotateSound()`, `playClearSound()`, `playFreezeSound()`, `playGameOverSound()`, `toggleMute()`

- Created `GameResources` class for resource loading

  - `loadSound(String filename)` – gets `.wav`/`.mp3` from `resources/sounds/`

- **Sound files added** (`src/main/resources/sounds/`):

  - `move.wav` – brick moves left/right
  - `rotate.wav` – brick rotates
  - `clear.wav` – row(s) cleared
  - `freeze.wav` – Time Freeze activated
  - `gameover.wav` – game ends
  - Optional: `bgm.mp3` – background music loop

- **Mute functionality**:
  - `M` key toggles `SoundManager.muted` flag
  - When muted, `playSound()` methods return immediately without playing
  - Mute state persisted in memory (resets on restart)

---

#### 5. **Visual Animations & Styling**

**Implementation:**

##### a) **Board Animations** (`BoardAnimator`)

- **Row clear animation**:

- **Hard drop shake effect**:

- **Freeze effect**:

##### b) **Custom Styling** (`BoardStyler`, `window_style.css`)

- **Pastel color palette** (Suika Game inspired):
  ```css
  I-brick: #FFB3BA (soft pink)
  J-brick: #FFDFBA (peach)
  L-brick: #FFFFBA (light yellow)
  O-brick: #BAFFC9 (mint green)
  S-brick: #BAE1FF (sky blue)
  T-brick: #E0BBE4 (lavender)
  Z-brick: #FFC9DE (rose)
  ```

---

#### 6. **Enhanced Scoring System** 🎯

**Implementation:**

- Created `ScoringSystem` class to centralise scoring rules and combo mechanics

  - Base points for line clears (not piece placement):
    ```
    1 line:  100 points
    2 lines: 300 points
    3 lines: 500 points
    4 lines: 800 points (Tetris!)
    ```
  - **Level multiplier**: Base score is multiplied by current level
  - **Combo system**: Tracks consecutive line clears
    - `comboCount` starts at -1 (no combo)
    - Increments each time a piece clears lines (+1, +2, +3...)
    - Resets to -1 when a piece lands without clearing
    - Adds bonus: `50 × comboCount × level` points

  **Formula:**

  ```
  Score = (BaseScore × Level) + (50 × ComboCount × Level)
  ```

- **Integration** (`GameController`, `Score`):
  - After `ClearRow.clearFullRows()` returns number of rows cleared
  - `score.add(scoringSystem.calculateScore(rowsCleared, currentLevel))` is called
  - If no rows cleared, combo automatically resets
  - Score displayed in real-time via `GuiController`

#### 7. **Configuration Management** ⚙️

**Implementation:**

- Created `GameConfig` class with all game constants
- All classes reference `GameConfig` instead of hardcoded values
- Makes game easy to tune and balance

#### 8. **Ghost Brick Preview**

**Implementation:**

- Ghost brick is calculated and rendered in `GameController`

  - `calculateGhostPosition()` simulates dropping the current brick until collision:
    ```java
    1. Clone current brick position
    2. Move copy downward one row at a time
    3. Check collision using Board.isValidMove()
    4. Stop at last valid position before collision
    5. Return Y-coordinate as ghost position
    ```
  - Ghost Y-position stored in `ViewData` and passed to UI layer

- **Visual styling** (`BoardStyler.styleGhost()`):
- **Update mechanism** (`BoardRefresher.refreshBrick()`):

---

#### 9. **Next Shape Preview**

**Implementation:**

- Created `NextShapeInfo` class as immutable data container

  - Stores brick shape as 2D `int[][]` matrix
  - Stores brick type/position (for color styling)
  - `getShape()` returns defensive copy via `MatrixOperations.copy()`

- **Preview UI** (`gameLayout.fxml`, `GuiController`):
- **Integration points**:

---

### 10. Testing

#### **`SimpleBoardTest.java`**`

**Core Board Logic**

**Rotation System**

**Advanced Features**

**Edge Cases**

---

#### **`BrickGeneratorTest.java`**

**7-Bag Randomization System**

**Drought Prevention**

**Preview System**'

**Integration**

---

#### **`BricksTest.java`**

- Verifies all 7 brick types have correct initial shapes
- Tests rotation: each brick rotates correctly (some have 2 states, some 4)
- Checks bounds: bricks stay within valid matrix dimensions
- Shape matrix immutability (defensive copies)

---

#### **`ClearRowTest.java`**

- Single row clear
- Multiple simultaneous row clears (2, 3, 4 rows)
- Board shifts down correctly after clear
- Edge cases: clearing top row, bottom row, non-contiguous rows
- Cleared row data capture (for particle animations)
- Empty rows added at top after clear

---

#### **`ScoreTest.java`**

- Initial score is 0
- Adding single score values
- Adding multiple scores (cumulative)
- Score reset functionality
- Score never goes negative
- JavaFX property binding updates

---

#### **`MatrixOperationsTest.java`**

**Collision Detection (`intersect`)**

**Matrix Manipulation**

**Row Clearing Logic**

---

#### **`LevelManagerTest.java`**

**Level Progression**

**Speed Calculation**

**Edge Cases**

---

#### **`FreezeManagerTest.java`**

**Activation**

**One-Time Use**

**Duration**

**Reset**

---

## Implemented but Not Working Properly

While the core game is fully functional, several features were implemented but have limitations or could be significantly improved with more development time.

---

### 1. **Sound System - Limited Audio Controls**

**What's Working:**

- ✅ Background music playback (looping)
- ✅ Sound effects (move, rotate, drop, clear, freeze)
- ✅ Basic mute toggle (M key)

**Limitations:**

#### **A. No Volume Control Slider**

- Mute is binary: 100% or 0% (no granular control)

**What Should Be Added:**

- **Master volume slider** (0-100%)
- **Separate sliders** for music and sound effects
- **Volume persistence** (save to config file)
- **Real-time preview** (play sound when adjusting slider)

**Workaround:**

- Users must adjust system volume instead of in-game volume

---

#### **B. No Individual Sound Toggle**

**Current Implementation:**

- Mute affects ALL sounds (music + effects together)

**What Should Be Added:**

- Separate toggles:
  - ☐ Mute Music
  - ☐ Mute Sound Effects
  - ☐ Mute UI Sounds (clicks, notifications)

---

### 2. **Notification System - Incomplete Visual Feedback**

**What's Working:**

- Basic notifications (score, game over)
- Fade in/out animations
- Auto-dismiss after duration

**Limitations:**

#### **A. Missing Gameplay Event Notifications**

**Currently NOT Displayed:**

| Event                    | Current Behavior         | Should Display                                 |
| ------------------------ | ------------------------ | ---------------------------------------------- |
| **Combo x2, x3, x4...**  | Score increases silently | "COMBO x3! +450 pts" with multiplier animation |
| **Tetris (4 lines)**     | None                     | "TETRIS! +2400 pts" with celebratory effect    |
| **Double/Triple Clear**  | No special feedback      | "DOUBLE! +900 pts" or "TRIPLE! +1500 pts"      |
| **Power-Up Used**        | None                     | Also show remaining duration countdown         |
| **Level Speed Increase** | None                     | "LEVEL 5! Speed: 810ms → 729ms"                |

---

### 3. **Scoring System - No T-Spin Detection**

**What's Working:**

- Basic line clear scoring (100/300/500/800 × level)
- Combo bonuses (+50 per combo × level)
- Soft/hard drop bonuses (+1/+2 per row)

**Major Missing Feature: T-Spin Recognition**

**Why Not Implemented:**

**Technical Complexity**

**Hard to Showcase in demo**

2. **Must Track Rotation State:**

   - Need to know if last move was rotation vs movement
   - Must differentiate rotation from wall kick vs normal rotation
   - Wall kick data stored in `BrickRotator` but not exposed

3. **Mini T-Spin vs Full T-Spin:**
   - "Mini" T-Spin = only 2 corners blocked (50% points)
   - "Full" T-Spin = 3+ corners blocked (full points)
   - Adds another layer of detection complexity

---

#### **Related Issue: No Visual Indicator for Special Moves**

**Current Behavior:**

- All line clears look identical
- No indication that Tetris (4 lines) is special
- No "perfect clear" detection (clearing entire board)

**What Should Happen:**

```
Regular Clear:  "1 LINE CLEAR"         (white text)
Double:         "DOUBLE CLEAR!"        (yellow text)
Triple:         "TRIPLE CLEAR!"        (orange text)
Tetris:         "TETRIS!!!"            (gold text + particles)
T-Spin Single:  "T-SPIN SINGLE!"       (purple text + special sound)
T-Spin Double:  "T-SPIN DOUBLE!!!"     (bright purple + screen flash)
Perfect Clear:  "PERFECT CLEAR!!!!"    (rainbow text + fireworks)
```

---

#### **Score Comparison Bar**

```
Current Score:  12,500  ████████░░░░░░░░░░  (25% to high score)
High Score:     50,000  ████████████████████
```

---

### 4. **Window Resizing - Non-Responsive Layout**

**What's Working:**

- Game runs at fixed 800×700 window size
- All UI elements positioned correctly at default size
- Layout looks polished on standard displays

**Limitations:**

#### **A. Fixed Dimensions - No Dynamic Scaling**

**Current Implementation:**

```java
// Main.java
primaryStage.setWidth(GameConfig.WINDOW_WIDTH);  // 800px fixed
primaryStage.setHeight(GameConfig.WINDOW_HEIGHT); // 700px fixed
primaryStage.setResizable(false); // ← Window locked to fixed size
```

**Issues:**

1. **Small Screens (Laptops, 1366×768 displays):**

2. **Large Screens (4K, Ultrawide monitors):**

3. **Accessibility:**

**What Should Happen:**

**Responsive Layout Options:**

**Option 1: Proportional Scaling**

**Option 2: Breakpoint-Based Layouts**

## Features Not Implemented

While the core game is fully functional, several advanced features were considered but not implemented due to time and scope constraints.

### 1. **Multiplayer / 2-Player Mode**

**What it would include:**

- Split-screen local multiplayer with two independent game boards
- Versus mode: first to reach target score or survive longest wins
- Synchronized game state and input handling for both players

**Why not implemented:**

- **Architecture complexity**: Current `GameController` manages single game state
  - Would require refactoring to support multiple independent game instances
  - Input handling would need to distinguish P1 (WASD) vs P2 (Arrow keys)
- **UI restructuring**: `gameLayout.fxml` designed for single board
  - Would need dual `GridPane` layout with proper spacing
  - Score/level displays would need duplication
  - Collision detection must work independently for both boards
  - Edge cases: simultaneous game overs, pause conflicts, sound mixing
- **Time constraints**: Estimated 20+ hours for full implementation and testing

**Partial implementation considered:**

- Online multiplayer via sockets – far beyond project scope

---

### 2. **Per-User High Score Tracking**

**What it would include:**

- Username entry system (text field on startup or game over)
- Leaderboard with top 10 scores, player names, and dates
- Profile system with stats: total games played, average score, best level reached
- Persistent storage using JSON or SQLite database instead of single `highscore.dat` file

**Why not implemented:**

- **Scope creep**: Current `HighScoreManager` only tracks single integer value
  - Would require data model refactoring (`HighScore` class with name/score/date fields)
  - Database setup (SQLite) or JSON parsing library (Gson/Jackson)
- **UI expansion**: Leaderboard would need dedicated FXML screen
  - Navigation system: main menu → leaderboard → game
  - Username input dialog (JavaFX `TextInputDialog`)
- **Data validation**: Username sanitization, duplicate handling, max length checks
- **Time vs. value**: Single high score sufficient for core gameplay demonstration

---

### 3. **Advanced Animations**

**What could be added:**

- **Combo visual streaks**: Screen flashes or particle trails during high combos
- **Level up animation**: Screen zoom/flash with celebratory particles
- **Game over screen transitions**: Fade-to-black or shatter effect instead of instant panel
- **UI scene transitions**: Smooth fade/slide animations when switching between screens (menu → game → game over → menu)
- **Interactive button effects**:
- **Menu entrance animations**: Elements slide in from sides or fade in sequentially on startup
- **Score counter animations**: Numbers "tick up" smoothly instead of instantly updating (e.g., 1000 → 1500 counts up over 0.5s)

**Why not implemented:**

- **Diminishing returns**: Current particle debris system already provides satisfying feedback
  - Additional animations risk cluttering screen or distracting from gameplay
- **Performance concerns**: Complex animations (especially particles) can drop FPS
  - JavaFX Timeline/Transition overhead adds up with multiple simultaneous effects
- **Polish vs. core features**: Time better spent on gameplay mechanics (levels, scoring)
- **Accessibility**: Too many animations can be overwhelming for some players

**What was implemented:**

- Particle debris on row clear (crumbling blocks)
- Hard drop shake effect
- Freeze visual overlay
- Notification fade-in/fade-out

---

### 4. **Alternative Game Modes**

**Potential modes:**

- **Sprint**: Clear 40 lines as fast as possible (speedrun mode)
- **Ultra**: Maximize score in 3 minutes (time attack)
- **Invisible**: Bricks disappear after locking (memory challenge)

**Why not implemented:**

- **Menu system required**: Would need mode selection screen before game starts
  - Navigation: main menu → mode select → game → results → back to menu
  - State management for different rulesets
- **Rule variations**: Each mode requires different win/loss conditions
  - Sprint needs timer and line counter
  - Ultra needs countdown timer and score focus
- **Testing multiplication**: Each mode needs separate testing for edge cases
- **Core experience priority**: Standard Tetris mode with levels/scoring provides sufficient depth

**Current implementation:**

- Single "Endless" mode with level progression.

---

### 5. **Dynamic Visual Themes Per Level**

**What it would include:**

- Background images changes theme every 5 levels (fruits → candy etc...)
- Color palettes shift (warm tones → cool tones )
- Parallax scrolling backgrounds for depth
- Music tracks change with themes

**Why not implemented:**

- **CSS complexity**: Dynamic theme switching requires:
  - Multiple stylesheets or extensive CSS variable manipulation
  - `BoardStyler` refactoring to support theme swapping
  - Risk of performance hit from frequent re-styling
- **Aesthetic consistency**: Current Suika-inspired pastel theme is cohesive
  - Frequent theme changes could feel jarring rather than polished

**Current implementation:**

- Single consistent pastel theme applied throughout all levels

---

### 6. **Additional Power-Ups**

**Potential power-ups:**

- **Bomb**: Clears a 3×3 area around selected cell
- **Line Eraser**: Click a row to instantly clear it
- **Brick Swap**: Replace current brick with next brick (one-time use)

**Why not implemented:**

- **Balance concerns**: Power-ups risk making game too easy
  - Would require extensive playtesting to tune costs/cooldowns
  - Could trivialize challenge, reducing satisfaction
- **UI real estate**: Power-up buttons would clutter interface
  - Current layout optimized for core controls
  - Notifications already dense with score/combo/freeze messages
- **Complexity creep**: Each power-up needs:
  - Manager class (similar to `FreezeManager`)
  - Visual feedback and animations
  - Sound effects
  - Collision/rule interactions (e.g., bomb during freeze?)
- **Core mechanic focus**: Time Freeze already provides strategic depth
  - Single well-implemented mechanic better than many shallow ones

**Current implementation:**

- Time Freeze (one-time use) as sole power-up

---

### 7. **Customizable Key Bindings**

**What it would include:**

- Settings menu to remap all controls
- Save preferences to config file (`keybindings.cfg`)
- Visual key prompt display (e.g., show custom keys in tutorial)
- Gamepad/controller support

**Why not implemented:**

- **UI overhead**: Requires dedicated settings screen with:
  - Input fields for each action (move left, rotate, freeze, etc.)
  - "Press any key to bind" detection system
  - Conflict detection (can't bind two actions to same key)
- **Persistence**: Config file parsing and validation
  - Default fallback if file corrupted
  - Cross-platform path handling
- **Current defaults sufficient**: WASD + Arrow keys cover most player preferences

**Current implementation:**

- Hardcoded WASD/Arrow key support

---

### 8. **Tutorial / First-Time User Experience**

**What it would include:**

- Interactive tutorial explaining controls step-by-step
- Overlay arrows/highlights showing where next brick will spawn
- Guided first line clear with hints
- Practice mode with slowed speed
- Help screen accessible from main menu

**Why not implemented:**

- **Implementation time**: Tutorial requires:
  - State machine for guided steps ("Now press ←", "Good! Now rotate...")
  - Conditional progression (wait for player to complete action)
  - Separate UI overlays and text prompts
- **Tetris conventions**: Standard mechanics require minimal explanation
  - Game over screen says "Press N for New Game" (minimal but sufficient)

**Current "tutorial":**

- Controls listed in README
- In-game notifications provide feedback

---

## New Java Classes

The following classes were created for this assignment. Classes are organized by package and purpose.

---

### `com.tetris.gameLogic` Package

#### 1. **`GameConfig.java`**

- **Purpose:** Centralizes all game configuration constants (board dimensions, speeds, freeze duration, scoring values, level thresholds)
- **Why created:** Eliminates hardcoded "magic numbers" throughout codebase; makes game tuning easy

#### 2. **`FreezeManager.java`**

- **Purpose:** Manages the Time Freeze power-up mechanic
- **Key methods:**
  - `tryActivate(Runnable onFreezeEnd)` - Attempts to activate freeze, returns success boolean
  - `isFrozen()` - Checks if freeze is currently active
  - `reset()` - Re-enables freeze for new game
- **Implementation:** Uses JavaFX `Timeline` for duration countdown; one-time use per game via `hasUsedFreeze` flag
- **Why created:** Encapsulates freeze logic separately from main game controller; single responsibility

#### 3. **`LevelManager.java`**

- **Purpose:** Handles level progression and speed calculations
- **Key methods:**
  - `addLines(int count)` - Increments cleared line counter
  - `checkLevelUp()` - Returns true if level threshold reached
  - `getSpeed()` - Calculates current drop speed using exponential formula
  - `levelProperty()` - JavaFX property for UI binding
- **Formula:** `Speed = BASE_SPEED × (MULTIPLIER ^ (level - 1))`
- **Why created:** Separates level/difficulty logic from scoring and game flow

#### 4. **`HighScoreManager.java`**

- **Purpose:** Persists and loads high score from disk
- **Key methods:**
  - `saveHighScore(int score)` - Writes score to `highscore.dat`
  - `loadHighScore()` - Reads saved score on startup (returns 0 if file missing)
- **File location:** User home directory (`System.getProperty("user.home")`)
- **Why created:** Simple file I/O abstraction; keeps persistence logic out of UI/game controller

#### 5. **`ScoringSystem.java`**

- **Purpose:** Calculates points for line clears and manages combo system
- **Key methods:**
  - `calculateScore(int linesCleared, int level)` - Returns points for current clear
  - `resetCombo()` - Resets combo counter (called when no lines cleared)
- **Formula:** `(BaseScore × Level) + (50 × ComboCount × Level)`
- **Combo tracking:** Increments on consecutive clears, resets on miss
- **Why created:** Centralizes scoring rules; makes balancing easier; supports future mode variations

---

### `com.tetris.ui` Package

#### 6. **`GameView.java`** (Interface)

- **Purpose:** Defines contract for game view operations
- **Key methods:**
  - `updateBoard(ViewData)` - Refresh board display
  - `updateScore(int)` - Update score label
  - `updateLevel(int)` - Update level label
  - `showGameOver()` - Display game over screen
- **Why created:** Decouples game logic from UI implementation; allows testing with mock views

#### 7. **`BoardVisuals.java`**

- **Purpose:** Manages visual representation of board cells (colors, styling)
- **Key methods:**
  - `getCells()` - Returns 2D array of JavaFX `Rectangle` nodes
  - `updateCell(int row, int col, int value)` - Sets cell color based on brick type
  - `clearCell(int row, int col)` - Resets cell to empty state
- **Why created:** Separates visual state from board logic; single place for cell appearance

#### 8. **`BoardStyler.java`**

- **Purpose:** Applies CSS styling and colors to board elements
- **Key methods:**
  - `styleBrick(Rectangle cell, int brickType)` - Applies color based on brick (I=cyan, J=blue, etc.)
  - `styleGhost(Rectangle cell, int brickType)` - Applies semi-transparent ghost styling
  - `applyTheme(String themeName)` - Switches color palettes (future expansion)
- **Why created:** Centralizes all styling logic; makes theme changes easy

#### 9. **`BoardInitiator.java`**

- **Purpose:** One-time setup of board UI structure
- **Key methods:**
  - `initBoard(GridPane, int width, int height)` - Creates grid of Rectangle cells
  - `setupLayers(Pane)` - Creates three-layer system (background, ghost, active brick)
- **Why created:** Separates initialization from refresh logic; keeps GuiController cleaner

#### 10. **`BoardRefresher.java`**

- **Purpose:** Updates board display when game state changes
- **Key methods:**
  - `refreshBrick(ViewData)` - Redraws all layers (background, ghost, active brick)
  - `updateNextPreviews(int[][][])` - Updates next brick preview grids
- **Why created:** Encapsulates all rendering logic; optimizes by only redrawing changed cells

#### 11. **`BoardAnimator.java`**

- **Purpose:** Handles visual effects and animations
- **Key methods:**
  - `spawnClearParticles(List<Integer> clearedRows)` - Creates particle debris for cleared lines
  - `shake(Node)` - Screen shake effect on hard drop
  - `flashFreeze(Pane)` - Visual overlay during Time Freeze
- **Animations:** Uses JavaFX `TranslateTransition`, `FadeTransition`, `RotateTransition`, `ScaleTransition`
- **Why created:** Keeps animation code separate from rendering logic

#### 12. **`GameInputHandler.java`**

- **Purpose:** Captures and processes keyboard input
- **Key methods:**
  - `handleKeyPress(KeyEvent)` - Maps keys to game actions
  - `setEventSource(EventSource)` - Connects to event system
- **Supported keys:** WASD, Arrow keys, Space, F (freeze), N (new game), P (pause), M (mute)
- **Why created:** Extracted from GuiController; allows input remapping in future

#### 13. **`NotificationRenderer.java`**

- **Purpose:** Displays and animates in-game notification messages
- **Key methods:**
  - `show(String message, Duration)` - Displays text with fade-in/fade-out
  - `queueNotification(String)` - Queues multiple messages to prevent overlap
- **Animations:** Fade in (200ms) → display → fade out (300ms)
- **Why created:** Separates notification logic from NotificationPanel; supports queuing

#### 14. **`GameResources.java`**

- **Purpose:** Centralized loading of FXML, CSS, images, and sounds
- **Key methods:**
  - `getFXML(String filename)` - Loads FXML from resources
  - `getCSS()` - Returns stylesheet URL
  - `getSound(String filename)` - Loads audio file from `sounds/` folder
  - `getImage(String filename)` - Loads image from `images/` folder
- **Why created:** Single place for resource paths; handles missing file errors gracefully

#### 15. **`SoundManager.java`**

- **Purpose:** Plays sound effects and background music
- **Key methods:**
  - `playMoveSound()`, `playRotateSound()`, `playClearSound()`, etc.
  - `toggleMute()` - Mutes/unmutes all sounds
  - `preloadSounds()` - Loads audio files on initialization
- **Implementation:** Uses JavaFX `MediaPlayer` for each sound effect
- **Why created:** Encapsulates all audio logic; prevents lag by preloading

---

### `com.tetris.bricks` Package

#### 16. **`GeneralBrick.java`**

- **Purpose:** Base implementation for bricks using matrix representation
- **Key methods:**
  - `getShape()` - Returns current rotation matrix
  - `rotate()` - Rotates brick 90° clockwise
- **Extends:** `Brick` (from original code)
- **Why created:** Reduces code duplication; all standard bricks inherit common rotation logic

---

## Modified Java Classes

The following classes from the original codebase were modified to support new features and improve architecture.

---

### Core Game Logic

#### 1. **`GameController.java`**

**Original Location:** `Tetris/GameController.java`  
**New Location:** `com.tetris.gameLogic/GameController.java`

**Major Changes:**

- **Added Timeline-based game loop:**

  - Original used hardcoded 400ms delay in `GuiController`
  - Now uses `createGameLoop()` method with level-based speed from `LevelManager.getGameSpeed()`
  - Loop can be recreated dynamically via `updateGameLoop()` when leveling up

- **Integrated new manager classes:**

  - Added `LevelManager` for difficulty progression
  - Added `FreezeManager` for Time Freeze power-up
  - Added `SoundManager` for audio playback
  - Added `HighScoreManager` integration for persistence

- **Added `onSpaceEvent()` method:**

  - Original only had basic movement (Down/Left/Right/Rotate)
  - New hard drop instantly moves brick to bottom and locks it

- **Enhanced `handleIntersect()` method:**

  - Original: Simple merge → clear → spawn
  - Now includes:
    - `processClearedLines()` with sound effects and level progression
    - `processGameOver()` with high score saving
    - Freeze state preservation after brick lands

- **Added `stopGame()` method:**

  - Saves high score when application closes
  - Properly stops Timeline to prevent memory leaks

- **New event handlers:**
  - `onFreezeEvent()` - Activates Time Freeze if available
  - `onPauseEvent()` - Toggles game loop pause state
  - `muteMusic()` - Controls audio
  - `saveHighScore()` - Persists score to disk

**Why Modified:**

- Needed centralized control of game speed (for level progression)
- Required integration points for new features (freeze, levels, audio)
- Improved separation: game loop logic moved from View to Controller

---

#### 2. **`Board.java`** (Interface)

**Original Location:** `Tetris/Board.java`  
**New Location:** `com.tetris.board/Board.java`

**Changes:**

- **Modified `clearRows()` signature:**

  - Now accepts level parameter to calculate score bonuses with level multipliers

**Why Modified:**

- Scoring system needed access to current level for multiplier calculations
- Supports future difficulty scaling (could affect row clearing behavior)

---

#### 3. **`SimpleBoard.java`**

**Original Location:** `Tetris/SimpleBoard.java`  
**New Location:** `com.tetris.board/SimpleBoard.java`

**Major Changes:**

- **Constructor now accepts `BrickGenerator`:**

  - Dependency injection allows testing with mock generators

- **Changed initial brick spawn position:**

  - Fixes visual bug where pieces appeared from middle of board

- **Updated `clearRows()` implementation:**

  - Now passes `currentLevel` to `ScoringSystem`
  - Captures additional data (cleared row indices, row data) for animations
  - Returns enhanced `ClearRow` object with particle effect data

- **Added ghost piece calculation:**

  - New method `calculateGhostPosition()` simulates dropping brick until collision
  - Result stored in `ViewData` for UI layer

- **Enhanced `getViewData()`:**

**Why Modified:**

- Support multiple next brick previews (queue of 3 instead of 1)
- Enable ghost piece shadow rendering
- Integrate level-based scoring
- Improve testability via dependency injection

---

#### 4. **`Score.java`**

**Original Location:** `Tetris/Score.java`  
**New Location:** `com.tetris.gameLogic/Score.java`

**Changes:**

- **Added high score tracking:**
- **Added automatic high score listener:**

  - High score automatically updates when current score exceeds it

- **Added `highScoreProperty()` method:**
  - Allows UI binding to high score display

**Why Modified:**

- Support dual score display (current + high score)
- Automatic high score tracking reduces logic in controller
- JavaFX property binding keeps UI synchronized

---

#### 5. **`ClearRow.java`**

**Original Location:** `Tetris/ClearRow.java`  
**New Location:** `com.tetris.gameLogic/ClearRow.java`

**Changes:**

- **Added fields for animation data:**

- **Added secondary constructor for tests:**
- **Added getter methods:**
  - `getClearedIndices()` - Returns row indices (e.g., [18, 19, 20])
  - `getClearedRowsData()` - Returns actual block colors before removal

**Why Modified:**

- `BoardAnimator` needs to know which rows were cleared to spawn particles at correct positions
- Row data (colors) needed for "crumbling debris" effect
- Test constructor simplifies unit tests (don't need to provide full data)

---

#### 6. **`ViewData.java`**

**Original Location:** `Tetris/ViewData.java`  
**New Location:** `com.tetris.data/ViewData.java`

**Changes:**

- **Added ghost position field:**

- **Changed `nextBrickData` to array:**

- **Added getter methods:**

**Why Modified:**

- UI needed ghost piece position for shadow rendering
- Support multiple next brick previews
- Maintains immutability (defensive copies in getters)

---

### UI Layer

#### 7. **`GuiController.java`**

**Original Location:** `Tetris/GuiController.java`  
**New Location:** `com.tetris.ui/GuiController.java`

**Massive Refactoring:**

**Removed responsibilities (delegated to new classes):**

- ❌ Direct keyboard event handling → `GameInputHandler`
- ❌ Rectangle creation/initialization → `BoardInitiator`
- ❌ Color styling logic → `BoardStyler`
- ❌ Refresh/rendering logic → `BoardRefresher`
- ❌ Animation effects → `BoardAnimator`
- ❌ Notification timing → `NotificationRenderer`

**Added responsibilities:**

- ✅ Menu navigation (Main Menu ↔ Game ↔ Pause ↔ Help)
- ✅ JavaFX property bindings (score, level, high score)
- ✅ Freeze state coordination
- ✅ FXML element references and wiring

**New methods:**

- `startNewGameFromMenu()` - Transitions from menu to game
- `showMainMenu()` - Returns to menu (auto-pauses game)
- `pauseGame()` - Toggles pause overlay
- `showHelp()` / `closeHelp()` - Help screen navigation
- `exitGame()` - Clean shutdown
- `activateFreeze()` - UI button for Time Freeze
- `bindLevel()` - Binds level label to property
- `bindHighScore()` - Binds high score label
- `setFreezeStatus()` - Updates freeze visual theme

**Why Modified:**

- Original was 400+ lines doing everything (God Object antipattern)
- Now ~250 lines focused on coordination and FXML wiring
- Follows Single Responsibility Principle
- Easier to test individual components

---

### Data Objects

---

#### 16. **`MatrixOperations.java`**

**Original Location:** `Tetris/MatrixOperations.java`  
**New Location:** `com.tetris.gameLogic/MatrixOperations.java`

**Changes:**

- **Enhanced `checkRemoving()` method:**

- **Package change:** `Tetris` → `com.tetris.gameLogic`

**Why Modified:**

- Scoring moved to `ScoringSystem` (level multipliers, combo tracking)
- Added data collection for particle animations
- Maintains immutability (returns copies)

---

#### 17. **`InputEventListener.java`** (Interface)

**Original Location:** `Tetris/InputEventListener.java`  
**New Location:** `com.tetris.ui/InputEventListener.java`

**Changes:**

- **Added new methods:**

  ```java
  // Original
  DownData onDownEvent(MoveEvent event);
  ViewData onLeftEvent(MoveEvent event);
  ViewData onRightEvent(MoveEvent event);
  ViewData onRotateEvent(MoveEvent event);
  void createNewGame();

  // New (added 5 methods)
  DownData onDownEvent(MoveEvent event);
  ViewData onLeftEvent(MoveEvent event);
  ViewData onRightEvent(MoveEvent event);
  ViewData onRotateEvent(MoveEvent event);
  DownData onSpaceEvent(MoveEvent event);  // NEW
  void onPauseEvent();                     // NEW
  void createNewGame();
  void muteMusic();                        // NEW
  void onFreezeEvent();                    // NEW
  void saveHighScore();                    // NEW
  ```

**Why Modified:**

- Support new features: hard drop, pause, mute, freeze
- Interface acts as contract between UI and logic layers
- Ensures `GameController` implements all required handlers

---

#### 18. **`Main.java`**

**Original Location:** `Tetris/Main.java`  
**New Location:** `com.tetris/Main.java`

**Changes:**

- **Updated window dimensions:**

- **Added high score loading:**

- **Added window close handler:**

- **Package change:** `Tetris` → `com.tetris`

**Why Modified:**

- Use centralized config constants
- Load persisted high score on startup
- Ensure high score saves when user closes window
- Larger window supports new UI elements (menu, level display)

---

## Modified Resources (FXML & CSS)

### 1. **`gameLayout.fxml`** - Complete UI Redesign

#### **Structural Changes:**

| Aspect               | Original                      | New                           | Why Changed                                       |
| -------------------- | ----------------------------- | ----------------------------- | ------------------------------------------------- |
| **Root Element**     | Simple `Pane`                 | Layered `StackPane`           | Allows overlaying menus on top of game            |
| **Layout Structure** | Flat, single screen           | Multi-layered with menus      | Supports main menu, pause, help screens           |
| **Controller Path**  | `Tetris.GuiController`        | `com.tetris.ui.GuiController` | Package reorganization                            |
| **Imports**          | `Tetris.GameOverPanel`        | `com.tetris.ui.GameOverPanel` | Updated package paths                             |
| **Score Display**    | Custom `ScoreLabel` component | Standard `Label`              | Removed custom component (unnecessary complexity) |

---

#### **New UI Elements Added:**

##### **A. Left Sidebar**

- **High Score Box:**
- **Level Display:**

**Purpose:** Display persistent game statistics

##### **B. Right Sidebar**

- **Game Title:** Large "TETRIS" heading
- **Current Score:** Trophy emoji icon, live score counter
- **Next Brick Preview:** Shows upcoming 3 bricks in queue
- **Action Buttons:** Pause, Freeze, Mute, New Game

**Purpose:** Centralized game controls and preview information

##### **C. Main Menu Screen**

- **Logo Image:** Visual branding
- **Title/Subtitle:** "TETRIS" + "JAVA EDITION"
- **Three buttons:** Play, Controls, Exit

**Purpose:** Professional landing screen before game starts

##### **D. Pause Menu**

- **"PAUSED" title**
- **Two buttons:** Resume, Quit to Title

**Purpose:** In-game pause navigation with clean overlay

##### **E. Help/Controls Screen**

- **"CONTROLS" title**
- **List of keybindings:** Movement, rotation, freeze, etc.
- **Back button**

**Purpose:** In-game tutorial/reference for accessibility

---

### 2. **`window_style.css`** - Complete Visual Overhaul

#### **Theme Transformation:**

| Aspect            | Original                           | New                                           | Visual Effect                  |
| ----------------- | ---------------------------------- | --------------------------------------------- | ------------------------------ |
| **Color Palette** | Dark grey/industrial               | Warm pastels (wheat, cornsilk, amber)         | Cozy, inviting aesthetic       |
| **Background**    | External PNG image                 | CSS gradient (wheat → cornsilk)               | Eliminates external dependency |
| **Game Board**    | Teal gradient border               | Golden amber border with frosted glass effect | "Suika Game" inspired look     |
| **Fonts**         | "Let's go Digital" (retro digital) | Verdana (clean sans-serif)                    | Improved readability           |
| **Shadows**       | Harsh black                        | Warm brown-tinted soft shadows                | Dreamy, cohesive feel          |

---

#### **Major CSS Additions:**

##### **A. Background**

##### **B. Game Board Container**

##### **C. Grid Lines**

##### **D. Sidebar Boxes** (Score, Level, Next Brick)

##### **E. Typography**

##### **F. Button Styles**

##### **G. Menu Overlays**

##### **H. Freeze State**

##### **I. Notifications**

##### **J. Game Over Screen**

## Unexpected Problems and Solutions

### 1. **High Score Persistence Timing Issue**

#### **Problem:**

High scores were only saved to disk when the `GameOverPanel` was displayed. This caused data loss in multiple scenarios:

- **Exiting via window close button** (X) - Score lost
- **Returning to main menu** mid-game - Score lost
- **Application crash** - Score lost
- **Only game over screen** triggered `HighScoreManager.saveHighScore()`

**Root Cause:** Save logic was coupled to UI event (game over panel display) rather than game state change.

**Solution:** **Event-driven save architecture**

- Added `primaryStage.setOnCloseRequest()` handler in `Main.java`
- Modified `GameController.stopGame()` to always save before cleanup
- Called `stopGame()` in all exit paths:
  - Window close
  - Main menu navigation
  - Application exit button
  - Game over screen

---

### 2. **Ghost Piece Position Calculation Complexity**

**Challenge:** Visual synchronization

- Ghost position updated separately from brick position
- **Issue:** Ghost "lagged" behind brick movement (visible stutter)

**Final Solution:** ✅ **Integrated calculation in game loop**

```java
// In SimpleBoard.java
public int calculateGhostPosition() {
    int ghostY = (int) currentOffset.getY();

    // Simulate dropping until collision
    while (isValidMove(currentOffset.getX(), ghostY + 1,
                      brickRotator.getCurrentShape())) {
        ghostY++;
    }

    return ghostY;
}

// Called once per game tick, result stored in ViewData
ViewData viewData = board.getViewData(); // Includes cached ghost position
```

**Optimizations applied:**

1. **Early termination:** Stop calculation if ghost would be below visible area
2. **Delta checking:** Only recalculate if X position or rotation changed
3. **Result caching:** Store in `ViewData` to avoid redundant calculations

---

### 3. **Freeze Power-Up ArrayIndexOutOfBoundsException**

#### **Problem:**

Time Freeze feature caused crashes when new bricks spawned at the top of the board:

```
Exception in thread "JavaFX Application Thread"
java.lang.ArrayIndexOutOfBoundsException: Index -1 out of bounds for length 20
    at SimpleBoard.isValidMove(SimpleBoard.java:145)
```

**Root Cause:** Bricks spawn at Y = 1 (in hidden rows above visible board). The issue occurred because collision detection didn't properly handle the "sky zone" where bricks spawn and rotate before becoming visible.

**Attempt :** Spawn bricks at Y = 2 instead of Y = 1

```java
currentOffset = new Point(4, 2); // Lower spawn point
```

- **Result:** Visible "pop-in" effect, looked unprofessional
- **Issue:** Bricks appeared suddenly in middle of screen instead of sliding from top

**Solution:** **Modified collision detection to handle spawn zone**

Updated `MatrixOperations.intersect()` to allow operations in the hidden spawn area while still preventing invalid moves:

### 4. **Score and High Score Desynchronization**

#### **Problem:**

Current score and high score displayed different values even when current score was higher:

```
Scenario:
- Game 1: Score 3000 → High Score: 3000 ✅
- Game 2: Score 5000 → High Score: 3000 ❌ (should be 5000)
- Game 3: Score 2000 → High Score: 5000 ✅ (finally updates)
```

**Root Cause:** Two separate update paths:

1. `scoreLabel.setText()` - Updated every frame
2. `highScoreLabel.setText()` - Updated only on file load or game over

**Solution:** **JavaFX Property Binding**

```java
// In Score.java
private final IntegerProperty score = new SimpleIntegerProperty(0);
private final IntegerProperty highScore = new SimpleIntegerProperty(0);

public Score() {
    // Auto-update high score when current score exceeds it
    score.addListener((obs, oldVal, newVal) -> {
        if (newVal.intValue() > highScore.get()) {
            highScore.set(newVal.intValue());
        }
    });
}

// In GuiController.java
public void initialize() {
    scoreLabel.textProperty().bind(
        score.scoreProperty().asString()
    );
    highScoreLabel.textProperty().bind(
        score.highScoreProperty().asString()
    );
}
```

**Benefits:**

- Labels update automatically (no manual setText calls)
- High score always synchronized with current score
- Single source of truth (Score class)

---

### 5. **Next Shape Preview: Single vs. Queue**

#### **Problem:**

Original code showed only 1 next brick, but modern Tetris shows 3-5. Expanding from 1 to 3 required architectural changes:

**Original Structure:**

```java
// ViewData.java
private final int[][] nextBrickData; // Single 4x4 matrix

// GuiController.java
GridPane nextBrickPane; // Single preview grid
```

**Issues when scaling to 3 previews:**

1. **Data structure:** `int[][]` → `int[][][]` (array of matrices)
2. **UI layout:** 1 GridPane → 3 GridPanes vertically stacked
3. **Generator:** `getNextBrick()` → `getNextBricks(int count)`
4. **Rendering:** Single update loop → Iterate over preview array

**_Solution:_** **List-based preview system**

```java
// BoardVisuals.java
private List<GridPane> nextBrickGrids = new ArrayList<>();

// BoardInitiator.java
for (int i = 0; i < 3; i++) {
    GridPane preview = createPreviewGrid();
    nextBrickGrids.add(preview);
    nextBrickPane.getChildren().add(preview);
}

// BoardRefresher.java
public void updateNextPreviews(int[][][] bricksData) {
    for (int i = 0; i < bricksData.length; i++) {
        updateSinglePreview(nextBrickGrids.get(i), bricksData[i]);
    }
}
```

**Benefits:**

- Easily scalable to 5+ previews (just change loop limit)
- Single update method for all previews
- Clean separation: data generation vs. rendering

---

### 6. **Brick Generation: Random vs. Fair Distribution**

#### **Problem:**

Using pure `Random.nextInt(7)` for brick generation caused frustrating gameplay:

**Issue A:** Drought problem

- Players could go 30+ bricks without seeing an I-piece
- Mathematically possible: (6/7)^30 = 1.3% chance

**Issue B:** Flood problem

- Sometimes got 5 O-pieces in a row
- Felt "rigged" or broken to players

#### **Initial Implementation:**

```java
// RandomBrickGenerator.java (original)
public Brick getNextBrick() {
    int random = new Random().nextInt(7);
    return switch(random) {
        case 0 -> new IBrick();
        case 1 -> new JBrick();
        // ... etc
    };
}
```

**Solution:** **7-Bag Randomizer (Tetris Standard)**

```java
// RandomBrickGenerator.java (new)
private Queue<Brick> bag = new LinkedList<>();

private void fillBag() {
    List<Brick> allBricks = Arrays.asList(
        new IBrick(), new JBrick(), new LBrick(),
        new OBrick(), new SBrick(), new TBrick(), new ZBrick()
    );
    Collections.shuffle(allBricks); // Random order
    bag.addAll(allBricks);
}

public Brick getNextBrick() {
    if (bag.isEmpty()) {
        fillBag(); // Refill when empty
    }
    return bag.poll();
}
```

**How 7-Bag works:**

1. Create "bag" with one of each brick type (7 total)
2. Shuffle bag randomly
3. Draw bricks from bag in order
4. When bag empty, create new shuffled bag

**Benefits:**

- **Guaranteed fairness:** Every 7 bricks contains all types once
- **Maximum drought:** 12 bricks (current bag + next bag before target brick)
- **Feels random:** Order within each bag is random
- **Industry standard:** Used in all official Tetris games since 2001

---

Jacob Villegas - 20647052
