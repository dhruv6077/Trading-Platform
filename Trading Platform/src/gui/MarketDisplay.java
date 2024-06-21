/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gui;

import java.awt.*;
import java.util.Date;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

import Exceptions.InvalidPriceOperation;
import User.ProductManager;
import Price.*;

/**
 * @author hieldc
 */
public class MarketDisplay extends JFrame {

    public static final HashMap<String, Double> basePrices = new HashMap<>();

    MarketDisplay() {
        initComponents();
        pack();

        basePrices.put("WMT", 60.17);
        basePrices.put("TGT", 177.21);
        basePrices.put("AMZN", 180.38);
        basePrices.put("TSLA", 175.79);
        basePrices.put("AAPL", 171.48);
        basePrices.put("MSFT", 420.72);
        basePrices.put("PYPL", 66.99);

        DefaultTableCellRenderer renderer = new MarketTableCellRenderer();
        renderer.setHorizontalAlignment(JLabel.CENTER);
        for (int i = 0; i < marketTable.getColumnCount(); i++) {
            marketTable.setDefaultRenderer(marketTable.getColumnClass(i), renderer);
        }
        // repaint to show table cell changes
        marketTable.updateUI();

        ((DefaultTableModel) marketTable.getModel()).setRowCount(0);

        stateLabel.setText("Last Update: " + new Date());

        addSymbols();

    }

