
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import java.util.stream.Stream;
import java.util.Arrays;
import java.util.Set;

import java.io.File;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class Main{
	public static ArrayList<File> fajlovi = new ArrayList<>();;
	
	public static ArrayList<File> fajloviSaSadrzajem = new ArrayList<>();
	
	public static Map<String,Long> brojPojavljivanja = new HashMap<>();
	
	public static String pocetnaPutanja;
	public static String kljucnaRijec;
	public static String ekstenzija;
	
	public static void main(String[] args) throws InterruptedException{
		pocetnaPutanja=args[0];
		kljucnaRijec=args[1];
		ekstenzija="."+args[2];
		
		FileCheck root=new FileCheck(pocetnaPutanja);
		root.start();
	
		root.join();
		
		fajlovi.stream().forEach(elem->{
			try(BufferedReader br = new BufferedReader(new FileReader(elem))){
				Stream<String> stream = br.lines();
				long result= stream.flatMap(e -> Arrays.asList(e.split(" "))
					.stream())
					.filter(e -> {
						return e.trim().contains(kljucnaRijec);
					})
					.count();
				if(result!= 0)
					brojPojavljivanja.put(elem.getPath(),result);
			}catch(IOException ex){
				ex.printStackTrace();
			}
		});
		Set<String> keys= brojPojavljivanja.keySet();
		for(String key : keys){
			System.out.println(key+ ":      "+brojPojavljivanja.get(key) );
		}
		
	}
}


class FileCheck extends Thread{
	File f;
	
	public FileCheck(String path){
		f= new File(path);
	}
	
	private ArrayList<FileCheck> newThreads= new ArrayList<>();
	
	@Override
	public void run(){
		File[] filesInDir= f.listFiles();
		for(File fl : filesInDir){
			if(fl.isDirectory()){
				newThreads.add(new FileCheck(fl.getPath()));
				newThreads.get(newThreads.size()-1).start();
			}
			else{
				String name= fl.getPath();
				if(name.endsWith(Main.ekstenzija)){
					Main.fajlovi.add(fl);
				}
			}
		}
		newThreads.stream().forEach(e -> 
					{
						try{
							e.join();
						}catch(InterruptedException ex){
							ex.printStackTrace();
						}
					});
	}
}










