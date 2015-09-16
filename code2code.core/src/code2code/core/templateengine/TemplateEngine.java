package code2code.core.templateengine;

import java.io.File;
import java.io.Reader;
import java.io.Writer;
import java.util.Map;

/**
 * Interface for the engines that apply substitutions for the templates
 */
public interface TemplateEngine
{
   /**
    * Find a file with the specified path, and name that can be handled by 
    * the template engine
    * @param p_folder
    * @param p_fileName
    * @return
    *   the file
    */
   File getKnownFile(File p_folder, String p_fileName);

   /**
    * @param p_fileName
    * @return
    *   true if the specified file is recognised by this engine
    */
   boolean canProcess(String p_fileName);

   /**
    * Apply the templating process to the values within a map
    * @param p_generator
    *   the source generator
    * @param p_process
    *   the map to process
    * @param p_initialContext
    *   the initial substitution context
    * @return
    *   the result of processing the map
    * @throws Exception
    */
   Map<String, String> processMap(Map<String, String> p_process, Map<String, Object> p_initialContext) throws Exception;

   /**
    * Apply the templating engine to the given string
    * @param p_generator
    *   the source generator
    * @param p_content
    *   the string to perform substitutions to
    * @param p_context
    *   the substitution context
    * @return
    *   the processed string
    * @throws Exception
    */
   String processString(String p_content, Map<String, Object> p_context) throws Exception;
   
   void process(String p_name, Reader p_reader, Writer p_writer, Map<String, Object> p_context) throws Exception;

   /**
    * Apply the templating engine to the specified template file
    * @param p_generatorRoot
    *   the directory that defiens the generator
    * @param p_templateFile
    *   the name of the template file
    * @param p_context
    *   the substitution context
    * @return
    *   the result of instantiating the template
    * @throws Exception
    */
   String processTemplate(File p_generatorRoot, String p_templateFile, Map<String, Object> p_context) throws Exception;

   /**
    * @param p_extension
    * @return
    *   true if the file extension is recognised by this engine, otherwise false
    */
   boolean knowExtension(String p_extension);

   /**
    * Escape data for this engine
    * @param contents
    * @return
    *   the escaped data
    */
   String escape(String contents);
}