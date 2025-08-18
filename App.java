import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.Vector;

public class App extends JFrame {
    private JTextField nameField, rollNoField, marksField, searchField;
    private DefaultTableModel tableModel;
    private JTable table;
    private final Font mainFont = new Font("Segoe UI", Font.PLAIN, 14);
    private final Color bgColor = new Color(245, 245, 255);

    public App() {
        setTitle("🎓 Student Info System");
        setSize(850, 550);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        getContentPane().setBackground(bgColor);

        add(createFormPanel(), BorderLayout.WEST);
        add(createSearchPanel(), BorderLayout.NORTH);
        add(createTablePanel(), BorderLayout.CENTER);

        loadData();
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                saveData();
            }
        });
    }

    private JPanel createFormPanel() {
        JPanel formPanel = new JPanel(new GridLayout(5, 2, 10, 10));
        formPanel.setBorder(BorderFactory.createTitledBorder("Add New Student"));
        formPanel.setBackground(bgColor);

        JLabel nameLabel = createLabel("Name:");
        nameField = createTextField();
        JLabel rollLabel = createLabel("Roll No:");
        rollNoField = createTextField();
        JLabel marksLabel = createLabel("Marks:");
        marksField = createTextField();

        JButton addButton = createButton("Add Student", e -> addStudent());
        JButton deleteButton = createButton("Delete Selected", e -> deleteStudent());

        formPanel.add(nameLabel); formPanel.add(nameField);
        formPanel.add(rollLabel); formPanel.add(rollNoField);
        formPanel.add(marksLabel); formPanel.add(marksField);
        formPanel.add(addButton); formPanel.add(deleteButton);

        return formPanel;
    }

    private JPanel createSearchPanel() {
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        searchPanel.setBackground(bgColor);
        searchPanel.add(createLabel("Search:"));
        searchField = createTextField();
        searchField.setPreferredSize(new Dimension(200, 28));

        searchField.getDocument().addDocumentListener(new DocumentListener() {
            public void insertUpdate(DocumentEvent e) { filterTable(); }
            public void removeUpdate(DocumentEvent e) { filterTable(); }
            public void changedUpdate(DocumentEvent e) { filterTable(); }
        });

        searchPanel.add(searchField);
        return searchPanel;
    }

    private JScrollPane createTablePanel() {
        tableModel = new DefaultTableModel(new String[]{"Name", "Roll No", "Marks", "Grade"}, 0);
        table = new JTable(tableModel);
        table.setFont(mainFont);
        table.setRowHeight(25);
        table.setBackground(Color.WHITE);

        table.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                                                           boolean isSelected, boolean hasFocus, int row, int col) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, col);
                if (!isSelected) {
                    c.setBackground(row % 2 == 0 ? new Color(235, 245, 255) : Color.WHITE);
                }
                setHorizontalAlignment(CENTER);
                return c;
            }
        });

        table.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2 && table.getSelectedRow() != -1) {
                    showProfile(table.getSelectedRow());
                }
            }
        });

        return new JScrollPane(table);
    }

    private void addStudent() {
        String name = nameField.getText().trim();
        String rollNo = rollNoField.getText().trim();
        String marksText = marksField.getText().trim();

        if (isAnyFieldEmpty(name, rollNo, marksText)) return;
        if (isDuplicateRollNo(rollNo)) return;

        int marks;
        try {
            marks = Integer.parseInt(marksText);
        } catch (NumberFormatException ex) {
            showMessage("Please enter valid marks!");
            return;
        }

        String grade = GradeCalculator.calculate(marks);
        tableModel.addRow(new Object[]{name, rollNo, marks, grade});
        clearFields();
    }

    private void deleteStudent() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow >= 0) {
            tableModel.removeRow(selectedRow);
        } else {
            showMessage("Select a row to delete!");
        }
    }

    private void filterTable() {
        String keyword = searchField.getText().toLowerCase();
        DefaultTableModel filteredModel = new DefaultTableModel(new String[]{"Name", "Roll No", "Marks", "Grade"}, 0);

        for (int i = 0; i < tableModel.getRowCount(); i++) {
            String name = tableModel.getValueAt(i, 0).toString().toLowerCase();
            String roll = tableModel.getValueAt(i, 1).toString().toLowerCase();
            if (name.contains(keyword) || roll.contains(keyword)) {
                Vector<?> row = (Vector<?>) tableModel.getDataVector().get(i);
                filteredModel.addRow(row.toArray());
            }
        }
        table.setModel(filteredModel);
    }

    private void showProfile(int row) {
        JOptionPane.showMessageDialog(this,
                "📄 Student Profile:\n\n" +
                        "Name: " + table.getValueAt(row, 0) + "\n" +
                        "Roll No: " + table.getValueAt(row, 1) + "\n" +
                        "Marks: " + table.getValueAt(row, 2) + "\n" +
                        "Grade: " + table.getValueAt(row, 3),
                "Student Profile",
                JOptionPane.INFORMATION_MESSAGE);
    }

    private void saveData() {
        try (PrintWriter pw = new PrintWriter(new FileWriter("students.csv"))) {
            for (int i = 0; i < tableModel.getRowCount(); i++) {
                pw.println(
                        tableModel.getValueAt(i, 0) + "," +
                                tableModel.getValueAt(i, 1) + "," +
                                tableModel.getValueAt(i, 2) + "," +
                                tableModel.getValueAt(i, 3)
                );
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadData() {
        File file = new File("students.csv");
        if (!file.exists()) return;

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                tableModel.addRow(line.split(","));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private JLabel createLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(mainFont);
        return label;
    }

    private JTextField createTextField() {
        JTextField tf = new JTextField();
        tf.setFont(mainFont);
        return tf;
    }

    private JButton createButton(String text, ActionListener action) {
        JButton btn = new JButton(text);
        btn.setFont(mainFont);
        btn.setBackground(new Color(70, 130, 180));
        btn.setForeground(Color.WHITE);
        btn.addActionListener(action);
        return btn;
    }

    private boolean isAnyFieldEmpty(String name, String rollNo, String marks) {
        if (name.isEmpty() || rollNo.isEmpty() || marks.isEmpty()) {
            showMessage("All fields are required!");
            return true;
        }
        return false;
    }

    private boolean isDuplicateRollNo(String rollNo) {
        for (int i = 0; i < tableModel.getRowCount(); i++) {
            if (tableModel.getValueAt(i, 1).toString().equalsIgnoreCase(rollNo)) {
                showMessage("Roll No already exists!");
                return true;
            }
        }
        return false;
    }

    private void showMessage(String msg) {
        JOptionPane.showMessageDialog(this, msg);
    }

    private void clearFields() {
        nameField.setText("");
        rollNoField.setText("");
        marksField.setText("");
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new App().setVisible(true);
        });
    }
}

class GradeCalculator {
    public static String calculate(int marks) {
        if (marks >= 90) return "A+";
        if (marks >= 80) return "A";
        if (marks >= 70) return "B";
        if (marks >= 60) return "C";
        if (marks >= 50) return "D";
        return "F";
    }
}
