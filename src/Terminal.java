import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Scanner;

public class Terminal {
	
	public String cd(){
		Main.currentDirectory = Main.homeDirectory;
        return "";
    }
	
	public String cd(String destinationPath){
		File file = new File(handlePath(destinationPath));
        if(!handlePath(destinationPath).equals("") && file.isDirectory()){
            Main.currentDirectory = handlePath(destinationPath);
            return "";
        } else {
            return "invalid path";
        }
	}
	
	public ArrayList<String> ls()  {
		var dirName = Paths.get(Main.currentDirectory);
        ArrayList<String> dirFiles = new ArrayList<String>();
        try (var paths = Files.newDirectoryStream(dirName)) {
            for (Path path : paths) {
            	dirFiles.add(path.getFileName().toString());
            }
        }catch (IOException e) {
  	      e.printStackTrace();
  	    } 
        return dirFiles;
    }
    
    public ArrayList<String> ls(String destinationPath){
        if(handlePath(destinationPath).equals("")){
            return new ArrayList<String>(Arrays.asList("invalid path"));
        }
        var dirName = Paths.get(handlePath(destinationPath));
        ArrayList<String> dirFiles = new ArrayList<String>();
        try (var paths = Files.newDirectoryStream(dirName)) {
            for (Path path : paths) {
                dirFiles.add(path.getFileName().toString());
            }
        }catch (IOException e) {
            e.printStackTrace();
        }
        return dirFiles;
    }
    
