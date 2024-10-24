import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Calendar;


    public class Receiver  implements ActionListener {
        JTextField text;
       static JPanel l1;
      static  Box vertical=Box.createVerticalBox();
    static   JFrame f=new JFrame();
       static  DataOutputStream dout;
        Receiver(){
            f.setLayout(null);
            JPanel p1= new JPanel();//if you want to something above the frame we use panel and can devide also
            p1.setBackground(new Color(22, 211, 245));
            p1.setBounds(0,0,500,70);//to set the panel location
            p1.setLayout(null);
            f.add(p1);

            ImageIcon i1=new ImageIcon(ClassLoader.getSystemResource("icons/3.png"));
            Image i2=i1.getImage().getScaledInstance(25,25,Image.SCALE_DEFAULT); //Its is used to scale the image
            ImageIcon i3= new ImageIcon(i2); //converting i2 to imageIcon by creating its object
            JLabel back= new JLabel(i3);
            back.setBounds(5, 20,25,25);
            p1.add(back);

            back.addMouseListener(new MouseAdapter() {
                public void mouseClicked(MouseEvent ae){
                    System.exit(0);
                }

            });
            ImageIcon i4=new ImageIcon(ClassLoader.getSystemResource("icons/2.png"));
            Image i5=i4.getImage().getScaledInstance(50,50,Image.SCALE_DEFAULT);
            ImageIcon i6= new ImageIcon(i5);
            JLabel profile= new JLabel(i6);
            profile.setBounds(40, 10,50,50);
            p1.add(profile);

            ImageIcon i7=new ImageIcon(ClassLoader.getSystemResource("icons/video.png"));
            Image i8=i7.getImage().getScaledInstance(30,30,Image.SCALE_DEFAULT);
            ImageIcon i9= new ImageIcon(i8);
            JLabel video= new JLabel(i9);
            video.setBounds(360, 20,30,30);
            p1.add(video);

            ImageIcon i10=new ImageIcon(ClassLoader.getSystemResource("icons/phone.png"));
            Image i11=i10.getImage().getScaledInstance(35,30,Image.SCALE_DEFAULT);
            ImageIcon i12= new ImageIcon(i11);
            JLabel phone= new JLabel(i12);
            phone.setBounds(420, 20,35,30);
            p1.add(phone);

            ImageIcon i13=new ImageIcon(ClassLoader.getSystemResource("icons/3icon.png"));
            Image i14=i13.getImage().getScaledInstance(10,25,Image.SCALE_DEFAULT);
            ImageIcon i15= new ImageIcon(i14);
            JLabel moreOptions= new JLabel(i15);
            moreOptions.setBounds(480, 20,10,25);
            p1.add(moreOptions);

            JLabel name=new JLabel("HR");
            name.setFont(new Font("SAN_SERIF",Font.BOLD,16));

            name.setBounds(110,15,100,18);
            name.setForeground(Color.WHITE);
            p1.add(name);

            JLabel status=new JLabel("Active Now");
            status.setBounds(110,35,100,14);
            status.setForeground(Color.WHITE);
            p1.add(status);

            l1=new JPanel();
            l1.setBounds(5,75,490,570);
            l1.setBackground(Color.WHITE);
            f.add(l1);

            text=new JTextField();
            text.setBounds(5,655,350,40);
            text.setFont(new Font("SAN_SERIF",Font.PLAIN,16));
            f.add(text);

            JButton send=new JButton("Send");
            send.setBounds(360,655,130,40);
            send.setForeground(Color.white);
            send.addActionListener(this);
            send.setBackground(new Color(22, 211, 245));

            send.setFont(new Font("SAN_SERIF",Font.PLAIN,16));
           f.add(send);

            f.setSize(500,700); //to set the size of the frame
            f.setLocation(850,100); //by default the frame always appear on the left top side but to change that we use this.
            f.setUndecorated(true);//to remove cross and outline of default box that appears
            /*  To get the whole frame*/ f.getContentPane().setBackground(Color.WHITE);
            f.setVisible(true);//by default visibility is always hidden so we have to set it to true after the all the implementations on the frame
        }

        //if we are implementing an interface which contains an abstract method then in your class you have to override that method
        public void actionPerformed(ActionEvent ae){
           try {
               String mssg = text.getText();// to get the text from the textfield

               JPanel p2 = formatLabel(mssg);//as string cannot be directly added we need it to be a panel

               l1.setLayout(new BorderLayout());
               JPanel right = new JPanel(new BorderLayout()); //this helps in aligning the message to the right side
               right.add(p2, BorderLayout.LINE_END);
               vertical.add(right);// to add the messages line by line vertically
               vertical.add(Box.createVerticalStrut(15));//for space between two messages
               l1.add(vertical, BorderLayout.PAGE_START);

               dout.writeUTF(mssg);

               text.setText(" ");
               f.repaint(); //to reload the page so the messages could appear
               f.invalidate();
               f.validate();
           }catch(Exception e){
               e.printStackTrace();
           }
        }

        public static JPanel formatLabel(String mssg){
            JPanel panel=new JPanel();
            panel.setLayout(new BoxLayout(panel,BoxLayout.Y_AXIS));

            JLabel message=new JLabel("<html><p style=\"width: 140px\">"+mssg+"</p></html>");
            message.setFont(new Font("Tahoma",Font.PLAIN,17));
            message.setBackground(new Color(22, 211, 245));
            message.setOpaque(true);
            message.setBorder(new EmptyBorder(15,15,15,50));

            panel.add(message);
            Calendar cal =Calendar.getInstance();
            SimpleDateFormat sdf=new SimpleDateFormat("HH:mm");

            JLabel time=new JLabel();
            time.setText(sdf.format(cal.getTime()));
            panel.add(time);
            return panel;
        }
        public static void main(String[] args) {
            new Receiver();

            try{
                Socket s=new Socket("127.0.0.1",3001);

                DataInputStream din=new DataInputStream(s.getInputStream());
              dout=new DataOutputStream(s.getOutputStream());

                while(true){
                    l1.setLayout(new BorderLayout());
                    String msg=din.readUTF();//to infinitely receive and send messages we use readUTF method here
                    JPanel panel=formatLabel(msg);// as we have to display the mssg in the frame

                    JPanel left=new JPanel(new BorderLayout());
                    left.add(panel,BorderLayout.LINE_START);
                    vertical.add(left);

                    vertical.add(Box.createVerticalStrut(15));
                    l1.add(vertical, BorderLayout.PAGE_START);

                    f.validate();
                }
            } catch (Exception e){
                e.printStackTrace();
            }
        }
    }


