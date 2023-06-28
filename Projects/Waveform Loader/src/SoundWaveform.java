// DO NOT DISTRIBUTE THIS FILE TO STUDENTS
import ecs100.UI;
import ecs100.UIFileChooser;

import javax.sound.sampled.*;
import java.awt.*;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

/*
  getAudioInputStream
  -> getframelength,
  -> read into byteArray of 2x that many bytes
  -> convert to array of doubles in reversed pairs of bytes (signed)
  -> scale #FFFF to +/- 300

  array of doubles
   -> unscale  +/- 300  to #FFFF (
   -> convert to array of bytes (pairs little endian, signed)
   -> convert to inputStream
   -> convert to AudioInputStream
   -> write to file.
 */

public class SoundWaveform{

    public static final double MAX_VALUE = 300;
    public static final int SAMPLE_RATE = 44100;
    public static final int MAX_SAMPLES = SAMPLE_RATE/100;   // samples in 1/100 sec

    public static final int GRAPH_LEFT = 10;
    public static final int ZERO_LINE = 310;
    public static final int X_STEP = 2;            //pixels between samples
    public static final int GRAPH_WIDTH = MAX_SAMPLES*X_STEP;

    private ArrayList<Double> waveform = new ArrayList<Double>();   // the displayed waveform
    private ArrayList<ComplexNumber> spectrum = new ArrayList<ComplexNumber>(); // the spectrum: length/mod of each X(k)
    private boolean test  = false;
    private boolean spectrumIsDisplayed = false;
    private boolean change = false;

    /**
     * Displays the waveform.
     */
    public void displayWaveform(){
    	if (this.waveform == null) {
    		 UI.println("No waveform to display");
             return;
    	}
        if (this.waveform.isEmpty()){ //there is no data to display
            UI.println("No waveform to display");
            return;
        }
        
        spectrumIsDisplayed =  false;
        UI.clearText();
        UI.println("Printing, please wait...");

        UI.clearGraphics();

        // draw x axis (showing where the value 0 will be)
        UI.setColor(Color.black);
        UI.drawLine(GRAPH_LEFT, ZERO_LINE, GRAPH_LEFT + GRAPH_WIDTH , ZERO_LINE);

        // plot points: blue line between each pair of values
        UI.setColor(Color.blue);

        double x = GRAPH_LEFT;
        for (int i=1; i<this.waveform.size(); i++){
            double y1 = ZERO_LINE - this.waveform.get(i-1);
            double y2 = ZERO_LINE - this.waveform.get(i);
            if (i>MAX_SAMPLES){UI.setColor(Color.red);}
            UI.drawLine(x, y1, x+X_STEP, y2);
            x = x + X_STEP;
        }

        UI.println("Printing completed!");
    }

    /**
     * Displays the spectrum. Scale to the range of +/- 300.
     */
    public void displaySpectrum() {
    	if (this.spectrum == null) {
   		 UI.println("No spectrum to display");
            return;
     	}
        if (this.spectrum.isEmpty()){ //there is no data to display
            UI.println("No spectrum to display");
            return;
        }
        
        spectrumIsDisplayed = true;
        UI.clearText();
        UI.println("Printing, please wait...");

        UI.clearGraphics();

        // calculate the mode of each element
        ArrayList<Double> spectrumMod = new ArrayList<Double>();
        double max = 0;
        for (int i = 0; i < spectrum.size(); i++) {
            if (i == MAX_SAMPLES)
                break;

            double value = spectrum.get(i).mod();
            max = Math.max(max, value);
            spectrumMod.add(spectrum.get(i).mod());
        }

        double scaling = 300/max;
        for (int i = 0; i < spectrumMod.size(); i++) {
            spectrumMod.set(i, spectrumMod.get(i)*scaling);
        }

        // draw x axis (showing where the value 0 will be)
        UI.setColor(Color.black);
        UI.drawLine(GRAPH_LEFT, ZERO_LINE, GRAPH_LEFT + GRAPH_WIDTH , ZERO_LINE);

        // plot points: blue line between each pair of values
        UI.setColor(Color.blue);

        double x = GRAPH_LEFT;
        for (int i=1; i<spectrumMod.size(); i++){
            double y1 = ZERO_LINE;
            double y2 = ZERO_LINE - spectrumMod.get(i);
            if (i>MAX_SAMPLES){UI.setColor(Color.red);}
            UI.drawLine(x, y1, x+X_STEP, y2);
            x = x + X_STEP;
        }

        UI.println("Printing completed!");
        if (change)UI.println("Modifying spectrum...");
    }
    