    public String cp(String sourcePath, String destinationPath ){
        File srcFile = new File(handlePath(sourcePath));
        if(destinationPath.contains("."))
        	createFile(Main.currentDirectory + "/" + destinationPath);
        File desFile = new File(handlePath(destinationPath));
        if(handlePath(sourcePath).equals("") || handlePath(destinationPath).equals("") || !srcFile.isFile()){
            return sourcePath + " is not a file or " + destinationPath  + " is not a directory.";
        }
        try {
        	String fileName = "";
        	File sourceFile = new File(handlePath(sourcePath));
            File destinationFile = null;
        	if (desFile.isDirectory()) {
        		String[] fileNames = sourcePath.split("/");
        		fileName = fileNames[fileNames.length - 1];
                createFile(handlePath(destinationPath) + "/" + fileName);
                destinationFile = new File(handlePath(destinationPath) + "/" + fileName);
        	} else {
        		destinationFile = new File(handlePath(destinationPath));
        	}
            InputStream inputStream = new FileInputStream(sourceFile);
            OutputStream outputStream = new FileOutputStream(destinationFile);
            byte[] buffer = new byte[1024];
            int length = 0;
            while ((length = inputStream.read(buffer)) > 0) {
                outputStream.write(buffer, 0, length);
            }
            return "";
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }
    
    public String cat(String destinationPath){
        File file = new File(handlePath(destinationPath));
        if(handlePath(destinationPath).equals("") && !file.isFile()){
            return destinationPath + " is not a file.";
        }
        Scanner fileReader;
        try {
            fileReader = new Scanner(file);
            String data = "";
            while (fileReader.hasNextLine()) {
                data = data + fileReader.nextLine()+ "\n";
            }
            fileReader.close();
            return data;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return "";
    }
    
    public String more(String destinationPath){
        File file = new File(handlePath(destinationPath));
        if(handlePath(destinationPath).equals("") && !file.isFile()){
            return destinationPath + " is not a file.";
        }
        Scanner fileReader , scanner;
        try {
            fileReader = new Scanner(file);
            scanner = new Scanner(System.in);
            String answer;
            String data = "";
            int count = 0;
            while (fileReader.hasNextLine()) {
                if(count == 10){
                    do {
                        System.out.print("Enter S to view more or Q to exit : ");
                        answer = scanner.next();
                    }while (!answer.equals("S") && !answer.equals("Q") && !answer.equals("s") && !answer.equals("q"));
                    if(answer.equals("Q") || answer.equals("q")){
                        break;
                    } else {
                        count = 0;
                    }
                }
                String tempStr = fileReader.nextLine();
                System.out.println(tempStr);
                data = data + tempStr+ "\n";
                count++;
            }
            fileReader.close();
            return data;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return "";
    }
   
    public String mkdir(String dirName){
        File file = new File(Main.currentDirectory + "/" + dirName);
        file.mkdir();
        return "";
    }
    
    public String rmdir(String dirName){
        File file = new File(Main.currentDirectory + "/" + dirName);
        if(handlePath(Main.currentDirectory + "/" + dirName).equals("") || !file.isDirectory()){
            return dirName + " path is invalid , or isn't a directory.";
        }
        if(!ls(Main.currentDirectory + "/" + dirName).isEmpty()){
            return dirName + ": cannot remove an non-empty directory.";
        }
        file.delete();
        return "";
    }
    
    public String mv(String sourcePath, String destinationPath){
    	String data = "";
        data = cp(sourcePath ,destinationPath);
        rm(sourcePath);
        return data;
    }
    
    public String rm(String destinationPath){
    	File file = new File(handlePath(destinationPath));
        if(handlePath(destinationPath).equals("") || !file.isFile()){
            return destinationPath + " path is invalid , or isn't a file.";
        }
        file.delete();
        return "";
    }
    
    public String args(String command){
        switch(command) {
            case "cd":
            case "ls":
                return "no arg or arg1: DestinationPath";
            case "cp":
            case "cat":
            case "more":
            case "mv":
                return "arg1: SourcePath[], arg2:DestinationPath";
            case "mkdir":
            case "rmdir":
                return "arg1: dirName";
            case "rm":
                return "arg1: DestinationPath";
            case "args":
                return "arg1: Command";
            case "date":
            case "help":
            case "pwd":
            case "clear":
                return "no args";
            default:
                return "invalid command";
        }
    }
    
    public String date(){
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        Date date = new Date();
        return formatter.format(date);
    }
    
    public String help(){
        return "cd : Change the shell working directory.\n" +
        "ls : List information about the FILEs (the current directory by default).\n" +
                "cp : Copy SOURCE to DEST, or multiple SOURCE(s) to DIRECTORY.\n" +
                "cat : Concatenate FILE(s) to standard output.\n"+
                "more : A file perusal filter for CRT viewing.\n"+
                "mkdir : Create the DIRECTORY(ies), if they do not already exist.\n"+
                "rmdir : Remove the DIRECTORY(ies), if they are empty.\n"+
                "mv : Rename SOURCE to DEST, or move SOURCE(s) to DIRECTORY.\n"+
                "rm : Remove (unlink) the FILE(s).\n"+
                "args : Lists argument(s) in a command.\n"+
                "date : Display the current time in the certain format.\n"+
                "help : List all commands functionalities\n"+
                "pwd : Print the name of the current working directory.\n"+
                "clear : Clears the shell.\n";

    }
    
    public String pwd(){
        return Main.currentDirectory;
    }
    
    public String clear(){
        for(int i = 0 ; i < 150 ; i++){
            System.out.println("\n");
        }
        System.out.println("\033[H\033[2J");
        return "";
    }
    
    private String handlePath(String destinationPath){
        String customPath = Main.currentDirectory;
        String[] files = destinationPath.split("/");
        if(files.length == 0)
            customPath = "/";
        boolean validPath = true;
        for(int i = 0 ; i < files.length ; i++) {
            if (files[i].equals("..")) {
                customPath = customPath.substring(0, customPath.lastIndexOf("/")) ;
                if(customPath.isEmpty()) {
                    customPath = "/";
                }
            } else if(files[i].equals(".")){
                continue;
            } else if(files[i].equals("~")){
                customPath = Main.homeDirectory;
            } else {
                if(files[i].equals("")){
                    customPath = "/";
                } else {
                    String[] pathnames;
                    File f = new File(customPath);
                    pathnames = f.list();
                    ArrayList<String> dirFiles = new ArrayList<String>(Arrays.asList(pathnames));
                    if (dirFiles.contains(files[i]) && !customPath.equals("/")) {
                        customPath = customPath + "/" + files[i];
                    } else if(dirFiles.contains(files[i])){
                        customPath = customPath + files[i];
                    } else {
                        validPath = false;
                        break;
                    }
                }
            }
        }
        if(validPath)
            return customPath;
        else
            return "";
    }
    
    private void createFile(String path){
        try {
            File myObj = new File(path);
            myObj.createNewFile();
        }catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }
}
