import java.io.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Scanner;

public class Terminal {
    public void cd(){
        Main.currentDirectory = Main.homeDirectory;
    }
    public void cd(String destinationPath){
        File file = new File(handlePath(destinationPath));
        if(!handlePath(destinationPath).equals("") && file.isDirectory()){
            Main.currentDirectory = handlePath(destinationPath);
        } else {
            System.out.println("invalid path");
        }
    }
    public ArrayList<String> ls(){
        String[] pathnames;
        File f = new File(Main.currentDirectory);
        pathnames = f.list();
        ArrayList<String> dirFiles = new ArrayList<String>(Arrays.asList(pathnames));
        return dirFiles;
    }
    public ArrayList<String> ls(String destinationPath){
        if(handlePath(destinationPath).equals("")){
            return new ArrayList<String>(Arrays.asList("invalid path"));
        }
        String[] pathnames;
        File f = new File(handlePath(destinationPath));
        pathnames = f.list();
        ArrayList<String> dirFiles = new ArrayList<String>(Arrays.asList(pathnames));
        return dirFiles;
    }
    public void cp(String sourcePath, String destinationPath ){
        File srcFile = new File(handlePath(sourcePath));
        File desFile = new File(handlePath(destinationPath));
        if(handlePath(sourcePath).equals("") || handlePath(destinationPath).equals("") || !srcFile.isFile() || !desFile.isDirectory()){
            System.out.println(sourcePath + " is not a file or " + destinationPath  + "is not a directory.");
            return;
        }
        try {
            String[] fileNames = sourcePath.split("/");
            String fileName = fileNames[fileNames.length - 1];
            createFile(handlePath(destinationPath) + "/" + fileName);
            File sourceFile = new File(handlePath(sourcePath));
            File destinationFile = new File(handlePath(destinationPath) + "/" + fileName);
            InputStream inputStream = new FileInputStream(sourceFile);
            OutputStream outputStream = new FileOutputStream(destinationFile);
            byte[] buffer = new byte[1024];
            int length = 0;
            while ((length = inputStream.read(buffer)) > 0) {
                outputStream.write(buffer, 0, length);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void cat(String destinationPath){
        File file = new File(handlePath(destinationPath));
        if(handlePath(destinationPath).equals("") && !file.isFile()){
            System.out.println(destinationPath + " is not a file.");
            return;
        }
        Scanner fileReader;
        try {
            fileReader = new Scanner(file);
            String data = "";
            while (fileReader.hasNextLine()) {
                data = data + fileReader.nextLine()+ "\n";
            }
            System.out.println(data);
            fileReader.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
    public void more(String destinationPath){
        File file = new File(handlePath(destinationPath));
        if(handlePath(destinationPath).equals("") && !file.isFile()){
            System.out.println(destinationPath + " is not a file.");
            return;
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
                System.out.println(fileReader.nextLine());
                data = data + fileReader.nextLine() + "\n";
                count++;
            }
            fileReader.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
    public void mkdir(String dirName){
        File file = new File(Main.currentDirectory + "/" + dirName);
        file.mkdir();
    }
    public void rmdir(String dirName){
        File file = new File(Main.currentDirectory + "/" + dirName);
        if(handlePath(Main.currentDirectory + "/" + dirName).equals("") || !file.isDirectory()){
            System.out.println("path is invalid , or isn't a directory.");
            return;
        }
        if(!ls(Main.currentDirectory + "/" + dirName).isEmpty()){
            System.out.println("cannot remove an non-empty directory.");
        }
        file.delete();
    }
    public void mv(String sourcePath, String destinationPath){
        cp(sourcePath ,destinationPath);
        rm(sourcePath);
    }
    public void rm(String destinationPath){
        File file = new File(handlePath(destinationPath));
        if(handlePath(destinationPath).equals("") || !file.isFile()){
            System.out.println("path is invalid , or isn't a file.");
            return;
        }
        if(!ls(destinationPath).isEmpty()){
            System.out.println("cannot remove an non-empty directory.");
        }
        file.delete();
    }
    public void args(String command){
        switch(command) {
            case "cd":
            case "ls":
                System.out.println("no arg or arg1: DestinationPath");
                break;
            case "cp":
            case "cat":
            case "more":
            case "mv":
                System.out.println("arg1: SourcePath[], arg2:DestinationPath");
                break;
            case "mkdir":
            case "rmdir":
                System.out.println("arg1: dirName");
            case "rm":
                System.out.println("arg1: DestinationPath");
                break;
            case "args":
                System.out.println("arg1: Command");
                break;
            case "date":
            case "help":
            case "pwd":
            case "clear":
                System.out.println("no args");
                break;
            default:
                System.out.println("invalid command");
        }
    }
    public void date(){
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        Date date = new Date();
        System.out.println(formatter.format(date));
    }
    public void help(){
        System.out.println("cd : Change the shell working directory.");
        System.out.println("ls : List information about the FILEs (the current directory by default).");
        System.out.println("cp : Copy SOURCE to DEST, or multiple SOURCE(s) to DIRECTORY.");
        System.out.println("cat : Concatenate FILE(s) to standard output.");
        System.out.println("more : A file perusal filter for CRT viewing.");
        System.out.println("mkdir : Create the DIRECTORY(ies), if they do not already exist.");
        System.out.println("rmdir : Remove the DIRECTORY(ies), if they are empty.");
        System.out.println("mv : Rename SOURCE to DEST, or move SOURCE(s) to DIRECTORY.");
        System.out.println("rm : Remove (unlink) the FILE(s).");
        System.out.println("args : Lists argument(s) in a command.");
        System.out.println("date : Display the current time in the certain format.");
        System.out.println("help : List all commands functionalities");
        System.out.println("pwd : Print the name of the current working directory.");
        System.out.println("clear : Clears the shell.");
    }
    public void pwd(){
        System.out.println(Main.currentDirectory);
    }
    public void clear(){
        for(int i = 0 ; i < 150 ; i++){
            System.out.println("\n");
        }
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
            if (myObj.createNewFile()) {
                System.out.println("File created: " + myObj.getName());
            } else {
                System.out.println("File already exists.");
            }
        }catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }
}