    public void case1() {
    	this.test = true;
    	if (this.waveform != null) this.waveform.clear();
    	if(this.spectrum != null) this.spectrum.clear();
    	for (double i = 1; i < 9; i++) {
    		this.waveform.add(i);
    	}
    	UI.println(this.waveform);
    }
    
    public void case2() {
    	this.test = true;
    	this.spectrum.clear();
    	this.waveform.clear();
    	for (double i = 0; i < 8; i++) {
    		if (i % 2 == 0) waveform.add(1.0);
    		else this.waveform.add(2.0);
    	}
        UI.println(this.waveform);
    }
    
    public void case3() {
    	this.test = true;
    	this.waveform.clear();
    	this.spectrum.clear();
    	for (int m = 0; m < 8; m++) {
    		this.waveform.add(null);
    	}
    	for (double i = 1, l = 4, ind = 0; ind < 4; ind++, i++, l--) {
    		this.waveform.set((int)ind,i);
    		this.waveform.set((int)ind+4,l);
    	}
    	UI.println(this.waveform);
    }
    

    public void dft() {
    	if (this.waveform == null) {
    		UI.println("Waveform array is empty!");
        	return;
    	}
    	if (this.waveform.isEmpty()) {
        	UI.println("Waveform array is empty!");
        	return;
        }
        if (!this.test)UI.clearText();
        UI.println("DFT in process, please wait...");

        
        ArrayList<ComplexNumber> tempWaveForm = cut(this.convertToComplex(this.waveform));
        int N = tempWaveForm.size();
        for (int k = 0 ; k < N; k++) {
        	ComplexNumber sum = new ComplexNumber();
        	for (int n = 0; n < N; n ++) {
                double ktn = -2 * k * n *  Math.PI / N;
                ComplexNumber wkn = new ComplexNumber(Math.cos(ktn), Math.sin(ktn));
        	    ComplexNumber current = tempWaveForm.get(n).multiply(wkn);
        	 	sum = sum.sum(current);
        	}
        	this.spectrum.add(sum);
        }

        UI.println("DFT completed!");
        if (this.test) {
        UI.println(this.spectrum);
        }
        this.waveform.clear();
    }
    
 
    public void idft() {
    	if (this.spectrum == null) {
    		UI.println("Spectrum array is empty!");
        	return;
    	}
    	if (this.spectrum.isEmpty()) {
        	UI.println("Spectrum array is empty!");
        	return;
        }
    	 if (!this.test)UI.clearText();
        UI.println("IDFT in process, please wait...");
        
        ArrayList<ComplexNumber> tempWaveForm = cut(this.spectrum);
        int N = tempWaveForm.size();
        for (int n = 0 ; n < N; n++) {
        	ComplexNumber sum = new ComplexNumber();
        	for (int k = 0; k < N; k ++) {
                double ktn = 2 * k * n *  Math.PI / N;
                ComplexNumber wkn = new ComplexNumber(Math.cos(ktn), Math.sin(ktn));
        	    ComplexNumber current = tempWaveForm.get(k).multiply(wkn);
        	 	sum = sum.sum(current);
        	}
        	this.waveform.add(sum.getRe()/N);	
        }
       
       UI.println("IDFT completed!");
       if (this.test) {
             UI.println(this.waveform);
        }
        this.spectrum.clear();
    }

    public void fft() {
    	if (this.waveform == null) {
    		UI.println("Waveform array is empty!");
        	return;
    	}
    	if (this.waveform.isEmpty()) {
        	UI.println("Waveform array is empty!");
        	return;
        }
    	if (!test)UI.clearText();
        UI.println("FFT in process, please wait...");

        
        ArrayList<ComplexNumber> tempWaveForm = this.convertToComplex(this.waveform);
        tempWaveForm = cut(tempWaveForm);
		this.spectrum = fft1(tempWaveForm);

        UI.println("FFT completed!");
        if (this.test) {
            UI.println(spectrum);
            }
        this.waveform.clear();
    }
    
