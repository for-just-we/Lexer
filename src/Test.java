import java.io.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.List;
import java.util.ArrayList;

public class Test {
    public static void main(String[] args){
        String regEx = "[a-zA-Z_]\\w*|([0-9]+)(\\.[0-9]+)?|=+|<=*|>=*|\\++|\\-+|\\*+|\\/+|;|:|,|\\(|\\)|\\[|\\]|\\{|\\}";//匹配关键字，标识符，常量

        String path = "src/source.txt";
        StringBuffer sb;
        Pattern p = Pattern.compile(regEx);
        try{
            InputStream is = new FileInputStream(path);
            String line;
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            sb = new StringBuffer();

            line = br.readLine();
            while(line != null){
                sb.append(line + '\n');
                //System.out.println(line);
                line = br.readLine();
            }
            br.close();
            is.close();
            String fileStr = sb.toString();
            System.out.println("文件内容如下：\n" + fileStr);

            Matcher matcher = p.matcher(fileStr);
            List<String> stringList = new ArrayList<>();
            while(matcher.find())
                stringList.add(matcher.group());

            for(String str: stringList)
                System.out.print(str + ' ');
            System.out.println();
        }catch (IOException e){
            e.printStackTrace();
        }
    }
}
