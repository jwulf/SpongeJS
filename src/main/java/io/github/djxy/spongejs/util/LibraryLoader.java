package io.github.djxy.spongejs.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by Samuel on 2016-09-07.
 */
public class LibraryLoader {

    /**
     * From https://github.com/adamheinrich/native-utils
     *
     * Loads library from current JAR archive
     *
     * The file from JAR is copied into system temporary directory and then loaded. The temporary file is deleted after exiting.
     * Method uses String as filename because the pathname is "abstract", not system-dependent.
     *
     * @throws IOException If temporary file creation or read/write operation fails
     * @throws IllegalArgumentException If source file (param path) does not exist
     * @throws IllegalArgumentException If the path is not absolute or if the filename is shorter than three characters (restriction of {@see File#createTempFile(java.lang.String, java.lang.String)}).
     */
    public static void loadLibraryFromJar() throws IOException {
        String path = "/"+computeLibraryFullName();

        // Obtain filename from path
        String[] parts = path.split("/");
        String filename = (parts.length > 1) ? parts[parts.length - 1] : null;

        // Split filename to prexif and suffix (extension)
        String prefix = "";
        String suffix = null;
        if (filename != null) {
            parts = filename.split("\\.", 2);
            prefix = parts[0];
            suffix = (parts.length > 1) ? "."+parts[parts.length - 1] : null; // Thanks, davs! :-)
        }

        // Check if the filename is okay
        if (filename == null || prefix.length() < 3) {
            throw new IllegalArgumentException("The filename has to be at least 3 characters long.");
        }

        // Prepare temporary file
        File temp = File.createTempFile(prefix, suffix);
        temp.deleteOnExit();

        if (!temp.exists()) {
            throw new FileNotFoundException("File " + temp.getAbsolutePath() + " does not exist.");
        }

        // Prepare buffer for data copying
        byte[] buffer = new byte[1024];
        int readBytes;

        // Open and check input stream
        InputStream is = LibraryLoader.class.getResourceAsStream(path);
        if (is == null) {
            throw new FileNotFoundException("File " + path + " was not found inside JAR.");
        }

        // Open output stream and copy data between source file in JAR and the temporary file
        OutputStream os = new FileOutputStream(temp);
        try {
            while ((readBytes = is.read(buffer)) != -1) {
                os.write(buffer, 0, readBytes);
            }
        } finally {
            // If read/write fails, close streams safely before throwing an exception
            os.close();
            is.close();
        }

        // Finally, load the library
        System.load(temp.getAbsolutePath());
    }

    /**
     * From com.eclipsesource.v8.LibraryLoader
     * @return
     */
    private static String computeLibraryFullName() {
        return "lib" + computeLibraryShortName() + "." + getOSFileExtension();
    }

    /**
     * From com.eclipsesource.v8.LibraryLoader
     * @return
     */
    private static String computeLibraryShortName() {
        String base = "j2v8";
        String osSuffix = getOS();
        String archSuffix = getArchSuffix();
        return base + "_" + osSuffix + "_" + archSuffix;
    }

    /**
     * From com.eclipsesource.v8.LibraryLoader
     * @return
     */
    private static String getOsName() {
        return System.getProperty("os.name") + System.getProperty("java.specification.vendor");
    }

    /**
     * From com.eclipsesource.v8.LibraryLoader
     * @return
     */
    private static boolean isWindows() {
        return getOsName().startsWith("Windows");
    }

    /**
     * From com.eclipsesource.v8.LibraryLoader
     * @return
     */
    private static boolean isMac() {
        return getOsName().startsWith("Mac");
    }

    /**
     * From com.eclipsesource.v8.LibraryLoader
     * @return
     */
    private static boolean isLinux() {
        return getOsName().startsWith("Linux");
    }

    /**
     * From com.eclipsesource.v8.LibraryLoader
     * @return
     */
    private static boolean isNativeClient() {
        return getOsName().startsWith("nacl");
    }

    /**
     * From com.eclipsesource.v8.LibraryLoader
     * @return
     */
    private static boolean isAndroid() {
        return getOsName().contains("Android");
    }

    /**
     * From com.eclipsesource.v8.LibraryLoader
     * @return
     */
    private static String getArchSuffix() {
        String arch = System.getProperty("os.arch");
        if (arch.equals("i686")) {
            return "x86";
        } else if (arch.equals("amd64")) {
            return "x86_64";
        } else if (arch.equals("nacl")) {
            return "armv7l";
        } else if (arch.equals("aarch64")) {
            return "armv7l";
        }
        return arch;
    }

    /**
     * From com.eclipsesource.v8.LibraryLoader
     * @return
     */
    private static String getOSFileExtension() {
        if (isWindows()) {
            return "dll";
        } else if (isMac()) {
            return "dylib";
        } else if (isLinux()) {
            return "so";
        } else if (isNativeClient()) {
            return "so";
        }
        throw new UnsatisfiedLinkError("Unsupported platform: " + getOsName());
    }

    /**
     * From com.eclipsesource.v8.LibraryLoader
     * @return
     */
    private static String getOS() {
        if (isWindows()) {
            return "win32";
        } else if (isMac()) {
            return "macosx";
        } else if (isLinux() && !isAndroid()) {
            return "linux";
        } else if (isAndroid()) {
            return "android";
        }
        throw new UnsatisfiedLinkError("Unsupported platform: " + getOsName());
    }

}
