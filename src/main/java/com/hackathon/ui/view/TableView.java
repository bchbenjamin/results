package com.hackathon.ui.view;

import com.hackathon.dao.ItemDAO;
import com.hackathon.model.Item;
import com.hackathon.ui.NavigationController;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import java.awt.*;
import java.math.BigDecimal;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * Modular Swing View to show database records inside JTable.
 * Implements ViewLifecycle to reload database content automatically when page is navigated to.
 */
public class TableView extends JPanel implements NavigationController.ViewLifecycle {
    private final ItemDAO itemDAO;
    private final NavigationController navController;

    private final JTable itemTable;
    private final ItemTableModel tableModel;
    private final JLabel rowCountLabel;

    public TableView(ItemDAO itemDAO, NavigationController navController) {
        this.itemDAO = itemDAO;
        this.navController = navController;

        // MigLayout configuration:
        // - wrap 1: single column vertical layout
        // - fill: let components expand to occupy vertical/horizontal spaces
        // - Row constraints: [pref!]15[pref!]15[grow, fill]15[pref!] (header, controls, table container, footer)
        setLayout(new MigLayout("insets 30, wrap 1, fill", "[grow, fill]", "[pref!]15[pref!]15[grow, fill]15[pref!]"));

        // Header Section
        JLabel titleLabel = new JLabel("Catalog Inventory");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 22));
        add(titleLabel);

        // Control Panel
        JButton refreshBtn = new JButton("Refresh Data");
        refreshBtn.addActionListener(e -> reloadData());

        JButton addBtn = new JButton("Add Item");
        addBtn.addActionListener(e -> this.navController.showView("FORM_VIEW"));

        JPanel controlPanel = new JPanel(new MigLayout("insets 0, gap 10", "[grow][pref!][pref!]"));
        rowCountLabel = new JLabel("Loading items...");
        rowCountLabel.setFont(new Font("Segoe UI", Font.ITALIC, 13));
        controlPanel.add(rowCountLabel, "grow");
        controlPanel.add(refreshBtn, "width 120!");
        controlPanel.add(addBtn, "width 120!");
        add(controlPanel);

        // JTable Setup
        tableModel = new ItemTableModel();
        itemTable = new JTable(tableModel);
        itemTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        itemTable.getTableHeader().setReorderingAllowed(false);
        itemTable.setRowHeight(25); // Roomy rows for premium look

        JScrollPane scrollPane = new JScrollPane(itemTable);
        add(scrollPane, "grow");

        // Footer Section
        JButton deleteBtn = new JButton("Delete Selected");
        deleteBtn.putClientProperty("JButton.buttonType", "warning"); // Standard warning styling flag if supported
        deleteBtn.addActionListener(e -> deleteSelected());

        JPanel footerPanel = new JPanel(new MigLayout("insets 0, gap 10", "[grow][pref!]"));
        footerPanel.add(new JLabel("Select any row to enable actions."), "grow");
        footerPanel.add(deleteBtn, "width 150!");
        add(footerPanel);
    }

    /**
     * Refreshes model items from DAO cache.
     */
    public void reloadData() {
        try {
            List<Item> items = itemDAO.findAll();
            tableModel.setItems(items);
            rowCountLabel.setText("Found " + items.size() + " item(s) in catalog.");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "Failed to reload data: " + e.getMessage(),
                    "Database Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Deletes the currently highlighted table record.
     */
    private void deleteSelected() {
        int selectedRow = itemTable.getSelectedRow();
        if (selectedRow < 0) {
            JOptionPane.showMessageDialog(this,
                    "Please select an item to delete.",
                    "No Selection",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Convert table row index to model row index (essential in case table sorting is added)
        int modelRow = itemTable.convertRowIndexToModel(selectedRow);
        Item item = tableModel.getItemAt(modelRow);

        int choice = JOptionPane.showConfirmDialog(this,
                "Are you sure you want to delete item '" + item.getName() + "' (ID: " + item.getId() + ")?",
                "Confirm Deletion",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE);

        if (choice == JOptionPane.YES_OPTION) {
            try {
                boolean success = itemDAO.deleteById(item.getId());
                if (success) {
                    reloadData();
                } else {
                    JOptionPane.showMessageDialog(this,
                            "Failed to delete item from database.",
                            "Error",
                            JOptionPane.ERROR_MESSAGE);
                }
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this,
                        "Error deleting item: " + e.getMessage(),
                        "Database Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    @Override
    public void onViewShown() {
        reloadData();
    }

    /**
     * Custom AbstractTableModel implementation representing the Item schema.
     */
    private static class ItemTableModel extends AbstractTableModel {
        private final String[] columnNames = {"ID", "Name", "Description", "Price ($)", "Created At"};
        private final List<Item> items = new ArrayList<>();
        private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        public void setItems(List<Item> newItems) {
            items.clear();
            items.addAll(newItems);
            fireTableDataChanged();
        }

        public Item getItemAt(int rowIndex) {
            return items.get(rowIndex);
        }

        @Override
        public int getRowCount() {
            return items.size();
        }

        @Override
        public int getColumnCount() {
            return columnNames.length;
        }

        @Override
        public String getColumnName(int column) {
            return columnNames[column];
        }

        @Override
        public Object getValueAt(int rowIndex, int columnIndex) {
            Item item = items.get(rowIndex);
            return switch (columnIndex) {
                case 0 -> item.getId();
                case 1 -> item.getName();
                case 2 -> item.getDescription() == null || item.getDescription().isEmpty() ? "-" : item.getDescription();
                case 3 -> item.getPrice() != null ? String.format("%.2f", item.getPrice()) : "0.00";
                case 4 -> item.getCreatedAt() != null ? item.getCreatedAt().format(formatter) : "";
                default -> null;
            };
        }

        @Override
        public Class<?> getColumnClass(int columnIndex) {
            return switch (columnIndex) {
                case 0 -> Long.class;
                case 1, 2, 3, 4 -> String.class;
                default -> Object.class;
            };
        }
    }
}
