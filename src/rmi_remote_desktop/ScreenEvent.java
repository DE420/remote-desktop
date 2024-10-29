/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package rmi_remote_desktop;

import java.rmi.Remote;
import java.rmi.RemoteException;
/**
 *
 * @author Admin
 */
public interface ScreenEvent extends Remote{
    //check pass
    public boolean checkPassword(String inputPassword) throws RemoteException;
    //gui man hinh
    public byte[] sendScreen() throws RemoteException;
    public double getWidth() throws RemoteException;
    public double getHeight() throws RemoteException;
    //xu ly chuot
    public void mouseMovedEvent(int xScale, int yScale) throws RemoteException;
    public void mousePressedEvent(int buttonPressed) throws RemoteException;
    public void mouseReleasedEvent(int buttonReleased) throws RemoteException;
    //xu ly ban phim
    public void keyPressed(int keyPressed) throws RemoteException;
    public void keyReleased(int keyReleased) throws RemoteException;
}
