package code2code.ui.utils;

import java.io.IOException;

import org.eclipse.ui.console.ConsolePlugin;
import org.eclipse.ui.console.IConsole;
import org.eclipse.ui.console.IConsoleManager;
import org.eclipse.ui.console.MessageConsole;
import org.eclipse.ui.console.MessageConsoleStream;

/**
 * Utility to output test to the console
 */
public class Console
{
   /** the constant name of the console used to write the result of the template instantation to */
   private static final String CONSOLE_NAME = "code2code.console";

   /**
    * @return
    *   the console to use for output
    */
   private static MessageConsole getConsole()
   {
      ConsolePlugin plugin = ConsolePlugin.getDefault();
      IConsoleManager manager = plugin.getConsoleManager();
      IConsole[] existing = manager.getConsoles();
      for (int i = 0; i < existing.length; i++)
      {
         if (CONSOLE_NAME.equals(existing[i].getName()))
         {
            return (MessageConsole) existing[i];
         }
      }
      MessageConsole myConsole = new MessageConsole(CONSOLE_NAME, null);
      manager.addConsoles(new IConsole[] {myConsole});
      return myConsole;
   }

   /**
    * Write the specified text to the console
    * @param p_text
    * @throws IOException
    */
   public static void write(String p_text) throws IOException
   {
      MessageConsole console = getConsole();
      ConsolePlugin.getDefault().getConsoleManager().showConsoleView(console);
      MessageConsoleStream out = console.newMessageStream();
      try
      {
         out.println(p_text);
      }
      finally
      {
         out.flush();
         out.close();
      }
   }

   /**
    * Used by tests
    * @return
    *   the text currently on the console
    */
   public static String getText()
   {
      return getConsole().getDocument().get();
   }
}