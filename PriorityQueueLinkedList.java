// 8 Puzzle A* Search Algorithm
// Sonia Kopel

import java.util.ArrayList;

public class PriorityQueueLinkedList<T extends Comparable<T>> extends PriorityQueue<T>{

	ArrayList<Comparable <T>> list;

	PriorityQueueLinkedList(){
		super();
		this.list = new ArrayList<Comparable<T>>();
	}

	
	public T PriorityDequeue(){
		Comparable <T> ret = this.list.get(0);
		this.list.remove(0);

		return (T) ret;
	}
	
	//public void PriorityEnqueue(Comparable<T> item){
		// You must implement this...
	//}

	public void PriorityEnqueue(Comparable item){
		boolean notFound = true;
		if(list.isEmpty()){
			this.list.add(item);
		}
		else{
			int idx = 0;

			//Find idx at which to insert
			while(notFound && idx<this.list.size()){
				if( item.compareTo(this.list.get(idx)) > 0){
					idx++;
				}
				else{
					notFound = false;
				}}

			if(idx == this.list.size()){
				list.add(item);
			}
			else {
				list.add(idx,item);
			}
		}

	}

	public void Print(){
		for (int i=0; i<list.size();i++){
			System.out.print(list.get(i));
		}
		System.out.println();
	}
	
}
