import java.io.*;
 
public class PythonCaller {

	public String Execute(String create) throws IOException {
    	PrintStream out = new PrintStream(new FileOutputStream("E:\\Softwares\\Python\\url.db"));
    	out.println(create);
    	out.close();
    	
    	String pythonScriptPath = "E:\\Softwares\\Python\\Youtube.py";
		String[] cmd = new String[2];
		cmd[0] = "python"; // check version of installed python: python -V
		cmd[1] = pythonScriptPath;

		Runtime rt = Runtime.getRuntime();
		Process pr = rt.exec(cmd);
		BufferedReader bfr = new BufferedReader(new InputStreamReader(pr.getInputStream()));
		String line = "";
		StringBuilder l = new StringBuilder();
		while((line = bfr.readLine()) != null)
			l.append(new String(line));
		return l.toString();
	}
}