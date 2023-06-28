
public class ComplexNumber
{
    /**
     * The real, Re(z), part of the <code>ComplexNumber</code>.
     */
    private double real;
    /**
     * The imaginary, Im(z), part of the <code>ComplexNumber</code>.
     */
    private double imaginary;

    /**
     * Constructs a new <code>ComplexNumber</code> object with both real and imaginary parts 0 (z = 0 + 0i).
     */
    public ComplexNumber()
    {
        real = 0.0;
        imaginary = 0.0;
    }

    /**
     * Constructs a new <code>ComplexNumber</code> object.
     * @param real the real part, Re(z), of the complex number
     * @param imaginary the imaginary part, Im(z), of the complex number
     */
    public ComplexNumber(double real, double imaginary)
    {
        this.real = real;
        this.imaginary = imaginary;
    }

    /**
     * Sets the value of current complex number to the passed complex number.
     * @param z the complex number
     */
    public void set(ComplexNumber z)
    {
        this.real = z.real;
        this.imaginary = z.imaginary;
    }

    /**
     * The real part of <code>ComplexNumber</code>
     * @return the real part of the complex number
     */
    public double getRe()
    {
        return this.real;
    }

    /**
     * The imaginary part of <code>ComplexNumber</code>
     * @return the imaginary part of the complex number
     */
    public double getIm()
    {
        return this.imaginary;
    }

    /**
     * The modulus, magnitude or the absolute value of current complex number.
     * @return the magnitude or modulus of current complex number
     */
    public double mod()
    {
        return Math.sqrt(Math.pow(this.real,2) + Math.pow(this.imaginary,2));
    }
    
    public ComplexNumber scale(double s)
    {
        return new ComplexNumber(this.getRe()*s, this.getIm()*s);
    }
    
    public ComplexNumber conjugate() {
    	return new ComplexNumber(this.getRe(),-this.getIm());
    }
    
    public ComplexNumber multiply(ComplexNumber otherC) {
        return new ComplexNumber(this.getRe() * otherC.getRe() - this.getIm() * otherC.getIm(),  this.getRe() * otherC.getIm() + this.getIm() * otherC.getRe());
    }
    
    public ComplexNumber sum(ComplexNumber thatNo) {
    	return new ComplexNumber(this.getRe() + thatNo.getRe(),  this.getIm() + thatNo.getIm());
    }
    
    public ComplexNumber minus(ComplexNumber thatNo) {
    	return new ComplexNumber(this.getRe() - thatNo.getRe(),  this.getIm() - thatNo.getIm());
    }
    
    public ComplexNumber division(ComplexNumber otherC) {
    	double real = (this.getRe() * otherC .getRe() + this.getIm() * otherC.getIm()) / (Math.pow(otherC.getRe(),2) +Math.pow(otherC.getIm(),2));
    	double imaginary = (this.getIm() * otherC.getRe() - this.getRe() * otherC.getIm()) / (Math.pow(otherC.getRe(),2) +Math.pow(otherC.getIm(),2));
        return new ComplexNumber(real, imaginary);
    }
    
    public ComplexNumber exponential() {
    	return new ComplexNumber(Math.exp(this.getRe()) * Math.cos(this.getIm()),  Math.exp(this.getRe()) * Math.sin(this.getIm()));
    }

    /**
     * @return the complex number in x + yi format
     */
    @Override
    public String toString()
    {
        String re = this.real+"";
        String im = "";
        if(this.imaginary < 0)
            im = this.imaginary+"i";
        else
            im = "+"+this.imaginary+"i";
        return re+im;
    }

    /**
     * Checks if the passed <code>ComplexNumber</code> is equal to the current.
     * @param z the complex number to be checked
     * @return true if they are equal, false otherwise
     */
    @Override
    public final boolean equals(Object z)
    {
        if (!(z instanceof ComplexNumber))
            return false;
        ComplexNumber a = (ComplexNumber) z;
        return (real == a.real) && (imaginary == a.imaginary);
    }

    // TODO
    // Fill in the operations between complex numbers used for DFT/IDFT/FFT/IFFT.
}