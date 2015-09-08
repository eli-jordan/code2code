package code2code.core.templateengine;

import java.util.Map;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;

import code2code.core.generator.Generator;

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
   IFile getKnownFile(IFolder p_folder, String p_fileName);

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
   Map<String, String> processMap(Generator p_generator, Map<String, String> p_process, Map<String, String> p_initialContext) throws Exception;

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
   String processString(Generator p_generator, String p_content, Map<String, String> p_context) throws Exception;

   /**
    * Apply the templating engine to the specified template file
    * @param p_generator
    *   the source generator
    * @param p_templateFile
    *   the name of the template file
    * @param p_context
    *   the substitution context
    * @return
    *   the result of instantiating the template
    * @throws Exception
    */
   String processTemplate(Generator p_generator, String p_templateFile, Map<String, String> p_context) throws Exception;

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