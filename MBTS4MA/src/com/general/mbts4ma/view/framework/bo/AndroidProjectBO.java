package com.general.mbts4ma.view.framework.bo;

import java.io.File;
import java.io.Serializable;
import java.text.MessageFormat;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import com.general.mbts4ma.view.framework.util.FileUtil;
import com.general.mbts4ma.view.framework.util.XmlUtil;

public class AndroidProjectBO implements Serializable {

	public static final String ANDROID_MANIFEST = "AndroidManifest.xml";
	public static final String ANDROID_MANIFEST_PATH = File.separator + ANDROID_MANIFEST;

	public static final String RES_STRINGS_PATH = File.separator + "res" + File.separator + "values{0}" + File.separator + "strings.xml";

	public static final String STRING_NODE_NAME = "string";
	public static final String STRING_ARRAY_NODE_NAME = "string-array";

	public static synchronized List<String> getServices(String androidProjectPath) {
		List<String> getServices = null;

		if (isAndroidProject(androidProjectPath)) {
			String filepath = androidProjectPath + ANDROID_MANIFEST_PATH;

			try {
				getServices = parseServices(filepath);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		return getServices;
	}

	private static synchronized List<String> parseServices(String filepath) throws Exception {
		List<String> services = new LinkedList<String>();

		File androidManifestFile = new File(filepath);

		if (androidManifestFile != null && androidManifestFile.exists()) {
			String androidManifestXML = FileUtil.readFile(androidManifestFile);

			if (androidManifestXML != null && !"".equalsIgnoreCase(androidManifestXML)) {
				Document document = XmlUtil.parseXML(androidManifestXML);

				if (document != null) {
					NodeList nodeList = ((Element) ((Element) document.getElementsByTagName("manifest").item(0)).getElementsByTagName("application").item(0)).getElementsByTagName("service");

					if (nodeList != null && nodeList.getLength() > 0) {
						for (int i = 0; i < nodeList.getLength(); i++) {
							if (nodeList.item(i) != null && nodeList.item(i).getNodeType() == Node.ELEMENT_NODE) {
								Element node = (Element) nodeList.item(i);

								services.add(node.getAttribute("android:name"));
							}
						}
					}
				}
			}
		}

		return services;
	}

	public static synchronized List<String> getUsesSDK(String androidProjectPath) {
		List<String> usesSDK = null;

		if (isAndroidProject(androidProjectPath)) {
			String filepath = androidProjectPath + ANDROID_MANIFEST_PATH;

			try {
				usesSDK = parseUsesSDK(filepath);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		return usesSDK;
	}

	private static synchronized List<String> parseUsesSDK(String filepath) throws Exception {
		List<String> usesSDK = new LinkedList<String>();

		File androidManifestFile = new File(filepath);

		if (androidManifestFile != null && androidManifestFile.exists()) {
			String androidManifestXML = FileUtil.readFile(androidManifestFile);

			if (androidManifestXML != null && !"".equalsIgnoreCase(androidManifestXML)) {
				Document document = XmlUtil.parseXML(androidManifestXML);

				if (document != null) {
					Element node = (Element) ((Element) document.getElementsByTagName("manifest").item(0)).getElementsByTagName("uses-sdk").item(0);

					if (node != null && node.getNodeType() == Node.ELEMENT_NODE) {
						String minSdkVersion = node.hasAttribute("android:minSdkVersion") ? node.getAttribute("android:minSdkVersion") : "";
						String targetSdkVersion = node.hasAttribute("android:targetSdkVersion") ? node.getAttribute("android:targetSdkVersion") : "";
						String maxSdkVersion = node.hasAttribute("android:maxSdkVersion") ? node.getAttribute("android:maxSdkVersion") : "";

						usesSDK.add(minSdkVersion);
						usesSDK.add(targetSdkVersion);
						usesSDK.add(maxSdkVersion);
					}
				}
			}
		}

		return usesSDK;
	}

	public static synchronized List<String> getLibraries(String androidProjectPath) {
		List<String> libraries = null;

		if (isAndroidProject(androidProjectPath)) {
			String filepath = androidProjectPath + ANDROID_MANIFEST_PATH;

			try {
				libraries = parseLibraries(filepath);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		return libraries;
	}

	private static synchronized List<String> parseLibraries(String filepath) throws Exception {
		List<String> libraries = new LinkedList<String>();

		File androidManifestFile = new File(filepath);

		if (androidManifestFile != null && androidManifestFile.exists()) {
			String androidManifestXML = FileUtil.readFile(androidManifestFile);

			if (androidManifestXML != null && !"".equalsIgnoreCase(androidManifestXML)) {
				Document document = XmlUtil.parseXML(androidManifestXML);

				if (document != null) {
					NodeList nodeList = ((Element) ((Element) document.getElementsByTagName("manifest").item(0)).getElementsByTagName("application").item(0)).getElementsByTagName("uses-library");

					if (nodeList != null && nodeList.getLength() > 0) {
						for (int i = 0; i < nodeList.getLength(); i++) {
							if (nodeList.item(i) != null && nodeList.item(i).getNodeType() == Node.ELEMENT_NODE) {
								Element node = (Element) nodeList.item(i);

								libraries.add(node.getAttribute("android:name"));
							}
						}
					}
				}
			}
		}

		return libraries;
	}

	public static synchronized List<String> getPermissions(String androidProjectPath) {
		List<String> permissions = null;

		if (isAndroidProject(androidProjectPath)) {
			String filepath = androidProjectPath + ANDROID_MANIFEST_PATH;

			try {
				permissions = parsePermissions(filepath);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		return permissions;
	}

	private static synchronized List<String> parsePermissions(String filepath) throws Exception {
		List<String> permissions = new LinkedList<String>();

		File androidManifestFile = new File(filepath);

		if (androidManifestFile != null && androidManifestFile.exists()) {
			String androidManifestXML = FileUtil.readFile(androidManifestFile);

			if (androidManifestXML != null && !"".equalsIgnoreCase(androidManifestXML)) {
				Document document = XmlUtil.parseXML(androidManifestXML);

				if (document != null) {
					NodeList nodeList = ((Element) document.getElementsByTagName("manifest").item(0)).getElementsByTagName("uses-permission");

					if (nodeList != null && nodeList.getLength() > 0) {
						for (int i = 0; i < nodeList.getLength(); i++) {
							if (nodeList.item(i) != null && nodeList.item(i).getNodeType() == Node.ELEMENT_NODE) {
								Element node = (Element) nodeList.item(i);

								permissions.add(node.getAttribute("android:name"));
							}
						}
					}
				}
			}
		}

		return permissions;
	}

	public static synchronized String getPackage(String androidProjectPath) {
		String pack = null;

		if (isAndroidProject(androidProjectPath)) {
			String filepath = androidProjectPath + ANDROID_MANIFEST_PATH;

			try {
				pack = parsePackage(filepath);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		return pack;
	}

	private static synchronized String parsePackage(String filepath) throws Exception {
		String pack = null;

		File androidManifestFile = new File(filepath);

		if (androidManifestFile != null && androidManifestFile.exists()) {
			String androidManifestXML = FileUtil.readFile(androidManifestFile);

			if (androidManifestXML != null && !"".equalsIgnoreCase(androidManifestXML)) {
				Document document = XmlUtil.parseXML(androidManifestXML);

				if (document != null) {
					Element node = (Element) document.getElementsByTagName("manifest").item(0);

					if (node != null && node.getNodeType() == Node.ELEMENT_NODE) {
						pack = node.getAttribute("package");
					}
				}
			}
		}

		return pack;
	}

	public static synchronized List<String> getActivities(String androidProjectPath) {
		List<String> activities = null;

		if (isAndroidProject(androidProjectPath)) {
			String filepath = androidProjectPath + ANDROID_MANIFEST_PATH;

			try {
				activities = parseActivities(filepath);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		return activities;
	}

	private static synchronized List<String> parseActivities(String filepath) throws Exception {
		List<String> activities = new LinkedList<String>();

		File androidManifestFile = new File(filepath);

		if (androidManifestFile != null && androidManifestFile.exists()) {
			String androidManifestXML = FileUtil.readFile(androidManifestFile);

			if (androidManifestXML != null && !"".equalsIgnoreCase(androidManifestXML)) {
				Document document = XmlUtil.parseXML(androidManifestXML);

				if (document != null) {
					NodeList nodeList = ((Element) ((Element) document.getElementsByTagName("manifest").item(0)).getElementsByTagName("application").item(0)).getElementsByTagName("activity");

					if (nodeList != null && nodeList.getLength() > 0) {
						for (int i = 0; i < nodeList.getLength(); i++) {
							if (nodeList.item(i) != null && nodeList.item(i).getNodeType() == Node.ELEMENT_NODE) {
								Element node = (Element) nodeList.item(i);

								activities.add(node.getAttribute("android:name"));
							}
						}
					}
				}
			}
		}

		return activities;
	}

	public static synchronized Map<String, Object> getStrings(String androidProjectPath, String language) {
		Map<String, Object> strings = null;

		if (isAndroidProject(androidProjectPath)) {
			String filepath = androidProjectPath + MessageFormat.format(RES_STRINGS_PATH, language != null && !"".equalsIgnoreCase(language) ? "-" + language : "");

			try {
				strings = parseStrings(filepath);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		return strings;
	}

	private static synchronized Map<String, Object> parseStrings(String filepath) throws Exception {
		Map<String, Object> strings = new LinkedHashMap<String, Object>();

		File stringsFile = new File(filepath);

		if (stringsFile != null && stringsFile.exists()) {
			String stringsXML = FileUtil.readFile(stringsFile);

			if (stringsXML != null && !"".equalsIgnoreCase(stringsXML)) {
				Document document = XmlUtil.parseXML(stringsXML);

				if (document != null) {
					NodeList nodeList = document.getElementsByTagName("resources").item(0).getChildNodes();

					if (nodeList != null && nodeList.getLength() > 0) {
						for (int i = 0; i < nodeList.getLength(); i++) {
							if (nodeList.item(i) != null && nodeList.item(i).getNodeType() == Node.ELEMENT_NODE) {
								Element node = (Element) nodeList.item(i);

								if (STRING_NODE_NAME.equals(node.getNodeName())) {
									strings.put("R.string." + node.getAttribute("name"), node.getTextContent());
								} else if (STRING_ARRAY_NODE_NAME.equals(node.getNodeName())) {
									NodeList stringArrayNodeList = node.getChildNodes();

									List<String> arrayValues = new LinkedList<String>();

									if (stringArrayNodeList != null && stringArrayNodeList.getLength() > 0) {
										for (int j = 0; j < stringArrayNodeList.getLength(); j++) {
											if (stringArrayNodeList.item(j) != null && stringArrayNodeList.item(j).getNodeType() == Node.ELEMENT_NODE) {
												Element nodeArrayItem = (Element) stringArrayNodeList.item(j);

												arrayValues.add(nodeArrayItem.getTextContent());
											}
										}
									}

									strings.put(node.getAttribute("name"), arrayValues);
								}
							}
						}
					}
				}
			}
		}

		return strings;
	}

	public static synchronized boolean isAndroidProject(String androidProjectPath) {
		if (androidProjectPath != null && !"".equalsIgnoreCase(androidProjectPath)) {
			File androidProjectDirectory = new File(androidProjectPath);

			if (androidProjectDirectory != null && androidProjectDirectory.exists() && androidProjectDirectory.isDirectory()) {
				return FileUtil.hasFile(androidProjectDirectory, ANDROID_MANIFEST);
			}
		}

		return false;
	}

}
