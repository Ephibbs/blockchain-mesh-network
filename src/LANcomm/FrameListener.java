package LANcomm;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

public class FrameListener implements WindowListener, KeyListener {
    private final LANChat parent;

    public FrameListener(LANChat parentArg) {
        parent = parentArg;
    }

    @Override
    public void windowOpened(WindowEvent we) {

    }

    @Override
    public void windowClosing(WindowEvent we) {
        parent.quit();
    }

    @Override
    public void windowClosed(WindowEvent we) {

    }

    @Override
    public void windowIconified(WindowEvent we) {

    }

    @Override
    public void windowDeiconified(WindowEvent we) {

    }

    @Override
    public void windowActivated(WindowEvent we) {

    }

    @Override
    public void windowDeactivated(WindowEvent we) {

    }

    @Override
    public void keyTyped(KeyEvent ke) {
        parent.keyTyped(ke);
    }

    @Override
    public void keyPressed(KeyEvent ke) {

    }

    @Override
    public void keyReleased(KeyEvent ke) {

    }

}