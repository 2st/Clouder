import sun.net.ftp.FtpClient;
import sun.net.ftp.FtpDirEntry;
import java.net.*;
import java.util.Iterator;

public class Main {
    public static void main(String[] args) throws Exception {
        Login login = new Login(); //private class

        InetAddress ip = InetAddress.getByName(login.ip());
        byte[] bytes = ip.getAddress();
        InetAddress address = InetAddress.getByAddress(bytes);

        SocketAddress socket = new InetSocketAddress(address, 21);
        FtpClient client = FtpClient.create().connect(socket);
        client.login(login.name(),login.pass().toCharArray());
        client.changeToParentDirectory().changeDirectory("files");
        System.out.println(client.getWorkingDirectory());

        Iterator<FtpDirEntry> iter = client.listFiles(client.getWorkingDirectory());
        while (iter.hasNext()) {
            String tmpName = iter.next().getName();
            if (!tmpName.equals(".") && !tmpName.equals("..")) {
                System.out.println(tmpName);
            }
        }
    }
}
