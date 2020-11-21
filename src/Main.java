import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
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
                    String data = invokeCommands(hasRedirect || hasTwoRedirect);
                    if(hasRedirect)
                    {
                        createFile(fileName);
                        writeToFile(data , fileName, false);
                    } else if (hasTwoRedirect){
                        createFile(fileName);
                        writeToFile(data , fileName, true);
                    } else
                        System.out.println(data);
                }

            }
        }
    }

    static String invokeCommands(boolean redirect){
        switch(parser.getCmd()) {
            case "cd":
                return cd();
            case "ls":
                return ls();
            case "cp":
                return cp();
            case "cat":
                return cat();
            case "more":
            	if(redirect)
            		return cat();
                return more();
            case "mkdir":
                return mkdir();
            case "rmdir":
                return rmdir();
            case "mv":
                return mv();
            case "rm":
                return rm();
            case "args":
                return args();
            case "date":
                return date();
            case "help":
                return help();
            case "pwd":
                return pwd();
            case "clear":
                return clear();
            case "exit":
            	System.exit(0);
            default:
                return "error";
        }
    }
    static String cd(){
        if(parser.getArguments().isEmpty()){
            return terminal.cd();
        } else {
            return terminal.cd(parser.getArguments().get(0));
        }
    }
    static String ls(){
        String data = "";
        if(parser.getArguments().isEmpty()){
            ArrayList<String> dirs = terminal.ls();
            for(int i = 0 ; i < dirs.size() ; i++){
                if(i % 5 == 0 && i != 0)
                    data = data + '\n';
                data = data + dirs.get(i)+ "    ";
            }
        } else {
            ArrayList<String> dirs = terminal.ls(parser.getArguments().get(0));
            for(int i = 0 ; i < dirs.size() ; i++){
                if(i % 5 == 0 && i != 0)
                    data = data + '\n';
                data = data + dirs.get(i) + "    ";
            }
        }
        return data;
    }
    static String cp(){
        int argSize = parser.getArguments().size();
        String data = "";
        for(int i = 0 ; i < argSize - 1 ; i++){
            data = data + terminal.cp(parser.getArguments().get(i) , parser.getArguments().get(argSize - 1)) + "\n";
        }
        return data;
    }
    static String cat(){
        int argSize = parser.getArguments().size();
        String data = "";
        for(int i = 0 ; i < argSize; i++){
            data = data + terminal.cat(parser.getArguments().get(i)) + "\n";
        }
        return data;
    }
    static String more(){
        int argSize = parser.getArguments().size();
        String data = "";
        for(int i = 0 ; i < argSize; i++){
            data = data + terminal.more(parser.getArguments().get(i)) + "\n";
        }
        return data;
    }
    static String mkdir(){
    	String data = "";
    	for(int i = 0 ; i < parser.getArguments().size(); i++) {
    		data = data + terminal.mkdir(parser.getArguments().get(i)) + "\n";
    	}
    	return data;
    }
    static String rmdir(){
    	String data = "";
    	for(int i = 0 ; i < parser.getArguments().size(); i++) {
    		data = data + terminal.rmdir(parser.getArguments().get(i)) + "\n";
    	}
    	return data;
    }
    static String mv(){
        int argSize = parser.getArguments().size();
        String data = "";
        for(int i = 0 ; i < argSize - 1 ; i++){
            data = data + terminal.mv(parser.getArguments().get(i) , parser.getArguments().get(argSize - 1)) + "\n";
        }
        return data;
    }
    static String rm(){
    	String data = "";
		for(int i = 0 ; i < parser.getArguments().size(); i++) {
			data = data + terminal.rm(parser.getArguments().get(i)) + "\n";
		}
		return data;
    }
    static String args(){
        return terminal.args(parser.getArguments().get(0));
    }
    static String date(){
        return terminal.date();
    }
    static String help(){
        return terminal.help();
    }
    static String pwd(){
        return terminal.pwd();
    }
    static String clear(){
        return terminal.clear();
    }
    static void initializeMap(){
        CommandsList.put("cd", new int[]{0, 1});
        CommandsList.put("ls",new int[]{0, 1});
        CommandsList.put("cp",new int[]{2, 50});
        CommandsList.put("cat",new int[]{1, 50});
        CommandsList.put("more",new int[]{1, 50});
        CommandsList.put("mkdir",new int[]{1, 50});
        CommandsList.put("rmdir",new int[]{1, 50});
        CommandsList.put("mv",new int[]{2, 50});
        CommandsList.put("rm",new int[]{1, 50});
        CommandsList.put("args",new int[]{1, 1});
        CommandsList.put("date",new int[]{0, 0});
        CommandsList.put("help",new int[]{0, 0});
        CommandsList.put("pwd",new int[]{0, 0});
        CommandsList.put("clear",new int[]{0, 0});
        CommandsList.put("exit",new int[]{0, 0});
    }
    public static void createFile(String fileName){
        try {
            File myObj = new File(currentDirectory + "/" + fileName);
            myObj.createNewFile();
        }catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }
    public static void writeToFile(String data , String fileName , boolean append){
        try {
            FileWriter myWriter = new FileWriter(currentDirectory + "/" + fileName ,append);
            myWriter.write(data);
            myWriter.close();
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }
}
