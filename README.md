# Java-Student-Management--Basic
Good for all beginners and learners who learned Java Programming. GUI/



# Student Management System (Java Swing)

A beginner-friendly **Java Swing GUI application** for managing students, courses, enrollments, and grades.  
This project is designed to help learners practice **object-oriented programming (OOP)** concepts, GUI design, and event-driven programming in Java.

---

## 🚀 Features
- **Student Management**
  - Add, update, delete, and view student records
  - Auto-generated unique student IDs
  - Validation for required fields

- **Course Enrollment**
  - Predefined courses (CS101, MATH120, ENG101, PHY101, CS201)
  - Enroll students into courses
  - Prevent duplicate enrollments
  - View all enrollment records

- **Grade Management**
  - Assign or update grades (A–F or numeric scores)
  - Dynamic dropdown showing enrolled courses with current grade status
  - Grade table with all records

- **Data Models**
  - `Student` – stores student details
  - `Course` – stores course code and title
  - `Enrollment` – links students to courses with grade tracking

- **User Interface**
  - Tabbed layout for Students, Enrollments, and Grades
  - Tables with scrollable views
  - Dropdowns auto-refresh when data changes
  - Confirmation dialogs for safe operations

---

## 🛠️ Technologies Used
- **Java SE**
- **Swing (javax.swing)**
- **AWT (java.awt)**
- **DefaultTableModel** for table management

---

## 📂 Project Structure
StudentManagementSystem.java
├── Student (model class)
├── Course (model class)
├── Enrollment (model class)
└── Main method (launches GUI)





---

## ▶️ How to Run
1. Clone the repository:
git clone https://github.com/NihalZevy/Java-Student-Management--Basic.git

Navigate to the project folder:
cd Java-Student-Management--Basic

Compile and run:
javac StudentManagementSystem.java
java StudentManagementSystem
/ Also direct use for VS code From Direct link

