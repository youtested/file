package
{
   public class cStructure
   {
       
      
      public var cName:String;
      
      public var cNumber:Number;
      
      public var cType:String;
      
      public var cLife:Boolean = true;
      
      public var cKind:String;
      
      public function cStructure(inType:*, inDm:*)
      {
         this.cName = new String();
         this.cNumber = new Number();
         this.cType = new String();
         super();
         this.cType = inType;
      }
      
      public function cExist() : *
      {
      }
   }
}
