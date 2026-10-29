# 🚀 Galaxy Trucker — Java Digital Edition (A.Y. 2024-2025)

This project consists of a full-stack, distributed multiplayer Java implementation of the popular board game **Galaxy Trucker** (published by Cranio Creations) for che course of *Software Engineering*. The system features a robust client-server architecture, allowing players to connect via Socket or RMI and interact through either a Command Line Interface (CLI) or a Graphical User Interface (GUI).

* **University:** Politecnico di Milano
* **Professor:** Prof. San Pietro Pierluigi
* **Authors:** [Lorusso Luca](https://github.com/Luc4Lo), [Manzoni Nicola](https://github.com/NicolaManzoni), [Martorano Gabriele](https://github.com/gabrielemartorano04), [Morciano Francesca](https://github.com/francescamorciano)

---

## 📖 Game Rules & Specifications

* **Galaxy Trucker Rulebook:** [English](deliverables/readmeFiles/galaxy-trucker-rules-en.pdf) | [Italian](deliverables/readmeFiles/galaxy-trucker-rules-it.pdf)
* **Project Specifications:** [Official Requirements Document](deliverables/readmeFiles/requirements.pdf)

---

## 🎯 Implemented Features

The project fully implements the core rules of the game alongside advanced network and architectural features required by the assignment:

| Feature | Status | Description |
| :--- | :---: | :--- |
| **Complete Rules** | ✅ | Full implementation of the base game mechanics |
| **CLI** | ✅ | Fully playable Command Line Interface |
| **GUI** | ✅ | Graphical User Interface (Java Swing/JavaFX) |
| **Socket** | ✅ | Network communication via TCP Sockets |
| **RMI** | ✅ | Network communication via Remote Method Invocation |
| **Trial Flight** (Advanced) | ✅ | Ability to test the spaceship before the actual flight |
| **Persistency** (Advanced) | ✅ | Saving and loading game states |
| Multiple Games | ❌ | Concurrent matches on the same server |
| Resilience to Disconnections | ❌ | Reconnecting to an ongoing match after a crash |

---

## 📂 Project Deliverables

The final version of the repository includes all the requested academic deliverables:
- High-level, general UML diagram.
- Detailed UML diagram (generated dynamically from the code).
- Interactive **[JavaDoc](deliverables/javaDoc)** documentation.
- Full working game implementation (Client and Server binaries).
- Complete source code for both the main implementation and the testing suite.

### 🧪 Testing & Coverage
Extensive unit testing was performed on the `Model` and `Controller` components to ensure a solid and bug-free game logic.

![Test Coverage](deliverables/readmeFiles/testCoverage.jpg)

---

## 🚀 How to Run the Game

Follow these instructions to start the server and connect one or more clients.

### 1. Locate the Executables
From your terminal (macOS/Linux) or command prompt/PowerShell (Windows), navigate to the folder containing the compiled `.jar` files:
```bash
cd deliverables/final/jar
```

### 2. Start the Server
Open a terminal window and run the server application. The server must be running before any client attempts to connect.
```bash
java -jar server-app.jar
```

### 3. Start the Clients
Open a new terminal window for each player who wants to join the game, and run the client application:
```bash
java -jar client-app.jar
```

### 4. Play! 🎮
Once the client starts, you will be prompted on-screen to select your preferred Connection Protocol (Socket or RMI) and User Interface (CLI or GUI). Follow the instructions and enjoy the flight!
