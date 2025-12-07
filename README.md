# Tetris Game Coursework

A polished, modern Tetris clone built with **JavaFX**, featuring:

- A custom **“Time Freeze”** mechanic
- A cozy, pastel aesthetic inspired by **Suika Game** 🍉

GitHub Repository: <https://github.com/LastGuest101/CW2025>

---

## 🛠️ Requirements

Before running the game, ensure you have the correct tools installed.

- **Java:** **JDK 23** or higher
- **Maven:** Not required globally (project uses Maven Wrapper), but if installed, use **3.8+**

### ✅ Check your Java version

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

## 🚀 Running the Game

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

## 🛠️ Troubleshooting Issues

### ⚠️ `mvnw.cmd` fails with a JAVA_HOME error

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

### 📦 Dependencies

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
