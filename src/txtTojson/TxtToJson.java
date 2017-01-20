package txtTojson;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TxtToJson {
	//keys保存json对象的key，value保存json对象的value，因为源文件的key和value出现在同一行，所以直接按顺序读写；
	private static ArrayList<String> keys = new ArrayList<String>();
	private static ArrayList<String> values = new ArrayList<String>();
	//result存储最后生成字符串；
	private static StringBuffer result= new StringBuffer("");
	//使用含参构造方法直接运行；
	public TxtToJson(String txt_adress,String keypattern,String valuepattern) {
		try {
			result.append("{\n[");
			//在try块中创建的filewriter对象在finally块中无法识别；
			FileReader fr = new FileReader(txt_adress);
			BufferedReader br = new BufferedReader(fr);
			String line=null ;
			//按行读取进行匹配和处理；
			while ((line=br.readLine())!=null){
				//checker方法对每行进行模式匹配，分类存储到keys和values中；
				TxtToJson.checker(line,keypattern,valuepattern);
				//toJson方法生成result字符串的中间内容部分；
				TxtToJson.toJson();
			}
			result.append("]}");
			//在项目目录内生成文件并写入result；
			FileWriter fw = new FileWriter("target.json");
			fw.write(result.toString());
			br.close();
			fw.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		
	}

public static void checker(String line,String keypattern,String valuepattern){
	Pattern key_p = Pattern.compile(keypattern);
	Pattern value_p = Pattern.compile(valuepattern);
	Matcher key_m = key_p.matcher(line);
	Matcher value_m = value_p.matcher(line);
	if (key_m.find()&&value_m.find()){
		keys.add(key_m.group());
		values.add(value_m.group());
	}
}

public static void toJson() throws IOException{
	for (int i=0;i<keys.size();i++){
		result.append("{\""+keys.remove(0)+"\",\""+values.remove(0)+"\"},\n");
	}
}
}
