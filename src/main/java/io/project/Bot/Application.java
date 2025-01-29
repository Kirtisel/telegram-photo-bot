package io.project.Bot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Component;

import java.io.*;
import java.util.ArrayList;

@SpringBootApplication
public class Application {
	private static ArrayList<String> captionList = new ArrayList<>();

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
		//initCaptionList();
	}

	private static void initCaptionList() {
		File file = new File("//home//user//testselete//nfile");
		BufferedReader reader;
		String s="";
		String string="";

		try {
			reader = new BufferedReader(new FileReader(file));
		} catch (FileNotFoundException e) {
			throw new RuntimeException(e);
		}


		try {
			string = "";
			do{
				s = reader.readLine();
				if(s != null) string=string.concat(s);
				if(s == null || s.isEmpty()){
					captionList.add(string);
					string = "";
				}
			}while (s != null);

		} catch (IOException e) {
			throw new RuntimeException(e);
		}

		for (String st: captionList) {
			System.out.println(st);
			System.out.println("");
		}

	}

}