    private void addSymbols() {

        for (String product : ProductManager.getInstance().getProductList()) {

            String[] row = {product,
                    "0",
                    "$0.00",
                    "$0.00",
                    "$0.00",
                    "0",
                    "$0.00"};
            ((DefaultTableModel) marketTable.getModel()).addRow(row);

            stateLabel.setText("Last Update: " + new Date());
        }
        //pack();


    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        JPanel jPanel1 = new JPanel();
        JLabel jLabel2 = new JLabel();
        JLabel userNameText = new JLabel();
        JLabel actionText = new JLabel();
        stateLabel = new JLabel();
        JLabel stateText = new JLabel();
        JPanel jPanel2 = new JPanel();
        JScrollPane jScrollPane1 = new JScrollPane();
        marketTable = new JTable();
        JPanel jPanel3 = new JPanel();
        tickerText = new JTextField();
        JScrollPane jScrollPane2 = new JScrollPane();
        JTextArea activityText = new JTextArea();

        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        jPanel1.setBorder(BorderFactory.createTitledBorder("Details"));

        jLabel2.setText("CURRENT MARKET DISPLAY");

        stateLabel.setText("Last Update:");


        GroupLayout jPanel1Layout = new GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
                jPanel1Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel1Layout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(jPanel1Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                        .addComponent(jLabel2))
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel1Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                        .addGroup(jPanel1Layout.createSequentialGroup()
                                                .addComponent(userNameText, GroupLayout.PREFERRED_SIZE, 158, GroupLayout.PREFERRED_SIZE)
                                                .addGap(18, 18, 18)
                                                .addComponent(stateLabel)
                                                .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                                                .addComponent(stateText, GroupLayout.PREFERRED_SIZE, 168, GroupLayout.PREFERRED_SIZE)
                                                .addGap(0, 0, Short.MAX_VALUE))
                                        .addComponent(actionText, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED))
        );
        jPanel1Layout.setVerticalGroup(
                jPanel1Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                                .addGroup(jPanel1Layout.createParallelGroup(GroupLayout.Alignment.TRAILING)
                                        .addComponent(userNameText, GroupLayout.Alignment.CENTER, GroupLayout.PREFERRED_SIZE, 19, GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jLabel2, GroupLayout.Alignment.CENTER)
                                        .addComponent(stateLabel, GroupLayout.Alignment.CENTER)
                                        .addComponent(stateText, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel1Layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                        .addComponent(actionText)))

        );

        jPanel1Layout.linkSize(SwingConstants.VERTICAL, actionText, userNameText);

        jPanel2.setBorder(BorderFactory.createTitledBorder("Market"));

        marketTable.setModel(new DefaultTableModel(
                new Object[][]{
                        {null, null, null, null, null, null, null},
                        {null, null, null, null, null, null, null},
                        {null, null, null, null, null, null, null},
                        {null, null, null, null, null, null, null}
                },
                new String[]{
                        "Symbol", "Buy Volume", "Buy Price", "Market Width", "Sell Price", "Sell Volume", "Last Sale"
                }
        ) {
            final Class[] types = new Class[]{
                    String.class, String.class, String.class, String.class, String.class, String.class, String.class
            };
            final boolean[] canEdit = new boolean[]{
                    false, false, false, false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types[columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit[columnIndex];
            }
        });
        jScrollPane1.setViewportView(marketTable);


        GroupLayout jPanel2Layout = new GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
                jPanel2Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addComponent(jScrollPane1, GroupLayout.DEFAULT_SIZE, 717, Short.MAX_VALUE));

        jPanel2Layout.setVerticalGroup(
                jPanel2Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jScrollPane1, GroupLayout.PREFERRED_SIZE, 135, GroupLayout.PREFERRED_SIZE))
        );

        jPanel3.setBorder(BorderFactory.createTitledBorder("Ticker"));

        tickerText.setFont(new Font("Consolas", Font.BOLD, 16)); // NOI18N


        GroupLayout jPanel3Layout = new GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
                jPanel3Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addComponent(tickerText)
        );
        jPanel3Layout.setVerticalGroup(
                jPanel3Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addComponent(tickerText, GroupLayout.DEFAULT_SIZE, 39, Short.MAX_VALUE)
        );


        activityText.setColumns(20);
        activityText.setFont(new Font("Tahoma", Font.PLAIN, 10)); // NOI18N
        activityText.setRows(5);
        jScrollPane2.setViewportView(activityText);

        GroupLayout layout = new GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addComponent(jPanel1, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jPanel2, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jPanel3, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addComponent(jPanel1, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                .addGap(5, 5, 5)
                                .addComponent(jPanel2, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jPanel3, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents


    void updateMarketData(String product, Price bp, int bv, Price sp, int sv) throws InvalidPriceOperation {
        int row = getRowForProduct(product);
        if (row < 0) {
            return;
        }

        Price buyP = bp == null ? PriceFactory.makePrice("0.00") : bp;
        Price sellP = sp == null ? PriceFactory.makePrice("0.00") : sp;

        String width = "";
        try {
            Price w = sellP.subtract(buyP);
            if (w.greaterOrEqual(PriceFactory.makePrice("0.00"))) {
                width = w.toString();
            } else {
                width = "$0.00";
            }
        } catch (InvalidPriceOperation ex) {
            Logger.getLogger(MarketDisplay.class.getName()).log(Level.SEVERE, null, ex);
        }


        marketTable.getModel().setValueAt(bv, row, 1);
        marketTable.getModel().setValueAt(buyP.toString(), row, 2);
        marketTable.getModel().setValueAt(width, row, 3);
        marketTable.getModel().setValueAt(sellP.toString(), row, 4);
        marketTable.getModel().setValueAt(sv, row, 5);
        marketTable.getModel().setValueAt(111, row, 6);

        Price p = Math.random() < 0.5 ? bp : sp;
        if (p == null) {
            p = PriceFactory.makePrice("0.00");
        }
        marketTable.getModel().setValueAt(p, row, 6);

        stateLabel.setText("Last Update: " + new Date());
        pack();
    }

    void updateTicker(String product, Price p, char direction) {

        int row = getRowForProduct(product);
        if (row < 0) {
            return;
        }

        String newData = " " + product + " " + p + direction + "  ";
        String s = tickerText.getText() + newData;

        int fieldWidth = tickerText.getWidth();
        int dataWidth = tickerText.getFontMetrics(tickerText.getFont()).stringWidth(s);

        if (dataWidth > fieldWidth) {
            s = s.substring(newData.length());
        }

        tickerText.setText(s);

    }

    private int getRowForProduct(String p) {
        int rows = (marketTable.getModel()).getRowCount();
        for (int i = 0; i < rows; i++) {
            if ((marketTable.getModel()).getValueAt(i, 0).equals(p)) {
                return i;
            }
        }
        return -1;
    }

    public void shutdown() {
        setVisible(false);
        dispose();
    }

    private JTable marketTable;
    private JLabel stateLabel;
    private JTextField tickerText;
}
