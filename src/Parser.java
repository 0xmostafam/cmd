import java.util.ArrayList;

public class Parser {
    private ArrayList<String> args = new ArrayList<String>();
    private String cmd;

    public boolean parse(String input){
        String[] tokens = input.split(" ");
        cmd = tokens[0];

        args.clear();
        for(int i = 1 ; i < tokens.length ; i++){
            args.add(tokens[i]);
        }

        if(!Main.CommandsList.containsKey(cmd)){
            return false;
        }

        if(Main.CommandsList.get(cmd) != (tokens.length - 1)){
            System.out.println(cmd + " have " + Main.CommandsList.get(cmd) + " arguments.");
            return false;
        }

        return true;
    }

    public String getCmd(){
        return cmd;
    }

    public ArrayList<String> getArguments(){
        return args;
    }
}
