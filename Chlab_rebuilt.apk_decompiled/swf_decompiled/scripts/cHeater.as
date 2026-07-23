package
{
   public class cHeater
   {
       
      
      public var cName:String;
      
      public var cNumber:Number;
      
      public var cType:String;
      
      public var cLife:Boolean;
      
      public var cKind:String;
      
      public function cHeater(inType:*, inDm:*)
      {
         this.cName = new String();
         this.cNumber = new Number();
         this.cType = new String();
         super();
         this.cType = inType;
         this.cLife = true;
      }
      
      public function cExist() : *
      {
      }
   }
}
