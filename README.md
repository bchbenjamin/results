# Academic Evaluation & Leaderboard Portal Architecture

## 1. System Overview
This platform is a Role-Based Access Control (RBAC) web application designed to manage student evaluations, securely isolate grading permissions, and foster gamified academic competition through a customized leaderboard system.

## 2. Role Specifications

### 2.1 Administrator View
* **Permissions:** Full CRUD (Create, Read, Update, Delete) access.
* **Responsibilities:** * Register new students and assign USNs.
  * Modify existing student demographic or batch data.
  * Manage evaluator accounts and subject allocations.
  * Override data in case of entry errors.

### 2.2 Evaluator View
* **Permissions:** Restricted Write / Filtered Read.
* **Responsibilities:**
  * View lists of students relevant to their assigned subject.
  * Input and update marks exclusively for their authorized column (e.g., the "Java" evaluator can only edit the `Java` column).
  * **SIP Mentor Logic:** For the SIP (Student Internship Project) column, write-access is determined by a relational mapping of the student's `Topic`. Evaluators assigned to a specific topic act as the SIP mentor for all students (and teams) sharing that topic.

### 2.3 Student View
* **Permissions:** Read-Only / Self-Isolated.
* **Responsibilities:**
  * Authenticate via USN.
  * View personal academic performance, assigned topics, and team details.
  * View the global and subject-specific leaderboards.

## 3. Core Database Schema

The database relies on normalized tables to enforce access control:

* **Students Table:** `USN` (PK), `Name`, `Batch`, `Team_No`, `Topic`
* **Marks Table:** `USN` (FK), `DSA`, `ADA`, `DBMS`, `Math`, `Python`, `Java`, `SIP`, `Total_Score`
* **Evaluators Table:** `Evaluator_ID` (PK), `Name`, `Core_Subject`
* **SIP_Mapping Table:** `Topic` (PK), `Evaluator_ID` (FK)

## 4. Leaderboard & Tie-Breaker Algorithm
The application features a Top-5 Leaderboard for each individual subject, as well as an overall aggregate leaderboard. To maintain strict rankings without relying on external metadata, the system utilizes an "Internal 1v1 Gauntlet" algorithm for tie-breaking.

**Execution Order:**
1. **Primary Sort:** Compare the scores of the target subject (Descending).
2. **The 1v1 Gauntlet:** If tied, compare the two tied students across the remaining 6 subjects. A student receives 1 point for every subject where their score is strictly greater than their opponent's. The student with the most head-to-head points wins the tie.
3. **Total Aggregate:** If the 1v1 Gauntlet results in a draw (e.g., 3-3), the tie is broken by comparing the students' overall `Total_Score`.
4. **Alphabetical Fallback:** If the total scores are also identical, the final tie-breaker is an alphabetical sort (A-Z) of the students' names.