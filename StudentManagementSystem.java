import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class StudentManagementSystem extends JFrame {

    // Data storage
    private final List<Student> students = new ArrayList<>();
    private final List<Course> courses = new ArrayList<>();
    private final List<Enrollment> enrollments = new ArrayList<>();

    private JTabbedPane tabbedPane;

    // Student tab components
    private JTextField txtFirstName, txtLastName, txtEmail;
    private JButton btnAdd, btnUpdate, btnDelete, btnClear;
    private JTable tblStudents;
    private DefaultTableModel studentModel;

    // Enrollment tab
    private JComboBox<Student> cbStudentEnroll;
    private JComboBox<Course> cbCourseEnroll;
    private JButton btnEnroll;
    private JTable tblEnrollments;
    private DefaultTableModel enrollModel;

    // Grade tab
    private JComboBox<Student> cbStudentGrade;
    private JComboBox<String> cbCourseGrade;
    private JTextField txtGradeScore;
    private JButton btnAssign;
    private JTable tblGrades;
    private DefaultTableModel gradeModel;

    public StudentManagementSystem() {
        setTitle("Student Management System");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 720);
        setLocationRelativeTo(null);

        // Predefined courses
        courses.add(new Course("CS101", "Introduction to Programming"));
        courses.add(new Course("MATH120", "Calculus I"));
        courses.add(new Course("ENG101", "English Composition"));
        courses.add(new Course("PHY101", "General Physics"));
        courses.add(new Course("CS201", "Data Structures"));

        tabbedPane = new JTabbedPane();
        createStudentManagementTab();
        createEnrollmentTab();
        createGradeManagementTab();

        add(tabbedPane);
    }

    // ──────────────────────────────
    // Student Management Tab
    // ──────────────────────────────

    private void createStudentManagementTab() {
        JPanel panel = new JPanel(new BorderLayout(12, 12));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Form area
        JPanel formPanel = new JPanel(new GridLayout(4, 2, 10, 14));
        formPanel.add(new JLabel("First Name:"));
        txtFirstName = new JTextField(20);
        formPanel.add(txtFirstName);

        formPanel.add(new JLabel("Last Name:"));
        txtLastName = new JTextField(20);
        formPanel.add(txtLastName);

        formPanel.add(new JLabel("Email:"));
        txtEmail = new JTextField(20);
        formPanel.add(txtEmail);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        btnAdd    = new JButton("Add Student");
        btnUpdate = new JButton("Update Selected");
        btnDelete = new JButton("Delete Selected");
        btnClear  = new JButton("Clear Form");
        buttonPanel.add(btnAdd);
        buttonPanel.add(btnUpdate);
        buttonPanel.add(btnDelete);
        buttonPanel.add(btnClear);

        formPanel.add(new JLabel());
        formPanel.add(buttonPanel);

        // Table
        studentModel = new DefaultTableModel(new String[]{"ID", "First Name", "Last Name", "Email"}, 0);
        tblStudents = new JTable(studentModel);
        tblStudents.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane scrollPane = new JScrollPane(tblStudents);

        panel.add(formPanel, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);

        tabbedPane.addTab("Manage Students", panel);

        // Event listeners
        btnAdd.addActionListener(e -> addStudent());
        btnUpdate.addActionListener(e -> updateStudent());
        btnDelete.addActionListener(e -> deleteStudent());
        btnClear.addActionListener(e -> clearStudentForm());

        tblStudents.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && tblStudents.getSelectedRow() >= 0) {
                loadStudentIntoForm();
            }
        });

        refreshStudentTable();
    }

    private void addStudent() {
        String first = txtFirstName.getText().trim();
        String last  = txtLastName.getText().trim();
        String email = txtEmail.getText().trim();

        if (first.isEmpty() || last.isEmpty() || email.isEmpty()) {
            JOptionPane.showMessageDialog(this, "All fields are required.", "Input Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        Student student = new Student(first, last, email);
        students.add(student);

        refreshStudentTable();
        refreshComboBoxesEverywhere();
        clearStudentForm();

        JOptionPane.showMessageDialog(this, "Student added successfully.\nID: " + student.getId());
    }

    private void updateStudent() {
        int selectedRow = tblStudents.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a student first.", "No Selection", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String first = txtFirstName.getText().trim();
        String last  = txtLastName.getText().trim();
        String email = txtEmail.getText().trim();

        if (first.isEmpty() || last.isEmpty() || email.isEmpty()) {
            JOptionPane.showMessageDialog(this, "All fields are required.", "Input Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int id = (Integer) studentModel.getValueAt(selectedRow, 0);
        Student student = findStudentById(id);

        if (student != null) {
            student.setFirstName(first);
            student.setLastName(last);
            student.setEmail(email);

            refreshStudentTable();
            refreshComboBoxesEverywhere();
            JOptionPane.showMessageDialog(this, "Student information updated.");
        }
    }

    private void deleteStudent() {
        int selectedRow = tblStudents.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a student to delete.", "No Selection", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int id = (Integer) studentModel.getValueAt(selectedRow, 0);
        Student student = findStudentById(id);

        if (student == null) return;

        int choice = JOptionPane.showConfirmDialog(this,
                "Delete student:\n" + student.getFullName() + " (ID: " + id + ")\n\nAll course enrollments will also be removed.\n\nContinue?",
                "Confirm Deletion", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);

        if (choice != JOptionPane.YES_OPTION) return;

        students.remove(student);
        enrollments.removeIf(en -> en.student == student);

        refreshStudentTable();
        refreshComboBoxesEverywhere();
        clearStudentForm();

        JOptionPane.showMessageDialog(this, "Student and related records deleted.");
    }

    private void loadStudentIntoForm() {
        int row = tblStudents.getSelectedRow();
        txtFirstName.setText(studentModel.getValueAt(row, 1).toString());
        txtLastName.setText(studentModel.getValueAt(row, 2).toString());
        txtEmail.setText(studentModel.getValueAt(row, 3).toString());
    }

    private void clearStudentForm() {
        txtFirstName.setText("");
        txtLastName.setText("");
        txtEmail.setText("");
        tblStudents.clearSelection();
    }

    private void refreshStudentTable() {
        studentModel.setRowCount(0);
        for (Student s : students) {
            studentModel.addRow(new Object[]{
                    s.getId(),
                    s.getFirstName(),
                    s.getLastName(),
                    s.getEmail()
            });
        }
    }

    private Student findStudentById(int id) {
        for (Student s : students) {
            if (s.getId() == id) return s;
        }
        return null;
    }

    // ──────────────────────────────
    // Enrollment Tab
    // ──────────────────────────────

    private void createEnrollmentTab() {
        JPanel panel = new JPanel(new BorderLayout(12, 12));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JPanel form = new JPanel(new GridLayout(3, 2, 10, 14));
        form.add(new JLabel("Select Student:"));
        cbStudentEnroll = new JComboBox<>();
        form.add(cbStudentEnroll);

        form.add(new JLabel("Select Course:"));
        cbCourseEnroll = new JComboBox<>();
        form.add(cbCourseEnroll);

        btnEnroll = new JButton("Enroll Student");
        form.add(new JLabel());
        form.add(btnEnroll);

        enrollModel = new DefaultTableModel(new String[]{"Student Name", "Course"}, 0);
        tblEnrollments = new JTable(enrollModel);
        JScrollPane scroll = new JScrollPane(tblEnrollments);

        panel.add(form, BorderLayout.NORTH);
        panel.add(scroll, BorderLayout.CENTER);

        tabbedPane.addTab("Course Enrollment", panel);

        btnEnroll.addActionListener(e -> enrollStudentInCourse());

        refreshEnrollmentTab();
    }

    private void enrollStudentInCourse() {
        Student student = (Student) cbStudentEnroll.getSelectedItem();
        Course course  = (Course)  cbCourseEnroll.getSelectedItem();

        if (student == null || course == null) {
            JOptionPane.showMessageDialog(this, "Please select both student and course.", "Selection Required", JOptionPane.ERROR_MESSAGE);
            return;
        }

        for (Enrollment en : enrollments) {
            if (en.student == student && en.course == course) {
                JOptionPane.showMessageDialog(this, "Student is already enrolled in this course.", "Duplicate Enrollment", JOptionPane.WARNING_MESSAGE);
                return;
            }
        }

        enrollments.add(new Enrollment(student, course));

        refreshEnrollmentTable();
        refreshComboBoxesEverywhere();

        JOptionPane.showMessageDialog(this, "Enrollment completed successfully.");
    }

    private void refreshEnrollmentTab() {
        cbStudentEnroll.removeAllItems();
        for (Student s : students) cbStudentEnroll.addItem(s);

        cbCourseEnroll.removeAllItems();
        for (Course c : courses) cbCourseEnroll.addItem(c);

        refreshEnrollmentTable();
    }

    private void refreshEnrollmentTable() {
        enrollModel.setRowCount(0);
        for (Enrollment en : enrollments) {
            enrollModel.addRow(new Object[]{
                    en.student.getFullName(),
                    en.course
            });
        }
    }

    // ──────────────────────────────
    // Grade Management Tab
    // ──────────────────────────────

    private void createGradeManagementTab() {
        JPanel panel = new JPanel(new BorderLayout(12, 12));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JPanel form = new JPanel(new GridLayout(4, 2, 10, 14));
        form.add(new JLabel("Select Student:"));
        cbStudentGrade = new JComboBox<>();
        form.add(cbStudentGrade);

        form.add(new JLabel("Select Course:"));
        cbCourseGrade = new JComboBox<>();
        form.add(cbCourseGrade);

        form.add(new JLabel("Grade (A-F or 0-100):"));
        txtGradeScore = new JTextField();
        form.add(txtGradeScore);

        btnAssign = new JButton("Assign / Update Grade");
        form.add(new JLabel());
        form.add(btnAssign);

        gradeModel = new DefaultTableModel(new String[]{"Student", "Course", "Grade"}, 0);
        tblGrades = new JTable(gradeModel);
        JScrollPane scroll = new JScrollPane(tblGrades);

        panel.add(form, BorderLayout.NORTH);
        panel.add(scroll, BorderLayout.CENTER);

        tabbedPane.addTab("Grade Management", panel);

        cbStudentGrade.addActionListener(e -> refreshCoursesDropdownForSelectedStudent());
        btnAssign.addActionListener(e -> assignGradeToStudent());

        refreshGradeTab();
    }

    private void refreshCoursesDropdownForSelectedStudent() {
        cbCourseGrade.removeAllItems();
        Student selected = (Student) cbStudentGrade.getSelectedItem();
        if (selected == null) return;

        boolean foundAny = false;
        for (Enrollment en : enrollments) {
            if (en.student == selected) {
                String text = en.course.toString() + "  (current: " + (en.grade.isEmpty() ? "None" : en.grade) + ")";
                cbCourseGrade.addItem(text);
                foundAny = true;
            }
        }

        if (!foundAny) {
            cbCourseGrade.addItem("(no courses enrolled yet)");
        }
    }

    private void assignGradeToStudent() {
        Student student = (Student) cbStudentGrade.getSelectedItem();
        if (student == null) {
            JOptionPane.showMessageDialog(this, "Please select a student.", "Input Required", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String selectedItem = (String) cbCourseGrade.getSelectedItem();
        if (selectedItem == null || selectedItem.contains("no courses")) {
            JOptionPane.showMessageDialog(this, "No enrolled courses available for grading.", "No Data", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String gradeInput = txtGradeScore.getText().trim();
        if (gradeInput.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter a grade.", "Input Required", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Extract course code (first part before space)
        String courseCode = selectedItem.split(" ")[0];

        for (Enrollment en : enrollments) {
            if (en.student == student && en.course.getCode().equals(courseCode)) {
                en.grade = gradeInput;
                refreshGradeTable();
                refreshCoursesDropdownForSelectedStudent();
                JOptionPane.showMessageDialog(this, "Grade saved successfully.");
                txtGradeScore.setText("");
                return;
            }
        }
    }

    private void refreshGradeTab() {
        cbStudentGrade.removeAllItems();
        for (Student s : students) cbStudentGrade.addItem(s);

        refreshGradeTable();
    }

    private void refreshGradeTable() {
        gradeModel.setRowCount(0);
        for (Enrollment en : enrollments) {
            gradeModel.addRow(new Object[]{
                    en.student.getFullName(),
                    en.course,
                    en.grade.isEmpty() ? "Not assigned" : en.grade
            });
        }
    }

    // Utility method – refresh all dropdowns & tables that depend on students/enrollments
    private void refreshComboBoxesEverywhere() {
        refreshEnrollmentTab();
        refreshGradeTab();
    }

    // ──────────────────────────────
    // Model Classes
    // ──────────────────────────────

    static class Student {
        private static int nextId = 1001;
        private final int id;
        private String firstName;
        private String lastName;
        private String email;

        public Student(String first, String last, String mail) {
            this.id = nextId++;
            this.firstName = first;
            this.lastName = last;
            this.email = mail;
        }

        public int getId()                  { return id; }
        public String getFirstName()        { return firstName; }
        public void setFirstName(String v)  { firstName = v; }
        public String getLastName()         { return lastName; }
        public void setLastName(String v)   { lastName = v; }
        public String getEmail()            { return email; }
        public void setEmail(String v)      { email = v; }
        public String getFullName()         { return firstName + " " + lastName; }

        @Override
        public String toString() {
            return getFullName() + " (ID " + id + ")";
        }
    }

    static class Course {
        private final String code;
        private final String title;

        public Course(String code, String title) {
            this.code = code;
            this.title = title;
        }

        public String getCode() { return code; }

        @Override
        public String toString() {
            return code + " – " + title;
        }
    }

    static class Enrollment {
        final Student student;
        final Course course;
        String grade = "";

        public Enrollment(Student s, Course c) {
            this.student = s;
            this.course = c;
        }
    }

    // ──────────────────────────────
    // Main method
    // ──────────────────────────────

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new StudentManagementSystem().setVisible(true));
    }
} 