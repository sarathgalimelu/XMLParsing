import java.io.*;
import java.util.*;
import javax.xml.parsers.*;
import org.w3c.dom.*;
import org.xml.sax.SAXException;

public class XMLParsingClass {

	public static void main(String[] args) throws IOException, SAXException {
		Map<String, String> textboxmap = new LinkedHashMap<String, String>();
		String input = "";
		StringBuilder hbuilder = new StringBuilder();
		StringBuilder sbuilder = new StringBuilder();
		StringBuilder insertbuilder = new StringBuilder();
		StringBuilder querybuilder = new StringBuilder();
		StringBuilder valuebuilder = new StringBuilder();
		StringBuilder endquery =  new StringBuilder();
		StringBuilder retrievebuilder = new StringBuilder();
		StringBuilder checkboxBuilder = new StringBuilder();
		String KeyVal = null, KeyLength = null, DatatypeVal = null, Keytype = null;
		for (String s : args) {
			input = s;
		}
		Properties prop = new Properties();
		FileInputStream fileInput = new FileInputStream(new File("config.properties"));
		prop.load(fileInput);
		String HostName=prop.getProperty("hostname");
		String DBName=prop.getProperty("database");
		String UserName=prop.getProperty("username");
		String Password=prop.getProperty("password");
		fileInput.close();
		DocumentBuilderFactory Dbf = DocumentBuilderFactory.newInstance();
		try {
			DocumentBuilder build = Dbf.newDocumentBuilder();
			Document document = build.parse(input);
			document.getDocumentElement().normalize();
			StringBuilder primary = new StringBuilder();
			hbuilder.append("<html><head><title>HTML Page</title></head><body><h1 align='center'>");
			hbuilder.append("<form name='FormName' action = 'Index.php' method='post'>");
			sbuilder.append("DROP TABLE  IF EXISTS form;\n");
			sbuilder.append("CREATE TABLE form(\n");
			insertbuilder.append("<?php\n session_start();\n?>\n");
			insertbuilder.append("<?php\n$hostname=\""+HostName+"\";\n$username=\""+UserName+"\";\n$dbname=\""+DBName+"\";\n$password="+Password+";\n\n");
			insertbuilder.append("$connection = new mysqli($hostname, $username, $password, $dbname);\n\n");
			insertbuilder.append("if ($connection->connect_error) {\ndie(\"Could not connect: \" . mysql_error());\n}\n\n");
			querybuilder.append("\n$temp=\"");
	
			NodeList elementlist1 = document.getElementsByTagName("title");
			for (int i = 0; i < elementlist1.getLength(); i++) {
				if (elementlist1.item(i).getNodeType() == Node.ELEMENT_NODE) {
					Element eElement1 = (Element) elementlist1.item(i);
					hbuilder.append(eElement1.getElementsByTagName("caption")
							.item(0).getTextContent()
							+ "</h1><br>");
				}
			}

	
			NodeList elementlist2 = document.getElementsByTagName("textbox");
			for (int i = 0; i < elementlist2.getLength(); i++) {
				if (elementlist2.item(i).getNodeType() == Node.ELEMENT_NODE) {
					Element eElement2 = (Element) elementlist2.item(i);
					if (eElement2.getAttribute("datatype").equals("string")) {
						DatatypeVal = "varchar";
						
					} else if (eElement2.getAttribute("datatype").equals(
							"integer")) {
						DatatypeVal = "int";
					}

					Attr val = ((Element) elementlist2.item(i))
							.getAttributeNode("key");
					String val1 = new StringBuilder().append(val).toString();
					String val2 = val1.concat("l");
					String val3 = "nulll";
					if (!(val2.equals(val3))) {
						
						KeyVal = new StringBuilder().append(
								eElement2.getElementsByTagName("name").item(0)
										.getTextContent()).toString();
						if (eElement2.getAttribute("datatype").equals("string")) {
							Keytype = "varchar";
						} else if (eElement2.getAttribute("datatype").equals(
								"integer")) {
							Keytype = "int";
						}

						KeyLength = new StringBuilder().append(
								eElement2.getElementsByTagName("maxlength")
										.item(0).getTextContent()).toString();

						primary.append("PRIMARY KEY("
								+ eElement2.getElementsByTagName("name")
										.item(0).getTextContent() + ")\n);\n");

					}

					hbuilder.append(eElement2.getElementsByTagName("caption")
							.item(0).getTextContent()
							+ "&nbsp");

					hbuilder.append("<input type= 'textbox' name='");
					hbuilder.append(eElement2.getElementsByTagName("name")
							.item(0).getTextContent()
							+ "' size='"
							+ eElement2.getElementsByTagName("size").item(0)
									.getTextContent()
							+ "' maxlength='"
							+ eElement2.getElementsByTagName("maxlength")
									.item(0).getTextContent() + "'><br><br>");

					sbuilder.append(eElement2.getElementsByTagName("name")
							.item(0).getTextContent()
							+ " "
							+ DatatypeVal
							+ "("
							+ eElement2.getElementsByTagName("maxlength")
									.item(0).getTextContent() + "),\n");
					
					insertbuilder.append("$"+eElement2.getElementsByTagName("name").item(0).getTextContent()+"="+"$_POST[\""+eElement2.getElementsByTagName("name")
							.item(0).getTextContent()+"\"];\n");
					querybuilder.append(eElement2.getElementsByTagName("name").item(0).getTextContent()+",");
					valuebuilder.append("'$"+eElement2.getElementsByTagName("name").item(0).getTextContent()+"',");
					
				}
			}

			NodeList elementlist3 = document.getElementsByTagName("checkboxes");
			checkboxBuilder
					.append("DROP TABLE IF EXISTS form_checkbox;\nCREATE TABLE form_checkbox(\n");
			checkboxBuilder
					.append("form_checkbox_id int(20) NOT NULL AUTO_INCREMENT,\n"
							+ KeyVal
							+ " "
							+ Keytype
							+ "("
							+ KeyLength
							+ ") NOT NULL,\n");
			checkboxBuilder
					.append("form_checkbox_Name varchar(200) NOT NULL,\n form_checkbox_value varchar(200) NOT NULL,\n form_checkbox_caption varchar(200) NOT NULL,\n PRIMARY KEY(form_checkbox_id),\n KEY Foreign_key_checkbox("
							+ KeyVal + "),\n");
			checkboxBuilder.append("CONSTRAINT FK_form_checkbox FOREIGN KEY ("
					+ KeyVal + ") REFERENCES form (" + KeyVal + ")\n);");
			for (int i = 0; i < elementlist3.getLength(); i++) {
				if (elementlist3.item(i).getNodeType() == Node.ELEMENT_NODE) {
					Element eElement3 = (Element) elementlist3.item(i);
					hbuilder.append(eElement3.getElementsByTagName("caption")
							.item(0).getTextContent()
							+ "<br> ");

					NodeList elementlist4 = document
							.getElementsByTagName("checkboxgroup");
					for (int j = 0; j < elementlist4.getLength(); j++) {
						if (elementlist4.item(j).getNodeType() == Node.ELEMENT_NODE) {
							NodeList elementlist5 = document
									.getElementsByTagName("checkbox");

							for (int k = 0; k < elementlist5.getLength(); k++) {
								if (elementlist5.item(k).getNodeType() == Node.ELEMENT_NODE) {

									Element eElement5 = (Element) elementlist5
											.item(k);
									hbuilder.append(eElement5
											.getElementsByTagName("caption")
											.item(0).getTextContent());
									hbuilder.append("<input type='checkbox' value='"
											+ eElement5
													.getElementsByTagName(
															"value").item(0)
													.getTextContent() + "' ");

									if (eElement5.getAttribute("status")
											.equals("checked")) {
										hbuilder.append("checked");
									}
									hbuilder.append("><br>");

								}

							}
							hbuilder.append("<br><br>");
						}
					}
				}
			}

		
			NodeList elementlist6 = document.getElementsByTagName("select");
			for (int i = 0; i < elementlist6.getLength(); i++) {
				if (elementlist6.item(i).getNodeType() == Node.ELEMENT_NODE) {
					Element eElement6 = (Element) elementlist6.item(i);
					hbuilder.append(eElement6.getElementsByTagName("caption")
							.item(0).getTextContent()
							+ "&nbsp");
					NodeList elementlist7 = document

					.getElementsByTagName("options");
					hbuilder.append("<select name='");
					hbuilder.append(eElement6.getElementsByTagName("name")
							.item(0).getTextContent()
							+ "'><br>");
					for (int j = 0; j < elementlist7.getLength(); j++) {
						if (elementlist7.item(j).getNodeType() == Node.ELEMENT_NODE) {
							NodeList elementlist8 = document
									.getElementsByTagName("option");
							for (int k = 0; k < elementlist8.getLength(); k++) {
								if (elementlist8.item(k).getNodeType() == Node.ELEMENT_NODE) {
									Element eElement8 = (Element) elementlist8
											.item(k);
									hbuilder.append("<option value='"
											+ eElement8
													.getElementsByTagName(
															"value").item(0)
													.getTextContent()
											+ "'>"
											+ eElement8
													.getElementsByTagName(
															"caption").item(0)
													.getTextContent() + "</option><br>");
								}
							}

						}
					}
					hbuilder.append("</select>");

					textboxmap.put(
							eElement6.getElementsByTagName("name").item(0)
									.getTextContent(), String.valueOf(200));
					insertbuilder.append("$"+eElement6.getElementsByTagName("name")
							.item(0).getTextContent()+"="+"$_POST[\""+eElement6.getElementsByTagName("name")
							.item(0).getTextContent()+"\"];\n");
					querybuilder.append(eElement6.getElementsByTagName("name").item(0).getTextContent()+",");
					valuebuilder.append("'$"+eElement6.getElementsByTagName("name").item(0).getTextContent()+"',");
				}
			}
			hbuilder.append("<br><br>");

			NodeList elementlist9 = document.getElementsByTagName("radio");
			for (int i = 0; i < elementlist9.getLength(); i++) {
				if (elementlist9.item(i).getNodeType() == Node.ELEMENT_NODE) {
					Element eElement9 = (Element) elementlist9.item(i);
					hbuilder.append(eElement9.getElementsByTagName("caption")
							.item(0).getTextContent()
							+ "<br>");
					NodeList elementlist10 = document
							.getElementsByTagName("radiogroup");

					for (int j = 0; j < elementlist10.getLength(); j++) {
						if (elementlist10.item(j).getNodeType() == Node.ELEMENT_NODE) {
							NodeList elementlist11 = document
									.getElementsByTagName("radioelement");
							for (int k = 0; k < elementlist11.getLength(); k++) {
								if (elementlist11.item(k).getNodeType() == Node.ELEMENT_NODE) {
									Element eElement11 = (Element) elementlist11
											.item(k);
									hbuilder.append("<input type='radio'  name='"
											+ eElement9
													.getElementsByTagName(
															"name").item(0)
													.getTextContent()
											+ "' value='"
											+ eElement11
													.getElementsByTagName(
															"value").item(0)
													.getTextContent()
											+ "' checked>"
											+ eElement11
													.getElementsByTagName(
															"caption").item(0)
													.getTextContent() + "<br>");
								}
							}

						}

					}
					textboxmap.put(
							eElement9.getElementsByTagName("name").item(0)
									.getTextContent(), String.valueOf(200));
					
					insertbuilder.append("$"+eElement9.getElementsByTagName("name")
							.item(0).getTextContent()+"="+"$_POST[\""+eElement9.getElementsByTagName("name")
							.item(0).getTextContent()+"\"];\n");
					querybuilder.append(eElement9.getElementsByTagName("name").item(0).getTextContent()+",");
					valuebuilder.append("'$"+eElement9.getElementsByTagName("name").item(0).getTextContent()+"',");
				}
			}
			hbuilder.append("<br>");

	
			NodeList elementlist13 = document
					.getElementsByTagName("multiselect");
			for (int i = 0; i < elementlist13.getLength(); i++) {
				if (elementlist13.item(i).getNodeType() == Node.ELEMENT_NODE) {
					Element eElement13 = (Element) elementlist13.item(i);
					hbuilder.append(eElement13.getElementsByTagName("caption")
							.item(0).getTextContent()
							+ "&nbsp");
					hbuilder.append("<select multiple>");
					NodeList elementlist14 = document
							.getElementsByTagName("options");
					for (int j = 0; j < elementlist14.getLength(); j++) {
						if (elementlist14.item(j).getNodeType() == Node.ELEMENT_NODE) {
							NodeList elementlist15 = document
									.getElementsByTagName("option");
							for (int k = 0; k < elementlist15.getLength(); k++) {
								if (elementlist15.item(k).getNodeType() == Node.ELEMENT_NODE) {
									Element eElement15 = (Element) elementlist15
											.item(k);
									hbuilder.append("<option value='"
											+ eElement15
													.getElementsByTagName(
															"value").item(0)
													.getTextContent()
											+ "'>"
											+ eElement15
													.getElementsByTagName(
															"caption").item(0)
													.getTextContent() + "<br>");
								}
							}

						}
					}
					textboxmap.put(eElement13.getElementsByTagName("name")
							.item(0).getTextContent(), String.valueOf(200));
					insertbuilder.append("$"+eElement13.getElementsByTagName("name")
							.item(0).getTextContent()+"="+"$_POST[\""+eElement13.getElementsByTagName("name")
							.item(0).getTextContent()+"\"];\n");
					querybuilder.append(eElement13.getElementsByTagName("name").item(0).getTextContent()+",");
					valuebuilder.append("'$"+eElement13.getElementsByTagName("name").item(0).getTextContent()+"',");
				}
				hbuilder.append("</select><br><br>");
			}
			
			
			NodeList elementlist12 = document.getElementsByTagName("submit");
			for (int i = 0; i < elementlist12.getLength(); i++) {
				if (elementlist12.item(i).getNodeType() == Node.ELEMENT_NODE) {
					Element eElement12 = (Element) elementlist12.item(i);
					hbuilder.append("<input type='submit' value='");
					hbuilder.append(eElement12.getElementsByTagName("caption")
							.item(0).getTextContent()
							+ "'>");
				}
			}
			hbuilder.append("</form></body></html>");

			// LINKEDHASHMAP VALUES ADDITION TO SQL StringBuilder 
			for (String key : textboxmap.keySet())
				sbuilder.append(key + " varchar(" + textboxmap.get(key)
						+ "),\n");
			
			// CODE FOR PHP INSERT QUERY

			
			querybuilder.append("\";\n$temp1=rtrim($temp, ',');");
			querybuilder.append("\n\n$sql=\"INSERT INTO form($temp1) values(");
			
			String values = valuebuilder.toString();
			values=values.substring(0,values.length()-1);
			 values=values+")\";\n";
			endquery.append("if ($connection->query($sql) === TRUE) {\necho \"Entered Data Succesfully in the Database\";");
			endquery.append("} else {\n	echo \"Error: \" . $sql . \"<br>\" . $connection->error;\n}\n\n$connection->close();\n\n?>");

			String html = hbuilder.toString();
			String sql = sbuilder.toString();
			String sql1 = primary.toString();
			String sql2 = checkboxBuilder.toString();
			String insert =insertbuilder.toString();
			String quer = querybuilder.toString();
			String end = endquery.toString();
			BufferedWriter bw = new BufferedWriter(new FileWriter(new File("Index.html").getAbsoluteFile()));
			BufferedWriter bw1 = new BufferedWriter(new FileWriter(new File("Index.sql").getAbsoluteFile()));
			BufferedWriter bw2 = new BufferedWriter(new FileWriter(new File("Index.php").getAbsoluteFile()));
			bw.write(html);
			bw1.write(sql);
			bw1.write(sql1);
			bw1.write(sql2);
			bw2.write(insert);
			bw2.write(quer);
			bw2.write(values);
			bw2.write(end);
			bw.close();
			bw1.close();
			bw2.close();
		}

		catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}