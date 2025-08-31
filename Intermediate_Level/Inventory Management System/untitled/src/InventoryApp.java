import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.awt.event.*;
import java.io.FileWriter;
import java.io.IOException;

public class InventoryApp extends JFrame {
    private DefaultTableModel model;
    private JTable table;
    private JTextField nameField, quantityField, priceField, searchField;
    private JLabel statusLabel;

    public InventoryApp() {
        setTitle("Inventory Management System");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());


        JPanel topPanel = new JPanel(new BorderLayout());
        JLabel searchLabel = new JLabel("Search: ");
        searchField = new JTextField();
        searchField.addKeyListener(new KeyAdapter() {
            public void keyReleased(KeyEvent e) {
                search(searchField.getText());
            }
        });
        topPanel.add(searchLabel, BorderLayout.WEST);
        topPanel.add(searchField, BorderLayout.CENTER);
        add(topPanel, BorderLayout.NORTH);

        JPanel leftPanel = new JPanel(new GridLayout(10, 1, 5, 5));
        leftPanel.setBorder(BorderFactory.createTitledBorder("Add / Update Item"));
        leftPanel.setPreferredSize(new Dimension(250, 0));

        nameField = new JTextField();
        quantityField = new JTextField();
        priceField = new JTextField();

        leftPanel.add(new JLabel("Item Name:"));
        leftPanel.add(nameField);
        leftPanel.add(new JLabel("Quantity:"));
        leftPanel.add(quantityField);
        leftPanel.add(new JLabel("Price:"));
        leftPanel.add(priceField);

        JButton addButton = new JButton("Add Item");
        JButton updateButton = new JButton("Update Item");
        JButton deleteButton = new JButton("Delete Item");
        JButton exportButton = new JButton("Export CSV");

        addButton.addActionListener(e -> addItem());
        updateButton.addActionListener(e -> updateItem());
        deleteButton.addActionListener(e -> deleteItem());
        exportButton.addActionListener(e -> exportCSV());

        leftPanel.add(addButton);
        leftPanel.add(updateButton);
        leftPanel.add(deleteButton);
        leftPanel.add(exportButton);

        add(leftPanel, BorderLayout.WEST);

        String[] columns = {"Item Name", "Quantity", "Price"};
        model = new DefaultTableModel(columns, 0);
        table = new JTable(model);
        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane, BorderLayout.CENTER);

        statusLabel = new JLabel("Ready");
        statusLabel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        add(statusLabel, BorderLayout.SOUTH);

        setVisible(true);
    }

    private void addItem() {
        try {
            String name = nameField.getText();
            int quantity = Integer.parseInt(quantityField.getText());
            double price = Double.parseDouble(priceField.getText());

            if (name.isEmpty()) {
                showStatus("Item name cannot be empty", Color.RED);
                return;
            }

            InventoryItem item = new InventoryItem(name, quantity, price);
            model.addRow(new Object[]{item.getName(), item.getQuantity(), item.getPrice()});
            clearFields();
            showStatus("Item Added Successfully", Color.GREEN);
        } catch (NumberFormatException e) {
            showStatus("Quantity & Price must be numbers", Color.RED);
        }
    }

    private void updateItem() {
        int row = table.getSelectedRow();
        if (row == -1) {
            showStatus("Select an item to update", Color.RED);
            return;
        }

        try {
            String name = nameField.getText();
            int quantity = Integer.parseInt(quantityField.getText());
            double price = Double.parseDouble(priceField.getText());

            InventoryItem updatedItem = new InventoryItem(name, quantity, price);

            model.setValueAt(updatedItem.getName(), row, 0);
            model.setValueAt(updatedItem.getQuantity(), row, 1);
            model.setValueAt(updatedItem.getPrice(), row, 2);

            clearFields();
            showStatus("Item Updated Successfully", Color.BLUE);
        } catch (NumberFormatException e) {
            showStatus("Quantity & Price must be numbers", Color.RED);
        }
    }

    private void deleteItem() {
        int row = table.getSelectedRow();
        if (row == -1) {
            showStatus("Select an item to delete", Color.RED);
            return;
        }
        int confirm = JOptionPane.showConfirmDialog(this, "Are you sure?", "Delete Item", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            model.removeRow(row);
            showStatus("Item Deleted", Color.ORANGE);
        }
    }

    private void search(String keyword) {
        TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(model);
        table.setRowSorter(sorter);
        sorter.setRowFilter(RowFilter.regexFilter("(?i)" + keyword));
    }

    private void exportCSV() {
        try (FileWriter writer = new FileWriter("inventory.csv")) {
            for (int i = 0; i < model.getRowCount(); i++) {
                writer.write(model.getValueAt(i, 0) + "," +
                        model.getValueAt(i, 1) + "," +
                        model.getValueAt(i, 2) + "\n");
            }
            showStatus("Inventory exported to inventory.csv", Color.MAGENTA);
        } catch (IOException e) {
            showStatus("Error exporting CSV", Color.RED);
        }
    }

    private void clearFields() {
        nameField.setText("");
        quantityField.setText("");
        priceField.setText("");
    }

    private void showStatus(String message, Color color) {
        statusLabel.setText(message);
        statusLabel.setForeground(color);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(InventoryApp::new);
    }
}

