package com.general.mbts4ma.view.framework.util;

import spoon.reflect.declaration.CtImport;
import spoon.reflect.declaration.CtPackage;
import spoon.reflect.declaration.CtType;
import spoon.reflect.factory.Factory;
import spoon.reflect.reference.CtTypeReference;
import spoon.reflect.visitor.DefaultJavaPrettyPrinter;
import spoon.support.compiler.jdt.JDTBasedSpoonCompiler;
import spoon.support.reflect.cu.CompilationUnitImpl;
import spoon.support.reflect.declaration.CtImportImpl;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

import org.apache.commons.io.IOUtils;

public class SpoonModelCompiler extends JDTBasedSpoonCompiler {
	
	public SpoonModelCompiler(Factory factory) {
		super(factory);
	}

	public void generateProcessedSourceFilesUsingCUs() {

		try {
			getFactory().getEnvironment().debugMessage(
					"Generating source using compilation units...");
			// Check output directory
			if (getSourceOutputDirectory() == null) {
				throw new RuntimeException(
						"You should set output directory before generating source files");
			}
			// Create spooned dir
			if (getSourceOutputDirectory().isFile()) {
				throw new RuntimeException("Output must be a directory");
			}
			if (!getSourceOutputDirectory().exists()) {
				if (!getSourceOutputDirectory().mkdirs()) {
					throw new RuntimeException("Error creating output directory");
				}
			}
			getFactory().getEnvironment().setSourceOutputDirectory(getSourceOutputDirectory().getCanonicalFile());
			getFactory().getEnvironment().debugMessage(
					"Generating source files to: " + getSourceOutputDirectory());

			List<File> printedFiles = new ArrayList<File>();
			printing:
			for (spoon.reflect.cu.CompilationUnit cu : getFactory().CompilationUnit()
					.getMap().values()) {
		        
				getFactory().getEnvironment().debugMessage(
						"Generating source for compilation unit: " + cu.getFile());

				CtType<?> element = cu.getMainType();

				CtPackage pack = element.getPackage();

				// create package directory
				File packageDir;
				if (pack.getQualifiedName()
						.equals(CtPackage.TOP_LEVEL_PACKAGE_NAME)) {
					packageDir = new File(getSourceOutputDirectory().getAbsolutePath());
				} else {
					char dot = '.';
					// Create current package dir
					packageDir = new File(getSourceOutputDirectory().getAbsolutePath()
							+ File.separatorChar
							+ pack.getQualifiedName().replace(dot, File.separatorChar));
				}
				if (!packageDir.exists()) {
					if (!packageDir.mkdirs()) {
						throw new RuntimeException(
								"Error creating output directory");
					}
				}

				File file = new File(packageDir.getAbsolutePath()
						+ File.separatorChar + element.getSimpleName()
						+ DefaultJavaPrettyPrinter.JAVA_FILE_EXTENSION);
				file.createNewFile();

				// the path must be given relatively to to the working directory
				InputStream is = getCompilationUnitInputStream(cu.getFile().getPath());

				IOUtils.copy(is, new FileOutputStream(file));

				if (!printedFiles.contains(file)) {
					printedFiles.add(file);
				}
			} // end for
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
}