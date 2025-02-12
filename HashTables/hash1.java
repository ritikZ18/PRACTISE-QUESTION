
import java.util.Hashtable;

public class hash1{

    public static void main(String[] args) {
        Hashtable<String, String>  table = new Hashtable<>(10) ;


table.put("100", "Ritik");
table.put("101", "Swami");
table.put("102", "Trump");
table.put("103", "Modi");
table.put("106", "Nirmala");
table.put("103", "Gadkari");
// table.remove("103");

// for(Integer items : table.keySet()){
//     System.out.println( items.hashCode()%10 + " " +items + "\t" + table.get(items));

// }
for(String items : table.keySet()){
    System.out.println( items.hashCode() % 10+ " " +items + "\t" + table.get(items));

}




    }



}