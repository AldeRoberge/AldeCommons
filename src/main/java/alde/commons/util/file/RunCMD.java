package alde.commons.util.file;

import java.io.File;

class RunCMD {

	public static void generateCMD(String runnableJarName) {

		String runFileName = "run.cmd";

		File runnableCMD = new File(runFileName);

		if (!runnableCMD.exists()) {

			runnableJarName = runnableJarName.replace(".jar", "");

			WriteToFile w = new WriteToFile(runFileName);

			w.write("rem Run the program using this file"); // Add a comment
			w.write("@echo off");
			w.write("color 02");
			w.write("java -jar " + runnableJarName + ".jar");
			w.write("pause");
		}

		System.out.println("Exists : " + runnableCMD.exists());

	}

}
