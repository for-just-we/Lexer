import java.io.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.ArrayList;
import java.lang.Character;
import java.util.Map;
import java.util.HashMap;

/*
目前不考虑unsigned,signed
 */
public class Lexer {
    //定义种别码
    // 26为标识符种别码
    private static final int ID = 26;

    // 30为常量种别码
    private static final int NUM = 30;

    // 31-40为运算符种别码
    private static final int AS = 31; // =
    private static final int EQ = 32; // ==
    private static final int GT = 33; // >
    private static final int LT = 34; // <
    private static final int GE = 35; // >=
    private static final int LE = 36; // <=
    private static final int ADD = 37; // +
    private static final int SUB = 38; // -
    private static final int MUL = 39; // *
    private static final int DIV = 40; // /

    // 41-49为界限符种别码
    private static final int LP = 41; // (
    private static final int RP = 42; // )
    private static final int LBT = 43; // [
    private static final int RBT = 44; // ]
    private static final int LBS = 45; // {
    private static final int RBS = 46; // }
    private static final int COM = 47; // ,
    private static final int COL = 48; // :
    private static final int SEM = 49; // ;

    // -1为无法识别的字符标志码
    private static final int ERROR = -1;
    private static int ERRORNUM; // 记录词法分析错误的个数

    private static final String blank = " ";
    private static final String newLine = "\n";

    private static Map<String,Integer> keywords;
    private static StringBuffer sb;
    private static String path = "src/source.txt";

    static{
        String[] keys = {"void","char","int","short","long","float","double","static","const","for","do","while","continue","break","return","switch","case",
                        "default","if","else","struct","sizeof","union","enum","typedef"};
        String[] tokens = {"=","==",">","<",">=","<=","+","-","*","/","(",")","[","]","{","}",",",":",";"};
        keywords = new HashMap<>();
        ERRORNUM = 0;
        int count = 1;
        for(String key: keys)
            keywords.put(key, count++);
        keywords.put("id",26);
        keywords.put("num",30);
        count = 41;
        for(String token: tokens)
            keywords.put(token,count++);
    }

    public static void getFile() throws IOException { //读取文件
        InputStream is = new FileInputStream(path);
        String line;
        BufferedReader br = new BufferedReader(new InputStreamReader(is));
        sb = new StringBuffer();

        line = br.readLine();
        while(line != null){
            sb.append(line + '\n');
            System.out.println(line);
            line = br.readLine();
        }
        br.close();
        is.close();
        String fileStr = sb.toString();

        System.out.println("文件内容如下：\n" + fileStr);
    }

    private static ArrayList<String> getWords(){
        ArrayList<String> stringList = new ArrayList<>();
        String regEx = "[a-zA-Z_]\\w*|([0-9]+)(\\.[0-9]+)?|=+|<=*|>=*|\\++|\\-+|\\*+|\\/+|;|:|,|\\(|\\)|\\[|\\]|\\{|\\}";//匹配关键字，标识符，常量
        Pattern pattern = Pattern.compile(regEx);
        Matcher matcher = pattern.matcher(sb.toString());
        while(matcher.find())
            stringList.add(matcher.group());
        return stringList;
    }

    private static ArrayList<Pair> analyse(ArrayList<String> arrayList) {
        ArrayList<Pair> result = new ArrayList<>();
        for(String str: arrayList){ //逐个判断符号
            if(keywords.containsKey(str))//关键字，符号
                result.add(new Pair(keywords.get(str),str));
            else{ //标识符，常量
                if(Character.isLetter(str.charAt(0)) || str.charAt(0) == '_')//标识符
                    result.add(new Pair(26, str));
                else if(Character.isDigit(str.charAt(0)))//数字
                    result.add(new Pair(30, str));
            }
        }
        return result;
    }

    private static boolean isNum(String str){
        Pattern pattern = Pattern.compile("[0-9]*");
        Matcher Num = pattern.matcher(str);
        if( !Num.matches() ){
            return false;
        }  return true;
    }

    private static class Pair{
        Integer key;
        String value;
        public Pair(Integer key, String value){
            this.key = key;
            this.value = value;
        }
    }

    public static void getResult(){
        ArrayList<String> stringList = getWords();
        ArrayList<Pair> result = analyse(stringList);

        System.out.println("词法分析结果如下：\n<单词种别码，单词>          //所属类别");
        StringBuffer outputBuffer = new StringBuffer("词法分析结果如下：\n<单词种别码，单词>          //所属类别\n");

        outputBuffer.append("词法分析结束！共" + ERRORNUM + "个无法识别的符号\n");
        System.out.println("词法分析结束！共" + ERRORNUM + "个无法识别的符号");

        for (Pair pair : result) {
            outputBuffer.append("<   " + pair.key + "  ,  " + pair.value + "  >          ");
            System.out.print("<   " + pair.key + "  ,  " + pair.value + "  >          ");
            if (pair.key > 0 && pair.key < 20) {
                outputBuffer.append("//关键字\n");
                System.out.println("//关键字");
            } else if (pair.key == 20) {
                outputBuffer.append("//标识符\n");
                System.out.println("//标识符");
            } else if (pair.key == 30) {
                outputBuffer.append("//常量\n");
                System.out.println("//常量");
            } else if (pair.key > 30 && pair.key <= 40) {
                outputBuffer.append("//运算符\n");
                System.out.println("//运算符");
            } else if (pair.key > 40 && pair.key < 50) {
                outputBuffer.append("//界限符\n");
                System.out.println("//界限符");
            } else if (pair.key == -1) {
                outputBuffer.append("//无法识别的符号\n");
                System.out.println("//无法识别的符号");
            }
        }

        String outputPath = "src/result.txt";
        File outputFile = new File(outputPath);
        if (outputFile.exists()) {
            outputFile.delete();
        }
        PrintWriter writer = null;
        try {
            outputFile.createNewFile();
            writer = new PrintWriter(new FileOutputStream(outputFile));
            writer.write(outputBuffer.toString());
        } catch (IOException e1) {
            e1.printStackTrace();
        } finally {
            if (writer != null) {
                writer.close();
            }
        }
    }
}
