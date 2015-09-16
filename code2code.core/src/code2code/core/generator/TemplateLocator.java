/********************************************************************/
/* (c) Elias Jordan 2015                                            */
/********************************************************************/

package code2code.core.generator;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;

/**
 * Abstracts the loacating of a template
 */
public class TemplateLocator
{
   /** the root of the generator */
   private final File m_generatorRoot;
   
   /** the template path relative to the generator root */
   private final String m_templatePath;
   
   /**
    * Constructor
    * @param p_generatorRoot
    * @param p_templatePath
    */
   public TemplateLocator(File p_generatorRoot, String p_templatePath)
   {
      m_generatorRoot = p_generatorRoot;
      m_templatePath = p_templatePath;
   }

   /**
    * @return
    *   the contents of the template file as a reader
    * @throws IOException
    */
   public Reader locate() throws IOException
   {
      return new FileReader(new File(m_generatorRoot, m_templatePath));
   }
}

