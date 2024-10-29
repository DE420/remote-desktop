/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package rmi_remote_desktop;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.UnknownHostException;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

/**
 *
 * @author Admin
 */
public class DesktopServer  extends JFrame implements ActionListener{
    private static final long serialVersionUID = 1L; //tuong thich phien ban
    InetAddress privateIP;
    JTextField password;
    
    public DesktopServer(){
        try{
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        }
        catch (ClassNotFoundException e) {}
        catch (InstantiationException e) {}
        catch (IllegalAccessException e) {}
        catch (UnsupportedLookAndFeelException e) {}
        
        try{
            privateIP = InetAddress.getLocalHost();
        }
        catch (UnknownHostException e) {
            e.printStackTrace();
        }
        
        //man hinh set mat khau cua may server
        JLabel label = new JLabel("Set Password:");
        password = new JTextField(15);
        JButton submit = new JButton("Submit");
        JTextField  IPlabel = new JTextField ();					
        IPlabel.setText("Your Machine' IP Address is:  " + privateIP.getHostAddress());
        IPlabel.setEditable(false);
        IPlabel.setBorder(null);
        
        JPanel westPanel = new JPanel();
        westPanel.setLayout(new BorderLayout());
        westPanel.add(label, BorderLayout.CENTER);
        JPanel eastPanel = new JPanel();
        eastPanel.add(submit);
        JPanel centerPanel = new JPanel();
        centerPanel.add(password);

        JPanel topPanel = new JPanel();
        topPanel.setLayout(new BorderLayout());
        topPanel.add(westPanel, BorderLayout.WEST);
        topPanel.add(centerPanel, BorderLayout.CENTER);
        topPanel.add(eastPanel, BorderLayout.EAST);

        JPanel gridPanel = new JPanel();
        gridPanel.setLayout(new GridLayout(2, 1));
        gridPanel.add(topPanel);  gridPanel.add(IPlabel);
        
        submit.addActionListener(this);
        
        setLayout(new BorderLayout());
        add(new JPanel().add(new JLabel(" ")), BorderLayout.NORTH);
        add(gridPanel, BorderLayout.CENTER);
        add(new JPanel().add(new JLabel(" ")), BorderLayout.SOUTH);

        setVisible(true);
        setSize(400, 130);
        password.requestFocusInWindow();
        setResizable(false);
        setLocation(500, 300);
        setTitle("Set Password!");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
    
    public static void main(String[] args) {
        new DesktopServer();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        dispose();
        try{
            ScreenEvent stub = new ScreenEventImpl(password.getText());
            LocateRegistry.createRegistry(1888);
            Naming.rebind("rmi://" + privateIP.getHostAddress() + ":1888/redesk", stub);
            System.out.println("Server Is Running!!!");
        }
        catch (RemoteException ex) {
            ex.printStackTrace();
        }
        catch (MalformedURLException ex) {
            ex.printStackTrace();
        }
    }
    
}
