package code2code.core.utils;import java.io.BufferedReader;import java.io.File;import java.io.FileOutputStream;import java.io.IOException;import java.io.InputStream;import java.io.InputStreamReader;import java.util.LinkedHashMap;import java.util.Map;import java.util.Map.Entry;/** * Utils to help with file processing */public final class FileUtils{   /**    * TODO: Eli: Why is this reading lines? Should we just consume the stream as is?    *     * Create a string from the provided InputStream    * @param p_stream    * @return    *   the created string    * @throws Exception    */   public static String toString(InputStream p_stream) throws Exception   {      BufferedReader reader = new BufferedReader(new InputStreamReader(p_stream));      try      {         StringBuilder result = new StringBuilder();         String line = reader.readLine();         while (line != null)         {            result.append(line);            line = reader.readLine();            if (line != null)            {               result.append(System.getProperty("line.separator"));            }         }         return result.toString();      }      finally      {         reader.close();      }   }   /**    * Load a properties file as a map    * @param p_input    * @return    *   the properties map    * @throws IOException    */   public static Map<String, String> loadProperties(InputStream p_input) throws IOException   {      try      {         Properties paramsProperties = new Properties();         paramsProperties.load(p_input);         Map<String, String> params = new LinkedHashMap<String, String>();         for (Entry<Object, Object> entry : paramsProperties.entrySet())         {            params.put((String) entry.getKey(), (String) entry.getValue());         }         return params;      }      finally      {         p_input.close();      }   }      /**    * Write a string to the specified file    * @param p_contents    * @param p_file    * @throws IOException    */   public static void write(String p_contents, File p_file) throws IOException   {      p_file.getParentFile().mkdirs();      FileOutputStream output = new FileOutputStream(p_file);      try      {         output.write(p_contents.getBytes());      }      finally      {         output.flush();         output.close();      }   }//   /**//    * Create a folder and its parents//    * @param folder//    * @throws CoreException//    *///   public static void createFolderWithParents(IFolder folder) throws CoreException//   {//      if (!folder.getParent().exists())//      {//         IFolder parent = (IFolder) folder.getParent();//         createFolderWithParents(parent);//      }////      if (!folder.exists())//      {//         folder.create(false, true, null);//      }//   }////   /**//    * Create the parent folders for a file//    * @param file//    * @throws Exception//    *///   public static void createParentFolders(IFile file) throws Exception//   {//      IContainer parent = file.getParent();////      if (parent instanceof IFolder)//      {//         IFolder parentFolder = (IFolder) file.getParent();////         if (!parentFolder.exists())//         {//            FileUtils.createFolderWithParents(parentFolder);//         }//      }//   }}