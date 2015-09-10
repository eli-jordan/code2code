package utils;

import org.eclipse.swtbot.eclipse.finder.SWTWorkbenchBot;

public class Driver
{
   private static SWTWorkbenchBot bot;

   public static SWTWorkbenchBot bot()
   {
      if (bot == null)
      {
         bot = new SWTWorkbenchBot();
      }
      return bot;
   }

}
