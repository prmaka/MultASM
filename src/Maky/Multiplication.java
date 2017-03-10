/**
 * Algorithm optimizes ASM code to multiply two int numbers
 * Input arg is N ( multiplier )
 * Allowed to use are next ASM commands:
 *   MOV A, B 	A := B 
 *   ADD A, B 	A := A + B 
 *   SUB A, B 	A := A - B 
 *   SHL A, k 	A := A * (2^k) 
 * A and B are processor registers and it is supposed that multiplicand is 
 * already in register A.
 * 
 * The goal is to build shortest ASM code to multiply two numbers.
 * 
 * Usage:
 * In file M_ulaz.dat write multipliers (one row one multiplier). 
 * 
 * Output:
 * Two files M_izlaz_old.dat and M_izlaz_new.dat
 * Old is procedure with no recursion and code is not always optimized.
 * New is recursion procedure where code is optimized.
 */
package Maky;

import java.util.*;
import java.io.*;
/**
 * @author Predrag Makaj
 * @mail prmaka18@gmail.com
 *
 */
public class Multiplication {
	private static List<String> li = new ArrayList<String>();
	private static List<String> newLi = new ArrayList<String>();
	private static int p = 0;
	private static boolean add_sub = false;
	private static int d0 = 0;

	/**
     * Gives the biggest 2 to n power that is lass then N
     */
	private static int _2naN(int NR) {
    	for(p=0; (Math.pow(2,p) <= NR && p<16); p++) {}
    	p--;
    	return (int) Math.pow(2, p);
    }

    /**
     * Gives the lowest 2 to n power that is bigger then N
     */
    private static int _2naNbigger(int NR) {
    	for(p=16; (Math.pow(2,p) > NR && p>0); p--) {}
    	p++;
    	return (int) Math.pow(2, p);
    }

	/**
     * Gives the biggest 2 to n power that is lass then N
     */
	private static int _2naNclosest(int NR) {
    	int i = _2naN(NR);
    	if (i != NR) {
        	int p0 = p;
        	int j = _2naNbigger(NR);
        	int p1 = p;
        	if ((j - i) / 2 + i >= NR) {
        		p = p0;
        	} else {
        		p = p1;
        	}
    	}
    	return (int) Math.pow(2, p);
    }

    /**
     * Gives the lowest 2 to n power that is bigger then N
     */
    private static void writeLnRepeatedly(String s, int n) {
    	for(int i=0; i < n; i++) {
    		li.add(0, s);
    	}
    }
    
    /**
     * Divide N to get ASM code and saves the code to "li" variable
     */
    private static void devideN_old(int N) {
    	p = 0;
    	while(N > 1) {
    	  if (N % 2 > 0) {
    		if (p > 0) {
    		  li.add(0, "SHL A,"+Integer.toString(p));
    		}
    		li.add(0, "ADD A,B");
    		if (! add_sub) {
    			add_sub = true;
    		}
    		p = 0;
    		N-= 1;
    	  } else {
    		p++;
    		N= N / 2;
    	  }
    	}
    	if (p > 0) {
    	  li.add(0, "SHL A,"+Integer.toString(p));
    	}
    }
    
    /**
	 * Procedure decodes multiplier into ASM code - OLD way
	 */
    private static void myOldProcedure(int N) {
    	p = 0;
    	int br = _2naN(N);
    	if (N == br) {
    		li.add(0, "SHL A,"+Integer.toString(p));
    	} else {
    		if (N - br <= 2) {
    			writeLnRepeatedly("ADD A,B", (N-br));
        		if (! add_sub) {
        			add_sub = true;
        		}
    			li.add(0, "SHL A,"+Integer.toString(p));
    		} else {
    			br = _2naNbigger(N);
    			if (br - N <= 2) { 
    				writeLnRepeatedly("SUB A,B", (br-N));
    	    		if (! add_sub) {
    	    			add_sub = true;
    	    		}
    				li.add(0, "SHL A,"+Integer.toString(p));
    			} else {
    				devideN_old(N);
    			}
    		}
    	}
    }
    
    /**
     * Old procedure to create ASM code
     * @param wr: File writer
     * @param N: Multiplier
     * @throws FileNotFoundException
     */
    private static void decodeProcedureOld(BufferedWriter wr, int N) throws FileNotFoundException {
    	try {
			li.clear();
			add_sub = false;
			myOldProcedure(N);
			
			li.add(0, "Number: " + Integer.toString(N));
			if (add_sub) {
				li.add(1, "MOV B,A");
			}
	        for(int i = 0; i < li.size(); i++) {
	        	wr.write(li.get(i));
	        	wr.newLine();
	        }
        	wr.newLine();
			wr.flush();
    	} catch (IOException e) {
			e.printStackTrace();
		}
    }
    
    /**
     * Adds ASM code to Lines variable.
     * It writes always SHL A,p0
     * If m > 0 then writes also ASS A,B/SUB A,B m times,
     * @param p0: power of 2
     * @param m: repeat cycles of ADD/SUB
     * @param doAdd: true -> use ADD A,B else use SUB A,B
     */
    private static void saveMyCode(int p0, int m, boolean doAdd) {
    	newLi.add("SHL A,"+Integer.toString(p0));
    	for(int i=0; i < m; i++) {
    		newLi.add(doAdd ? "ADD A,B" : "SUB A,B");
    	}
		if (! add_sub && m > 0) {
			add_sub = true;
		}
    }
    
