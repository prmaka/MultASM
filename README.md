This is a simple algorithm witch build optimized ASM (assembler) code for multiplying two integer numbers. 
The goal is to build shortest ASM code to multiply two numbers.
Input is number N (multiplier) which is stored in M_ulaz.dat file. It is possible to enter more than one N number in file. Algorithm goes through each number and calculates best possible ASM code to multiply two integer numbers.

Allowed to use are next ASM commands:
MOV A, B 	A := B 
ADD A, B 	A := A + B 
SUB A, B 	A := A - B 
SHL A, k 	A := A * (2^k) 
A and B are processor registers and it is supposed that multiplicand is already in register A.

Usage:
In file M_ulaz.dat write multipliers (one row one multiplier). 

Output:
Two files M_izlaz.dat and M_izlaz_new.dat
M_izlaz gets output of procedure with no recursion and code is not always optimized.
M_izlaz_new is recursion procedure where code is always optimized.

Example:
M_ulaz.dat 	
5
100
135	
M_izlaz.dat	M_izlaz_new.dat
Number: 5	Number: 5
MOV B,A		MOV B,A
SHL A,2		SHL A,2
ADD A,B		ADD A,B

Number: 100	Number: 100
MOV B,A		MOV B,A
SHL A,1		SHL A,1
ADD A,B		ADD A,B
SHL A,3		SHL A,3
ADD A,B		ADD A,B
SHL A,2		SHL A,2

Number: 135	Number: 135
MOV B,A		MOV B,A
SHL A,5		SHL A,4
ADD A,B		ADD A,B
SHL A,1		SHL A,3
ADD A,B		SUB A,B
SHL A,1
ADD A,B
