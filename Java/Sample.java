import java.awt.*;
import javax.swing.*;

public class Sample extends JFrame {

    Sample() {
        Container c = this.getContentPane();
        c.setLayout(new FlowLayout());
        JLabel ul = new JLabel("Username");
        JTextField ut = new JTextField(15);
        JLabel pl = new JLabel("Password");
        JPasswordField pt = new JPasswordField(15);
        JButton lb = new JButton("Login");
        JButton cb = new JButton("Cancel");
        c.add(ul); c.add(ut);
        c.add(pl); c.add(pt);
        c.add(lb); c.add(cb);
    }

    public static void main(String args[]) {
        Sample f = new Sample();
        f.setTitle("My First Swing frame");
        f.setSize(300, 200);
        f.setVisible(true);
    }
}