    public ArrayList<ComplexNumber> fft1(ArrayList<ComplexNumber> wf){
    	int N = wf.size();
    	if (N == 1) {
    		return  wf;
        }else {
    	if (!isPof2(N))  throw new IllegalArgumentException("n is not a power of 2");
    	
    	ArrayList<ComplexNumber> xeven = new ArrayList<ComplexNumber>();
    	for (int i = 0; i < N/2; i++) {
            xeven.add(wf.get(2*i));
        }
    	ArrayList<ComplexNumber> Xeven = fft1(xeven);
    	
    	ArrayList<ComplexNumber> xodd = new  ArrayList<ComplexNumber>();
    	 for (int i = 0; i < N/2; i++) {
             xodd.add(wf.get(2*i+1));
         }
    	ArrayList<ComplexNumber> Xodd = fft1(xodd);
    	
    	ArrayList<ComplexNumber> X = new ArrayList<ComplexNumber>();
    	for (int m = 0; m < N; m++) {
    		X.add(null);
    	}
    	for(int k = 0; k < N/2 ; k++) {
    		double kth =  -2 * k * Math.PI / N;
    		ComplexNumber wk = new ComplexNumber(Math.cos(kth), Math.sin(kth));
    		X.set(k, Xeven.get(k).sum(Xodd.get(k).multiply(wk))) ;
    		X.set(k + N/2, Xeven.get(k).minus(Xodd.get(k).multiply(wk))) ;
    	}
		return X;
        }
    	
    }  
    
    public void ifft() {
    	if (this.spectrum == null) {
    		UI.println("Spectrum array is empty!");
        	return;
    	}
    	if (this.spectrum.isEmpty()) {
        	UI.println("Spectrum array is empty!");
        	return;
        }
    	if (!this.test)UI.clearText();
        UI.println("IFFT in process, please wait...");

        if (this.spectrum.isEmpty()) {
        	 UI.println("IFFT completed!");
        	return;
        }
        int N = spectrum.size();
        ArrayList<ComplexNumber> y = new ArrayList<ComplexNumber>();

        for (int i = 0; i < N; i++) {
            y.add(this.spectrum.get(i).conjugate());
        }

        ArrayList<ComplexNumber> tempArray = cut(y);
        y = ifft1(tempArray);

        for (int i = 0; i < N; i++) {
        	ComplexNumber temp = y.get(i);
            y.set(i, temp.conjugate());
        }

        for (int i = 0; i < N; i++) {
            this.waveform.add(y.get(i).getRe()/N);
        }
        
        UI.println("IFFT completed!");
        if (this.test) {
            UI.println(this.waveform);
        }
        this.spectrum.clear();
    }
    
    public ArrayList<ComplexNumber> ifft1(ArrayList<ComplexNumber> spec){
    	int N = spec.size();
    	if (N == 1) {
    		return spec;
        }else {
    	if (!isPof2(N))  throw new IllegalArgumentException("n is not a power of 2");
    	
    	ArrayList<Double> K = new ArrayList<Double>();
    	for(int k = 0; k < N ; k++) {
    		K.add(-2 * k *  Math.PI / N);
    	}
    	
    	ArrayList<ComplexNumber> xeven = new ArrayList<ComplexNumber>();
    	for (int i = 0; i < N/2; i++) {
            xeven.add(spec.get(2*i));
        }
    	ArrayList<ComplexNumber> Xeven = ifft1(xeven);
    	
    	ArrayList<ComplexNumber> xodd = new  ArrayList<ComplexNumber>();
    	 for (int i = 0; i < N/2; i++) {
             xodd.add(spec.get(2*i+1));
         }
    	ArrayList<ComplexNumber> Xodd = ifft1(xodd);
    	
    	ArrayList<ComplexNumber> X = new ArrayList<ComplexNumber>();
    	for (int m = 0; m < N; m++) {
    		X.add(null);
    	}
    	for(int k = 0; k < N/2 ; k++) {
    		double kth =  -2 * k * Math.PI / N;
    		ComplexNumber wk = new ComplexNumber(Math.cos(kth), Math.sin(kth));
    		X.set(k, Xeven.get(k).sum(Xodd.get(k).multiply(wk))) ;
    		X.set(k + N/2, Xeven.get(k).minus(Xodd.get(k).multiply(wk))) ;
    	}
		return X;
        }
    	
    }
    