    /**
     * Check if "n" is prime number.
     * @param n: number to check
     * @return true if "n" is prime number otherwise false
     */
    private static boolean IsPrime(int n)
    {
        if (n < 4)
            return true;
        if (n % 2 == 0)
            return false;

        int s = (int) Math.sqrt(n);
        for (int i = 3; i <= s; i+=2)
            if (n % i == 0)
                return false;
        return true;
    }

    /**
     * Creates ASM code for given NR
     * @param NR
     */
    private static void getASMLines(int NR) {
    	if (NR == 1) return;
    	int br = _2naNclosest(NR);
    	int x = Math.abs(br - NR);
    	if (x < 2) {
    		saveMyCode(p, x, (NR > br));
    	} else {
        	if (NR % 2 == 0) {
        		int rest = NR / 2;
        		getASMLines( rest );
        		d0++;
        	} else {
        		int rest;
        		boolean primeNr = IsPrime( NR );
        		boolean divBy2 = ((NR+1) % 2 == 0);
        		boolean divBy3 = (NR % 3 == 0);
        		boolean divBy5 = (NR % 5 == 0);
        		boolean divBy9 = (NR % 9 == 0);
        		if ( primeNr || divBy2 || divBy3 || divBy5 || divBy9) {
        			if ( (primeNr || divBy9) && !divBy3 && !divBy5)
        				rest = NR + 1;
        			else
        				rest = NR - 1;
            		getASMLines( rest );
        			if (d0 > 0) {
        				newLi.add("SHL A,"+Integer.toString(d0));
        		    	d0 = 0;
        			}
        			if ( primeNr || !divBy3 || !divBy5 || divBy9)
        				newLi.add("SUB A,B");
        			else
        				newLi.add("ADD A,B");
        			if (! add_sub) {
        				add_sub = true;
        			}
        		} else {
            		rest = NR / p;
            		getASMLines( rest );
        		}
        	}
    	}
    }
    
    private static int getBiggestPow(int NR) {
    	int n = 1;
    	while ((NR % Math.pow(2, n) == 0) && (NR / Math.pow(2, n) >= 1)) {
    		n++;
    	}
    	n--;
    	return n; 
    }

    /**
     * New procedure to assemble ASM code
     * p0 = getBiggestPow(NR)
     * p1 = getBiggestPow(NR+1)
     * p2 = getBiggestPow(NR-1)
     * 
     * Check which power of 2 is biggest and take this NR for recursion
     * 
     * This is done till NR falls under 16.
     * 
     * insert SHL A, biggest power
     * if p1 used then 
     *   SUB A,B
     * else if p2 used then
     *   ADD A,B
     *   
     * @param NR: Multiplier
     */
    private static void myNewProcedure(int NR) {
    	if (NR <= 16) {
    		getASMLines( NR );
    	} else {
    		int p0 = getBiggestPow(NR);
    		int p1 = getBiggestPow(NR+1);
    		int p2 = getBiggestPow(NR-1);
    		int pm;
    		int idxP;
    		if ((p0 >= p1) && (p0 >= p2))  {
    			pm = p0;
    			idxP = 0;
    		} else {
        		if ((p1 >= p0) && (p1 >= p2))  {
        			pm = p1;
        			NR++;
        			idxP = 1;
        		} else {
        			pm = p2;
        			NR--;
        			idxP = 2;
        		}
    		}
    		NR = (int) (NR / Math.pow(2, pm));
    		if (NR > 2)
    			myNewProcedure( NR );
    		switch (idxP) {
			case 1:
				saveMyCode(pm, 1, false);
				break;
			case 2:
				saveMyCode(pm, 1, true);
				break;
			default:
				saveMyCode(pm, 0, true);
				break;
			}
    	}
    }
    
    /**
     * New procedure to create ASM code
     * @param wr: File writer
     * @param N: Multiplier
     * @throws FileNotFoundException
     */
    private static void decodeProcedureNew(BufferedWriter wr, int N) throws FileNotFoundException {
    	try {
			newLi.clear();
			add_sub = false;
			p = 0;
			d0 = 0;
			myNewProcedure( N );
			if (d0 > 0)
		    	newLi.add("SHL A,"+Integer.toString(d0));
			newLi.add(0, "Number: " + Integer.toString(N));
			if (add_sub) {
				newLi.add(1, "MOV B,A");
			}
	        for(int i = 0; i < newLi.size(); i++) {
	        	wr.write(newLi.get(i));
	        	wr.newLine();
	        }
	        wr.newLine();
	        wr.flush();
    	} catch (IOException e) {
			e.printStackTrace();
		}
    }
    
    /**
	 * @param args - Number to be multiplied with
	 */
	public static void main(String[] args) throws FileNotFoundException {
    	try {
			BufferedReader reader = new BufferedReader(new FileReader("M_ulaz.dat"));
			BufferedWriter wrOld = new BufferedWriter(new FileWriter("M_izlaz.dat"));
			BufferedWriter wrNew = new BufferedWriter(new FileWriter("M_izlaz_new.dat"));
			String L;
			while ((L = reader.readLine()) != null) {
				int N = Integer.parseInt( L );
				decodeProcedureOld(wrOld, N );
				decodeProcedureNew(wrNew, N );
			}
			wrOld.close();
			wrNew.close();
			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}