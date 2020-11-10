import java.util.HashMap;
import java.util.Scanner;

public class Main {
    public static HashMap<String, Integer> CommandsList = new HashMap<>();
    public static String homeDirectory = "/home/mostafa";
    public static String currentDirectory = "/home/mostafa/";
    public static void main(String[] args) {
        initializeMap();
        Parser parser = new Parser();
        Scanner scanner = new Scanner(System.in);
        while(true){
            System.out.print(currentDirectory + "$ ");
            String input = scanner.nextLine();
            String[] commands = input.split(" \\| ");
            for (String command : commands) {
                boolean hasRedirect = false;
                boolean hasTwoRedirect = false;
                String fileName = null;

                if(command.contains(">>")){
                    String[] commandAndFile = command.split(" >> ");
                    command = commandAndFile[0];
                    fileName = commandAndFile[1];
                    hasTwoRedirect = true;
                } else if(command.contains(">")){
                    String[] commandAndFile = command.split(" > ");
                    command = commandAndFile[0];
                    fileName = commandAndFile[1];
                    hasRedirect = true;
                }

                if (parser.parse(command))
                    System.out.println("success");
                else
                    System.out.println("error");

                if(hasRedirect || hasTwoRedirect)
                    System.out.println("Output in file : " + fileName);
            }
        }
    }

    static void initializeMap(){
        CommandsList.put("cd",1);
        CommandsList.put("ls",0);
        CommandsList.put("cp",2);
        CommandsList.put("cat",1);
        CommandsList.put("more",1);
        CommandsList.put("mkdir",1);
        CommandsList.put("rmdir",1);
        CommandsList.put("mv",2);
        CommandsList.put("rm",1);
        CommandsList.put("args",1);
        CommandsList.put("date",0);
        CommandsList.put("help",0);
        CommandsList.put("pwd",0);
        CommandsList.put("clear",0);
        CommandsList.put("exit",0);
    }
}
