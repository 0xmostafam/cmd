import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
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
        if(!handlePath(destinationPath).equals("")){
            return new ArrayList<String>(Arrays.asList("invalid path"));
        }
        String[] pathnames;
        File f = new File(destinationPath);
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
                count++;
            }
            fileReader.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
    public void mkdir(String dirName){}
    public void rmdir(String dirName){}
    public void mv(String sourcePath, String destinationPath){}
    public void rm(String sourcePath){}
    public void args(){}
    public void date(){}
    public void help(){}
    public void pwd(){}
    public void clear(){}

    private String handlePath(String destinationPath){
        String customPath = Main.currentDirectory;
        String[] files = destinationPath.split("/");
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
                    ArrayList<String> dirFiles = ls(customPath);
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
