/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package rmi_remote_desktop;

import java.awt.AWTException;
import java.awt.Dimension;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.event.InputEvent;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import javax.imageio.ImageIO;

/**
 *
 * @author Admin
 */
public class ScreenEventImpl extends UnicastRemoteObject implements ScreenEvent{
    private static final long serialVersionUID = 1L; //tuong thich phien ban khi truyen lop qua mang
    Robot robot = null;
    String password;
    double width, height;
    
    public ScreenEventImpl(String password) throws RemoteException{
        super();
        this.password = password;
    }

    @Override
    public boolean checkPassword(String inputPassword) throws RemoteException {
        if(password.equals(inputPassword))
            return true;
        return false;
    }

    @Override
    public byte[] sendScreen() throws RemoteException {
        byte[] byteArray = null;
        try{
            //lay moi truong do hoa cua may chu
            GraphicsEnvironment graphicsEnv = GraphicsEnvironment.getLocalGraphicsEnvironment();
            GraphicsDevice graphicsScreen = graphicsEnv.getDefaultScreenDevice();
            robot = new Robot(graphicsScreen);
            
            //lay kich thuoc man hinh may chu
            Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
            width = dimension.getWidth();
            height = dimension.getHeight();
            
            //gui man hinh may chu
            BufferedImage bImage = robot.createScreenCapture(new Rectangle(dimension));					//Capturing screen of the Server as an image
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            ImageIO.write(bImage, "jpeg", bos);										//Image is converted into an array of bytes
            bos.flush();
            
            byteArray = bos.toByteArray();
            bos.close();
        }
        catch(AWTException ex) {
            ex.printStackTrace();
        }
        catch (IOException ex) {
            ex.printStackTrace();
        }
        return byteArray;
    }

    @Override
    public double getWidth() throws RemoteException {
        return width;
    }

    @Override
    public double getHeight() throws RemoteException {
        return height;
    }

    @Override
    public void mouseMovedEvent(int xScale, int yScale) throws RemoteException {
        robot.mouseMove(xScale, yScale);
    }

    @Override
    public void mousePressedEvent(int buttonPressed) throws RemoteException {
        if(buttonPressed == 1) 									//Left Key
            robot.mousePress(InputEvent.BUTTON1_MASK);
        else if(buttonPressed == 2)								//Middle Key
            robot.mousePress(InputEvent.BUTTON2_MASK);
        else if(buttonPressed == 3)								//Right Key
            robot.mousePress(InputEvent.BUTTON3_MASK);
    }

    @Override
    public void mouseReleasedEvent(int buttonReleased) throws RemoteException {
        if(buttonReleased == 1)									//Left Key
            robot.mouseRelease(InputEvent.BUTTON1_MASK);
        else if(buttonReleased == 2)							//Middle Key
            robot.mouseRelease(InputEvent.BUTTON2_MASK);
        else if(buttonReleased == 3)							//Right Key
            robot.mouseRelease(InputEvent.BUTTON3_MASK);
    }

    @Override
    public void keyPressed(int keyPressed) throws RemoteException {
        robot.keyPress(keyPressed);
    }

    @Override
    public void keyReleased(int keyReleased) throws RemoteException {
        robot.keyRelease(keyReleased);
    }
    
}
