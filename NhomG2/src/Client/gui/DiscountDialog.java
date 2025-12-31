package Client.gui;

import Client.ClientConnection;
import Server.model.*;
import javax.swing.*;
import com.toedter.calendar.JDateChooser;
import java.awt.*;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

/**
 * DIALOG THÊM/SỬA MÃ GIẢM GIÁ
 */
public class DiscountDialog extends JDialog {
    private ClientConnection connection;
    private Discount discount;
    private boolean isUpdate;
    
    private JTextField txtCode;
    private JTextField txtPercentage;
    private JDateChooser dateStart;
    private JDateChooser dateEnd;
    private JCheckBox chkActive;
    private JButton btnSave;
    private JButton btnCancel;
    
    private final Color PRIMARY_COLOR = new Color(41, 128, 185);
    private final Color SUCCESS_COLOR = new Color(39, 174, 96);

    public DiscountDialog(Frame parent, ClientConnection connection, Discount discount) {
        super(parent, discount == null ? "➕ Thêm mã giảm giá" : "✏️ Cập nhật mã giảm giá", true);
        
        this.connection = connection;
        this.discount = discount;
        this.isUpdate = (discount != null);
        
        initComponents();
        
        if (isUpdate) {
            fillData();
            txtCode.setEditable(false);
            txtCode.setBackground(new Color(236, 240, 241));
        }
    }

