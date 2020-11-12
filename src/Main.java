import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

public class Main {
    public static HashMap<String, int[]> CommandsList = new HashMap<>();
    final public static String homeDirectory = "/home/mostafa";
    public static String currentDirectory = "/home/mostafa";
    public static Parser parser = new Parser();
    public static Terminal terminal = new Terminal();
    public static void main(String[] args) {
        initializeMap();
        Scanner scanner = new Scanner(System.in);
        while(true){
            System.out.print("\n" + currentDirectory + "$ ");
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

                if (parser.parse(command)) {
                    invokeCommands();
                }
                else
                    System.out.println("error");

                if(hasRedirect || hasTwoRedirect)
                    System.out.println("Output in file : " + fileName);
            }
        }
    }

    static void invokeCommands(){
        switch(parser.getCmd()) {
            case "cd":
                cd();
                break;
            case "ls":
                ls();
                break;
            case "cp":
                cp();
                break;
            case "cat":
                cat();
                break;
            case "more":
                more();
                break;
            default:
                System.out.println("error");
        }
    }
    static void cd(){
        if(parser.getArguments().isEmpty()){
            terminal.cd();
        } else {
            terminal.cd(parser.getArguments().get(0));
        }
    }
    static void ls(){
        if(parser.getArguments().isEmpty()){
            ArrayList<String> dirs = terminal.ls();
            for(int i = 0 ; i < dirs.size() ; i++){
                if(i % 5 == 0 && i != 0)
                    System.out.println('\n');
                System.out.print(dirs.get(i)+ "    ");
            }
        } else {
            ArrayList<String> dirs = terminal.ls(parser.getArguments().get(0));
            for(int i = 0 ; i < dirs.size() ; i++){
                if(i % 5 == 0 && i != 0)
                    System.out.println('\n');
                System.out.print(dirs.get(i) + "    ");
            }
        }
    }
    static void cp(){
        int argSize = parser.getArguments().size();
        for(int i = 0 ; i < argSize - 1 ; i++){
            terminal.cp(parser.getArguments().get(i) , parser.getArguments().get(argSize - 1));
        }
    }
    static void cat(){
        int argSize = parser.getArguments().size();
        for(int i = 0 ; i < argSize; i++){
            terminal.cat(parser.getArguments().get(i));
        }
    }
    static void more(){
        int argSize = parser.getArguments().size();
        for(int i = 0 ; i < argSize; i++){
            terminal.more(parser.getArguments().get(i));
        }
    }
    static void initializeMap(){
        CommandsList.put("cd", new int[]{0, 1});
        CommandsList.put("ls",new int[]{0, 1});
        CommandsList.put("cp",new int[]{2, 10});
        CommandsList.put("cat",new int[]{1, 10});
        CommandsList.put("more",new int[]{1, 10});
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
