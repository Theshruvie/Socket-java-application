import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.Image;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

class Client extends JFrame{
    Socket socket;
    BufferedReader br;
    PrintWriter out;

    private JLabel heading= new JLabel ("Client Area");
    private JTextArea messageArea = new JTextArea();
    private JTextField messageInput = new JTextField();
    private Font font=new Font("Roboto",Font.PLAIN,20);
    public Client(){
        try {
            System.out.println("sending req to server");
            socket = new Socket("127.0.0.1",5000);
            System.out.println("condition done");

            br= new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out=new PrintWriter(socket.getOutputStream());

            createGUI();
            handleEvents();
            startReading();
            startWriting();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private void handleEvents(){
        messageInput.addKeyListener(new KeyListener(){
            @Override 
            public void keyTyped(KeyEvent e){

            }
            @Override 
            public void keyPressed(KeyEvent e){

            }
            @Override 
            public void keyReleased(KeyEvent e){
                System.out.println("key released" +e.getKeyCode());
                if(e.getKeyCode()==10){
                    System.out.println("you have pressed enter button");
                    String contentToSend=messageInput.getText();
                    messageArea.append("Me: "+contentToSend+"\n");

                    out.println(contentToSend);
                    out.flush();
                    messageInput.setText("");
                    messageInput.requestFocus();

                }
            }
        });

    }

    private void createGUI(){

        this.setTitle("Client Messanger[END]");
        this.setSize(500,500);
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        heading.setFont(font);
        messageArea.setFont(font);
        messageInput.setFont(font);

        ImageIcon originalIcon = new ImageIcon("logo.png");
        Image scaledImage = originalIcon.getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH); // Adjust width and height
        ImageIcon scaledIcon = new ImageIcon(scaledImage);

        heading.setIcon(scaledIcon);
        heading.setHorizontalTextPosition(SwingConstants.CENTER);
        heading.setVerticalTextPosition(SwingConstants.BOTTOM);
        heading.setHorizontalAlignment(SwingConstants.CENTER);
        heading.setBorder(BorderFactory.createEmptyBorder(20,20,20,20));

        heading.setForeground(Color.WHITE);
        heading.setBackground(Color.BLACK);
        heading.setOpaque(true);


        this.setLayout(new BorderLayout());

        this.add(heading,BorderLayout.NORTH);
        this.add(messageArea,BorderLayout.CENTER);
        this.add(messageInput,BorderLayout.SOUTH);

        this.setVisible(true);

    }
    public void startReading(){
        Runnable r1 = ()->{
            System.out.println("readstarted");
            while (true) { 
                try{
                    String msg = br.readLine();
                    if(msg.equals("exit")){
                        System.out.println("Server terminated the chat");
                        JOptionPane.showMessageDialog(this,"Server Terminated the chat");
                        messageInput.setEnabled(false);
                        break;
                    }
                    messageArea.append("Server:"+msg+"\n");
                }catch(Exception e){
                    e.printStackTrace();
                }
            }  
        };
        new Thread(r1).start();
    }
    

    public void startWriting(){
        Runnable r2= ()->{
            System.out.println("writing");
            while (true) { 
                try{
                    BufferedReader br1 =  new BufferedReader(new InputStreamReader(System.in));
                    String content = br1.readLine();
                    out.println(content);
                    out.flush();
                }catch(Exception e){
                    e.printStackTrace();
                }
            }
        };
        new Thread(r2).start();
    }

    public static void main(String[] args) {
        System.out.println("client side");
        new Client();
    }
}
