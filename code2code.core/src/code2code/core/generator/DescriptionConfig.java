//package code2code.core.generator;
//
//import org.eclipse.core.resources.IFile;
//
//import code2code.core.templateengine.TemplateEngineFactory;
//import code2code.utils.FileUtils;
//
//class DescriptionConfig
//{
//   /** the description */
//   private String description;
//
//   DescriptionConfig(OldGenerator generator, GlobalParamsConfig globalParamsConfig) throws Exception
//   {
//      IFile file = TemplateEngineFactory.findKnownTemplate(generator.getGeneratorFolder(), "description");
//      if (file == null)
//      {
//         description = null;
//      }
//      else
//      {
//         description = TemplateEngineFactory.forFile(file).processString(generator, FileUtils.toString(file.getContents()),
//            globalParamsConfig.getProcessedParams());
//      }
//   }
//
//   /**
//    * @return
//    *   the description
//    */
//   String getDescription()
//   {
//      return description;
//   }
//}
