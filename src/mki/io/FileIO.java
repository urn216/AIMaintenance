package mki.io;

import java.awt.image.BufferedImage;

import javax.imageio.ImageIO;
import java.io.*;
import java.nio.file.*;

import java.awt.Desktop;

import java.util.List;
import java.util.ArrayList;

/**
 * Non initializable class with helper methods and fields for use in file Input/Output operations
 * 
 * @author William Kilty
 */
public abstract class FileIO {
  
  //-------------------------------------------------------------------------------------
  //                              FILE TRANSFER HELPERS
  //-------------------------------------------------------------------------------------
  
  /**
   * Writes the given contents out to a file. 
   * <p>
   * Elements of the {@code content} array will not automatically be line-separated. 
   * If this behaviour is desired, each {@code String} will need to end with a {@code '\n'} character.
   * 
   * @param filename the desired destination to write to
   * @param content the contents to write into a file
   */
  public static final void saveToFile(String filename, String... content) {
    try {
      File f = new File(filename);
      f.createNewFile();
      PrintStream out = new PrintStream(f);
      for (String s : content) out.print(s);
      out.close();
    } catch(IOException e){System.err.println("Saving failed " + e);}
  }
  
  /**
   * Copies the contents of a {@code File} to a destination {@code Path}.
   * Will replace any existing contents at the {@code Path}.
   * 
   * @param source the {@code File} to read from
   * @param dest the {@code Path} to write to
   */
  public static final void copyContents(File source, Path dest) {
    try {
      Files.copy(source.toPath(), dest, StandardCopyOption.valueOf("REPLACE_EXISTING"));
    } catch(IOException e){System.err.println("Copying failed " + e);}
    if (source.isDirectory()) {
      for (File fi : source.listFiles()) {
        FileIO.copyContents(fi, dest.resolve(fi.toPath().getFileName()));
      }
    }
  }
  
  /**
   * Copies the contents of an {@code InputStream} to a destination {@code Path}.
   * Will replace any existing contents at the {@code Path}.
   * 
   * @param source the {@code InputStream} to read from
   * @param dest the {@code Path} to write to
   */
  public static final void copyContents(InputStream source, Path dest) {
    try {
      Files.copy(source, dest, StandardCopyOption.valueOf("REPLACE_EXISTING"));
    } catch(IOException e){System.err.println("Copying failed " + e);}
  }
  
  /**
  * Creates an empty file directory at the given location.
  *
  * Deletes everything within the directory if something exists in its place. Be careful with this power.
  *
  * @param filename the directory to create.
  *
  * @return true if creation was a success; false if something went wrong
  */
  public static final boolean createDir(String filename) {
    File fi = new File(filename);
    if (fi.exists()) {
      for (File f : fi.listFiles()) {
        if (!delete(f)) return false;
      }
      return true;
    }
    else {return fi.mkdirs();}
  }
  
  /**
   * Checks if a file or directory exists at a given path.
   * 
   * @param filename the location of a file or directory
   * 
   * @return {@code true} if the destination exists
   */
  public static final boolean exists(String filename) {return new File(filename).exists();}
  
  /**
   * Deletes a given {@code File} from the local drive.
   * This can be a path to a directory or an individual file.
   * <p>
   * In the case that the given {@code File} is a directory, 
   * all children of the directory will be deleted as well.
   * 
   * @param f the {@code File} path to delete
   * 
   * @return {@code true} if the file or directory was successfully deleted
   */
  public static boolean delete(File f) {
    if (f.isDirectory()) {
      for (File fi : f.listFiles()) {
        FileIO.delete(fi);
      }
    }
    return f.delete();
  }
  
  /**
   * Reads the lines of a file into a {@code List}. 
   * <p>
   * Each element of the {@code List} consists of a {@code String} representation of a line from the file
   * 
   * @param filename the location of the file to attempt to read
   * @param inJar whether or not this file is located within the same jar file as the running code
   * 
   * @return A {@code List} containing the lines of the file, or an empty {@code List} if the file was not found
   */
  public static final List<String> readAllLines(String filename, boolean inJar) {
    try {
      if (!inJar) return Files.readAllLines(Paths.get(filename));

      BufferedReader file = new BufferedReader(new InputStreamReader(FileIO.class.getResourceAsStream(filename)));
      List<String> allLines = new ArrayList<String>();
      String line;
      while ((line = file.readLine()) != null) {
        allLines.add(line);
      }
      return allLines;
    } catch(Exception e){System.err.println("Reading failed: " + e);}

    return new ArrayList<String>();
  }
  
  /**
   * Reads an image from a file into a {@code BufferedImage}.
   * 
  * @param filename The path of the texture file desired
  *
  * @return a buffered image of the desired texture
  */
  public static final BufferedImage readImage(String filename) {
    filename = "/data/textures/" + filename;
    try {
      return ImageIO.read(FileIO.class.getResourceAsStream(filename));
    }catch(Exception e){
      System.err.println("Failed to find Texture at \"" + filename + "\"! Trying \".." + filename + "\"...");
      try {
        return ImageIO.read(new File(".."+filename));
      }catch(Exception er){
        System.err.println("Failed to find Texture at \".." + filename + "\"!");
      }
    }
    return null;
  }
  
  /**
   * Reads an image from a file into an {@code int} array.
   * 
  * @param filename The path of the texture file desired
  *
  * @return an image in RGBA array format
  */
  public static final int[] readImageInt(String filename) {
    BufferedImage img = FileIO.readImage(filename);
    
    return img.getRGB(0, 0, img.getWidth(), img.getHeight(), null, 0, img.getWidth());
  }

  /**
   * Writes a {@code BufferedImage} out to a {@code png} file
   * 
   * @param filename the name to store the image under
   * @param img the {@code BufferedImage} to write out
   */
  public static final void writeImage(String filename, BufferedImage img) {
    try {
      ImageIO.write(img, "PNG", new File(filename));
    } catch (IOException e) {System.err.println("Failed to write image to " + filename);}
  }

  /**
   * If supported by the user's {@code Operating System}, 
   * this method will attempt to open the given file path using the {@code OS}'s default application for the given file
   * 
   * @param filename the path to the desired file
   * 
   * @return {@code true} if the file was opened successfully
   */
  public static final boolean openProgram(String filename) {
    if (!Desktop.isDesktopSupported()) return false;
    try {
      Desktop.getDesktop().open(new File(filename));
      return true;
    } catch (IOException e) {System.err.println("Failed to open file at " + filename);}
    return false;
  }
}
