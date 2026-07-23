package
{
   import flash.display.MovieClip;
   import flash.text.TextField;
   
   [Embed(source="/_assets/assets.swf", symbol="iDebug")]
   public class iDebug extends MovieClip
   {
       
      
      public var itxt:TextField;
      
      public function iDebug()
      {
         super();
      }
      
      public static function iTrace(t:String) : void
      {
      }
   }
}
