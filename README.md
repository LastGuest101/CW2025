# Tetris Game Coursework

> A polished, modern Tetris clone built with JavaFX. Features a custom "Time Freeze" mechanic, and a cozy, pastel aesthetic inspired by Suika Game.🍉
> 
GitHub Repository: https://github.com/LastGuest101/CW2025


## 🛠️ Compilation & Installation

### 1. Prerequisites (Crucial Step)
Before running the game, you **must** ensure you have the correct Java Development Kit (JDK) installed.

* **Java Version:** This project requires **JDK 23** or higher.
* **Maven:** You do **not** need to install Maven manually (the project includes a wrapper), but if you do, use version 3.8+.

**How to verify your Java version:**
Open your terminal (Command Prompt/Terminal) and type:
```sh

java -version
```
If the output says version "23..." (or higher), you are ready.

If it says version "1.8", 17, or 21, the game will not compile. Please download and install JDK 23 from Oracle or Adoptium.

Option 1: Running via Command Line (Recommended)
This method uses the bundled Maven Wrapper (mvnw), which automatically downloads the correct dependencies.

Open your terminal (Command Prompt, PowerShell, or Terminal).

Clone the repository:

```sh

git clone https://github.com/LastGuest101/CW2025
```
Navigate to the project folder:
```sh
cd CW2025
```
(Note: Ensure you are in the folder containing mvnw and pom.xml)

Run the Game:

Windows:

DOS
```sh
mvnw.cmd clean javafx:run
```
Mac / Linux:

```sh
chmod +x mvnw
./mvnw clean javafx:run
```
(The first run may take a minute to download dependencies.)

Option 2: Running in IntelliJ IDEA
The easiest way to run the project is by importing it directly from GitHub.

Clone from Version Control:

Open IntelliJ IDEA.

On the Welcome screen, click "Get from VCS" (or go to File > New > Project from Version Control).

Paste your repository 
```sh
URL: https://github.com/LastGuest101/CW2025
```
Click Clone.

Configure JDK 23:

Go to File > Project Structure... (or press Ctrl+Alt+Shift+S).

Click on Project in the left sidebar.

SDK: Ensure it is set to 23 (e.g., "23 Oracle OpenJDK").

If 23 is missing: Click the dropdown -> Add SDK -> Download JDK -> Select Version 23.

Language Level: Ensure it is set to 23 - String templates....

Click Apply and OK.

Sync Maven:

Look for the Maven tab on the right sidebar.

Click the Reload All Maven Projects button (spinning arrows icon).

Wait for the indexing bar at the bottom right to finish.

Run:

Navigate to src/main/java/com/tetris/Main.java.

Click the green Run (▶) button next to the class declaration.

Building an Executable (Optional)
If you want to build a standalone JAR file to share:

Run the package command:

Windows: mvnw.cmd package

Mac/Linux: ./mvnw package

Find the JAR:

Go to the newly created target folder.

You will see a file named something like CW2025-1.0-SNAPSHOT.jar.

Run it using: java -jar target/CW2025-1.0-SNAPSHOT.jar
