/********************************************************************/
/* (c) Elias Jordan 2015                                            */
/********************************************************************/

package code2code.core.generator;

import java.io.File;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Set;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.FileFilterUtils;

import code2code.core.templateengine.TemplateEngineFactory;

public class GeneratorLocator
{
   /** the engine factory */
   private final TemplateEngineFactory m_engines;

   /**
    * Constructor
    * @param p_engines
    */
   public GeneratorLocator(TemplateEngineFactory p_engines)
   {
      m_engines = p_engines;
   }

   /**
    * Find all generators define din the specified directory 
    * @param p_root
    * @return
    *   all generators that were found
    * @throws Exception
    */
   public Set<Generator> findGenerators(File p_root) throws Exception
   {
      Set<Generator> generators = new LinkedHashSet<Generator>();
      Iterator<File> files = FileUtils.iterateFilesAndDirs(p_root, FileFilterUtils.trueFileFilter(), FileFilterUtils.trueFileFilter());
      while (files.hasNext())
      {
         File file = files.next();
         if (file.isDirectory() && isGeneratorFolder(file))
         {
            GeneratorTransformer transformer = new GeneratorTransformer(m_engines, p_root);
            Generator generator = transformer.create(file);
            generators.add(generator);
         }
      }
      
      return generators;
   }

   private boolean isGeneratorFolder(File p_folder)
   {
      return p_folder.getName().matches(".+\\.generator$") && 
             m_engines.findKnownTemplate(p_folder, "templates") != null;
   }
}
