package
{
   import flash.display.*;
   
   [Embed(source="/_assets/assets.swf", symbol="iNS1")]
   public class iNS1 extends iNumberStepper
   {
       
      
      public var iNbar:Sprite;
      
      public function iNS1()
      {
         this.iNbar = new Sprite();
         super();
         init();
      }
   }
}
