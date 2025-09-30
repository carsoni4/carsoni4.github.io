    .text
    .globl main

main:
    # Initialize registers
    addi $t0, $zero, 10         
    addi $t1, $zero, 20         
    addi $t2, $zero, 5           

    # Arithmetic Instructions
    add $t3, $t0, $t1      
    addi $t4, $t0, 5 
    addiu $t4, $t0, 5       
    sub $t5, $t1, $t0       
    addu $t6, $t0, $t1      
    subu $t7, $t1, $t0      

    # Logical Instructions
    and $t8, $t0, $t1       
    andi $t9, $t0, 15       
    or $s0, $t0, $t1       
    ori $s1, $t0, 8        
    xor $s2, $t0, $t1      
    xori $s3, $t0, 4        
    nor $s4, $t0, $t1
    slt $s4, $t0, $t1
    slti $s4, $t0, 2

    #shifts
    sll  $s4, $t0, 2    
    srl  $s4, $t0, 2
    sra  $s4, $t0, 2

    halt                     

