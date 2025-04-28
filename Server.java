import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.*;
class Server{

    BufferedReader br;
    PrintWriter out;
    ServerSocket server;
    Socket socket;

    public Server(){
        try {
            server= new ServerSocket(5000);
            while(true){
                System.out.println("done");
                System.out.println("waiting");
                socket=server.accept();
                br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                out=new PrintWriter(socket.getOutputStream());

                startReading();
                startWriting();
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    

    public void startReading(){
        Runnable r1 = ()->{
            System.out.println("readstarted");
            while (true) { 
                try{
                    String msg = br.readLine();
                    if(msg.equals("exit")){
                        System.out.println("yes exitted fully");
                        break;
                    }
                    System.out.println("client:"+msg);
                }catch(Exception e){
                    e.printStackTrace();
                    break;
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
        System.out.println("sysy");
        new Server();
    }
}