import java.util.HashMap;
import java.util.Scanner;

public class Main {
    public static HashMap<String, Integer> Commands = new HashMap<>();
    public static void main(String[] args) {
        initializeMap();
        Parser parser = new Parser();
        Scanner scanner = new Scanner(System.in);
        while(true){
            System.out.print("Enter Your Command : ");
            String command = scanner.nextLine();
            if(parser.parse(command))
                System.out.println("success");
        }
    }

    static void initializeMap(){
        Commands.put("cd",1);
        Commands.put("ls",0);
        Commands.put("cp",2);
        Commands.put("cat",1);
        Commands.put("more",1);
        Commands.put("mkdir",1);
        Commands.put("rmdir",1);
        Commands.put("mv",2);
        Commands.put("rm",1);
        Commands.put("args",1);
        Commands.put("date",0);
        Commands.put("help",0);
        Commands.put("pwd",0);
        Commands.put("clear",0);
        Commands.put("exit",0);
    }
}