    private void initComponents() {
        setSize(550, 550);
        setLocationRelativeTo(getParent());
        setResizable(false);

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout(15, 15));
        mainPanel.setBackground(new Color(236, 240, 241));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Title
        JLabel lblTitle = new JLabel(isUpdate ? "✏️ CẬP NHẬT MÃ GIẢM GIÁ" : "➕ THÊM MÃ GIẢM GIÁ");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 20));
        lblTitle.setForeground(PRIMARY_COLOR);
        lblTitle.setHorizontalAlignment(SwingConstants.CENTER);
        mainPanel.add(lblTitle, BorderLayout.NORTH);

        // Form Panel
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(Color.WHITE);
        formPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(189, 195, 199), 1),
            BorderFactory.createEmptyBorder(20, 20, 20, 20)
        ));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(10, 10, 10, 10);

        // Mã giảm giá
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 0;
        JLabel lblCode = new JLabel("Mã giảm giá:");
        lblCode.setFont(new Font("Segoe UI", Font.BOLD, 13));
        formPanel.add(lblCode, gbc);
        
        gbc.gridx = 1;
        gbc.weightx = 1.0;
        txtCode = new JTextField(20);
        txtCode.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        txtCode.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(189, 195, 199), 1),
            BorderFactory.createEmptyBorder(8, 10, 8, 10)
        ));
        formPanel.add(txtCode, gbc);

        // Phần trăm giảm
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weightx = 0;
        JLabel lblPercentage = new JLabel("Phần trăm giảm (%):");
        lblPercentage.setFont(new Font("Segoe UI", Font.BOLD, 13));
        formPanel.add(lblPercentage, gbc);
        
        gbc.gridx = 1;
        gbc.weightx = 1.0;
        txtPercentage = new JTextField(20);
        txtPercentage.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        txtPercentage.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(189, 195, 199), 1),
            BorderFactory.createEmptyBorder(8, 10, 8, 10)
        ));
        formPanel.add(txtPercentage, gbc);

        // Ngày bắt đầu
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.weightx = 0;
        JLabel lblStartDate = new JLabel("Ngày bắt đầu:");
        lblStartDate.setFont(new Font("Segoe UI", Font.BOLD, 13));
        formPanel.add(lblStartDate, gbc);
        
        gbc.gridx = 1;
        gbc.weightx = 1.0;
        dateStart = new JDateChooser();
        dateStart.setDateFormatString("dd/MM/yyyy");
        dateStart.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        dateStart.setPreferredSize(new Dimension(200, 35));
        formPanel.add(dateStart, gbc);

        // Ngày kết thúc
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.weightx = 0;
        JLabel lblEndDate = new JLabel("Ngày kết thúc:");
        lblEndDate.setFont(new Font("Segoe UI", Font.BOLD, 13));
        formPanel.add(lblEndDate, gbc);
        
        gbc.gridx = 1;
        gbc.weightx = 1.0;
        dateEnd = new JDateChooser();
        dateEnd.setDateFormatString("dd/MM/yyyy");
        dateEnd.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        dateEnd.setPreferredSize(new Dimension(200, 35));
        formPanel.add(dateEnd, gbc);

        // Active
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.weightx = 0;
        JLabel lblActive = new JLabel("Trạng thái:");
        lblActive.setFont(new Font("Segoe UI", Font.BOLD, 13));
        formPanel.add(lblActive, gbc);
        
        gbc.gridx = 1;
        gbc.weightx = 1.0;
        chkActive = new JCheckBox("Kích hoạt mã giảm giá");
        chkActive.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        chkActive.setBackground(Color.WHITE);
        chkActive.setSelected(true);
        formPanel.add(chkActive, gbc);

        mainPanel.add(formPanel, BorderLayout.CENTER);

        // Button Panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 15));
        buttonPanel.setBackground(new Color(236, 240, 241));
        
        btnSave = createStyledButton(isUpdate ? "✅ Cập nhật" : "➕ Thêm", SUCCESS_COLOR, 140);
        btnSave.addActionListener(e -> save());
        
        btnCancel = createStyledButton("❌ Hủy", new Color(149, 165, 166), 140);
        btnCancel.addActionListener(e -> dispose());
        
        buttonPanel.add(btnSave);
        buttonPanel.add(btnCancel);
        
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        add(mainPanel);
    }

    private JButton createStyledButton(String text, Color color, int width) {
        JButton button = new JButton(text);
        button.setFont(new Font("Segoe UI", Font.BOLD, 13));
        button.setBackground(color);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setPreferredSize(new Dimension(width, 40));
        
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(color.darker());
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(color);
            }
        });
        
        return button;
    }

    private void fillData() {
        txtCode.setText(discount.getCode());
        txtPercentage.setText(String.valueOf(discount.getPercentage()));
        
        if (discount.getStartDate() != null) {
            dateStart.setDate(Date.from(discount.getStartDate()
                .atStartOfDay(ZoneId.systemDefault()).toInstant()));
        }
        
        if (discount.getEndDate() != null) {
            dateEnd.setDate(Date.from(discount.getEndDate()
                .atStartOfDay(ZoneId.systemDefault()).toInstant()));
        }
        
        chkActive.setSelected(discount.isActive());
    }

    private void save() {
        try {
            String code = txtCode.getText().trim().toUpperCase();
            String percentageStr = txtPercentage.getText().trim();
            Date startDate = dateStart.getDate();
            Date endDate = dateEnd.getDate();
            boolean active = chkActive.isSelected();
            
            // Validate
            if (code.isEmpty() || percentageStr.isEmpty() || startDate == null || endDate == null) {
                JOptionPane.showMessageDialog(this, 
                    "⚠️ Vui lòng nhập đầy đủ thông tin!", 
                    "Cảnh báo", 
                    JOptionPane.WARNING_MESSAGE);
                return;
            }
            
            double percentage = Double.parseDouble(percentageStr);
            
            if (percentage <= 0 || percentage > 100) {
                JOptionPane.showMessageDialog(this, 
                    "⚠️ Phần trăm giảm phải từ 0 đến 100!", 
                    "Cảnh báo", 
                    JOptionPane.WARNING_MESSAGE);
                return;
            }
            
            LocalDate start = startDate.toInstant()
                .atZone(ZoneId.systemDefault()).toLocalDate();
            LocalDate end = endDate.toInstant()
                .atZone(ZoneId.systemDefault()).toLocalDate();
            
            if (end.isBefore(start)) {
                JOptionPane.showMessageDialog(this, 
                    "⚠️ Ngày kết thúc phải sau ngày bắt đầu!", 
                    "Cảnh báo", 
                    JOptionPane.WARNING_MESSAGE);
                return;
            }
            
            // Create discount object
            Discount d = new Discount(code, percentage, start, end);
            d.setActive(active);
            
            Response response;
            
            if (isUpdate) {
                d.setId(discount.getId());
                response = connection.updateDiscount(d);
            } else {
                response = connection.addDiscount(d);
            }
            
            if (response.isSuccess()) {
                JOptionPane.showMessageDialog(this, 
                    "✅ " + response.getMessage(), 
                    "Thành công", 
                    JOptionPane.INFORMATION_MESSAGE);
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, 
                    "❌ " + response.getMessage(), 
                    "Lỗi", 
                    JOptionPane.ERROR_MESSAGE);
            }
            
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, 
                "❌ Phần trăm giảm không hợp lệ!", 
                "Lỗi", 
                JOptionPane.ERROR_MESSAGE);
        }
    }
}