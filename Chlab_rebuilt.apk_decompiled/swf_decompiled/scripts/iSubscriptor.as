package
{
   public class iSubscriptor
   {
       
      
      public function iSubscriptor()
      {
         super();
      }
      
      public static function iSub(st:String) : String
      {
         var t:String = st;
         t = String(strReplace(t,"1","₁"));
         t = String(strReplace(t,"2","₂"));
         t = String(strReplace(t,"3","₃"));
         t = String(strReplace(t,"4","₄"));
         t = String(strReplace(t,"5","₅"));
         t = String(strReplace(t,"6","₆"));
         t = String(strReplace(t,"7","₇"));
         t = String(strReplace(t,"8","₈"));
         t = String(strReplace(t,"9","₉"));
         return String(strReplace(t,"0","₀"));
      }
      
      private static function strReplace(str:String, search:String, replace:String) : String
      {
         return str.split(search).join(replace);
      }
   }
}
