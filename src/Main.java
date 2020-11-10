import java.util.HashMap;
import java.util.Scanner;

public class Main {
    public static HashMap<String, int[]> CommandsList = new HashMap<>();
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
        CommandsList.put("cd", new int[]{0, 1});
        CommandsList.put("ls",new int[]{0, 1});
        CommandsList.put("cp",new int[]{2, 10});
        CommandsList.put("cat",new int[]{1, 1});
        CommandsList.put("more",new int[]{1, 1});
        CommandsList.put("mkdir",new int[]{1, 1});
        CommandsList.put("rmdir",new int[]{1, 1});
        CommandsList.put("mv",new int[]{2, 10});
        CommandsList.put("rm",new int[]{1, 1});
        CommandsList.put("args",new int[]{1, 1});
        CommandsList.put("date",new int[]{0, 0});
        CommandsList.put("help",new int[]{0, 0});
        CommandsList.put("pwd",new int[]{0, 0});
        CommandsList.put("clear",new int[]{0, 0});
        CommandsList.put("exit",new int[]{0, 0});
    }
}
