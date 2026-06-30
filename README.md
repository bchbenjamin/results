<div align="center">
  <h1>🏆 Academic Result Management System</h1>
  <p><strong>A sophisticated, role-based academic portal built for the Java Hackathon!</strong></p>
  
  <p>
    <a href="https://java.com"><img src="https://img.shields.io/badge/Java-17-orange.svg?style=for-the-badge&logo=java" alt="Java 17" /></a>
    <a href="https://mariadb.org/"><img src="https://img.shields.io/badge/MariaDB-003545.svg?style=for-the-badge&logo=mariadb&logoColor=white" alt="MariaDB" /></a>
    <a href="https://maven.apache.org/"><img src="https://img.shields.io/badge/Maven-C71A36.svg?style=for-the-badge&logo=apache-maven&logoColor=white" alt="Maven" /></a>
    <a href="#"><img src="https://img.shields.io/badge/Swing-FlatLaf-blue.svg?style=for-the-badge" alt="FlatLaf" /></a>
  </p>
</div>

---

## 🌟 Overview

The **Academic Result Management System** is a robust desktop application designed to streamline the grading and evaluation process. Replacing flat file spreadsheets with a fully normalized relational database, it provides role-based access control, automated performance metrics, and a dynamic tie-breaking leaderboard.

## ✨ Key Features

- 🔐 **Role-Based Access Control (RBAC):**
  - **Student View:** Read-only dashboard showing personal grades, subject marks, and calculated averages.
  - **Evaluator View:** Strict constraint-based view where evaluators can only edit marks for their specifically assigned subject and allocated SIP topics.
  - **Admin View:** Full CRUD capabilities with powerful inline table editing.
- 🗄️ **MariaDB Integration:** Replaced the legacy in-memory system with a robust, persistent MariaDB connection. Changes made in the GUI are instantly reflected in the database.
- 🏆 **1v1 Gauntlet Leaderboard:** A sophisticated tie-breaking algorithm that runs a head-to-head comparison across 7 subjects to accurately rank the Top 5 students per subject and overall.
- 🎨 **Modern Swing UI:** Powered by the **FlatLaf** Look and Feel for a sleek, student-friendly interface.

## 🚀 Getting Started

### Prerequisites
- **Java 17** or higher
- **Maven** (for dependency management)
- **MariaDB / MySQL** (running on localhost:3306)

### Setup

1. **Database Configuration:**
   Ensure you have a local MariaDB instance running. The application connects to `hackathon_db`. The system will automatically seed all students, evaluators, and mappings on the first run!

2. **Build the Project:**
   ```bash
   mvn clean compile
   ```

3. **Run the Application:**
   ```bash
   mvn exec:java -Dexec.mainClass="com.hackathon.App"
   ```

## 📚 Architecture

Please refer to the enclosed documentation for deeper insights into the project's design:
- [PORTAL_ARCHITECTURE.md](./PORTAL_ARCHITECTURE.md): Database normalization, RBAC guidelines, and tie-breaker algorithms.

---

## 👨‍💻 Team

We are thrilled to submit this project for the Java Hackathon! Connect with us:

<div align="center">
  
  <table>
    <tr>
      <td align="center">
        <b>B C H Benjamin</b><br>
        <a href="https://www.linkedin.com/in/bchbenjamin"><img src="https://img.shields.io/badge/LinkedIn-0077B5?style=for-the-badge&logo=linkedin&logoColor=white" alt="LinkedIn" /></a><br>
        <a href="https://github.com/bchbenjamin"><img src="https://img.shields.io/badge/GitHub-100000?style=for-the-badge&logo=github&logoColor=white" alt="GitHub" /></a>
      </td>
      <td align="center">
        <b>C Yogeetha</b><br>
        <a href="https://www.linkedin.com/in/c-yogeetha"><img src="https://img.shields.io/badge/LinkedIn-0077B5?style=for-the-badge&logo=linkedin&logoColor=white" alt="LinkedIn" /></a><br>
        <a href="https://github.com/gitGojo"><img src="https://img.shields.io/badge/GitHub-100000?style=for-the-badge&logo=github&logoColor=white" alt="GitHub" /></a>
      </td>
    </tr>
  </table>

</div>

<br>
<div align="center">
  <b>Built with ❤️ during the Hackathon</b>
</div>