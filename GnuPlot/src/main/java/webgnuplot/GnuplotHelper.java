package de.wg.core;

import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;

import javax.imageio.ImageIO;

/**
 * This class encapsulate the OS dependent executable procedure for calling
 * gnuplot.
 * 
 * @author peter
 */
public class GnuplotHelper {

	private final static GnuplotHelper gpHelper = new GnuplotHelper();

	public static GnuplotHelper getDefault() {
		return gpHelper;
	}

	private GnuplotHelper() {
	}
	
	 /** Portable Network Graphics - lossless */
	public static final String PNG = "png";
	
	/** Joint Photographic Experts Group format - lossy */
	public static final String JPEG = "jpeg";
	
	/** Graphics Interchange Format - lossless, but 256 colour restriction */
	public static final String GIF = "gif";

	private static String FILE_TYPE = JPEG;
	
        public static String pathPrefix = "";
        
	/**
	 * @param statementsText
	 * @return
	 * @throws IOException
	 */
	public BufferedImage getImage(String statementsText) throws IOException {	
		//BufferedImage image = new BufferedImage(123, 123, BufferedImage.TYPE_INT_RGB);	
		File imageResult = File.createTempFile("gpResult", "." + FILE_TYPE);
		File statements = File.createTempFile("gpStatements", ".gp");

		String canonImageResult = imageResult.getPath();
		String canonStatementsFile = statements.getPath();

		// Put the statements into a file.
		// TODO: The line number in the text area will not be the same like in file, 
		// because we add here two additional lines.
		FileWriter fw = new FileWriter(statements);
		fw.write(";set terminal " + getGPType(FILE_TYPE) + ";");
		fw.write("set output '" + canonImageResult + "';");
		fw.write(statementsText);
		fw.close();

		Process process = Runtime.getRuntime().exec(
				pathPrefix + "gnuplot \"" + canonStatementsFile + "\"");

		BufferedReader br = new BufferedReader(new InputStreamReader(process
				.getErrorStream()));
		String line = null;
		StringBuffer sb = new StringBuffer();
		while ((line = br.readLine()) != null) {
			sb.append(line);
			sb.append('\n');
		}
                
		// process.waitFor(); is possible but could hang if OS waits for
		// emptying the error stream

		if (sb.length() > 0) {
			throw new IOException("Couldn't successfully execute gnuplot. "
					+ "It says:\n\n" + sb.toString());
		}

		BufferedImage image = ImageIO.read(imageResult);

		// clean up temporary files:
		imageResult.delete();
		statements.delete();

		return image;
	}

	private String getGPType(String fileType) {		
		return fileType;		
	}

	public byte[] getImageBytes(String statementsText) throws IOException {
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();		 		 		
		BufferedImage image = getImage(statementsText);		
		ImageIO.write(image, FILE_TYPE, outputStream);
		return outputStream.toByteArray();
	}
}
