package eth.infsys.group1.task4;

import java.util.ArrayList;
import java.util.List;

import javafx.util.Pair;

@SuppressWarnings("restriction")
public class My_timer {
	
	private List<Pair<Long,Long>> timer = new ArrayList<>();
	private int runs = 0;
	
	//Flags
	private Boolean start_expected = false;
	private Boolean stop_expected = false;
	
	private long start_time = 0;
	private long stop_time = 0;
	
	private double calc_time(Pair<Long,Long> data){
		double start = data.getKey();
		double stop = data.getValue();
		return (stop-start)/1.e9;
	}
		

	
	My_timer(){
		this.timer = new ArrayList<>();
		this.runs = 0;
		
		this.start_expected = false;
		this.stop_expected = false;
	}
	
	public void reset(){
		this.timer = new ArrayList<>();
		this.runs = 0;
		
		this.start_expected = false;
		this.stop_expected = false;
	}
	
	public void new_run(){
		//checks....
		if ( !start_expected && !stop_expected){
			this.start_expected = true;			
		} else {
			System.out.println("Error: new_run not expected...");
			System.exit(666);
		}
	}
	
	public void start(){
		//checks....
		if ( start_expected ){
			this.start_expected = false;
			this.stop_expected = true;
			this.start_time = System.nanoTime();
		} else {
			System.out.println("Error: start not expected...");
			System.exit(666);
		}
	}
	
	public void stop(){
		//checks....
		if ( stop_expected ){
			this.stop_expected = false;
			this.stop_time = System.nanoTime();
			
			this.runs++;
			timer.add(new Pair<Long,Long>(start_time,stop_time));
			
			this.start_time = 0;
			this.stop_time = 0;
			
		} else {
			System.out.println("Error: stop not expected...");
			System.exit(666);
		}
	}
	
	public int get_runs(){
		return this.runs;
	}
	
	public double get_run_x(int run_x){
		if ( run_x < 1 || this.runs < run_x ){
			System.out.println("Error: get_run_x...");
			System.exit(666);
		}
		return calc_time(timer.get(run_x - 1));
	}
	
	public double get_last_run(){
		if ( this.runs < 1 ){
			System.out.println("Error: no get_last_run()...");
			System.exit(666);
		}
		return get_run_x(this.runs);
	}
	
	public double get_avg_run(){
		if ( this.runs < 1 ){
			System.out.println("Error: no get_avg_run()...");
			System.exit(666);
		}
		
		double total_run_time = 0;
		for (int run = 1; run <= this.runs; run++){
			total_run_time += get_run_x(run);
		}
		return total_run_time/this.runs;
	}
	
	public double get_max_run(){
		if ( this.runs < 1 ){
			System.out.println("Error: no get_max_run()...");
			System.exit(666);
		}
		
		double max_run_time = 0;
		for (int run = 1; run <= this.runs; run++){
			double run_time = get_run_x(run);
			if (run_time > max_run_time){
				max_run_time = run_time;
			}
		}
		return max_run_time;
	}
}
