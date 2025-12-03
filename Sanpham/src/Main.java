import controller.ProductController;
import dao.ProductDAO;
import database.DatabaseConnection;
import view.ProductPanel;
import javax.swing.*;
import java.awt.*;

public class Main extends JFrame {
    
    public Main() {
        // Khởi tạo database trước
        DatabaseConnection.initDatabase();
        
        initComponents();
    }

    private void initComponents() {
        setTitle("Hệ thống quản lý cửa hàng tạp hóa");
        setSize(1000, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        // Header
        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(new Color(41, 128, 185));
        headerPanel.setPreferredSize(new Dimension(0, 60));
        JLabel lblTitle = new JLabel("QUẢN LÝ SẢN PHẨM TẠP HÓA");
        lblTitle.setFont(new Font("Arial", Font.BOLD, 24));
        lblTitle.setForeground(Color.WHITE);
        headerPanel.add(lblTitle);
        add(headerPanel, BorderLayout.NORTH);

        // Initialize MVC
        ProductDAO productDAO = new ProductDAO();
        ProductPanel productPanel = new ProductPanel();
        new ProductController(productDAO, productPanel);

        add(productPanel, BorderLayout.CENTER);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception e) {
                e.printStackTrace();
            }
            new Main().setVisible(true);
        });
    }
}