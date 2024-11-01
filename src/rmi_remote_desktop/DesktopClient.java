/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package rmi_remote_desktop;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GraphicsEnvironment;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.beans.PropertyVetoException;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDesktopPane;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

/**
 *
 * @author Admin
 */
public class DesktopClient extends JFrame implements ActionListener{
    private static final long serialVersionUID = 1L;
    ScreenEvent stub;
    JTextField serverIP, password;
    double stubWidth, stubHeight;
    
    public DesktopClient(){
        try{
            //cai dat giao dien cua so giong voi he thong
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        }
        catch (ClassNotFoundException e) {}
        catch (InstantiationException e) {}
        catch (IllegalAccessException e) {}
        catch (UnsupportedLookAndFeelException e) {} 
        
        //tao cua so nhap dia chi ip va mk
        JLabel IPlabel = new JLabel("Server IP: ");
        JLabel passwordLabel = new JLabel("Password:");
        serverIP = new JTextField(15);
        password = new JTextField(15);
        JButton submit = new JButton("Submit");
        
        JPanel panel1 = new JPanel();
        panel1.setLayout(new BorderLayout());
        panel1.add(IPlabel, BorderLayout.CENTER);

        JPanel panel2 = new JPanel();
        panel2.add(serverIP);

        JPanel topPanel = new JPanel();
        topPanel.add(panel1);
        topPanel.add(panel2);

        JPanel panel3 = new JPanel();
        panel3.setLayout(new BorderLayout());
        panel3.add(passwordLabel, BorderLayout.CENTER);

        JPanel panel4 = new JPanel();
        panel4.add(password);

        JPanel midPanel = new JPanel();
        midPanel.add(panel3);
        midPanel.add(panel4);

        JPanel bottomPanel = new JPanel();
        bottomPanel.add(submit);

        JPanel gridPanel = new JPanel();
        gridPanel.setLayout(new GridLayout(3, 1));
        gridPanel.add(topPanel);  
        gridPanel.add(midPanel); 
        gridPanel.add(bottomPanel);
        
        submit.addActionListener(this);
        
        setLayout(new BorderLayout());
        add(new JPanel().add(new JLabel(" ")), BorderLayout.NORTH);
        add(gridPanel, BorderLayout.CENTER);
        add(new JPanel().add(new JLabel(" ")), BorderLayout.SOUTH);

        setVisible(true);
        setSize(330, 175);
        setResizable(false);
        setLocation(500, 300);
        setTitle("Enter Password to Connect!");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
    
    public static void main(String[] args) {
        new DesktopClient();
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
        try{
            stub = (ScreenEvent) Naming.lookup("rmi://" + serverIP.getText() + ":1888/redesk");
            if(!(stub.checkPassword(password.getText()))){
                System.out.println("Wrong password!");
                System.exit(0);
            }
            else{
                dispose();
                System.out.println("Sucessfully connected to the server!");
            }
            
            //luu chieu dai, chieu rong cua man hinh may server
            stubWidth = stub.getWidth();
            stubHeight = stub.getHeight();
            
            new ScreenFrame();
        }
        catch (MalformedURLException ex) {
            ex.printStackTrace();
        } 
        catch (RemoteException ex) {
            ex.printStackTrace();
        } 
        catch (NotBoundException ex) {
            ex.printStackTrace();
        }
    }
    
    class ScreenFrame extends JFrame implements KeyListener, MouseMotionListener, MouseListener {
    private static final long serialVersionUID = 1L;
    JLabel label;
    JPanel panel;
    JInternalFrame internalFrame;
    
    public ScreenFrame() {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        }
        catch (ClassNotFoundException e) {}
        catch (InstantiationException e) {}
        catch (IllegalAccessException e) {}
        catch (UnsupportedLookAndFeelException e) {}
        
        // Tạo các components
        label = new JLabel();
        JDesktopPane desktopPane = new JDesktopPane();
        add(desktopPane, BorderLayout.CENTER);
        panel = new JPanel();
        panel.add(label);
        
        internalFrame = new JInternalFrame("Screen", true, true, true);
        internalFrame.setLayout(new BorderLayout());
        internalFrame.getContentPane().add(panel, BorderLayout.CENTER);
        
        // Cài đặt kích thước
        Rectangle r = GraphicsEnvironment.getLocalGraphicsEnvironment().getMaximumWindowBounds();
        setSize(r.width, r.height);
        
        setMaximumSize(new Dimension(r.width, r.height));
        setMinimumSize(new Dimension((int)(r.width /1.2), (int)(r.height /1.2)));
        
        internalFrame.setSize(getWidth(), getHeight());
        internalFrame.setBorder(null);
        ((javax.swing.plaf.basic.BasicInternalFrameUI) internalFrame.getUI()).setNorthPane(null);
        
        desktopPane.add(internalFrame);
        
        // Thêm các listener cho cả panel và internalFrame
        panel.addKeyListener(this);
        panel.addMouseListener(this);
        panel.addMouseMotionListener(this);
        
        label.addKeyListener(this);
        internalFrame.addKeyListener(this);
        
        // Đặt focusable cho cả panel và internalFrame
        panel.setFocusable(true);
        label.setFocusable(true);
        internalFrame.setFocusable(true);
        
        setVisible(true);
        internalFrame.setVisible(true);
        
        try {
            internalFrame.setMaximum(true);
        } catch (PropertyVetoException e) {
            e.printStackTrace();
        }
        
        setTitle("Remote Desktop");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        // Thêm FocusListener để debug focus
        panel.addFocusListener(new java.awt.event.FocusListener() {
            @Override
            public void focusGained(java.awt.event.FocusEvent evt) {
                System.out.println("Panel gained focus");
            }
            
            @Override
            public void focusLost(java.awt.event.FocusEvent evt) {
                System.out.println("Panel lost focus");
            }
        });
        
        // Request focus cho panel
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                panel.requestFocusInWindow();
            }
        });
        
        // Thread nhận màn hình
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                while(true) {
                    try {
                        byte[] bytes = new byte[1024 * 1024];
                        bytes = stub.sendScreen();
                        BufferedImage bImage = ImageIO.read(new ByteArrayInputStream(bytes));
                        Image img = bImage.getScaledInstance(panel.getWidth(), panel.getHeight(), BufferedImage.SCALE_SMOOTH);
                        label.setIcon(new ImageIcon(img));
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }                
        });
        thread.start();
    }

    // Các methods xử lý sự kiện giữ nguyên
    @Override
    public void keyTyped(KeyEvent e) {}

    @Override
    public void keyPressed(KeyEvent e) {
        try {
            int keyCode = e.getKeyCode();
            // Thêm debug log
            System.out.println("Client sending keyPress: " + keyCode + 
                             " (" + KeyEvent.getKeyText(keyCode) + ")");
            stub.keyPressed(keyCode);
        } catch (RemoteException ex) {
            System.out.println("Failed to send keyPress event to server");
            ex.printStackTrace();
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        try {
            int keyCode = e.getKeyCode();
            // Thêm debug log
            System.out.println("Client sending keyRelease: " + keyCode + 
                             " (" + KeyEvent.getKeyText(keyCode) + ")");
            stub.keyReleased(keyCode);
        } catch (RemoteException ex) {
            System.out.println("Failed to send keyRelease event to server");
            ex.printStackTrace();
        }
    }

        @Override
        public void mouseDragged(MouseEvent e) {}

        @Override
        public void mouseMoved(MouseEvent e) {
            double xAxis = (double) stubWidth / panel.getWidth();
            double yAxis = (double) stubHeight / panel.getHeight();
            try {
                stub.mouseMovedEvent((int) (e.getX() * xAxis), (int) (e.getY() * yAxis));
            } 
            catch (RemoteException ex) {
                ex.printStackTrace();
            }        
        }

        @Override
        public void mouseClicked(MouseEvent e) {}

        @Override
        public void mousePressed(MouseEvent e) {
            int buttonPressed = e.getButton();
            try {
                stub.mousePressedEvent(buttonPressed);
                System.out.println("button: " + e.getButton());
            } 
            catch (RemoteException ex) {
                ex.printStackTrace();
            }        
        }

        @Override
        public void mouseReleased(MouseEvent e) {
            int buttonReleased = e.getButton();
            try {
                stub.mouseReleasedEvent(buttonReleased);
                System.out.println("button: " + e.getButton());
            }
            catch (RemoteException ex) {
                ex.printStackTrace();
            }        
        }

        @Override
        public void mouseEntered(MouseEvent e) {}

        @Override
        public void mouseExited(MouseEvent e) {}
        
    }
}
