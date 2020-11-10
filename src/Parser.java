public class Parser {
    private String[] args = new String[2];
    private String cmd;

    public Parser(){
        cmd = "";
    }
    public boolean parse(String input){
        String[] tokens = input.split(" ");
        cmd = tokens[0];

        for(int i = 1 ; i < tokens.length ; i++){
            args[i - 1] = tokens[i];
        }

        if(!Main.Commands.containsKey(cmd)){
            return false;
        }

        if(Main.Commands.get(cmd) != (tokens.length - 1)){
            System.out.println(cmd + " have " + Main.Commands.get(cmd) + " arguments.");
            return false;
        }

        return true;
    }

    public String getCmd(){
        return cmd;
    }

    public String[] getArguments(){
        return args;
    }
}
