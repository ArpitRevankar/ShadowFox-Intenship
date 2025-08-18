import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.Vector;

public class StudentInfoSystem extends JFrame {

    private JTable table;
    private DefaultTableModel model;
    private JTextField tfId, tfName, tfMarks, tfCourse, tfYear, tfSearch;

    public StudentInfoSystem() {
        setTitle("Enhanced Student Information System");
        setSize(800, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Table Setup
        model = new DefaultTableModel(new String[]{"ID", "Name", "Course", "Year", "Marks", "Grade"}, 0);
        table = new JTable(model);
        JScrollPane scrollPane = new JScrollPane(table);

        // Input Panel
        JPanel inputPanel = new JPanel(new GridLayout(3, 4, 10, 10));
        tfId = new JTextField();
        tfName = new JTextField();
        tfCourse = new JTextField();
        tfYear = new JTextField();
        tfMarks = new JTextField();

        JButton addButton = new JButton("Add Student");
        JButton clearButton = new JButton("Clear Form");
        JButton deleteButton = new JButton("Delete Student");

        inputPanel.add(new JLabel("ID:"));
        inputPanel.add(tfId);
        inputPanel.add(new JLabel("Name:"));
        inputPanel.add(tfName);
        inputPanel.add(new JLabel("Course:"));
        inputPanel.add(tfCourse);
        inputPanel.add(new JLabel("Year:"));
        inputPanel.add(tfYear);
        inputPanel.add(new JLabel("Marks:"));
        inputPanel.add(tfMarks);
        inputPanel.add(addButton);
        inputPanel.add(clearButton);

        // Search & Sort Panel
        JPanel controlPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        tfSearch = new JTextField(15);
        JButton searchButton = new JButton("Search");
        JButton resetButton = new JButton("Reset");
        JButton sortByNameButton = new JButton("Sort by Name");
        JButton sortByMarksButton = new JButton("Sort by Marks");

        controlPanel.add(new JLabel("Search:"));
        controlPanel.add(tfSearch);
        controlPanel.add(searchButton);
        controlPanel.add(resetButton);
        controlPanel.add(sortByNameButton);
        controlPanel.add(sortByMarksButton);
        controlPanel.add(deleteButton);

        add(inputPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        add(controlPanel, BorderLayout.SOUTH);

        // Add Button Action
        addButton.addActionListener(e -> {
            String id = tfId.getText().trim();
            String name = tfName.getText().trim();
            String course = tfCourse.getText().trim();
            String year = tfYear.getText().trim();
            String marksStr = tfMarks.getText().trim();

            if (id.isEmpty() || name.isEmpty() || course.isEmpty() || year.isEmpty() || marksStr.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please fill all fields!");
                return;
            }

            try {
                int marks = Integer.parseInt(marksStr);
                String grade = calculateGrade(marks);

                model.addRow(new Object[]{id, name, course, year, marks, grade});
                clearForm();
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Marks must be a number!");
            }
        });

        // Clear Button Action
        clearButton.addActionListener(e -> clearForm());

        // Delete Button Action
        deleteButton.addActionListener(e -> {
            int selectedRow = table.getSelectedRow();
            if (selectedRow != -1) {
                model.removeRow(selectedRow);
            } else {
                JOptionPane.showMessageDialog(this, "Please select a student to delete.");
            }
        });

        // Search Action
        searchButton.addActionListener(e -> {
            String query = tfSearch.getText().trim().toLowerCase();
            if (query.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Enter something to search!");
                return;
            }

            for (int i = 0; i < model.getRowCount(); i++) {
                String id = model.getValueAt(i, 0).toString().toLowerCase();
                String name = model.getValueAt(i, 1).toString().toLowerCase();
                if (id.contains(query) || name.contains(query)) {
                    table.setRowSelectionInterval(i, i);
                    return;
                }
            }
            JOptionPane.showMessageDialog(this, "No match found.");
        });

        // Reset Button Action
        resetButton.addActionListener(e -> {
            tfSearch.setText("");
            table.clearSelection();
        });

        // Sort by Name
        sortByNameButton.addActionListener(e -> {
            model.getDataVector().sort((o1, o2) -> 
                ((Vector) o1).get(1).toString().compareToIgnoreCase(((Vector) o2).get(1).toString()));
            model.fireTableDataChanged();
        });

        // Sort by Marks
        sortByMarksButton.addActionListener(e -> {
            model.getDataVector().sort((o1, o2) -> {
                int m1 = Integer.parseInt(((Vector) o1).get(4).toString());
                int m2 = Integer.parseInt(((Vector) o2).get(4).toString());
                return Integer.compare(m2, m1); // descending
            });
            model.fireTableDataChanged();
        });
    }

    private String calculateGrade(int marks) {
        if (marks >= 90) return "A";
        else if (marks >= 75) return "B";
        else if (marks >= 60) return "C";
        else if (marks >= 40) return "D";
        else return "F";
    }

    private void clearForm() {
        tfId.setText("");
        tfName.setText("");
        tfCourse.setText("");
        tfYear.setText("");
        tfMarks.setText("");
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new StudentInfoSystem().setVisible(true));
    }
}

