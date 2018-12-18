package cpmadeeasy.utils;

import com.cpmadeeasy.dto.TestCase;
import com.intellij.psi.PsiDirectory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class FileUtils
{
	private static String testCaseTag = "test";
	private static String inputTag = "input";
	private static String outputTag = "output";

	public static void addTests(PsiDirectory directory, List<TestCase>[] lists)
	{
		String path = directory.toString();

		path = path.replace("PsiDirectory:", "");
		path += "/";

		char problem = 'A';

		for (List<TestCase> list : lists)
		{
			writeTestCases(path, problem, list);
			problem++;
		}
	}

	private static String getSrcPath(String path)
	{
		String[] tok = path.split("/");
		int ind = -1;

		for (int i = 0; i < tok.length; i++)
		{
			if (tok[i].equals("src"))
			{
				ind = i;

				break;
			}
		}

		StringBuilder srcPath = new StringBuilder("");

		for (int i = 0; i <= ind; i++)
			srcPath.append(tok[i]).append("/");

		return srcPath.toString();
	}

	private static void writeTestCases(String path, char problem, List<TestCase> list)
	{
		String xmlFile = path + problem + ".xml";
		String javaFile = path + "Task" + problem + ".java";
		Document dom;
		Element e, input, output;
		// instance of a DocumentBuilderFactory
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		DocumentBuilder db;

		try
		{
			String srcPath = getSrcPath(path);
			File file = new File(javaFile);
			File template = new File(srcPath + "template.txt");
			PrintWriter writer = new PrintWriter(file);

			file.createNewFile();

			if (!template.exists())
				createTemplateFile(template);

			Scanner scanner = new Scanner(template);
			StringBuilder templateText = new StringBuilder("");

			while (scanner.hasNext())
				templateText.append(scanner.nextLine()).append("\n");

			String temp = templateText.toString();

			temp = temp.replaceAll("Task", "Task" + problem);
			writer.println(temp);
			scanner.close();
			writer.flush();
			writer.close();
		}
		catch (IOException e1)
		{
			e1.printStackTrace();
		}

		try
		{
			db = dbf.newDocumentBuilder();
			dom = db.newDocument();

			// create the root element
			String rootTag = "testCases";
			Element rootElement = dom.createElement(rootTag);

			for (TestCase testCase : list)
			{
				// create data elements and place them under root
				e = dom.createElement(testCaseTag);
				input = dom.createElement(inputTag);
				output = dom.createElement(outputTag);
				input.appendChild(dom.createTextNode(testCase.getInput()));
				output.appendChild(dom.createTextNode(testCase.getOutput()));
				e.appendChild(input);
				e.appendChild(output);
				rootElement.appendChild(e);
			}

			dom.appendChild(rootElement);

			try
			{
				Transformer tr = TransformerFactory.newInstance().newTransformer();

				tr.setOutputProperty(OutputKeys.INDENT, "yes");
				tr.setOutputProperty(OutputKeys.METHOD, "xml");
				tr.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
				tr.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");
				tr.transform(new DOMSource(dom), new StreamResult(new FileOutputStream(xmlFile)));

			}
			catch (TransformerException | IOException te)
			{
				System.out.println(te.getMessage());
			}
		}
		catch (ParserConfigurationException e1)
		{
			e1.printStackTrace();
		}
	}

	private static void createTemplateFile(File template) throws IOException
	{
		PrintWriter writer = new PrintWriter(template);
		String defaultText =
				"import java.io.IOException;\n" + "import java.io.InputStream;\n" + "import java.io.OutputStream;\n"
						+ "import java.io.PrintWriter;\n" + "import java.util.Arrays;\n"
						+ "import java.util.InputMismatchException;\n" + "\n" + "public class Task\n{\n"
						+ "\tpublic static void main(String[] args)\n" + "\t{\n"
						+ "\t\tnew Task(System.in, System.out);\n" + "\t}\n" + "\n" + "\tstatic class Solver\n"
						+ "\t{\n" + "//\t\tBufferedReader in;\n" + "\t\tint n;\n" + "\t\tInputReader in;\n"
						+ "\t\tPrintWriter out;\n" + "\n" + "\t\tvoid solve() throws IOException\n" + "\t\t{\n"
						+ "\t\t}\n" + "\n" + "//\t\tuncomment below line to change to BufferedReader\n"
						+ "//\t\tpublic Solver(BufferedReader in, PrintWriter out)\n"
						+ "\t\tpublic Solver(InputReader in, PrintWriter out)\n" + "\t\t{\n" + "\t\t\tthis.in = in;\n"
						+ "\t\t\tthis.out = out;\n" + "\t\t}\n" + "\n" + "\t}\n" + "\n" + "\tstatic class "
						+ "InputReader\n" + "\t{\n" + "\t\tprivate InputStream stream;\n"
						+ "\t\tprivate byte[] buf = new byte[1024];\n" + "\t\tprivate int curChar;\n"
						+ "\t\tprivate int numChars;\n" + "\n" + "\t\tpublic int read()\n" + "\t\t{\n"
						+ "\t\t\tif (numChars == -1)\n" + "\t\t\t\tthrow new InputMismatchException();\n" + "\n"
						+ "\t\t\tif (curChar >= numChars)\n" + "\t\t\t{\n" + "\t\t\t\tcurChar = 0;\n" + "\t\t\t\ttry\n"
						+ "\t\t\t\t{\n" + "\t\t\t\t\tnumChars = stream.read(buf);\n" + "\t\t\t\t}\n"
						+ "\t\t\t\tcatch (IOException " + "e)\n" + "\t\t\t\t{\n"
						+ "\t\t\t\t\tthrow new InputMismatchException();\n" + "\t\t\t\t}\n"
						+ "\t\t\t\tif (numChars <= 0)\n" + "\t\t\t\t\treturn -1;\n" + "\t\t\t}\n" + "\n"
						+ "\t\t\treturn buf[curChar++];\n" + "\t\t}\n" + "\n" + "\t\tpublic int nextInt()\n" +
						"\t\t{\n"
						+ "\t\t\tint c = read();\n" + "\n" + "\t\t\twhile (isSpaceChar(c))\n" + "\t\t\t\tc = read();\n"
						+ "\n" + "\t\t\tint sgn = 1;\n" + "\n" + "\t\t\tif (c == '-')\n" + "\t\t\t{\n"
						+ "\t\t\t\tsgn = -1;\n" + "\t\t\t\tc = read();\n" + "\t\t\t}\n" + "\n" + "\t\t\tint res = 0;\n"
						+ "\n" + "\t\t\tdo\n" + "\t\t\t{\n" + "\t\t\t\tif (c < '0' || c > '9')\n"
						+ "\t\t\t\t\tthrow new InputMismatchException();\n" + "\n" + "\t\t\t\tres *= 10;\n"
						+ "\t\t\t\tres += c & 15;\n" + "\n" + "\t\t\t\tc = read();\n"
						+ "\t\t\t} while (!isSpaceChar(c));\n" + "\n" + "\t\t\treturn res * sgn;\n" + "\t\t}\n" + "\n"
						+ "\t\tpublic int[] nextIntArray(int arraySize)\n" + "\t\t{\n"
						+ "\t\t\tint array[] = new int[arraySize];\n" + "\n"
						+ "\t\t\tfor (int i = 0; i < arraySize; i++)\n" + "\t\t\t\tarray[i] = nextInt();\n" + "\n"
						+ "\t\t\treturn array;\n" + "\t\t}\n" + "\n" + "\t\tpublic long nextLong()\n" + "\t\t{\n"
						+ "\t\t\tint c = read();\n" + "\n" + "\t\t\twhile (isSpaceChar(c))\n" + "\t\t\t\tc = read();\n"
						+ "\n" + "\t\t\tint sign = 1;\n" + "\n" + "\t\t\tif (c == '-')\n" + "\t\t\t{\n"
						+ "\t\t\t\tsign = -1;\n" + "\n" + "\t\t\t\tc = read();\n" + "\t\t\t}\n" + "\n"
						+ "\t\t\tlong result = 0;\n" + "\n" + "\t\t\tdo\n" + "\t\t\t{\n"
						+ "\t\t\t\tif (c < '0' || c > '9')\n" + "\t\t\t\t\tthrow new InputMismatchException();\n" +
						"\n"
						+ "\t\t\t\tresult *= 10;\n" + "\t\t\t\tresult += c & 15;\n" + "\n" + "\t\t\t\tc = read();\n"
						+ "\t\t\t} while (!isSpaceChar(c));\n" + "\n" + "\t\t\treturn result * sign;\n" + "\t\t}\n"
						+ "\n" + "\t\tpublic long[] nextLongArray(int arraySize)\n" + "\t\t{\n"
						+ "\t\t\tlong array[] = new long[arraySize];\n" + "\n"
						+ "\t\t\tfor (int i = 0; i < arraySize; i++)\n" + "\t\t\t\tarray[i] = nextLong();\n" + "\n"
						+ "\t\t\treturn array;\n" + "\t\t}\n" + "\n" + "\t\tpublic float nextFloat()\n" + "\t\t{\n"
						+ "\t\t\tfloat result, div;\n" + "\t\t\tbyte c;\n" + "\n" + "\t\t\tresult = 0;\n"
						+ "\t\t\tdiv = 1;\n" + "\t\t\tc = (byte) read();\n" + "\n" + "\t\t\twhile (c <= ' ')\n"
						+ "\t\t\t\tc = (byte) read();\n" + "\n" + "\t\t\tboolean isNegative = (c == '-');\n" + "\n"
						+ "\t\t\tif (isNegative)\n" + "\t\t\t\tc = (byte) read();\n" + "\n" + "\t\t\tdo\n" +
						"\t\t\t{\n"
						+ "\t\t\t\tresult = result * 10 + c - '0';\n"
						+ "\t\t\t} while ((c = (byte) read()) >= '0' && c <= '9');\n" + "\n" + "\t\t\tif (c == '.')\n"
						+ "\t\t\t\twhile ((c = (byte) read()) >= '0' && c <= '9')\n"
						+ "\t\t\t\t\tresult += (c - '0') / (div *= 10);\n" + "\n" + "\t\t\tif (isNegative)\n"
						+ "\t\t\t\treturn -result;\n" + "\n" + "\t\t\treturn result;\n" + "\t\t}\n" + "\n"
						+ "\t\tpublic double nextDouble()\n" + "\t\t{\n" + "\t\t\tdouble ret = 0, div = 1;\n"
						+ "\t\t\tbyte c = (byte) read();\n" + "\n" + "\t\t\twhile (c <= ' ')\n"
						+ "\t\t\t\tc = (byte) read();\n" + "\n" + "\t\t\tboolean neg = (c == '-');\n" + "\n"
						+ "\t\t\tif (neg)\n" + "\t\t\t\tc = (byte) read();\n" + "\n" + "\t\t\tdo\n" + "\t\t\t{\n"
						+ "\t\t\t\tret = ret * 10 + c - '0';\n"
						+ "\t\t\t} while ((c = (byte) read()) >= '0' && c <= '9');\n" + "\n" + "\t\t\tif (c == '.')\n"
						+ "\t\t\t\twhile ((c = (byte) read()) >= '0' && c <= '9')\n"
						+ "\t\t\t\t\tret += (c - '0') / (div *= 10);\n" + "\n" + "\t\t\tif (neg)\n"
						+ "\t\t\t\treturn -ret;\n" + "\n" + "\t\t\treturn ret;\n" + "\t\t}\n" + "\n"
						+ "\t\tpublic String next()\n" + "\t\t{\n" + "\t\t\tint c = read();\n" + "\n"
						+ "\t\t\twhile (isSpaceChar(c))\n" + "\t\t\t\tc = read();\n" + "\n"
						+ "\t\t\tStringBuilder res = new StringBuilder();\n" + "\n" + "\t\t\tdo\n" + "\t\t\t{\n"
						+ "\t\t\t\tres.appendCodePoint(c);\n" + "\n" + "\t\t\t\tc = read();\n"
						+ "\t\t\t} while (!isSpaceChar(c));\n" + "\n" + "\t\t\treturn res.toString();\n" + "\t\t}\n"
						+ "\n" + "\t\tpublic boolean isSpaceChar(int c)\n" + "\t\t{\n"
						+ "\t\t\treturn c == ' ' || c == '\\n' || c == '\\r' || c == '\\t' || c == -1;\n" + "\t\t}\n"
						+ "\n" + "\t\tpublic String nextLine()\n" + "\t\t{\n" + "\t\t\tint c = read();\n" + "\n"
						+ "\t\t\tStringBuilder result = new StringBuilder();\n" + "\n" + "\t\t\tdo\n" + "\t\t\t{\n"
						+ "\t\t\t\tresult.appendCodePoint(c);\n" + "\n" + "\t\t\t\tc = read();\n"
						+ "\t\t\t} while (!isNewLine(c));\n" + "\n" + "\t\t\treturn result.toString();\n" + "\t\t}\n"
						+ "\n" + "\t\tpublic boolean isNewLine(int c)\n" + "\t\t{\n" + "\t\t\treturn c == '\\n';\n"
						+ "\t\t}\n" + "\n" + "\t\tpublic void close()\n" + "\t\t{\n" + "\t\t\ttry\n" + "\t\t\t{\n"
						+ "\t\t\t\tstream.close();\n" + "\t\t\t}\n" + "\t\t\tcatch (IOException e)\n" + "\t\t\t{\n"
						+ "\t\t\t\te.printStackTrace();\n" + "\t\t\t}\n" + "\t\t}\n" + "\n"
						+ "\t\tpublic InputReader(InputStream stream)\n" + "\t\t{\n" + "\t\t\tthis.stream = stream;\n"
						+ "\t\t}\n" + "\n" + "\t}\n" + "\n" + "\tpublic Task(InputStream inputStream, "
						+ "OutputStream outputStream)\n" + "\t{\n"
						+ "//\t\tuncomment below line to change to BufferedReader\n"
						+ "//\t\tBufferedReader in = new BufferedReader(new InputStreamReader(inputStream));\n"
						+ "\t\tInputReader in = new InputReader(inputStream);\n"
						+ "\t\tPrintWriter out = new PrintWriter(outputStream);\n"
						+ "\t\tSolver solver = new Solver(in, out);\n" + "\n" + "\t\ttry\n" + "\t\t{\n"
						+ "\t\t\tsolver.solve();\n" + "\t\t\tin.close();\n" + "\t\t}\n" + "\t\tcatch (IOException e)\n"
						+ "\t\t{\n" + "\t\t\te.printStackTrace();\n" + "\t\t}\n" + "\n" + "\t\tout.flush();\n"
						+ "\t\tout.close();\n" + "\t}\n" + "\n" + "}";

		template.createNewFile();
		writer.write(defaultText);
		writer.flush();
		writer.close();
	}

	public static boolean createTesterFile(String parentCanonicalPath, char problem)
	{
		String input = parentCanonicalPath + "/Tester.java";
		File testerFile = new File(input);

		try
		{
			if (!testerFile.createNewFile() && !testerFile.exists())
				return false;

			PrintWriter out = new PrintWriter(testerFile);
			String testerTemplate =
					"import org.w3c.dom.Document;\n" + "import org.w3c.dom.Element;\n" + "import org.w3c.dom.Node;\n"
							+ "import org.w3c.dom.NodeList;\n" + "\n" + "import javax.xml.parsers.DocumentBuilder;\n"
							+ "import javax.xml.parsers.DocumentBuilderFactory;\n" + "import java.io.File;\n"
							+ "import java.io.IOException;\n" + "import java.io.InputStream;\n"
							+ "import java.io.OutputStream;\n" + "import java.util.ArrayList;\n"
							+ "import java.util.List;\n" + "\n" + "/**\n"
							+ " * This code is automatically generated. Do not change it.\n" + " */\n"
							+ "public class Tester\n" + "{\n" + "\tpublic static void main(String[] args)\n" + "\t{\n"
							+ "\t\tList<Boolean> results = new ArrayList<>();\n"
							+ "\t\tList<Long> timeTaken = new ArrayList<>();\n"
							+ "\t\tList<TestCase> testCases = readTestCases(\"" + parentCanonicalPath + problem
							+ ".xml\");\n" + "\n" + "\t\tfor (int i = 0; i < testCases.size(); i++)\n" + "\t\t{\n"
							+ "\t\t\tSystem.out.println(\"==========TestCase #\" + (i + 1) + \"==========\");\n"
							+ "\t\t\tTestCase testCase = testCases.get(i);\n"
							+ "\t\t\tStringInputStream inputStream = new StringInputStream(testCase.getInput());\n"
							+ "\t\t\tStringOutputStream outputStream = new StringOutputStream();\n"
							+ "\t\t\tlong start = System.currentTimeMillis();\n" + "\n" + "\t\t\tnew Task" + problem
							+ "(inputStream, outputStream);\n" + "\n"
							+ "\t\t\tlong time = System.currentTimeMillis() - start;\n" + "\n" + "\t\t\ttry\n"
							+ "\t\t\t{\n" + "\t\t\t\tinputStream.close();\n" + "\t\t\t\toutputStream.flush();\n"
							+ "\t\t\t\toutputStream.close();\n" + "\n"
							+ "\t\t\t\tboolean isMatching = testCase.getOutput().trim().equals(outputStream.getOutput"
							+ "().toString().trim());\n" + "\n" + "\t\t\t\tresults.add(isMatching);\n"
							+ "\t\t\t\ttimeTaken.add(time);\n"
							+ "\t\t\t\tSystem.out.print(\"Input :\\n\" + testCase.getInput() + \"Output :\\n\" + "
							+ "outputStream.getOutput()\n"
							+ "\t\t\t\t\t\t+ \"Expected Output :\\n\" + testCase.getOutput() + \"Result : \" + "
							+ "(isMatching ? \"SUCCESS\"\n"
							+ "\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t   : \"WRONG \"\n"
							+ "\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t+ \"ANSWER\") + \"\\nTime \"\n"
							+ "\t\t\t\t\t\t+ \"taken \" + \": \" + time + \"ms\\n\");\n" + "\t\t\t}\n"
							+ "\t\t\tcatch (IOException e)\n" + "\t\t\t{\n" + "\t\t\t\te.printStackTrace();\n"
							+ "\t\t\t}\n" + "\t\t}\n" + "\n"
							+ "\t\tSystem.out.println(\"******************************************\");\n" + "\n"
							+ "\t\tfor (int i = 0; i < results.size(); i++)\n"
							+ "\t\t\tSystem.out.println(\"TestCase #\" + (i + 1) + \": \" + (results.get(i) ? "
							+ "\"SUCCESS\" : \"WRONG ANSWER\") + \" in \"\n"
							+ "\t\t\t\t\t+ timeTaken.get(i) + \"ms\");\n" + "\t}\n" + "\n"
							+ "\tstatic List<TestCase> readTestCases(String xml)\n" + "\t{\n"
							+ "\t\tList<TestCase> testCases = new ArrayList<>();\n"
							+ "\t\tString testCaseTag = \"test\";\n" + "\t\tString inputTag = \"input\";\n"
							+ "\t\tString outputTag = \"output\";\n" + "\n" + "\t\ttry\n" + "\t\t{\n"
							+ "\t\t\tFile xmlFile = new File(xml);\n"
							+ "\t\t\tDocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();\n"
							+ "\t\t\tDocumentBuilder dBuilder = dbFactory.newDocumentBuilder();\n"
							+ "\t\t\tDocument doc = dBuilder.parse(xmlFile);\n" + "\n"
							+ "\t\t\tdoc.getDocumentElement().normalize();\n" + "\n"
							+ "\t\t\tNodeList nodeList = doc.getElementsByTagName(testCaseTag);\n" + "\n"
							+ "\t\t\tfor (int temp = 0; temp < nodeList.getLength(); temp++)\n" + "\t\t\t{\n"
							+ "\t\t\t\tNode nNode = nodeList.item(temp);\n" + "\n"
							+ "\t\t\t\tif (nNode.getNodeType() == Node.ELEMENT_NODE)\n" + "\t\t\t\t{\n"
							+ "\t\t\t\t\tElement element = (Element) nNode;\n" + "\n"
							+ "\t\t\t\t\ttestCases.add(new TestCase(element.getElementsByTagName(inputTag).item(0)"
							+ ".getTextContent(),\n"
							+ "\t\t\t\t\t\t\telement.getElementsByTagName(outputTag).item(0).getTextContent()));\n"
							+ "\t\t\t\t}\n" + "\t\t\t}\n" + "\t\t}\n" + "\t\tcatch (Exception e)\n" + "\t\t{\n"
							+ "\t\t\te.printStackTrace();\n" + "\t\t}\n" + "\n" + "\t\treturn testCases;\n" + "\t}\n"
							+ "\n" + "\tstatic class StringInputStream extends InputStream\n" + "\t{\n"
							+ "\t\tString string;\n" + "\t\tint pos;\n" + "\n"
							+ "\t\t@Override public int read() throws IOException\n" + "\t\t{\n"
							+ "\t\t\tif (pos >= string.length())\n" + "\t\t\t\treturn -1;\n" + "\n"
							+ "\t\t\treturn string.charAt(pos++);\n" + "\t\t}\n" + "\n"
							+ "\t\tpublic String getString()\n" + "\t\t{\n" + "\t\t\treturn string;\n" + "\t\t}\n"
							+ "\n" + "\t\tpublic int getPos()\n" + "\t\t{\n" + "\t\t\treturn pos;\n" + "\t\t}\n" + "\n"
							+ "\t\tStringInputStream(String string)\n" + "\t\t{\n" + "\t\t\tthis.string = string;\n"
							+ "\t\t}\n" + "\n" + "\t}\n" + "\n"
							+ "\tstatic class StringOutputStream extends OutputStream\n" + "\t{\n"
							+ "\t\tStringBuilder output;\n" + "\n"
							+ "\t\t@Override public void write(int b) throws IOException\n" + "\t\t{\n"
							+ "\t\t\toutput.appendCodePoint(b);\n" + "\t\t}\n" + "\n"
							+ "\t\tpublic StringBuilder getOutput()\n" + "\t\t{\n" + "\t\t\treturn output;\n"
							+ "\t\t}\n" + "\n" + "\t\tStringOutputStream()\n" + "\t\t{\n"
							+ "\t\t\toutput = new StringBuilder(\"\");\n" + "\t\t}\n" + "\n" + "\t}\n" + "\n"
							+ "\tstatic class TestCase\n" + "\t{\n" + "\t\tprivate String input, output;\n" + "\n"
							+ "\t\tpublic String getInput()\n" + "\t\t{\n" + "\t\t\treturn input;\n" + "\t\t}\n" + "\n"
							+ "\t\tpublic void setInput(String input)\n" + "\t\t{\n" + "\t\t\tthis.input = input;\n"
							+ "\t\t}\n" + "\n" + "\t\tpublic String getOutput()\n" + "\t\t{\n"
							+ "\t\t\treturn output;\n" + "\t\t}\n" + "\n" + "\t\tpublic void setOutput(String "
							+ "output)\n" + "\t\t{\n" + "\t\t\tthis.output = output;\n" + "\t\t}\n" + "\n"
							+ "\t\t@Override public String toString()\n" + "\t\t{\n"
							+ "\t\t\treturn input + \"=>\\n\" + output;\n" + "\t\t}\n" + "\n"
							+ "\t\tpublic TestCase(String input)\n" + "\t\t{\n" + "\t\t\tthis.input = input;\n"
							+ "\t\t}\n" + "\n" + "\t\tpublic TestCase(String input, String output)\n" + "\t\t{\n"
							+ "\t\t\tthis.input = input;\n" + "\t\t\tthis.output = output;\n" + "\t\t}\n" + "\n"
							+ "\t}\n" + "\n" + "}\n";

			out.print(testerTemplate);
			out.flush();
			out.close();

			return true;
		}
		catch (IOException e1)
		{
			e1.printStackTrace();
		}

		return false;
	}

	public static List<TestCase> readTestCases(String xml)
	{
		List<TestCase> testCases = new ArrayList<>();

		try
		{
			File xmlFile = new File(xml);
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(xmlFile);

			doc.getDocumentElement().normalize();

			NodeList nodeList = doc.getElementsByTagName(testCaseTag);

			for (int temp = 0; temp < nodeList.getLength(); temp++)
			{
				Node nNode = nodeList.item(temp);

				if (nNode.getNodeType() == Node.ELEMENT_NODE)
				{
					Element element = (Element) nNode;

					testCases.add(new TestCase(element.getElementsByTagName(inputTag).item(0).getTextContent(),
							element.getElementsByTagName(outputTag).item(0).getTextContent()));
				}
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}

		return testCases;
	}

}
