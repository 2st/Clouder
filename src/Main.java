import sun.net.ftp.FtpClient;
import sun.net.ftp.FtpDirEntry;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.net.*;
import java.util.ArrayList;
import java.util.Iterator;

public class Main {
    public static FtpClient client;
    public static String command;
    public static BufferedReader scan;

    public static void main(String[] args) throws Exception {
        Login login = new Login(); //private class

        InetAddress ip = InetAddress.getByName(login.ip());
        byte[] bytes = ip.getAddress();
        InetAddress address = InetAddress.getByAddress(bytes);
        SocketAddress socket = new InetSocketAddress(address, 21);
        client = FtpClient.create().connect(socket);
        client.login(login.name(),login.pass().toCharArray());
        client.changeToParentDirectory().changeDirectory("files");
        System.out.println("Commands:\n1.Get files list" +
                "\n2.Upload <file path>" +
                "\n3.Download <filename>" +
                "\n4.Delete <filename>" +
                "\n5.Exit");
        scan = new BufferedReader(new InputStreamReader(System.in));
        boolean inwork = true;
        while (inwork) {
            try {
                System.out.print("\nEnter command: ");
                command = scan.readLine();
                byte num = Byte.valueOf(command);
                switch (num) {
                    case 1:
                        getFilelist();
                        break;
                    case 2:
                        upload(); 
                        break;
                    case 5:
                        inwork = false;
                        break;
                }
            } catch (Exception exc) {
                System.out.println("Please enter a correct value");
            }

        }
    }
    public static void getFilelist() throws Exception{
        ArrayList<String> fileList = new ArrayList<>();
        Iterator<FtpDirEntry> iter = client.listFiles(client.getWorkingDirectory());
        while (iter.hasNext()) {
            String tmpName = iter.next().getName();
            if (!tmpName.equals(".") && !tmpName.equals("..")) {
                fileList.add(tmpName);
            }
        }
        for (String s : fileList) {
            System.out.println(s);
        }
    }
    public static void upload() throws Exception {
        System.out.print("File name: ");
        String filename = scan.readLine();
        FileInputStream fileInpStream = new FileInputStream(filename);
        client.putFile(filename, fileInpStream);
    }
}
