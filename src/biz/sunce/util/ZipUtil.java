/*
 * Project opticari
 *
 */
package biz.sunce.util;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;

/**
 * datum:2005.09.01
 * 
 * @author dstanic
 * 
 */
public class ZipUtil {

	public static boolean sprasiZip(String imeNovogZipa,
			String direktorijZaZipat) {

		// imeNovogZipa -> apsolutna putanja
		// direktorijZaZipat -> apsolutna putanja

		try {
			// create a ZipOutputStream to zip the data to
			ZipOutputStream zos = new ZipOutputStream(new FileOutputStream(
					imeNovogZipa));
			// assuming that there is a directory named inFolder (If there isn't
			// create one) in the
			// same directory as the one the code runs from, call the zipDir
			// method
			zipDir(direktorijZaZipat, zos);
			// close the stream
			zos.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	public static void zipDir(String dir2zip, ZipOutputStream zos) {
		try {
			// create a new File object based on the directory we have to zip
			// File
			File zipDir = new File(dir2zip);
			// get a listing of the directory content

			String[] dirList = zipDir.list();
			byte[] readBuffer = new byte[2156];
			int bytesIn = 0;

			// loop through dirList, and zip the files
			if (dirList != null) // 29.11.05. -asabo- dodano
				for (int i = 0; i < dirList.length; i++) {
					File f = new File(zipDir, dirList[i]);
					if (f.isDirectory()) {
						// if the File object is a directory, call this
						// function again to add its content recursively
						String filePath = f.getPath();
						zipDir(filePath, zos);
						// loop again
						continue;
					}
					// if we reached here, the File object f was not a directory
					// create a FileInputStream on top of f

					FileInputStream fis = new FileInputStream(f);
					// create a new zip entry
					ZipEntry anEntry = new ZipEntry(f.getPath());
					// place the zip entry in the ZipOutputStream object
					zos.putNextEntry(anEntry);
					// now write the content of the file to the ZipOutputStream
					while ((bytesIn = fis.read(readBuffer)) != -1) {
						zos.write(readBuffer, 0, bytesIn);
					}
					// close the Stream
					fis.close();
				}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}// zipDir

	/**
	 * raspakirava zip datoteku u željeni folder
	 * 
	 * @param zip
	 *            - adresa streama u classpathu
	 * @param directoryTo
	 *            - folder di æe se extractat
	 */
	public static final void extractZipStreamFromClasspath(String szip,
			File directoryTo) {

		InputStream is = null;
		FileOutputStream out=null;
		try {
			ClassLoader classLoader = ResourceLoader.class.getClassLoader();
			URL resource = classLoader.getResource(szip);
			is = resource.openStream();
			File tempZip=File.createTempFile("tempZip",".zip");
			
			out = new FileOutputStream(tempZip);
			
			byte[] buff=new byte[16384];
			int read=0;
			
			while ((read=is.read(buff))>0){
				out.write(buff, 0, read);
			}
			try{is.close();}catch(IOException ioe){}
			try{out.close();}catch(IOException ioe){}
			
			unzipFile(tempZip, directoryTo);
			
		} catch (IOException ex) {
			throw new IllegalArgumentException("File " + szip + " not found");
		}
		finally{
			try{is.close();}catch(IOException ioe){}
			try{out.close();}catch(IOException ioe){}
		}
	}

	public static final void unzipFile(File zipFile, File directoryTo)
			throws ZipException, IOException {
		int BUFFER = 2048;

		ZipFile zip = new ZipFile(zipFile);
		String newPath = directoryTo.getAbsolutePath();

		new File(newPath).mkdir();
		Enumeration<ZipEntry> zipFileEntries = (Enumeration<ZipEntry>) zip
				.entries();

		// Process each entry
		while (zipFileEntries.hasMoreElements()) {
			// grab a zip file entry
			ZipEntry entry = (ZipEntry) zipFileEntries.nextElement();
			String currentEntry = entry.getName();
			File destFile = new File(newPath, currentEntry);
			// destFile = new File(newPath, destFile.getName());
			File destinationParent = destFile.getParentFile();

			// create the parent directory structure if needed
			destinationParent.mkdirs();

			if (!entry.isDirectory()) {
				BufferedInputStream is = new BufferedInputStream(
						zip.getInputStream(entry));
				int currentByte;
				// establish buffer for writing file
				byte data[] = new byte[BUFFER];

				// write the current file to disk
				FileOutputStream fos = new FileOutputStream(destFile);
				BufferedOutputStream dest = new BufferedOutputStream(fos,
						BUFFER);

				// read and write until last byte is encountered
				while ((currentByte = is.read(data, 0, BUFFER)) != -1) {
					dest.write(data, 0, currentByte);
				}
				dest.flush();
				dest.close();
				is.close();
			}

			// if (currentEntry.endsWith(".zip"))
			// {
			// // found a zip file, try to open
			// unzipFile(currentEntry,destFile);
			// }
		}
	}// unzipFile
}