    public void doMouse(String action, double x, double y) {
    	if (y > ZERO_LINE) return;
    	double xClick = -GRAPH_LEFT/2 + x/2;
    	if (xClick < 0 || xClick > spectrum.size()) return;
    	double yClick = ZERO_LINE - y;
    	if (action.equals("released") && spectrumIsDisplayed && !spectrum.isEmpty()) {
    		ArrayList<Double> spectrumMod = new ArrayList<Double>();
            double max = 0;
            for (int i = 0; i < spectrum.size(); i++) {
                if (i == MAX_SAMPLES)
                    break;

                double value = spectrum.get(i).mod();
                max = Math.max(max, value);
                spectrumMod.add(spectrum.get(i).mod());
            }

            double scaling = 300/max;
            if (change) {	
            	ComplexNumber tempCNo = spectrum.get((int)xClick);	
            	double ratio = (yClick/scaling)/tempCNo.mod();
                ComplexNumber temp = tempCNo.scale(ratio);
            	spectrum.set((int)xClick, temp);
            	displaySpectrum();
            }
            else {
            	UI.clearText();
            	UI.println("Complex from: " + spectrum.get((int)xClick));
            	UI.println("Magnitude: " + spectrumMod.get((int)xClick));
            }
    	}
    	
    }

    
    public ArrayList<ComplexNumber> cut(ArrayList<ComplexNumber> ar){
    	int n = ar.size();
        while (!isPof2(n)) {
        	ar.remove(n-1);
        	n--;
        }
        return ar;
    }
    

	public static boolean isPof2(int n){
    	if(n==0)
    	    return false;
    	else   return (int)(Math.ceil((Math.log(n) / Math.log(2)))) ==
    	       (int)(Math.floor(((Math.log(n) / Math.log(2)))));
    }
    
    public void change() {
    	if (change) {
    		UI.println("Modification off.");
    		change = false;
    	}
    	else {
    		change = true;
    		UI.println("Modifying spectrum...");
    	}
    }
    
    public ArrayList<ComplexNumber> convertToComplex(ArrayList<Double> input) {
    	ArrayList<ComplexNumber> temp = new ArrayList<ComplexNumber>();
    	for (Double i : input) {
    		temp.add(new ComplexNumber(i,0));
    	}
    	return temp;
    }

    /**
     * Save the wave form to a WAV file
     */
    public void doSave() {
        WaveformLoader.doSave(waveform, WaveformLoader.scalingForSavingFile);
    }

    /**
     * Load the WAV file.
     */
    public void doLoad() {
        UI.clearText();
        UI.println("Loading...");

        this.waveform = WaveformLoader.doLoad();
        this.spectrum.clear();

        this.displayWaveform();
        this.test = false;
        UI.println("Loading completed!");
    }

    public static void main(String[] args){
        SoundWaveform wfm = new SoundWaveform();
        //core
        UI.addButton("Display Waveform", wfm::displayWaveform);
        UI.addButton("Display Spectrum", wfm::displaySpectrum);
        UI.addButton("Case 1", wfm::case1);
        UI.addButton("Case 2", wfm::case2);
        UI.addButton("Case 3", wfm::case3);
        UI.addButton("DFT", wfm::dft);
        UI.addButton("IDFT", wfm::idft);
        UI.addButton("FFT", wfm::fft);
        UI.addButton("IFFT", wfm::ifft);
        UI.addButton("Modify spectrum", wfm::change);
        UI.addButton("Save", wfm::doSave);
        UI.addButton("Load", wfm::doLoad);
        UI.addButton("Quit", UI::quit);
        UI.setMouseMotionListener(wfm::doMouse);
        UI.setWindowSize(950, 630);
    }
}
